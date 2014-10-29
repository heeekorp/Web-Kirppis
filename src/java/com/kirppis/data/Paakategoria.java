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
@Table(name = "paakategoria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paakategoria.findAll", query = "SELECT p FROM Paakategoria p"),
    @NamedQuery(name = "Paakategoria.findByPaakategoriaId", query = "SELECT p FROM Paakategoria p WHERE p.paakategoriaId = :paakategoriaId"),
    @NamedQuery(name = "Paakategoria.findByPaakategoriannimi", query = "SELECT p FROM Paakategoria p WHERE p.paakategoriannimi = :paakategoriannimi")})
public class Paakategoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Collection<Valikategoria> valikategoriaCollection;

    public Paakategoria() {
    }

    public Paakategoria(Integer paakategoriaId) {
        this.paakategoriaId = paakategoriaId;
    }

    public Paakategoria(Integer paakategoriaId, String paakategoriannimi) {
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
    public Collection<Valikategoria> getValikategoriaCollection() {
        return valikategoriaCollection;
    }

    public void setValikategoriaCollection(Collection<Valikategoria> valikategoriaCollection) {
        this.valikategoriaCollection = valikategoriaCollection;
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
        if (!(object instanceof Paakategoria)) {
            return false;
        }
        Paakategoria other = (Paakategoria) object;
        if ((this.paakategoriaId == null && other.paakategoriaId != null) || (this.paakategoriaId != null && !this.paakategoriaId.equals(other.paakategoriaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kirppis.data.Paakategoria[ paakategoriaId=" + paakategoriaId + " ]";
    }
    
}
