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
    
    private List<Ilmoitus>KaikkiIlmoituksetLista = new ArrayList<>();
    private List<Ilmoitus>NaytettavatIlmoituksetLista = new ArrayList<>();
    private List<Ilmoitus>IlmoitusLuonnosLista = new ArrayList<>();
    
    private List<Paakategoria>PaakategoriatLista = new ArrayList<>();
    private List<Valikategoria>ValikategoriatLista = new ArrayList<>();
    private List<Alakategoria>AlakategoriatLista = new ArrayList<>();
    
    private int valittuPaakategoriaID;
    private int valittuValigategoriaID;
    private List<Valikategoria>ValittuValikategoriaLista;
    private List<Alakategoria>ValittuAlakategoriaLista;
   
    private List<String> ilmoituksenKuvat = new ArrayList<>();
    
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
        
        try {
            System.out.println("Haetaan ilmoitukset ja kategoriat kannasta!");
            trans.begin();
            KaikkiIlmoituksetLista = eManageri.createQuery("Select e from Ilmoitus e").getResultList();
            PaakategoriatLista = eManageri.createQuery("Select paa from Paakategoria paa").getResultList();
            ValikategoriatLista = eManageri.createQuery("Select vali from Valikategoria vali").getResultList();
            AlakategoriatLista = eManageri.createQuery("Select ala from Alakategoria ala").getResultList();
            trans.commit();
            System.out.println("Ilmoitukset ja kategoriat haettu onnistuneesti!");
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

    /**
     * @return the valittuValigategoriaID
     */
    public int getValittuValigategoriaID() {
        return valittuValigategoriaID;
    }

    /**
     * @param valittuValigategoriaID the valittuValigategoriaID to set
     */
    public void setValittuValigategoriaID(int valittuValigategoriaID) {
        this.valittuValigategoriaID = valittuValigategoriaID;
    }

    /**
     * @return the ValittuValikategoriaLista
     */
    public List<Valikategoria> getValittuValikategoriaLista() {
        return ValittuValikategoriaLista;
    }

    /**
     * @param ValittuValikategoriaLista the ValittuValikategoriaLista to set
     */
    public void setValittuValikategoriaLista(List<Valikategoria> ValittuValikategoriaLista) {
        this.ValittuValikategoriaLista = ValittuValikategoriaLista;
    }

    /**
     * @return the ValittuAlakategoriaLista
     */
    public List<Alakategoria> getValittuAlakategoriaLista() {
        return ValittuAlakategoriaLista;
    }

    /**
     * @param ValittuAlakategoriaLista the ValittuAlakategoriaLista to set
     */
    public void setValittuAlakategoriaLista(List<Alakategoria> ValittuAlakategoriaLista) {
        this.ValittuAlakategoriaLista = ValittuAlakategoriaLista;
    }
    
    // Valikategoriat selectOneMenuun
    public List<Valikategoria> haeValittuValikategoria(){
        valittuValigategoriaID = 0;
        ValittuValikategoriaLista = new ArrayList<>();
        for(Valikategoria vali: ValikategoriatLista){
            if(vali.getPaakategoriaId().getPaakategoriaId() == valittuPaakategoriaID){
                ValittuValikategoriaLista.add(vali);
            }
        }
        return ValittuValikategoriaLista;
    }
    
    //Alakategoriat selectOneMenuun
    public List<Alakategoria> haeValittuAlakategoria(){
        ValittuAlakategoriaLista = new ArrayList<>();
        for(Alakategoria ala: AlakategoriatLista){
            if(ala.getValikategoriaId().getValikategoriaId() == valittuValigategoriaID){
                ValittuAlakategoriaLista.add(ala);
            }
        }
        return ValittuAlakategoriaLista;
    }

    /**
     * @return the ilmoituksenKuvat
     */
    public List<String> getIlmoituksenKuvat() {
        ilmoituksenKuvat.clear();
        String kuvat = NaytettavaIlmoitus.getKuvienpolku();
        String split[]= kuvat.split("\\s+");
        for(int i = 0; i < split.length; i++) {
            ilmoituksenKuvat.add(split[i]);
        }
        return ilmoituksenKuvat;
    }

    /**
     * @param ilmoituksenKuvat the ilmoituksenKuvat to set
     */
    public void setIlmoituksenKuvat(List<String> ilmoituksenKuvat) {
        this.ilmoituksenKuvat = ilmoituksenKuvat;
    }

    public String haeIlmoituksenOletuskuva(int id) {
        for(Ilmoitus i : NaytettavatIlmoituksetLista) {
            if(i.getIlmoitusId().equals(id)) {
                String kuvat = i.getKuvienpolku();
                String oletuskuva[]= kuvat.split("\\s+");
                return oletuskuva[0];
            }
        }
        return "default-picture.jpg";
    }
}
