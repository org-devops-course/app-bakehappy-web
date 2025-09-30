package com.example.Proyecto_DAW.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Future(message = "La fecha de entrega debe ser en el futuro")
    private LocalDate fechaEntrega;

    private String notasEspeciales;

    public Reserva() {
    }

    public Reserva(int idReserva, Cliente cliente, Producto producto, LocalDate fechaEntrega, String notasEspeciales) {
        this.cliente = cliente;
        this.producto = producto;
        this.fechaEntrega = fechaEntrega;
        this.notasEspeciales = notasEspeciales;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getNotasEspeciales() {
        return notasEspeciales;
    }

    public void setNotasEspeciales(String notasEspeciales) {
        this.notasEspeciales = notasEspeciales;
    }
}
