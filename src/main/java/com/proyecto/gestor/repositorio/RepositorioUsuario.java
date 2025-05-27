package com.proyecto.gestor.repositorio;

import com.proyecto.gestor.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
    Usuario findByNombreUsuario(String nombreUsuario);
    Usuario findByCorreo(String correo);
}