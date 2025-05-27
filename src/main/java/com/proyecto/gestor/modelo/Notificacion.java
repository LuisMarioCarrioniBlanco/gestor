package com.proyecto.gestor.modelo;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Notificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    private String contenido;
    
    private String tipo;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    private boolean leida;
    
    private String url;
    
    public Notificacion() {
        this.fechaCreacion = new Date();
        this.leida = false;
    }
    
    public Notificacion(Usuario usuario, String contenido, String tipo) {
        this.usuario = usuario;
        this.contenido = contenido;
        this.tipo = tipo;
        this.fechaCreacion = new Date();
        this.leida = false;
    }
    
    public Notificacion(Usuario usuario, String contenido, String tipo, String url) {
        this.usuario = usuario;
        this.contenido = contenido;
        this.tipo = tipo;
        this.url = url;
        this.fechaCreacion = new Date();
        this.leida = false;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public boolean isLeida() {
        return leida;
    }
    
    public void setLeida(boolean leida) {
        this.leida = leida;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}