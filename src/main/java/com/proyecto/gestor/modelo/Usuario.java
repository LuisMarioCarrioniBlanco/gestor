package com.proyecto.gestor.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String nombreUsuario;
    
    private String contrasena;
    
    @Column(unique = true)
    private String correo;
    
    private String rol;
    
    private boolean activo = true;
    
    @OneToMany(mappedBy = "emisor", cascade = CascadeType.ALL)
    private List<Mensaje> mensajesEnviados = new ArrayList<>();
    
    @OneToMany(mappedBy = "receptor", cascade = CascadeType.ALL)
    private List<Mensaje> mensajesRecibidos = new ArrayList<>();
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Notificacion> notificaciones = new ArrayList<>();
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Comentario> comentarios = new ArrayList<>();
    
    public Usuario() {}
    
    public Usuario(String nombreUsuario, String contrasena, String correo, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.correo = correo;
        this.rol = rol;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    
    public List<Mensaje> getMensajesEnviados() {
        return mensajesEnviados;
    }
    
    public void setMensajesEnviados(List<Mensaje> mensajesEnviados) {
        this.mensajesEnviados = mensajesEnviados;
    }
    
    public List<Mensaje> getMensajesRecibidos() {
        return mensajesRecibidos;
    }
    
    public void setMensajesRecibidos(List<Mensaje> mensajesRecibidos) {
        this.mensajesRecibidos = mensajesRecibidos;
    }
    
    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }
    
    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }
    
    public List<Comentario> getComentarios() {
        return comentarios;
    }
    
    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
    
    public int getNumeroMensajesNoLeidos() {
        return (int) mensajesRecibidos.stream()
            .filter(m -> !m.isLeido())
            .count();
    }
    
    public int getNumeroNotificacionesNoLeidas() {
        return (int) notificaciones.stream()
            .filter(n -> !n.isLeida())
            .count();
    }
}