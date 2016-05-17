/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.UniqueConstraint;

/**
 *
 * @author u201993
 */
@Entity
@Table(name = "kimitirt", catalog = "konyvtar", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"konyv", "szerzo"})})
@NamedQueries({
    @NamedQuery(name = "Kimitirt.findAll", query = "SELECT k FROM Kimitirt k"),
    @NamedQuery(name = "Kimitirt.findById", query = "SELECT k FROM Kimitirt k WHERE k.id = :id"),
    @NamedQuery(name = "Kimitirt.findByKonyv", query = "SELECT k FROM Kimitirt k WHERE k.konyv = :konyv"),
    @NamedQuery(name = "Kimitirt.findByKonyvAndSzerzo", query = "SELECT k FROM Kimitirt k WHERE k.konyv = :konyv and k.szerzo = :szerzo")})
public class Kimitirt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @JoinColumn(name = "szerzo", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Szerzo szerzo;
    @JoinColumn(name = "konyv", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Konyv konyv;

    public Kimitirt() {
    }

    public Kimitirt(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Szerzo getSzerzo() {
        return szerzo;
    }

    public void setSzerzo(Szerzo szerzo) {
        this.szerzo = szerzo;
    }

    public Konyv getKonyv() {
        return konyv;
    }

    public void setKonyv(Konyv konyv) {
        this.konyv = konyv;
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
        if (!(object instanceof Kimitirt)) {
            return false;
        }
        Kimitirt other = (Kimitirt) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return szerzo.getNev() + ", " + konyv.getCim();
    }
    
}
