
<%@ page import="com.github.dipodoc.diploweb.diploexec.ProcessRunParameter" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'processRunParameter.label', default: 'ProcessRunParameter')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-processRunParameter" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-processRunParameter" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list processRunParameter">
			
				<g:if test="${processRunParameterInstance?.key}">
				<li class="fieldcontain">
					<span id="key-label" class="property-label"><g:message code="processRunParameter.key.label" default="Key" /></span>
					
						<span class="property-value" aria-labelledby="key-label"><g:fieldValue bean="${processRunParameterInstance}" field="key"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${processRunParameterInstance?.processRun}">
				<li class="fieldcontain">
					<span id="processRun-label" class="property-label"><g:message code="processRunParameter.processRun.label" default="Process Run" /></span>
					
						<span class="property-value" aria-labelledby="processRun-label"><g:link controller="processRun" action="show" id="${processRunParameterInstance?.processRun?.id}">${processRunParameterInstance?.processRun?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${processRunParameterInstance?.type}">
				<li class="fieldcontain">
					<span id="type-label" class="property-label"><g:message code="processRunParameter.type.label" default="Type" /></span>
					
						<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${processRunParameterInstance}" field="type"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${processRunParameterInstance?.value}">
				<li class="fieldcontain">
					<span id="value-label" class="property-label"><g:message code="processRunParameter.value.label" default="Value" /></span>
					
						<span class="property-value" aria-labelledby="value-label"><g:fieldValue bean="${processRunParameterInstance}" field="value"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:processRunParameterInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${processRunParameterInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
