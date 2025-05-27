package com.proyecto.gestor.servicio;

import com.proyecto.gestor.modelo.Notificacion;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.repositorio.RepositorioNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioNotificacion {

    private final RepositorioNotificacion repositorioNotificacion;

    @Autowired
    public ServicioNotificacion(RepositorioNotificacion repositorioNotificacion) {
        this.repositorioNotificacion = repositorioNotificacion;
    }

    public List<Notificacion> obtenerNotificacionesUsuario(Usuario usuario) {
        return repositorioNotificacion.findByUsuarioOrderByFechaCreacionDesc(usuario);
    }

    public List<Notificacion> obtenerNotificacionesNoLeidas(Usuario usuario) {
        return repositorioNotificacion.findByUsuarioAndLeidaFalseOrderByFechaCreacionDesc(usuario);
    }

    @Transactional
    public Notificacion marcarComoLeida(Long notificacionId) {
        Optional<Notificacion> optNotificacion = repositorioNotificacion.findById(notificacionId);
        if (optNotificacion.isPresent()) {
            Notificacion notificacion = optNotificacion.get();
            notificacion.setLeida(true);
            return repositorioNotificacion.save(notificacion);
        }
        return null;
    }
    
    @Transactional
    public void marcarTodasComoLeidas(Usuario usuario) {
        List<Notificacion> notificaciones = obtenerNotificacionesNoLeidas(usuario);
        notificaciones.forEach(n -> n.setLeida(true));
        repositorioNotificacion.saveAll(notificaciones);
    }

    public Optional<Notificacion> obtenerNotificacionPorId(Long id) {
        return repositorioNotificacion.findById(id);
    }
    
    public long contarNotificacionesNoLeidas(Usuario usuario) {
        return repositorioNotificacion.countByUsuarioAndLeidaFalse(usuario);
    }
    
    @Transactional
    public Notificacion crearNotificacionSistema(Usuario usuario, String contenido) {
        Notificacion notificacion = new Notificacion(usuario, contenido, "SISTEMA");
        return repositorioNotificacion.save(notificacion);
    }
}