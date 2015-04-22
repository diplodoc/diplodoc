<%@ page import="com.github.dipodoc.diploweb.domain.diploexec.ProcessRun" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'processRun.label', default: 'ProcessRun')}" />
		<title><g:message code="default.show.label" args='[ "Process run id=${processRunInstance.id}" ]' /></title>
	</head>

	<body>
		<a href="#show-processRun" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

		<div id="show-processRun" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args='[ "Process run id=${processRunInstance.id}" ]' /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list processRun">

				<g:if test="${processRunInstance?.id}">
					<li class="fieldcontain">
						<span id="id-label" class="property-label"><g:message code="processRun.id.label" default="id" /></span>

						<span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${processRunInstance}" field="id"/></span>
					</li>
				</g:if>

				<g:if test="${processRunInstance?.process}">
					<li class="fieldcontain">
						<span id="process-label" class="property-label"><g:message code="processRun.process.label" default="process" /></span>

						<span class="property-value" aria-labelledby="process-label"><g:link controller="process" action="show" id="${processRunInstance?.process?.id}">${processRunInstance?.process?.name}</g:link></span>
					</li>
				</g:if>

				<g:if test="${processRunInstance?.startTime}">
					<li class="fieldcontain">
						<span id="startTime-label" class="property-label"><g:message code="processRun.startTime.label" default="start" /></span>

						<span class="property-value" aria-labelledby="startTime-label"><g:fieldValue bean="${processRunInstance}" field="startTime"/></span>
					</li>
				</g:if>
			
				<g:if test="${processRunInstance?.endTime}">
					<li class="fieldcontain">
						<span id="endTime-label" class="property-label"><g:message code="processRun.endTime.label" default="end" /></span>

						<span class="property-value" aria-labelledby="endTime-label"><g:fieldValue bean="${processRunInstance}" field="endTime"/></span>
					</li>
				</g:if>
			
				<g:if test="${processRunInstance?.exitStatus}">
					<li class="fieldcontain">
						<span id="exitStatus-label" class="property-label"><g:message code="processRun.exitStatus.label" default="exit status" /></span>

						<span class="property-value" aria-labelledby="exitStatus-label"><g:fieldValue bean="${processRunInstance}" field="exitStatus"/></span>
					</li>
				</g:if>
			
				<g:if test="${processRunInstance?.parameters}">
					<li class="fieldcontain">
						<span id="parameters-label" class="property-label"><g:message code="processRun.parameters.label" default="parameters" /></span>

						<g:each in="${processRunInstance.parameters}" var="p">
							<div class="property-value" aria-labelledby="parameters-label">
								${p?.key} = ${p?.value} (of type ${p?.type})
							</div>
						</g:each>
					</li>
				</g:if>
			
			</ol>

		</div>

	</body>
</html>
