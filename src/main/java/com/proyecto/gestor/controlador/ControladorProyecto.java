package com.proyecto.gestor.controlador;

import com.proyecto.gestor.modelo.Comentario;
import com.proyecto.gestor.modelo.Proyecto;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.servicio.ServicioComentario;
import com.proyecto.gestor.servicio.ServicioProyecto;
import com.proyecto.gestor.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/proyectos")
public class ControladorProyecto {

    private final ServicioProyecto servicioProyecto;
    private final ServicioUsuario servicioUsuario;
    private final ServicioComentario servicioComentario; // Añadir este servicio

    @Autowired
    public ControladorProyecto(ServicioProyecto servicioProyecto, 
                              ServicioUsuario servicioUsuario,
                              ServicioComentario servicioComentario) { // Actualizar constructor
        this.servicioProyecto = servicioProyecto;
        this.servicioUsuario = servicioUsuario;
        this.servicioComentario = servicioComentario; // Inicializar servicio
    }

    @GetMapping
    public String listarProyectos(Model model) {
        model.addAttribute("proyectos", servicioProyecto.obtenerTodosLosProyectos());
        return "proyecto/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevoProyecto(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("usuarios", servicioUsuario.obtenerTodosLosUsuarios());
        return "proyecto/formulario";
    }

    @PostMapping
    public String guardarProyecto(@ModelAttribute("proyecto") Proyecto proyecto) {
        servicioProyecto.guardarProyecto(proyecto);
        return "redirect:/proyectos";
    }

     @GetMapping("/{id}")
    public String verProyecto(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Proyecto> optProyecto = servicioProyecto.obtenerProyectoPorId(id);
        
        if (optProyecto.isPresent()) {
            Proyecto proyecto = optProyecto.get();
            model.addAttribute("proyecto", proyecto);

            model.addAttribute("comentarios", servicioComentario.obtenerComentariosProyecto(proyecto));
            model.addAttribute("nuevoComentario", new Comentario());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioActual = servicioUsuario.obtenerUsuarioPorNombreUsuario(auth.getName());
            model.addAttribute("usuarioActual", usuarioActual);
            
            return "proyecto/ver";
        }
        
        redirectAttributes.addFlashAttribute("mensajeError", "El proyecto no existe");
        return "redirect:/proyectos";
    }

    @GetMapping("/{id}/editar")
    public String formularioEditarProyecto(@PathVariable("id") Long id, Model model) {
        Proyecto proyecto = servicioProyecto.obtenerProyectoPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de proyecto no válido: " + id));
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("usuarios", servicioUsuario.obtenerTodosLosUsuarios());
        return "proyecto/formulario";
    }

    @GetMapping("/{id}/agregarMiembro")
    public String mostrarFormularioAgregarMiembro(@PathVariable("id") Long id, Model model) {
        Proyecto proyecto = servicioProyecto.obtenerProyectoPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de proyecto no válido: " + id));
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("usuarios", servicioUsuario.obtenerTodosLosUsuarios());
        return "proyecto/agregarMiembro";
    }

    @PostMapping("/{id}/agregarMiembro")
    public String agregarMiembro(@PathVariable("id") Long idProyecto, @RequestParam("idUsuario") Long idUsuario) {
        Proyecto proyecto = servicioProyecto.obtenerProyectoPorId(idProyecto)
                .orElseThrow(() -> new IllegalArgumentException("ID de proyecto no válido: " + idProyecto));
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("ID de usuario no válido: " + idUsuario));

        proyecto.agregarMiembro(usuario);
        servicioProyecto.guardarProyecto(proyecto);

        return "redirect:/proyectos/" + idProyecto;
    }
}
