package com.proyecto.gestor.servicio;

import com.proyecto.gestor.dto.RegistroDTO;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.repositorio.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioUsuario implements UserDetailsService {
    
    private final RepositorioUsuario repositorioUsuario;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public ServicioUsuario(RepositorioUsuario repositorioUsuario, PasswordEncoder passwordEncoder) {
        this.repositorioUsuario = repositorioUsuario;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repositorioUsuario.findByNombreUsuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));
        
        return new User(
            usuario.getNombreUsuario(), 
            usuario.getContrasena(), 
            usuario.isActivo(), 
            true, 
            true, 
            true, 
            authorities
        );
    }
    
    public List<Usuario> obtenerTodosLosUsuarios() {
        return repositorioUsuario.findAll();
    }
    
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return repositorioUsuario.save(usuario);
    }
    
    @Transactional
    public Usuario registrarUsuario(RegistroDTO registroDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(registroDTO.getNombreUsuario());
        usuario.setContrasena(passwordEncoder.encode(registroDTO.getContrasena()));
        usuario.setCorreo(registroDTO.getCorreo());
        usuario.setRol(registroDTO.getRol());
        usuario.setActivo(true);
        
        return repositorioUsuario.save(usuario);
    }
    
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return repositorioUsuario.findById(id);
    }
    
    public Usuario obtenerUsuarioPorNombreUsuario(String nombreUsuario) {
        return repositorioUsuario.findByNombreUsuario(nombreUsuario);
    }
    
    public boolean existeUsuarioPorNombreUsuario(String nombreUsuario) {
        return repositorioUsuario.findByNombreUsuario(nombreUsuario) != null;
    }
    
    public boolean existeUsuarioPorCorreo(String correo) {
        return repositorioUsuario.findByCorreo(correo) != null;
    }
}