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
                <li><g:link class="list" action="list"><g:message code="default.list.label" args="['Post']" /></g:link></li>
                <li><g:link action="trainNext"><g:message message="train next post" /></g:link></li>
            </ul>
        </div>

        <div id="show-post" class="content scaffold-show" role="main">
            <h1>Train post id=${postToTrain.id}</h1>

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <g:hasErrors bean="${postToTrain}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${postToTrain}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
            </g:hasErrors>

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

                    <li class="fieldcontain">
                        <span id="htmlSource-label" class="property-label"><g:message code="post.htmlSource.label" default="html source" /></span>
                        <span class="property-value" aria-labelledby="htmlSource-label"><a href="view-source:${postToTrain.url}" target="_blank"><g:fieldValue bean="${postToTrain}" field="url"/></a></span>
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

                <g:form controller="trainMeaningHtml" action="saveAndNext" method="PUT">
                    <g:hiddenField name="id" value="${postToTrain?.id}" />
                    <fieldset class="form">
                        <div class="fieldcontain ${hasErrors(bean: postToTrain, field: 'train_meaningHtml', 'error')} required">
                            <label for="train_meaningHtml">
                                <g:message code="process.train_meaningHtml.label" default="train meaning html" />
                                <span class="required-indicator">*</span>
                            </label>
                            <g:textArea name="train_meaningHtml" required="" value="${postToTrain?.train_meaningHtml}"/>
                        </div>
                    </fieldset>
                    <fieldset class="buttons">
                        <g:actionSubmit class="save" action="saveAndNext" value="${message(code: 'default.button.saveAndNext.label', default: 'Save and next')}"  />
                    </fieldset>
                </g:form>

            </ol>

        </div>
    </body>
</html>
