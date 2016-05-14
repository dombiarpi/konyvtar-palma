package controller;

import entity.Peldany;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import facade.PeldanyFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("peldanyController")
@SessionScoped
public class PeldanyController implements Serializable {

    @EJB
    private facade.PeldanyFacade ejbFacade;
    private List<Peldany> items = null;
    private List<Peldany> szemelyPeldanyaiItems = null;
    private List<Peldany> konyvPeldanyaiItems = null;

    private Peldany selected;

    public PeldanyController() {
    }

    public Peldany getSelected() {
        return selected;
    }

    public void setSelected(Peldany selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PeldanyFacade getFacade() {
        return ejbFacade;
    }

    public Peldany prepareCreate() {
        selected = new Peldany();
        selected.setKikolcs(Boolean.FALSE);
        selected.setAktKolcs(Boolean.TRUE);
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PeldanyCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PeldanyUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PeldanyDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Peldany> getItems() {
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

    public Peldany getPeldany(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Peldany> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Peldany> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public void setItems(List<Peldany> items) {
        this.items = items;
    }

    public List<Peldany> getSzemelyPeldanyaiItems() {
        return szemelyPeldanyaiItems;
    }

    public void setSzemelyPeldanyaiItems(List<Peldany> szemelyPeldanyaiItems) {
        this.szemelyPeldanyaiItems = szemelyPeldanyaiItems;
    }

    public List<Peldany> getKonyvPeldanyaiItems() {
        return konyvPeldanyaiItems;
    }

    public void setKonyvPeldanyaiItems(List<Peldany> konyvPeldanyaiItems) {
        this.konyvPeldanyaiItems = konyvPeldanyaiItems;
    }

    @FacesConverter(forClass = Peldany.class)
    public static class PeldanyControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PeldanyController controller = (PeldanyController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "peldanyController");
            return controller.getPeldany(getKey(value));
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
            if (object instanceof Peldany) {
                Peldany o = (Peldany) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Peldany.class.getName()});
                return null;
            }
        }

    }

}
