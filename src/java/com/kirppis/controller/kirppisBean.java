package com.kirppis.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.kirppis.data.Ilmoitus;
import com.kirppis.model.kirppisService;
import com.kirppis.model.kirppisServiceQualifier;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author Opiframe
 */
@Named(value = "kirppisBean")
@SessionScoped
public class kirppisBean implements Serializable {
    @Inject @kirppisServiceQualifier private kirppisService kirppisService;
    private List<String> images;
    /**
     * Creates a new instance of kirppisBean
    **/
    public kirppisBean() {
    }
    @PostConstruct
    public void init() {
        images = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            getImages().add("kuva" + i + ".jpg");
            System.out.println("kuva" + i + ".jpg");
        }
    }
     
    /**
     * @return the kirppisService
     */
    public kirppisService getKirppisService() {
        return kirppisService;
    }

    /**
     * @param kirppisService the kirppisService to set
     */
    public void setKirppisService(kirppisService kirppisService) {
        this.kirppisService = kirppisService;
    }

    /**
     * @return the images
     */
    public List<String> getImages() {
        return images;
    }
    
}
