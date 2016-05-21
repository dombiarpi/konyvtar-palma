package controller;

import entity.Kolcsonzes;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import entity.Elojegyzes;
import entity.Konyv;
import entity.Oszlop;
import entity.Peldany;
import entity.Szemely;
import facade.KolcsonzesFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.primefaces.event.CloseEvent;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.Visibility;

@Named("kolcsonzesController")
@SessionScoped
public class KolcsonzesController implements Serializable {
    
    private final static int MAX_KOLCSONZESI_IDO = 30;

    @EJB
    private facade.KolcsonzesFacade ejbFacade;
    private List<Kolcsonzes> items = null;
    private Kolcsonzes selected;
    
    private DashboardModel model;
    
    @Inject
    private SzemelyController szemelyController;
    @Inject
    private KonyvController konyvController;
    @Inject
    private PeldanyController peldanyController;
    @Inject
    private ElojegyzesController elojegyzesController;
    @Inject
    private OszlopController oszlopController;
    
    private Peldany kolcsonzi;
 
    private Peldany visszahozta;
    private List<Oszlop> kolcsonzesOszlopok;

    public List<Oszlop> getKolcsonzesOszlopok() {
        return kolcsonzesOszlopok;
    }

    private void initKolcsonzesOszlopok() {
        kolcsonzesOszlopok = new ArrayList<>();
        List<Oszlop> oszlopok = oszlopController.getItems();
        for (Oszlop oszlop : oszlopok) {
            if (oszlop != null && oszlop.getNev().startsWith("kolcsonzes")) {
                kolcsonzesOszlopok.add(oszlop);
            }
        }
    }    
    
    public void onToggle(ToggleEvent e) {
        Oszlop oszlop = kolcsonzesOszlopok.get((Integer) e.getData());
        oszlop.setLathatosag(e.getVisibility() == Visibility.VISIBLE);
        oszlopController.setSelected(oszlop);
        oszlopController.update();
    }

    public KolcsonzesController() {
    }
    
    // Init ---------------------------------------------------------------------------------------

    @PostConstruct
    public void init() {
        szemelyController.setSelected(szemelyController.getItems().get(0));
        konyvController.setSelected(konyvController.getItems().get(0));
        szemelyPeldanyai();
        konyvPeldanyai();
        // You can do here your initialization thing based on managed properties, if necessary.
        
        setupDashboard();
        initKolcsonzesOszlopok();
     }    

    private void setupDashboard() {
        model = new DefaultDashboardModel();
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
          
        column1.addWidget("a");
        column1.addWidget("d");
        column1.addWidget("e");
         
        column2.addWidget("b");
        column2.addWidget("c");
        column2.addWidget("f");
 
        model.addColumn(column1);
        model.addColumn(column2);
    }    
    
