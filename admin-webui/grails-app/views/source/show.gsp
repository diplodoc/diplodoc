<%@ page import="com.github.dipodoc.webui.admin.domain.data.Source" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'source.label', default: 'Source')}" />
		<title><g:message code="default.show.label" args='[ "Source id=${source.id}" ]' /></title>
	</head>

	<body>
		<a href="#show-source" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
	
		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
	
		<div id="show-source" class="content scaffold-show" role="main">
			<h1>Source id=${source.id}</h1>
			
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			
			<ol class="property-list source">

				<g:if test="${source?.id}">
					<li class="fieldcontain">
						<span id="id-label" class="property-label"><g:message code="source.id.label" default="id" /></span>

						<span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${source}" field="id"/></span>
					</li>
				</g:if>
			
				<g:if test="${source?.name}">
					<li class="fieldcontain">
						<span id="name-label" class="property-label"><g:message code="source.name.label" default="name" /></span>
						
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${source}" field="name"/></span>
					</li>
				</g:if>
			
				<g:if test="${source?.newDocsFinderModule}">
					<li class="fieldcontain">
						<span id="newDocsFinderModule-label" class="property-label"><g:message code="source.newDocsFinderModule.label" default="new docs finder module" /></span>

						<span class="property-value" aria-labelledby="newDocsFinderModule-label"><g:fieldValue bean="${source}" field="newDocsFinderModule"/></span>
					</li>
				</g:if>
			
				<g:if test="${source?.rssUrl}">
					<li class="fieldcontain">
						<span id="rssUrl-label" class="property-label"><g:message code="source.rssUrl.label" default="rss url" /></span>

						<span class="property-value" aria-labelledby="rssUrl-label"><g:fieldValue bean="${source}" field="rssUrl"/></span>
					</li>
				</g:if>
			
			</ol>

			<g:form url="[resource:source, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${source}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>

	</body>
</html>
