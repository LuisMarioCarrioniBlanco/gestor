package com.proyecto.gestor.servicio;

import com.proyecto.gestor.modelo.Mensaje;
import com.proyecto.gestor.modelo.Notificacion;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.repositorio.RepositorioMensaje;
import com.proyecto.gestor.repositorio.RepositorioNotificacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioMensaje {

    private final RepositorioMensaje repositorioMensaje;
    private final RepositorioNotificacion repositorioNotificacion;

    @Autowired
    public ServicioMensaje(RepositorioMensaje repositorioMensaje, RepositorioNotificacion repositorioNotificacion) {
        this.repositorioMensaje = repositorioMensaje;
        this.repositorioNotificacion = repositorioNotificacion;
    }

    @Transactional
    public Mensaje enviarMensaje(Usuario emisor, Usuario receptor, String asunto, String contenido) {
        Mensaje mensaje = new Mensaje(emisor, receptor, asunto, contenido);
        mensaje.setFechaEnvio(new Date());
        
        Mensaje mensajeGuardado = repositorioMensaje.save(mensaje);
        
        Notificacion notificacion = new Notificacion(
            receptor, 
            emisor.getNombreUsuario() + " te ha enviado un mensaje: " + asunto,
            "MENSAJE",
            "/mensajes/" + mensajeGuardado.getId()
        );
        repositorioNotificacion.save(notificacion);
        
        return mensajeGuardado;
    }

    public List<Mensaje> obtenerMensajesRecibidos(Usuario usuario) {
        return repositorioMensaje.findByReceptorOrderByFechaEnvioDesc(usuario);
    }

    public List<Mensaje> obtenerMensajesEnviados(Usuario usuario) {
        return repositorioMensaje.findByEmisorOrderByFechaEnvioDesc(usuario);
    }

    public List<Mensaje> obtenerMensajesNoLeidos(Usuario usuario) {
        return repositorioMensaje.findByReceptorAndLeidoFalseOrderByFechaEnvioDesc(usuario);
    }

    @Transactional
    public Mensaje marcarComoLeido(Long mensajeId) {
        Optional<Mensaje> optMensaje = repositorioMensaje.findById(mensajeId);
        if (optMensaje.isPresent()) {
            Mensaje mensaje = optMensaje.get();
            mensaje.setLeido(true);
            return repositorioMensaje.save(mensaje);
        }
        return null;
    }

    public Optional<Mensaje> obtenerMensajePorId(Long id) {
        return repositorioMensaje.findById(id);
    }
    
    public List<Mensaje> obtenerConversacion(Usuario usuario1, Usuario usuario2) {
        return repositorioMensaje.findConversationBetweenUsers(usuario1, usuario2);
    }
    
    public long contarMensajesNoLeidos(Usuario usuario) {
        return repositorioMensaje.countByReceptorAndLeidoFalse(usuario);
    }
    
    @Transactional
    public void eliminarMensaje(Long mensajeId) {
        repositorioMensaje.deleteById(mensajeId);
    }
}