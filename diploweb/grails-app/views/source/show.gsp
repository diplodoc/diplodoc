<%@ page import="com.github.dipodoc.diploweb.diplodata.Source" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'source.label', default: 'Source')}" />
		<title><g:message code="default.show.label" args='[ "Source id=${sourceInstance.id}" ]' /></title>
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
			<h1>Source id=${sourceInstance.id}</h1>
			
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			
			<ol class="property-list source">

				<g:if test="${sourceInstance?.id}">
					<li class="fieldcontain">
						<span id="id-label" class="property-label"><g:message code="source.id.label" default="id" /></span>

						<span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${sourceInstance}" field="id"/></span>
					</li>
				</g:if>
			
				<g:if test="${sourceInstance?.name}">
					<li class="fieldcontain">
						<span id="name-label" class="property-label"><g:message code="source.name.label" default="name" /></span>
						
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${sourceInstance}" field="name"/></span>
					</li>
				</g:if>
			
				<g:if test="${sourceInstance?.newPostsFinderModule}">
					<li class="fieldcontain">
						<span id="newPostsFinderModule-label" class="property-label"><g:message code="source.newPostsFinderModule.label" default="new posts finder module" /></span>

						<span class="property-value" aria-labelledby="newPostsFinderModule-label"><g:fieldValue bean="${sourceInstance}" field="newPostsFinderModule"/></span>
					</li>
				</g:if>
			
				<g:if test="${sourceInstance?.rssUrl}">
					<li class="fieldcontain">
						<span id="rssUrl-label" class="property-label"><g:message code="source.rssUrl.label" default="Rss Url" /></span>

						<span class="property-value" aria-labelledby="rssUrl-label"><g:fieldValue bean="${sourceInstance}" field="rssUrl"/></span>
					</li>
				</g:if>
			
			</ol>

			<g:form url="[resource:sourceInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${sourceInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>

	</body>
</html>
