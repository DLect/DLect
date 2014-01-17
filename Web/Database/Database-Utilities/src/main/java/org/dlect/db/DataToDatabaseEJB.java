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
package org.dlect.db;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.dlect.export.University;
import org.dlect.helpers.DatabaseWhereOp;
import org.dlect.internal.data.SemesterData;
import org.dlect.internal.data.SemesterData_;
import org.dlect.internal.data.UniversityData;
import org.dlect.internal.data.UniversityData_;
import org.dlect.log.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author lee
 */
@Stateless
@Startup
public class DataToDatabaseEJB {

    @PersistenceContext(name = "DaaS-Uni")
    private EntityManager manager;

    @PostConstruct
    public void initManager() {
        // This prevents NPEs when an application first tries to use the metamodel.
        manager.getMetamodel();
    }

    private static final Logger LOG = LoggerFactory.getLogger(DataToDatabaseEJB.class);

    public UniversityData getFromExport(University u) {
        return getEqualTo(UniversityData_.code, u.getCode());
    }

    public <RT, V> RT getEqualTo(SingularAttribute<RT, V> attr, V val) {
        return getEqualTo(attr, val, null);
    }

    public <RT, V> RT getEqualTo(SingularAttribute<RT, V> attr, V val, RT ifNone) {
        Class<RT> returnClass = attr.getDeclaringType().getJavaType();
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<RT> cq = cb.createQuery(returnClass);
        Root<RT> ud = cq.from(returnClass);
        cq.where(cb.equal(ud.get(attr), val));

        List<RT> r = manager.createQuery(cq).getResultList();

        if (r.isEmpty()) {
            return ifNone;
        } else {
            return r.get(0);
        }
    }

    public <RT, V1, V2> RT getEqualToWithFK(SingularAttribute<RT, V1> attr1, V1 val1, SingularAttribute<RT, V2> attr2, V2 val2) {
        return getEqualToWithFK(attr1, val1, attr2, val2, null);
    }

    public <RT, V1, V2> RT getEqualToWithFK(SingularAttribute<RT, V1> attr1, V1 val1, SingularAttribute<RT, V2> attr2, V2 val2, RT ifNone) {
        Class<RT> returnClass = attr1.getDeclaringType().getJavaType();
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<RT> cq = cb.createQuery(returnClass);
        Root<RT> r = cq.from(returnClass);

        cq.where(cb.and(
                cb.equal(r.get(attr1), val1),
                cb.equal(r.get(attr2), val2)
        ));

        List<RT> res = manager.createQuery(cq).getResultList();

        if (res.isEmpty()) {
            return ifNone;
        } else {
            return res.get(0);
        }
    }

    public <RT, V> List<RT> getAllEqualTo(SingularAttribute<RT, V> attr, V val) {
        Class<RT> returnClass = attr.getDeclaringType().getJavaType();
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<RT> cq = cb.createQuery(returnClass);
        Root<RT> ud = cq.from(returnClass);
        cq.where(cb.equal(ud.get(attr), val));

        return manager.createQuery(cq).getResultList();
    }

    public <RT, V> List<RT> getAllWhere(SingularAttribute<RT, V> attr, DatabaseWhereOp<V> condition, V val) {
        Class<RT> returnClass = attr.getDeclaringType().getJavaType();
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<RT> cq = cb.createQuery(returnClass);
        Root<RT> ud = cq.from(returnClass);
        cq.where(condition.getWhere(cb, ud.get(attr), val));

        return manager.createQuery(cq).getResultList();
    }

    /**
     *
     * @param uni     This is expected to be the university object stored inside the database.
     * @param semCode
     *
     * @return
     */
    public SemesterData getSemesterDataFromCode(UniversityData uni, String semCode) {
        return getEqualToWithFK(SemesterData_.semesterCode, semCode,
                                SemesterData_.universityId, uni);
    }

    public <T> T merge(T genData) {
        try {
            return manager.merge(genData);
        } catch (ConstraintViolationException e) {
            Stores.LOG.error("CVE for " + genData.getClass());
            Stores.LOG.error("        :" + genData);
            for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
                Stores.LOG.error("Constraint Violation: " + cv);
                Stores.LOG.error("\t" + cv.getRootBean());
            }
            throw e;
        }
    }

    public <T> void persist(T genData) {
        try {
            manager.persist(genData);
        } catch (ConstraintViolationException e) {
            Stores.LOG.error("CVE for " + genData);
            
            for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
                Stores.LOG.error("Constraint Violation: " + cv);
                Stores.LOG.error("\t" + cv.getRootBean());
            }
            throw e;
        }
    }

    public void flush() {
        try {
            manager.flush();
        } catch (ConstraintViolationException e) {
            for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
                Stores.LOG.error("Constraint Violation: " + cv);
                Stores.LOG.error("\t" + cv.getRootBean());
            }
            throw e;
        }
    }

}
