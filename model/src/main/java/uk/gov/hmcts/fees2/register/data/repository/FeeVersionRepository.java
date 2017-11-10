package uk.gov.hmcts.fees2.register.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;

@Repository
public interface FeeVersionRepository extends JpaRepository<FeeVersion, Long> {
    FeeVersion findByFeeAndVersion(Fee fee, Integer version);
}
