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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author u201993
 */
@Entity
@Table(name = "polc", catalog = "konyvtar", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"szekreny", "polc"})})
@NamedQueries({
    @NamedQuery(name = "Polc.findAll", query = "SELECT p FROM Polc p"),
    @NamedQuery(name = "Polc.findById", query = "SELECT p FROM Polc p WHERE p.id = :id"),
    @NamedQuery(name = "Polc.findBySzekreny", query = "SELECT p FROM Polc p WHERE p.szekreny = :szekreny"),
    @NamedQuery(name = "Polc.findByPolc", query = "SELECT p FROM Polc p WHERE p.polc = :polc")})
public class Polc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Szekreny szekreny;
    @Basic(optional = false)
    @NotNull
    @Column(name = "polc", nullable = false)
    private int polc;
    
    @Transient
    private Szekreny[] szekrenyek;

    public Szekreny[] getSzekrenyek() {
        return Szekreny.values();
    }    

    public Polc() {
    }

    public Polc(Integer id) {
        this.id = id;
    }

    public Polc(Integer id, String szekreny, int polc) {
        this.id = id;
        this.polc = polc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Szekreny getSzekreny() {
        return szekreny;
    }

    public void setSzekreny(Szekreny szekreny) {
        this.szekreny = szekreny;
    }

    public int getPolc() {
        return polc;
    }

    public void setPolc(int polc) {
        this.polc = polc;
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
        if (!(object instanceof Polc)) {
            return false;
        }
        Polc other = (Polc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return szekreny + "" + polc;
    }
    
}
