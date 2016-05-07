/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author u201993
 */
@Entity
@Table(name = "nincs", catalog = "konyvtar", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"peldany"})})
@NamedQueries({
    @NamedQuery(name = "Nincs.findAll", query = "SELECT n FROM Nincs n"),
    @NamedQuery(name = "Nincs.findById", query = "SELECT n FROM Nincs n WHERE n.id = :id"),
    @NamedQuery(name = "Nincs.findByPeldany", query = "SELECT n FROM Nincs n WHERE n.peldany = :peldany"),
    @NamedQuery(name = "Nincs.findByKelt", query = "SELECT n FROM Nincs n WHERE n.kelt = :kelt"),
    @NamedQuery(name = "Nincs.findBySelejtLopott", query = "SELECT n FROM Nincs n WHERE n.selejtLopott = :selejtLopott")})
public class Nincs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @JoinColumn(name = "peldany", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)    
    private Peldany peldany;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kelt", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date kelt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "selejt_lopott", nullable = false, length = 2)
    private String selejtLopott;

    public Nincs() {
    }

    public Nincs(Integer id) {
        this.id = id;
    }

    public Nincs(Integer id, Date kelt, String selejtLopott) {
        this.id = id;

        this.kelt = kelt;
        this.selejtLopott = selejtLopott;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Peldany getPeldany() {
        return peldany;
    }

    public void setPeldany(Peldany peldany) {
        this.peldany = peldany;
    }

    public Date getKelt() {
        return kelt;
    }

    public void setKelt(Date kelt) {
        this.kelt = kelt;
    }

    public String getSelejtLopott() {
        return selejtLopott;
    }

    public void setSelejtLopott(String selejtLopott) {
        this.selejtLopott = selejtLopott;
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
        if (!(object instanceof Nincs)) {
            return false;
        }
        Nincs other = (Nincs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return peldany + ", " + selejtLopott + ", " + kelt;
    }
    
}
