/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.ejb.internal.provder;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 *
 * @author lee
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
@Documented
public @interface UniversityProvider {
/**
 * The universities internal code that the annotated class supports.
 * @return 
 */
    public String value();
}
