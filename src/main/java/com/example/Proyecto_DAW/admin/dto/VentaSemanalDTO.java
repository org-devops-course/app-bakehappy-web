package com.example.Proyecto_DAW.admin.dto;

public class VentaSemanalDTO {
    private String dia;
    private int total;

    public VentaSemanalDTO() {
    }

    public VentaSemanalDTO(String dia, int total) {
        this.dia = dia;
        this.total = total;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
