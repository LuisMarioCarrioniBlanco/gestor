package com.proyecto.gestor.repositorio;

import com.proyecto.gestor.modelo.Notificacion;
import com.proyecto.gestor.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioNotificacion extends JpaRepository<Notificacion, Long> {
    
    List<Notificacion> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
    
    List<Notificacion> findByUsuarioAndLeidaFalseOrderByFechaCreacionDesc(Usuario usuario);
    
    long countByUsuarioAndLeidaFalse(Usuario usuario);
}