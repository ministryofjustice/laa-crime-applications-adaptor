{{/* vim: set filetype=mustache: */}}
{{/*
Environment variables for service containers
*/}}
{{- define "laa-caa-api-test.env-vars" }}
env:
  - name: MAAT_CD_BASE_URL
    value: {{ .Values.maatApi.baseUrl }}
  - name: MAAT_CD_AUTH_BASE_URL
    value: {{ .Values.maatApi.oauthUrl }}
  - name: MAAT_CD_AUTH_TOKEN_URI
    value: {{ .Values.maatApi.tokenUri }}
  - name: MAAT_CD_AUTH_CAA_CLIENT_ID
    valueFrom:
      secretKeyRef:
        name: maat-api-oauth-client-credentials
        key: MAAT_API_OAUTH_CLIENT_ID
  - name: MAAT_CD_AUTH_CAA_CLIENT_SECRET
    valueFrom:
      secretKeyRef:
        name: maat-api-oauth-client-credentials
        key: MAAT_API_OAUTH_CLIENT_SECRET
  - name: CAM_BASE_URL
    value: {{ .Values.crimeApplyApi.baseUrl }}
  - name: CAM_JWT_ISSUER
    value: {{ .Values.crimeApplyApi.issuer }}
  - name: CAM_JWT_SECRET
    value: {{ .Values.crimeApplyApi.secret }}
  - name: CAA_BASE_URL
    value: {{ .Values.caa.baseUrl }}
  - name: CAA_OAUTH_BASE_URL
    value: {{ .Values.caa.oauthBaseUrl }}
  - name: CAA_OAUTH_TOKEN_URI
    value: {{ .Values.caa.tokenUri }}
  - name: CAA_OAUTH_CLIENT_ID
    valueFrom:
      secretKeyRef:
        name: caa-cognito-client-secret-output
        key: client_id
  - name: CAA_OAUTH_CLIENT_SECRET
    valueFrom:
      secretKeyRef:
        name: caa-cognito-client-secret-output
        key: client_secret
  - name: CHART_NAME
    value: {{ .Release.Name }}
{{- end -}}