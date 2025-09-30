package com.bakehappy.web.admin.controller;

import com.bakehappy.web.admin.entity.Usuario;
import com.bakehappy.web.admin.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("totalAdmins", usuarioService.getTotalAdmins());
        model.addAttribute("adminsActivos", usuarioService.getAdminsActivos());
        return "admin/usuarios/listUsuario";
    }

    @GetMapping("/new")
    public String mostrarFormularioNuevo(Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        model.addAttribute("usuario", new Usuario());
        return "admin/usuarios/createUsuario"; // Vista: usuarios/create.html
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        usuario.setRol("ADMIN");
        usuarioService.crearUsuario(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        Usuario usuario = usuarioService.obtenerPorId(id);
        model.addAttribute("usuario", usuario);
        return "admin/usuarios/editUsuario"; // Vista: usuarios/edit.html
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        usuario.setIdUsuario(id); // Mantener el mismo ID
        usuarioService.crearUsuario(usuario); // Reutilizamos crearUsuario para actualizar
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if (rol == null || !rol.equals("ADMIN")) {
            return "redirect:/admin/login";
        }
        usuarioService.eliminarUsuario(id);
        return "redirect:/usuarios";
    }
}
