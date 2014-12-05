/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirppis.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Opiframe
 */
@Entity
@Table(name = "viesti")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Viesti.findAll", query = "SELECT v FROM Viesti v"),
    @NamedQuery(name = "Viesti.findByViestiId", query = "SELECT v FROM Viesti v WHERE v.viestiId = :viestiId"),
    @NamedQuery(name = "Viesti.findByViestiluettu", query = "SELECT v FROM Viesti v WHERE v.viestiluettu = :viestiluettu"),
    @NamedQuery(name = "Viesti.findByLahetysaika", query = "SELECT v FROM Viesti v WHERE v.lahetysaika = :lahetysaika"),
    @NamedQuery(name = "Viesti.findByViestinrunko", query = "SELECT v FROM Viesti v WHERE v.viestinrunko = :viestinrunko"),
    @NamedQuery(name = "Viesti.findByJulkinenviesti", query = "SELECT v FROM Viesti v WHERE v.julkinenviesti = :julkinenviesti")})
public class Viesti implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Ilmoitus ilmoitusId;
    @JoinColumn(name = "lahettajaId", referencedColumnName = "kayttajaId")
    @ManyToOne(optional = false)
    private Kayttaja lahettajaId;

    public Viesti() {
        this.julkinenviesti = -1;
    }

    public Viesti(Integer viestiId) {
        this.viestiId = viestiId;
    }

    public Viesti(Integer viestiId, int viestiluettu, Date lahetysaika, String viestinrunko, int julkinenviesti) {
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

    public Ilmoitus getIlmoitusId() {
        return ilmoitusId;
    }

    public void setIlmoitusId(Ilmoitus ilmoitusId) {
        this.ilmoitusId = ilmoitusId;
    }

    public Kayttaja getLahettajaId() {
        return lahettajaId;
    }

    public void setLahettajaId(Kayttaja lahettajaId) {
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
        if (!(object instanceof Viesti)) {
            return false;
        }
        Viesti other = (Viesti) object;
        if ((this.viestiId == null && other.viestiId != null) || (this.viestiId != null && !this.viestiId.equals(other.viestiId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kirppis.data.Viesti[ viestiId=" + viestiId + " ]";
    }
    
    public String viestilahetysaikaToString() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm").format(lahetysaika);
    }
    
}
