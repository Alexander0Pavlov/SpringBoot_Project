package src.main.java.hello;

import src.main.java.hello.exceptions.BadRequestException;
import java.util.List;
import java.util.Date;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.EntityManager;

import javax.persistence.criteria.Order;


public interface TestDataServiceSearch<V> {


    PageReturnFormat<V> criteriaSearchTestData(Integer page, Integer limit, String[] searchParams,
                                               String[] sortByParams, String phone,
                                               List<String> fieldnamesAvaliable, Class<V> ClassV) throws BadRequestException;


    List<SortCriteria> mapSortByParamsToSortCriteriaList(List<String> fieldnamesAvaliable, String[] sortByParams)
            throws BadRequestException;

    List<SearchCriteria> parseSearchCriteriaListByFieldnamesAndSearchParams(List<String> fieldnamesAvaliable,
                                                                            List<String> operationsAvaliable,
                                                                            String[] searchParams) throws BadRequestException;

    Date formatStringToDateYYYY_MM_DD(String dateInString) throws BadRequestException;

    Predicate predicateBySearchCriteria(CriteriaBuilder builder, Root root, Predicate predicate,
                                        List<SearchCriteria> searchCriteriaList) throws BadRequestException;

    List<Order> orderListBySortCriteriaList(CriteriaBuilder builder, Root root, List<SortCriteria> sortCriteriaList);

    PageReturnFormat<V> pagingByQuery(EntityManager entityManager, CriteriaQuery<V> query,
                                      Integer page, Integer limit) throws BadRequestException;


    Predicate addSearchByPhoneToPredicateAndJoinTableWithPhone(CriteriaBuilder builder, Root root, Predicate predicate,
                                                               String phone);
}