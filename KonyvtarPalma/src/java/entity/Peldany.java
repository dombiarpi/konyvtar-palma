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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "peldany", catalog = "konyvtar", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"konyv", "konyv_peldany"})})
@NamedQueries({
    @NamedQuery(name = "Peldany.findAll", query = "SELECT p FROM Peldany p"),
    @NamedQuery(name = "Peldany.findById", query = "SELECT p FROM Peldany p WHERE p.id = :id"),
    @NamedQuery(name = "Peldany.findByKonyv", query = "SELECT p FROM Peldany p WHERE p.konyv = :konyv"),
    @NamedQuery(name = "Peldany.findByKonyvPeldany", query = "SELECT p FROM Peldany p WHERE p.konyvPeldany = :konyvPeldany"),
    @NamedQuery(name = "Peldany.findByKikolcs", query = "SELECT p FROM Peldany p WHERE p.kikolcs = :kikolcs"),
    @NamedQuery(name = "Peldany.findByAktKolcs", query = "SELECT p FROM Peldany p WHERE p.aktKolcs = :aktKolcs"),
    @NamedQuery(name = "Peldany.findByPolc", query = "SELECT p FROM Peldany p WHERE p.polc = :polc")})
public class Peldany implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @JoinColumn(name = "konyv", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)    
    private Konyv konyv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "konyv_peldany", nullable = false)
    private int konyvPeldany;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "kikolcs", nullable = false, length = 2)
    private String kikolcs;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "akt_kolcs", nullable = false, length = 15)
    private String aktKolcs;
    @JoinColumn(name = "polc", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)    
    private Polc polc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "peldany")
    private Collection<Kolcsonzes> kolcsonzesCollection;

    public Peldany() {
    }

    public Peldany(Integer id) {
        this.id = id;
    }

    public Peldany(Integer id, int konyvPeldany, String kikolcs, String aktKolcs) {
        this.id = id;
        this.konyvPeldany = konyvPeldany;
        this.kikolcs = kikolcs;
        this.aktKolcs = aktKolcs;
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

    public int getKonyvPeldany() {
        return konyvPeldany;
    }

    public void setKonyvPeldany(int konyvPeldany) {
        this.konyvPeldany = konyvPeldany;
    }

    public String getKikolcs() {
        return kikolcs;
    }

    public void setKikolcs(String kikolcs) {
        this.kikolcs = kikolcs;
    }

    public String getAktKolcs() {
        return aktKolcs;
    }

    public void setAktKolcs(String aktKolcs) {
        this.aktKolcs = aktKolcs;
    }

    public Polc getPolc() {
        return polc;
    }

    public void setPolc(Polc polc) {
        this.polc = polc;
    }

    public Collection<Kolcsonzes> getKolcsonzesCollection() {
        return kolcsonzesCollection;
    }

    public void setKolcsonzesCollection(Collection<Kolcsonzes> kolcsonzesCollection) {
        this.kolcsonzesCollection = kolcsonzesCollection;
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
        if (!(object instanceof Peldany)) {
            return false;
        }
        Peldany other = (Peldany) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return konyv + " - " + konyvPeldany;
    }
    
}
