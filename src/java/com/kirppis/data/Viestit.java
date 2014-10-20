/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirppis.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Opiframe
 */
@Entity
@Table(name = "viestit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Viestit.findAll", query = "SELECT v FROM Viestit v"),
    @NamedQuery(name = "Viestit.findByViestiId", query = "SELECT v FROM Viestit v WHERE v.viestiId = :viestiId"),
    @NamedQuery(name = "Viestit.findByViestiluettu", query = "SELECT v FROM Viestit v WHERE v.viestiluettu = :viestiluettu"),
    @NamedQuery(name = "Viestit.findByLahetysaika", query = "SELECT v FROM Viestit v WHERE v.lahetysaika = :lahetysaika"),
    @NamedQuery(name = "Viestit.findByViestinrunko", query = "SELECT v FROM Viestit v WHERE v.viestinrunko = :viestinrunko"),
    @NamedQuery(name = "Viestit.findByJulkinenviesti", query = "SELECT v FROM Viestit v WHERE v.julkinenviesti = :julkinenviesti")})
public class Viestit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "viestiId")
    private Integer viestiId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "viestiluettu")
    private int viestiluettu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lahetysaika")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lahetysaika;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "viestinrunko")
    private String viestinrunko;
    @Basic(optional = false)
    @NotNull
    @Column(name = "julkinenviesti")
    private int julkinenviesti;
    @JoinColumn(name = "ilmoitusId", referencedColumnName = "ilmoitusId")
    @ManyToOne(optional = false)
    private Ilmoitukset ilmoitusId;
    @JoinColumn(name = "lahettajaId", referencedColumnName = "kayttajaId")
    @ManyToOne(optional = false)
    private Kayttajat lahettajaId;

    public Viestit() {
    }

    public Viestit(Integer viestiId) {
        this.viestiId = viestiId;
    }

    public Viestit(Integer viestiId, int viestiluettu, Date lahetysaika, String viestinrunko, int julkinenviesti) {
        this.viestiId = viestiId;
        this.viestiluettu = viestiluettu;
        this.lahetysaika = lahetysaika;
        this.viestinrunko = viestinrunko;
        this.julkinenviesti = julkinenviesti;
    }

    public Integer getViestiId() {
        return viestiId;
    }

    public void setViestiId(Integer viestiId) {
        this.viestiId = viestiId;
    }

    public int getViestiluettu() {
        return viestiluettu;
    }

    public void setViestiluettu(int viestiluettu) {
        this.viestiluettu = viestiluettu;
    }

    public Date getLahetysaika() {
        return lahetysaika;
    }

    public void setLahetysaika(Date lahetysaika) {
        this.lahetysaika = lahetysaika;
    }

    public String getViestinrunko() {
        return viestinrunko;
    }

    public void setViestinrunko(String viestinrunko) {
        this.viestinrunko = viestinrunko;
    }

    public int getJulkinenviesti() {
        return julkinenviesti;
    }

    public void setJulkinenviesti(int julkinenviesti) {
        this.julkinenviesti = julkinenviesti;
    }

    public Ilmoitukset getIlmoitusId() {
        return ilmoitusId;
    }

    public void setIlmoitusId(Ilmoitukset ilmoitusId) {
        this.ilmoitusId = ilmoitusId;
    }

    public Kayttajat getLahettajaId() {
        return lahettajaId;
    }

    public void setLahettajaId(Kayttajat lahettajaId) {
        this.lahettajaId = lahettajaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (viestiId != null ? viestiId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Viestit)) {
            return false;
        }
        Viestit other = (Viestit) object;
        if ((this.viestiId == null && other.viestiId != null) || (this.viestiId != null && !this.viestiId.equals(other.viestiId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kirppis.data.Viestit[ viestiId=" + viestiId + " ]";
    }
    
}
