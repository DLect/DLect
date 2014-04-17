/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.helpers;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import org.dlect.export.University;
import org.dlect.internal.data.SemesterData;
import org.dlect.internal.data.SubjectData;
import org.dlect.internal.data.UniversityData;

/**
 *
 * @author lee
 */
@Local
public interface DatabaseInserterEJBLocal {

    public void flush();

    public List<SubjectData> insertAllIntoDatabase(UniversityData uni, final List<SubjectData> subjects);

    public List<SemesterData> insertAllIntoDatabase(UniversityData uni, Collection<SemesterData> semesterList);

    public SubjectData insertIntoDatabase(University uni, SubjectData genSd);

    public SubjectData insertIntoDatabase(UniversityData uni, SubjectData genSd);

    public SemesterData insertIntoDatabase(UniversityData uni, SemesterData genData);

    public SemesterData insertIntoDatabase(University uni, SemesterData genData);

}
