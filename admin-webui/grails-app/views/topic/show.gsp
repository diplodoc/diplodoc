<%@ page import="com.github.dipodoc.webui.admin.domain.data.Topic" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'topic.label', default: 'Topic')}" />
		<title><g:message code="default.show.label" args='[ "Topic id=${topicInstance.id}" ]' /></title>
	</head>

	<body>
		<a href="#show-topic" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

		<div id="show-topic" class="content scaffold-show" role="main">
			<h1>Topic id=${topicInstance.id}</h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list topic">

				<g:if test="${topicInstance?.id}">
					<li class="fieldcontain">
						<span id="id-label" class="property-label"><g:message code="topic.id.label" default="id" /></span>

						<span class="property-value" aria-idledby="id-label"><g:fieldValue bean="${topicInstance}" field="id"/></span>
					</li>
				</g:if>

				<g:if test="${topicInstance?.label}">
					<li class="fieldcontain">
						<span id="label-label" class="property-label"><g:message code="topic.label.label" default="label" /></span>

						<span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${topicInstance}" field="label"/></span>
					</li>
				</g:if>
			
				<g:if test="${topicInstance?.parent}">
					<li class="fieldcontain">
						<span id="parent-label" class="property-label"><g:message code="topic.hierarchy.label" default="hierarchy" /></span>

						<diplo:topics topics="${topicInstance}" divClass="property-value" />
					</li>
				</g:if>
			
			</ol>

			<g:form url="[resource:topicInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${topicInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>

	</body>
</html>
