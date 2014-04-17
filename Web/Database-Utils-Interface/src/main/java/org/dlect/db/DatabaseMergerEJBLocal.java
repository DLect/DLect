/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dlect.db;

import java.util.Map;
import javax.ejb.Local;
import javax.persistence.metamodel.SingularAttribute;
import org.dlect.internal.data.merge.PartialData;
import org.dlect.internal.data.merge.config.PartialDataConfigurator;

/**
 *
 * @author lee
 */
@Local
public interface DatabaseMergerEJBLocal {

    public <T> T getObjectFor(Map<SingularAttribute<T, ?>, Object> columnValueMap, Class<T> clz);

    public <T> T mergePartialData(PartialDataConfigurator<T> pdc, PartialData<T> lecture);
    
}
