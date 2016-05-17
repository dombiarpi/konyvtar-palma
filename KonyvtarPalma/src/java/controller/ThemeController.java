package controller;

import entity.Theme;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import facade.ThemeFacade;

import java.io.Serializable;
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
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.themeswitcher.ThemeSwitcher;

@Named("themeController")
@SessionScoped
public class ThemeController implements Serializable {

    @EJB
    private facade.ThemeFacade ejbFacade;
    private List<Theme> items = null;
    private Theme selected;
    
    private String globalTheme = "afterdark";
    
    

    public String getGlobalTheme() {
        return globalTheme;
    }

    public void setGlobalTheme(String globalTheme) {
        this.globalTheme = globalTheme;
    }   
    
    @PostConstruct
    public void init() {
        getItems();
        selected = ejbFacade.findActiveTheme();
    }

    public ThemeController() {
    }

    public Theme getSelected() {
        return selected;
    }

    public void setSelected(Theme selected) {
        this.selected = selected;
    }
    
//    public void changeTheme(AjaxBehaviorEvent event) {
//
//        String t = getSelected().getName();
//        setGlobalTheme(t);
//    }        

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ThemeFacade getFacade() {
        return ejbFacade;
    }

    public Theme prepareCreate() {
        selected = new Theme();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/BundleTheme").getString("ThemeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        if (!selected.getActive()) {
            return;
        }
        Theme th = ejbFacade.findActiveTheme();
        th.setActive(Boolean.FALSE);
        persistTheme(th);
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/BundleTheme").getString("ThemeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/BundleTheme").getString("ThemeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Theme> getItems() {
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/BundleTheme").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/BundleTheme").getString("PersistenceErrorOccured"));
            }
        }
    }
    private void persistTheme(Theme theme) {
        if (theme != null) {
            setEmbeddableKeys();
            try {
                getFacade().edit(theme);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/BundleTheme").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/BundleTheme").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Theme getTheme(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Theme> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Theme> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Theme.class)
    public static class ThemeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ThemeController controller = (ThemeController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "themeController");
            return controller.getTheme(getKey(value));
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
            if (object instanceof Theme) {
                Theme o = (Theme) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Theme.class.getName()});
                return null;
            }
        }

    }

}
