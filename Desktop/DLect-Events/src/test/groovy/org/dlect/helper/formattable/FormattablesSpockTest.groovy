/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.helper.formattable


import com.google.common.base.Optional
import spock.lang.*
import static spock.lang.MockingApi.*


class FormattablesSpockTest extends Specification {
    
    @Unroll
    def "getIndent[#indent]"(indent, _x) {
        expect:
        Formattables.getIndent(indent).length() == indent
        
        where:
        indent | _x
        1      | _
        3      | _
        10     | _
        20     | _
    }
    
    @Unroll
    def "toString[EmptyFormattable]"() {
        setup:
        def f = {[:]} as Formattable
        expect:
        Formattables.toString(f) == f.getClass().toString() + " {\n}"
    }
         
    @Unroll
    def "toString[One Item(#name: #value)]"(name, value) {
        setup:
        def f = {[(name):value]} as Formattable
        expect:
        Formattables.toString(f) == f.getClass().toString() + " {\n" + "    " + name + "=" + value + "\n}"
        where:
        name | value
        "H"  | "sdlkfhsdkjhfsd"
        "V"  | "334tfrtdfg"
        "sd" | "sd892357892347893427892348902348902348908902348423989234890423890234f"
        "aa" | "aop2oieiutiui289jk"
    }
    
    @Unroll
    def "toString[One depth repeat(#fname:(#name: #value))]"(fname, name, value) {
        setup:
        def f1 = {[(name):value]} as Formattable
        def f = {[(fname):f1]} as Formattable
        expect:
        Formattables.toString(f) == f.getClass().toString() + " {\n" + 
                                        "    " + fname + "=" + f1.getClass().toString() + " {\n" + 
                                        "        " + name + "=" + value +
                                        "\n    }" + 
                                        "\n}"
        where:
        fname | name | value
        "ASD" |"H"  | "sdlkfhsdkjhfsd"
        "vv"  |"V"  | "334tfrtdfg"
        "1"   |"sd" | "sd892357892347893427892348902348902348908902348423989234890423890234f"
        "aa"  |"aa" | "aop2oieiutiui289jk"
    }
         
}