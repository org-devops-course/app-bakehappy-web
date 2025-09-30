package com.example.Proyecto_DAW.admin.dto;

import java.util.List;

public class PedidoCrearRequestDTO {

    private Long idCliente;
    private List<DetallePedidoCrearDTO> detalles;
    private String direccionEnvio;

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public List<DetallePedidoCrearDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidoCrearDTO> detalles) {
        this.detalles = detalles;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
}
