/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 *
 * @author u201993
 */
@Entity
@Table(name = "kolcsonzes", catalog = "konyvtar", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"szemely", "peldany", "datum"})})
@NamedQueries({
    @NamedQuery(name = "Kolcsonzes.findAll", query = "SELECT k FROM Kolcsonzes k"),
    @NamedQuery(name = "Kolcsonzes.findById", query = "SELECT k FROM Kolcsonzes k WHERE k.id = :id"),
    @NamedQuery(name = "Kolcsonzes.findAllPeldanyBySzemely", query = "SELECT DISTINCT p FROM Kolcsonzes k, Szemely s, Peldany p where k.szemely = s and k.peldany = p and k.szemely = :szemely and p.aktKolcs = :aktKolcs and p.kikolcs = :kikolcs"),    
    @NamedQuery(name = "Kolcsonzes.findAllBySzemelyAndPeldany", query = "SELECT DISTINCT k FROM Kolcsonzes k, Szemely s, Peldany p where k.szemely = s and k.peldany = p and k.szemely = :szemely and k.peldany = :peldany"),    
    @NamedQuery(name = "Kolcsonzes.findAllPeldanyByKonyv", query = "SELECT p FROM Peldany p join p.konyv k where k = :konyv"),// and p.aktKolcs = :aktKolcs and p.kikolcs = :kikolcs"),    
    @NamedQuery(name = "Kolcsonzes.findByDatum", query = "SELECT k FROM Kolcsonzes k WHERE k.datum = :datum"),
    @NamedQuery(name = "Kolcsonzes.findByVisszahozDatum", query = "SELECT k FROM Kolcsonzes k WHERE k.visszahozDatum = :visszahozDatum"),
    @NamedQuery(name = "Kolcsonzes.findByFelszolit", query = "SELECT k FROM Kolcsonzes k WHERE k.felszolit = :felszolit"),
    @NamedQuery(name = "Kolcsonzes.findByMaxKolcs", query = "SELECT k FROM Kolcsonzes k WHERE k.maxKolcs = :maxKolcs")})
public class Kolcsonzes implements Serializable {

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
    @Column(name = "visszahoz_datum")
    @Temporal(TemporalType.DATE)
    private Date visszahozDatum;
    @Column(name = "felszolit")
    private Integer felszolit;
    @Basic(optional = false)
    @NotNull
    @Column(name = "max_kolcs", nullable = false)
    private int maxKolcs;
    @JoinColumn(name = "szemely", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Szemely szemely;
    @JoinColumn(name = "peldany", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Peldany peldany;
    @Transient
    private Date hatarido;
    @Transient
    private String kesesben;
    @Transient
    private final int NAPI_KESESI_DIJ = 10;

    public Kolcsonzes() {
    }

    public Kolcsonzes(Integer id) {
        this.id = id;
    }

    public Kolcsonzes(Integer id, Date datum, int maxKolcs) {
        this.id = id;
        this.datum = datum;
        this.maxKolcs = maxKolcs;
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

    public Date getVisszahozDatum() {
        return visszahozDatum;
    }

    public void setVisszahozDatum(Date visszahozDatum) {
        this.visszahozDatum = visszahozDatum;
    }

    public Integer getFelszolit() {
        return felszolit;
    }

    public void setFelszolit(Integer felszolit) {
        this.felszolit = felszolit;
    }

    public int getMaxKolcs() {
        return maxKolcs;
    }

    public void setMaxKolcs(int maxKolcs) {
        this.maxKolcs = maxKolcs;
    }

    public Szemely getSzemely() {
        return szemely;
    }

    public void setSzemely(Szemely szemely) {
        this.szemely = szemely;
    }

    public Peldany getPeldany() {
        return peldany;
    }

    public void setPeldany(Peldany peldany) {
        this.peldany = peldany;
    }
    
    public Date getHatarido() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(datum);
        calendar.add(Calendar.DATE, maxKolcs);
        return calendar.getTime();
    }
    
    public Integer getKesett() {
        Integer diffInDays = 0;
        if (visszahozDatum != null) {
            diffInDays =  (int)( (visszahozDatum.getTime() - getHatarido().getTime()) 
                 / (1000 * 60 * 60 * 24) );
        } else {
            diffInDays = (int)( ((new Date()).getTime() - getHatarido().getTime()) 
                 / (1000 * 60 * 60 * 24) );
        }
        if (0 >= diffInDays) {diffInDays = null;}
        return diffInDays;
    }
    
    public Integer getFizetendo() {
        if (getKesett() == null) {return null;}
        return getKesett() * NAPI_KESESI_DIJ;
    }
    
    public String getKesesben() {
        kesesben = getKesett() == null ? "":"kesesben";
        return kesesben;
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
        if (!(object instanceof Kolcsonzes)) {
            return false;
        }
        Kolcsonzes other = (Kolcsonzes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return szemely.getNev() + ", " + peldany + ", " + datum;
    }
    
}
