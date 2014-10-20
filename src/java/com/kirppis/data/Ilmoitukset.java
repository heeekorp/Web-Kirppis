/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirppis.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Opiframe
 */
@Entity
@Table(name = "ilmoitukset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ilmoitukset.findAll", query = "SELECT i FROM Ilmoitukset i"),
    @NamedQuery(name = "Ilmoitukset.findByIlmoitusId", query = "SELECT i FROM Ilmoitukset i WHERE i.ilmoitusId = :ilmoitusId"),
    @NamedQuery(name = "Ilmoitukset.findByOtsikko", query = "SELECT i FROM Ilmoitukset i WHERE i.otsikko = :otsikko"),
    @NamedQuery(name = "Ilmoitukset.findByKuvienpolku", query = "SELECT i FROM Ilmoitukset i WHERE i.kuvienpolku = :kuvienpolku"),
    @NamedQuery(name = "Ilmoitukset.findByHinta", query = "SELECT i FROM Ilmoitukset i WHERE i.hinta = :hinta"),
    @NamedQuery(name = "Ilmoitukset.findByKuvaus", query = "SELECT i FROM Ilmoitukset i WHERE i.kuvaus = :kuvaus"),
    @NamedQuery(name = "Ilmoitukset.findByTuotteenkunto", query = "SELECT i FROM Ilmoitukset i WHERE i.tuotteenkunto = :tuotteenkunto"),
    @NamedQuery(name = "Ilmoitukset.findByIlmoitusjatettyPvm", query = "SELECT i FROM Ilmoitukset i WHERE i.ilmoitusjatettyPvm = :ilmoitusjatettyPvm")})
public class Ilmoitukset implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ilmoitusId")
    private Integer ilmoitusId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "otsikko")
    private String otsikko;
    @Size(max = 255)
    @Column(name = "kuvienpolku")
    private String kuvienpolku;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "hinta")
    private Float hinta;
    @Size(max = 2000)
    @Column(name = "kuvaus")
    private String kuvaus;
    @Column(name = "tuotteenkunto")
    private Integer tuotteenkunto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ilmoitusjatettyPvm")
    @Temporal(TemporalType.DATE)
    private Date ilmoitusjatettyPvm;
    @JoinColumn(name = "myyjanId", referencedColumnName = "kayttajaId")
    @ManyToOne(optional = false)
    private Kayttajat myyjanId;
    @JoinColumn(name = "alakategoriaId", referencedColumnName = "alakategoriaId")
    @ManyToOne(optional = false)
    private Alakategoriat alakategoriaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ilmoitusId")
    private Collection<Viestit> viestitCollection;

    public Ilmoitukset() {
    }

    public Ilmoitukset(Integer ilmoitusId) {
        this.ilmoitusId = ilmoitusId;
    }

    public Ilmoitukset(Integer ilmoitusId, String otsikko, Date ilmoitusjatettyPvm) {
        this.ilmoitusId = ilmoitusId;
        this.otsikko = otsikko;
        this.ilmoitusjatettyPvm = ilmoitusjatettyPvm;
    }

    public Integer getIlmoitusId() {
        return ilmoitusId;
    }

    public void setIlmoitusId(Integer ilmoitusId) {
        this.ilmoitusId = ilmoitusId;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public String getKuvienpolku() {
        return kuvienpolku;
    }

    public void setKuvienpolku(String kuvienpolku) {
        this.kuvienpolku = kuvienpolku;
    }

    public Float getHinta() {
        return hinta;
    }

    public void setHinta(Float hinta) {
        this.hinta = hinta;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public Integer getTuotteenkunto() {
        return tuotteenkunto;
    }

    public void setTuotteenkunto(Integer tuotteenkunto) {
        this.tuotteenkunto = tuotteenkunto;
    }

    public Date getIlmoitusjatettyPvm() {
        return ilmoitusjatettyPvm;
    }

    public void setIlmoitusjatettyPvm(Date ilmoitusjatettyPvm) {
        this.ilmoitusjatettyPvm = ilmoitusjatettyPvm;
    }

    public Kayttajat getMyyjanId() {
        return myyjanId;
    }

    public void setMyyjanId(Kayttajat myyjanId) {
        this.myyjanId = myyjanId;
    }

    public Alakategoriat getAlakategoriaId() {
        return alakategoriaId;
    }

    public void setAlakategoriaId(Alakategoriat alakategoriaId) {
        this.alakategoriaId = alakategoriaId;
    }

    @XmlTransient
    public Collection<Viestit> getViestitCollection() {
        return viestitCollection;
    }

    public void setViestitCollection(Collection<Viestit> viestitCollection) {
        this.viestitCollection = viestitCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ilmoitusId != null ? ilmoitusId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ilmoitukset)) {
            return false;
        }
        Ilmoitukset other = (Ilmoitukset) object;
        if ((this.ilmoitusId == null && other.ilmoitusId != null) || (this.ilmoitusId != null && !this.ilmoitusId.equals(other.ilmoitusId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kirppis.data.Ilmoitukset[ ilmoitusId=" + ilmoitusId + " ]";
    }
    
}
