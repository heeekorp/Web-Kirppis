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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "kayttaja")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Kayttaja.findAll", query = "SELECT k FROM Kayttaja k"),
    @NamedQuery(name = "Kayttaja.findByKayttajaId", query = "SELECT k FROM Kayttaja k WHERE k.kayttajaId = :kayttajaId"),
    @NamedQuery(name = "Kayttaja.findByFacabookkayttajatili", query = "SELECT k FROM Kayttaja k WHERE k.facabookkayttajatili = :facabookkayttajatili"),
    @NamedQuery(name = "Kayttaja.findByPostinumero", query = "SELECT k FROM Kayttaja k WHERE k.postinumero = :postinumero"),
    @NamedQuery(name = "Kayttaja.findByPuhelinnumero", query = "SELECT k FROM Kayttaja k WHERE k.puhelinnumero = :puhelinnumero"),
    @NamedQuery(name = "Kayttaja.findByPuhelinnumeronaytetaan", query = "SELECT k FROM Kayttaja k WHERE k.puhelinnumeronaytetaan = :puhelinnumeronaytetaan"),
    @NamedQuery(name = "Kayttaja.findBySahkoposti", query = "SELECT k FROM Kayttaja k WHERE k.sahkoposti = :sahkoposti"),
    @NamedQuery(name = "Kayttaja.findBySahkopostinaytetaan", query = "SELECT k FROM Kayttaja k WHERE k.sahkopostinaytetaan = :sahkopostinaytetaan")})
public class Kayttaja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Collection<Ilmoitus> ilmoitusCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lahettajaId")
    private Collection<Viesti> viestiCollection;

    public Kayttaja() {
    }

    public Kayttaja(Integer kayttajaId) {
        this.kayttajaId = kayttajaId;
    }

    public Kayttaja(Integer kayttajaId, String facabookkayttajatili, int postinumero, int puhelinnumeronaytetaan) {
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
    public Collection<Ilmoitus> getIlmoitusCollection() {
        return ilmoitusCollection;
    }

    public void setIlmoitusCollection(Collection<Ilmoitus> ilmoitusCollection) {
        this.ilmoitusCollection = ilmoitusCollection;
    }

    @XmlTransient
    public Collection<Viesti> getViestiCollection() {
        return viestiCollection;
    }

    public void setViestiCollection(Collection<Viesti> viestiCollection) {
        this.viestiCollection = viestiCollection;
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
        if (!(object instanceof Kayttaja)) {
            return false;
        }
        Kayttaja other = (Kayttaja) object;
        if ((this.kayttajaId == null && other.kayttajaId != null) || (this.kayttajaId != null && !this.kayttajaId.equals(other.kayttajaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kirppis.data.Kayttaja[ kayttajaId=" + kayttajaId + " ]";
    }
        
    public String puhelinnumeroToString() {
        return "+358" + puhelinnumero;
    }
}
