/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirppis.model;

import com.kirppis.data.Alakategoria;
import com.kirppis.data.Ilmoitus;
import com.kirppis.data.Kayttaja;
import com.kirppis.data.Muistilista;
import com.kirppis.data.Paakategoria;
import com.kirppis.data.Valikategoria;
import com.kirppis.data.Viesti;
import com.kirppis.data.hintaComparator;
import com.kirppis.data.pvmComparator;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
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
    
    private Kayttaja kirjautunutKayttaja = new Kayttaja();
    private String facebookIdString = "0";
    private boolean onkoKayttajaKirjautunut = false;
    private boolean rekisterointiSivuNaytetaan = false;
    private boolean naytaSahkoposti = false;
    private boolean naytaPuhelin = false;
    
    private List<Ilmoitus> muistilistanIlmoitukset = new ArrayList<>();
    
    private Ilmoitus naytettavaIlmoitus;
    private Ilmoitus uusiIlmoitus = new Ilmoitus();
    
    private Viesti uusiViesti = new Viesti();
    private String vastausViestinRunko;
    private List<Viesti> yksityisetViestit = new ArrayList<>();
    private int yksityisenViestinVastaanottajaId;
    private List<Kayttaja> yksityistenViestienVastaanottajat = new ArrayList<>();
    private long kayttajanUusienViestienMaara;
    private List<Viesti> kayttajanSaapuneetViestit = new ArrayList<>();
      
    private String hakusana;
    private int postinumero;
    private int etaisyys;
    private int ilmoituksetAjalta;
    private int kunto;
    private int myyja;
    
    private List<Ilmoitus>haunTuloksetLista = new ArrayList<>();
    private int ilmoitusListanJarjestys = 1;
    private List<String> ilmoituksenKuvat = new ArrayList<>();
    private List<Viesti> ilmoituksenViestitLista = new ArrayList();
    
    private List<Paakategoria>paakategoriatLista = new ArrayList<>();
    private List<Valikategoria>valikategoriatLista = new ArrayList<>();
    private List<Alakategoria>alakategoriatLista = new ArrayList<>();    
    private int valittuPaakategoriaID;
    private int valittuValikategoriaID;
    private int valittuAlakategoriaID;
    private List<Valikategoria>valittuValikategoriaLista;
    private List<Alakategoria>valittuAlakategoriaLista;
    
    private boolean poista;
    private List<Integer>poistolista = new ArrayList<>();
    
    private Part kuvaTiedosto;
    private List<String> lisattyjenKuvatiedostojenNimet = new ArrayList<>();
    private String kuvienPolkuServerilla;
    private final static int kuvanLeveys = 1000; // Ilmoitukseen lisättävien kuvien max leveys pikseleinä.
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
        
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        kuvienPolkuServerilla = ctx.getRealPath("/") + "resources\\images\\";
        
        System.out.println("polku on --> " + kuvienPolkuServerilla);
        try {
            System.out.println("Haetaan kategoriat kannasta!");
            trans.begin();
            paakategoriatLista = eManageri.createQuery("Select paa from Paakategoria paa").getResultList();
            valikategoriatLista = eManageri.createQuery("Select vali from Valikategoria vali").getResultList();
            alakategoriatLista = eManageri.createQuery("Select ala from Alakategoria ala").getResultList();
            trans.commit();
            System.out.println("Kategoriat haettu onnistuneesti!");
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
     * Haetaan välikategoriat panelMenuun, parametrina tulevan pääkategorian mukaan
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
     * Haetaan alakategoriat panelMenuun, parametrina tulevan välikategorian mukaan
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
        valittuAlakategoriaLista = new ArrayList<>();
        for(Alakategoria ala: alakategoriatLista){
            if(ala.getValikategoriaId().getValikategoriaId() == valittuValikategoriaID){
                valittuAlakategoriaLista.add(ala);
            }
        }
        return valittuAlakategoriaLista;
    }

    // Haetaan ilmoitukset  alakategorian mukaan. Kutsutaan templaten panelMenusta.
    public void alaKategoriaIdHaku(int AlakategoriaId) throws IOException{
        // Tyhjennetään haun tulokset lista!
        haunTuloksetLista.clear();

        try {
            System.out.println("Haetaan ilmoituksen tiedot tietokannasta!");
            trans.begin();  
            haunTuloksetLista = eManageri.createQuery("Select i from Ilmoitus i WHERE i.alakategoriaId.alakategoriaId = " + AlakategoriaId).getResultList();
            trans.commit();
            System.out.println("Tiedot haettu onnistuneesti!");
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        
        if(haunTuloksetLista.isEmpty()){
            System.out.println("Ilmoituksia ei löytynyt!");
            FacesContext.getCurrentInstance().getExternalContext().redirect("ilmoituksiaeiloytynyt.xhtml");
            return;
        }
        jarjestaHauntulokseLista();
        FacesContext.getCurrentInstance().getExternalContext().redirect("listasivu.xhtml");
    }
    
    /*************************************************************************
     * Kategorioihin liittyvät funktiot - loppuu
     *************************************************************************/
   
     /**
     Hakee käyttäjän ilmoitukset. Funktiota kutsutaaan omasivu.xhtml:stä
     * @return
     */
    public String kayttajanIlmoituksetHaku(){
        System.out.println("Näytetetään ilmoitukset käyttäjälle: " + kirjautunutKayttaja.getKayttajaId());
        // Tyhjennetään haun tulokset lista!
        haunTuloksetLista.clear();
        
        try {
            System.out.println("Haetaan ilmoitukset tietokannasta!");
            trans.begin();  
            haunTuloksetLista = eManageri.createQuery("Select i from Ilmoitus i WHERE i.myyjanId.kayttajaId = " + this.kirjautunutKayttaja.getKayttajaId() + " ORDER BY i.ilmoitusjatettyPvm").getResultList();
            jarjestaHauntulokseLista();
            trans.commit();
            System.out.println("Tiedot haettu onnistuneesti!");
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        } 
        
        if(haunTuloksetLista.isEmpty()){
            System.out.println("Ilmoituksia ei löytynyt!");
            return "Sinulla ei ole omia ilmoituksia";
        }
        return null;
    }
        
    public void jarjestaHauntulokseLista(){
        if(ilmoitusListanJarjestys == 1){// uusin eka
            Collections.sort(haunTuloksetLista, new pvmComparator());
        }
        if(ilmoitusListanJarjestys == 2){ // edullisin eka
            Collections.sort(haunTuloksetLista, new hintaComparator());
        }
        if(ilmoitusListanJarjestys == 3){ // kallein eka
            Collections.sort(haunTuloksetLista, new hintaComparator());
            Collections.reverse(haunTuloksetLista);
        }
    }
      
    /*************************************************************************
     *  Ilmoitusten hakemiseen liittyvät funktiot alkaa!
     *************************************************************************/          
    /**
     * Ilmoitusten hakemiseen liittyvät funktiot alkaa!
     * @throws java.io.IOException
     */
    public void toteutaHaku() throws IOException {
        haunTuloksetLista.clear();
        String kysely = "SELECT i FROM Ilmoitus i";
        String taulunlinkki = "", hakuosat = "";
        
        // Jos kaikki hakukentät ovat tyhjiä tulosta kaikki ilmoitukset
        if("".equals(hakusana) &&
           postinumero == 0 &&
           etaisyys == 0 &&
           valittuAlakategoriaID == 0 &&
           valittuValikategoriaID == 0 &&
           valittuPaakategoriaID == 0 &&
           ilmoituksetAjalta == 0 &&
           kunto == 0) {
            System.out.println("Kaikki kentät tyhjiä!");
        }
           
        // Hakukenttiin on lisätty jotain hakukriteerejä ja muodostetaan niistä SQL-kysely lähetettäväksi kantaan
        else {  
            if(!"".equals(hakusana)) {
                hakuosat += " (i.otsikko LIKE '%" + hakusana + "%' OR i.kuvaus LIKE '%" + hakusana + "%')";
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
            if(valittuPaakategoriaID != 0) {
                taulunlinkki += " LEFT OUTER JOIN i.alakategoriaId a LEFT OUTER JOIN a.valikategoriaId v LEFT OUTER JOIN v.paakategoriaId p";
                
                if(valittuValikategoriaID != 0) {
                    if(valittuAlakategoriaID != 0) {
                        if(hakuosat.equals("")) {
                            hakuosat += " a.alakategoriaId = " + valittuAlakategoriaID;
                        }
                        else {
                            hakuosat += " AND a.alakategoriaId = " + valittuAlakategoriaID;
                        }
                    }
                    if(hakuosat.equals("")) {
                        hakuosat += " v.valikategoriaId = " + valittuValikategoriaID;
                    }
                    else {
                        hakuosat += " AND v.valikategoriaId = " + valittuValikategoriaID;
                    }
                }
                if(hakuosat.equals("")) {
                    hakuosat += " p.paakategoriaId = " + valittuPaakategoriaID;
                }
                else {
                    hakuosat += " AND p.paakategoriaId = " + valittuPaakategoriaID;
                }
            }
            if(ilmoituksetAjalta != 0) {
                // Määritetään miltä ajalta ilmoitukset haetaan
                // Nykyinen päiväys
                Date pvmNyt = new Date( );
                // Haun alkupäivämäärä.
                int dayInMs = 1000 * 60 * 60 * 24 * ilmoituksetAjalta;
                Date alkuPvm = new Date(pvmNyt.getTime() - dayInMs);
                // Päivämäärä formaatti
                SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
                
                if(!hakuosat.equals(""))
                    hakuosat += " AND";
                
                hakuosat += " i.ilmoitusjatettyPvm BETWEEN '" + ft.format(alkuPvm) + "' AND '" + ft.format(pvmNyt) + "'";
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
               
        //Suorittaa tietokantahaun annetuilla hakukriteereillä ilmoituksista
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
            FacesContext.getCurrentInstance().getExternalContext().redirect("ilmoituksiaeiloytynyt.xhtml");
        }
        else {
            jarjestaHauntulokseLista();
            FacesContext.getCurrentInstance().getExternalContext().redirect("listasivu.xhtml");
        }
    }
    
    // Hauetaan pelkällä hakusanalla.
    public void haeHakusanalla() throws IOException{
        this.postinumero=0;
        this.etaisyys=0;
        this.valittuPaakategoriaID=0;
        this.valittuAlakategoriaID=0;
        this.valittuValikategoriaID=0;
        this.ilmoituksetAjalta=0;
        this.kunto=0;
        toteutaHaku();
    }
    
    // Tyhjennä hakusivu
    public void tyhjennaHaku() throws IOException {
        this.hakusana="";
        this.postinumero=0;
        this.etaisyys=0;
        this.valittuPaakategoriaID=0;
        this.valittuAlakategoriaID=0;
        this.valittuValikategoriaID=0;
        this.ilmoituksetAjalta=0;
        this.kunto=0;
        FacesContext.getCurrentInstance().getExternalContext().redirect("haku.xhtml");
    }
    /*************************************************************************
     *  Haku osuus - loppuu
     *************************************************************************/
    
    /*************************************************************************
     *  Ilmoituksen näyttämiseen liittyvät funktiot - alkaa
     *************************************************************************/
    
    /**
     * Näyttää ilmoitutuksen, ja siihen liittyvät viestit, ilmoitusId:n mukaan. Ohjaa ilmoituksenesittely.xhtml -sivulle.
     * @param NaytettavanIlmoituksenID
     * @throws java.io.IOException
     */
    public void NaytaIlmoitus(int NaytettavanIlmoituksenID) throws IOException{
        try {
            System.out.println("Haetaan näytttävä ilmoitus/viestit tietokannasta!");
            eManageri.getEntityManagerFactory().getCache().evictAll();
            trans.begin();
            // Viestin tiedot!
            setNaytettavaIlmoitus((Ilmoitus)eManageri.createQuery("Select i from Ilmoitus i WHERE i.ilmoitusId = " + NaytettavanIlmoituksenID).getSingleResult());
            // Viestiin liittyvät julkiset viestit.
            ilmoituksenViestitLista = eManageri.createQuery("Select v from Viesti v WHERE v.julkinenviesti=1 AND v.ilmoitusId.ilmoitusId = " + naytettavaIlmoitus.getIlmoitusId() + " ORDER BY v.lahetysaika").getResultList();
            // Viestiin liittyvät yksityisviestit.
            yksityisetViestit = eManageri.createQuery("Select v from Viesti v WHERE v.julkinenviesti=0 AND v.ilmoitusId.ilmoitusId = " + naytettavaIlmoitus.getIlmoitusId() + " ORDER BY v.lahetysaika").getResultList();
            
            trans.commit();
            System.out.println("Tiedot haettu onnistuneesti!");
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
       
        // Asetataan julkiset viestit luetuksi.
        for(Viesti v : ilmoituksenViestitLista){
            // Mikäli viestiä ei ole asetettu luetuksi ja kirjautunut käyttäjä on sama kuin myyjä.
            if(v.getViestiluettu() == 0 && kirjautunutKayttaja.equals(naytettavaIlmoitus.getMyyjanId()))
                asetaViestiLuetuksi(v);
        }
        
        // Asetetaan yksitysviestit luetuksi ja päivitetään yksityistenViestienVastaanottajat -lista.
        yksityistenViestienVastaanottajat.clear();
        for(Viesti v : yksityisetViestit){
            // Mikäli viestiä ei ole asetettu luetuksi ja kirjautunut käyttäjä on viestin vastaanottaja.
            if(v.getViestiluettu() == 0 && kirjautunutKayttaja.equals(v.getVastaanottajaId()))
                asetaViestiLuetuksi(v);
            // Mikäli kirjautunut käyttäjä on sama kuin ilmoittaja.
            if(kirjautunutKayttaja.equals(naytettavaIlmoitus.getMyyjanId()) && !kirjautunutKayttaja.equals(v.getLahettajaId())){
                // Lisätään yksityisen viestin lähettäjä yv.vastaanottajat listaan, mikäli se ei siellä jo ole.
                if(!yksityistenViestienVastaanottajat.contains(v.getLahettajaId()))
                    yksityistenViestienVastaanottajat.add(v.getLahettajaId());
            }
        }
        // Mikäli kirjautunut käyttäjä eri kuin myyjä, lisätään myyjä yv:n vastaanottajiin.
        if(!kirjautunutKayttaja.equals(naytettavaIlmoitus.getMyyjanId()))
            yksityistenViestienVastaanottajat.add(naytettavaIlmoitus.getMyyjanId());
            
        FacesContext.getCurrentInstance().getExternalContext().redirect("ilmoituksenesittely.xhtml");              
    }
    
    // Funktio hakee ilmoituksen oletuskuvan listasivulle. 
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
    // Funktio hakee ilmoituksen oletuskuvan muistilistaan. 
    public String haeIlmoituksenOletuskuvaMuistilistaan(int id) {
        for(Ilmoitus i : muistilistanIlmoitukset) {
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
     * @param viesti
     * @return 
     *************************************************************************/
    
    //************************************************************************
    //*  Viestin lisäämiseen/näyttämiseen liittyvät funktiot - alkaa 
    //************************************************************************
	 
    // Funktio tarkistaa näytetäänkö viesti. Kutsutaan ilmoituksenesittely.xhtml:stä	 
    public boolean naytetaankoViesti(Viesti viesti){      
        //  Jos kirjautunutkäyttäjä on sama kuin viestin lähettäjä -> viesti näytetään.
        if(this.kirjautunutKayttaja.equals(viesti.getLahettajaId()))
            return true;
        // Jos kirjautunutkäyttäjä on sama kuin viestin vastaanottaja -> viesti näytetään.
        if(this.kirjautunutKayttaja.equals(viesti.getVastaanottajaId()))
            return true;
        // Julkinen viesti näytetään kaikille.
        if(viesti.getJulkinenviesti() == 1)
            return true;
        // Jos kirjautunutkäyttäjä on myyjä -> viesti näytetään.
        if(this.kirjautunutKayttaja.equals(this.naytettavaIlmoitus.getMyyjanId()))
            return true;
     
        // muuten -> viestiä ei näytetä.
        return false;
    }
    
    // Funktio päivittää viestin luetuksi tietokantaan.
    public void asetaViestiLuetuksi(Viesti muokattavaViesti){
        try {
               System.out.println("Asetetaan viesti luetuksi tietokantaan! ->" + muokattavaViesti.getViestinrunko());
               Viesti v = eManageri.find(Viesti.class, muokattavaViesti.getViestiId());
               trans.begin();
               v.setViestiluettu(1);
               eManageri.merge(v);
               trans.commit();
               System.out.println("Tallennus onnistui!");
           }
           catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
           System.out.println(e.getMessage());
       }
    }
    
    // Funktio asettaa uusien viestien määrän ja kayttajanSaapuneetViestit -listan.
    public void asetaKayttajanUudetViestit(){
        try {
            eManageri.getEntityManagerFactory().getCache().evictAll();
            trans.begin();              
            Query query = eManageri.createQuery("Select Count(v.viestiId) from Viesti v WHERE v.viestiluettu = 0 AND ((v.ilmoitusId.myyjanId.kayttajaId = " + kirjautunutKayttaja.getKayttajaId() + " AND v.julkinenviesti = 1) OR v.vastaanottajaId.kayttajaId = " + kirjautunutKayttaja.getKayttajaId() + ")");
            kayttajanUusienViestienMaara = (long)query.getSingleResult();
            kayttajanSaapuneetViestit.clear();
            kayttajanSaapuneetViestit = eManageri.createQuery("Select v FROM Viesti v WHERE (v.julkinenviesti = 1 AND v.lahettajaId.kayttajaId != " + kirjautunutKayttaja.getKayttajaId() + ") OR (v.julkinenviesti = 0 AND v.vastaanottajaId.kayttajaId = " + kirjautunutKayttaja.getKayttajaId() + ") ORDER BY v.lahetysaika DESC").getResultList();
            trans.commit();
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
	 
     /*************************************************************************
     * @param viestintyyppi
     * @throws java.io.IOException 
     *************************************************************************/
    // Funktio lisää uuden viestin tietokantaan (viestintyypi 0 = yksityinen, 1 = julkinen). 
    public void lisaaViesti(int viestintyyppi) throws IOException{
        uusiViesti.setLahettajaId(kirjautunutKayttaja);
        uusiViesti.setIlmoitusId(naytettavaIlmoitus);
        uusiViesti.setLahetysaika(new Date());
        uusiViesti.setJulkinenviesti(viestintyyppi);
        
        if(viestintyyppi == 0){
            for(Kayttaja k : yksityistenViestienVastaanottajat){
                if(k.getKayttajaId() == yksityisenViestinVastaanottajaId){
                    uusiViesti.setVastaanottajaId(k);
                    break;
                }
            }
            yksityisenViestinVastaanottajaId = -1;
        }
        // Jos viestin lähettäjä on sama kuin ilmoituksen jättäjä ja viesti on julkinen,
        // merkataan viesti luetuksi (=1). Muuten merkataan vieti lukemattomaksi(=0).
        if(uusiViesti.getIlmoitusId().getMyyjanId().equals(kirjautunutKayttaja) && uusiViesti.getJulkinenviesti() == 1)
                uusiViesti.setViestiluettu(1);
        else
                uusiViesti.setViestiluettu(0);
        
        try {
            System.out.println("Lisätään uusi viesti kantaan!");
            trans.begin();
                       
            eManageri.persist(getUusiViesti());
           
            trans.commit();
            System.out.println("Lisäys onnistui!");
            
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
        System.out.println(e.getMessage());
        }
        this.setUusiViesti(new Viesti());
        // Ladataan iloituksenesittely uudelleen
        NaytaIlmoitus(naytettavaIlmoitus.getIlmoitusId());
    }
    
    /*************************************************************************
    *  Viestin lisäämiseen/näyttämiseen liittyvät funktiot - loppuu
    *************************************************************************/    
    
    /*************************************************************************
     *  Ilmoituksen lisäämiseen liittyvät funktiot - alkaa
     * @throws java.io.IOException
     *************************************************************************/
    
    public void lueIlmoituksentiedot() throws IOException {
        System.out.println("Luetaan käyttäjän antamat ilmoituksen tiedot!");
       
        for(Alakategoria a : valittuAlakategoriaLista) {
            if(a.getAlakategoriaId().equals(valittuAlakategoriaID)) {
                uusiIlmoitus.setAlakategoriaId(a);
                break;
            }
        }       
        uusiIlmoitus.setMyyjanId(kirjautunutKayttaja);
        uusiIlmoitus.setIlmoitusjatettyPvm(new Date());
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("lisaakuvat.xhtml");
    }
    
    // Funktio lisää käyttäjän lataaman kuvatiedoston listaan. 
    public void lisaaKuva() throws IOException {
        // Generoidaan kuvalle nimi, muotoa "päivämäärä millisekunteina" + "." +  "kuvanyyppi"; 
        Calendar cal = Calendar.getInstance();
        String generoituKuvanNimi = cal.getTimeInMillis() + "." + haeTiedostontyyppi(kuvaTiedosto);
        
        // Ladattu kuva kuvatiedostojen nimet listaan.
        lisattyjenKuvatiedostojenNimet.add(generoituKuvanNimi);
        
        // Tallennetaan kuva serverille.
        tallennaKuva(kuvaTiedosto, generoituKuvanNimi);            
        
        // Skaalataan kuva 1000 pikseliä leveäksi. (Mikäli kuva tätä leveämpi) 
        saveScaledImage(kuvienPolkuServerilla+generoituKuvanNimi, haeTiedostontyyppi(kuvaTiedosto));
        
        kuvaTiedosto = null;
        
        //Ladataan sivu uudelleen.
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
    // Funktio poistaa kuvan ilmoituksesta.
    public void poistaKuvaIlmoituksesta(String kuvanNimi) throws IOException{
        lisattyjenKuvatiedostojenNimet.remove(kuvanNimi);
        poistaKuva(kuvanNimi);
      
        //Latataan sivu uudelleen.
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
    // Funktio asettaa kuvan ensimmäseksi kuvalistalla
    public void asetaKuvaEnsimmaiseksi(String kuvanNimi) throws IOException{
        lisattyjenKuvatiedostojenNimet.remove(kuvanNimi);
        lisattyjenKuvatiedostojenNimet.add(0, kuvanNimi);
       
        //Latataan sivu uudelleen.
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
    // Funktio tallentaa uuden ilmoituksen tietokantaan. Käyttäjän lataamat kuvat tallennetaan serverille.
    public void julkaiseUusiIlmoitus() throws IOException {
        if(lisattyjenKuvatiedostojenNimet.isEmpty()){ // mikäli käyttäjä ei ladannut yhtään kuvaa!
            uusiIlmoitus.setKuvienpolku("default-picture.jpg");
        } 
        try {
            System.out.println("Lisätään uusi ilmoitus!");
            trans.begin();
            eManageri.persist(uusiIlmoitus);
            
            if(!lisattyjenKuvatiedostojenNimet.isEmpty()){ // Mikäli käyttäjä latasi kuvia, aseteaan kuvien polku tietokantaan.
                String polku = "";
                
                for(String kuvaNimi : lisattyjenKuvatiedostojenNimet){
                    polku += kuvaNimi + " ";
                }
                // Päivitetään kuvienpolku tietokantaan.
                Ilmoitus muokattavaIlmoitus = eManageri.find(Ilmoitus.class, uusiIlmoitus.getIlmoitusId());
                muokattavaIlmoitus.setKuvienpolku(polku);
                eManageri.merge(muokattavaIlmoitus);
            }      
            trans.commit();
            System.out.println("Ilmoitus lisätty onnistuneesti!");
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
      
        // nollataan muuttujat;
        this.valittuPaakategoriaID = 0;
        this.valittuValikategoriaID = 0;
        this.valittuAlakategoriaID = 0;
        kuvaTiedosto = null;
        lisattyjenKuvatiedostojenNimet = new ArrayList<>();
        this.uusiIlmoitus = new Ilmoitus();
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("ilmoituslisattyonnistuneesti.xhtml");       
    }
    // Funktio tallentaa kuvan serverille.
    public void tallennaKuva(Part kuvaTiedosto, String kuvannimi) throws IOException{
        try ( 
            InputStream inputStream = kuvaTiedosto.getInputStream(); 
            FileOutputStream outputStream = new FileOutputStream(kuvienPolkuServerilla + kuvannimi)) {
            
            byte[] buffer = new byte[4096];
            int bytesRead = 0;
          
            while(true) {
                bytesRead = inputStream.read(buffer);
               
                if(bytesRead > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                }else {
                    break;
                }
            }
        }
    }
      
    // Funktio hakee kuvan tiedostotyypin kuvatiedoston headerista.
    private static String haeTiedostontyyppi(Part tiedosto) {  
        for (String cd : tiedosto.getHeader("content-disposition").split(";")) { 
            if (cd.trim().startsWith("filename")) {  
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");  
                filename = filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.  
                int i = filename.lastIndexOf('.');
                return filename.substring(i+1);
            }  
        }  
        return null;  
    }
  
    // Funktio skaalaa kuvan siten, että kuvan leveys on 1000 pikseliä.
    public void saveScaledImage(String filePath,String filetType){
    try {         
        BufferedImage sourceImage = ImageIO.read(new File(filePath));
        if(sourceImage.getWidth() <= kuvanLeveys)
            return;
        
        float ratio = (float)sourceImage.getWidth()/(float)sourceImage.getHeight();
        float percentHight = kuvanLeveys/ratio;
        
        BufferedImage img = new BufferedImage(kuvanLeveys, (int)percentHight, BufferedImage.TYPE_INT_RGB);
        Image scaledImage = sourceImage.getScaledInstance(kuvanLeveys,(int)percentHight, Image.SCALE_DEFAULT);
        img.createGraphics().drawImage(scaledImage, 0, 0, null);
       
        ImageIO.write(img, filetType, new File(filePath));
    } catch (IOException e) {
        System.out.println("Virhe skaalattaessa kuvaa:\n" + e);
    }
}
    /*************************************************************************
     *  Ilmoituksen lisäämiseen liittyvät funktiot - loppuu
     *************************************************************************/
    
    /************************************************************************
    *   Ilmoituksen muokkaamiseen liittyvät  funktiot - alkaa 
     * @throws java.io.IOException
    ************************************************************************/
    public void ohjaaIlmoituksenmuokkaussivulle() throws IOException{
        valittuAlakategoriaID = naytettavaIlmoitus.getAlakategoriaId().getAlakategoriaId();
        valittuValikategoriaID = naytettavaIlmoitus.getAlakategoriaId().getValikategoriaId().getValikategoriaId();
        valittuPaakategoriaID = naytettavaIlmoitus.getAlakategoriaId().getValikategoriaId().getPaakategoriaId().getPaakategoriaId();
       
        FacesContext.getCurrentInstance().getExternalContext().redirect("ilmoituksenmuokkaus.xhtml");    
    }
    
    public void lueIlmoituksenMuutokset() throws IOException { 
        System.out.println("Luetaan muutokset ilmoituksen tietoihin!");
       
        for(Alakategoria a : valittuAlakategoriaLista) {
            if(a.getAlakategoriaId().equals(valittuAlakategoriaID)) {
                naytettavaIlmoitus.setAlakategoriaId(a);
                break;
            }
        }
        lisattyjenKuvatiedostojenNimet = getIlmoituksenKuvat();
        // Oletuskuvaa ei näytetä muokkaailmoituksenkuvia.xhtml -sivulla! 
        if(lisattyjenKuvatiedostojenNimet.contains("default-picture.jpg"))
            lisattyjenKuvatiedostojenNimet.clear();
            
        FacesContext.getCurrentInstance().getExternalContext().redirect("muokkaailmoituksenkuvia.xhtml");
    }
    
    public void tallennaMuokattuIlmoitus() throws IOException{
        if(lisattyjenKuvatiedostojenNimet.isEmpty()){ // Mikäli käyttäjä ei ladannut yhtään kuvaa!
            naytettavaIlmoitus.setKuvienpolku("default-picture.jpg");
        }
        else{
            String polku ="";
     
            for(String kuvaNimi : lisattyjenKuvatiedostojenNimet){           // Pivitetään kuvien nimet serverille ja kuvienpolku kenttään!
                    polku += kuvaNimi + " "; 
            }
            naytettavaIlmoitus.setKuvienpolku(polku);
        }
        // Poistetaan kuvat, jotka eivät enää kuulu ilmoitukseen, serveriltä! 
        for(String kuva : getIlmoituksenKuvat()){
            if(!lisattyjenKuvatiedostojenNimet.contains(kuva))
               poistaKuvaIlmoituksesta(kuva);
        }
      
        // nollataan muuttujat;
        this.valittuPaakategoriaID = 0;
        this.valittuValikategoriaID = 0;
        this.valittuAlakategoriaID = 0;
        kuvaTiedosto = null;
        lisattyjenKuvatiedostojenNimet = new ArrayList<>();
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("ilmoituksenesittely.xhtml");       
    }

    /************************************************************************
    *   Ilmoituksen muokkaamiseen liittyvät  funktiot - loppuu 
    ************************************************************************/
    
    /*************************************************************************
     *  Ilmoituksen poistamiseen liittyvät funktiot - alkaa
     * @param ilmoitusId
     *************************************************************************/
    // Funktio lisää tai poistaa poistettavaksi valitun ilmoituksen poistolistaan. Fuktiota kutsutaan omasivu.xhtml:stä 
    public void poistolista(int ilmoitusId) {        
        if (isPoista() == true && !poistolista.contains(ilmoitusId)) {
           poistolista.add(ilmoitusId);
        }
        else {
            poistolista.remove(new Integer(ilmoitusId));
        }
    }
    // Funktio poistaa yhden ilmoituksen, kusutaan ilmoituksenesittely.xhtml sivulta.
    public void poistaYksiIlmoitus(int ilmoitusId) throws IOException{
        poistolista.clear();
        poistolista.add(ilmoitusId);
        poistaIlmoitus();
    }
    // Funktio poistaa ilmoituksen tietokannasta.
    public void poistaIlmoitus() throws IOException { 
        try {
            System.out.println("Poistetaan ilmoitus kannasta");
            trans.begin();
            for (int poistettavaId : poistolista) {
                Ilmoitus poistettavaIlmoitus = eManageri.find(Ilmoitus.class, poistettavaId);
                
                // Poistetaan ilmoitukseen liittyvät kuvat serveriltä!
                String kuvat =  poistettavaIlmoitus.getKuvienpolku();
                while(true){
                    int i = kuvat.indexOf(" ");
                   
                    if(i == -1){
                        poistaKuva(kuvat);
                        break;
                    }
                    else{
                        poistaKuva(kuvat.substring(0, i));
                        kuvat = kuvat.substring(i + 1, kuvat.length() );
                    }
                }
                // Poistetaan ilmoitus muistilistalta.
                muistilistanIlmoitukset.remove(poistettavaIlmoitus);
                eManageri.createQuery("DELETE FROM Muistilista m WHERE m.ilmoitusId = " + poistettavaId).executeUpdate();
                eManageri.remove(poistettavaIlmoitus);
            }
           
            trans.commit();
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        // Alustetaan poistolista
        poistolista = new ArrayList<>();
        kayttajanIlmoituksetHaku();
        FacesContext.getCurrentInstance().getExternalContext().redirect("omasivu.xhtml");
    }
    // Poistaa kuvan serveriltä.   
    public void poistaKuva(String kuvanNimi){
        // Oletuskuvaa ei poisteta!
        if("default-picture.jpg".equals(kuvanNimi))
            return;
        
        try{
            File file = new File(kuvienPolkuServerilla + kuvanNimi);

            if(file.delete()){
                    System.out.println(file.getName() + " is deleted!");
            }else{
                    System.out.println(kuvanNimi + " Delete operation is failed.");
            }

        }catch(Exception e){
            System.out.println("Virhe poistettaessa kuvaa!\n" + e);
        }       
    }   
    /*************************************************************************
     *  Ilmoituksen poistamiseen liittyvät funktiot - loppuu
     *************************************************************************/    
    
    /*************************************************************************
    *   Kirjautumiseen ja ulos kirjautumiseen liittyvät funktiot - alkaa
     * @throws java.io.IOException
    **************************************************************************/
        // Funktiota kutsutaan kun käyttäjä kirjautuu facebook tunnuksilla.
        public void kayttajaKirjaudu() throws IOException{
        // Mikäli käyttäjä on jo kirjauteneena!
        if(this.onkoKayttajaKirjautunut == true){
            return;
        }
        if(kirjautunutKayttaja.getFacebookid() == 0)
            return;
        
        List<Kayttaja>kayttajat = new ArrayList<>();
        
         try {
            System.out.println("Haetaan facebookid:tä vastaava käyttäjä tietokannasta!");
            trans.begin();
            kayttajat = eManageri.createQuery("Select user from Kayttaja user WHERE USER.facebookid = " + kirjautunutKayttaja.getFacebookid()).getResultList();
            trans.commit();
            System.out.println("Haku onnistui!!!!");
            
            if(kayttajat.isEmpty()){ 
                System.out.println("FacebookId:tä vastaavaa käyttäjää ei löytynyt!");
                this.rekisterointiSivuNaytetaan = true;
                
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                String url = ((HttpServletRequest) ec.getRequest()).getRequestURI();
                url = url.substring(0, url.lastIndexOf("/") + 1);
                ec.redirect(url + "rekisterointi.xhtml");
            }
            else{
                System.out.println("FacebookId:tä vastaavaa käyttäjä löytyi tietokannasta!");
                this.onkoKayttajaKirjautunut = true;
                kirjautunutKayttaja = kayttajat.get(0);
                
                List<Muistilista>mlista = new ArrayList<>();
                trans.begin();
                mlista = eManageri.createQuery("Select m from Muistilista m WHERE m.kayttajaId = " + kirjautunutKayttaja.getKayttajaId()).getResultList();
                trans.commit();
                
                for(Muistilista m: mlista){
                    trans.begin();
                    muistilistanIlmoitukset.add((Ilmoitus)eManageri.createQuery("Select i from Ilmoitus i WHERE i.ilmoitusId = " + m.getIlmoitusId()).getSingleResult());
                    trans.commit();
                }
                
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
            }   
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }   
    }
    
    // Lisää uuden käyttäjän tiedot tietokantaan!
    public void lisaaKayttaja() throws IOException{
       
            try {
            System.out.println("Lisätään uusi käyttäjä kantaan!");
            if (naytaSahkoposti == true) {
                kirjautunutKayttaja.setSahkopostinaytetaan(1);
            }
            else kirjautunutKayttaja.setSahkopostinaytetaan(0);
            if (naytaPuhelin == true) {
                kirjautunutKayttaja.setPuhelinnumeronaytetaan(1);
            }
            else kirjautunutKayttaja.setPuhelinnumeronaytetaan(0);
            trans.begin();
            eManageri.persist(kirjautunutKayttaja);
            trans.commit();
            System.out.println("Lisäys onnistui!");
            
            this.onkoKayttajaKirjautunut = true;
            this.rekisterointiSivuNaytetaan = false;
            FacesContext.getCurrentInstance().getExternalContext().redirect("omasivu.xhtml");
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
        System.out.println(e.getMessage());
        }
    }
    
    // Muuta käyttäjän tietoja tietokantaan!
    public void muutaTietoja() throws IOException{ 
        try {
            System.out.println("Muutetaan käyttäjän tietoja!");
            if (naytaSahkoposti == true) {
                kirjautunutKayttaja.setSahkopostinaytetaan(1);
            }
            else kirjautunutKayttaja.setSahkopostinaytetaan(0);
            if (naytaPuhelin == true) {
                kirjautunutKayttaja.setPuhelinnumeronaytetaan(1);
            }
            else kirjautunutKayttaja.setPuhelinnumeronaytetaan(0);
            
            Kayttaja k = eManageri.find(Kayttaja.class, kirjautunutKayttaja.getKayttajaId());
            trans.begin();
            k.setFacebooknimi(kirjautunutKayttaja.getFacebooknimi());
                System.out.println(kirjautunutKayttaja.getFacebooknimi());
            k.setSahkoposti(kirjautunutKayttaja.getSahkoposti());
                System.out.println(kirjautunutKayttaja.getSahkoposti());
            k.setPostinumero(kirjautunutKayttaja.getPostinumero());
                System.out.println(kirjautunutKayttaja.getPostinumero());
            k.setPuhelinnumero(kirjautunutKayttaja.getPuhelinnumero());
                System.out.println(kirjautunutKayttaja.getPuhelinnumero());
            k.setSahkopostinaytetaan(kirjautunutKayttaja.getSahkopostinaytetaan());
                System.out.println(kirjautunutKayttaja.getSahkopostinaytetaan());
            k.setPuhelinnumeronaytetaan(kirjautunutKayttaja.getPuhelinnumeronaytetaan());
                System.out.println(kirjautunutKayttaja.getPuhelinnumeronaytetaan());
            eManageri.merge(k);
            trans.commit();
            
            this.onkoKayttajaKirjautunut = true;
            this.rekisterointiSivuNaytetaan = false;
            ohjaaOmasivu();
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
        System.out.println(e.getMessage());
        }
    }
    
    // Kutsutaan käyttäjän kirjautuessa ulos.
    public void kirjauduUlos() throws IOException{
        System.out.println("Kirjaudutaan ulos!");
        // Nollataan sessio!
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        // Ohjataan etusivulle!
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
    }
    
    public boolean naytaFacebookkirjautuminen(){
        if(this.onkoKayttajaKirjautunut == false && this.rekisterointiSivuNaytetaan == false)
            return true;
        
        return false;
    }
    
    public void ohjaaOmasivu() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("omasivu.xhtml");
    }

    /*************************************************************************
    *   Kirjautumiseen ja ulos kirjautumiseen liittyvät funktiot - loppuu
     * @return 
    **************************************************************************/
    
    /*************************************************************************
    *   Muistilistan funktiot - alkaa
    **************************************************************************/
    
    /**
     * Muistilistan funktiot - alkaa
     * @param ilmoitus
     * @throws java.io.IOException
     */
    public void lisaaIlmoitusMuistilistalle(Ilmoitus ilmoitus) throws IOException{
        muistilistanIlmoitukset.add(ilmoitus);
        
        Muistilista uusiMuistilistaIlmoitus = new Muistilista();
        uusiMuistilistaIlmoitus.setIlmoitusId(ilmoitus.getIlmoitusId());
        uusiMuistilistaIlmoitus.setKayttajaId(kirjautunutKayttaja.getKayttajaId());
        
        try { // Päivietään ilmoitus tietokantaan
            trans.begin();
            eManageri.persist(uusiMuistilistaIlmoitus);
            trans.commit();
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        
        //Ladataan sivu uudelleen.
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
    public void poistaIlmoitusMuistilistalta(Ilmoitus ilmoitus) throws IOException{
        muistilistanIlmoitukset.remove(ilmoitus);
  
        try { // Poistetaan ilmoitus tietokannasta
            trans.begin();
            Muistilista m = (Muistilista)eManageri.createQuery("Select i from Muistilista i WHERE i.ilmoitusId = " + ilmoitus.getIlmoitusId() + " AND i.kayttajaId = " + kirjautunutKayttaja.getKayttajaId()).getSingleResult();
            
            Muistilista poistettavaIlmoitus = eManageri.find(Muistilista.class, m.getMuistilistaId());
            eManageri.remove(poistettavaIlmoitus);
            trans.commit();
        }
        catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        
        //Ladataan sivu uudelleen.
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
    public boolean onkoIlmoitusMuistilistalla(Ilmoitus ilmoitus){
        return muistilistanIlmoitukset.contains(ilmoitus);
    }
    
    /*************************************************************************
    *   Muistilistan funktiot - loppuu
    **************************************************************************/
   
    /*************************************************************************
     *  Getterit ja setterit - alkaa
     *************************************************************************/
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
    
    /**
     * @return the naytaSahkoposti
     */
    public boolean isNaytaSahkoposti() {
        if(kirjautunutKayttaja.getSahkopostinaytetaan() == null)
            return false;
        
        if(kirjautunutKayttaja.getSahkopostinaytetaan() == 1)
            return true;
        else
            return false;
    }

    /**
     * @param naytaSahkoposti the naytaSahkoposti to set
     */
    public void setNaytaSahkoposti(boolean naytaSahkoposti) {
        this.naytaSahkoposti = naytaSahkoposti;
    }

    /**
     * @return the naytaPuhelin
     */
    public boolean isNaytaPuhelin() {
        if(kirjautunutKayttaja.getPuhelinnumeronaytetaan() == 1)
            naytaPuhelin = true;
        else
            naytaPuhelin = false;
        
        return naytaPuhelin;
    }

    /**
     * @param naytaPuhelin the naytaPuhelin to set
     */
    public void setNaytaPuhelin(boolean naytaPuhelin) {
        this.naytaPuhelin = naytaPuhelin;
    }

    /**
     * @return the uusiViesti
     */
    public Viesti getUusiViesti() {
        return uusiViesti;
    }

    /**
     * @param uusiViesti the uusiViesti to set
     */
    public void setUusiViesti(Viesti uusiViesti) {
        this.uusiViesti = uusiViesti;
    }

    /**
     * @return the rekisterointiSivuNaytetaan
     */
    public boolean isRekisterointiSivuNaytetaan() {
        return rekisterointiSivuNaytetaan;
    }

    /**
     * @param rekisterointiSivuNaytetaan the rekisterointiSivuNaytetaan to set
     */
    public void setRekisterointiSivuNaytetaan(boolean rekisterointiSivuNaytetaan) {
        this.rekisterointiSivuNaytetaan = rekisterointiSivuNaytetaan;
    }
    
    /**
     * @return the poista
     */
    public boolean isPoista() {
        return poista;
    }

    /**
     * @param poista the poista to set
     */
    public void setPoista(boolean poista) {
        this.poista = poista;
    }

    /**
     * @return the poistolista
     */
    public List<Integer> getPoistolista() {
        return poistolista;
    }

    /**
     * @param poistolista the poistolista to set
     */
    public void setPoistolista(List<Integer> poistolista) {
        this.poistolista = poistolista;
    }

    /**
     * @return the ilmoituksenViestitLista
     */
    public List<Viesti> getIlmoituksenViestitLista() {
        return ilmoituksenViestitLista;
    }

    /**
     * @return the ilmoitusListanJarjestys
     */
    public int getIlmoitusListanJarjestys() {
        return ilmoitusListanJarjestys;
    }

    /**
     * @param ilmoitusListanJarjestys the ilmoitusListanJarjestys to set
     */
    public void setIlmoitusListanJarjestys(int ilmoitusListanJarjestys) {
        this.ilmoitusListanJarjestys = ilmoitusListanJarjestys;
    }

    /**
     * @param kuvaTiedosto the kuvaTiedosto to set
     */
    public void setKuvaTiedosto(Part kuvaTiedosto) {
        this.kuvaTiedosto = kuvaTiedosto;
    }

    /**
     * @return the kuvaTiedosto
     */
    public Part getKuvaTiedosto() {
        return kuvaTiedosto;
    }

    /**
     * @return the lisattyjenKuvatiedostojenNimet
     */
    public List<String> getLisattyjenKuvatiedostojenNimet() {
        return lisattyjenKuvatiedostojenNimet;
    }

    /**
     * @return the kayttajanUusienViestienMaara
     */
    public long getKayttajanUusienViestienMaara() {
        return kayttajanUusienViestienMaara;
    }

    /**
     * @param vastausViestinRunko the vastausViestinRunko to set
     */
    public void setVastausViestinRunko(String vastausViestinRunko) {
        this.vastausViestinRunko = vastausViestinRunko;
    }

    /**
     * @return the vastausViestinRunko
     */
    public String getVastausViestinRunko() {
        return vastausViestinRunko;
    }

    /**
     * @return the vastaukset
     */
    public List<Viesti> getYksityisetViestit() {
        return yksityisetViestit;
    }

    /**
     * @return the yksityistenViestienLahettajat
     */
    public List<Kayttaja> getYksityistenViestienVastaanottajat() {
        return yksityistenViestienVastaanottajat;
    }

    /**
     * @return the yksityisenViestinVastaanottajaId
     */
    public int getYksityisenViestinVastaanottajaId() {
        return yksityisenViestinVastaanottajaId;
    }

    /**
     * @param yksityisenViestinVastaanottajaId the yksityisenViestinVastaanottajaId to set
     */
    public void setYksityisenViestinVastaanottajaId(int yksityisenViestinVastaanottajaId) {
        this.yksityisenViestinVastaanottajaId = yksityisenViestinVastaanottajaId;
    }

    /**
     * @return the kayttajanSaapuneetViestit
     */
    public List<Viesti> getKayttajanSaapuneetViestit() {
        return kayttajanSaapuneetViestit;
    }

    /**
     * @return the muistilistanIlmoitukset
     */
    public List<Ilmoitus> getMuistilistanIlmoitukset() {
        return muistilistanIlmoitukset;
    }   
}
    /*************************************************************************
     *  Getterit ja setterit - loppuu
     *************************************************************************/
    

