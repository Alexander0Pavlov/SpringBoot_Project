package src.main.java.hello;

import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ErrorMessage {
    public final Timestamp timestamp;
    @JsonIgnore
    public final String exClassName;
    @JsonIgnore
    public final String exRootCauseClassName;
    @JsonIgnore
    public final String exRootCauseMethodName;
    @JsonIgnore
    public final String url;
    public final Integer status;
    public final String message;


    public ErrorMessage(Timestamp timestamp, String exClassName, String exRootCauseClassName,
                        String exRootCauseMethodName, String url, Integer status, String message) {
        this.timestamp = timestamp;
        this.exClassName = exClassName;
        this.exRootCauseClassName = exRootCauseClassName;
        this.exRootCauseMethodName = exRootCauseMethodName;
        this.url = url;
        this.status = status;
        this.message = message;
    }


    @Override
    public String toString() {
        return (timestamp + " --- " + exClassName + " --- " + exRootCauseClassName + " - " + exRootCauseMethodName +
                " --- " + "url: " + url + " --- " + message);
    }

}