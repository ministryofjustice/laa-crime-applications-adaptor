{{/* vim: set filetype=mustache: */}}
{{/*
Environment variables for service containers
*/}}
{{- define "laa-crime-applications-adaptor.env-vars" }}
env:
  - name: AWS_REGION
    value: {{ .Values.aws_region }}
  - name: SENTRY_DSN
    value: {{ .Values.sentry_dsn }}
  - name: SENTRY_ENV
    value: {{ .Values.java.host_env }}
  - name: MAAT_API_BASE_URL
    value: {{ .Values.maatApi.baseUrl }}
  - name: MAAT_API_OAUTH_URL
    value: {{ .Values.maatApi.oauthUrl }}
  - name: MAAT_API_OAUTH_CLIENT_ID
    value: {{ .Values.maatApi.clientId }}
  - name: MAAT_API_OAUTH_CLIENT_SECRET
    value: {{ .Values.maatApi.clientSecret }}
  - name: MAAT_API_REGISTRATION_ID
    value: {{ .Values.maatApi.registrationId }}
  - name: MAAT_API_OAUTH_SCOPE
    value: {{ .Values.maatApi.oauthScope }}
  - name: CRIME_APPLY_API_URL
    value: {{ .Values.crimeApplyApi.baseUrl }}
  - name: CRIME_APPLY_AUTH_KEY_ISSUER
    value: {{ .Values.crimeApplyApi.issuer }}
  - name: CRIME_APPLICATION_ADAPTOR_RESOURCE_SERVER_ISSUER_URI
    value: {{ .Values.crimeApplicationAdaptor.issuerUri }}
  - name: CRIME_APPLY_API_AUTH_SECRET
    valueFrom:
      secretKeyRef:
        name: datastore-api-auth-secret
        key: secret
{{- end -}}