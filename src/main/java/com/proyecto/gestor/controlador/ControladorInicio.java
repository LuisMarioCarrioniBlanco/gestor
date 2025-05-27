package com.proyecto.gestor.controlador;

import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.servicio.ServicioProyecto;
import com.proyecto.gestor.servicio.ServicioTarea;
import com.proyecto.gestor.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorInicio {
    
    private final ServicioProyecto servicioProyecto;
    private final ServicioUsuario servicioUsuario;
    private final ServicioTarea servicioTarea;
    
    @Autowired
    public ControladorInicio(ServicioProyecto servicioProyecto, ServicioUsuario servicioUsuario, ServicioTarea servicioTarea) {
        this.servicioProyecto = servicioProyecto;
        this.servicioUsuario = servicioUsuario;
        this.servicioTarea = servicioTarea;
    }
    
    @GetMapping("/")
    public String inicio(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            Usuario usuario = servicioUsuario.obtenerUsuarioPorNombreUsuario(auth.getName());
            model.addAttribute("usuarioActual", usuario);
            
            if (usuario.getRol().equals("PROFESOR") || usuario.getRol().equals("LIDER_EQUIPO")) {
                model.addAttribute("proyectos", servicioProyecto.obtenerTodosLosProyectos());
            } else {
                model.addAttribute("proyectos", servicioProyecto.obtenerProyectosPorMiembro(usuario));
            }
            
            model.addAttribute("tareas", servicioTarea.obtenerTareasPorUsuarioAsignado(usuario));
        } else {
            model.addAttribute("proyectos", servicioProyecto.obtenerTodosLosProyectos());
        }
        
        model.addAttribute("usuarios", servicioUsuario.obtenerTodosLosUsuarios());
        model.addAttribute("totalTareas", servicioTarea.obtenerTodasLasTareas().size());
        
        return "inicio";
    }
}