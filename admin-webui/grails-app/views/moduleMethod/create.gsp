<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'moduleMethod.label', default: 'Module method')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>

	<body>
		<a href="#create-moduleMethod" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link controller="module" action="show" id="${moduleMethod?.module?.id}"><g:message message="to module" /></g:link></li>
			</ul>
		</div>

		<div id="create-moduleMethod" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<g:hasErrors bean="${moduleMethod}">
				<ul class="errors" role="alert">
					<g:eachError bean="${moduleMethod}" var="error">
						<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
			</g:hasErrors>

			<g:form url="[resource:moduleMethod, action:'save']" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>

	</body>
</html>
