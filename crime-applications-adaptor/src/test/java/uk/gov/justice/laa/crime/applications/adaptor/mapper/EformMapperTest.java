package uk.gov.justice.laa.crime.applications.adaptor.mapper;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.justice.laa.crime.applications.adaptor.model.MaatApplication;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.FileUtils;
import uk.gov.justice.laa.crime.applications.adaptor.testutils.JsonUtils;

class EformMapperTest {

    private EformMapper eformMapper;

    @BeforeEach
    void setUp() {
        eformMapper = new EformMapper();
    }

    @Test
    void mapToMaatApplication() throws JSONException {
        String eformXML = FileUtils.readFileToString("data/eforms/EFORMS_STAGING_4716.xml");

        MaatApplication maatApplication = eformMapper.mapToMaatApplication(eformXML);

        String actualMaatApplicationJson = JsonUtils.objectToJson(maatApplication);

        JSONCompare.compareJSON("hjgdhjgd", actualMaatApplicationJson, JSONCompareMode.STRICT);
    }
}