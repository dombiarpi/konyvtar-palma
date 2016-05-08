/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import entity.Kolcsonzes;
import entity.Konyv;
import entity.Peldany;
import entity.Szemely;
import java.util.AbstractList;
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
public class KolcsonzesFacade extends AbstractFacade<Kolcsonzes> {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KolcsonzesFacade() {
        super(Kolcsonzes.class);
    }

    public List<Peldany> findPeldanyBySzemely(Szemely szemely) {
        if (szemely == null) {
            szemely = new Szemely(); 
            szemely.setId(1);
        }
        Query q = em.createNamedQuery("Kolcsonzes.findAllPeldanyBySzemely");
        q.setParameter("szemely", szemely);
        return q.getResultList();
    }
    
    public List<Peldany> findPeldanyByKonyv(Konyv konyv) {
        if (konyv == null) {
            konyv = new Konyv(); 
            konyv.setId(1);
        }
        Query q = em.createNamedQuery("Kolcsonzes.findAllPeldanyByKonyv");
        q.setParameter("konyv", konyv);
        return q.getResultList();
    }
    
    

}
