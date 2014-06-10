package es.uah.controllers;

import es.uah.beans.Productos;
import es.uah.controllers.util.JsfUtil;
import es.uah.controllers.util.PaginationHelper;
import es.uah.beans.ProductosFacade;
import es.uah.controllers.util.ClientProveedores;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

@Named("productosController")
@SessionScoped
public class ProductosController implements Serializable {

    private Productos current;
    private DataModel items = null;
    @EJB
    private es.uah.beans.ProductosFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private int cantidadAComprar;
    private String nombreUsuario;
    private static Productos productoSeleccionado;
    private String criterioBusqueda;
    private boolean renderfecha = false;
    private Date fecha;
    private int cantidadReposicion;
    //private static final String path = "http://localhost:8080/ProveedoresWS/webresources/productosprov/";
    private static final String path = "http://apolo.utpl.edu.ec:8084/ProveedoresWS/webresources/productosprov/";

    public ProductosController() {
    }

    public Productos getSelected() {
        if (current == null) {
            current = new Productos();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ProductosFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Productos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Productos();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            current.setEstado("Activado");
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductosCreated"));
            recreatePagination();
            recreateModel();
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Productos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String prepareAltaYBaja() {
        current = (Productos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "altaBajaProductos";
    }

    public String prepareReponer() {
        current = (Productos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "reposicionProducto";
    }

    public String seleccionar() {
//        LoginController lg = (LoginController)FacesContext.getCurrentInstance()
//                .getExternalContext().getApplicationMap().get("loginController");
        nombreUsuario = LoginController.getUsuarioGlobal().getUsername();
        //System.out.println(LoginController.getUsuarioGlobal().getUsername());
        current = (Productos) getItems().getRowData();
        System.out.println(current.getCod() + "------");
        //RequestContext.getCurrentInstance().openDialog("detalleProducto");
        //RequestContext.getCurrentInstance().execute("dlgd.show()");
        productoSeleccionado = current;
        return "factura";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductosUpdated"));
            recreatePagination();
            recreateModel();
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public void cambiarCriterio(ValueChangeEvent ev) {
        System.out.println("----------------" + ev.getNewValue().toString());
        String value = ev.getNewValue().toString();
        if (value.equals("1")) {
            System.out.println("Por cantidad");
            renderfecha = false;
        } else if (value.equals("2")) {
            System.out.println("Por fecha");
            renderfecha = true;
        }
    }

    public String reponerProducto() {
        String page = "";
        if (criterioBusqueda.equals("1")) {
            System.out.println("Por cantidad");
            renderfecha = false;
            String res = ClientProveedores
                    .getResource(path + "productosPorCantidad?cod=" + current.getCod() + "&cantidad=" + cantidadReposicion);
            if (res.equals("204")) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "No existe el producto: " + current.getNombre()
                        + " con la cantidad deseada en los proveedores disponibles"));
                System.out.println(res);
            } else if (res.equals("400") || res.equals("404")) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Bad request"));

            } else {
                System.out.println(res);
                try {
                    JSONArray arr = new JSONArray(res);
                    JSONObject obj = arr.getJSONObject(0);
                    double precio = obj.getDouble("precio");
                    current.setCantidad(current.getCantidad() + cantidadReposicion);
                    ClientProveedores.persist(path + "editPP", ClientProveedores.jsonEditado(res, cantidadReposicion));
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage("La cantidad ACTUAL de producto: " + current.getNombre() + " en la tienda es: " + current.getCantidad()+"\n"
                            + "El total a pagar al PROVEEDOR es: "+precio*cantidadReposicion);
                    recreatePagination();
                    recreateModel();
                    page = "View";
                } catch (Exception e) {
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
                
            }
        } else if (criterioBusqueda.equals("2")) {
            System.out.println("Por fecha");
            System.out.println("fechaaaa: " + fecha.toString());
            Calendar c = Calendar.getInstance();
            c.setTime(fecha);
            // formatemaos la fecha para el web service
            int m = c.get(Calendar.MONTH) + 1;
            String f = c.get(Calendar.YEAR) + "-" + m + "-" + c.get(Calendar.DAY_OF_MONTH);
            renderfecha = true;
            String res = ClientProveedores
                    .getResource(path + "productosPorFecha?cod=" + current.getCod() + "&cantidad=" + cantidadReposicion + "&fecha=" + f);
            System.out.println(path + "productosPorFecha?cod=" + current.getCod() + "&cantidad=" + cantidadReposicion + "&fecha=" + f);
            if (res.equals("204")) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "No existe el producto: " + current.getNombre()
                        + " con la cantidad y fecha deseada en los proveedores disponibles"));
            } else if (res.equals("400") || res.equals("404")) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", "Bad request"));

            } else {
                try {
                    JSONArray arr = new JSONArray(res);
                    JSONObject obj = arr.getJSONObject(0);
                    double precio = obj.getDouble("precio");
                    current.setCantidad(current.getCantidad() + cantidadReposicion);
                    ClientProveedores.persist(path + "editPP", ClientProveedores.jsonEditado(res, cantidadReposicion));
                    getFacade().edit(current);
                    JsfUtil.addSuccessMessage("La cantidad ACTUAL de producto: " + current.getNombre() + " en la tienda es: " + current.getCantidad()+"\n"
                            + "El total a pagar al PROVEEDOR es: "+precio*cantidadReposicion);
                    recreatePagination();
                    recreateModel();
                    page = "View";
                } catch (Exception e) {
                    JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            }
        }
        return page;

    }

    public String destroy() {
        current = (Productos) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ProductosDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        //return "List";
        return "";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        //return "List";
        return "";
    }

    public String regresarLogin() {
        return "/login";
    }

    public String regresarIndex() {
        return "/index";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Productos getProductos(java.lang.String id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Productos.class)
    public static class ProductosControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ProductosController controller = (ProductosController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "productosController");
            return controller.getProductos(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Productos) {
                Productos o = (Productos) object;
                return getStringKey(o.getCod());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Productos.class.getName());
            }
        }
    }

    public Productos getCurrent() {
        return current;
    }

    public void setCurrent(Productos current) {
        this.current = current;
    }

    public int getCantidadAComprar() {
        return cantidadAComprar;
    }

    public void setCantidadAComprar(int cantidadAComprar) {
        this.cantidadAComprar = cantidadAComprar;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public static Productos getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public static void setProductoSeleccionado(Productos productoSeleccionado) {
        ProductosController.productoSeleccionado = productoSeleccionado;
    }

    public String getCriterioBusqueda() {
        return criterioBusqueda;
    }

    public void setCriterioBusqueda(String criterioBusqueda) {
        this.criterioBusqueda = criterioBusqueda;
    }

    public boolean isRenderfecha() {
        return renderfecha;
    }

    public void setRenderfecha(boolean renderfecha) {
        this.renderfecha = renderfecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getCantidadReposicion() {
        return cantidadReposicion;
    }

    public void setCantidadReposicion(int cantidadReposicion) {
        this.cantidadReposicion = cantidadReposicion;
    }
    
    
}
