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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "valikategoriat")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Valikategoriat.findAll", query = "SELECT v FROM Valikategoriat v"),
    @NamedQuery(name = "Valikategoriat.findByValikategoriaId", query = "SELECT v FROM Valikategoriat v WHERE v.valikategoriaId = :valikategoriaId"),
    @NamedQuery(name = "Valikategoriat.findByValikategoriannimi", query = "SELECT v FROM Valikategoriat v WHERE v.valikategoriannimi = :valikategoriannimi")})
public class Valikategoriat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "valikategoriaId")
    private Integer valikategoriaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "valikategoriannimi")
    private String valikategoriannimi;
    @JoinColumn(name = "paakategoriaId", referencedColumnName = "paakategoriaId")
    @ManyToOne(optional = false)
    private Paakategoriat paakategoriaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valikategoriaId")
    private Collection<Alakategoriat> alakategoriatCollection;

    public Valikategoriat() {
    }

    public Valikategoriat(Integer valikategoriaId) {
        this.valikategoriaId = valikategoriaId;
    }

    public Valikategoriat(Integer valikategoriaId, String valikategoriannimi) {
        this.valikategoriaId = valikategoriaId;
        this.valikategoriannimi = valikategoriannimi;
    }

    public Integer getValikategoriaId() {
        return valikategoriaId;
    }

    public void setValikategoriaId(Integer valikategoriaId) {
        this.valikategoriaId = valikategoriaId;
    }

    public String getValikategoriannimi() {
        return valikategoriannimi;
    }

    public void setValikategoriannimi(String valikategoriannimi) {
        this.valikategoriannimi = valikategoriannimi;
    }

    public Paakategoriat getPaakategoriaId() {
        return paakategoriaId;
    }

    public void setPaakategoriaId(Paakategoriat paakategoriaId) {
        this.paakategoriaId = paakategoriaId;
    }

    @XmlTransient
    public Collection<Alakategoriat> getAlakategoriatCollection() {
        return alakategoriatCollection;
    }

    public void setAlakategoriatCollection(Collection<Alakategoriat> alakategoriatCollection) {
        this.alakategoriatCollection = alakategoriatCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (valikategoriaId != null ? valikategoriaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Valikategoriat)) {
            return false;
        }
        Valikategoriat other = (Valikategoriat) object;
        if ((this.valikategoriaId == null && other.valikategoriaId != null) || (this.valikategoriaId != null && !this.valikategoriaId.equals(other.valikategoriaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kirppis.data.Valikategoriat[ valikategoriaId=" + valikategoriaId + " ]";
    }
    
}
