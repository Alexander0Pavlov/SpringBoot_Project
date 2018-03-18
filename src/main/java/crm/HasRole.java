package src.main.java.crm;


public interface HasRole {
    public static final String DBA = "hasRole('DBA')";
    public static final String URW = "hasRole('USER_READ_WRITE')";
    public static final String UR = "hasRole('USER_READ')";

    public static final String DBA_URW = "hasRole('DBA') or hasRole('USER_READ_WRITE')";
    public static final String DBA_URW_UR = "hasRole('DBA') or hasRole('USER_READ_WRITE') or hasRole('USER_READ')";

    public static final String[] ALL_ROLE_NAMES = {"ROLE_DBA", "ROLE_USER_READ_WRITE", "ROLE_USER_READ"};
    //без ROLE_ @PreAuthorize("hasRole('DBA')") итд не воспринимает акк. Префикс перед ролью не рекомендуется убирать, см.
    //https://docs.spring.io/spring-security/site/docs/4.0.4.RELEASE/apidocs/org/springframework/security/access/vote/RoleVoter.html

}