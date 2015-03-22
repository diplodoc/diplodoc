
<%@ page import="com.github.dipodoc.diploweb.diploexec.ProcessRun" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'processRun.label', default: 'ProcessRun')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-processRun" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-processRun" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list processRun">
			
				<g:if test="${processRunInstance?.endTime}">
				<li class="fieldcontain">
					<span id="endTime-label" class="property-label"><g:message code="processRun.endTime.label" default="End Time" /></span>
					
						<span class="property-value" aria-labelledby="endTime-label"><g:fieldValue bean="${processRunInstance}" field="endTime"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${processRunInstance?.exitStatus}">
				<li class="fieldcontain">
					<span id="exitStatus-label" class="property-label"><g:message code="processRun.exitStatus.label" default="Exit Status" /></span>
					
						<span class="property-value" aria-labelledby="exitStatus-label"><g:fieldValue bean="${processRunInstance}" field="exitStatus"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${processRunInstance?.parameters}">
				<li class="fieldcontain">
					<span id="parameters-label" class="property-label"><g:message code="processRun.parameters.label" default="Parameters" /></span>
					
						<g:each in="${processRunInstance.parameters}" var="p">
						<span class="property-value" aria-labelledby="parameters-label"><g:link controller="processRunParameter" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			
				<g:if test="${processRunInstance?.process}">
				<li class="fieldcontain">
					<span id="process-label" class="property-label"><g:message code="processRun.process.label" default="Process" /></span>
					
						<span class="property-value" aria-labelledby="process-label"><g:link controller="process" action="show" id="${processRunInstance?.process?.id}">${processRunInstance?.process?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${processRunInstance?.startTime}">
				<li class="fieldcontain">
					<span id="startTime-label" class="property-label"><g:message code="processRun.startTime.label" default="Start Time" /></span>
					
						<span class="property-value" aria-labelledby="startTime-label"><g:fieldValue bean="${processRunInstance}" field="startTime"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:processRunInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${processRunInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
