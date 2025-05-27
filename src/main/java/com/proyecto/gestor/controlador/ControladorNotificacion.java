package com.proyecto.gestor.controlador;

import com.proyecto.gestor.modelo.Notificacion;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.servicio.ServicioNotificacion;
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
@RequestMapping("/notificaciones")
public class ControladorNotificacion {

    private final ServicioNotificacion servicioNotificacion;
    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorNotificacion(ServicioNotificacion servicioNotificacion, ServicioUsuario servicioUsuario) {
        this.servicioNotificacion = servicioNotificacion;
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping
    public String verNotificaciones(Model model) {
        Usuario usuarioActual = obtenerUsuarioActual();
        List<Notificacion> notificaciones = servicioNotificacion.obtenerNotificacionesUsuario(usuarioActual);
        
        model.addAttribute("notificaciones", notificaciones);
        
        return "notificacion/lista";
    }

    @PostMapping("/{id}/leer")
    public String marcarComoLeida(@PathVariable Long id, @RequestParam(required = false) String redireccion) {
        servicioNotificacion.marcarComoLeida(id);

        if (redireccion != null && !redireccion.isEmpty()) {
            return "redirect:" + redireccion;
        }

        return "redirect:/notificaciones";
    }

    @PostMapping("/marcar-todas-leidas")
    public String marcarTodasLeidas(RedirectAttributes redirectAttributes) {
        Usuario usuarioActual = obtenerUsuarioActual();
        servicioNotificacion.marcarTodasComoLeidas(usuarioActual);
        
        redirectAttributes.addFlashAttribute("mensajeExito", "Todas las notificaciones han sido marcadas como leídas");
        return "redirect:/notificaciones";
    }

    @GetMapping("/{id}")
    public String verNotificacion(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuarioActual = obtenerUsuarioActual();
        Optional<Notificacion> optNotificacion = servicioNotificacion.obtenerNotificacionPorId(id);
        
        if (optNotificacion.isPresent()) {
            Notificacion notificacion = optNotificacion.get();

            if (notificacion.getUsuario().getId().equals(usuarioActual.getId())) {
                if (!notificacion.isLeida()) {
                    servicioNotificacion.marcarComoLeida(id);
                }

                if (notificacion.getUrl() != null && !notificacion.getUrl().isEmpty()) {
                    return "redirect:" + notificacion.getUrl();
                }
                
                model.addAttribute("notificacion", notificacion);
                return "notificacion/ver";
            }
        }
        
        redirectAttributes.addFlashAttribute("mensajeError", "Notificación no encontrada o no tienes permiso para verla");
        return "redirect:/notificaciones";
    }

    private Usuario obtenerUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return servicioUsuario.obtenerUsuarioPorNombreUsuario(auth.getName());
    }
}