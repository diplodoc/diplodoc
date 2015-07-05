<%@ page import="com.github.dipodoc.webui.admin.domain.orchestration.Process" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'process.label', default: 'Process')}" />
		<title><g:message code="default.show.label" args='["Process name=${process.name}" ]' /></title>
	</head>

	<body>
		<a href="#show-process" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				<li><g:link action="run" resource="${process}"><g:message message="Run" /></g:link></li>
			</ul>
		</div>

		<div id="show-process" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args='["Process name=${process.name}" ]' /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list process">

				<g:if test="${process?.name}">
					<li class="fieldcontain">
						<span id="name-label" class="property-label"><g:message code="process.name.label" default="Name" /></span>

						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${process}" field="name"/></span>
					</li>
				</g:if>

				<g:if test="${process?.definition}">
					<li class="fieldcontain">
						<span id="definition-label" class="property-label"><g:message code="process.definition.label" default="Definition" /></span>

						<span class="property-value" aria-labelledby="definition-label">${process.definition}</span>
					</li>
				</g:if>
			
				<g:if test="${process?.active}">
					<li class="fieldcontain">
						<span id="active-label" class="property-label"><g:message code="process.state.label" default="State" /></span>

						<span class="property-value" aria-labelledby="active-label">${process.active ? 'active' : 'disabled'}</span>
					</li>
				</g:if>
			
				<g:if test="${process?.lastUpdate}">
					<li class="fieldcontain">
						<span id="lastUpdate-label" class="property-label"><g:message code="process.lastUpdate.label" default="Last Update" /></span>

						<span class="property-value" aria-labelledby="lastUpdate-label"><g:fieldValue bean="${process}" field="lastUpdate"/></span>
					</li>
				</g:if>
			
			</ol>

			<g:form url="[resource:process, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${process}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>

	</body>
</html>
