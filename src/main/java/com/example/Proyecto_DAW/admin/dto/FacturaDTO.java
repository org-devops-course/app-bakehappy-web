package com.example.Proyecto_DAW.admin.dto;

import java.math.BigDecimal;
import java.util.List;

public class FacturaDTO {
    private List<ItemFacturaDTO> items;
    private BigDecimal subtotal;
    private BigDecimal envio;
    private BigDecimal impuestos;
    private BigDecimal total;

    public FacturaDTO() {
    }

    public FacturaDTO(List<ItemFacturaDTO> items, BigDecimal subtotal, BigDecimal envio, BigDecimal impuestos, BigDecimal total) {
        this.items = items;
        this.subtotal = subtotal;
        this.envio = envio;
        this.impuestos = impuestos;
        this.total = total;
    }

    public List<ItemFacturaDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemFacturaDTO> items) {
        this.items = items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getEnvio() {
        return envio;
    }

    public void setEnvio(BigDecimal envio) {
        this.envio = envio;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
