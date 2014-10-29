/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirppis.model;

import com.kirppis.data.Alakategoria;
import com.kirppis.data.Ilmoitus;
import com.kirppis.data.Paakategoria;
import com.kirppis.data.Valikategoria;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ViewScoped;
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
@ViewScoped
public class kirppisService implements Serializable {
    UserTransaction trans;
    EntityManager eManageri;
    
    private Ilmoitus NaytettavaIlmoitus;
    private Ilmoitus UusiIlmoitus = new Ilmoitus();
    
    private List<Ilmoitus>KaikkiIlmoituksetLista;
    private List<Ilmoitus>NaytettavatIlmoituksetLista;
    private List<Ilmoitus>IlmoitusLuonnosLista;
    
    private List<Paakategoria>PaakategoriatLista;
    private List<Valikategoria>ValikategoriatLista;
    private List<Alakategoria>AlakategoriatLista;
    
    private int valittuPaakategoriaID;
    private List<Valikategoria>ValittuValikategoriaLista;
   
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
        
        KaikkiIlmoituksetLista = new ArrayList<>();
        PaakategoriatLista = new ArrayList<>();
        ValikategoriatLista = new ArrayList<>();
        AlakategoriatLista = new ArrayList<>();
        
        try {
            //this.blogikirjoitukset = new ArrayList<>();
            System.out.println("Haetaan ilmoitukset ja kategoriat kannasta!");
            trans.begin();
            //blogService.setBlogikirjoitukset(eManageri.createQuery("Select e from artikkeli e").getResultList());
            KaikkiIlmoituksetLista = eManageri.createQuery("Select e from Ilmoitus e").getResultList();
            PaakategoriatLista = eManageri.createQuery("Select paa from Paakategoria paa").getResultList();
            ValikategoriatLista = eManageri.createQuery("Select vali from Valikategoria vali").getResultList();
            AlakategoriatLista = eManageri.createQuery("Select ala from Alakategoria ala").getResultList();
            trans.commit();
            System.out.println("Ilmoitukset haettu onnistuneesti!");
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }    
    }
    
    public List<Valikategoria> haeValigatekoriat(int paagatekoriaId){
        List<Valikategoria>NaytettavatValikategoriat = new ArrayList<>();
        for(Valikategoria v : ValikategoriatLista){
            if(v.getPaakategoriaId().getPaakategoriaId() == paagatekoriaId){
                NaytettavatValikategoriat.add(v);
            }
        }
        
        return NaytettavatValikategoriat;
    }
    
    public List<Alakategoria> haeAlagatekoriat(int valikategoriaId){
        List<Alakategoria>NaytettavatAlakategoriat = new ArrayList<>();
        for(Alakategoria a : AlakategoriatLista){
            if(a.getValikategoriaId().getValikategoriaId() == valikategoriaId){
                NaytettavatAlakategoriat.add(a);
            }
        }
        
        return NaytettavatAlakategoriat;
    }
    
    /**
     * @return the KaikkiIlmoituksetLista
     */
    public List<Ilmoitus> getKaikkiIlmoituksetLista() {
        return KaikkiIlmoituksetLista;
    }

    /**
     * @param KaikkiIlmoituksetLista the KaikkiIlmoituksetLista to set
     */
    public void setKaikkiIlmoituksetLista(List<Ilmoitus> KaikkiIlmoituksetLista) {
        this.KaikkiIlmoituksetLista = KaikkiIlmoituksetLista;
    }

    /**
     * @return the Paakategoria
     */
    public List<Paakategoria> getPaakategoriat() {
        return PaakategoriatLista;
    }

    /**
     * @param Paakategoriat the Paakategoria to set
     */
    public void setPaakategoriat(List<Paakategoria> Paakategoriat) {
        this.PaakategoriatLista = Paakategoriat;
    }

    public String haeIlmoituslista() {
        return "listasivu";
    }
    
    public String NaytaIlmoitus(int NaytettavanIlmoituksenID){
        for(Ilmoitus item: KaikkiIlmoituksetLista){
            if(item.getIlmoitusId() == NaytettavanIlmoituksenID){
                setNaytettavaIlmoitus(item);
                break;
            }
        }
        System.out.println("Näytetään ilmoitus: " + NaytettavanIlmoituksenID);
        return "ilmoituksenesittely";
    }
    
    public String NaytaIlmoitusListasivu(int AlagategoriaId){
        System.out.println("Näytetetään ilmoitukset kategorialle: " + AlagategoriaId);
      
        NaytettavatIlmoituksetLista = new ArrayList<>();
        
        for(Ilmoitus il: KaikkiIlmoituksetLista){
            if(il.getAlakategoriaId().getAlakategoriaId() == AlagategoriaId)
                NaytettavatIlmoituksetLista.add(il);
        }
        
        if(NaytettavatIlmoituksetLista.isEmpty()){
            System.out.println("Ilmoituksia ei löytynyt!");
            return "ilmoituksiaeiloytynyt";
        }
        
        return "listasivu";
    }

    /**
     * @return the NaytettavaIlmoitus
     */
    public Ilmoitus getNaytettavaIlmoitus() {
        return NaytettavaIlmoitus;
    }

    /**
     * @param NaytettavaIlmoitus the NaytettavaIlmoitus to set
     */
    public void setNaytettavaIlmoitus(Ilmoitus NaytettavaIlmoitus) {
        this.NaytettavaIlmoitus = NaytettavaIlmoitus;
    }

    /**
     * @return the NaytettavatIlmoituksetLista
     */
    public List<Ilmoitus> getNaytettavatIlmoituksetLista() {
        return NaytettavatIlmoituksetLista;
    }

    /**
     * @return the UusiIlmoitus
     */
    public Ilmoitus getUusiIlmoitus() {
        return UusiIlmoitus;
    }

    /**
     * @param UusiIlmoitus the UusiIlmoitus to set
     */
    public void setUusiIlmoitus(Ilmoitus UusiIlmoitus) {
        this.UusiIlmoitus = UusiIlmoitus;
    }

    public void tallennaUusiIlmoitusLuonnos() {
        IlmoitusLuonnosLista.add(UusiIlmoitus);
        // ilmoitusId = null
    }
    
    public List<Valikategoria> HaeValittuValikategoria(int PaakategoriaID){
        ValittuValikategoriaLista = new ArrayList<>();
        for(Valikategoria vali: ValikategoriatLista){
            if(vali.getPaakategoriaId().getPaakategoriaId() == PaakategoriaID){
                ValittuValikategoriaLista.add(vali);
            }
        }
        return ValittuValikategoriaLista;
    }

    /**
     * @return the valittuPaakategoriaID
     */
    public int getValittuPaakategoriaID() {
        return valittuPaakategoriaID;
    }

    /**
     * @param valittuPaakategoriaID the valittuPaakategoriaID to set
     */
    public void setValittuPaakategoriaID(int valittuPaakategoriaID) {
        this.valittuPaakategoriaID = valittuPaakategoriaID;
    }

    /**
     * @return the IlmoitusLuonnosLista
     */
    public List<Ilmoitus> getIlmoitusLuonnosLista() {
        return IlmoitusLuonnosLista;
    }

    /**
     * @param IlmoitusLuonnosLista the IlmoitusLuonnosLista to set
     */
    public void setIlmoitusLuonnosLista(List<Ilmoitus> IlmoitusLuonnosLista) {
        this.IlmoitusLuonnosLista = IlmoitusLuonnosLista;
    }
}
