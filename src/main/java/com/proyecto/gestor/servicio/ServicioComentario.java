package com.proyecto.gestor.servicio;

import com.proyecto.gestor.modelo.Comentario;
import com.proyecto.gestor.modelo.Notificacion;
import com.proyecto.gestor.modelo.Proyecto;
import com.proyecto.gestor.modelo.Tarea;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.repositorio.RepositorioComentario;
import com.proyecto.gestor.repositorio.RepositorioNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioComentario {

    private final RepositorioComentario repositorioComentario;
    private final RepositorioNotificacion repositorioNotificacion;

    @Autowired
    public ServicioComentario(RepositorioComentario repositorioComentario, RepositorioNotificacion repositorioNotificacion) {
        this.repositorioComentario = repositorioComentario;
        this.repositorioNotificacion = repositorioNotificacion;
    }

    @Transactional
    public Comentario crearComentarioProyecto(Usuario usuario, Proyecto proyecto, String contenido) {
        Comentario comentario = new Comentario(usuario, proyecto, contenido);
        Comentario comentarioGuardado = repositorioComentario.save(comentario);
        
        for (Usuario miembro : proyecto.getMiembros()) {
            if (!miembro.getId().equals(usuario.getId())) {
                Notificacion notificacion = new Notificacion(
                    miembro,
                    usuario.getNombreUsuario() + " ha comentado en el proyecto " + proyecto.getNombre(),
                    "COMENTARIO",
                    "/proyectos/" + proyecto.getId()
                );
                repositorioNotificacion.save(notificacion);
            }
        }
        
        return comentarioGuardado;
    }

    @Transactional
    public Comentario crearComentarioTarea(Usuario usuario, Tarea tarea, String contenido) {
        Comentario comentario = new Comentario(usuario, tarea, contenido);
        Comentario comentarioGuardado = repositorioComentario.save(comentario);
        
        Usuario asignado = tarea.getAsignadoA();
        if (asignado != null && !asignado.getId().equals(usuario.getId())) {
            Notificacion notificacion = new Notificacion(
                asignado,
                usuario.getNombreUsuario() + " ha comentado en la tarea " + tarea.getTitulo(),
                "COMENTARIO",
                "/tareas/" + tarea.getId()
            );
            repositorioNotificacion.save(notificacion);
        }
        
        return comentarioGuardado;
    }

    public List<Comentario> obtenerComentariosProyecto(Proyecto proyecto) {
        return repositorioComentario.findByProyectoOrderByFechaCreacionDesc(proyecto);
    }

    public List<Comentario> obtenerComentariosTarea(Tarea tarea) {
        return repositorioComentario.findByTareaOrderByFechaCreacionDesc(tarea);
    }

    public Optional<Comentario> obtenerComentarioPorId(Long id) {
        return repositorioComentario.findById(id);
    }
    
    @Transactional
    public void eliminarComentario(Long comentarioId) {
        repositorioComentario.deleteById(comentarioId);
    }
}