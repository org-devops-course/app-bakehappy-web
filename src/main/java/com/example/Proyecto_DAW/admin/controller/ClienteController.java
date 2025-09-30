package com.example.Proyecto_DAW.admin.controller;

import com.example.Proyecto_DAW.admin.entity.Cliente;
import com.example.Proyecto_DAW.admin.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(@RequestParam(value = "filtro", required = false) String filtro, Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        List<Cliente> clientes;
        if (filtro != null && !filtro.trim().isEmpty()) {
            clientes = clienteService.buscarPorNombre(filtro);
        } else {
            clientes = clienteService.listarClientes();
        }
        model.addAttribute("clientes", clientes);
        model.addAttribute("filtro", filtro);

        // Agrega las estadísticas al modelo
        model.addAttribute("totalClientes", clienteService.getTotalClientes());
        model.addAttribute("clientesActivos", clienteService.getClientesActivos());
        model.addAttribute("nuevosClientesUltimoMes", clienteService.getNuevosClientesUltimoMes());

        return "admin/clientes/list";
    }
}