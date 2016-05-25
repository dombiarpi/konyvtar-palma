/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author u201993
 */
@Entity
@Table(name = "szemely", catalog = "konyvtar", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nev", "email", "mobil", "cim"})})
@NamedQueries({
    @NamedQuery(name = "Szemely.findAll", query = "SELECT s FROM Szemely s"),
    @NamedQuery(name = "Szemely.findById", query = "SELECT s FROM Szemely s WHERE s.id = :id"),
    @NamedQuery(name = "Szemely.findByNev", query = "SELECT s FROM Szemely s WHERE s.nev = :nev"),
    @NamedQuery(name = "Szemely.findByLeanykoriNev", query = "SELECT s FROM Szemely s WHERE s.leanykoriNev = :leanykoriNev"),
    @NamedQuery(name = "Szemely.findByBeiratkozas", query = "SELECT s FROM Szemely s WHERE s.beiratkozas = :beiratkozas"),
    @NamedQuery(name = "Szemely.findByElofizTipus", query = "SELECT s FROM Szemely s WHERE s.elofizTipus = :elofizTipus"),
    @NamedQuery(name = "Szemely.findByTagdij", query = "SELECT s FROM Szemely s WHERE s.tagdij = :tagdij"),
    @NamedQuery(name = "Szemely.findByEmail", query = "SELECT s FROM Szemely s WHERE s.email = :email"),
    @NamedQuery(name = "Szemely.findByMobil", query = "SELECT s FROM Szemely s WHERE s.mobil = :mobil"),
    @NamedQuery(name = "Szemely.findByCim", query = "SELECT s FROM Szemely s WHERE s.cim = :cim"),
    @NamedQuery(name = "Szemely.findByMegjegyzes", query = "SELECT s FROM Szemely s WHERE s.megjegyzes = :megjegyzes")})
public class Szemely implements Serializable {

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
    @Size(max = 50)
    @Column(name = "leanykori_nev", length = 50)
    private String leanykoriNev;
    @Basic(optional = false)
    @NotNull
    @Column(name = "beiratkozas", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date beiratkozas;
    @Basic(optional = true)
    @Column(name = "elofiz_datum", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date elofizDatum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 39)
    @Column(name = "elofiz_tipus", nullable = false, length = 39)
    private String elofizTipus;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tagdij", nullable = false)
    private int tagdij;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;
    @Column(name = "mobil")
    private Integer mobil;
    @Size(max = 50)
    @Column(name = "cim", length = 50)
    private String cim;
    @Size(max = 100)
    @Column(name = "megjegyzes", length = 100)
    private String megjegyzes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "szemely")
    private Collection<Elojegyzes> elojegyzesCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "szemely")
    private Collection<Kolcsonzes> kolcsonzesCollection;
    @Transient
    private ElofizTipus[] elofizTipusok;

    public ElofizTipus[] getElofizTipusok() {
        return ElofizTipus.values();
    }

    public Szemely() {
    }

    public Szemely(Integer id) {
        this.id = id;
    }

    public Szemely(Integer id, String nev, Date beiratkozas, String elofizTipus, int tagdij) {
        this.id = id;
        this.nev = nev;
        this.beiratkozas = beiratkozas;
        this.elofizTipus = elofizTipus;
        this.tagdij = tagdij;
    }
    
    public String getKesesben() {
        if (ElofizTipus.SPECIALIS.toString().equalsIgnoreCase(elofizTipus)) {
            return ""; // speciálisok nem fizetnek elő
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        Calendar befizetve = Calendar.getInstance();
        if (elofizDatum != null) {
            befizetve.setTime(elofizDatum);
        } else {
            return "kesesben"; // aki beíratkozott már és még nem fizetett mind késében van
        }
        return befizetve.before(cal) ? "kesesben" : ""; // egy év után lejár ez előfizetés
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

    public String getLeanykoriNev() {
        return leanykoriNev;
    }

    public void setLeanykoriNev(String leanykoriNev) {
        this.leanykoriNev = leanykoriNev;
    }

    public Date getBeiratkozas() {
        return beiratkozas;
    }

    public void setBeiratkozas(Date beiratkozas) {
        this.beiratkozas = beiratkozas;
    }

    public String getElofizTipus() {
        return elofizTipus;
    }

    public void setElofizTipus(String elofizTipus) {
        this.elofizTipus = elofizTipus;
    }

    public int getTagdij() {
        return tagdij;
    }

    public void setTagdij(int tagdij) {
        this.tagdij = tagdij;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMobil() {
        return mobil;
    }

    public void setMobil(Integer mobil) {
        this.mobil = mobil;
    }

    public String getCim() {
        return cim;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public String getMegjegyzes() {
        return megjegyzes;
    }

    public void setMegjegyzes(String megjegyzes) {
        this.megjegyzes = megjegyzes;
    }

    public Collection<Elojegyzes> getElojegyzesCollection() {
        return elojegyzesCollection;
    }

    public void setElojegyzesCollection(Collection<Elojegyzes> elojegyzesCollection) {
        this.elojegyzesCollection = elojegyzesCollection;
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
        if (!(object instanceof Szemely)) {
            return false;
        }
        Szemely other = (Szemely) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nev + (leanykoriNev == null ? "": (" " + leanykoriNev));
    }

    /**
     * @return the elofizDatum
     */
    public Date getElofizDatum() {
        return elofizDatum;
    }

    /**
     * @param elofizDatum the elofizDatum to set
     */
    public void setElofizDatum(Date elofizDatum) {
        this.elofizDatum = elofizDatum;
    }
    
}
