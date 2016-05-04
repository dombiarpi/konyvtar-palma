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
    
    public int findMaxKatalogNumber() {
         Query q = em.createNativeQuery("select max(konyv.katal) from konyv");
         Object obj = q.getSingleResult();
         Integer result = (Integer)obj;
         return result;
    }
    
    public List<Konyv> findAllWithSzerzo() {
        List<Konyv> result = findAll();
        Query q = em.createNamedQuery("Konyv.findAllWithSzerzo");
        for (Konyv konyv : result) {
            q.setParameter("id", konyv.getId());
            List l = q.getResultList();
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (Object object : l) {
                String delimiter = count > 0 ? ", ":"";
                sb.append(delimiter).append(object.toString());
                count++;
            }
            konyv.setSzerzoNev(sb.toString());
        }
//        List list = q.getResultList();
//        for (Object object : list) {
//            Object[] konyvSzerzovel = (Object[])object;
//            Konyv konyv = (Konyv)konyvSzerzovel[0];
//            Szerzo szerzo = (Szerzo)konyvSzerzovel[1];
//            konyv.setSzerzoNev(szerzo.getNev());
//            result.add(konyv);
//        }
        return result;
    }
    
    public Konyv editKonyv(Konyv konyv) {
        return em.merge(konyv);
    }
    
}
