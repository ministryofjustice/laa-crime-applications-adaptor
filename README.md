# laa-crime-applications-adaptor

This is a Java 17 based Spring Boot application hosted on [MOJ Cloud Platform](https://user-guide.cloud-platform.service.justice.gov.uk/documentation/concepts/about-the-cloud-platform.html).

[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

### Decrypting docker-compose.override.yml

The `docker-compose.override.yml` is encrypted using [git-crypt](https://github.com/AGWA/git-crypt).

To run the app locally you need to be able to decrypt this file.

You will first need to create a GPG key. See [Create a GPG Key](https://docs.publishing.service.gov.uk/manual/create-a-gpg-key.html) for details on how to do this with `GPGTools` (GUI) or `gpg` (command line).
You can install either from a terminal or just download the UI version.

```
brew update
brew install gpg
brew install git-crypt
```

Once you have done this, a team member who already has access can add your key by running `git-crypt add-gpg-user USER_ID`\* and creating a pull request to this repo.

Once this has been merged you can decrypt your local copy of the repository by running `git-crypt unlock`.

\*`USER_ID` can be your key ID, a full fingerprint, an email address, or anything else that uniquely identifies a public key to GPG (see "HOW TO SPECIFY A USER ID" in the gpg man page).
The apps should then startup cleanly if you run

### Application Set up

Clone Repository

```sh
git clone git@github.com:ministryofjustice/laa-crime-applications-adaptor.git

cd crime-applications-adaptor
```

Make sure all tests are passed by running following ‘gradle’ Command

```sh
./gradlew clean test
```

When running the application locally you will likely want to disable OAuth request authorisation, this can be done by replacing the `SecurityFilterChain` implementation with:

```java
http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest()
.permitAll());

return http.build();
```

You will need to build the artifacts for the source code, using `gradle`.

```sh
./gradlew clean build
```

```sh
docker-compose build
docker-compose up
```

`laa-crime-applications-adaptor` application will be running on http://localhost:8088

### Metrics and Health Check endpoints

There are a number of out of the box endpoints provided by Spring Boot for verifying application health and various metrics, some of which are enabled and accessible.

These endpoints include:

- http://localhost:8099/actuator
- http://localhost:8099/actuator/prometheus
- http://localhost:8099/actuator/health
- http://localhost:8099/actuator/info
- http://localhost:8099/actuator/metrics
- http://localhost:8088/api-docs (generated by `springdoc-openapi-starter-webmvc-ui` with URL path defined in `application.yaml` by `springdoc.api-docs.path: /api-docs`)

For a complete list of all out of the box actuator endpoints see [Spring Boot 3.1.5 - Actuator](https://docs.spring.io/spring-boot/docs/3.1.5/reference/html/actuator.html#actuator).

### Functional API Tests

A test module `functionalApiTest` has been added to hold API tests written use Serenity, Cucumber
and RestAssured. There tests
are designed to be run against a deployed version of the application in DEV or TEST and will not be
run automatically as part of
the gradle build. A separate gradle task , `functionalApiTest`, has been created in order to run
these tests in isolation.

#### Test Configuration

The functional tests use Apache Commons Configuration2 to read the `config.properties` file and
provide environment
variable substitution. This is designed for use on the CI/CD pipeline where secrets will be injected
by environment variable.
When running locally create a copy of the properties file and name is `local.config.properties` and
populate it with the required values. This file is included in the `.gitignore` to avoid accidental
commits.
The trigger for using the `config.properties` is the presence of the `CHART_NAME` environment
variable which indicates
that the tests is being deployed and run via helm.

#### Running the API Tests

The API tests can be run through IntelliJ or using the following gradle command.

`./gradlew clean functionalApiTest`

This command will compile the main java sources in order for the tests to re-use the schema files.
