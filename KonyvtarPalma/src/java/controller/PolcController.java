package controller;

import entity.Polc;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import entity.Szekreny;
import facade.PolcFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

@Named("polcController")
@SessionScoped
public class PolcController implements Serializable {

    @EJB
    private facade.PolcFacade ejbFacade;
    private List<Polc> items = null;
    private Polc selected;

    public PolcController() {
    }

    public Polc getSelected() {
        return selected;
    }

    public void setSelected(Polc selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }
    
    public List<Polc> completePolc(String query) {
        List<Polc> filteredPolc = new ArrayList<>();
         
        for (int i = 0; i < getItems().size(); i++) {
            Polc polc = items.get(i);
            if(polc.toString().startsWith(query)) {
                filteredPolc.add(polc);
            }
        }
         
        return filteredPolc;
    }         

    private PolcFacade getFacade() {
        return ejbFacade;
    }

    public Polc prepareCreate() {
        selected = new Polc();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PolcCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PolcUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PolcDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Polc> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    
    public List<Szekreny> completeSzekreny(String query) {
        List<Szekreny> allSzekreny = Arrays.asList(Szekreny.values());
        List<Szekreny> filteredSzekreny = new ArrayList<>();
         
        for (int i = 0; i < allSzekreny.size(); i++) {
            Szekreny szekreny = allSzekreny.get(i);
            if(szekreny.getName().startsWith(query)) {
                filteredSzekreny.add(szekreny);
            }
        }
        return filteredSzekreny;
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

    public Polc getPolc(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Polc> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Polc> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(value="polcControllerConverter", forClass = Polc.class)
    public static class PolcControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PolcController controller = (PolcController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "polcController");
            return controller.getPolc(getKey(value));
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
            if (object instanceof Polc) {
                Polc o = (Polc) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Polc.class.getName()});
                return null;
            }
        }

    }

}
