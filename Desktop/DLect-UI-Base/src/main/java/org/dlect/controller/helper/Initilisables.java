/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.helper;

/**
 *
 * @author lee
 */
public class Initilisables {

    private Initilisables() {
    }

    public static void doInit(Initilisable... initilisables) {
        for (Initilisable i : initilisables) {
            i.init();
        }
    }

}
