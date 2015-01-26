/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirppis.data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Hezu
 */
@Entity
@Table(name = "muistilista")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Muistilista.findAll", query = "SELECT m FROM Muistilista m"),
    @NamedQuery(name = "Muistilista.findByKayttajaId", query = "SELECT m FROM Muistilista m WHERE m.kayttajaId = :kayttajaId"),
    @NamedQuery(name = "Muistilista.findByIlmoitusId", query = "SELECT m FROM Muistilista m WHERE m.ilmoitusId = :ilmoitusId"),
    @NamedQuery(name = "Muistilista.findByMuistilistaId", query = "SELECT m FROM Muistilista m WHERE m.muistilistaId = :muistilistaId")})
public class Muistilista implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kayttajaId")
    private int kayttajaId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ilmoitusId")
    private int ilmoitusId;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "muistilistaId")
    private Integer muistilistaId;

    public Muistilista() {
    }

    public Muistilista(Integer muistilistaId) {
        this.muistilistaId = muistilistaId;
    }

    public Muistilista(Integer muistilistaId, int kayttajaId, int ilmoitusId) {
        this.muistilistaId = muistilistaId;
        this.kayttajaId = kayttajaId;
        this.ilmoitusId = ilmoitusId;
    }

    public int getKayttajaId() {
        return kayttajaId;
    }

    public void setKayttajaId(int kayttajaId) {
        this.kayttajaId = kayttajaId;
    }

    public int getIlmoitusId() {
        return ilmoitusId;
    }

    public void setIlmoitusId(int ilmoitusId) {
        this.ilmoitusId = ilmoitusId;
    }

    public Integer getMuistilistaId() {
        return muistilistaId;
    }

    public void setMuistilistaId(Integer muistilistaId) {
        this.muistilistaId = muistilistaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (muistilistaId != null ? muistilistaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Muistilista)) {
            return false;
        }
        Muistilista other = (Muistilista) object;
        if ((this.muistilistaId == null && other.muistilistaId != null) || (this.muistilistaId != null && !this.muistilistaId.equals(other.muistilistaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kirppis.data.Muistilista[ muistilistaId=" + muistilistaId + " ]";
    }
    
}
