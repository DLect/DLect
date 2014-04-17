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
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import javax.ws.rs.Path;
import org.dlect.logging.TestLogging;
import org.glassfish.jersey.server.JSONP;
import static org.dlect.test.finders.AnnotatedMethodFinder.*;
import static org.dlect.test.MarshalCapableTester.*;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 *
 * @author lee
 */
public class RestReturnTypeTest {

    @Test
    public void testRestMethodsReturnResponceType() {
        Set<Method> methods = getMethodsWithAny("org.dlect", Path.class);

        List<String> errorMessages = Lists.newArrayList();

        Set<Class<?>> illegalReturnTypes = Sets.newHashSet();
        
        
        for (Method m : methods) {
            String prefix = m.toString() + "(" + m.getDeclaringClass() + ") ";
            
            TestLogging.LOG.error("Testing: {}", prefix);
            
            testMarshalObject(m.getReturnType());
            
            JSONP jsonpAnnot = m.getAnnotation(JSONP.class);
            if (jsonpAnnot == null) {
                errorMessages.add(prefix + "does not declare a @JSONP annotation");
            } else if (jsonpAnnot.queryParam() == null || !"callback".equals(jsonpAnnot.queryParam())) {
                errorMessages.add(prefix + "must declare a query parameter of \"callback\"");
            }
        }

        if (!errorMessages.isEmpty() ||! illegalReturnTypes.isEmpty()) {
            
            
            String message = "Failed due to the following problems: \n" + Joiner.on('\n').join(errorMessages);
            TestLogging.LOG.error(message);
            fail(message);
        }
    }

}
