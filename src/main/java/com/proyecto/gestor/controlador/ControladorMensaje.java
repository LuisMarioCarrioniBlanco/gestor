package com.proyecto.gestor.controlador;

import com.proyecto.gestor.modelo.Mensaje;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.servicio.ServicioMensaje;
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
@RequestMapping("/mensajes")
public class ControladorMensaje {

    private final ServicioMensaje servicioMensaje;
    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorMensaje(ServicioMensaje servicioMensaje, ServicioUsuario servicioUsuario) {
        this.servicioMensaje = servicioMensaje;
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping
    public String mostrarBandejaEntrada(Model model) {
        Usuario usuarioActual = obtenerUsuarioActual();
        List<Mensaje> mensajesRecibidos = servicioMensaje.obtenerMensajesRecibidos(usuarioActual);
        
        model.addAttribute("mensajes", mensajesRecibidos);
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("tipoMensaje", "recibidos");
        
        return "mensaje/bandeja";
    }

    @GetMapping("/enviados")
    public String mostrarMensajesEnviados(Model model) {
        Usuario usuarioActual = obtenerUsuarioActual();
        List<Mensaje> mensajesEnviados = servicioMensaje.obtenerMensajesEnviados(usuarioActual);
        
        model.addAttribute("mensajes", mensajesEnviados);
        model.addAttribute("usuario", usuarioActual);
        model.addAttribute("tipoMensaje", "enviados");
        
        return "mensaje/bandeja";
    }

    @GetMapping("/nuevo")
    public String formularioNuevoMensaje(Model model, @RequestParam(required = false) Long receptorId) {
        model.addAttribute("mensaje", new Mensaje());
        model.addAttribute("usuarios", servicioUsuario.obtenerTodosLosUsuarios());
        
        if (receptorId != null) {
            servicioUsuario.obtenerUsuarioPorId(receptorId).ifPresent(receptor -> {
                model.addAttribute("receptorPreseleccionado", receptor);
            });
        }
        
        return "mensaje/formulario";
    }

    @PostMapping
    public String enviarMensaje(@RequestParam Long receptorId, 
                               @RequestParam String asunto,
                               @RequestParam String contenido,
                               RedirectAttributes redirectAttributes) {
        Usuario usuarioActual = obtenerUsuarioActual();
        Optional<Usuario> optReceptor = servicioUsuario.obtenerUsuarioPorId(receptorId);
        
        if (optReceptor.isPresent()) {
            servicioMensaje.enviarMensaje(usuarioActual, optReceptor.get(), asunto, contenido);
            redirectAttributes.addFlashAttribute("mensajeExito", "Mensaje enviado correctamente");
            return "redirect:/mensajes/enviados";
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "Error al enviar el mensaje: usuario no encontrado");
            return "redirect:/mensajes/nuevo";
        }
    }

    @GetMapping("/{id}")
    public String verMensaje(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuarioActual = obtenerUsuarioActual();
        Optional<Mensaje> optMensaje = servicioMensaje.obtenerMensajePorId(id);
        
        if (optMensaje.isPresent()) {
            Mensaje mensaje = optMensaje.get();

            if (mensaje.getEmisor().getId().equals(usuarioActual.getId()) || 
                mensaje.getReceptor().getId().equals(usuarioActual.getId())) {

                if (mensaje.getReceptor().getId().equals(usuarioActual.getId()) && !mensaje.isLeido()) {
                    servicioMensaje.marcarComoLeido(id);
                }
                
                model.addAttribute("mensaje", mensaje);
                model.addAttribute("esReceptor", mensaje.getReceptor().getId().equals(usuarioActual.getId()));
                
                return "mensaje/ver";
            }
        }
        
        redirectAttributes.addFlashAttribute("mensajeError", "No tienes permiso para ver este mensaje o no existe");
        return "redirect:/mensajes";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarMensaje(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuarioActual = obtenerUsuarioActual();
        Optional<Mensaje> optMensaje = servicioMensaje.obtenerMensajePorId(id);
        
        if (optMensaje.isPresent()) {
            Mensaje mensaje = optMensaje.get();
            
            if (mensaje.getEmisor().getId().equals(usuarioActual.getId()) || 
                mensaje.getReceptor().getId().equals(usuarioActual.getId())) {
                
                servicioMensaje.eliminarMensaje(id);
                redirectAttributes.addFlashAttribute("mensajeExito", "Mensaje eliminado correctamente");
                
                if (mensaje.getReceptor().getId().equals(usuarioActual.getId())) {
                    return "redirect:/mensajes";
                } else {
                    return "redirect:/mensajes/enviados";
                }
            }
        }
        
        redirectAttributes.addFlashAttribute("mensajeError", "No tienes permiso para eliminar este mensaje o no existe");
        return "redirect:/mensajes";
    }
    
    private Usuario obtenerUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return servicioUsuario.obtenerUsuarioPorNombreUsuario(auth.getName());
    }
}