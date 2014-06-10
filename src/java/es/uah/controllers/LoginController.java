/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uah.controllers;

import es.uah.beans.Clientes;
import es.uah.beans.ClientesFacade;
import es.uah.beans.Usuarios;
import es.uah.beans.UsuariosFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author marlonvinan
 */
@Named("loginController")
@SessionScoped
public class LoginController implements Serializable {

    private Usuarios usuario;
    @EJB
    private UsuariosFacade usuarioFacade;
    @EJB
    private ClientesFacade clientesFacade;
    
    private static Usuarios usuarioGlobal;
    
    
    
    public String ingresar() {
        try{
            usuarioGlobal = getUsuarioFacade().findByUserName(getUsuario().getUsername());
        }catch(EJBException ex){
            usuarioGlobal = null;
            System.err.println("no encontrado");
        }
        String page = "";
        if (usuarioGlobal != null) {
            if (usuarioGlobal.getPassword().equals(getUsuario().getPassword())) {
                
                if (usuarioGlobal.getRolename().equals("admin")) {
                    page = "/index.xhtml";
                } else {
                    Clientes c = getClientesFacade().findByUsuario(usuarioGlobal);
                    if(c != null){
                        page = "/comprasCliente/comprasCliente.xhtml";
                    }else{
                        FacesContext context = FacesContext.getCurrentInstance();  
                        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Datos del cliente no están creados aún.!!")); 
                    }
                }
                System.out.println("usuario autenticado");
            } else {
                FacesContext context = FacesContext.getCurrentInstance();  
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Usuario/Password no válido")); 
            
            }
        } else {
            FacesContext context = FacesContext.getCurrentInstance();  
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Usuario no encontrado")); 
            //System.out.println("Usuario no encontrado");
        }

        return page;
    }

    public Usuarios getUsuario() {
        if (usuario == null) {
            usuario = new Usuarios();
        }
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public UsuariosFacade getUsuarioFacade() {
        return usuarioFacade;
    }

    public void setUsuarioFacade(UsuariosFacade usuarioFacade) {
        this.usuarioFacade = usuarioFacade;
    }

    public static Usuarios getUsuarioGlobal() {
        return usuarioGlobal;
    }

    public static void setUsuarioGlobal(Usuarios usuarioGlobal) {
        LoginController.usuarioGlobal = usuarioGlobal;
    }

    public ClientesFacade getClientesFacade() {
        return clientesFacade;
    }

    public void setClientesFacade(ClientesFacade clientesFacade) {
        this.clientesFacade = clientesFacade;
    }

    
    

}
