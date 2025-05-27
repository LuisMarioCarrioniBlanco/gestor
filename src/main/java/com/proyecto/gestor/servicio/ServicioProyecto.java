package com.proyecto.gestor.servicio;

import com.proyecto.gestor.modelo.Proyecto;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.repositorio.RepositorioProyecto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioProyecto {
    
    private final RepositorioProyecto repositorioProyecto;
    
    @Autowired
    public ServicioProyecto(RepositorioProyecto repositorioProyecto) {
        this.repositorioProyecto = repositorioProyecto;
    }
    
    public List<Proyecto> obtenerTodosLosProyectos() {
        return repositorioProyecto.findAll();
    }
    
    public Proyecto guardarProyecto(Proyecto proyecto) {
        return repositorioProyecto.save(proyecto);
    }
    
    public Optional<Proyecto> obtenerProyectoPorId(Long id) {
        return repositorioProyecto.findById(id);
    }
    
    public List<Proyecto> obtenerProyectosPorLiderEquipo(Usuario usuario) {
        return repositorioProyecto.findByLiderEquipo(usuario);
    }
    
    public List<Proyecto> obtenerProyectosPorMiembro(Usuario usuario) {
        return repositorioProyecto.findByMiembrosContaining(usuario);
    }
}