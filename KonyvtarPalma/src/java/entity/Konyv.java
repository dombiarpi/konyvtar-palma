/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
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
@Table(name = "konyv", catalog = "konyvtar", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cim", "kiado", "kiadev"}),
    @UniqueConstraint(columnNames = {"katal"})})
@NamedQueries({
    @NamedQuery(name = "Konyv.findAll", query = "SELECT k FROM Konyv k"),
    @NamedQuery(name = "Konyv.findById", query = "SELECT k FROM Konyv k WHERE k.id = :id"),
    @NamedQuery(name = "Konyv.findByKatal", query = "SELECT k FROM Konyv k WHERE k.katal = :katal"),
    @NamedQuery(name = "Konyv.findByCim", query = "SELECT k FROM Konyv k WHERE k.cim = :cim"),
    @NamedQuery(name = "Konyv.findByAlcim", query = "SELECT k FROM Konyv k WHERE k.alcim = :alcim"),
    @NamedQuery(name = "Konyv.findByKiadszam", query = "SELECT k FROM Konyv k WHERE k.kiadszam = :kiadszam"),
    @NamedQuery(name = "Konyv.findByKiadev", query = "SELECT k FROM Konyv k WHERE k.kiadev = :kiadev"),
    @NamedQuery(name = "Konyv.findByMufaj", query = "SELECT k FROM Konyv k WHERE k.mufaj = :mufaj"),
    @NamedQuery(name = "Konyv.findByBeszerzesiAr", query = "SELECT k FROM Konyv k WHERE k.beszerzesiAr = :beszerzesiAr"),
    @NamedQuery(name = "Konyv.findByEgysAr", query = "SELECT k FROM Konyv k WHERE k.egysAr = :egysAr"),
    @NamedQuery(name = "Konyv.findBySzorzo", query = "SELECT k FROM Konyv k WHERE k.szorzo = :szorzo"),
    @NamedQuery(name = "Konyv.findByMegjegyzes", query = "SELECT k FROM Konyv k WHERE k.megjegyzes = :megjegyzes"),
    @NamedQuery(name = "Konyv.findByMedia", query = "SELECT k FROM Konyv k WHERE k.media = :media"),
    @NamedQuery(name = "Konyv.findByNyelv", query = "SELECT k FROM Konyv k WHERE k.nyelv = :nyelv")})
public class Konyv implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "katal", nullable = false)
    private int katal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "cim", nullable = false, length = 100)
    private String cim;
    @Size(max = 100)
    @Column(name = "alcim", length = 100)
    private String alcim;
    @Column(name = "kiadszam")
    private Integer kiadszam;
    @Column(name = "kiadev")
    @Temporal(TemporalType.DATE)
    private Date kiadev;
    @Basic(optional = false)
    @NotNull
    @Column(name = "mufaj", nullable = false)
    private int mufaj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "beszerzesi_ar", nullable = false)
    private int beszerzesiAr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "egys_ar", nullable = false)
    private int egysAr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "szorzo", nullable = false)
    private double szorzo;
    @Size(max = 50)
    @Column(name = "megjegyzes", length = 50)
    private String megjegyzes;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 42)
    @Column(name = "media", nullable = false, length = 42)
    private String media;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nyelv", nullable = false)
    private int nyelv;
    @JoinColumn(name = "sorozat", referencedColumnName = "id")
    @ManyToOne
    private Sorozat sorozat;
    @JoinColumn(name = "kiado", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Kiado kiado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "konyv")
    private Collection<Utalas> utalasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "konyv")
    private Collection<Elojegyzes> elojegyzesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "konyv")
    private Collection<Kimitirt> kimitirtCollection;

    public Konyv() {
    }

    public Konyv(Integer id) {
        this.id = id;
    }

    public Konyv(Integer id, int katal, String cim, int mufaj, int beszerzesiAr, int egysAr, double szorzo, String media, int nyelv) {
        this.id = id;
        this.katal = katal;
        this.cim = cim;
        this.mufaj = mufaj;
        this.beszerzesiAr = beszerzesiAr;
        this.egysAr = egysAr;
        this.szorzo = szorzo;
        this.media = media;
        this.nyelv = nyelv;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getKatal() {
        return katal;
    }

    public void setKatal(int katal) {
        this.katal = katal;
    }

    public String getCim() {
        return cim;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public String getAlcim() {
        return alcim;
    }

    public void setAlcim(String alcim) {
        this.alcim = alcim;
    }

    public Integer getKiadszam() {
        return kiadszam;
    }

    public void setKiadszam(Integer kiadszam) {
        this.kiadszam = kiadszam;
    }

    public Date getKiadev() {
        return kiadev;
    }

    public void setKiadev(Date kiadev) {
        this.kiadev = kiadev;
    }

    public int getMufaj() {
        return mufaj;
    }

    public void setMufaj(int mufaj) {
        this.mufaj = mufaj;
    }

    public int getBeszerzesiAr() {
        return beszerzesiAr;
    }

    public void setBeszerzesiAr(int beszerzesiAr) {
        this.beszerzesiAr = beszerzesiAr;
    }

    public int getEgysAr() {
        return egysAr;
    }

    public void setEgysAr(int egysAr) {
        this.egysAr = egysAr;
    }

    public double getSzorzo() {
        return szorzo;
    }

    public void setSzorzo(double szorzo) {
        this.szorzo = szorzo;
    }

    public String getMegjegyzes() {
        return megjegyzes;
    }

    public void setMegjegyzes(String megjegyzes) {
        this.megjegyzes = megjegyzes;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public int getNyelv() {
        return nyelv;
    }

    public void setNyelv(int nyelv) {
        this.nyelv = nyelv;
    }

    public Sorozat getSorozat() {
        return sorozat;
    }

    public void setSorozat(Sorozat sorozat) {
        this.sorozat = sorozat;
    }

    public Kiado getKiado() {
        return kiado;
    }

    public void setKiado(Kiado kiado) {
        this.kiado = kiado;
    }

    public Collection<Utalas> getUtalasCollection() {
        return utalasCollection;
    }

    public void setUtalasCollection(Collection<Utalas> utalasCollection) {
        this.utalasCollection = utalasCollection;
    }

    public Collection<Elojegyzes> getElojegyzesCollection() {
        return elojegyzesCollection;
    }

    public void setElojegyzesCollection(Collection<Elojegyzes> elojegyzesCollection) {
        this.elojegyzesCollection = elojegyzesCollection;
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
        if (!(object instanceof Konyv)) {
            return false;
        }
        Konyv other = (Konyv) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return cim + ", " + kiado.getNev() + ", " + kiadev;
    }
    
}
