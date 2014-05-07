/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.controller.file;

import org.dlect.model.Lecture;
import org.dlect.model.LectureDownload;
import spock.lang.*
import static spock.lang.MockingApi.*


class FileFolderControllerSpockTest extends Specification {
    def d(y,mon,d,h,min) {
        def i = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        i.set(y,mon - 1, d, h, min)
        return i.getTime()
    }
    
    @Unroll
    def "Check file dates are in 24 hour time. formatLectureDate[#date] => #expected"(date, expected) {
        
        setup:
            def ffc = new FolderFileController();
            def lec = new Lecture();
            lec.setTime(date);
            def lecD = new LectureDownload();
        when:
            def res = ffc.formatLectureDate(lec, lecD);
        then:
            res == expected
        
        where:

            // Yr, Mon, Dat, Hrs, Min
            date                           | expected
            d(2014, 5, 5, 13, 10)   | "2014-05-05 13.10"
            d(2014, 5, 5, 12, 10)   | "2014-05-05 12.10"
            d(2014, 5, 5, 11, 10)   | "2014-05-05 11.10"
            d(2014, 4, 6, 13, 10)   | "2014-04-06 13.10"
            d(2014, 5, 5, 19, 59)   | "2014-05-05 19.59"
            d(2012, 1, 4, 8, 10)    | "2012-01-04 08.10"
            d(2014, 12, 29, 13, 10) | "2014-12-29 13.10"
            d(2014, 5, 5, 13, 0)    | "2014-05-05 13.00"
            d(2014, 5, 5, 13, 1)    | "2014-05-05 13.01"
        
    }
}
