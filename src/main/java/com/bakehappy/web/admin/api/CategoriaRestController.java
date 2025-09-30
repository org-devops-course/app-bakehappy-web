package com.bakehappy.web.admin.api;

import com.bakehappy.web.admin.dto.CategoriaResponseDTO;
import com.bakehappy.web.admin.dto.ProductoResponseDTO;
import com.bakehappy.web.admin.entity.Categoria;
import com.bakehappy.web.admin.entity.Producto;
import com.bakehappy.web.admin.service.CategoriaService;
import com.bakehappy.web.admin.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:8082")
public class CategoriaRestController {

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<CategoriaResponseDTO> listarCategorias() {
        List<Categoria> categorias = categoriaService.listarCategorias();
        List<CategoriaResponseDTO> categoriasDTO = new ArrayList<>();
        for (Categoria categoria : categorias) {
            CategoriaResponseDTO dto = new CategoriaResponseDTO();
//            dto.setId(categoria.getIdCategoria());
            dto.setNombre(categoria.getNombre());
            categoriasDTO.add(dto);
        }
        return categoriasDTO;
    }

    @GetMapping("/categoria/{idCategoria}")
    public List<ProductoResponseDTO> listarProductosPorCategoria(@PathVariable Categoria categoria, Long idCategoria) {
        List<Producto> productos = productoService.getAllProducts();
        List<ProductoResponseDTO> productosDTO = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getCategoria().getIdCategoria().equals(idCategoria)) {
                ProductoResponseDTO dto = new ProductoResponseDTO();
                dto.setId(producto.getIdProducto());
                dto.setNombre(producto.getNombre());
                dto.setDescripcion(producto.getDescripcion());
                dto.setPrecio(producto.getPrecio());
                dto.setStock(producto.getStock());
                dto.setImagen(producto.getImagen());
                dto.setId(categoria.getIdCategoria());
                dto.setIdCategoria(producto.getCategoria().getIdCategoria());
                dto.setNombreCategoria(producto.getCategoria().getNombre());
                productosDTO.add(dto);
            }
        }
        return productosDTO;
    }
}