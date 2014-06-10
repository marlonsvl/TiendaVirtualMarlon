/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uah.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author marlonvinan
 */
@Stateless
public class ComprasProductosFacade extends AbstractFacade<ComprasProductos> {
    @PersistenceContext(unitName = "TiendaVirtualMarlonPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComprasProductosFacade() {
        super(ComprasProductos.class);
    }
    
}
