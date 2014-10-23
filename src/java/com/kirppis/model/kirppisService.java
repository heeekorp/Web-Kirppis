/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirppis.model;

import com.kirppis.data.Alakategoriat;
import com.kirppis.data.Ilmoitukset;
import com.kirppis.data.Paakategoriat;
import com.kirppis.data.Valikategoriat;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Opiframe
 */
@Named(value = "kirppisService")
@SessionScoped
public class kirppisService implements Serializable {
    UserTransaction trans;
    EntityManager eManageri;
    
    private List<Ilmoitukset>NaytettavatIlmoitukset;
    
    private List<Paakategoriat>PaakategoriatLista;
    private List<Valikategoriat>ValikategoriatLista;
    private List<Alakategoriat>AlakategoriatLista;
   
    /**
     * 
     * 
     * Creates a new instance of kirppisService
     */
    public kirppisService() {
     
    }

    public kirppisService(UserTransaction trans, EntityManager eManageri) {
        this.trans = trans;
        this.eManageri = eManageri;
        
        NaytettavatIlmoitukset = new ArrayList<>();
        PaakategoriatLista = new ArrayList<>();
        ValikategoriatLista = new ArrayList<>();
        AlakategoriatLista = new ArrayList<>();
        
        try {
            //this.blogikirjoitukset = new ArrayList<>();
            System.out.println("Haetaan ilmoitukset ja kategoriat kannasta!");
            trans.begin();
            //blogService.setBlogikirjoitukset(eManageri.createQuery("Select e from artikkeli e").getResultList());
            NaytettavatIlmoitukset = eManageri.createQuery("Select e from Ilmoitukset e").getResultList();
            PaakategoriatLista = eManageri.createQuery("Select paa from Paakategoriat paa").getResultList();
            ValikategoriatLista = eManageri.createQuery("Select vali from Valikategoriat vali").getResultList();
            AlakategoriatLista = eManageri.createQuery("Select ala from Alakategoriat ala").getResultList();
            trans.commit();
            System.out.println("Ilmoitukset haettu onnistuneesti!");
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }    
    }
    
    public List<Valikategoriat> haeValigatekoriat(int paagatekoriaId){
        List<Valikategoriat>NaytettavatValikategoriat = new ArrayList<>();
        for(Valikategoriat v : ValikategoriatLista){
            if(v.getPaakategoriaId().getPaakategoriaId() == paagatekoriaId){
                NaytettavatValikategoriat.add(v);
            }
        }
        
        return NaytettavatValikategoriat;
    }
    
    public List<Alakategoriat> haeAlagatekoriat(int valikategoriaId){
        List<Alakategoriat>NaytettavatAlakategoriat = new ArrayList<>();
        for(Alakategoriat a : AlakategoriatLista){
            if(a.getValikategoriaId().getValikategoriaId() == valikategoriaId){
                NaytettavatAlakategoriat.add(a);
            }
        }
        
        return NaytettavatAlakategoriat;
    }
    
    /**
     * @return the NaytettavatIlmoitukset
     */
    public List<Ilmoitukset> getNaytettavatIlmoitukset() {
        return NaytettavatIlmoitukset;
    }

    /**
     * @param NaytettavatIlmoitukset the NaytettavatIlmoitukset to set
     */
    public void setNaytettavatIlmoitukset(List<Ilmoitukset> NaytettavatIlmoitukset) {
        this.NaytettavatIlmoitukset = NaytettavatIlmoitukset;
    }

    /**
     * @return the Paakategoriat
     */
    public List<Paakategoriat> getPaakategoriat() {
        return PaakategoriatLista;
    }

    /**
     * @param Paakategoriat the Paakategoriat to set
     */
    public void setPaakategoriat(List<Paakategoriat> Paakategoriat) {
        this.PaakategoriatLista = Paakategoriat;
    }

    public String haeIlmoituslista() {
        
        return "listasivu";
    }

}
