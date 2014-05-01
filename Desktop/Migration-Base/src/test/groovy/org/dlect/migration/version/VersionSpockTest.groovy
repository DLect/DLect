/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.migration.version

import spock.lang.Specification

import spock.lang.*
import static spock.lang.MockingApi.*

class VersionSpockTest extends Specification {
    
    @Unroll
    def "getVersionString[#version] -> #versionString"(version, versionString) {
        when:
        Version v = new Version(version)
        
        then:
        v.getVersionString().equals(versionString)
        
        where:
        version           | versionString
        [1]               | "1"
        [3,1]             | "3.1"
        [10,100]          | "10.100"
        [1,2,3,4,5,6,7,8] | "1.2.3.4.5.6.7.8"
        [123,123,123]     | "123.123.123"
    }
    
    @Unroll
    def "getVersionNumbers[#version] -> #version"(version, _x) {
        when:
        Version v = new Version(version)
        
        then:
        v.getVersionNumbers().equals(version)
        
        where:
        version           | _x
        [1]               | _
        [3,1]             | _
        [10,100]          | _
        [1,2,3,4,5,6,7,8] | _
        [123,123,123]     | _
    }
    
    @Unroll
    def "parseVersion{Strict}{Valid}[#versionString] -> #versionString"(versionString, _x) {
        when:
        Version v = Version.parseVersion(versionString)
        
        then:
        v.getVersionString().equals(versionString)
        
        where:
        versionString     | _x
         "1"               | _
         "3.1"             | _
         "100.1000.10"     | _
         "1.2.3.4.5.6.7.8" | _
         "123.123.123"     | _
    }
    
    @Unroll
    def "parseVersion{Strict}{Invalid}[#versionInputString]"(versionInputString, _x) {
        when:
        Version.parseVersion(versionInputString)
        
        then:
        thrown(IllegalArgumentException)
            
        where:
        versionInputString  | _x
         "-Pre"             | _
         "--"               | _
         ""                 | _
         "             "    | _
         "1-Pre"            | _
         "."                | _
         "3--"              | _
         ".3--"             | _
         ".3"               | _
         "........."        | _
         "1..2..3.."        | _
         "ASDF-123"         | _
         "-1.-2.-3"         | _
         "1.2.3   "         | _
    }
    
    @Unroll
    def "parseVersion{Non Strict}{PartiallyValid}[#versionInputString] -> #versionString"(versionInputString, versionString) {
        when:
        Version v = Version.parseVersionNonStrict(versionInputString)
        
        then:
        v.getVersionString().equals(versionString)
        
        where:
        versionInputString | versionString
         "1-Pre"            | "1"
         "3--"              | "3"
         ".3--"             | "0.3"
         "........."        | "0"
         "1..2..3.."        | "1.2.3"
         "ASDF-123"         | "123"
         "-1.-2.-3"         | "1.2.3"
    }
    
    @Unroll
    def "parseVersion{Non Strict}{Valid}[#versionString] -> #versionString"(versionString, _x) {
        when:
        Version v = Version.parseVersionNonStrict(versionString)
        
        then:
        v.getVersionString().equals(versionString)
        
        where:
        versionString      | _x
         "1"               | _
         "3.1"             | _
         "100.1000.10"     | _
         "1.2.3.4.5.6.7.8" | _
         "123.123.123"     | _
    }
    
    @Unroll
    def "parseVersion{Non Strict}{Invalid}[#versionString] -> #versionString"(versionString, _x) {
        when:
        Version.parseVersion(versionString)
        
        then:
        thrown(IllegalArgumentException)
        where:
        versionString       | _x
         "-Pre"             | _
         "--"               | _
         ""                 | _
         "             "    | _
    }
    
    @Unroll
    def "[#subVersion].isSubversion[#version] -> #result"(subVersion, version, result) {
        when:
        def subVer = new Version(subVersion)
        def ver = new Version(version)

        then:
        subVer.isSubversion(ver) == result
        
        where:
        subVersion      | version      | result
        [1]             | [1]          | true
        [1, 0]          | [1]          | true
        [1, 1]          | [1]          | true
        [1, 2, 3, 4]    | [1]          | true
        [2]             | [1]          | false
        [2, 0]          | [1]          | false
        [1]             | [1, 1]       | false
        [1, 0]          | [1, 1]       | false
        [1, 0, 1]       | [1, 1]       | false
        [1, 1]          | [1, 1]       | true
        [1, 2, 3, 4]    | [1, 1]       | false
        [1, 3]          | [1, 1]       | false
        [1, 2, 3, 4]    | [3, 4, 5, 6] | false
        [3]             | [3, 4, 5, 6] | false
        [3, 4]          | [3, 4, 5, 6] | false
        [3, 4, 5]       | [3, 4, 5, 6] | false
        [3, 4, 5, 6]    | [3, 4, 5, 6] | true
        [3, 4, 5, 6, 7] | [3, 4, 5, 6] | true
        [3, 4, 5, 7]    | [3, 4, 5, 6] | false
        [3, 4, 6]       | [3, 4, 5, 6] | false
    }
    
}