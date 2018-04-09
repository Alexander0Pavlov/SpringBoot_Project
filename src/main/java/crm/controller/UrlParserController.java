package src.main.java.crm;



import java.net.MalformedURLException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class UrlParserController {


    @Autowired
    private UrlParserService urlParserService;


    @RequestMapping(value = "/urlparser/parse", method = RequestMethod.GET,
            consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public String parserMain(
            @RequestParam(value = "searchString", required = false, defaultValue = "") String searchString,
            @RequestParam(value = "pageNumberStartFrom", required = false, defaultValue = "1") Integer pageNumberStartFrom,
            @RequestParam(value = "pageNumberMax", required = false, defaultValue = "1") Integer pageNumberMax,
            @RequestParam(value = "recordsPerPage", required = false, defaultValue = "10") Integer recordsPerPage,
            @RequestParam(value = "maxParallelThreads", required = false, defaultValue = "10") Integer maxParallelThreads)
            throws MalformedURLException, IOException, ParserConfigurationException, XPathExpressionException {

        urlParserService.startParse(searchString, pageNumberStartFrom, pageNumberMax, recordsPerPage, maxParallelThreads);

        return "Parsing finished.";
    }



}