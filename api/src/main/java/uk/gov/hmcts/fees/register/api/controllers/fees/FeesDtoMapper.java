package uk.gov.hmcts.fees.register.api.controllers.fees;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.contract.FixedFeeDto;
import uk.gov.hmcts.fees.register.api.contract.PercentageFeeDto;
import uk.gov.hmcts.fees.register.api.model.Fee;
import uk.gov.hmcts.fees.register.api.model.FixedFee;
import uk.gov.hmcts.fees.register.api.model.PercentageFee;

@Component
public class FeesDtoMapper {

    public FeeDto toFeeDto(Fee fee) {
        if (fee instanceof FixedFee) {
            return new FixedFeeDto(fee.getCode(), fee.getDescription(), ((FixedFee) fee).getAmount());
        } else if (fee instanceof PercentageFee) {
            return new PercentageFeeDto(fee.getCode(), fee.getDescription(), ((PercentageFee) fee).getPercentage());
        } else {
            throw new IllegalArgumentException("Unknown fee type: " + fee.getClass());
        }
    }

    public Fee toFee(String code, FeeDto dto) {
        if (dto instanceof FixedFeeDto) {
            return new FixedFee(null, code, dto.getDescription(), ((FixedFeeDto) dto).getAmount());
        } else if (dto instanceof PercentageFeeDto) {
            return new PercentageFee(null, code, dto.getDescription(), ((PercentageFeeDto) dto).getPercentage());
        } else {
            throw new IllegalArgumentException("Unknown fee dto type: " + dto.getClass());
        }
    }

}