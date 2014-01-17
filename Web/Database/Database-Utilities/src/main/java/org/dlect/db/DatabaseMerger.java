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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.dlect.annotate.RecurseForMerge;
import org.dlect.annotate.Unique;
import org.dlect.internal.data.merge.PartialData;
import org.dlect.internal.data.merge.config.PartialDataConfigurator;

/**
 *
 * @author lee
 */
@Stateless
public class DatabaseMerger {

    @PersistenceContext(name = "DaaS-Uni")
    private EntityManager manager;

    @EJB
    private DataToDatabaseEJB helper;
//
    
    public <T> T mergePartialData(PartialDataConfigurator<T> pdc, PartialData<T> lecture) {
        Map<SingularAttribute<T, ?>, Object> ldKvm = lecture.fillUniqueData(pdc.getForeignKey());
        T ld = getObjectFor(ldKvm, pdc.getObjectClass());
        if (ld == null) {
            ld = pdc.getObject();
        }
        lecture.assignTo(ld);
        ld = helper.merge(ld);
        return ld;
    }

    public <T> T getObjectFor(Map<SingularAttribute<T, ?>, Object> columnValueMap, Class<T> clz) {

        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clz);
        Root<T> ud = cq.from(clz);

        List<Predicate> predicates = Lists.newArrayList();

        for (Entry<SingularAttribute<T, ?>, Object> entry : columnValueMap.entrySet()) {
            predicates.add(cb.equal(ud.get(entry.getKey()), entry.getValue()));
        }

        cq.where(cb.and(
                predicates.toArray(new Predicate[predicates.size()])
        ));

        List<T> r = manager.createQuery(cq).getResultList();
        return r.isEmpty() ? null : r.get(0);
    }

}
