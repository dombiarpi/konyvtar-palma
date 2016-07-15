package controller;

import entity.Konyv;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import entity.Kimitirt;
import entity.Nyelv;
import entity.Oszlop;
import entity.Peldany;
import entity.Szerzo;
import facade.KimitirtFacade;
import facade.KonyvFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

@Named("konyvController")
@SessionScoped
public class KonyvController implements Serializable {

    @EJB
    private facade.KonyvFacade ejbFacade;
    @EJB
    private facade.SzerzoFacade szerzoFacade;
    @EJB
    private facade.KimitirtFacade kimitirtFacade;
    @Inject
    private OszlopController oszlopController;
    @Inject
    private KimitirtController kimitirtController;
    @Inject
    private NyelvController nyelvController;
    @Inject
    private PeldanyController peldanyController;
    
    private List<Konyv> konyvek;
    private List<Integer> katalogNumbers;

    public List<Konyv> getKonyvek() {
        return konyvek;
    }

    public void setKonyvek(List<Konyv> konyvek) {
        this.konyvek = konyvek;
    }
    
    public String reinit() {
        selected = new Konyv();
        setDefaultsOfNewKonyv();
        Collections.sort(katalogNumbers);
        Integer katal = katalogNumbers.get(katalogNumbers.size()-1);
        Integer newKatal = katal + 1;
        katalogNumbers.add(newKatal);
        selected.setKatal(newKatal);
        initPeldany();
        initializeEmbeddableKey();
        return null;
    }

    public KimitirtFacade getKimitirtFacade() {
        return kimitirtFacade;
    }
    private List<Konyv> items = null;

    public void setItems(List<Konyv> items) {
        this.items = items;
    }
    private List<Konyv> filteredItems = null;
    private Konyv selected;
    private List<Szerzo> szerzok;
    private List<Oszlop> konyvOszlopok;
    private boolean elsoPeldany = true;
    
    public void elsoPeldanyChange() {
        String s = "";
    }

    public List<Oszlop> getKonyvOszlopok() {
        return konyvOszlopok;
    }

    public KonyvController() {
    }
    
    @PostConstruct
    public void init() {
        konyvOszlopok = new ArrayList<>();
        konyvek = new ArrayList<>();
        katalogNumbers = new ArrayList<>();
        List<Oszlop> oszlopok = oszlopController.getItems();
        for (Oszlop oszlop : oszlopok) {
            if (oszlop != null && oszlop.getNev().startsWith("konyv")) {
                konyvOszlopok.add(oszlop);
            }
        }
    }    
    
    public void onToggle(ToggleEvent e) {
        Oszlop oszlop = konyvOszlopok.get((Integer) e.getData());
        oszlop.setLathatosag(e.getVisibility() == Visibility.VISIBLE);
        oszlopController.setSelected(oszlop);
        oszlopController.update();
    }
    
    public List<Szerzo> completeSzerzo(String query) {
        List<Szerzo> allSzerzo = szerzoFacade.findAll();
        List<Szerzo> filteredSzerzo = new ArrayList<>();
         
        for (int i = 0; i < allSzerzo.size(); i++) {
            Szerzo szerzo = allSzerzo.get(i);
            if(szerzo.getNev().startsWith(query)) {
                filteredSzerzo.add(szerzo);
            }
        }
         
        return filteredSzerzo;
    } 
    
    public List<Konyv> completeKonyv(String query) {
        List<Konyv> filteredKonyv = new ArrayList<>();
         
        for (int i = 0; i < getItems().size(); i++) {
            Konyv konyv = items.get(i);
            if(konyv.getCim().startsWith(query)) {
                filteredKonyv.add(konyv);
            }
        }
         
        return filteredKonyv;
    }    
    

    public Konyv getSelected() {
        return selected;
    }

