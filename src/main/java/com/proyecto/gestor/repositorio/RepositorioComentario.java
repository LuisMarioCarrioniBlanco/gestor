package com.proyecto.gestor.repositorio;

import com.proyecto.gestor.modelo.Comentario;
import com.proyecto.gestor.modelo.Proyecto;
import com.proyecto.gestor.modelo.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioComentario extends JpaRepository<Comentario, Long> {
    
    List<Comentario> findByProyectoOrderByFechaCreacionDesc(Proyecto proyecto);
    
    List<Comentario> findByTareaOrderByFechaCreacionDesc(Tarea tarea);
}