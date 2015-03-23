<%@ page import="com.github.dipodoc.diploweb.diplodata.Post" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'post.label', default: 'Post')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>

	<body>
		<a href="#show-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

		<div id="show-post" class="content scaffold-show" role="main">
            <h1><g:fieldValue bean="${postInstance}" field="title"/></h1>

			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list post">

                <g:if test="${postInstance?.url}">
                    <li class="fieldcontain">
                        <span id="url-label" class="property-label"><g:message code="post.url.label" default="Url" /></span>
                        <span class="property-value" aria-labelledby="url-label"><a href="${postInstance.url}"><g:fieldValue bean="${postInstance}" field="url"/></a></span>
                    </li>
                </g:if>
			
				<g:if test="${postInstance?.id}">
                    <li class="fieldcontain">
                        <span id="id-label" class="property-label"><g:message code="post.id.label" default="id" /></span>
                        <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${postInstance}" field="id"/></span>
                    </li>
				</g:if>

                <g:if test="${postInstance?.source}">
                    <li class="fieldcontain">
                        <span id="source-label" class="property-label"><g:message code="post.source.label" default="Source" /></span>
                        <span class="property-value" aria-labelledby="source-label"><g:link controller="source" action="show" id="${postInstance?.source?.id}">${postInstance?.source?.name}</g:link></span>
                    </li>
                </g:if>

                <g:if test="${postInstance?.description}">
                    <li class="fieldcontain">
                        <span id="description-label" class="property-label"><g:message code="post.description.label" default="Description" /></span>
                        <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${postInstance}" field="description"/></span>
                    </li>
                </g:if>

                <g:if test="${postInstance?.publishTime}">
                    <li class="fieldcontain">
                        <span id="publishTime-label" class="property-label"><g:message code="post.publishTime.label" default="Publish Time" /></span>
                        <span class="property-value" aria-labelledby="publishTime-label"><g:fieldValue bean="${postInstance}" field="publishTime"/></span>
                    </li>
                </g:if>

                <g:if test="${postInstance?.loadTime}">
                    <li class="fieldcontain">
                        <span id="loadTime-label" class="property-label"><g:message code="post.loadTime.label" default="Load Time" /></span>
                        <span class="property-value" aria-labelledby="loadTime-label"><g:fieldValue bean="${postInstance}" field="loadTime"/></span>
                    </li>
                </g:if>

                <g:if test="${postInstance?.train_topics}">
                    <li class="fieldcontain">
                        <span id="trainTopics-label" class="property-label"><g:message code="post.trainTopics.label" default="Train topics" /></span>
                        <g:each in="${postInstance.train_topics}" var="p">
                            <span class="property-value" aria-labelledby="trainTopics-label"><g:link controller="topic" action="show" id="${p.id}"><g:fieldValue bean="${p}" field="label"/></g:link></span>
                        </g:each>
                    </li>
                </g:if>

                <g:if test="${postInstance?.meaningText}">
                    <li class="fieldcontain">
                        <span id="meaningText-label" class="property-label"><g:message code="post.meaningText.label" default="Meaning text" /></span>
                        <span class="property-value" aria-labelledby="meaningText-label"><g:fieldValue bean="${postInstance}" field="meaningText"/></span>
                    </li>
                </g:if>
                
			</ol>

			<g:form url="[resource:postInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${postInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>

		</div>
	</body>
</html>
