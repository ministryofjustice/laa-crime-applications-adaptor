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
  - name: CRIME_APPLY_API_URL
    value: {{ .Values.crimeApplyApi.baseUrl }}
  - name: CRIME_APPLY_AUTH_KEY_ISSUER
    value: {{ .Values.crimeApplyApi.issuer }}
  - name: CRIME_APPLY_API_AUTH_SECRET
    value: {{ .Values.crimeApplyApi.clientSecret }}

{{- end -}}