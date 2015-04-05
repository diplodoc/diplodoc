<%@ page import="com.github.dipodoc.diploweb.diploexec.ModuleMethod" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'moduleMethod.label', default: 'ModuleMethod')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>

	<body>
		<a href="#edit-moduleMethod" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link controller="module" action="show" id="${moduleMethodInstance?.module?.id}"><g:message message="to module" /></g:link></li>
			</ul>
		</div>

		<div id="edit-moduleMethod" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${moduleMethodInstance}">
				<ul class="errors" role="alert">
					<g:eachError bean="${moduleMethodInstance}" var="error">
						<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
			</g:hasErrors>

			<g:form url="[resource:moduleMethodInstance, action:'update']" method="PUT" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>

	</body>
</html>
