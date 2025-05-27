package com.proyecto.gestor.util;

import com.proyecto.gestor.modelo.Tarea;

import java.util.*;

public class EstructuraGrafo {
    private Map<Long, List<Long>> listaAdyacencia = new HashMap<>();
    
    public void agregarTarea(Tarea tarea) {
        listaAdyacencia.putIfAbsent(tarea.getId(), new ArrayList<>());
    }
    
    public void agregarDependencia(Tarea tarea, Tarea dependencia) {
        if (!listaAdyacencia.containsKey(tarea.getId())) {
            agregarTarea(tarea);
        }
        
        if (!listaAdyacencia.containsKey(dependencia.getId())) {
            agregarTarea(dependencia);
        }
        
        listaAdyacencia.get(tarea.getId()).add(dependencia.getId());
    }
    
    public List<Long> obtenerDependencias(Tarea tarea) {
        return listaAdyacencia.getOrDefault(tarea.getId(), Collections.emptyList());
    }
    
    // Método para verificar dependencias cíclicas usando DFS
    public boolean tieneDependenciaCiclica() {
        Set<Long> visitados = new HashSet<>();
        Set<Long> enProceso = new HashSet<>();
        
        for (Long tareaId : listaAdyacencia.keySet()) {
            if (!visitados.contains(tareaId)) {
                if (esCiclicoUtil(tareaId, visitados, enProceso)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean esCiclicoUtil(Long tareaId, Set<Long> visitados, Set<Long> enProceso) {
        if (enProceso.contains(tareaId)) {
            return true;
        }
        
        if (visitados.contains(tareaId)) {
            return false;
        }
        
        visitados.add(tareaId);
        enProceso.add(tareaId);
        
        for (Long dependenciaId : listaAdyacencia.getOrDefault(tareaId, Collections.emptyList())) {
            if (esCiclicoUtil(dependenciaId, visitados, enProceso)) {
                return true;
            }
        }
        
        enProceso.remove(tareaId);
        return false;
    }
}