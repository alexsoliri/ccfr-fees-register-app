package uk.gov.hmcts.fees2.register.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.FeeController;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeLoaderJsonMapper;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by tarun on 10/11/2017.
 */

@Component
public class FeeLoader implements ApplicationRunner {
    private static final Logger LOG = LoggerFactory.getLogger(FeeLoader.class);

    @Value("classpath:${fees.loader.json}")
    private String feesJsonInputFile;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FeeController feeController;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            FeeLoaderJsonMapper feeLoaderMapper = loadFromResource(feesJsonInputFile);

            if (feeLoaderMapper.getFixedFees().size() > 0) {
                List<CreateFixedFeeDto> fixedFees = feeLoaderMapper.getFixedFees();
                fixedFees.forEach(f -> {
                    try {
                        if (f.getUnspecifiedClaimAmount() == null) {
                            f.setUnspecifiedClaimAmount(false);
                        }
                        feeController.createFixedFee(f, null);
                        LOG.info("Fixed fee with code " +f.getCode()+ " inserted into database.");
                    } catch (BadRequestException be) {
                        LOG.info("Fixed fee with code " +f.getCode()+ " already in use.");
                    }
                });
            }

            if (feeLoaderMapper.getRangedFees().size() > 0) {
                List<CreateRangedFeeDto> rangedFees = feeLoaderMapper.getRangedFees();
                rangedFees.forEach(r -> {
                    try {
                        feeController.createRangedFee(r, null);
                        LOG.info("Ranged fee with code " +r.getCode()+ " inserted into database.");
                    } catch (BadRequestException be) {
                        LOG.info("Ranged fee with code " +r.getCode()+ " already in use.");
                    }
                });
            }


        } catch (IOException  | NullPointerException ex) {
            LOG.error("Error is loading cmc fee json loader");
            throw new Exception("Error in loading fee into the database.", ex);
        }


    }

    private FeeLoaderJsonMapper loadFromResource(String location) throws IOException {
        InputStream fileAsInputStream = resourceLoader.getResource(location).getInputStream();
        return objectMapper.readValue(fileAsInputStream, FeeLoaderJsonMapper.class);
    }

}