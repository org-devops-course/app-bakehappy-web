package com.example.Proyecto_DAW.admin.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {

    private Long idPedido;
    private LocalDateTime fecha;
    private String direccionEnvio;
    private BigDecimal total;
    private List<DetallePedidoResponseDTO> detalles;
}
