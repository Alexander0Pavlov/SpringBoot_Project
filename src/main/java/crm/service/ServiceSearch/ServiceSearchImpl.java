package src.main.java.crm;

import src.main.java.crm.exceptions.BadRequestException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;

import javax.persistence.criteria.Order;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Join;

//^^6 форматтер даты
import java.lang.Exception;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
//^^6

//Map, HashMap, List, Date
//  .and(Predicate<? super T> other)
// Returns a composed predicate that represents a short-circuiting logical AND of this predicate and another.
import java.util.*;
import com.google.common.collect.Lists;



@Service("serviceSearch")
public class ServiceSearchImpl<V> implements ServiceSearch<V> {


    @PersistenceContext
    private EntityManager entityManager;


////////////^SEARCH собственно сам поиск с сортировкой
    @Override
    @Transactional(readOnly = true)
    public PageReturnFormat<V> criteriaSearch(Integer page, Integer limit,
                                                      String[] searchParams, String[] sortByParams, String clientsPhone,
                                                      List<String> fieldnamesAvaliable, Class<V> ClassV) throws BadRequestException {

        if (page == null || limit == null || !(page > 0 && limit > 0) ||
                (searchParams.length % 3 != 0) || (sortByParams.length % 2 != 0))
            throw new BadRequestException("BAD_REQUEST");

        List<String> operationsAvaliable = Lists.newArrayList("=", "~");

        // раскидываем параметры поиска searchParams по полям класса SearchCriteria и формируем лист SearchCriteria.
        List<SearchCriteria> searchCriteriaList = parseSearchCriteriaListByFieldnamesAndSearchParams(fieldnamesAvaliable,
                operationsAvaliable, searchParams);

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<V> query = builder.createQuery((Class<V>) ClassV);
        Root root = query.from((Class<V>) ClassV);
        Predicate predicate = builder.conjunction();

        //добавляем поиск по полю phone из таблицы ClientsInfo
        if (!(clientsPhone == null || clientsPhone.equals(""))) {
            predicate = addSearchByPhoneToPredicateAndJoinTableWithPhone(builder, root, predicate, clientsPhone);
        }
        // формируем из параметров поиска SearchCriteria - параметр запроса WHERE predicate
        predicate = predicateBySearchCriteria(builder, root, predicate, searchCriteriaList);
        query.where(predicate);

        // мэппим sortByParams на SortCriteriaList
        List<SortCriteria> sortCriteriaList = mapSortByParamsToSortCriteriaList(fieldnamesAvaliable, sortByParams);

        // Формируем из SortCriteriaList - List<Order>, (javax.persistence.criteria.Order - в нём зашито поле-тип сортировки)
        List<Order> orderList = orderListBySortCriteriaList(builder, root, sortCriteriaList);
        query.orderBy(orderList);

        //дистинкт чтобы не дублировалися несколько раз контакт с несколькими данными из сджойненой таблицы, подходящими под маску поиска.
        query.distinct(true);

        //^PAGING Разбивка на страницы
        PageReturnFormat<V> PageReturnFormat = pagingByQuery(entityManager, query, page, limit);

        return PageReturnFormat;
    }
////////////^SEARCH


/////////////^^PARSE_SORT_CRITERIA парсим массив стрингов, что приняли по REST на лист SortCriteria(класс Поле - параметр сортировки)

