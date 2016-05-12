/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author u201993
 */
@Entity
@Table(name = "elojegyzes", catalog = "konyvtar", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"szemely", "konyv"})})
@NamedQueries({
    @NamedQuery(name = "Elojegyzes.findAll", query = "SELECT e FROM Elojegyzes e"),
    @NamedQuery(name = "Elojegyzes.findById", query = "SELECT e FROM Elojegyzes e WHERE e.id = :id"),
    @NamedQuery(name = "Elojegyzes.findByDatum", query = "SELECT e FROM Elojegyzes e WHERE e.datum = :datum")})
public class Elojegyzes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datum", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date datum;
    @JoinColumn(name = "szemely", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Szemely szemely;
    @JoinColumn(name = "konyv", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Konyv konyv;

    public Elojegyzes() {
    }

    public Elojegyzes(Integer id) {
        this.id = id;
    }

    public Elojegyzes(Integer id, Date datum) {
        this.id = id;
        this.datum = datum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Szemely getSzemely() {
        return szemely;
    }

    public void setSzemely(Szemely szemely) {
        this.szemely = szemely;
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
        if (!(object instanceof Elojegyzes)) {
            return false;
        }
        Elojegyzes other = (Elojegyzes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return szemely.getNev() + ", " + konyv.getCim() + ", " + datum;
    }
    
}
