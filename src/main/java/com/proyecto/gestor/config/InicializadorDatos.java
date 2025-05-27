package com.proyecto.gestor.config;

import com.proyecto.gestor.modelo.Proyecto;
import com.proyecto.gestor.modelo.Tarea;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.servicio.ServicioProyecto;
import com.proyecto.gestor.servicio.ServicioTarea;
import com.proyecto.gestor.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class InicializadorDatos implements CommandLineRunner {

    private final ServicioUsuario servicioUsuario;
    private final ServicioProyecto servicioProyecto;
    private final ServicioTarea servicioTarea;

    @Autowired
    public InicializadorDatos(ServicioUsuario servicioUsuario, ServicioProyecto servicioProyecto, ServicioTarea servicioTarea) {
        this.servicioUsuario = servicioUsuario;
        this.servicioProyecto = servicioProyecto;
        this.servicioTarea = servicioTarea;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Usuario usuario1 = new Usuario("admin", "admin123", "admin@example.com", "PROFESOR");
        Usuario usuario2 = new Usuario("lider", "lider123", "lider@example.com", "LIDER_EQUIPO");
        Usuario usuario3 = new Usuario("estudiante1", "estudiante123", "estudiante1@example.com", "ESTUDIANTE");
        Usuario usuario4 = new Usuario("estudiante2", "estudiante123", "estudiante2@example.com", "ESTUDIANTE");
        
        usuario1 = servicioUsuario.guardarUsuario(usuario1);
        usuario2 = servicioUsuario.guardarUsuario(usuario2);
        usuario3 = servicioUsuario.guardarUsuario(usuario3);
        usuario4 = servicioUsuario.guardarUsuario(usuario4);
        
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre("Proyecto de Ejemplo");
        proyecto.setDescripcion("Este es un proyecto de ejemplo para demostrar el funcionamiento del sistema.");
        proyecto.setFechaInicio(new Date());
        
        Date fechaFin = new Date();
        fechaFin.setTime(fechaFin.getTime() + 30L * 24 * 60 * 60 * 1000);
        proyecto.setFechaFin(fechaFin);
        
        proyecto.setLiderEquipo(usuario2);
        proyecto.agregarMiembro(usuario2);
        proyecto.agregarMiembro(usuario3);
        proyecto.agregarMiembro(usuario4);
        
        proyecto = servicioProyecto.guardarProyecto(proyecto);
        
        Tarea tarea1 = new Tarea();
        tarea1.setTitulo("Análisis de requisitos");
        tarea1.setDescripcion("Realizar análisis de los requisitos del proyecto.");
        tarea1.setEstado("PENDIENTE");
        tarea1.setAsignadoA(usuario2);
        tarea1.setProyecto(proyecto);
        tarea1 = servicioTarea.guardarTarea(tarea1);
        
        Tarea tarea2 = new Tarea();
        tarea2.setTitulo("Diseño de base de datos");
        tarea2.setDescripcion("Crear el modelo de base de datos para el proyecto.");
        tarea2.setEstado("PENDIENTE");
        tarea2.setAsignadoA(usuario3);
        tarea2.setProyecto(proyecto);
        tarea2 = servicioTarea.guardarTarea(tarea2);
        
        Tarea tarea3 = new Tarea();
        tarea3.setTitulo("Implementación de interfaz");
        tarea3.setDescripcion("Desarrollar la interfaz de usuario del proyecto.");
        tarea3.setEstado("PENDIENTE");
        tarea3.setAsignadoA(usuario4);
        tarea3.setProyecto(proyecto);
        tarea3 = servicioTarea.guardarTarea(tarea3);
        
        tarea2.agregarDependencia(tarea1);
        tarea3.agregarDependencia(tarea2);
        
        servicioTarea.guardarTarea(tarea2);
        servicioTarea.guardarTarea(tarea3);
    }
}