package src.main.java.crm;

import src.main.java.crm.exceptions.BadRequestException;
import java.util.List;
import java.util.Date;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.EntityManager;

import javax.persistence.criteria.Order;

import org.springframework.security.access.prepost.PreAuthorize;


@PreAuthorize(HasRole.DBA_URW_UR)
public interface ServiceSearch<V> {


    PageReturnFormat<V> criteriaSearch(Integer page, Integer limit, String[] searchParams,
                                               String[] sortByParams, String clientsPhone,
                                               List<String> fieldnamesAvaliable, Class<V> ClassV) throws BadRequestException;


}