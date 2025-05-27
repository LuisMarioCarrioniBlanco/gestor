package com.proyecto.gestor.controlador;

import com.proyecto.gestor.modelo.Comentario;
import com.proyecto.gestor.modelo.Proyecto;
import com.proyecto.gestor.modelo.Tarea;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.servicio.ServicioComentario;
import com.proyecto.gestor.servicio.ServicioProyecto;
import com.proyecto.gestor.servicio.ServicioTarea;
import com.proyecto.gestor.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/comentarios")
public class ControladorComentario {

    private final ServicioComentario servicioComentario;
    private final ServicioUsuario servicioUsuario;
    private final ServicioProyecto servicioProyecto;
    private final ServicioTarea servicioTarea;

    @Autowired
    public ControladorComentario(ServicioComentario servicioComentario, 
                                ServicioUsuario servicioUsuario,
                                ServicioProyecto servicioProyecto,
                                ServicioTarea servicioTarea) {
        this.servicioComentario = servicioComentario;
        this.servicioUsuario = servicioUsuario;
        this.servicioProyecto = servicioProyecto;
        this.servicioTarea = servicioTarea;
    }

    @PostMapping("/proyecto/{id}")
    public String agregarComentarioProyecto(@PathVariable("id") Long proyectoId, 
                                          @RequestParam("contenido") String contenido,
                                          RedirectAttributes redirectAttributes) {
        Usuario usuarioActual = obtenerUsuarioActual();
        Optional<Proyecto> optProyecto = servicioProyecto.obtenerProyectoPorId(proyectoId);
        
        if (optProyecto.isPresent()) {
            Proyecto proyecto = optProyecto.get();
            servicioComentario.crearComentarioProyecto(usuarioActual, proyecto, contenido);
            redirectAttributes.addFlashAttribute("mensajeExito", "Comentario agregado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Error: proyecto no encontrado");
        }
        
        return "redirect:/proyectos/" + proyectoId;
    }

    @PostMapping("/tarea/{id}")
    public String agregarComentarioTarea(@PathVariable("id") Long tareaId, 
                                        @RequestParam("contenido") String contenido,
                                        RedirectAttributes redirectAttributes) {
        Usuario usuarioActual = obtenerUsuarioActual();
        Optional<Tarea> optTarea = servicioTarea.obtenerTareaPorId(tareaId);
        
        if (optTarea.isPresent()) {
            Tarea tarea = optTarea.get();
            servicioComentario.crearComentarioTarea(usuarioActual, tarea, contenido);
            redirectAttributes.addFlashAttribute("mensajeExito", "Comentario agregado correctamente");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Error: tarea no encontrada");
        }
        
        return "redirect:/tareas/" + tareaId;
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarComentario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuarioActual = obtenerUsuarioActual();
        Optional<Comentario> optComentario = servicioComentario.obtenerComentarioPorId(id);
        
        if (optComentario.isPresent()) {
            Comentario comentario = optComentario.get();

            if (comentario.getUsuario().getId().equals(usuarioActual.getId()) || 
                usuarioActual.getRol().equals("PROFESOR") || 
                usuarioActual.getRol().equals("LIDER_EQUIPO")) {
                
                servicioComentario.eliminarComentario(id);
                redirectAttributes.addFlashAttribute("mensajeExito", "Comentario eliminado correctamente");

                if (comentario.esComentarioProyecto()) {
                    return "redirect:/proyectos/" + comentario.getProyecto().getId();
                } else {
                    return "redirect:/tareas/" + comentario.getTarea().getId();
                }
            }
            
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permiso para eliminar este comentario");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Comentario no encontrado");
        }
        

        return "redirect:/";
    }

    private Usuario obtenerUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return servicioUsuario.obtenerUsuarioPorNombreUsuario(auth.getName());
    }
}