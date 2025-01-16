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
    {{- $mockSecret := lookup "v1" "Secret" .Release.Namespace "crime-apply-mock-api-auth-secret" }}
      {{- if $mockSecret.data }}
      - name: CAM_JWT_SECRET
        valueFrom:
          secretKeyRef:
            name: crime-apply-mock-api-auth-secret
            key: CRIME_APPLY_MOCK_API_AUTH_SECRET
      {{- else }}
      - name: CAM_JWT_SECRET
        valueFrom:
          secretKeyRef:
            name: datastore-api-auth-secret
            key: secret
      {{- end -}}
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
        key: maat_client_id
  - name: CAA_OAUTH_CLIENT_SECRET
    valueFrom:
      secretKeyRef:
        name: caa-cognito-client-secret-output
        key: maat_client_secret
  - name: CHART_NAME
    value: {{ .Release.Name }}
{{- end -}}