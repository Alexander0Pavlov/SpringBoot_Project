package src.main.java.crm;

import src.main.java.crm.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import javax.xml.parsers.ParserConfigurationException;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.DomSerializer;

import java.net.URL;
import java.net.MalformedURLException;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.io.InputStream;
import java.net.URLConnection;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

import java.util.concurrent.Semaphore;



/**

    Добавил парсинг контактов с zakupki.gov.ru.
    Парсим бесплатные прокси с сайта в пул,
    проходим по страницам zakupki.gov соответствующим запросу и парсим с них ссылки на конкурсы
    через пул прокси коннектимся по ссылкам на конкурсы и выкачиваем оттуда контакты в указанное число потоков.
    За всем этим следит монитор, который в случае если в течении 30 секунд ни 1 ссылки не распаршено, - завершает парсинг,
    + выводятся данные где остановились и какие ссылки не обработали. (на случай, к примеру, проблем со связью, сайтом, прокси итд).

*/



@Service("urlParserService")
public class UrlParserServiceImpl implements UrlParserService {

    private static final Integer PARSER_TYPE_1 = Integer.valueOf(1);
    private static final Integer PARSER_TYPE_2 = Integer.valueOf(2);

    private volatile Integer notFinishedLinks = Integer.valueOf(0);
    private volatile boolean notFinishedLinksChanged = true;
    private volatile Thread notFinishedLinksMonitorThread;
    private volatile Thread concurrentParseOfPagesThread;

    private boolean printLog = true;


