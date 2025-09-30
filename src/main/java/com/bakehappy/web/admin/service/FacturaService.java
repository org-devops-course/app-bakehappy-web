package com.bakehappy.web.admin.service;

import com.bakehappy.web.admin.dto.*;
import com.bakehappy.web.admin.entity.*;
import com.bakehappy.web.admin.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public String metodoDeFactura() {
        return "Factura ejecutada";
    }

    public List<VentaMensualDTO> obtenerVentasPorMes() {
        List<Pedido> pedidos = facturaRepository.findAll(Sort.by("fechaPedido"));
        Map<String, Integer> ventasPorMes = new LinkedHashMap<>();

        for (Pedido pedido : pedidos) {
            String mes = pedido.getFechaPedido().getMonth().getDisplayName(TextStyle.SHORT, new Locale("es"));
            ventasPorMes.put(mes, ventasPorMes.getOrDefault(mes, 0) + pedido.getTotal().intValue());
        }

        List<VentaMensualDTO> resultado = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : ventasPorMes.entrySet()) {
            VentaMensualDTO dto = new VentaMensualDTO();
            dto.setMes(entry.getKey());
            dto.setTotal(entry.getValue());
            resultado.add(dto);
        }
        return resultado;
    }

    public List<VentaSemanalDTO> obtenerVentasPorDiaSemana() {
        List<Pedido> pedidos = facturaRepository.findAll();
        String[] dias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        Map<String, Integer> ventasPorDia = new LinkedHashMap<>();
        for (String dia : dias) ventasPorDia.put(dia, 0);

        for (Pedido pedido : pedidos) {
            String dia = pedido.getFechaPedido().getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("es")).substring(0, 1).toUpperCase() + pedido.getFechaPedido().getDayOfWeek().getDisplayName(TextStyle.SHORT, new Locale("es")).substring(1).toLowerCase();
            ventasPorDia.put(dia, ventasPorDia.getOrDefault(dia, 0) + pedido.getTotal().intValue());
        }

        List<VentaSemanalDTO> resultado = new ArrayList<>();
        for (String dia : dias) {
            VentaSemanalDTO dto = new VentaSemanalDTO();
            dto.setDia(dia);
            dto.setTotal(ventasPorDia.get(dia));
            resultado.add(dto);
        }
        System.out.println("Ventas por día: " + ventasPorDia);
        return resultado;
    }

    public List<Map<String, Object>> obtenerProductosMasVendidos() {
        List<Object[]> resultados = pedidoDetalleRepository.findProductosMasVendidos();
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("nombre", fila[0]);
            map.put("cantidadVendida", fila[1]);
            lista.add(map);
        }
        return lista;
    }

    public List<Map<String, Object>> obtenerCategoriasMasVendidas() {
        List<Object[]> resultados = pedidoDetalleRepository.findCategoriasMasVendidas();
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("nombre", fila[0]);
            map.put("cantidadVendida", fila[1]);
            lista.add(map);
        }
        return lista;
    }

    public List<Map<String, Object>> obtenerClientesConMasVentas() {
        List<Object[]> resultados = pedidoDetalleRepository.findClientesConMasVentas();
        List<Map<String, Object>> lista = new ArrayList<>();
        for (Object[] fila : resultados) {
            Map<String, Object> map = new HashMap<>();
            map.put("nombre", fila[0]);
            map.put("ventas", fila[1]);
            lista.add(map);
        }
        return lista;
    }

    public FacturaDTO obtenerDatosFactura(Carrito carrito) {
        List<ItemFacturaDTO> items = carrito.getItems().stream().map(item -> {
            ItemFacturaDTO dto = new ItemFacturaDTO();
            dto.setIdProducto(item.getProducto().getIdProducto());
            dto.setNombreProducto(item.getNombreProducto());
            dto.setImagen(item.getImagen());
            dto.setCategoria(item.getCategoriaNombre());
            dto.setCantidad(item.getCantidad());
            dto.setPrecioUnitario(item.getPrecioUnitario());
            dto.setSubtotal(item.getSubtotal());
            return dto;
        }).collect(Collectors.toList());

        BigDecimal subtotal = items.stream()
                .map(ItemFacturaDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal envio = new BigDecimal("10.00");
        BigDecimal impuestos = subtotal.multiply(new BigDecimal("0.18"));
        BigDecimal total = subtotal.add(envio).add(impuestos);

        FacturaDTO factura = new FacturaDTO();
        factura.setItems(items);
        factura.setSubtotal(subtotal);
        factura.setEnvio(envio);
        factura.setImpuestos(impuestos);
        factura.setTotal(total);

        return factura;
    }

    @Transactional
    public Pedido procesarPagoYRegistrarOrden(Carrito carrito, DatosPagoDTO datosPago) {
        if (datosPago.getMetodoPago() == null || datosPago.getMetodoPago().isEmpty()) {
            throw new IllegalArgumentException("Método de pago requerido");
        }
        Cliente cliente = clienteRepository.findById(carrito.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        pedido.setDireccion(
                datosPago.getNombre() + " " + datosPago.getApellido() + ", " +
                        datosPago.getDireccion() + ", " +
                        datosPago.getCiudad() + ", " +
                        datosPago.getCodigoPostal()
        );
        pedido.setCantidad(carrito.getItems().stream().mapToInt(CarritoItem::getCantidad).sum());
        pedido.setSubtotal(carrito.getItems().stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        pedido.setTotal(pedido.getSubtotal().add(new BigDecimal("10.00")).add(
                pedido.getSubtotal().multiply(new BigDecimal("0.18"))));
        if (!carrito.getItems().isEmpty()) {
            pedido.setProducto(carrito.getItems().get(0).getProducto());
        }
        Pedido pedidoGuardado = facturaRepository.save(pedido);
        for (CarritoItem item : carrito.getItems()) {
            Producto producto = productoRepository.findById(item.getProducto().getIdProducto()).orElseThrow();
            if (item.getCantidad() > producto.getStock()) {
                throw new IllegalArgumentException("No hay suficiente stock para el producto: " + producto.getNombre());
            }
            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setPedido(pedidoGuardado);
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            pedidoDetalleRepository.save(detalle);
            // Actualizar stock del producto
            int nuevoStock = producto.getStock() - item.getCantidad();
            producto.setStock(nuevoStock);
            productoRepository.save(producto);
        }
        Pago pago = new Pago();
        pago.setPedido(pedidoGuardado);
        pago.setMetodoPago(datosPago.getMetodoPago());
        pago.setMonto(pedidoGuardado.getTotal());
        pago.setFechaPago(LocalDateTime.now());
        pagoRepository.save(pago);

        return pedidoGuardado;
    }

    public ConfirmacionDTO obtenerConfirmacionCompra(Pedido pedido, List<CarritoItem> itemsCarrito) {
        List<ItemFacturaDTO> resumenCompra = itemsCarrito.stream().map(item -> {
            ItemFacturaDTO dto = new ItemFacturaDTO();
            dto.setIdProducto(item.getProducto().getIdProducto());
            dto.setNombreProducto(item.getNombreProducto());
            dto.setImagen(item.getImagen());
            dto.setCategoria(item.getCategoriaNombre());
            dto.setCantidad(item.getCantidad());
            dto.setPrecioUnitario(item.getPrecioUnitario());
            dto.setSubtotal(item.getSubtotal());
            return dto;
        }).collect(Collectors.toList());

        ConfirmacionDTO confirmacion = new ConfirmacionDTO();
        confirmacion.setNumeroPedido(pedido.getIdPedido());
        confirmacion.setResumenCompra(resumenCompra);
        confirmacion.setTotal(pedido.getTotal());
        confirmacion.setMensaje("¡Gracias por tu compra!");
        return confirmacion;
    }


}