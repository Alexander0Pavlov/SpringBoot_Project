package src.main.java.hello;

public class SearchCriteria {
    private String field;
    private String operation; // "=" если значение field value должно быть равно значению в базе, "~" если like.
    private String value;

    public SearchCriteria() {
    }


    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }


    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}