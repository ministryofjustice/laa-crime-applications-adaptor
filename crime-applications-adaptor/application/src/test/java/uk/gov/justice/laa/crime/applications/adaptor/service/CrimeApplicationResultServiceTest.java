package uk.gov.justice.laa.crime.applications.adaptor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import uk.gov.justice.laa.crime.applications.adaptor.client.MaatCourtDataApiClient;
import uk.gov.justice.laa.crime.applications.adaptor.mapper.crimeapply.CrimeApplicationResultMapper;
import uk.gov.justice.laa.crime.applications.adaptor.model.RepOrderState;
import uk.gov.justice.laa.crime.model.common.atis.CrimeApplicationResult;
import uk.gov.justice.laa.crime.model.common.atis.CrimeApplicationResult.CaseType;

@ExtendWith(MockitoExtension.class)
class CrimeApplicationResultServiceTest {

  @InjectMocks private CrimeApplicationResultService crimeApplicationResultService;
  @Mock private MaatCourtDataApiClient maatCourtDataApiClient;
  @Mock private CrimeApplicationResultMapper crimeApplicationResultMapper;

  @Test
  void shouldReturnCrimeApplicationResults_AfterInvokingMAATCourtDataAPI() {

    RepOrderState repOrderState =
        RepOrderState.builder().usn(123455).meansInitResult("PASS").build();
    CrimeApplicationResult expected = new CrimeApplicationResult();
    expected.setUsn(123455);
    expected.setMeansResult("PASS");

    when(maatCourtDataApiClient.retrieveCrimeApplicationResultsByUsn(123455))
        .thenReturn(repOrderState);
    when(crimeApplicationResultMapper.map(repOrderState)).thenReturn(expected);

    CrimeApplicationResult actual = crimeApplicationResultService.getCrimeApplicationResult(123455);

    verify(maatCourtDataApiClient, times(1)).retrieveCrimeApplicationResultsByUsn(123455);
    assertEquals(expected.getUsn(), actual.getUsn());
    assertEquals(expected.getMeansResult(), actual.getMeansResult());
  }

  @Test
  void shouldThrowWebClientRequestException_givenNetworkUnreachableOnInvokingMaatCourtDataAPI() {
    when(maatCourtDataApiClient.retrieveCrimeApplicationResultsByUsn(123455))
        .thenThrow(WebClientRequestException.class);
    assertThrows(
        WebClientRequestException.class,
        () -> crimeApplicationResultService.getCrimeApplicationResult(123455));
  }

  @Test
  void shouldReturnCrimeApplicationResultsByRepId_AfterInvokingMAATCourtDataAPI() {

    RepOrderState repOrderState =
        RepOrderState.builder()
            .maatRef(123456)
            .usn(123455)
            .meansInitResult("FAIL")
            .meansInitStatus("COMPLETE")
            .meansFullResult("PASS")
            .meansFullStatus("COMPLETE")
            .build();

    CrimeApplicationResult expected = new CrimeApplicationResult();
    expected.setMaatRef(123456);
    expected.setUsn(123455);
    expected.setMeansResult("PASS");


    when(maatCourtDataApiClient.retrieveCrimeApplicationResultsByRepId(123456))
        .thenReturn(repOrderState);
    when(crimeApplicationResultMapper.map(repOrderState)).thenReturn(expected);

    CrimeApplicationResult actual = crimeApplicationResultService.getCrimeApplicationResultByRepId(123456);

    verify(maatCourtDataApiClient, times(1)).retrieveCrimeApplicationResultsByRepId(123456);
    assertEquals(expected.getMaatRef(), actual.getMaatRef());
    assertEquals(expected.getMeansResult(), actual.getMeansResult());
  }
}
