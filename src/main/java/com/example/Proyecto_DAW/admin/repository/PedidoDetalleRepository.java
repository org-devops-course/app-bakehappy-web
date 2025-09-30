package com.example.Proyecto_DAW.admin.repository;

import com.example.Proyecto_DAW.admin.entity.Pedido;
import com.example.Proyecto_DAW.admin.entity.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long> {
    List<PedidoDetalle> findByPedido(Pedido pedido);

    boolean existsByProducto_IdProducto(Long idProducto);

    @Query("SELECT pd.producto.nombre, SUM(pd.cantidad) as cantidadVendida " +
            "FROM PedidoDetalle pd GROUP BY pd.producto.nombre ORDER BY cantidadVendida DESC")
    List<Object[]> findProductosMasVendidos();

    @Query("SELECT pd.producto.categoria.nombre, SUM(pd.cantidad) as cantidadVendida " +
            "FROM PedidoDetalle pd GROUP BY pd.producto.categoria.nombre ORDER BY cantidadVendida DESC")
    List<Object[]> findCategoriasMasVendidas();

    @Query("SELECT pd.pedido.cliente.nombre, COUNT(pd.pedido.idPedido) as ventas " +
            "FROM PedidoDetalle pd GROUP BY pd.pedido.cliente.nombre ORDER BY ventas DESC")
    List<Object[]> findClientesConMasVentas();
}
