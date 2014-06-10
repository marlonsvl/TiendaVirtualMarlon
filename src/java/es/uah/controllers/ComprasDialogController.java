/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uah.controllers;

import org.primefaces.context.RequestContext;

/**
 *
 * @author utpl
 */
public class ComprasDialogController {
     public void openDialog() {  
            RequestContext.getCurrentInstance().openDialog("selectCar");  
        }  
  
//        public void onCarChosen(SelectEvent event) {  
//            Car car = (Car) event.getObject();  
//            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Car Selected", "Model:" + car.getModel());  
//  
//            FacesContext.getCurrentInstance().addMessage(null, message);  
//        }  
}
