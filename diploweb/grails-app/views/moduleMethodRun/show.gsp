<%@ page import="com.github.dipodoc.diploweb.diploexec.ModuleMethodRun" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'moduleMethodRun.label', default: 'ModuleMethodRun')}" />
		<title><g:message code="default.show.label" args='[ "Module method run id=${moduleMethodRunInstance.id}" ]' /></title>
	</head>

	<body>
		<a href="#show-moduleMethodRun" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

		<div id="show-moduleMethodRun" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args='[ "Module method run id=${moduleMethodRunInstance.id}" ]' /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list moduleMethodRun">

				<g:if test="${moduleMethodRunInstance?.id}">
					<li class="fieldcontain">
						<span id="id-label" class="property-label"><g:message code="moduleMethodRun.id.label" default="id" /></span>

						<span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${moduleMethodRunInstance}" field="id"/></span>
					</li>
				</g:if>

				<g:if test="${moduleMethodRunInstance?.moduleMethod}">
					<li class="fieldcontain">
						<span id="moduleMethod-label" class="property-label"><g:message code="moduleMethodRun.moduleMethod.label" default="Module Method" /></span>

						<span class="property-value" aria-labelledby="moduleMethod-label">${moduleMethodRunInstance?.moduleMethod?.name}</span>
					</li>
				</g:if>

				<g:if test="${moduleMethodRunInstance?.startTime}">
					<li class="fieldcontain">
						<span id="startTime-label" class="property-label"><g:message code="moduleMethodRun.startTime.label" default="Start Time" /></span>

						<span class="property-value" aria-labelledby="startTime-label"><g:fieldValue bean="${moduleMethodRunInstance}" field="startTime"/></span>
					</li>
				</g:if>
			
				<g:if test="${moduleMethodRunInstance?.endTime}">
					<li class="fieldcontain">
						<span id="endTime-label" class="property-label"><g:message code="moduleMethodRun.endTime.label" default="End Time" /></span>

						<span class="property-value" aria-labelledby="endTime-label"><g:fieldValue bean="${moduleMethodRunInstance}" field="endTime"/></span>
					</li>
				</g:if>
			
				<g:if test="${moduleMethodRunInstance?.metrics}">
					<li class="fieldcontain">
						<span id="metrics-label" class="property-label"><g:message code="moduleMethodRun.metrics.label" default="Metrics" /></span>

						<div class="property-value" aria-labelledby="metrics-label">
							<g:each in="${moduleMethodRunInstance.metrics.entrySet()}" var="metricsItem">
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
