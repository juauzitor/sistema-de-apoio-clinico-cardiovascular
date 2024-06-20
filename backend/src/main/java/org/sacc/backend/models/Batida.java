package org.sacc.backend.models;

public class Batida {
    private double value;
    private String status;
    private String expected;

    public Batida(){
    }

    public Batida(double value, String status, String expected) {
        this.value = value;
        this.status = status;
        this.expected = expected;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }
}


