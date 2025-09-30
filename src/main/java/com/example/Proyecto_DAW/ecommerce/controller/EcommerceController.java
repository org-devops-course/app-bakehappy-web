package com.example.Proyecto_DAW.ecommerce.controller;

import com.example.Proyecto_DAW.admin.dto.*;
import com.example.Proyecto_DAW.admin.entity.*;
import com.example.Proyecto_DAW.admin.repository.FacturaRepository;
import com.example.Proyecto_DAW.admin.repository.PedidoDetalleRepository;
import com.example.Proyecto_DAW.admin.service.CarritoService;
import com.example.Proyecto_DAW.admin.service.CategoriaService;
import com.example.Proyecto_DAW.admin.service.FacturaService;
import com.example.Proyecto_DAW.admin.service.ProductoService;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import com.itextpdf.text.Document;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ecommerce")
public class EcommerceController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    @GetMapping("/")
    public String ecommerce() {
        return "ecommerce/listProducts";
    }

    @GetMapping("/usar-producto")
    public String usarMetodoProducto() {
        return productoService.metodoDeProducto();
    }

    @GetMapping("/api/ventas-mensuales")
    @ResponseBody
    public List<VentaMensualDTO> obtenerVentasMensuales() {
        return facturaService.obtenerVentasPorMes();
    }

    @GetMapping("/api/ventas-semanales")
    @ResponseBody
    public List<VentaSemanalDTO> obtenerVentasSemanal() {
        return facturaService.obtenerVentasPorDiaSemana();
    }

    @GetMapping("/api/productos-mas-vendidos")
    @ResponseBody
    public List<Map<String, Object>> obtenerProductosMasVendidos() {
        return facturaService.obtenerProductosMasVendidos();
    }

    @GetMapping("/api/categorias-mas-vendidas")
    @ResponseBody
    public List<Map<String, Object>> obtenerCategoriasMasVendidas() {
        return facturaService.obtenerCategoriasMasVendidas();
    }

    @GetMapping("/api/clientes-mas-ventas")
    @ResponseBody
    public List<Map<String, Object>> obtenerClientesConMasVentas() {
        return facturaService.obtenerClientesConMasVentas();
    }

    @GetMapping("/productos/categoria/{idCategoria}")
    public String listarPorCategoria(@PathVariable Long idCategoria, Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("CLIENTE")) {
            return "redirect:/login";
        }
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }
        model.addAttribute("productos", productoService.getProductsByCategoriaId(idCategoria));
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("categoriaSeleccionada", idCategoria);
        model.addAttribute("items", carritoService.getCarrito(clienteId).getItems());
        model.addAttribute("total", carritoService.getTotal(clienteId));
        model.addAttribute("cantidadCarrito", carritoService.getCantidadTotal(clienteId));
        model.addAttribute("totalConEnvio", carritoService.getTotal(clienteId).add(new java.math.BigDecimal("10.00")));
        return "ecommerce/listProducts";
    }

    @GetMapping("/vista-productos")
    public String mostrarVistaProductos(Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("CLIENTE")) {
            return "redirect:/login";
        }
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }
        model.addAttribute("productos", productoService.getAllProducts());
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("items", carritoService.getCarrito(clienteId).getItems());
        model.addAttribute("total", carritoService.getTotal(clienteId));
        model.addAttribute("cantidadCarrito", carritoService.getCantidadTotal(clienteId));
        model.addAttribute("totalConEnvio", carritoService.getTotal(clienteId).add(new java.math.BigDecimal("10.00")));
        return "ecommerce/listProducts";
    }

    @GetMapping("/productos")
    public String listarTodos(Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("CLIENTE")) {
            return "redirect:/login";
        }
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }
        model.addAttribute("productos", productoService.getAllProducts());
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("categoriaSeleccionada", null);
        model.addAttribute("items", carritoService.getCarrito(clienteId).getItems());
        model.addAttribute("total", carritoService.getTotal(clienteId));
        model.addAttribute("cantidadCarrito", carritoService.getCantidadTotal(clienteId));
        model.addAttribute("totalConEnvio", carritoService.getTotal(clienteId).add(new java.math.BigDecimal("10.00")));
        return "ecommerce/listProducts";
    }

    @GetMapping("/verDetalle/{id}")
    public String verProductoDetalle(@PathVariable Long id, Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("CLIENTE")) {
            return "redirect:/login";
        }
        Producto producto = productoService.getProductById(id);
        model.addAttribute("producto", producto);

        List<String> productosEspeciales = List.of("Alfajorcitos", "Brownies", "Pionono", "Trufas");
        model.addAttribute("productosEspeciales", productosEspeciales);

        List<String> productosPies = List.of("Mini Pie de Limon", "Mini Pie de Fresa");
        model.addAttribute("productosPies", productosPies);

        List<String> productosCarnes = List.of("Sanguchitos de Pollo", "Empanaditas de Pollo", "Empanaditas de Carne");
        model.addAttribute("productosCarnes", productosCarnes);

        List<String> productosBurger = List.of("Hamburguesitas");
        model.addAttribute("productosBurger", productosBurger);

        List<String> productosMini = List.of("Mini Triple Pollo, Jamon y Queso", "Mini Triple Huevo, Aceitunas Y Pecanas", "Mini Croissant de Jamón y Queso");
        model.addAttribute("productosMini", productosMini);

        List<String> productosPizza = List.of("Mini Pizza");
        model.addAttribute("productosPizza", productosPizza);

        List<String> productosAlitas = List.of("Alitas Bouchet");
        model.addAttribute("productosAlitas", productosAlitas);

        List<String> productosShot = List.of("Postres en Shot");
        model.addAttribute("productosShot", productosShot);


        return "ecommerce/detailView";
    }

    @PostMapping("/carrito/agregar/{idProducto}")
    public String agregarAlCarrito(@PathVariable Long idProducto, @RequestParam(defaultValue = "1") int cantidad, Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("CLIENTE")) {
            return "redirect:/login";
        }
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }
        carritoService.agregarProducto(clienteId, idProducto, cantidad);
        model.addAttribute("productos", productoService.getAllProducts());
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("categoriaSeleccionada", null);
        model.addAttribute("items", carritoService.getCarrito(clienteId).getItems());
        model.addAttribute("total", carritoService.getTotal(clienteId));
        model.addAttribute("cantidadCarrito", carritoService.getCantidadTotal(clienteId));
        model.addAttribute("totalConEnvio", carritoService.getTotal(clienteId).add(new java.math.BigDecimal("10.00")));
        model.addAttribute("abrirCarrito", true);
        return "ecommerce/listProducts";
    }

    @PostMapping("/carrito/quitar/{idProducto}")
    public String quitarDelCarrito(@PathVariable Long idProducto, Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("CLIENTE")) {
            return "redirect:/login";
        }
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }
        carritoService.quitarProducto(clienteId, idProducto);
        model.addAttribute("productos", productoService.getAllProducts());
        model.addAttribute("categorias", categoriaService.listarCategorias());
        model.addAttribute("categoriaSeleccionada", null);
        model.addAttribute("items", carritoService.getCarrito(clienteId).getItems());
        model.addAttribute("total", carritoService.getTotal(clienteId));
        model.addAttribute("cantidadCarrito", carritoService.getCantidadTotal(clienteId));
        model.addAttribute("totalConEnvio", carritoService.getTotal(clienteId).add(new java.math.BigDecimal("10.00")));
        model.addAttribute("abrirCarrito", true);
        return "ecommerce/listProducts";
    }

    @GetMapping("/carrito")
    public String verCarrito(Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("CLIENTE")) {
            return "redirect:/login";
        }
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }
        model.addAttribute("carrito", carritoService.getCarrito(clienteId));
        model.addAttribute("items", carritoService.getCarrito(clienteId).getItems());
        model.addAttribute("total", carritoService.getTotal(clienteId));
        model.addAttribute("cantidadCarrito", carritoService.getCantidadTotal(clienteId));
        model.addAttribute("totalConEnvio", carritoService.getTotal(clienteId).add(new java.math.BigDecimal("10.00")));
        return "ecommerce/carrito";
    }

    //Factura y pago

    @GetMapping("/factura")
    public String verFactura(Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("CLIENTE")) {
            return "redirect:/login";
        }
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }
        Carrito carrito = carritoService.getCarrito(clienteId);

        for (CarritoItem item : carrito.getItems()) {
            Producto producto = productoService.getProductById(item.getProducto().getIdProducto());
            if (item.getCantidad() > producto.getStock()) {
                model.addAttribute("error", "La cantidad seleccionada para '" + producto.getNombre() + "' supera el stock disponible.");
                model.addAttribute("productos", productoService.getAllProducts());
                model.addAttribute("categorias", categoriaService.listarCategorias());
                model.addAttribute("items", carrito.getItems());
                model.addAttribute("total", carritoService.getTotal(clienteId));
                model.addAttribute("cantidadCarrito", carritoService.getCantidadTotal(clienteId));
                model.addAttribute("totalConEnvio", carritoService.getTotal(clienteId).add(new java.math.BigDecimal("10.00")));
                return "ecommerce/listProducts";
            }
        }

        FacturaDTO factura = facturaService.obtenerDatosFactura(carrito);
        model.addAttribute("factura", factura);
        model.addAttribute("datosPago", new DatosPagoDTO());
        return "ecommerce/factura";
    }

    @PostMapping("/pagar")
    public String procesarPago(@ModelAttribute DatosPagoDTO datosPago, HttpSession session, Model model) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("CLIENTE")) {
            return "redirect:/login";
        }
        Long clienteId = (Long) session.getAttribute("clienteId");
        if (clienteId == null) {
            return "redirect:/login";
        }
        Carrito carrito = carritoService.getCarrito(clienteId);

        if ("Tarjeta".equalsIgnoreCase(datosPago.getMetodoPago())) {
            if (datosPago.getNumeroTarjeta() == null || datosPago.getNumeroTarjeta().length() != 16) {
                model.addAttribute("error", "Número de tarjeta inválido");
                FacturaDTO factura = facturaService.obtenerDatosFactura(carrito);
                model.addAttribute("factura", factura);
                model.addAttribute("datosPago", datosPago);
                return "ecommerce/factura";
            }
        }

        Pedido pedido = facturaService.procesarPagoYRegistrarOrden(carrito, datosPago);

        session.setAttribute("itemsCompra", carrito.getItems());

        carritoService.vaciarCarrito(clienteId);
        return "redirect:/ecommerce/confirmacion/" + pedido.getIdPedido();
    }

    @GetMapping("/confirmacion/{idPedido}")
    public String mostrarConfirmacion(@PathVariable Long idPedido, HttpSession session, Model model) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("CLIENTE")) {
            return "redirect:/login";
        }
        Pedido pedido = facturaRepository.findById(idPedido).orElse(null);
        if (pedido == null) {
            return "redirect:/ecommerce";
        }
        List<CarritoItem> itemsCompra = (List<CarritoItem>) session.getAttribute("itemsCompra");
        ConfirmacionDTO confirmacion = facturaService.obtenerConfirmacionCompra(pedido, itemsCompra != null ? itemsCompra : List.of());
        model.addAttribute("confirmacion", confirmacion);
        session.removeAttribute("itemsCompra");
        return "ecommerce/confirmacion";
    }

    @GetMapping("/descargar-factura/{idPedido}")
    public void descargarFactura(@PathVariable Long idPedido, HttpServletResponse response) throws IOException, DocumentException {
        Pedido pedido = facturaRepository.findById(idPedido).orElseThrow();
        List<PedidoDetalle> detalles = pedidoDetalleRepository.findByPedido(pedido);
        Pago pago = pedido.getPagos().isEmpty() ? null : pedido.getPagos().get(0);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=factura_" + idPedido + ".pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("Factura #" + pedido.getIdPedido()));
        document.add(new Paragraph("Fecha: " + pedido.getFechaPedido()));
        document.add(new Paragraph("Cliente: " + pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellidos()));
        document.add(new Paragraph("Dirección: " + pedido.getDireccion()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.addCell("Producto");
        table.addCell("Cantidad");
        table.addCell("Precio");
        table.addCell("Subtotal");
        for (PedidoDetalle detalle : detalles) {
            table.addCell(detalle.getProducto().getNombre());
            table.addCell(String.valueOf(detalle.getCantidad()));
            table.addCell(detalle.getPrecioUnitario().toString());
            table.addCell(detalle.getPrecioUnitario().multiply(new java.math.BigDecimal(detalle.getCantidad())).toString());
        }
        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Subtotal: S/ " + pedido.getSubtotal()));
        document.add(new Paragraph("Envío: S/ 10.00"));
        document.add(new Paragraph("Impuestos: S/ " + pedido.getSubtotal().multiply(new java.math.BigDecimal("0.18"))));
        document.add(new Paragraph("Total: S/ " + pedido.getTotal()));

        if (pago != null) {
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Método de Pago: " + pago.getMetodoPago()));
            document.add(new Paragraph("Monto Pagado: S/ " + pago.getMonto()));
            document.add(new Paragraph("Fecha de Pago: " + pago.getFechaPago()));
        }

        document.close();
    }

    @GetMapping("/index")
    public String mostrarIndex(Model model) {
        Producto productoDestacado = productoService.getProductoMasVendido();
        model.addAttribute("productoDestacado", productoDestacado);

        List<Producto> productosDestacados = productoService.getAllProducts()
                .stream()
                .filter(p -> p.getStock() > 0)
                .sorted((a, b) -> b.getStock().compareTo(a.getStock()))
                .limit(6)
                .toList();
        model.addAttribute("productosDestacados", productosDestacados);

        List<Producto> productosMasVendidos = productoService.getProductosMasVendidos(3); // método personalizado
        model.addAttribute("productosMasVendidos", productosMasVendidos);

        return "index";
    }
}