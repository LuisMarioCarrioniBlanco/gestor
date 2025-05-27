package com.proyecto.gestor.controlador;

import com.proyecto.gestor.dto.RegistroDTO;
import com.proyecto.gestor.modelo.Usuario;
import com.proyecto.gestor.servicio.ServicioUsuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControladorAutenticacion {

    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorAutenticacion(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "autenticacion/login";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new RegistroDTO());
        return "autenticacion/registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@Valid @ModelAttribute("usuario") RegistroDTO registroDTO, 
                                 BindingResult result, Model model) {
        if (!registroDTO.getContrasena().equals(registroDTO.getConfirmarContrasena())) {
            result.rejectValue("confirmarContrasena", "", "Las contraseñas no coinciden");
        }
        
        if (servicioUsuario.existeUsuarioPorNombreUsuario(registroDTO.getNombreUsuario())) {
            result.rejectValue("nombreUsuario", "", "El nombre de usuario ya está en uso");
        }
        
        if (servicioUsuario.existeUsuarioPorCorreo(registroDTO.getCorreo())) {
            result.rejectValue("correo", "", "El correo electrónico ya está registrado");
        }
        
        if (result.hasErrors()) {
            return "autenticacion/registro";
        }
        
        servicioUsuario.registrarUsuario(registroDTO);
        
        return "redirect:/login?registroExitoso";
    }
}