    public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Reordered: " + event.getWidgetId());
        message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());
         
        addMessage(message);
    }
     
    public void handleClose(CloseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent().getId() + "'");
         
        addMessage(message);
    }
     
    public void handleToggle(ToggleEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled", "Status:" + event.getVisibility().name());
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
     
    public DashboardModel getModel() {
        return model;
    }

    public void onPeldanyDrop(DragDropEvent ddEvent) {
        Peldany peld = ((Peldany) ddEvent.getData());
        peldanyController.getKonyvPeldanyaiItems().add(peld);
        peldanyController.getItems().remove(peld);
     }
    
    public void szemelyPeldanyai() {
        Szemely valasztottSzemely = szemelyController.getSelected();
        List<Peldany> friss = ejbFacade.findPeldanyBySzemely(valasztottSzemely);
        peldanyController.setSzemelyPeldanyaiItems(ejbFacade.findPeldanyBySzemely(valasztottSzemely));
    }
    
    public String visszahoz(Peldany peldany) {
        Szemely kolcsonzo = szemelyController.getSelected();
        if (kolcsonzo == null) {
            return null;
        }
        peldanyController.setSelected(peldany);
        peldany.setKikolcs(Boolean.FALSE);
        peldany.setAktKolcs(Boolean.TRUE);
        peldanyController.update();        
        selected = ejbFacade.findAllBySzemelyAndPeldany(kolcsonzo, peldany).get(0);// can be only one
        selected.setVisszahozDatum(new Date());
        update();
        peldanyController.getSzemelyPeldanyaiItems().remove(peldany);
        szemelyPeldanyai();
        konyvPeldanyai();
        return null;
    }
    
    public void konyvPeldanyai() {
        Konyv valasztottKonyv = konyvController.getSelected();
//        List<Peldany> friss = ejbFacade.findPeldanyByKonyv(valasztottKonyv);
        peldanyController.setKonyvPeldanyaiItems(ejbFacade.findPeldanyByKonyv(valasztottKonyv));
    }
    
    private void countKonyvPeldanyai() {
        
        for (Konyv item : konyvController.getItems()) {
            List<Peldany> friss = ejbFacade.findPeldanyByKonyv(item);
            
        }
    }
    
    public String kolcsonoz(Peldany peldany) {
        Szemely kolcsonzo = szemelyController.getSelected();
        if (kolcsonzo == null) {
            return null;
        }
        
        peldany.setKikolcs(Boolean.TRUE);
        peldany.setAktKolcs(Boolean.FALSE);   
        
        createKolcsonzes(kolcsonzo, peldany);
        peldanyController.getSzemelyPeldanyaiItems().add(peldany);
        szemelyPeldanyai();
        konyvPeldanyai();        
        return null;
    }
    
    public String kinelVan(Peldany peldany) {
        selected = ejbFacade.findAllByPeldany(peldany).get(0);
        return null;
    }

    private void createKolcsonzes(Szemely kolcsonzo, Peldany peldany) {
        Kolcsonzes kolcsonzes = prepareCreate();
        kolcsonzes.setSzemely(kolcsonzo);
        kolcsonzes.setPeldany(peldany);
        kolcsonzes.setDatum(new Date());
        kolcsonzes.setMaxKolcs(MAX_KOLCSONZESI_IDO);
        create();
    }
    
    public String elojegyzi(Konyv konyv) {
        Szemely elojegyzo = szemelyController.getSelected();
        if (elojegyzo == null) {
            return null;
        }
        createElojegyzes(elojegyzo, konyv);
        return null;
    }

    private void createElojegyzes(Szemely elojegyzo, Konyv konyv) {
        Elojegyzes elojegy = elojegyzesController.prepareCreate();
        elojegy.setSzemely(elojegyzo);
        elojegy.setKonyv(konyv);
        elojegy.setDatum(new Date());
        elojegyzesController.create();
    }

    public Kolcsonzes getSelected() {
        return selected;
    }

    public void setSelected(Kolcsonzes selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private KolcsonzesFacade getFacade() {
        return ejbFacade;
    }

    public Kolcsonzes prepareCreate() {
        selected = new Kolcsonzes();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("KolcsonzesCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            peldanyController.setKonyvPeldanyaiItems(null);
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("KolcsonzesUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("KolcsonzesDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Kolcsonzes> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
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

    public Kolcsonzes getKolcsonzes(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Kolcsonzes> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Kolcsonzes> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public void setSzemelyController(SzemelyController szemelyController) {
        this.szemelyController = szemelyController;
    }

    public SzemelyController getSzemelyController() {
        return szemelyController;
    }

    public PeldanyController getPeldanyController() {
        return peldanyController;
    }

    public void setPeldanyController(PeldanyController peldanyController) {
        this.peldanyController = peldanyController;
    }

    /**
     * @return the kolcsonzi
     */
    public Peldany getKolcsonzi() {
        return kolcsonzi;
    }

    /**
     * @param kolcsonzi the kolcsonzi to set
     */
    public void setKolcsonzi(Peldany kolcsonzi) {
        this.kolcsonzi = kolcsonzi;
    }

    /**
     * @return the visszahozta
     */
    public Peldany getVisszahozta() {
        return visszahozta;
    }

    /**
     * @param visszahozta the visszahozta to set
     */
    public void setVisszahozta(Peldany visszahozta) {
        this.visszahozta = visszahozta;
    }
    
    @FacesConverter(forClass = Kolcsonzes.class)
    public static class KolcsonzesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            KolcsonzesController controller = (KolcsonzesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "kolcsonzesController");
            return controller.getKolcsonzes(getKey(value));
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
            if (object instanceof Kolcsonzes) {
                Kolcsonzes o = (Kolcsonzes) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Kolcsonzes.class.getName()});
                return null;
            }
        }

    }

}
