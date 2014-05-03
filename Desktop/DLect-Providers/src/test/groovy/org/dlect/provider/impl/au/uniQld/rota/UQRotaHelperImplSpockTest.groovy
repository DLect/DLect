/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.provider.impl.au.uniQld.rota


import com.google.common.base.Optional
import org.dlect.exception.DLectException
import org.dlect.exception.DLectExceptionCause
import org.dlect.immutable.model.ImmutableSemester
import spock.lang.*
import static spock.lang.MockingApi.*


class UQRotaHelperImplSpockTest extends Specification {
    
    UQRotaSemesterParser semParser = Mock(UQRotaSemesterParser)
    UQRotaOfferingSearchParser offSearchParser = Mock(UQRotaOfferingSearchParser)
    UQRotaOfferingParser offParser = Mock(UQRotaOfferingParser)
    UQRotaHelperImpl obj = new UQRotaHelperImpl(semParser, offSearchParser, offParser)
       

    
    @Unroll
    def "getSemester[#code] with Exception Throw"(code, _x) {
        when:
         def sem = obj.getSemester(code)
         println "Hello: " + sem
        
        then:
         1 * semParser.getSemester(code) >> {throw new DLectException(DLectExceptionCause.DISK_ERROR, "Oops")}
         sem.getNum() == code
         sem.getLongName() == "Semester " + code
         sem.getCoursePostfixName() == Integer.toString(code)
        
        where:
        code | _x
        1    | _
        3    | _
        10   | _
        -1   | _
        20   | _
    }
    
    @Unroll
    def "getSemester[#code] with Absent Responce"(code, _x) {
        when:
         def sem = obj.getSemester(code)
         println "Hello: " + sem
        
        then:
         1 * semParser.getSemester(code) >> Optional.absent()
         sem.getNum() == code
         sem.getLongName() == "Semester " + code
         sem.getCoursePostfixName() == Integer.toString(code)
        
        where:
        code | _x
        1    | _
        3    | _
        10   | _
        -1   | _
        20   | _
    }
    
    @Unroll
    def "getSemester[#code] with Existing Responce"(code, longName, prefix) {
        setup:
         def expectedSem = new ImmutableSemester(code, longName, prefix)
        
        when:
         def sem = obj.getSemester(code)
         println "Hello: " + sem
        
        then:
         1 * semParser.getSemester(code) >> Optional.of(expectedSem)
         sem.getNum() == expectedSem.getNum()
         sem.getLongName() == expectedSem.getLongName()
         sem.getCoursePostfixName() == expectedSem.getCoursePostfixName()
        
        where:
        code | longName | prefix
        1    | "123"    | "24354a"
        3    | "sss"    | "asdd"
        10   | "1237d"  | "xx2"
        -1   | "yvhgff" | "asd"
        20   | "ututut" | "--1"
    }
    
    
}