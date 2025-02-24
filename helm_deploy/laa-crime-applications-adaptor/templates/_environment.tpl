{{/* vim: set filetype=mustache: */}}
{{/*
Environment variables for service containers
*/}}
{{- define "laa-crime-applications-adaptor.env-vars" }}
env:
  - name: AWS_REGION
    value: {{ .Values.aws_region }}
  - name: SENTRY_DSN
    valueFrom:
        secretKeyRef:
            name: sentry-dsn
            key: SENTRY_DSN
  - name: SENTRY_SAMPLE_RATE
    value: {{ .Values.sentry.sampleRate | quote }}
  - name: SENTRY_ENV
    value: {{ .Values.java.host_env }}
  - name: MAAT_API_BASE_URL
    value: {{ .Values.maatApi.baseUrl }}
  - name: MAAT_API_OAUTH_URL
    value: {{ .Values.maatApi.oauthUrl }}
  - name: MAAT_API_OAUTH_CLIENT_ID
    valueFrom:
      secretKeyRef:
        name: maat-api-oauth-client-credentials
        key: MAAT_API_OAUTH_CLIENT_ID
  - name: MAAT_API_OAUTH_CLIENT_SECRET
    valueFrom:
      secretKeyRef:
        name: maat-api-oauth-client-credentials
        key: MAAT_API_OAUTH_CLIENT_SECRET
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
  {{- $mockSecret := lookup "v1" "Secret" .Release.Namespace "crime-apply-mock-api-auth-secret" }}
  {{- if $mockSecret.data }}
  - name: CRIME_APPLY_API_AUTH_SECRET
    valueFrom:
      secretKeyRef:
        name: crime-apply-mock-api-auth-secret
        key: CRIME_APPLY_MOCK_API_AUTH_SECRET
  {{- else }}
  - name: CRIME_APPLY_API_AUTH_SECRET
    valueFrom:
      secretKeyRef:
        name: datastore-api-auth-secret
        key: secret
  {{- end }}
{{- end -}}