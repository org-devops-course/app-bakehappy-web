package com.bakehappy.web.admin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @NotNull
    private LocalDateTime fechaPedido;

    @NotBlank
    @Column(length = 20)
    private String estado;

    @Column(name = "direccion_envio", length = 255)
    private String direccion;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal subtotal;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Producto producto;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Pago> pagos;

    public Pedido() {

    }

    public Pedido(Long idPedido, Cliente cliente, LocalDateTime fechaPedido, String estado, String direccion, int cantidad, BigDecimal subtotal, BigDecimal total, Producto producto, List<Pago> pagos) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.total = total;
        this.producto = producto;
        this.pagos = pagos;
        this.direccion = direccion;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}