    public void setSelected(Konyv selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private KonyvFacade getFacade() {
        return ejbFacade;
    }

    public Konyv prepareCreate() {
        selected = new Konyv();
        konyvek = new ArrayList<>();
        setDefaultsOfNewKonyv();
        reInitKatalogNumbers();
        initializeEmbeddableKey();
        initPeldany();
        return selected;
    }

    private void initPeldany() {
        Peldany elso = new Peldany();
        elso.setKikolcs(Boolean.FALSE);
        elso.setAktKolcs(Boolean.TRUE); 
        elso.setKonyvPeldany(1);
        selected.setElso(elso);
    }
    
    public void prepareUpdate() {
        if (selected == null) {
            return;
        }
        szerzok = ejbFacade.findSzerzokOfKonyv(selected);
     }

    private void setDefaultsOfNewKonyv() {
        selected.setSzorzo(1.5);
        selected.setEgysAr(1000);
        for (Nyelv nyelv  :  nyelvController.getItemsAvailableSelectOne()) {
           if (nyelv.getNev().equalsIgnoreCase("Magyar")) {
               selected.setNyelv(nyelv);
           }
        }
     }

    private void reInitKatalogNumbers() {
        katalogNumbers = new ArrayList<>();
        Integer katalog = getFacade().findMaxKatalogNumber();
        katalogNumbers.add(katalog + 1);
        selected.setKatal(katalog + 1);
    }
    
    public void createAll() {
        for (Konyv konyv : konyvek) {
            selected = konyv;
            create();
        }
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("KonyvCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("KonyvUpdated"));
         if (!JsfUtil.isValidationFailed()) {
            kimitirtController.setItems(null);    // Invalidate list of items to trigger re-query.
        }       
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("KonyvDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Konyv> getItems() {
        if (items == null) {
            items = getFacade().findAllWithSzerzo();
        }
        return items;
    }
    
    private void setupSzerzok(Konyv konyv) {
        for (Szerzo item : konyv.getSzerzok()) {
            Szerzo szerzo = szerzoFacade.find(item.getId());
            List<Kimitirt> kimitirtak = kimitirtFacade.find(konyv, szerzo);
            if (kimitirtak == null || kimitirtak.isEmpty()) {
                Kimitirt kimitirt = new Kimitirt();
                kimitirt.setKonyv(konyv);
                kimitirt.setSzerzo(szerzo);
                kimitirtFacade.create(kimitirt);
            }
        }
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    // kimitirtFacade and peldanyController also persists the konyv
                    Konyv k = getFacade().editKonyv(selected);
                    setupSzerzok(k);
                    setupPeldany(k);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Konyv getKonyv(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Konyv> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Konyv> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public facade.SzerzoFacade getSzerzoFacade() {
        return szerzoFacade;
    }

    public List<Szerzo> getSzerzok() {
        return szerzok;
    }

    public void setSzerzok(List<Szerzo> szerzok) {
        this.szerzok = szerzok;
    }

    public List<Konyv> getFilteredItems() {
        return filteredItems;
    }

    public void setFilteredItems(List<Konyv> filteredItems) {
        this.filteredItems = filteredItems;
    }

    /**
     * @return the elsoPeldany
     */
    public boolean isElsoPeldany() {
        return elsoPeldany;
    }

    /**
     * @param elsoPeldany the elsoPeldany to set
     */
    public void setElsoPeldany(boolean elsoPeldany) {
        this.elsoPeldany = elsoPeldany;
    }

    private void setupPeldany(Konyv k) {
        peldanyController.setSelected(k.getElso());
        peldanyController.getSelected().setKonyv(k);
        peldanyController.create();
    }

    @FacesConverter(value="konyvControllerConverter", forClass = Konyv.class)
    public static class KonyvControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            KonyvController controller = (KonyvController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "konyvController");
            return controller.getKonyv(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Konyv) {
                Konyv o = (Konyv) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Konyv.class.getName()});
                return null;
            }
        }

    }

}
