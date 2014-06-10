/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uah.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author marlonvinan
 */
@Stateless
public class ClientesFacade extends AbstractFacade<Clientes> {
    @PersistenceContext(unitName = "TiendaVirtualMarlonPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ClientesFacade() {
        super(Clientes.class);
    }
    
    public Clientes findByUsuario(Usuarios usuario) {
        try{
            return (Clientes)getEntityManager().createNamedQuery("Clientes.findByUsuario").setParameter("usuario", usuario).getSingleResult();
        }catch(NoResultException ex){
            return null;
        }
    }
    
}
