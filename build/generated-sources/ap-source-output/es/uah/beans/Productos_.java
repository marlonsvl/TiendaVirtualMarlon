package es.uah.beans;

import es.uah.beans.ComprasProductos;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.0.v20130507-rNA", date="2014-06-06T16:40:03")
@StaticMetamodel(Productos.class)
public class Productos_ { 

    public static volatile SingularAttribute<Productos, String> nombre;
    public static volatile SingularAttribute<Productos, String> estado;
    public static volatile CollectionAttribute<Productos, ComprasProductos> comprasProductosCollection;
    public static volatile SingularAttribute<Productos, String> cod;
    public static volatile SingularAttribute<Productos, Double> precio;
    public static volatile SingularAttribute<Productos, Integer> cantidad;

}