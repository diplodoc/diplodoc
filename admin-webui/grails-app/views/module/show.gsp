<%@ page import="com.github.dipodoc.webui.admin.domain.orchestration.Module" %>
<%@ page import="com.github.dipodoc.webui.admin.domain.orchestration.ModuleMethod" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'module.label', default: 'Module')}" />
		<title><g:message code="default.show.label" args='[ "Module name=${module.name}" ]' /></title>
	</head>

	<body>
		<a href="#show-module" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

		<div id="show-module" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args='[ "Module name=${module.name}" ]' /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list module">

				<g:if test="${module?.name}">
					<li class="fieldcontain">
						<span id="name-label" class="property-label"><g:message code="module.name.label" default="name" /></span>

						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${module}" field="name"/></span>
					</li>
				</g:if>
			
				<g:if test="${module?.data}">
					<li class="fieldcontain">
						<span id="data-label" class="property-label"><g:message code="module.data.label" default="data items" /></span>

						<div class="property-value" aria-labelledby="data-label">
							<g:each in="${module.data.entrySet()}" var="dataItem">
								<div class="property-value" aria-labelledby="dataItem-label">
									${dataItem.key} = ${dataItem.value}
								</div>
							</g:each>
						</div>
					</li>
				</g:if>

				<li class="fieldcontain">
					<span id="methods-label" class="property-label"><g:message code="module.methods.label" default="methods" /></span>

					<div class="property-value" aria-labelledby="methods-label">
						<g:each in="${moduleMethodsList}" var="moduleMethod">
							<div class="property-value" aria-labelledby="moduleMethod-label">
								${moduleMethod.name}
							</div>
						</g:each>
					</div>
				</li>
			
			</ol>

			<g:form url="[resource:module, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${module}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>

	</body>
</html>
