/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author u201993
 */
@Entity
@Table(name = "fogalom", catalog = "konyvtar", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nev"})})
@NamedQueries({
    @NamedQuery(name = "Fogalom.findAll", query = "SELECT f FROM Fogalom f"),
    @NamedQuery(name = "Fogalom.findById", query = "SELECT f FROM Fogalom f WHERE f.id = :id"),
    @NamedQuery(name = "Fogalom.findByNev", query = "SELECT f FROM Fogalom f WHERE f.nev = :nev")})
public class Fogalom implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nev", nullable = false, length = 50)
    private String nev;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fogalom")
    private Collection<Utalas> utalasCollection;

    public Fogalom() {
    }

    public Fogalom(Integer id) {
        this.id = id;
    }

    public Fogalom(Integer id, String nev) {
        this.id = id;
        this.nev = nev;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public Collection<Utalas> getUtalasCollection() {
        return utalasCollection;
    }

    public void setUtalasCollection(Collection<Utalas> utalasCollection) {
        this.utalasCollection = utalasCollection;
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
        if (!(object instanceof Fogalom)) {
            return false;
        }
        Fogalom other = (Fogalom) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nev;
    }
    
}
