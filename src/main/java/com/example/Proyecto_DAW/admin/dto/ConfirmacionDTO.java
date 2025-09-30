package com.example.Proyecto_DAW.admin.dto;

import java.math.BigDecimal;
import java.util.List;

public class ConfirmacionDTO {
    private Long numeroPedido;
    private List<ItemFacturaDTO> resumenCompra;
    private BigDecimal total;
    private String mensaje;

    public ConfirmacionDTO() {
    }

    public ConfirmacionDTO(Long numeroPedido, List<ItemFacturaDTO> resumenCompra, BigDecimal total, String mensaje) {
        this.numeroPedido = numeroPedido;
        this.resumenCompra = resumenCompra;
        this.total = total;
        this.mensaje = mensaje;
    }

    public Long getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(Long numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public List<ItemFacturaDTO> getResumenCompra() {
        return resumenCompra;
    }

    public void setResumenCompra(List<ItemFacturaDTO> resumenCompra) {
        this.resumenCompra = resumenCompra;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
