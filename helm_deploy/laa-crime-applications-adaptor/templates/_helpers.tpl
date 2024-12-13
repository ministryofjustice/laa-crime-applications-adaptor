{{/*
Expand the name of the chart.
*/}}
{{- define "laa-crime-applications-adaptor.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "laa-crime-applications-adaptor.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "laa-crime-applications-adaptor.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "laa-crime-applications-adaptor.labels" -}}
helm.sh/chart: {{ include "laa-crime-applications-adaptor.chart" . }}
{{ include "laa-crime-applications-adaptor.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "laa-crime-applications-adaptor.selectorLabels" -}}
app.kubernetes.io/name: {{ include "laa-crime-applications-adaptor.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "laa-crime-applications-adaptor.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "laa-crime-applications-adaptor.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Create ingress configuration
*/}}
{{- define "laa-crime-applications-adaptor.ingress" -}}
{{- $internalAllowlistSourceRange := (lookup "v1" "Secret" .Release.Namespace "ingress-internal-allowlist-source-range").data.INTERNAL_ALLOWLIST_SOURCE_RANGE | b64dec }}
{{- if $internalAllowlistSourceRange }}
  nginx.ingress.kubernetes.io/whitelist-source-range: {{ $internalAllowlistSourceRange }}
  external-dns.alpha.kubernetes.io/set-identifier: {{ include "laa-crime-applications-adaptor.fullname" . }}-{{ $.Values.ingress.environmentName}}-green
{{- end }}
{{- end }}

{{/*
Create external ingress configuration
*/}}
{{- define "laa-crime-applications-adaptor.externalIngress" -}}
{{- $externalAllowlistSourceRange := (lookup "v1" "Secret" .Release.Namespace "ingress-external-allowlist-source-range").data.EXTERNAL_ALLOWLIST_SOURCE_RANGE | b64dec }}
{{- if $externalAllowlistSourceRange }}
  nginx.ingress.kubernetes.io/whitelist-source-range: {{ $externalAllowlistSourceRange }}
  external-dns.alpha.kubernetes.io/set-identifier: {{ include "laa-crime-applications-adaptor.fullname" . }}-external-{{ $.Values.ingress.environmentName}}-green
{{- end }}
{{- end }}