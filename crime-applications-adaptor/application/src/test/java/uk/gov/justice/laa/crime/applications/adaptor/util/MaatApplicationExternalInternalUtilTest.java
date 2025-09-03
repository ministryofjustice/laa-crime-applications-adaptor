package uk.gov.justice.laa.crime.applications.adaptor.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MaatApplicationExternalInternalUtilTest {

  @Test
  void testJWSTokenGenerationForCrimeApplyApi() {
    assertThat(
            CrimeApplicationHttpUtil.getHttpHeaders(
                "TESTVALUENOTREALxGZIKU5MQUVtd2NPY0FSLVN6J66=", "maat-adapter"))
        .isNotEmpty();
  }
}
