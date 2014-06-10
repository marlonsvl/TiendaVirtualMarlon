/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.uah.beans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author marlonvinan
 */
@Entity
@Table(name = "COMPRAS_PRODUCTOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComprasProductos.findAll", query = "SELECT c FROM ComprasProductos c"),
    @NamedQuery(name = "ComprasProductos.findById", query = "SELECT c FROM ComprasProductos c WHERE c.id = :id"),
    @NamedQuery(name = "ComprasProductos.findByCantidad", query = "SELECT c FROM ComprasProductos c WHERE c.cantidad = :cantidad"),
    @NamedQuery(name = "ComprasProductos.findByValorProducto", query = "SELECT c FROM ComprasProductos c WHERE c.valorProducto = :valorProducto")})
public class ComprasProductos implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANTIDAD")
    private int cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "VALOR_PRODUCTO")
    private double valorProducto;
    @JoinColumn(name = "PRODUCTO", referencedColumnName = "CODIGO")
    @ManyToOne(optional = false)
    private Productos producto;
    @JoinColumn(name = "COMPRA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Compras compra;

    public ComprasProductos() {
    }

    public ComprasProductos(Integer id) {
        this.id = id;
    }

    public ComprasProductos(Integer id, int cantidad, double valorProducto) {
        this.id = id;
        this.cantidad = cantidad;
        this.valorProducto = valorProducto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getValorProducto() {
        return valorProducto;
    }

    public void setValorProducto(double valorProducto) {
        this.valorProducto = valorProducto;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public Compras getCompra() {
        return compra;
    }

    public void setCompra(Compras compra) {
        this.compra = compra;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComprasProductos)) {
            return false;
        }
        ComprasProductos other = (ComprasProductos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "id=" + id + " ]";
    }
    
}
