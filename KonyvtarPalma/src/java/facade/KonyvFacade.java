/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Konyv;
import entity.Szerzo;
import java.util.ArrayList;
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
public class KonyvFacade extends AbstractFacade<Konyv> {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KonyvFacade() {
        super(Konyv.class);
    }
    
    public List<Konyv> findAllWithSzerzo() {
        List<Konyv> result = new ArrayList<>();
        Query q = em.createNamedQuery("Konyv.findAllWithSzerzo");
        List list = q.getResultList();
        for (Object object : list) {
            Object[] konyvSzerzovel = (Object[])object;
            Konyv konyv = (Konyv)konyvSzerzovel[0];
            Szerzo szerzo = (Szerzo)konyvSzerzovel[1];
            konyv.setSzerzoNev(szerzo.getNev());
            result.add(konyv);
        }
        return result;
    }
    
}
