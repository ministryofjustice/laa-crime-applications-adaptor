package uk.gov.justice.laa.crime.applications.adaptor.util;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CrimeApplicationUtilTest {

    @Test
    void testJWSTokenGenerationForCrimeApplyApi(){
        assertThat(CrimeApplicationHttpUtil.getHttpHeaders(
                  "TESTVjFtRXlpQGNDJGZIKU5MQUVtd2NPY0FSLVN6JGg=",
                "maat-adapter")).isNotEmpty();
    }


}
