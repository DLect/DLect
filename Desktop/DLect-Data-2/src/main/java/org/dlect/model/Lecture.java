/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.model;

import org.dlect.events.listenable.Listenable;

/**
 *
 * @author lee
 */
public class Lecture extends Listenable<Lecture> implements Comparable<Lecture>{

    @Override
    public int compareTo(Lecture o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
