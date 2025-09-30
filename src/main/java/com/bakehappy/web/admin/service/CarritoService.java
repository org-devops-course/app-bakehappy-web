package com.bakehappy.web.admin.service;

import com.bakehappy.web.admin.entity.Carrito;
import com.bakehappy.web.admin.entity.CarritoItem;
import com.bakehappy.web.admin.entity.Producto;
import com.bakehappy.web.admin.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoService productoService;

    public Carrito getCarrito(Long clienteId) {
        Optional<Carrito> carritoOpt = carritoRepository.findAll().stream()
                .filter(c -> c.getClienteId().equals(clienteId))
                .findFirst();
        if (carritoOpt.isPresent()) {
            return carritoOpt.get();
        } else {
            Carrito nuevo = new Carrito();
            nuevo.setClienteId(clienteId);
            nuevo.setFechaCreacion(LocalDateTime.now());
            nuevo.setFechaActualizacion(LocalDateTime.now());
            return carritoRepository.save(nuevo);
        }
    }

    public void agregarProducto(Long clienteId, Long idProducto, int cantidad) {
        Carrito carrito = getCarrito(clienteId);
        Producto producto = productoService.getProductById(idProducto);
        Optional<CarritoItem> existente = carrito.getItems().stream()
                .filter(item -> item.getProducto().getIdProducto().equals(idProducto))
                .findFirst();

        if (existente.isPresent()) {
            CarritoItem item = existente.get();
            int nuevaCantidad = item.getCantidad() + cantidad;
            if (nuevaCantidad <= 0) {
                carrito.getItems().remove(item);
            } else {
                item.setCantidad(nuevaCantidad);
            }
        } else if (cantidad > 0) {
            CarritoItem nuevo = new CarritoItem();
            nuevo.setProducto(producto);
            nuevo.setCantidad(cantidad);
            nuevo.setPrecioUnitario(producto.getPrecio());
            nuevo.setNombreProducto(producto.getNombre());
            nuevo.setImagen(producto.getImagen());
            nuevo.setCarrito(carrito);
            carrito.getItems().add(nuevo);
        }
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);
    }

    public void quitarProducto(Long clienteId, Long idProducto) {
        Carrito carrito = getCarrito(clienteId);
        carrito.getItems().removeIf(item -> item.getProducto().getIdProducto().equals(idProducto));
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);
    }

    public void vaciarCarrito(Long clienteId) {
        Carrito carrito = getCarrito(clienteId);
        carrito.getItems().clear();
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);
    }

    public int getCantidadTotal(Long clienteId) {
        Carrito carrito = getCarrito(clienteId);
        return carrito.getItems().stream().mapToInt(CarritoItem::getCantidad).sum();
    }

    public BigDecimal getTotal(Long clienteId) {
        Carrito carrito = getCarrito(clienteId);
        return carrito.getItems().stream()
                .map(item -> item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}