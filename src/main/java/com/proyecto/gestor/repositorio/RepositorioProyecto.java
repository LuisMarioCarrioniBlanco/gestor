package com.proyecto.gestor.repositorio;

import com.proyecto.gestor.modelo.Proyecto;
import com.proyecto.gestor.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioProyecto extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByLiderEquipo(Usuario liderEquipo);
    List<Proyecto> findByMiembrosContaining(Usuario miembro);
}