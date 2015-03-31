<%@ page import="com.github.dipodoc.diploweb.diplodata.Post" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'post.label', default: 'Post')}" />
        <title><g:message code="default.show.label" args='[ "Post id=${postInstance.id}" ]' /></title>
    </head>

    <body>
        <a href="#show-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>

        <div id="show-post" class="content scaffold-show" role="main">
            <h1>Post id=${postInstance.id}</h1>

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <ol class="property-list post">

                <g:if test="${postInstance?.id}">
                    <li class="fieldcontain">
                        <span id="id-label" class="property-label"><g:message code="post.id.label" default="id" /></span>
                        <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${postInstance}" field="id"/></span>
                    </li>
                </g:if>

                <g:if test="${postInstance?.url}">
                    <li class="fieldcontain">
                        <span id="url-label" class="property-label"><g:message code="post.url.label" default="Url" /></span>
                        <span class="property-value" aria-labelledby="url-label"><a href="${postInstance.url}" target="_blank"><g:fieldValue bean="${postInstance}" field="url"/></a></span>
                    </li>
                </g:if>

                <g:if test="${postInstance?.title}">
                    <li class="fieldcontain">
                        <span id="title-label" class="property-label"><g:message code="post.title.label" default="Title" /></span>
                        <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${postInstance}" field="title"/></span>
                    </li>
                </g:if>

                <g:if test="${postInstance?.source}">
                    <li class="fieldcontain">
                        <span id="source-label" class="property-label"><g:message code="post.source.label" default="Source" /></span>
                        <span class="property-value" aria-labelledby="source-label"><g:link controller="source" action="show" id="${postInstance?.source?.id}">${postInstance?.source?.name}</g:link></span>
                    </li>
                </g:if>

                <g:if test="${postInstance?.train_meaningHtml}">
                    <li class="fieldcontain">
                        <span id="train_meaningHtml-label" class="property-label"><g:message code="post.train_meaningHtml.label" default="Train meaning HTML" /></span>
                        <span class="property-value" aria-labelledby="train_meaningHtml-label"><g:fieldValue bean="${postInstance}" field="train_meaningHtml"/></span>
                    </li>
                </g:if>

            </ol>

            <g:form controller="trainMeaningHtml" action="removeFromTrain" method="DELETE">
                <g:hiddenField name="id" value="${postInstance?.id}" />
                <fieldset class="buttons">
                    <g:actionSubmit class="delete" action="removeFromTrain" value="${message(code: 'default.button.removeFromTrain.label', default: 'Remove from train set')}"  />
                </fieldset>
            </g:form>

        </div>
    </body>
</html>
