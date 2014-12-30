/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirppis.model;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author Opiframe
 */
@Named(value = "kirppisServiceFactory")
@SessionScoped
public class kirppisServiceFactory implements Serializable {
    private kirppisService kirppisService;
 
    @Resource private UserTransaction trans;
    @PersistenceContext private EntityManager eManageri;
    
    
    /**
     * Creates a new instance of kirppisServiceFactory
     */
    
    /**
     * Creates a new instance of kirppisServiceFactory
     * @return
     */
    @Produces
    @kirppisServiceQualifier
 

    /**
     * @return the kirppisService
     */
    public kirppisService getKirppisService() {
        if(kirppisService != null){
            return kirppisService;
        }
        else{
            kirppisService = new kirppisService(trans, eManageri);
            return kirppisService;
        }
    }

}
