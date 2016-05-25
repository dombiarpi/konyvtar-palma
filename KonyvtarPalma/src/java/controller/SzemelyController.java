package controller;

import entity.Szemely;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import entity.Oszlop;
import facade.SzemelyFacade;

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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

@Named("szemelyController")
@SessionScoped
public class SzemelyController implements Serializable {

    @EJB
    private facade.SzemelyFacade ejbFacade;
    private List<Szemely> items = null;
    private Szemely selected;
    @Inject
    private OszlopController oszlopController;    
    private List<Oszlop> szemelyOszlopok;

    public List<Oszlop> getSzemelyOszlopok() {
        return szemelyOszlopok;
    }    

    public SzemelyController() {
    }
    
    @PostConstruct
    public void init() {
        szemelyOszlopok = new ArrayList<>();
        List<Oszlop> oszlopok = oszlopController.getItems();
        for (Oszlop oszlop : oszlopok) {
            if (oszlop != null && oszlop.getNev().startsWith("olvaso")) {
                szemelyOszlopok.add(oszlop);
            }
        }
    }    
    
    public void onToggle(ToggleEvent e) {
        Oszlop oszlop = szemelyOszlopok.get((Integer) e.getData());
        oszlop.setLathatosag(e.getVisibility() == Visibility.VISIBLE);
        oszlopController.setSelected(oszlop);
        oszlopController.update();
    }
    
    public Szemely getSelected() {
        return selected;
    }

    public void setSelected(Szemely selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SzemelyFacade getFacade() {
        return ejbFacade;
    }

    public Szemely prepareCreate() {
        selected = new Szemely();
        initializeEmbeddableKey();
        return selected;
    }
    
    // Nincs használva de mintaként itt marad. Ezzel együtt működne:
    // <f:event listener="#{szemelyController.validateElofizetesDatum}" type="postValidate" />
    // csak nem marad nyitva a dialógus ablak
    public void validateElofizetesDatum(ComponentSystemEvent event) {
	  FacesContext fc = FacesContext.getCurrentInstance();
	  UIComponent components = event.getComponent();
	  UIInput uiInputElofizDatum = (UIInput) components.findComponent("elofizDatum");
	  UIInput uiInputTagdij = (UIInput) components.findComponent("tagdij");
          
	  String elofizdatum = uiInputElofizDatum.getLocalValue() == null ? ""
		: uiInputElofizDatum.getLocalValue().toString();
	  String elofizdatumId = uiInputElofizDatum.getClientId();          
	  String tagDij = uiInputTagdij.getSubmittedValue() == null ? ""
		: uiInputTagdij.getSubmittedValue().toString();

	  if (elofizdatum.equals("") && Integer.parseInt(tagDij) > 0) {

		FacesMessage msg = new FacesMessage("Ha fizetnek tagdíjat, az előfizetés dátuma mező nem lehet üres!");
		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		fc.addMessage(elofizdatumId, msg);
		fc.renderResponse();
	  }
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SzemelyCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SzemelyUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SzemelyDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Szemely> getItems() {
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

    public Szemely getSzemely(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Szemely> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Szemely> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Szemely.class)
    public static class SzemelyControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SzemelyController controller = (SzemelyController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "szemelyController");
            return controller.getSzemely(getKey(value));
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
            if (object instanceof Szemely) {
                Szemely o = (Szemely) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Szemely.class.getName()});
                return null;
            }
        }

    }

}
