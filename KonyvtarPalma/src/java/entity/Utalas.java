/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

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
import javax.persistence.UniqueConstraint;

/**
 *
 * @author u201993
 */
@Entity
@Table(name = "utalas", catalog = "konyvtar", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"konyv", "fogalom"})})
@NamedQueries({
    @NamedQuery(name = "Utalas.findAll", query = "SELECT u FROM Utalas u"),
    @NamedQuery(name = "Utalas.findById", query = "SELECT u FROM Utalas u WHERE u.id = :id")})
public class Utalas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @JoinColumn(name = "konyv", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Konyv konyv;
    @JoinColumn(name = "fogalom", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Fogalom fogalom;

    public Utalas() {
    }

    public Utalas(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Konyv getKonyv() {
        return konyv;
    }

    public void setKonyv(Konyv konyv) {
        this.konyv = konyv;
    }

    public Fogalom getFogalom() {
        return fogalom;
    }

    public void setFogalom(Fogalom fogalom) {
        this.fogalom = fogalom;
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
        if (!(object instanceof Utalas)) {
            return false;
        }
        Utalas other = (Utalas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return konyv.getCim() + ", " + fogalom.getNev();
    }
    
}
