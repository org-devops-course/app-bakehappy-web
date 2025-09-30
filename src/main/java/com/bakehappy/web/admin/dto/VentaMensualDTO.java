package com.bakehappy.web.admin.dto;

public class VentaMensualDTO {
    private String mes;
    private int total;

    public VentaMensualDTO() {
    }

    public VentaMensualDTO(String mes, int total) {
        this.mes = mes;
        this.total = total;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
