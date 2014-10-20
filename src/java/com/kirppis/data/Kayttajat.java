/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirppis.data;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Opiframe
 */
@Entity
@Table(name = "kayttajat")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kayttajat.findAll", query = "SELECT k FROM Kayttajat k"),
    @NamedQuery(name = "Kayttajat.findByKayttajaId", query = "SELECT k FROM Kayttajat k WHERE k.kayttajaId = :kayttajaId"),
    @NamedQuery(name = "Kayttajat.findByFacabookkayttajatili", query = "SELECT k FROM Kayttajat k WHERE k.facabookkayttajatili = :facabookkayttajatili"),
    @NamedQuery(name = "Kayttajat.findByPostinumero", query = "SELECT k FROM Kayttajat k WHERE k.postinumero = :postinumero"),
    @NamedQuery(name = "Kayttajat.findByPuhelinnumero", query = "SELECT k FROM Kayttajat k WHERE k.puhelinnumero = :puhelinnumero"),
    @NamedQuery(name = "Kayttajat.findByPuhelinnumeronaytetaan", query = "SELECT k FROM Kayttajat k WHERE k.puhelinnumeronaytetaan = :puhelinnumeronaytetaan"),
    @NamedQuery(name = "Kayttajat.findBySahkoposti", query = "SELECT k FROM Kayttajat k WHERE k.sahkoposti = :sahkoposti"),
    @NamedQuery(name = "Kayttajat.findBySahkopostinaytetaan", query = "SELECT k FROM Kayttajat k WHERE k.sahkopostinaytetaan = :sahkopostinaytetaan")})
public class Kayttajat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "kayttajaId")
    private Integer kayttajaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "facabookkayttajatili")
    private String facabookkayttajatili;
    @Basic(optional = false)
    @NotNull
    @Column(name = "postinumero")
    private int postinumero;
    @Column(name = "puhelinnumero")
    private Integer puhelinnumero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "puhelinnumeronaytetaan")
    private int puhelinnumeronaytetaan;
    @Size(max = 255)
    @Column(name = "sahkoposti")
    private String sahkoposti;
    @Column(name = "sahkopostinaytetaan")
    private Integer sahkopostinaytetaan;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "myyjanId")
    private Collection<Ilmoitukset> ilmoituksetCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lahettajaId")
    private Collection<Viestit> viestitCollection;

    public Kayttajat() {
    }

    public Kayttajat(Integer kayttajaId) {
        this.kayttajaId = kayttajaId;
    }

    public Kayttajat(Integer kayttajaId, String facabookkayttajatili, int postinumero, int puhelinnumeronaytetaan) {
        this.kayttajaId = kayttajaId;
        this.facabookkayttajatili = facabookkayttajatili;
        this.postinumero = postinumero;
        this.puhelinnumeronaytetaan = puhelinnumeronaytetaan;
    }

    public Integer getKayttajaId() {
        return kayttajaId;
    }

    public void setKayttajaId(Integer kayttajaId) {
        this.kayttajaId = kayttajaId;
    }

    public String getFacabookkayttajatili() {
        return facabookkayttajatili;
    }

    public void setFacabookkayttajatili(String facabookkayttajatili) {
        this.facabookkayttajatili = facabookkayttajatili;
    }

    public int getPostinumero() {
        return postinumero;
    }

    public void setPostinumero(int postinumero) {
        this.postinumero = postinumero;
    }

    public Integer getPuhelinnumero() {
        return puhelinnumero;
    }

    public void setPuhelinnumero(Integer puhelinnumero) {
        this.puhelinnumero = puhelinnumero;
    }

    public int getPuhelinnumeronaytetaan() {
        return puhelinnumeronaytetaan;
    }

    public void setPuhelinnumeronaytetaan(int puhelinnumeronaytetaan) {
        this.puhelinnumeronaytetaan = puhelinnumeronaytetaan;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }

    public Integer getSahkopostinaytetaan() {
        return sahkopostinaytetaan;
    }

    public void setSahkopostinaytetaan(Integer sahkopostinaytetaan) {
        this.sahkopostinaytetaan = sahkopostinaytetaan;
    }

    @XmlTransient
    public Collection<Ilmoitukset> getIlmoituksetCollection() {
        return ilmoituksetCollection;
    }

    public void setIlmoituksetCollection(Collection<Ilmoitukset> ilmoituksetCollection) {
        this.ilmoituksetCollection = ilmoituksetCollection;
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
        hash += (kayttajaId != null ? kayttajaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kayttajat)) {
            return false;
        }
        Kayttajat other = (Kayttajat) object;
        if ((this.kayttajaId == null && other.kayttajaId != null) || (this.kayttajaId != null && !this.kayttajaId.equals(other.kayttajaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kirppis.data.Kayttajat[ kayttajaId=" + kayttajaId + " ]";
    }
    
}