    @Transactional(readOnly = true)
    private List<SortCriteria> mapSortByParamsToSortCriteriaList(List<String> fieldnamesAvaliable, String[] sortByParams)
            throws BadRequestException {

        if (sortByParams.length % 2 != 0) throw new BadRequestException("BAD_REQUEST");

        List<SortCriteria> sortCriteriaList = Lists.newArrayList();
        SortCriteria sortCriteria = new SortCriteria();

        int j = 0;
        boolean paramSetted = false;
        //sortByParams массив вида id,asc,name,dsc
        for (String sortByParam : sortByParams) {
            j++;
            if (j == 1) {  //if FIRST_PARAMETER  (FIELDNAME_PARAMETER)
                for (String fieldname : fieldnamesAvaliable)
                    if (sortByParam.compareToIgnoreCase(fieldname) == 0) {
                        sortCriteria = new SortCriteria();
                        sortCriteria.setField(fieldname); //установили первый параметр
                        paramSetted = true;
                        break;
                    }
            } else { // if SECOND_PARAMETER (ASC_DSC_PARAMETER)
                if (sortByParam.compareToIgnoreCase("asc") == 0) {
                    sortCriteria.setOperation("asc");
                    paramSetted = true;
                    sortCriteriaList.add(sortCriteria);
                } else if (sortByParam.compareToIgnoreCase("dsc") == 0) {
                    sortCriteria.setOperation("dsc"); //установили второй параметр
                    paramSetted = true;
                    sortCriteriaList.add(sortCriteria); //добавили в List
                }
                j = 0;
            }

            if (paramSetted)
                paramSetted = false;//сброс флага
            else throw new BadRequestException("BAD_REQUEST");
        }
        return sortCriteriaList;
    }
/////////////^^PARSE_SORT_CRITERIA

/////////////^^PARSE_SEARCH_CRITERIA парсим массив стрингов, что приняли по REST
    // на лист SearchCriteria(формат: ИмяПоля - Операция(=, like) - ЗначениеПоКоторомуИщем)

    @Transactional(readOnly = true)
    private List<SearchCriteria> parseSearchCriteriaListByFieldnamesAndSearchParams(List<String> fieldnamesAvaliable,
                                                                                   List<String> operationsAvaliable,
                                                                                   String[] searchParams)
            throws BadRequestException {
        if (searchParams.length % 3 != 0) throw new BadRequestException("BAD_REQUEST");

        List<SearchCriteria> searchCriteriaList = Lists.newArrayList();


        SearchCriteria searchCriteria = new SearchCriteria();
        int i = 0;
        boolean paramSetted = false;
        //searchParams массив вида company,=,ИмяКомпании,name,~,Алексан
        for (String param : searchParams) {
            i++;
            if (i == 1) { //if FIRST_PARAMETER  (FIELDNAME_PARAMETER)
                for (String fieldname : fieldnamesAvaliable)
                    if (param.compareToIgnoreCase(fieldname) == 0) {
                        searchCriteria = new SearchCriteria();
                        searchCriteria.setField(fieldname);
                        paramSetted = true;
                        break;
                    }

            } else if (i == 2) { //if SECOND_PARAMETER  (OPERATION_PARAMETER)
                for (String operation : operationsAvaliable)
                    if (param.compareToIgnoreCase(operation) == 0) {
                        searchCriteria.setOperation(operation);
                        paramSetted = true;
                        break;
                    }
            } else { //if THIRD_PARAMETER  (VALUE_PARAMETER)
                searchCriteria.setValue(param);
                paramSetted = true;
                searchCriteriaList.add(searchCriteria);
                i = 0;
            }

            if (paramSetted) {
                paramSetted = false; //сброс флага
            } else throw new BadRequestException("BAD_REQUEST");
        }
        return searchCriteriaList;
    }
/////////////^^PARSE_SEARCH_CRITERIA

/////////////^FORMAT DATE

