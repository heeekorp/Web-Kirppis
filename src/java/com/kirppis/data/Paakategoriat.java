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
@Table(name = "paakategoriat")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paakategoriat.findAll", query = "SELECT p FROM Paakategoriat p"),
    @NamedQuery(name = "Paakategoriat.findByPaakategoriaId", query = "SELECT p FROM Paakategoriat p WHERE p.paakategoriaId = :paakategoriaId"),
    @NamedQuery(name = "Paakategoriat.findByPaakategoriannimi", query = "SELECT p FROM Paakategoriat p WHERE p.paakategoriannimi = :paakategoriannimi")})
public class Paakategoriat implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "paakategoriaId")
    private Integer paakategoriaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "paakategoriannimi")
    private String paakategoriannimi;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paakategoriaId")
    private Collection<Valikategoriat> valikategoriatCollection;

    public Paakategoriat() {
    }

    public Paakategoriat(Integer paakategoriaId) {
        this.paakategoriaId = paakategoriaId;
    }

    public Paakategoriat(Integer paakategoriaId, String paakategoriannimi) {
        this.paakategoriaId = paakategoriaId;
        this.paakategoriannimi = paakategoriannimi;
    }

    public Integer getPaakategoriaId() {
        return paakategoriaId;
    }

    public void setPaakategoriaId(Integer paakategoriaId) {
        this.paakategoriaId = paakategoriaId;
    }

    public String getPaakategoriannimi() {
        return paakategoriannimi;
    }

    public void setPaakategoriannimi(String paakategoriannimi) {
        this.paakategoriannimi = paakategoriannimi;
    }

    @XmlTransient
    public Collection<Valikategoriat> getValikategoriatCollection() {
        return valikategoriatCollection;
    }

    public void setValikategoriatCollection(Collection<Valikategoriat> valikategoriatCollection) {
        this.valikategoriatCollection = valikategoriatCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paakategoriaId != null ? paakategoriaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paakategoriat)) {
            return false;
        }
        Paakategoriat other = (Paakategoriat) object;
        if ((this.paakategoriaId == null && other.paakategoriaId != null) || (this.paakategoriaId != null && !this.paakategoriaId.equals(other.paakategoriaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kirppis.data.Paakategoriat[ paakategoriaId=" + paakategoriaId + " ]";
    }
    
}
