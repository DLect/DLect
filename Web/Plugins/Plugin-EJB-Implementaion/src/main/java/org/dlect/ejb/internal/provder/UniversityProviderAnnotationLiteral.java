/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ejb.internal.provder;

import javax.enterprise.util.AnnotationLiteral;

/**
 *
 * @author lee
 */
public class UniversityProviderAnnotationLiteral extends AnnotationLiteral<UniversityProvider> implements UniversityProvider {

    private static final long serialVersionUID = 1L;

    public UniversityProviderAnnotationLiteral(String value) {
        super();
        this.value = value;
    }

    private final String value;

    @Override
    public String value() {
        return value;
    }

}