    private Date formatStringToDateYYYY_MM_DD(String dateInString) throws BadRequestException {
        DateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date();
        try {
            date = format.parse(dateInString);
        } catch (Exception exc) {
            throw new BadRequestException("BAD_REQUEST");
        }
        return date;
    }
/////////////^FORMAT DATE


/////////^WHERE(predicate) формируем из параметров поиска - параметр запроса WHERE predicate (дополняем predicate)
    private Predicate predicateBySearchCriteria(CriteriaBuilder builder, Root root, Predicate predicate,
                                               List<SearchCriteria> searchCriteriaList) throws BadRequestException {

        for (SearchCriteria searchCriteria : searchCriteriaList) {
            if (searchCriteria.getOperation().equals("=")) { //если EQUALS


                if (root.get(searchCriteria.getField()).getJavaType() == Date.class) {
                    //Если ищем по дате - берём промежуток от даты до (даты + 1 день)
                    Date minDate = formatStringToDateYYYY_MM_DD(searchCriteria.getValue());
                    Date maxDate = new Date(minDate.getTime() + TimeUnit.DAYS.toMillis(1));
                    predicate = builder.and(predicate, builder.between(root.get(searchCriteria.getField()), minDate, maxDate));
                }

                else //если не по дате - просто сравниваем
                    predicate = builder.and(predicate, builder.equal(root.get(searchCriteria.getField()), searchCriteria.getValue()));



            } else if (searchCriteria.getOperation().equals("~")) { //если LIKE

                if (searchCriteria.getValue() != null &&
                        (root.get(searchCriteria.getField()).getJavaType() == String.class ||
                                root.get(searchCriteria.getField()).getJavaType() == Long.class)) {

                    predicate = builder.and(predicate,
                            builder.like(builder.lower(root.get(searchCriteria.getField()).as(String.class)),
                                    ("%" + searchCriteria.getValue() + "%").toLowerCase()));

                } else throw new BadRequestException("BAD_REQUEST");

            } else throw new BadRequestException("BAD_REQUEST"); //если не EQUALS и не LIKE
        }
        return predicate;
    }
/////////^WHERE(predicate)


////////^ORDER_LIST Формируем orderList, что передаётся в запрос query.orderBy (javax.persistence.criteria.Order - в нём зашито поле-тип сортировки)
    private List<Order> orderListBySortCriteriaList(CriteriaBuilder builder, Root root, List<SortCriteria> sortCriteriaList) {

        List<Order> orderList = Lists.newArrayList();

        for (SortCriteria sortCriteria_ : sortCriteriaList) {
            if (sortCriteria_.getOperation().compareToIgnoreCase("dsc") == 0) {
                orderList.add( builder.desc(root.get(sortCriteria_.getField())) );
            } else {
                orderList.add( builder.asc(root.get(sortCriteria_.getField())) );
            }
        }
        return orderList;
    }
////////^ORDER_LIST


/////////^PAGING ~~~~~~~~~~ Разбивка на страницы  PageReturnFormat
    private PageReturnFormat<V> pagingByQuery(EntityManager entityManager, CriteriaQuery<V> query,
                                             Integer page, Integer limit) throws BadRequestException {

        TypedQuery<V> queryToCountResults = entityManager.createQuery(query);
        long totalRecords = queryToCountResults.getResultList().size();


        long totalPages = 0;
        if (limit > 0)
            totalPages = (long) Math.ceil(((double) totalRecords) / limit);
        else throw new BadRequestException("BAD_REQUEST");


        Integer startIndex = limit * (page - 1);


        //запрос выборки результатов от startIndex , limit шт.
        List<V> dataList = Lists.newArrayList(
                entityManager.createQuery(query).setFirstResult(startIndex).setMaxResults(limit).getResultList());


        PageReturnFormat<V> PageReturnFormat = new PageReturnFormat<V>();

        PageReturnFormat.setCurrentPage(page);
        PageReturnFormat.setTotalPages(totalPages);
        PageReturnFormat.setTotalRecords(totalRecords);
        PageReturnFormat.setDataList(dataList);

        return PageReturnFormat;
    }
/////////^PAGING ~~~~~~~~~~


//^ADD SEARCH BY PHONE
    private Predicate addSearchByPhoneToPredicateAndJoinTableWithPhone(CriteriaBuilder builder, Root root, Predicate predicate,
                                                                      String clientsPhone) {
        Join<Clients, ClientsInfo> joinClientsInfo = null;


        if (!(clientsPhone == null || clientsPhone.equals(""))) {
            joinClientsInfo = root.join("clientsInfoSet", JoinType.LEFT);
            predicate = builder.and(predicate,
                    builder.like(builder.lower(joinClientsInfo.get("phone").as(String.class)),
                            ("%" + clientsPhone + "%").toLowerCase()));
        }
        return predicate;
    }
//^SEARCH BY PHONE


}