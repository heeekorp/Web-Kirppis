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
public class hintaComparator implements Comparator<Ilmoitus>{
    @Override
    public int compare(Ilmoitus ilmoitus1, Ilmoitus ilmoitus2) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return (int)(ilmoitus1.getHinta() - ilmoitus2.getHinta());
    }
}
