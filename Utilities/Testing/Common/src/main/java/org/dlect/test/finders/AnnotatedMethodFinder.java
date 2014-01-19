/*
 * This file is part of DLect. DLect is a suite of code that facilitates the downloading of lecture recordings.
 *
 * Copyright © 2014 Lee Symes.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dlect.test.finders;

import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

/**
 *
 * @author lee
 */
public class AnnotatedMethodFinder {

    @SafeVarargs
    public static Set<Method> getMethodsWithAll(String packageName, Class<? extends Annotation> annotation, Class<? extends Annotation>... annotations) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage(packageName))
                        .setScanners(new MethodAnnotationsScanner()));
        Set<Method> methods = Sets.newHashSet(reflections.getMethodsAnnotatedWith(annotation));

        for (Class<? extends Annotation> anot : annotations) {
            methods.retainAll(reflections.getMethodsAnnotatedWith(anot));
        }

        return methods;
    }

    @SafeVarargs
    public static Set<Method> getMethodsWithAny(String packageName, Class<? extends Annotation> annotation, Class<? extends Annotation>... annotations) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage(packageName))
                        .setScanners(new MethodAnnotationsScanner()));
        Set<Method> methods = Sets.newHashSet(reflections.getMethodsAnnotatedWith(annotation));

        for (Class<? extends Annotation> anot : annotations) {
            methods.addAll(reflections.getMethodsAnnotatedWith(anot));
        }

        return methods;
    }

}
