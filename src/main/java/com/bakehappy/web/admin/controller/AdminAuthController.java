package com.bakehappy.web.admin.controller;

import com.bakehappy.web.admin.entity.Usuario;
import com.bakehappy.web.admin.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "admin/auth/registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
        usuario.setEstado(Usuario.Estado.ACTIVO);
        usuario.setRol("ADMIN");
        usuarioRepository.save(usuario);
        model.addAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "admin/auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String clave, HttpSession session, Model model) {
        Usuario usuario = usuarioRepository.findByEmailAndClave(email, clave)
                .orElse(null);
        if (usuario != null && usuario.getEstado() == Usuario.Estado.ACTIVO) {
            session.setAttribute("adminId", usuario.getIdUsuario());
            session.setAttribute("rol", usuario.getRol());
            return "index.html";
        } else {
            model.addAttribute("error", "Credenciales incorrectas o usuario inactivo");
            return "admin/auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}