package com.proyecto.gestor.repositorio;

import com.proyecto.gestor.modelo.Mensaje;
import com.proyecto.gestor.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositorioMensaje extends JpaRepository<Mensaje, Long> {
    
    List<Mensaje> findByEmisorOrderByFechaEnvioDesc(Usuario emisor);
    
    List<Mensaje> findByReceptorOrderByFechaEnvioDesc(Usuario receptor);
    
    List<Mensaje> findByReceptorAndLeidoFalseOrderByFechaEnvioDesc(Usuario receptor);
    
    @Query("SELECT m FROM Mensaje m WHERE (m.emisor = :usuario1 AND m.receptor = :usuario2) OR (m.emisor = :usuario2 AND m.receptor = :usuario1) ORDER BY m.fechaEnvio DESC")
    List<Mensaje> findConversationBetweenUsers(Usuario usuario1, Usuario usuario2);
    
    long countByReceptorAndLeidoFalse(Usuario receptor);
}