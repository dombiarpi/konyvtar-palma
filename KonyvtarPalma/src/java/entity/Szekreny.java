/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.inject.Named;

/**
 *
 * @author u201993
 */
@Named("Szekreny")
public enum Szekreny {
    A("A"), B("B"), C("C"), D("D"), E("E"), F("F"), G("G"),H("E"), I("I"), J("J"), K("K"), L("L"), M("M"), N("N");
    
    Szekreny(String name) {
        this.name = name;
    }
    
    private final String name;
    public String getName() {
        return name;
    }
}
