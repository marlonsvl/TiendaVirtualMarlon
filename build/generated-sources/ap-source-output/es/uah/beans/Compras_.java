package es.uah.beans;

import es.uah.beans.Clientes;
import es.uah.beans.ComprasProductos;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.0.v20130507-rNA", date="2014-06-06T16:40:03")
@StaticMetamodel(Compras.class)
public class Compras_ { 

    public static volatile SingularAttribute<Compras, Integer> id;
    public static volatile SingularAttribute<Compras, Double> iva;
    public static volatile SingularAttribute<Compras, Double> total;
    public static volatile CollectionAttribute<Compras, ComprasProductos> comprasProductosCollection;
    public static volatile SingularAttribute<Compras, Date> fecha;
    public static volatile SingularAttribute<Compras, Clientes> cliente;
    public static volatile SingularAttribute<Compras, Double> subtotal;

}