<%@ page import="com.github.dipodoc.webui.admin.domain.orchestration.ModuleMethodRun" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'moduleMethodRun.label', default: 'Module method run')}" />
		<title><g:message code="default.show.label" args='[ "Module method run id=${moduleMethodRun.id}" ]' /></title>
	</head>

	<body>
		<a href="#show-moduleMethodRun" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

		<div id="show-moduleMethodRun" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args='[ "Module method run id=${moduleMethodRun.id}" ]' /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list moduleMethodRun">

				<g:if test="${moduleMethodRun?.id}">
					<li class="fieldcontain">
						<span id="id-label" class="property-label"><g:message code="moduleMethodRun.id.label" default="id" /></span>

						<span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${moduleMethodRun}" field="id"/></span>
					</li>
				</g:if>

				<g:if test="${moduleMethodRun?.moduleMethod}">
					<li class="fieldcontain">
						<span id="moduleMethod-label" class="property-label"><g:message code="moduleMethodRun.moduleMethod.label" default="module method" /></span>

						<span class="property-value" aria-labelledby="moduleMethod-label">${moduleMethodRun?.moduleMethod?.module?.name}::${moduleMethodRun?.moduleMethod?.name}</span>
					</li>
				</g:if>

				<g:if test="${moduleMethodRun?.parameters}">
					<li class="fieldcontain">
						<span id="parameters-label" class="property-label"><g:message code="moduleMethodRun.parameters.label" default="parameters" /></span>

						<div class="property-value" aria-labelledby="parameters-label">
							<g:each in="${moduleMethodRun.parameters.entrySet()}" var="parametersItem">
								<div class="property-value" aria-labelledby="parametersItem-label">
									${parametersItem.key} = ${parametersItem.value}
								</div>
							</g:each>
						</div>
					</li>
				</g:if>

				<g:if test="${moduleMethodRun?.startTime}">
					<li class="fieldcontain">
						<span id="startTime-label" class="property-label"><g:message code="moduleMethodRun.startTime.label" default="start time" /></span>

						<span class="property-value" aria-labelledby="startTime-label"><g:fieldValue bean="${moduleMethodRun}" field="startTime"/></span>
					</li>
				</g:if>
			
				<g:if test="${moduleMethodRun?.endTime}">
					<li class="fieldcontain">
						<span id="endTime-label" class="property-label"><g:message code="moduleMethodRun.endTime.label" default="end time" /></span>

						<span class="property-value" aria-labelledby="endTime-label"><g:fieldValue bean="${moduleMethodRun}" field="endTime"/></span>
					</li>
				</g:if>
			
				<g:if test="${moduleMethodRun?.metrics}">
					<li class="fieldcontain">
						<span id="metrics-label" class="property-label"><g:message code="moduleMethodRun.metrics.label" default="metrics" /></span>

						<div class="property-value" aria-labelledby="metrics-label">
							<g:each in="${moduleMethodRun.metrics.entrySet()}" var="metricsItem">
								<div class="property-value" aria-labelledby="metricsItem-label">
									${metricsItem.key} = ${metricsItem.value}
								</div>
							</g:each>
						</div>
					</li>
				</g:if>
			
			</ol>
		</div>

	</body>
</html>
