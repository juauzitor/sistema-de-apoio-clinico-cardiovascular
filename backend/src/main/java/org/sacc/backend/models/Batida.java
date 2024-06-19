package org.sacc.backend.models;

public class Batida {
    private double value;
    private String status;

    public Batida(){
    }

    public Batida(double value, String status) {
        this.value = value;
        this.status = status;
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
}
