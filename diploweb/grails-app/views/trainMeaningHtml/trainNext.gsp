<%@ page import="com.github.dipodoc.diploweb.diplodata.Post" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message message="Train post id=${postToTrain.id}" /></title>
    </head>

    <body>
        <a href="#show-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><g:link action="trainNext"><g:message message="train next post" /></g:link></li>
            </ul>
        </div>

        <div id="show-post" class="content scaffold-show" role="main">
            <h1>Train post id=${postToTrain.id}</h1>

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <ol class="property-list post">

                <g:if test="${postToTrain?.id}">
                    <li class="fieldcontain">
                        <span id="id-label" class="property-label"><g:message code="post.id.label" default="id" /></span>
                        <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${postToTrain}" field="id"/></span>
                    </li>
                </g:if>

                <g:if test="${postToTrain?.url}">
                    <li class="fieldcontain">
                        <span id="url-label" class="property-label"><g:message code="post.url.label" default="Url" /></span>
                        <span class="property-value" aria-labelledby="url-label"><a href="${postToTrain.url}" target="_blank"><g:fieldValue bean="${postToTrain}" field="url"/></a></span>
                    </li>
                </g:if>

                <g:if test="${postToTrain?.title}">
                    <li class="fieldcontain">
                        <span id="title-label" class="property-label"><g:message code="post.title.label" default="Title" /></span>
                        <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${postToTrain}" field="title"/></span>
                    </li>
                </g:if>

                <g:if test="${postToTrain?.source}">
                    <li class="fieldcontain">
                        <span id="source-label" class="property-label"><g:message code="post.source.label" default="Source" /></span>
                        <span class="property-value" aria-labelledby="source-label"><g:link controller="source" action="show" id="${postToTrain?.source?.id}">${postToTrain?.source?.name}</g:link></span>
                    </li>
                </g:if>

                <g:if test="${postToTrain?.description}">
                    <li class="fieldcontain">
                        <span id="description-label" class="property-label"><g:message code="post.description.label" default="Description" /></span>
                        <span class="property-value" aria-labelledby="description-label"><g:fieldValue bean="${postToTrain}" field="description"/></span>
                    </li>
                </g:if>

                <g:if test="${postToTrain?.meaningText}">
                    <li class="fieldcontain">
                        <span id="meaningText-label" class="property-label"><g:message code="post.meaningText.label" default="Meaning text" /></span>
                        <span class="property-value" aria-labelledby="meaningText-label"><g:fieldValue bean="${postToTrain}" field="meaningText"/></span>
                    </li>
                </g:if>

            </ol>

            <g:form url="[ resource: postToTrain, action: 'saveAndNext' ]" method="POST">
                <fieldset class="buttons">
                    <g:actionSubmit class="save" action="saveAndNext" value="${message(code: 'default.button.saveAndNext.label', default: 'Save and next')}"  />
                </fieldset>
            </g:form>

        </div>
    </body>
</html>
