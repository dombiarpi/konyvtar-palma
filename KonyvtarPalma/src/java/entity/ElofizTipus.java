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
@Named("ElofizTipus")
public enum ElofizTipus {
    EGYENI("Egyéni"), CSALADI("Családi"), NYUGDIJAS("Nyugdíjas"), DIAK("Diák"), SPECIALIS("Speciális");
    
    ElofizTipus(String name) {
        this.name = name;
    }
    
    private final String name;
    public String getName() {
        return name;
    }
}
