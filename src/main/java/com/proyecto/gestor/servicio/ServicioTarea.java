package com.proyecto.gestor.servicio;

import com.proyecto.gestor.modelo.Proyecto;
import com.proyecto.gestor.modelo.Tarea;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.repositorio.RepositorioTarea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioTarea {
    
    private final RepositorioTarea repositorioTarea;
    
    @Autowired
    public ServicioTarea(RepositorioTarea repositorioTarea) {
        this.repositorioTarea = repositorioTarea;
    }
    
    public List<Tarea> obtenerTodasLasTareas() {
        return repositorioTarea.findAll();
    }
    
    @Transactional
    public Tarea guardarTarea(Tarea tarea) {
        return repositorioTarea.save(tarea);
    }
    
    public Optional<Tarea> obtenerTareaPorId(Long id) {
        return repositorioTarea.findById(id);
    }
    
    public List<Tarea> obtenerTareasPorProyecto(Proyecto proyecto) {
        return repositorioTarea.findByProyecto(proyecto);
    }
    
    public List<Tarea> obtenerTareasPorUsuarioAsignado(Usuario usuario) {
        return repositorioTarea.findByAsignadoA(usuario);
    }
}