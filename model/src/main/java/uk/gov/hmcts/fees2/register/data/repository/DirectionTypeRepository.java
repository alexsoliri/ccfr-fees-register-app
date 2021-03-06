package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.model.DirectionType;

/**
 *
 * Specifies methods used to obtain and modify DirectionType related information
 * which is stored in the database.
 *
 * @author Tarun Palisetty
 *
 */

@Repository
@Transactional(readOnly = true)
public interface DirectionTypeRepository extends AbstractRepository<DirectionType, String> {

    @Override
    default String getEntityName() {
        return DirectionType.class.getSimpleName();
    }

}
