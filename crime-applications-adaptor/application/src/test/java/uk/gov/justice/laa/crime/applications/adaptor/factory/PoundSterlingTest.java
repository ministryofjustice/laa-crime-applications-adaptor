package uk.gov.justice.laa.crime.applications.adaptor.factory;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PoundSterlingTest {
    @ParameterizedTest
    @MethodSource("pennyValues")
    void shouldSuccessfullyConvertPenniesToPounds(Number pennies, BigDecimal expectedValue) {
        BigDecimal actualValue = PoundSterling.ofPennies(pennies).toPounds();

        assertEquals(actualValue, expectedValue);
    }

    private static Stream<Arguments> pennyValues() {
        return Stream.of(
                Arguments.of(0, new BigDecimal("0.00")),
                Arguments.of(12500, new BigDecimal("125.00")),
                Arguments.of(1000, new BigDecimal("10.00")),
                Arguments.of(13550, new BigDecimal("135.50")),
                Arguments.of(100, new BigDecimal("1.00")),
                Arguments.of(175, new BigDecimal("1.75")),
                Arguments.of(200000, new BigDecimal("2000.00")),
                Arguments.of(3000000, new BigDecimal("30000.00"))
        );
    }
}
