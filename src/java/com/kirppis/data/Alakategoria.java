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
@Table(name = "alakategoria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alakategoria.findAll", query = "SELECT a FROM Alakategoria a"),
    @NamedQuery(name = "Alakategoria.findByAlakategoriaId", query = "SELECT a FROM Alakategoria a WHERE a.alakategoriaId = :alakategoriaId"),
    @NamedQuery(name = "Alakategoria.findByAlakategorianimi", query = "SELECT a FROM Alakategoria a WHERE a.alakategorianimi = :alakategorianimi")})
public class Alakategoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "alakategoriaId")
    private Integer alakategoriaId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "alakategorianimi")
    private String alakategorianimi;
    @JoinColumn(name = "valikategoriaId", referencedColumnName = "valikategoriaId")
    @ManyToOne(optional = false)
    private Valikategoria valikategoriaId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alakategoriaId")
    private Collection<Ilmoitus> ilmoitusCollection;

    public Alakategoria() {
    }

    public Alakategoria(Integer alakategoriaId) {
        this.alakategoriaId = alakategoriaId;
    }

    public Alakategoria(Integer alakategoriaId, String alakategorianimi) {
        this.alakategoriaId = alakategoriaId;
        this.alakategorianimi = alakategorianimi;
    }

    public Integer getAlakategoriaId() {
        return alakategoriaId;
    }

    public void setAlakategoriaId(Integer alakategoriaId) {
        this.alakategoriaId = alakategoriaId;
    }

    public String getAlakategorianimi() {
        return alakategorianimi;
    }

    public void setAlakategorianimi(String alakategorianimi) {
        this.alakategorianimi = alakategorianimi;
    }

    public Valikategoria getValikategoriaId() {
        return valikategoriaId;
    }

    public void setValikategoriaId(Valikategoria valikategoriaId) {
        this.valikategoriaId = valikategoriaId;
    }

    @XmlTransient
    public Collection<Ilmoitus> getIlmoitusCollection() {
        return ilmoitusCollection;
    }

    public void setIlmoitusCollection(Collection<Ilmoitus> ilmoitusCollection) {
        this.ilmoitusCollection = ilmoitusCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alakategoriaId != null ? alakategoriaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alakategoria)) {
            return false;
        }
        Alakategoria other = (Alakategoria) object;
        if ((this.alakategoriaId == null && other.alakategoriaId != null) || (this.alakategoriaId != null && !this.alakategoriaId.equals(other.alakategoriaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kirppis.data.Alakategoria[ alakategoriaId=" + alakategoriaId + " ]";
    }
    
}
