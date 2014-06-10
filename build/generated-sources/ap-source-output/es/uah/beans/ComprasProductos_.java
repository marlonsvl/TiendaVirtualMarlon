package es.uah.beans;

import es.uah.beans.Compras;
import es.uah.beans.Productos;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.0.v20130507-rNA", date="2014-06-06T16:40:03")
@StaticMetamodel(ComprasProductos.class)
public class ComprasProductos_ { 

    public static volatile SingularAttribute<ComprasProductos, Integer> id;
    public static volatile SingularAttribute<ComprasProductos, Compras> compra;
    public static volatile SingularAttribute<ComprasProductos, Integer> cantidad;
    public static volatile SingularAttribute<ComprasProductos, Productos> producto;
    public static volatile SingularAttribute<ComprasProductos, Double> valorProducto;

}