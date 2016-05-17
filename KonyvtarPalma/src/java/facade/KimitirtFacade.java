/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Kimitirt;
import entity.Konyv;
import entity.Szerzo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author u201993
 */
@Stateless
public class KimitirtFacade extends AbstractFacade<Kimitirt> {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KimitirtFacade() {
        super(Kimitirt.class);
    }
    
    public Kimitirt find(Konyv konyv) {
        Query q = getEntityManager().createNamedQuery("Kimitirt.findByKonyv", Kimitirt.class);
        q.setParameter("konyv", konyv);
        Kimitirt km = (Kimitirt)q.getSingleResult();
        return km;
    } 
    
    public List<Kimitirt> find(Konyv konyv, Szerzo szerzo) {
        List<Kimitirt> result = null;
        Query q = getEntityManager().createNamedQuery("Kimitirt.findByKonyvAndSzerzo", Kimitirt.class);
        q.setParameter("konyv", konyv);
        q.setParameter("szerzo", szerzo);
        result = (List<Kimitirt>)q.getResultList();
        return result;
    } 
    
    
    
}
