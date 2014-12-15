/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kirppis.data;

import java.util.Comparator;

/**
 *
 * @author Hezu
 */
public class pvmComparator implements Comparator<Ilmoitus> {

    public pvmComparator() {
    }

    
    @Override
    public int compare(Ilmoitus ilmoitus1, Ilmoitus ilmoitus2) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(ilmoitus1.getIlmoitusjatettyPvm().after(ilmoitus2.getIlmoitusjatettyPvm()))
            return -1;
        else
            return 1;
    }
 
}
