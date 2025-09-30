package com.bakehappy.web.admin.api;

import com.bakehappy.web.admin.dto.ProductoDetalleResponseDTO;
import com.bakehappy.web.admin.dto.ProductoResponseDTO;
import com.bakehappy.web.admin.entity.Producto;
import com.bakehappy.web.admin.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:8082")
public class ProductoRestController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<ProductoResponseDTO> listarProductos() {
        List<Producto> productos = productoService.getAllProducts();
        List<ProductoResponseDTO> productosDTO = new ArrayList<>();
        for (Producto producto : productos) {
            ProductoResponseDTO dto = new ProductoResponseDTO();
            dto.setId(producto.getIdProducto());
            dto.setNombre(producto.getNombre());
            dto.setDescripcion(producto.getDescripcion());
            dto.setPrecio(producto.getPrecio());
            dto.setStock(producto.getStock());
            dto.setImagen(producto.getImagen());
            dto.setIdCategoria(producto.getCategoria().getIdCategoria());
            dto.setNombreCategoria(producto.getCategoria().getNombre());
            productosDTO.add(dto);
        }
        return productosDTO;
    }

    @GetMapping("/{id}")
    public ProductoDetalleResponseDTO obtenerProducto(@PathVariable Long id) {
        Producto producto = productoService.getProductById(id);
        ProductoDetalleResponseDTO dto = new ProductoDetalleResponseDTO();
        dto.setId(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setImagenUrl(producto.getImagen());
        dto.setIdCategoria(producto.getCategoria().getIdCategoria());
        dto.setNombreCategoria(producto.getCategoria().getNombre());
        return dto;
    }
}