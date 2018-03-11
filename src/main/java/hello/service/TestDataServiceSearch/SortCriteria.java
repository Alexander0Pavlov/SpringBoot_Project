package src.main.java.hello;

public class SortCriteria {
    private String field;
    private String operation; // ASC, DSC


    public SortCriteria() {
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

}