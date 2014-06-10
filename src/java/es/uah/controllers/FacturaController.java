/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uah.controllers;

import es.uah.beans.Clientes;
import es.uah.beans.ClientesFacade;
import es.uah.beans.Compras;
import es.uah.beans.ComprasProductos;
import es.uah.beans.ComprasProductosFacade;
import es.uah.beans.Productos;
import es.uah.beans.ProductosFacade;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author utpl
 */
@Named("facturaController")
@SessionScoped
public class FacturaController implements Serializable{
    
    private int cantidadSeleccionada;
    @EJB
    private es.uah.beans.ComprasFacade comprasFacade;
    @EJB
    private ComprasProductosFacade comprasProductoFacade;
    @EJB
    private ClientesFacade clientesFacade;
    @EJB
    private ProductosFacade productosFacade;
    
    private Clientes cliente;
    private Compras compras;
    
    public FacturaController() {
    }

    public int getCantidadSeleccionada() {
        return cantidadSeleccionada;
    }

    public void setCantidadSeleccionada(int cantidadSeleccionada) {
        this.cantidadSeleccionada = cantidadSeleccionada;
    }
    
    public String verDetalle(){
        if(cantidadSeleccionada <= ProductosActivosController.getProductoSeleccionado().getCantidad() && ProductosActivosController.getProductoSeleccionado().getCantidad() > 0){
            if(cantidadSeleccionada > 0){
                compras = new Compras();
                compras.setFecha(new Date());
                compras.setIva(12);
                double subtotal = ProductosActivosController.getProductoSeleccionado().getPrecio()*cantidadSeleccionada;
                compras.setSubtotal(subtotal);
                double iva = (subtotal*12)/100;
                compras.setTotal(subtotal+iva);
                RequestContext.getCurrentInstance().execute("dlgd.show()");
            }else{
                FacesContext context = FacesContext.getCurrentInstance();  
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Debe ingresar La cantidad que desea comprar")); 
            }
        }else{
            System.out.println("entra error");
            FacesContext context = FacesContext.getCurrentInstance();  
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Cantidad no disponible en Stock")); 
        }
        
        return "";
    }
    
    public String comprar(){
        if(cantidadSeleccionada <= ProductosActivosController.getProductoSeleccionado().getCantidad() && ProductosActivosController.getProductoSeleccionado().getCantidad() > 0){
            if(cantidadSeleccionada > 0){
                compras = new Compras();
                compras.setFecha(new Date());
                compras.setIva(12);
                double subtotal = ProductosActivosController.getProductoSeleccionado().getPrecio()*cantidadSeleccionada;
                compras.setSubtotal(subtotal);
                double iva = (subtotal*12)/100;
                compras.setTotal(subtotal+iva);
                cliente = clientesFacade.findByUsuario(LoginController.getUsuarioGlobal());
                compras.setCliente(cliente);
                comprasFacade.create(compras);
                ComprasProductos cp = new ComprasProductos();
                cp.setCantidad(cantidadSeleccionada);
                cp.setCompra(compras);
                cp.setProducto(ProductosActivosController.getProductoSeleccionado());
                cp.setValorProducto(ProductosActivosController.getProductoSeleccionado().getPrecio());
                comprasProductoFacade.create(cp);
                Productos aux = ProductosActivosController.getProductoSeleccionado();
                aux.setCantidad(ProductosActivosController.getProductoSeleccionado().getCantidad()-cantidadSeleccionada);
                productosFacade.edit(aux);
                FacesContext context = FacesContext.getCurrentInstance();  
                context.addMessage(null, new FacesMessage("Información", "Compra realizada con éxito!! VALOR A PAGAR: "+(subtotal+iva)));
                System.out.println("cantidad restante:"+(aux.getCantidad()));
            }else{
                FacesContext context = FacesContext.getCurrentInstance();  
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Debe ingresar La cantidad que desea comprar")); 
            }
        }else{
            System.out.println("entra error");
            FacesContext context = FacesContext.getCurrentInstance();  
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Cantidad no disponible en Stock")); 
        }
        return "";
    }
    
    public String regresar(){
        setCantidadSeleccionada(0);
        return "comprasCliente";
    }

    public Clientes getCliente() {
        if(cliente == null)
            cliente = clientesFacade.findByUsuario(LoginController.getUsuarioGlobal());
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public Compras getCompras() {
        compras = new Compras();
        compras.setFecha(new Date());
        compras.setIva(12);
        double subtotal = ProductosActivosController.getProductoSeleccionado().getPrecio()*cantidadSeleccionada;
        compras.setSubtotal(subtotal);
        double iva = (subtotal*12)/100;
        compras.setTotal(subtotal+iva);
        return compras;
    }

    public void setCompras(Compras compras) {
        this.compras = compras;
    }
    
    
    
    
}
