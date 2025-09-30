package com.example.Proyecto_DAW.admin.service;

import com.example.Proyecto_DAW.admin.entity.Cliente;
import com.example.Proyecto_DAW.admin.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public long getTotalClientes() {
        return clienteRepository.count();
    }

    public long getClientesActivos() {
        return clienteRepository.findAll().stream()
                .filter(c -> "ACTIVO".equalsIgnoreCase(c.getEstado()))
                .count();
    }

    // Nuevos clientes en el último mes (asumiendo que tienes un campo 'fechaRegistro' en Cliente)
    public long getNuevosClientesUltimoMes() {
        LocalDateTime haceUnMes = LocalDateTime.now().minusMonths(1);
        return clienteRepository.findByFechaRegistroAfter(haceUnMes).size();
    }
}