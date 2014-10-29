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
@Table(name = "valikategoria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Valikategoria.findAll", query = "SELECT v FROM Valikategoria v"),
    @NamedQuery(name = "Valikategoria.findByValikategoriaId", query = "SELECT v FROM Valikategoria v WHERE v.valikategoriaId = :valikategoriaId"),
    @NamedQuery(name = "Valikategoria.findByValikategoriannimi", query = "SELECT v FROM Valikategoria v WHERE v.valikategoriannimi = :valikategoriannimi")})
public class Valikategoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @NotNull
    @Column(name = "valikategoriaId")
    private Integer valikategoriaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "valikategoriannimi")
    private String valikategoriannimi;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "valikategoriaId")
    private Collection<Alakategoria> alakategoriaCollection;
    @JoinColumn(name = "paakategoriaId", referencedColumnName = "paakategoriaId")
    @ManyToOne(optional = false)
    private Paakategoria paakategoriaId;

    public Valikategoria() {
    }

    public Valikategoria(Integer valikategoriaId) {
        this.valikategoriaId = valikategoriaId;
    }

    public Valikategoria(Integer valikategoriaId, String valikategoriannimi) {
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

    @XmlTransient
    public Collection<Alakategoria> getAlakategoriaCollection() {
        return alakategoriaCollection;
    }

    public void setAlakategoriaCollection(Collection<Alakategoria> alakategoriaCollection) {
        this.alakategoriaCollection = alakategoriaCollection;
    }

    public Paakategoria getPaakategoriaId() {
        return paakategoriaId;
    }

    public void setPaakategoriaId(Paakategoria paakategoriaId) {
        this.paakategoriaId = paakategoriaId;
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
        if (!(object instanceof Valikategoria)) {
            return false;
        }
        Valikategoria other = (Valikategoria) object;
        if ((this.valikategoriaId == null && other.valikategoriaId != null) || (this.valikategoriaId != null && !this.valikategoriaId.equals(other.valikategoriaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kirppis.data.Valikategoria[ valikategoriaId=" + valikategoriaId + " ]";
    }
    
}
