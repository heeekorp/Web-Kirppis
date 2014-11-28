/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirppis.model;

import com.kirppis.data.Alakategoria;
import com.kirppis.data.Ilmoitus;
import com.kirppis.data.Kayttaja;
import com.kirppis.data.Paakategoria;
import com.kirppis.data.Valikategoria;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    
    private Kayttaja kirjautunutKayttaja;
    private String facebookIdString;
    private boolean onkoKayttajaKirjautunut;
    
    private String paivamaaraTanaan;
    private Ilmoitus naytettavaIlmoitus;
    private Ilmoitus uusiIlmoitus = new Ilmoitus();
    
    private String hakusana;
    private int postinumero;
    private int etaisyys;
    private int pKategoria;
    private int vKategoria;
    private int aKategoria;
    private int ilmoituksetAjalta;
    private int kunto;
    private int myyja;
    
    private List<Ilmoitus>haunTuloksetLista = new ArrayList<>();
    private List<Ilmoitus>kaikkiIlmoituksetLista = new ArrayList<>();
    private List<Ilmoitus>ilmoitusLuonnosLista = new ArrayList<>();
    private List<String> ilmoituksenKuvat = new ArrayList<>();
    
    private List<Paakategoria>paakategoriatLista = new ArrayList<>();
    private List<Valikategoria>valikategoriatLista = new ArrayList<>();
    private List<Alakategoria>alakategoriatLista = new ArrayList<>();    
    private int valittuPaakategoriaID;
    private int valittuValikategoriaID;
    private int valittuAlakategoriaID;
    private List<Valikategoria>valittuValikategoriaLista;
    private List<Alakategoria>valittuAlakategoriaLista;
   
        
    /*************************************************************************
     *  Konstruktorit - alkaa
     *************************************************************************/
    /**
     * 
     * 
     * Creates a new instance of kirppisService
     */
    public kirppisService() {
     
    }

    /**
     * 
     * @param trans
     * @param eManageri 
     */
    public kirppisService(UserTransaction trans, EntityManager eManageri) {
        this.trans = trans;
        this.eManageri = eManageri;
        
        kirjautunutKayttaja = new Kayttaja();
        onkoKayttajaKirjautunut = false;
        facebookIdString = "0";
        
        try {
            System.out.println("Haetaan ilmoitukset ja kategoriat kannasta!");
            trans.begin();
            kaikkiIlmoituksetLista = eManageri.createQuery("Select e from Ilmoitus e").getResultList();
            paakategoriatLista = eManageri.createQuery("Select paa from Paakategoria paa").getResultList();
            valikategoriatLista = eManageri.createQuery("Select vali from Valikategoria vali").getResultList();
            alakategoriatLista = eManageri.createQuery("Select ala from Alakategoria ala").getResultList();
            trans.commit();
            System.out.println("Ilmoitukset ja kategoriat haettu onnistuneesti!");
            
            // Haetaan kuluva päivämäärä
            paivamaaraTanaan = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }    
    }
    
    /*************************************************************************
     *  Konstruktorit - loppuu
     *************************************************************************/
    
    /*************************************************************************
     * Kategorioihin liittyvät funktiot - alkaa
     *************************************************************************/
    
    /**
     * Haetaan välikategoriat parametrina tulevan pääkategorian mukaan
     * @param paagatekoriaId
     * @return 
     */
    public List<Valikategoria> haeValikategoriatPaakategorianMukaan(int paagatekoriaId){
        List<Valikategoria>NaytettavatValikategoriat = new ArrayList<>();
        for(Valikategoria v : valikategoriatLista){
            if(v.getPaakategoriaId().getPaakategoriaId() == paagatekoriaId){
                NaytettavatValikategoriat.add(v);
            }
        }
        return NaytettavatValikategoriat;
    }
    
    /**
     * Haetaan alakategoriat parametrina tulevan välikategorian mukaan
     * @param valikategoriaId
     * @return 
     */
    public List<Alakategoria> haeAlakategoriatValikategorianMukaan(int valikategoriaId){
        List<Alakategoria>NaytettavatAlakategoriat = new ArrayList<>();
        for(Alakategoria a : alakategoriatLista){
            if(a.getValikategoriaId().getValikategoriaId() == valikategoriaId){
                NaytettavatAlakategoriat.add(a);
            }
        }
        return NaytettavatAlakategoriat;
    }
    
    /**
     * Valikategoriat selectOneMenuun
     * @return 
     */
    public List<Valikategoria> haeValittuValikategoria(){
        valittuValikategoriaID = 0;
        valittuValikategoriaLista = new ArrayList<>();
        for(Valikategoria vali: valikategoriatLista){
            if(vali.getPaakategoriaId().getPaakategoriaId() == valittuPaakategoriaID){
                valittuValikategoriaLista.add(vali);
            }
        }
        return valittuValikategoriaLista;
    }
    
    /**
     * Alakategoriat selectOneMenuun
     * @return 
     */
    public List<Alakategoria> haeValittuAlakategoria(){
        valittuAlakategoriaID = 0;
        valittuAlakategoriaLista = new ArrayList<>();
        for(Alakategoria ala: alakategoriatLista){
            if(ala.getValikategoriaId().getValikategoriaId() == valittuValikategoriaID){
                valittuAlakategoriaLista.add(ala);
            }
        }
        return valittuAlakategoriaLista;
    }

    // Kutsutaan omaseututemplatesta
    public String alaKategoriaIdHaku(int AlakategoriaId){
        System.out.println("Näytetetään ilmoitukset kategorialle: " + AlakategoriaId);
      
        //Tyhjennä hauntulokset lista
        haunTuloksetLista.clear();
        
        for(Ilmoitus i: kaikkiIlmoituksetLista){
            if(i.getAlakategoriaId().getAlakategoriaId() == AlakategoriaId)
                if(!haunTuloksetLista.contains(i)) {
                        haunTuloksetLista.add(i);
                    }
        }
        
        if(haunTuloksetLista.isEmpty()){
            System.out.println("Ilmoituksia ei löytynyt!");
            return "ilmoituksiaeiloytynyt";
        }
        return "listasivu";
    }
    
    /*************************************************************************
     * Kategorioihin liittyvät funktiot - loppuu
     *************************************************************************/
    
    /*************************************************************************
     *  Haku osuus - alkaa
     *************************************************************************/

    public String haeHakusanalla() {
        haunTuloksetLista.clear();
        if(!"".equals(hakusana)) {
            //Suorita tietokanta haku ilmoituksista annetulla hakusanalla
            try {
                System.out.println("Haetaan ilmoitukset kannasta, kyselyllä: ");
                System.out.println("SELECT i FROM Ilmoitus i WHERE i.otsikko LIKE '%" + hakusana + "%' OR i.kuvaus LIKE '%" + hakusana + "%'");
                trans.begin();
                haunTuloksetLista = eManageri.createQuery("SELECT i FROM Ilmoitus i WHERE i.otsikko LIKE '%" + hakusana + "%' OR i.kuvaus LIKE '%" + hakusana + "%'").getResultList();
                trans.commit();
                System.out.println("Ilmoituksia haettu onnistuneesti " + haunTuloksetLista.size() + "kpl!");
            }
            catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
        hakusana = "";
        // Lopullinen sivun palautus
        if(haunTuloksetLista.isEmpty()) {
            return "ilmoituksiaeiloytynyt";
        }
        else {
            return "listasivu";
        }
    }
    
    public String toteutaHaku() {
        haunTuloksetLista.clear();
        String kysely = "SELECT i FROM Ilmoitus i";
        String taulunlinkki = "", hakuosat = "";
        
        // Jos kaikki hakukentät ovat tyhjiä tulosta kaikki ilmoitukset
        if("".equals(hakusana) &&
           postinumero == 0 &&
           etaisyys == 0 &&
           pKategoria == 0 &&
           vKategoria == 0 &&
           aKategoria == 0 &&
           ilmoituksetAjalta == 0 &&
           kunto == 0) {
            System.out.println("Kaikki kentät tyhjiä!");
        }
           
        // Hakukenttiin on lisätty jotain hakukriteerejä ja muodostetaan niistä SQL-kysely lähetettäväksi kantaan
        else {  
            if(!"".equals(hakusana)) {
                hakuosat += " i.otsikko LIKE '%" + hakusana + "%' OR i.kuvaus LIKE '%" + hakusana + "%'";
            }
            if(postinumero != 0) {
                taulunlinkki += " LEFT OUTER JOIN i.myyjanId k";
                
                if(hakuosat.equals("")) {
                    hakuosat += " k.postinumero = " + postinumero;
                }
                else {
                    hakuosat += " AND k.postinumero = " + postinumero;
                }
            }
            if(etaisyys != 0) {
                //Etäisyys käyttäjän omasta postinumerosta myyjän postinumeroon
            }
            if(pKategoria != 0) {
                taulunlinkki += " LEFT OUTER JOIN i.alakategoriaId a LEFT OUTER JOIN a.valikategoriaId v LEFT OUTER JOIN v.paakategoriaId p";
                
                if(vKategoria != 0) {
                    if(aKategoria != 0) {
                        if(hakuosat.equals("")) {
                            hakuosat += " i.alakategoriaId = " + aKategoria;
                        }
                        else {
                            hakuosat += " AND i.alakategoriaId = " + aKategoria;
                        }
                    }
                    if(hakuosat.equals("")) {
                        hakuosat += " v.valikategoriaId = " + vKategoria;
                    }
                    else {
                        hakuosat += " AND v.valikategoriaId = " + vKategoria;
                    }
                }
                if(hakuosat.equals("")) {
                    hakuosat += " p.paakategoriaId = " + pKategoria;
                }
                else {
                    hakuosat += " AND p.paakategoriaId = " + pKategoria;
                }
            }
            if(ilmoituksetAjalta != 0) {
                // Määritetään miltä ajalta ilmoitukset haetaan
            }
            if(kunto != 0) {
                if(hakuosat.equals("")) {
                    hakuosat += " i.tuotteenkunto = " + kunto;
                }
                else {
                    hakuosat += " AND i.tuotteenkunto = " + kunto;
                }      
            }
        } 
        
        //Jos hakuosat ei ole tyhjä
        if(!"".equals(hakuosat)) {
            kysely = "SELECT i FROM Ilmoitus i" + taulunlinkki + " WHERE" + hakuosat;
        }
                
        //Suorita tietokanta haku annetuilla hakukriteereillä ilmoituksista
        try {
            System.out.println("Haetaan ilmoitukset kannasta, kyselyllä: ");
            System.out.println(kysely);
            trans.begin();
            haunTuloksetLista = eManageri.createQuery(kysely).getResultList();
            trans.commit();
            System.out.println("Ilmoituksia haettu onnistuneesti " + haunTuloksetLista.size() + "kpl!");
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        
        // Lopullinen sivun palautus
        if(haunTuloksetLista.isEmpty()) {
            return "ilmoituksiaeiloytynyt";
        }
        else {
            return "listasivu";
        }
    }

    /*************************************************************************
     *  Haku osuus - loppuu
     *************************************************************************/
    
    /*************************************************************************
     *  Ilmoituksen näyttämiseen liittyvät funktiot - alkaa
     *************************************************************************/
    
    /**
     * Ilmoituksen näyttämiseen liittyvät funktiot - alkaa
     * @return
     */
    public String haeIlmoituslista() {
        return "listasivu";
    }
    
    public String NaytaIlmoitus(int NaytettavanIlmoituksenID){
        for(Ilmoitus item: kaikkiIlmoituksetLista){
            if(item.getIlmoitusId() == NaytettavanIlmoituksenID){
                setNaytettavaIlmoitus(item);
                break;
            }
        }
        System.out.println("Näytetään ilmoitus: " + NaytettavanIlmoituksenID);
        return "ilmoituksenesittely";
    }
    
    public String haeIlmoituksenOletuskuva(int id) {
        for(Ilmoitus i : haunTuloksetLista) {
            if(i.getIlmoitusId().equals(id)) {
                String kuvat = i.getKuvienpolku();
                String oletuskuva[]= kuvat.split("\\s+");
                return oletuskuva[0];
            }
        }
        return "default-picture.jpg";
    }
    
    /*************************************************************************
     *  Ilmoituksen näyttämiseen liittyvät funktiot - loppuu
     *************************************************************************/
    
    /*************************************************************************
     *  Ilmoituksen lisäämiseen liittyvät funktiot - alkaa
     *************************************************************************/
    
    public void tallennaUusiIlmoitusLuonnos() {
        ilmoitusLuonnosLista.add(uusiIlmoitus);
        // ilmoitusId = null
    }
    
    /*************************************************************************
     *  Ilmoituksen lisäämiseen liittyvät funktiot - loppuu
     *************************************************************************/
    
    
    /*************************************************************************
    *   Kirjautumiseen ja ulos kirjautumiseen liittyvät funktiot - alkaa
    **************************************************************************/
    
        public void kayttajaKirjaudu(){
        
        if(this.onkoKayttajaKirjautunut == true){
            return;
        }
        if(kirjautunutKayttaja.getFacebookid() == 0)
            return;
        
        System.out.println("Kirjaudutaan - Nimi: " + kirjautunutKayttaja.getFacebooknimi());
        System.out.println("Kirjaudutaan - Face ID: " + kirjautunutKayttaja.getFacebookid());
        List<Kayttaja>kayttajat = new ArrayList<>();
        
         try {
            System.out.println("Haetaan facebookid:tä vastaava käyttäjä tietokannasta!");
            trans.begin();
            kayttajat = eManageri.createQuery("Select user from Kayttaja user WHERE USER.facebookid = " + kirjautunutKayttaja.getFacebookid()).getResultList();
            trans.commit();
            System.out.println("Haku onnistui!!!!");
            if(kayttajat.isEmpty()){
                System.out.println("FacebookId:tä vastaavaa käyttäjää ei löytynyt!");
                lisaaKayttaja();
            }
            else{
                System.out.println("FacebookId:tä vastaavaa käyttäjä löytyi tietokannasta!");
                this.onkoKayttajaKirjautunut = true;
                kirjautunutKayttaja = kayttajat.get(0);
            }
            
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }   
    }
    public void lisaaKayttaja(){
       
            try {
            System.out.println("Lisätään uusi käyttäjä kantaan!");
            trans.begin();
            eManageri.persist(kirjautunutKayttaja);
            trans.commit();
            System.out.println("Lisäys onnistui!");
            this.onkoKayttajaKirjautunut = true;
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
        System.out.println(e.getMessage());
        }
    }
    public void kirjauduUlos(){
        System.out.println("Kirjaudutaan ulos!");
        kirjautunutKayttaja = new Kayttaja();
        this.onkoKayttajaKirjautunut = false;
    }
    
    /*************************************************************************
    *   Kirjautumiseen ja ulos kirjautumiseen liittyvät funktiot - loppuu
    **************************************************************************/
    
    
    
    /*************************************************************************
     *  Getterit ja setterit - alkaa
     *************************************************************************/
    /**
     * @return the kaikkiIlmoituksetLista
     */
    public List<Ilmoitus> getkaikkiIlmoituksetLista() {
        return kaikkiIlmoituksetLista;
    }

    /**
     * @param kaikkiIlmoituksetLista the kaikkiIlmoituksetLista to set
     */
    public void setkaikkiIlmoituksetLista(List<Ilmoitus> kaikkiIlmoituksetLista) {
        this.kaikkiIlmoituksetLista = kaikkiIlmoituksetLista;
    }

    /**
     * @return the Paakategoria
     */
    public List<Paakategoria> getPaakategoriat() {
        return paakategoriatLista;
    }

    /**
     * @param Paakategoriat the Paakategoria to set
     */
    public void setPaakategoriat(List<Paakategoria> Paakategoriat) {
        this.paakategoriatLista = Paakategoriat;
    }

    /**
     * @return the naytettavaIlmoitus
     */
    public Ilmoitus getNaytettavaIlmoitus() {
        return naytettavaIlmoitus;
    }

    /**
     * @param NaytettavaIlmoitus the naytettavaIlmoitus to set
     */
    public void setNaytettavaIlmoitus(Ilmoitus NaytettavaIlmoitus) {
        this.naytettavaIlmoitus = NaytettavaIlmoitus;
    }

    /**
     * @return the uusiIlmoitus
     */
    public Ilmoitus getUusiIlmoitus() {
        return uusiIlmoitus;
    }

    /**
     * @param UusiIlmoitus the uusiIlmoitus to set
     */
    public void setUusiIlmoitus(Ilmoitus UusiIlmoitus) {
        this.uusiIlmoitus = UusiIlmoitus;
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
        this.pKategoria = valittuPaakategoriaID;
    }

    /**
     * @return the ilmoitusLuonnosLista
     */
    public List<Ilmoitus> getIlmoitusLuonnosLista() {
        return ilmoitusLuonnosLista;
    }

    /**
     * @param IlmoitusLuonnosLista the ilmoitusLuonnosLista to set
     */
    public void setIlmoitusLuonnosLista(List<Ilmoitus> IlmoitusLuonnosLista) {
        this.ilmoitusLuonnosLista = IlmoitusLuonnosLista;
    }

    /**
     * @return the valittuValikategoriaID
     */
    public int getValittuValikategoriaID() {
        return valittuValikategoriaID;
    }

    /**
     * @param valittuValikategoriaID the valittuValikategoriaID to set
     */
    public void setValittuValikategoriaID(int valittuValikategoriaID) {
        this.valittuValikategoriaID = valittuValikategoriaID;
        this.vKategoria = valittuValikategoriaID;
    }

    /**
     * @return the valittuValikategoriaLista
     */
    public List<Valikategoria> getValittuValikategoriaLista() {
        return valittuValikategoriaLista;
    }

    /**
     * @param ValittuValikategoriaLista the valittuValikategoriaLista to set
     */
    public void setValittuValikategoriaLista(List<Valikategoria> ValittuValikategoriaLista) {
        this.valittuValikategoriaLista = ValittuValikategoriaLista;
    }

    /**
     * @return the valittuAlakategoriaLista
     */
    public List<Alakategoria> getValittuAlakategoriaLista() {
        return valittuAlakategoriaLista;
    }

    /**
     * @param ValittuAlakategoriaLista the valittuAlakategoriaLista to set
     */
    public void setValittuAlakategoriaLista(List<Alakategoria> ValittuAlakategoriaLista) {
        this.valittuAlakategoriaLista = ValittuAlakategoriaLista;
    }
    
    /**
     * @return the ilmoituksenKuvat
     */
    public List<String> getIlmoituksenKuvat() {
        ilmoituksenKuvat.clear();
        String kuvat = naytettavaIlmoitus.getKuvienpolku();
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

    /**
     * @return the valittuAlakategoriaID
     */
    public int getValittuAlakategoriaID() {
        return valittuAlakategoriaID;
    }

    /**
     * @param valittuAlakategoriaID the valittuAlakategoriaID to set
     */
    public void setValittuAlakategoriaID(int valittuAlakategoriaID) {
        this.valittuAlakategoriaID = valittuAlakategoriaID;
        this.aKategoria = valittuAlakategoriaID;
    }

    /**
     * @return the paivamaaraTanaan
     */
    public String getPaivamaaraTanaan() {
        return paivamaaraTanaan;
    }

    /**
     * @param paivamaaraTanaan the paivamaaraTanaan to set
     */
    public void setPaivamaaraTanaan(String paivamaaraTanaan) {
        this.paivamaaraTanaan = paivamaaraTanaan;
    }

    /**
     * @return the hakusana
     */
    public String getHakusana() {
        return hakusana;
    }

    /**
     * @param hakusana the hakusana to set
     */
    public void setHakusana(String hakusana) {
        this.hakusana = hakusana;
    }

    /**
     * @return the postinumero
     */
    public int getPostinumero() {
        return postinumero;
    }

    /**
     * @param postinumero the postinumero to set
     */
    public void setPostinumero(int postinumero) {
        this.postinumero = postinumero;
    }

    /**
     * @return the etaisyys
     */
    public int getEtaisyys() {
        return etaisyys;
    }

    /**
     * @param etaisyys the etaisyys to set
     */
    public void setEtaisyys(int etaisyys) {
        this.etaisyys = etaisyys;
    }

    /**
     * @return the pKategoria
     */
    public int getpKategoria() {
        return pKategoria;
    }

    /**
     * @param pKategoria the pKategoria to set
     */
    public void setpKategoria(int pKategoria) {
        this.pKategoria = pKategoria;
    }

    /**
     * @return the vKategoria
     */
    public int getvKategoria() {
        return vKategoria;
    }

    /**
     * @param vKategoria the vKategoria to set
     */
    public void setvKategoria(int vKategoria) {
        this.vKategoria = vKategoria;
    }

    /**
     * @return the aKategoria
     */
    public int getaKategoria() {
        return aKategoria;
    }

    /**
     * @param aKategoria the aKategoria to set
     */
    public void setaKategoria(int aKategoria) {
        this.aKategoria = aKategoria;
    }

    /**
     * @return the ilmoituksetAjalta
     */
    public int getIlmoituksetAjalta() {
        return ilmoituksetAjalta;
    }

    /**
     * @param ilmoituksetAjalta the ilmoituksetAjalta to set
     */
    public void setIlmoituksetAjalta(int ilmoituksetAjalta) {
        this.ilmoituksetAjalta = ilmoituksetAjalta;
    }

    /**
     * @return the kunto
     */
    public int getKunto() {
        return kunto;
    }

    /**
     * @param kunto the kunto to set
     */
    public void setKunto(int kunto) {
        this.kunto = kunto;
    }

    /**
     * @return the myyja
     */
    public int getMyyja() {
        return myyja;
    }

    /**
     * @param myyja the myyja to set
     */
    public void setMyyja(int myyja) {
        this.myyja = myyja;
    }

    /**
     * @return the haunTuloksetLista
     */
    public List<Ilmoitus> getHaunTuloksetLista() {
        return haunTuloksetLista;
    }

    /**
     * @param haunTuloksetLista the haunTuloksetLista to set
     */
    public void setHaunTuloksetLista(List<Ilmoitus> haunTuloksetLista) {
        this.haunTuloksetLista = haunTuloksetLista;
    }
    
    /**
     * @return the kirjautunutKayttaja
     */
    public Kayttaja getKirjautunutKayttaja() {
        return kirjautunutKayttaja;
    }

    /**
     * @param kirjautunutKayttaja the kirjautunutKayttaja to set
     */
    public void setKirjautunutKayttaja(Kayttaja kirjautunutKayttaja) {
        this.kirjautunutKayttaja = kirjautunutKayttaja;
        
    }

    /**
     * @return the facebookIdString
     */
    public String getFacebookIdString() {
        return facebookIdString;
    }

    /**
     * @param facebookIdString the facebookIdString to set
     */
    public void setFacebookIdString(String facebookIdString) {
        this.facebookIdString = facebookIdString;
        kirjautunutKayttaja.setFacebookid(Long.parseLong(facebookIdString));
    }

    /**
     * @return the onkoKayttajaKirjautunut
     */
    public boolean isOnkoKayttajaKirjautunut() {
        return onkoKayttajaKirjautunut;
    }

    /**
     * @param onkoKayttajaKirjautunut the onkoKayttajaKirjautunut to set
     */
    public void setOnkoKayttajaKirjautunut(boolean onkoKayttajaKirjautunut) {
        this.onkoKayttajaKirjautunut = onkoKayttajaKirjautunut;
    }

}
    /*************************************************************************
     *  Getterit ja setterit - loppuu
     *************************************************************************/
    

