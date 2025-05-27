package com.proyecto.gestor.controlador;

import com.proyecto.gestor.modelo.Comentario;
import com.proyecto.gestor.modelo.Proyecto;
import com.proyecto.gestor.modelo.Tarea;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.servicio.ServicioComentario;
import com.proyecto.gestor.servicio.ServicioTarea;
import com.proyecto.gestor.servicio.ServicioUsuario;
import com.proyecto.gestor.servicio.ServicioProyecto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/tareas")
public class ControladorTarea {

    private final ServicioTarea servicioTarea;
    private final ServicioUsuario servicioUsuario;
    private final ServicioComentario servicioComentario;
    private final ServicioProyecto servicioProyecto;

    @Autowired
    public ControladorTarea(ServicioTarea servicioTarea, 
                           ServicioUsuario servicioUsuario,
                           ServicioComentario servicioComentario,
                           ServicioProyecto servicioProyecto) {
        this.servicioTarea = servicioTarea;
        this.servicioUsuario = servicioUsuario;
        this.servicioComentario = servicioComentario;
        this.servicioProyecto = servicioProyecto;
    }
    
    @GetMapping
    public String listarTareas(Model model) {
        model.addAttribute("tareas", servicioTarea.obtenerTodasLasTareas());
        return "tarea/lista";
    }
    
    @GetMapping("/proyecto/{idProyecto}")
    public String listarTareasPorProyecto(@PathVariable("idProyecto") Long idProyecto, Model model) {
        Proyecto proyecto = servicioProyecto.obtenerProyectoPorId(idProyecto)
                .orElseThrow(() -> new IllegalArgumentException("ID de proyecto no válido: " + idProyecto));
        model.addAttribute("tareas", servicioTarea.obtenerTareasPorProyecto(proyecto));
        model.addAttribute("proyecto", proyecto);
        return "tarea/lista";
    }
    
    @GetMapping("/nueva")
    public String formularioNuevaTarea(Model model) {
        model.addAttribute("tarea", new Tarea());
        model.addAttribute("proyectos", servicioProyecto.obtenerTodosLosProyectos());
        model.addAttribute("usuarios", servicioUsuario.obtenerTodosLosUsuarios());
        return "tarea/formulario";
    }
    
    @GetMapping("/proyecto/{idProyecto}/nueva")
    public String formularioNuevaTareaParaProyecto(@PathVariable("idProyecto") Long idProyecto, Model model) {
        Proyecto proyecto = servicioProyecto.obtenerProyectoPorId(idProyecto)
                .orElseThrow(() -> new IllegalArgumentException("ID de proyecto no válido: " + idProyecto));
        
        Tarea tarea = new Tarea();
        tarea.setProyecto(proyecto);
        
        model.addAttribute("tarea", tarea);
        model.addAttribute("proyectos", servicioProyecto.obtenerTodosLosProyectos());
        model.addAttribute("usuarios", servicioUsuario.obtenerTodosLosUsuarios());
        
        return "tarea/formulario";
    }
    
    @PostMapping
    public String guardarTarea(@ModelAttribute("tarea") Tarea tarea) {
        servicioTarea.guardarTarea(tarea);
        return "redirect:/tareas/proyecto/" + tarea.getProyecto().getId();
    }
    
    @GetMapping("/{id}")
    public String verTarea(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Tarea> optTarea = servicioTarea.obtenerTareaPorId(id);
        
        if (optTarea.isPresent()) {
            Tarea tarea = optTarea.get();
            model.addAttribute("tarea", tarea);

            model.addAttribute("comentarios", servicioComentario.obtenerComentariosTarea(tarea));
            model.addAttribute("nuevoComentario", new Comentario());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuarioActual = servicioUsuario.obtenerUsuarioPorNombreUsuario(auth.getName());
            model.addAttribute("usuarioActual", usuarioActual);
            
            return "tarea/ver";
        }
        
        redirectAttributes.addFlashAttribute("mensajeError", "La tarea no existe");
        return "redirect:/tareas";
    }
    
    @GetMapping("/{id}/editar")
    public String formularioEditarTarea(@PathVariable("id") Long id, Model model) {
        Tarea tarea = servicioTarea.obtenerTareaPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de tarea no válido: " + id));
        model.addAttribute("tarea", tarea);
        model.addAttribute("proyectos", servicioProyecto.obtenerTodosLosProyectos());
        model.addAttribute("usuarios", servicioUsuario.obtenerTodosLosUsuarios());
        return "tarea/formulario";
    }
}