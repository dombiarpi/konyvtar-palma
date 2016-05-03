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
@Table(name = "szerzo", catalog = "konyvtar", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nev"})})
@NamedQueries({
    @NamedQuery(name = "Szerzo.findAll", query = "SELECT s FROM Szerzo s"),
    @NamedQuery(name = "Szerzo.findById", query = "SELECT s FROM Szerzo s WHERE s.id = :id"),
    @NamedQuery(name = "Szerzo.findByNev", query = "SELECT s FROM Szerzo s WHERE s.nev = :nev")})
public class Szerzo implements Serializable {

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "szerzo")
    private Collection<Kimitirt> kimitirtCollection;

    public Szerzo() {
    }

    public Szerzo(Integer id) {
        this.id = id;
    }

    public Szerzo(Integer id, String nev) {
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

    public Collection<Kimitirt> getKimitirtCollection() {
        return kimitirtCollection;
    }

    public void setKimitirtCollection(Collection<Kimitirt> kimitirtCollection) {
        this.kimitirtCollection = kimitirtCollection;
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
        if (!(object instanceof Szerzo)) {
            return false;
        }
        Szerzo other = (Szerzo) object;
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
