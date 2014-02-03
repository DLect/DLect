/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.db;

import java.util.List;
import javax.ejb.Local;
import javax.persistence.metamodel.SingularAttribute;
import org.dlect.export.University;
import org.dlect.helpers.DatabaseWhereOp;
import org.dlect.internal.data.SemesterData;
import org.dlect.internal.data.UniversityData;

/**
 *
 * @author lee
 */
@Local
public interface DataToDatabaseEJBLocal {

    public void flush();

    public <RT, V> List<RT> getAllEqualTo(SingularAttribute<RT, V> attr, V val);

    public <RT, V> List<RT> getAllWhere(SingularAttribute<RT, V> attr, DatabaseWhereOp<V> condition, V val);

    public <RT, V> RT getEqualTo(SingularAttribute<RT, V> attr, V val);

    public <RT, V> RT getEqualTo(SingularAttribute<RT, V> attr, V val, RT ifNone);

    public <RT, V1, V2> RT getEqualToWithFK(SingularAttribute<RT, V1> attr1, V1 val1, SingularAttribute<RT, V2> attr2, V2 val2);

    public <RT, V1, V2> RT getEqualToWithFK(SingularAttribute<RT, V1> attr1, V1 val1, SingularAttribute<RT, V2> attr2, V2 val2, RT ifNone);

    public UniversityData getFromExport(University u);

    /**
     *
     * @param uni     This is expected to be the university object stored inside the database.
     * @param semCode
     *
     * @return
     */
    public SemesterData getSemesterDataFromCode(UniversityData uni, String semCode);

    public <T> T merge(T genData);

    public <T> void persist(T genData);
    
}
