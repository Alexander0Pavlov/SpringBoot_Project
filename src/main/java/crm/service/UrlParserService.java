package src.main.java.crm;

import java.net.MalformedURLException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.security.access.prepost.PreAuthorize;


@PreAuthorize(HasRole.DBA_URW)
public interface UrlParserService {

    public void startParse(String searchString,
                           Integer pageNumberStartFrom,
                           Integer pageNumberMax,
                           Integer recordsPerPage,
                           Integer maxParallelThreads)
            throws MalformedURLException, IOException, ParserConfigurationException, XPathExpressionException;
}