package com.proyecto.gestor.repositorio;

import com.proyecto.gestor.modelo.Proyecto;
import com.proyecto.gestor.modelo.Tarea;
import com.proyecto.gestor.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioTarea extends JpaRepository<Tarea, Long> {
    List<Tarea> findByProyecto(Proyecto proyecto);
    List<Tarea> findByAsignadoA(Usuario usuario);
}