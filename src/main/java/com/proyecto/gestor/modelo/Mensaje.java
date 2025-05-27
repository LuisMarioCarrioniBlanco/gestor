package com.proyecto.gestor.modelo;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "emisor_id")
    private Usuario emisor;
    
    @ManyToOne
    @JoinColumn(name = "receptor_id")
    private Usuario receptor;
    
    private String asunto;
    
    @Column(columnDefinition = "TEXT")
    private String contenido;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEnvio;
    
    private boolean leido;
    
    public Mensaje() {
        this.fechaEnvio = new Date();
        this.leido = false;
    }
    
    public Mensaje(Usuario emisor, Usuario receptor, String asunto, String contenido) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.asunto = asunto;
        this.contenido = contenido;
        this.fechaEnvio = new Date();
        this.leido = false;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getEmisor() {
        return emisor;
    }
    
    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }
    
    public Usuario getReceptor() {
        return receptor;
    }
    
    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }
    
    public String getAsunto() {
        return asunto;
    }
    
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public Date getFechaEnvio() {
        return fechaEnvio;
    }
    
    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
    
    public boolean isLeido() {
        return leido;
    }
    
    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}