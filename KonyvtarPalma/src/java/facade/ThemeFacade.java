/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Theme;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author U201993
 */
@Stateless
public class ThemeFacade extends AbstractFacade<Theme> {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ThemeFacade() {
        super(Theme.class);
    }
    
        public Theme findActiveTheme() {
        Query q = em.createNamedQuery("Theme.findActive");
        q.setParameter("active", Boolean.TRUE);
        
        return (Theme)q.getSingleResult();
    }
    
}
