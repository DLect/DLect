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

package org.dlect.ejb;

import com.google.common.base.Joiner;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.dlect.test.finders.AnnotatedClassFinder.getClassesWithAny;


/**
 *
 * @author lee
 */
public class InjecteeStateTest {

    @Test
    public void testNoSingleton() {
        Set<Class<?>> singletonClasses = getClassesWithAny("org.dlect", javax.ejb.Singleton.class, javax.inject.Singleton.class);
        assertTrue("The following classes are annotated with @Singleton(either ejb or inject. Please refrain from using it.\n"
                   + Joiner.on('\n').join(singletonClasses), singletonClasses.isEmpty());

    }
    @Test
    public void testNoEJBStatefull() {
        Set<Class<?>> statefulClasses = getClassesWithAny("org.dlect", javax.ejb.Stateful.class);
        
        for (Class<?> cls : statefulClasses) {
        }
        
        
//        assertTrue("The following classes are annotated with @Singleton(either ejb or inject. Please refrain from using it.\n"
//                   + Joiner.on('\n').join(statefulClasses), statefulClasses.isEmpty());

    }
    
    @Test
    public void testStatelessInjecteesOnly() {
        
    }

}
