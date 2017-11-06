package uk.gov.hmcts.fees2.register.api.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.BaseTest;
import uk.gov.hmcts.fees2.register.api.controllers.advice.exception.BadRequestException;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.ChannelTypeService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static org.junit.Assert.*;


/**
 * Created by tarun on 02/11/2017.
 */

public class Fee2CrudComponentTest extends BaseTest {

    @Autowired
    private FeeService feeService;

    @Autowired
    private FeeDtoMapper feeDtoMapper;

    private RangedFeeDto rangedFeeDto;

    @Autowired
    private ChannelTypeService channelTypeService;


    /**
     *
     */
    @Test
    public void createRangedFeeTest() {
        rangedFeeDto = getRangedFeeDto();
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto));

        assertNotNull(savedFee);
    }

    @Test(expected = BadRequestException.class)
    public void duplicateFeeCreationTest() {
        rangedFeeDto = getRangedFeeDto();
        feeService.save(feeDtoMapper.toFee(rangedFeeDto));
    }

    @Test
    public void createRangedFeeWithAllReferenceDataTest() {
        rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 3000, "X0011", FeeVersionStatus.approved);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto));

        RangedFeeDto rangedFeeDto = feeDtoMapper.toFeeDto(savedFee);

        assertEquals(rangedFeeDto.getCode(), "X0011");
        FeeVersionDto feeVersionDtoResult = rangedFeeDto.getFeeVersionDtos().stream().filter(v -> v.getStatus().equals(FeeVersionStatus.approved)).findAny().orElse(null);
        assertNotNull(feeVersionDtoResult);
        assertEquals(feeVersionDtoResult.getStatus(), FeeVersionStatus.approved);
        assertEquals(feeVersionDtoResult.getDescription(), "First version description");

        // could not map entity to Dto
        //assertEquals(feeVersionDtoResult.getFlatAmount(), new BigDecimal(2500));
    }

    @Test
    @Transactional
    public void ReadRangedFeeWithAllReferenceDataTest() {
        // Insert a new ranged fee
        rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2000, "X0012", FeeVersionStatus.approved);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto));

        Fee fee = feeService.get("X0012");

        RangedFeeDto rangedFeeDto = feeDtoMapper.toFeeDto(fee);
        assertEquals(rangedFeeDto.getCode(), "X0012");
        FeeVersionDto feeVersionDtoResult = rangedFeeDto.getFeeVersionDtos().stream().filter(v -> v.getStatus().equals(FeeVersionStatus.approved)).findAny().orElse(null);
        assertNotNull(feeVersionDtoResult);
        assertEquals(feeVersionDtoResult.getStatus(), FeeVersionStatus.approved);
        assertEquals(feeVersionDtoResult.getDescription(), "First version description");
    }


    @Test
    public void createDraftFeeAndApproveTheFeeTest() {
        // Insert a new ranged fee
        rangedFeeDto = getRangedFeeDtoWithReferenceData(1, 2999, "X0013", FeeVersionStatus.draft);
        Fee savedFee = feeService.save(feeDtoMapper.toFee(rangedFeeDto));

        Fee fee = feeService.get("X0013");

        boolean result = feeService.approve(fee.getCode(), 1);
        assertTrue(result);
    }


}
