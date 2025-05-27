package com.proyecto.gestor.modelo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titulo;
    private String descripcion;
    private String estado;
    
    @ManyToOne
    private Usuario asignadoA;
    
    @ManyToOne
    private Proyecto proyecto;
    
    @ManyToMany
    private List<Tarea> dependencias = new ArrayList<>();
    
    // Constructores
    public Tarea() {}
    
    public Tarea(String titulo, String descripcion, String estado, Usuario asignadoA, Proyecto proyecto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.asignadoA = asignadoA;
        this.proyecto = proyecto;
    }
    
    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public Usuario getAsignadoA() { return asignadoA; }
    public void setAsignadoA(Usuario asignadoA) { this.asignadoA = asignadoA; }
    
    public Proyecto getProyecto() { return proyecto; }
    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }
    
    public List<Tarea> getDependencias() { return dependencias; }
    public void setDependencias(List<Tarea> dependencias) { this.dependencias = dependencias; }
    
    // Métodos de ayuda
    public void agregarDependencia(Tarea tarea) {
        this.dependencias.add(tarea);
    }
}