<%@ page import="com.github.dipodoc.diploweb.domain.diplodata.Doc" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'doc.label', default: 'Doc')}" />
		<title><g:message code="default.show.label" args='[ "Doc id=${docInstance.id}" ]' /></title>
	</head>

	<body>
		<a href="#show-doc" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
                <g:render template="/navigation/base-navigation"/>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

		<div id="show-doc" class="content scaffold-show" role="main">
            <h1>Doc id=${docInstance.id}</h1>

			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

			<ol class="property-list doc">

                <g:if test="${docInstance?.id}">
                    <li class="fieldcontain">
                        <span id="id-label" class="property-label"><g:message code="doc.id.label" default="id" /></span>
                        <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${docInstance}" field="id"/></span>
                    </li>
                </g:if>

                <g:if test="${docInstance?.uri}">
                    <li class="fieldcontain">
                        <span id="uri-label" class="property-label"><g:message code="doc.uri.label" default="uri" /></span>
                        <span class="property-value" aria-labelledby="uri-label"><a href="${docInstance.uri}" target="_blank"><g:fieldValue bean="${docInstance}" field="uri"/></a></span>
                    </li>
                </g:if>

                <g:if test="${docInstance?.title}">
                    <li class="fieldcontain">
                        <span id="title-label" class="property-label"><g:message code="doc.title.label" default="Title" /></span>
                        <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${docInstance}" field="title"/></span>
                    </li>
                </g:if>

                <g:if test="${docInstance?.source}">
                    <li class="fieldcontain">
                        <span id="source-label" class="property-label"><g:message code="doc.source.label" default="Source" /></span>
                        <span class="property-value" aria-labelledby="source-label"><g:link controller="source" action="show" id="${docInstance?.source?.id}">${docInstance?.source?.name}</g:link></span>
                    </li>
                </g:if>

                <g:if test="${docInstance?.description}">
                    <li class="fieldcontain">
                        <span id="description-label" class="property-label"><g:message code="doc.description.label" default="Description" /></span>
                        <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${docInstance}" field="description"/></span>
                    </li>
                </g:if>

                <g:if test="${docInstance?.publishTime}">
                    <li class="fieldcontain">
                        <span id="publishTime-label" class="property-label"><g:message code="doc.publishTime.label" default="Publish time" /></span>
                        <span class="property-value" aria-labelledby="publishTime-label"><g:fieldValue bean="${docInstance}" field="publishTime"/></span>
                    </li>
                </g:if>

                <g:if test="${docInstance?.loadTime}">
                    <li class="fieldcontain">
                        <span id="loadTime-label" class="property-label"><g:message code="doc.loadTime.label" default="Load time" /></span>
                        <span class="property-value" aria-labelledby="loadTime-label"><g:fieldValue bean="${docInstance}" field="loadTime"/></span>
                    </li>
                </g:if>

                <g:if test="${docInstance?.train_topics}">
                    <li class="fieldcontain">
                        <span id="train_topics-label" class="property-label"><g:message code="doc.train_topics.label" default="Train topics" /></span>
                        <diplo:topics topics="${docInstance.train_topics}" divClass="property-value" />
                    </li>
                </g:if>

                <g:if test="${docInstance?.predicted_topics}">
                    <li class="fieldcontain">
                        <span id="predicted_topics-label" class="property-label"><g:message code="doc.predicted_topics.label" default="Predicted topics" /></span>
                        <diplo:topics topics="${docInstance.predicted_topics}" hierarchy="all" divClass="property-value" />
                    </li>
                </g:if>

                <g:if test="${docInstance?.train_meaningHtml}">
                    <li class="fieldcontain">
                        <span id="train_meaningHtml-label" class="property-label"><g:message code="doc.train_meaningHtml.label" default="Train meaning HTML" /></span>
                        <span class="property-value" aria-labelledby="train_meaningHtml-label"><g:fieldValue bean="${docInstance}" field="train_meaningHtml"/></span>
                    </li>
                </g:if>

                <g:if test="${docInstance?.meaningText}">
                    <li class="fieldcontain">
                        <span id="meaningText-label" class="property-label"><g:message code="doc.meaningText.label" default="Meaning text" /></span>
                        <span class="property-value" aria-labelledby="meaningText-label"><g:fieldValue bean="${docInstance}" field="meaningText"/></span>
                    </li>
                </g:if>

                <g:if test="${docInstance?.meaningHtml}">
                    <li class="fieldcontain">
                        <span id="meaningHtml-label" class="property-label"><g:message code="doc.meaningHtml.label" default="Meaning HTML" /></span>
                        <span class="property-value" aria-labelledby="meaningHtml-label"><g:fieldValue bean="${docInstance}" field="meaningHtml"/></span>
                    </li>
                </g:if>

                <g:if test="${docInstance?.html}">
                    <li class="fieldcontain">
                        <span id="html-label" class="property-label"><g:message code="doc.html.label" default="HTML" /></span>
                        <span class="property-value" aria-labelledby="html-label"><g:fieldValue bean="${docInstance}" field="html"/></span>
                    </li>
                </g:if>
                
			</ol>

			<g:form url="[resource:docInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>

		</div>
	</body>
</html>
