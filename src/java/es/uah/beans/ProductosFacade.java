/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uah.beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author marlonvinan
 */
@Stateless
public class ProductosFacade extends AbstractFacade<Productos> {
    @PersistenceContext(unitName = "TiendaVirtualMarlonPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProductosFacade() {
        super(Productos.class);
    }
    
    public List<Productos> findAllActivos() {
        return (List<Productos>)getEntityManager().createNamedQuery("Productos.findAllActivos").setParameter("estado", "Activado").getResultList();
    }
    
}
