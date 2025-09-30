package com.example.Proyecto_DAW.admin.controller;

import com.example.Proyecto_DAW.admin.entity.Producto;
import com.example.Proyecto_DAW.admin.service.CategoriaService;
import com.example.Proyecto_DAW.admin.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listarProductos(@RequestParam(value = "filtro", required = false) String filtro, Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        List<Producto> productos;
        if (filtro != null && !filtro.trim().isEmpty()) {
            productos = productoService.buscarPorNombre(filtro);
            model.addAttribute("filtroProductos", filtro);
        } else {
            productos = productoService.getAllProducts();
            model.addAttribute("filtroProductos", "");
        }
        model.addAttribute("productos", productos);
        model.addAttribute("totalProductos", productoService.getTotalProductos());
        model.addAttribute("productoMasVendido", productoService.getNombreProductoMasVendido());
        model.addAttribute("productoMasStock", productoService.getProductoMasStock());
        model.addAttribute("productoMenosStock", productoService.getProductoMenosStock());
        return "admin/productos/list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listarCategorias());
        return "admin/productos/create";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,
                                  @RequestParam("categoria.idCategoria") Long categoriaId,
                                  @RequestParam("archivoImagen") MultipartFile imagenFile, HttpSession session) throws IOException {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        producto.setCategoria(categoriaService.obtenerPorId(categoriaId));
        if (!imagenFile.isEmpty()) {
            String nombreArchivo = UUID.randomUUID() + "_" + imagenFile.getOriginalFilename();
            Path ruta = Paths.get("uploads").resolve(nombreArchivo);
            // Crear la carpeta 'uploads' si no existe
            Path carpetaUploads = Paths.get("uploads");
            if (!Files.exists(carpetaUploads)) {
                Files.createDirectories(carpetaUploads);
            }
            Files.copy(imagenFile.getInputStream(), ruta);
            producto.setImagen(nombreArchivo);
        }
        productoService.createProduct(producto);
        return "redirect:/productos";
    }

    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> verImagen(@PathVariable String filename, HttpSession session) throws MalformedURLException {
        Path ruta = Paths.get("uploads").resolve(filename);
        Resource recurso = new UrlResource(ruta.toUri());
        return ResponseEntity.ok().body(recurso);
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        Producto producto = productoService.getProductById(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listarCategorias());
        return "admin/productos/edit";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Long id,
                                     @ModelAttribute Producto producto,
                                     @RequestParam("categoria.idCategoria") Long categoriaId, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        producto.setIdProducto(id); // aseguramos que mantiene el mismo ID
        producto.setCategoria(categoriaService.obtenerPorId(categoriaId));
        productoService.updateProduct(producto);
        return "redirect:/productos";
    }

    @GetMapping("/ver/{id}")
    public String verProducto(@PathVariable Long id, Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        Producto producto = productoService.getProductById(id);
        model.addAttribute("producto", producto);
        return "admin/productos/detail";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("productos", productoService.getAllProducts());
        model.addAttribute("categorias", categoriaService.listarCategorias());
        return "admin/productos/list";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        productoService.deleteProduct(id);
        return "redirect:/productos";
    }
}
