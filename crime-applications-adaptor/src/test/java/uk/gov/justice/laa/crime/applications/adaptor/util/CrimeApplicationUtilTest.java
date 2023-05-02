package uk.gov.justice.laa.crime.applications.adaptor.util;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CrimeApplicationUtilTest {

    @Test
    void testJWSTokenGenerationForCrimeApplyApi(){
        assertThat(CrimeApplicationUtil.getHttpHeaders()).isNotEmpty();
    }


}
