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
package org.dlect.helpers;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.dlect.db.DataToDatabaseEJBLocal;
import org.dlect.export.University;
import org.dlect.helpers.DatabaseInserterEJBLocal;
import org.dlect.internal.data.SemesterData;
import org.dlect.internal.data.SubjectData;
import org.dlect.internal.data.SubjectData_;
import org.dlect.internal.data.UniversityData;
import org.dlect.log.Stores;

/**
 *
 * @author lee
 */
@Stateless
public class DatabaseInserterEJB implements DatabaseInserterEJBLocal {

    @EJB
    private DataToDatabaseEJBLocal helper;

    @Override
    public List<SubjectData> insertAllIntoDatabase(UniversityData uni, final List<SubjectData> subjects) {
        List<SubjectData> databaseSubjects = Lists.newArrayListWithCapacity(subjects.size());
        for (SubjectData genSd : subjects) {
            databaseSubjects.add(insertIntoDatabase(uni, genSd));
        }
        return databaseSubjects;
    }

    @Override
    public SubjectData insertIntoDatabase(University uni, SubjectData genSd) {
        return insertIntoDatabase(helper.getFromExport(uni), genSd);
    }

    @Override
    public SubjectData insertIntoDatabase(UniversityData uni, SubjectData genSd) {
        SemesterData semDb = helper.getSemesterDataFromCode(uni, genSd.getSemesterId().getSemesterCode());
        SubjectData oldSubDb = helper.getEqualToWithFK(SubjectData_.name, genSd.getName(), SubjectData_.semesterId, semDb);
        if (oldSubDb != null) {
            // Keep existing data
            genSd.setLectureList(oldSubDb.getLectureList());
            genSd.setStreamList(oldSubDb.getStreamList());
            genSd.setSubjectId(oldSubDb.getSubjectId());
        }
        genSd.setDataLastUpdated(new Date());
        // TODO add information for stream timeout ect.
        // To be sure.
        genSd.setSemesterId(semDb);
        SubjectData subDb = helper.merge(genSd);

        semDb.addSubjectData(subDb);

        Stores.LOG.error("Subject data after merge:\nName: {}\nOld ID: {}\nNew ID: {}", genSd.getName(), genSd.getSubjectId(), subDb.getSubjectId());

        return subDb;
    }

    @Override
    public List<SemesterData> insertAllIntoDatabase(UniversityData uni, Collection<SemesterData> semesterList) {
        List<SemesterData> data = Lists.newArrayListWithCapacity(semesterList.size());
        for (SemesterData genData : semesterList) {
            data.add(insertIntoDatabase(uni, genData));
        }
        return data;
    }

    @Override
    public SemesterData insertIntoDatabase(UniversityData uni, SemesterData genData) {
        SemesterData semDb = helper.getSemesterDataFromCode(uni, genData.getSemesterCode());

        if (semDb != null) {
            genData.setSemesterId(semDb.getSemesterId());
        }

        // To be sure.
        genData.setUniversityId(uni);

        return helper.merge(genData);
    }

    @Override
    public SemesterData insertIntoDatabase(University uni, SemesterData genData) {
        return insertIntoDatabase(helper.getFromExport(uni), genData);
    }

    @Override
    public void flush() {
        helper.flush();
    }

}
