package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.fees2.register.data.model.ChannelType;

/**
 *
 * Specifies methods used to obtain and modify ChannelType related information
 * which is stored in the database.
 *
 * @author Tarun Palisetty
 *
 */

@Repository
@Transactional(readOnly = true)
public interface ChannelTypeRepository extends AbstractRepository<ChannelType, String> {

    @Override
    default String getEntityName() {
        return ChannelType.class.getSimpleName();
    }

}
