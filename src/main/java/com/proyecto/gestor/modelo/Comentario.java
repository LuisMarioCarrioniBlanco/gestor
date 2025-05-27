package com.proyecto.gestor.modelo;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Comentario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;
    
    @ManyToOne
    @JoinColumn(name = "tarea_id")
    private Tarea tarea;
    
    @Column(columnDefinition = "TEXT")
    private String contenido;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    public Comentario() {
        this.fechaCreacion = new Date();
    }
    
    public Comentario(Usuario usuario, Proyecto proyecto, String contenido) {
        this.usuario = usuario;
        this.proyecto = proyecto;
        this.contenido = contenido;
        this.fechaCreacion = new Date();
    }
    
    public Comentario(Usuario usuario, Tarea tarea, String contenido) {
        this.usuario = usuario;
        this.tarea = tarea;
        this.contenido = contenido;
        this.fechaCreacion = new Date();
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
    
    public Proyecto getProyecto() {
        return proyecto;
    }
    
    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
    
    public Tarea getTarea() {
        return tarea;
    }
    
    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public boolean esComentarioProyecto() {
        return this.proyecto != null;
    }
    
    public boolean esComentarioTarea() {
        return this.tarea != null;
    }
}