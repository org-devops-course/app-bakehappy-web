package com.bakehappy.web.admin.service;

import com.bakehappy.web.admin.entity.Categoria;
import com.bakehappy.web.admin.repository.CategoriaRepository;
import com.bakehappy.web.admin.repository.PedidoDetalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Component
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PedidoDetalleRepository pedidoDetalleRepository;

    public String metodoDeCategoria() {
        return "Categoria ejecutado";
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
    }

    public Categoria crearCategoria(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        return categoriaRepository.save(categoria);
    }

    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar. Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    public long getTotalCategorias() {
        return categoriaRepository.count();
    }

    public String getCategoriaConMasProductos() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .max(Comparator.comparingInt(c -> c.getProductos().size()))
                .map(Categoria::getNombre)
                .orElse("-");
    }

    public String getCategoriaMasVendida() {
        List<Object[]> resultados = pedidoDetalleRepository.findCategoriasMasVendidas();
        if (resultados.isEmpty()) return "-";
        Object[] fila = resultados.get(0);
        return (String) fila[0];
    }
}