package com.bakehappy.web.ecommerce.controller;

import com.bakehappy.web.admin.entity.Cliente;
import com.bakehappy.web.admin.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;

@Controller
public class AuthController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "ecommerce/registro";
    }

    @PostMapping("/registro")
    public String registrarCliente(@ModelAttribute Cliente cliente, Model model) {
        cliente.setRol("CLIENTE"); // Asigna el rol antes de guardar
        cliente.setEstado("ACTIVO");
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setRol("CLIENTE");
        clienteRepository.save(cliente);
        model.addAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "ecommerce/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String correo, @RequestParam String password, HttpSession session, Model model) {
        Cliente cliente = clienteRepository.findAll().stream()
                .filter(c -> c.getCorreo().equals(correo) && c.getPassword().equals(password))
                .findFirst().orElse(null);
        if (cliente != null) {
            session.setAttribute("clienteId", cliente.getIdCliente());
            session.setAttribute("rol", cliente.getRol());
            return "redirect:/ecommerce/productos";
        } else {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "ecommerce/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}