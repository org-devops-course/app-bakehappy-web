package com.bakehappy.web.admin.service;

import com.bakehappy.web.admin.entity.Producto;
import com.bakehappy.web.admin.repository.PedidoDetalleRepository;
import com.bakehappy.web.admin.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class ProductoService {

    @Autowired
    private ProductoRepository productRepository;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    public String metodoDeProducto() {
        return "Producto ejecutado";
    }

    public Producto createProduct(Producto product) {
        if (product.getCategoria() == null || product.getCategoria().getIdCategoria() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria");
        }
        if (product.getNombre() == null || product.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (product.getDescripcion() == null || product.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion del producto es obligatoria");
        }
        if (product.getPrecio() == null || product.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (product.getStock() == null || product.getStock() <= 0) {
            throw new IllegalArgumentException("El stock debe ser mayor a 0");
        }
        return productRepository.save(product);
    }

    public List<Producto> getAllProducts() {
        return (List<Producto>) productRepository.findAll();
    }

    public List<Producto> getProductsByCategoriaId(Long categoriaId) {
        return productRepository.findByCategoria_IdCategoria(categoriaId);
    }

    public Producto getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    public Producto updateProduct(Producto producto) {
        return productRepository.save(producto);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar. Producto no encontrado con ID: " + id);
        }
        boolean tienePedidos = pedidoDetalleRepository.existsByProducto_IdProducto(id);
        if (tienePedidos) {
            throw new IllegalStateException("No se puede eliminar el producto porque tiene pedidos asociados.");
        }
        productRepository.deleteById(id);
    }

    public List<Producto> getProductosMasVendidos(int cantidad) {
        List<Object[]> resultados = pedidoDetalleRepository.findProductosMasVendidos();
        List<Producto> productos = new ArrayList<>();
        for (int i = 0; i < Math.min(cantidad, resultados.size()); i++) {
            Object[] fila = resultados.get(i);
            String nombreProducto = (String) fila[0];
            Producto producto = productRepository.findAll().stream()
                    .filter(p -> p.getNombre().equals(nombreProducto))
                    .findFirst()
                    .orElse(null);
            if (producto != null) productos.add(producto);
        }
        return productos;
    }

    public Producto getProductoMasVendido() {
        List<Object[]> resultados = pedidoDetalleRepository.findProductosMasVendidos();
        if (resultados.isEmpty()) return null;
        Object[] fila = resultados.get(0);
        String nombreProducto = (String) fila[0];
        return productRepository.findAll().stream()
                .filter(p -> p.getNombre().equals(nombreProducto))
                .findFirst()
                .orElse(null);
    }

    public long getTotalProductos() {
        return productRepository.count();
    }

    public String getNombreProductoMasVendido() {
        Producto producto = getProductoMasVendido();
        return producto != null ? producto.getNombre() : "N/A";
    }

    public String getProductoMasStock() {
        return productRepository.findAll().stream()
                .max((a, b) -> Integer.compare(a.getStock(), b.getStock()))
                .map(Producto::getNombre)
                .orElse("N/A");
    }

    public String getProductoMenosStock() {
        return productRepository.findAll().stream()
                .min((a, b) -> Integer.compare(a.getStock(), b.getStock()))
                .map(Producto::getNombre)
                .orElse("N/A");
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
