package com.proyecto.gestor.controlador;

import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class ControladorUsuario {
    
    private final ServicioUsuario servicioUsuario;
    
    @Autowired
    public ControladorUsuario(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }
    
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", servicioUsuario.obtenerTodosLosUsuarios());
        return "usuario/lista";
    }
    
    @GetMapping("/nuevo")
    public String formularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/formulario";
    }
    
    @PostMapping
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        servicioUsuario.guardarUsuario(usuario);
        return "redirect:/usuarios";
    }
    
    @GetMapping("/{id}")
    public String verUsuario(@PathVariable("id") Long id, Model model) {
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario no válido: " + id));
        model.addAttribute("usuario", usuario);
        return "usuario/ver";
    }
    
    @GetMapping("/{id}/editar")
    public String formularioEditarUsuario(@PathVariable("id") Long id, Model model) {
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario no válido: " + id));
        model.addAttribute("usuario", usuario);
        return "usuario/formulario";
    }
    
    @GetMapping("/{id}/eliminar")
    public String eliminarUsuario(@PathVariable("id") Long id) {
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario no válido: " + id));
        // Aquí deberías agregar lógica para manejar dependencias antes de eliminar
        return "redirect:/usuarios";
    }
}