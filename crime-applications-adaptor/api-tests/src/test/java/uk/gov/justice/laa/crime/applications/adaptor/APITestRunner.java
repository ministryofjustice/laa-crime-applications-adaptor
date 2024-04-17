package uk.gov.justice.laa.crime.applications.adaptor;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

/**
 * This is the test runner class that allows make the test scenarios discoverable by the test
 * engine.
 */
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
public class APITestRunner {}