    public void startParse(String searchString,
                           Integer pageNumberStartFrom, Integer pageNumberMax, Integer recordsPerPage,
                           Integer maxParallelThreads)
            throws MalformedURLException, IOException, ParserConfigurationException, XPathExpressionException {

        if (searchString == null || pageNumberStartFrom == null || pageNumberMax == null || recordsPerPage == null ||
                maxParallelThreads == null ||
                recordsPerPage > 500 || pageNumberStartFrom < 1 || pageNumberMax < pageNumberStartFrom || maxParallelThreads < 1) {
            throw new BadRequestException("BAD_REQUEST");
            // сайт возвращает максимум 500 записей на страницу, 501 - уже нет.
        }

        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Parse thread pool-%d")
                .setDaemon(false).build();
        final ExecutorService executorService = Executors.newFixedThreadPool(maxParallelThreads + 1, threadFactory);

        ConcurrentLinkedDeque<Proxy> proxyQueue = new ConcurrentLinkedDeque();
        proxyQueue = addProxyToQueue(proxyQueue);
        proxyQueue.addFirst(Proxy.NO_PROXY);

        // метод отдаёт набор ссылок с предыдущей страницы, их же передаём как параметр при следующем вызове и в методе убираем дубли.
        Map<String, Integer> mapLinkParserTypeOfPreviousPage = new TreeMap();
        Map<String, Integer> mapLinkParserType;

        String strurl;

        try {
            Integer pageNumber = pageNumberStartFrom;
            do {
                strurl = makeUrlRequestByParams(searchString, pageNumber, recordsPerPage);
                System.out.println("START TO PARSE PAGE № " + pageNumber + "URL: " + strurl);

                mapLinkParserType = mainPageParse(strurl, mapLinkParserTypeOfPreviousPage, executorService, proxyQueue, maxParallelThreads);
                mapLinkParserTypeOfPreviousPage = mapLinkParserType; //можно и в 1 переменную, но так понятнее

                System.out.println("Finished parsing of page = " + pageNumber + " of " + pageNumberMax);
                System.out.println("Number of unique links at the page = " + mapLinkParserType.size());
                pageNumber++;
            } while (!mapLinkParserType.isEmpty() && pageNumber <= pageNumberMax);

            System.out.println("Parsing finished.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
        }
    }


    private String makeUrlRequestByParams(String searchStringValue, Integer pageNumberValue, Integer recordsPerPageValue)
            throws UnsupportedEncodingException {
        if (pageNumberValue < 1 || recordsPerPageValue < 1 ||
                searchStringValue == null || pageNumberValue == null || recordsPerPageValue == null) {
            throw new BadRequestException("BAD_REQUEST");
        }

        String prefix = "http://zakupki.gov.ru/epz/order/extendedsearch/results.html?";
        String searchString = "searchString=" + URLEncoder.encode(searchStringValue, "utf-8");
        String somedefparams = "&morphology=on&openMode=USE_DEFAULT_PARAMS";
        String pageNumber = "&pageNumber=" + pageNumberValue;
        String somedefparams2 = "&sortDirection=false";
        String recordsPerPage = "&recordsPerPage=_" + recordsPerPageValue;
        String somedefparams3 = "&showLotsInfoHidden=false&fz44=on&fz223=on";
        String sortBy = "&sortBy=UPDATE_DATE";

        String stringUrlRequest = prefix + searchString +
                somedefparams + pageNumber +
                somedefparams2 + recordsPerPage +
                somedefparams3 + sortBy;

        return stringUrlRequest;
    }

    // возвращаем весь список ссылок, чтобы в следующем проходе откинуть дубликаты.
    private Map<String, Integer> mainPageParse(
            String strurl, Map<String, Integer> mapLinkParserTypeOfPreviousPage,
            ExecutorService executorService, ConcurrentLinkedDeque<Proxy> proxyQueue, Integer maxParallelThreads)
            throws MalformedURLException, IOException, ParserConfigurationException, XPathExpressionException {

        Map<String, Integer> mapLinkParserType = getLinksFromMainPage(strurl, Proxy.NO_PROXY);

        //убираем ссылки, что уже распарсили на предыдущей странице
        mapLinkParserType.keySet().removeAll(mapLinkParserTypeOfPreviousPage.keySet());
        log("Deleted duplicates from previous page");
        ConcurrentLinkedQueue<String> linkQueue = new ConcurrentLinkedQueue(mapLinkParserType.keySet());

        linkQueue = concurrentParseOfPages(executorService, linkQueue, mapLinkParserType, proxyQueue, maxParallelThreads);
        //если что-то вдруг не распарсилось
        // - попробуем распарсить ещё раз, но если нет, - запишем нераспаршенные ссылки
        if (!linkQueue.isEmpty()) {
            linkQueue = concurrentParseOfPages(executorService, linkQueue, mapLinkParserType, proxyQueue, maxParallelThreads);
        }
        log("Last URL: " + strurl);
        log("Links that were not parsed : " + linkQueue.size());
        linkQueue.forEach(System.out::println);

        if (linkQueue.size() == mapLinkParserType.size()) {
            throw new IOException("No links from page was parsed. Check connection, list of proxy, xPath expression");
        }

        return mapLinkParserType;
    }


    private Map<String, Integer> getLinksFromMainPage(String strurl, Proxy proxy)
            throws MalformedURLException, IOException, ParserConfigurationException, XPathExpressionException {

        Document documentMainPage = getDocumentByUrl(strurl, proxy);

        String xPathExpressionForMainPageToGetLinks = "//a[contains(@class, 'printLink')]/@href";
        NodeList nodeList = nodeListByDocumentAndExpression(documentMainPage, xPathExpressionForMainPageToGetLinks);

        Map<String, Integer> mapLinkParserType = nodeToMapLinkParserType(nodeList);

        mapLinkParserType.forEach((k, v) -> log(k + " = " + v));

        log("==============================================");
        return mapLinkParserType;
    }


    private ConcurrentLinkedDeque<Proxy> addProxyToQueue(ConcurrentLinkedDeque<Proxy> proxyQueue)
            throws MalformedURLException, IOException, ParserConfigurationException, XPathExpressionException {
        Map<String, Integer> mapOfProxyIpPort = parseProxyList();
        mapOfProxyIpPort.forEach((ip, port) -> {
            SocketAddress addr = new InetSocketAddress(ip, port);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
            proxyQueue.add(proxy);
        });
        return proxyQueue;
    }


    private ConcurrentLinkedQueue<String> concurrentParseOfPages(
            ExecutorService executorService,
            ConcurrentLinkedQueue<String> linkQueue,
            Map<String, Integer> mapLinkParserType,
            ConcurrentLinkedDeque<Proxy> proxyQueue, Integer maxParallelThreads) {

        int semaphoreStartSize = Math.min(Math.min(linkQueue.size(), proxyQueue.size()), maxParallelThreads);

        Semaphore semaphore = new Semaphore(semaphoreStartSize);

        log("Created Semaphore, size = " + semaphoreStartSize +
                " linkQueue.size(), proxyQueue.size(), maxParallelThreads = " +
                linkQueue.size() + ", " + proxyQueue.size() + ", " + maxParallelThreads);
        notFinishedLinks = linkQueue.size();
        notFinishedLinksMonitor(executorService);
        concurrentParseOfPagesThread = Thread.currentThread();
        try {
            while (notFinishedLinks > 0 && notFinishedLinksChanged) {

                try {
                    log("Waiting for permit, semaphore.availablePermits() = " + semaphore.availablePermits());
                    semaphore.acquire();
                    log("Taken permit, semaphore.availablePermits() = " + semaphore.availablePermits());
                    if (!linkQueue.isEmpty() && !proxyQueue.isEmpty()) {
                        String link = linkQueue.poll();

                        processParseContactByLink(executorService, semaphore,
                                linkQueue, link, mapLinkParserType.get(link),
                                proxyQueue);
                        // запустил поток и пошёл дальше.
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                log("linkQueue.size() = " + linkQueue.size() + " proxyQueue.size() = " + proxyQueue.size());
            }
        } finally {
            if (!(notFinishedLinksMonitorThread == null))
                notFinishedLinksMonitorThread.interrupt();
            return linkQueue;
        }
    }


    private Map<String, Integer> parseProxyList()
            throws MalformedURLException, IOException, ParserConfigurationException, XPathExpressionException {

        String link = "https://www.my-proxy.com/free-proxy-list-2.html";
        String xPathExpression = "//div[@class='list' or @class='to-lock']/text()[position() < 44]";

        Document documentToParseContact = getDocumentByUrl(link, Proxy.NO_PROXY);

        NodeList nodeListOfProxy = nodeListByDocumentAndExpression(documentToParseContact, xPathExpression);

        Map<String, Integer> mapOfProxyIpPort = new TreeMap();

        for (int i = 0; i < nodeListOfProxy.getLength(); i++) {
            String ipPort = nodeListOfProxy.item(i).getNodeValue();

            String separators = "[:#]+";
            String[] ipPortSeparated = ipPort.split(separators);
            String ip = ipPortSeparated[0];
            Integer port = Integer.valueOf(ipPortSeparated[1]);

            log(ip + " : " + port);
            mapOfProxyIpPort.put(ip, port);
        }
        return mapOfProxyIpPort;
    }


    private void notFinishedLinksMonitor(ExecutorService executorService) {
        executorService.execute(new Runnable() {
            public void run() {
                try {
                    notFinishedLinksMonitorThread = Thread.currentThread();
                    log("notFinishedLinksMonitor Started.");
                    int notFinishedLinksOld;

                    while (notFinishedLinks > 0) {
                        notFinishedLinksOld = notFinishedLinks;
                        Thread.sleep(30_000);
                        if (notFinishedLinks.equals(notFinishedLinksOld)) {
                            notFinishedLinksChanged = false;
                            if (!(concurrentParseOfPagesThread == null))
                                concurrentParseOfPagesThread.interrupt();
                            break;
                        } else notFinishedLinksChanged = true;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    log("notFinishedLinksMonitor Stopped.");
                }
            }
        });
    }


    private void processParseContactByLink(ExecutorService executorService,
                                           Semaphore semaphore,
                                           ConcurrentLinkedQueue<String> linkQueue,
                                           String link,
                                           Integer parserType,
                                           ConcurrentLinkedDeque<Proxy> proxyQueue) {

        executorService.execute(new Runnable() {
            public void run() {
                final Thread currentThread = Thread.currentThread();
                final String oldName = currentThread.getName();
                String proxyAddress = "";

                try {
                    Proxy proxy = proxyQueue.poll();

                    if (proxy == null)
                        throw new InternalServerErrorException("proxy == null");
                    if (proxy == Proxy.NO_PROXY) {
                        proxyAddress = "WITHOUT PROXY";
                    } else {
                        proxyAddress = proxy.address().toString();
                    }
                    currentThread.setName("Thread parsing " + link + " by proxy " + proxyAddress);


                    //сам парсинг
                    log("TRY parsing " + link + " by proxy " + proxyAddress);
                    Thread.sleep(1000); // пауза на 1 сек, чтобы не ддосить с одного IP и не получить блокировку
                    parseContactByLink(link, parserType, proxy);
                    notFinishedLinks--;
                    proxyQueue.addFirst(proxy); // не в finally, т.к. если коннект с этого прокси выдал ошибку, то возвращать его в пул не надо
                    log("returned proxy to queue: " + proxyAddress + " proxy queue size: " + proxyQueue.size());
                    semaphore.release();
                    log("semaphore.release();");
                } catch (Exception e) {

                    log("Exception in a thread parsing: " + link + " by proxy " + proxyAddress);
                    log(e.getMessage());
                    linkQueue.add(link);

                    if (!proxyQueue.isEmpty() &&
                            !linkQueue.isEmpty()) {
                        semaphore.release(); //нет смысла запускать ещё 1 поток если текущие уже забрали все прокси или ссылки из очереди
                        log("semaphore.release();");
                    }

                } finally {
                    currentThread.setName(oldName);
                }
            }
        });
    }


    private void parseContactByLink(String link, Integer parserType, Proxy proxy)
            throws MalformedURLException, IOException, ParserConfigurationException, XPathExpressionException {

        String expressionParseType1 = "//td[@class='row' " +
                "and (text()='Номер извещения:' " +
                "or text()='Наименование закупки:'" +
                "or text()='Ф.И.О:'" +
                "or text()='Адрес электронной почты:'" +
                "or text()='Телефон:')]" +
                "/following-sibling::td[last()]/text()";
        String expressionParseType2 = "//p[@class='parameter' " +
                "and (text()='Номер извещения' " +
                "or text()='Наименование объекта закупки'" +
                "or text()='Ответственное должностное лицо'" +
                "or text()='Адрес электронной почты'" +
                "or text()='Номер контактного телефона')]" +
                "/parent::td" +
                "/following-sibling::td[last()]/p/text()";

        Document documentToParseContact = getDocumentByUrl(link, proxy);

        NodeList nodeListParsedContact;

        if (parserType.equals(PARSER_TYPE_1)) {
            nodeListParsedContact = nodeListByDocumentAndExpression(documentToParseContact, expressionParseType1);
        } else { //parserType.equals(PARSER_TYPE_2)
            nodeListParsedContact = nodeListByDocumentAndExpression(documentToParseContact, expressionParseType2);
        }
        System.out.println("Number of fields parsed = " + nodeListParsedContact.getLength());
        if (!(nodeListParsedContact.getLength() > 0)) {
            throw new ParserConfigurationException("Parsed no fields from the page, " +
                    "so somebody changed a code of the page or this proxy is not good. " +
                    "Deleting proxy from a pool and adding link back to a Queue."); // "by throwing this Exception"
        }
        for (int i = 0; i < nodeListParsedContact.getLength(); i++) {
            String str = nodeListParsedContact.item(i).getNodeValue();
            System.out.println(str); //Выводим что распарсили
        }

        System.out.println("------------------------------------");
    }


    private Document getDocumentByUrl(String strurl, Proxy proxy)
            throws MalformedURLException, IOException, ParserConfigurationException {

        URL url = new URL(strurl);

        URLConnection connection = url.openConnection(proxy);
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(6000);
        //притворяемся браузером чтобы не получать 403
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.connect();

        InputStream inputStream = connection.getInputStream(); //обработать что нет коннекшена и др. прокси в методе выше.


        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode tagNode = cleaner.clean(inputStream);
        inputStream.close(); //не забываем

        Document document = new DomSerializer(cleaner.getProperties(), true).createDOM(tagNode);

        return document;
    }


    private NodeList nodeListByDocumentAndExpression(Document document, String expression) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        //XPath компилирует XPath-выражение:
        XPathExpression expr = xpath.compile(expression);
        //выполнение запроса для получения результата:
        Object result = expr.evaluate(document, XPathConstants.NODESET);
        //сохранить результат в NodeList
        NodeList nodeList = (NodeList) result;
        return nodeList;
    }


    private Map<String, Integer> nodeToMapLinkParserType(NodeList nodeList) {

        log("Number of links finded at the page = " + nodeList.getLength());
        Map<String, Integer> mapLinkParserType = new TreeMap();

        for (int i = 0; i < nodeList.getLength(); i++) {
            String nodeValue = nodeList.item(i).getNodeValue();

            /* формат получаемых nodeValue:
            a) http://zakupki.gov.ru/223/purchase/public/notification/print-form/show.html?noticeId=6157070
            (PARSER_TYPE_1)

            б) /epz/order/notice/printForm/view.html?regNumber=0322100004518000026
            (PARSER_TYPE_2)

            +по разным ссылкам разные параметры-форматы страниц => помечаем, парсер какого типа страницы использовать в дальнейшем
            */
            if (!nodeValue.isEmpty()) {
                if (nodeValue.contains("http://zakupki.gov.ru")) {
                    mapLinkParserType.put(nodeValue, PARSER_TYPE_1);
                } else {
                    nodeValue = "http://zakupki.gov.ru" + nodeValue;
                    mapLinkParserType.put(nodeValue, PARSER_TYPE_2);
                }
            }
        }
        return mapLinkParserType;
    }


    private void log(String str) {
        if (printLog)
            System.out.println(str);
    }

}