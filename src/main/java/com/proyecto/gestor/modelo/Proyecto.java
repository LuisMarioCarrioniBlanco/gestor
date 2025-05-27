package com.proyecto.gestor.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String descripcion;
    
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    
    @ManyToMany
    private List<Usuario> miembros = new ArrayList<>();
    
    @ManyToOne
    private Usuario liderEquipo;
    
    // Constructores
    public Proyecto() {}
    
    public Proyecto(String nombre, String descripcion, Date fechaInicio, Date fechaFin, Usuario liderEquipo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.liderEquipo = liderEquipo;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    
    public List<Usuario> getMiembros() { return miembros; }
    public void setMiembros(List<Usuario> miembros) { this.miembros = miembros; }
    
    public Usuario getLiderEquipo() { return liderEquipo; }
    public void setLiderEquipo(Usuario liderEquipo) { this.liderEquipo = liderEquipo; }
    
    // Métodos de ayuda
    public void agregarMiembro(Usuario usuario) {
        this.miembros.add(usuario);
    }
}