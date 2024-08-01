FROM amazoncorretto:17-alpine
RUN mkdir -p /opt/laa-crime-applications-adaptor/
WORKDIR /opt/laa-crime-applications-adaptor/
COPY ./application/build/libs/crime-applications-adaptor.jar /opt/laa-crime-applications-adaptor/app.jar
RUN addgroup -S appgroup && adduser -u 1001 -S appuser -G appgroup
USER 1001
EXPOSE 8088 8099 8183
# Update the ENTRYPOINT to include JDWP configuration
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8183", "-jar", "app.jar"]
