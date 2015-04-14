<%@ page import="com.github.dipodoc.diploweb.domain.diplodata.Doc" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message message="Train doc id=${docToTrain.id}" /></title>
    </head>

    <body>
        <a href="#show-doc" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><g:link class="list" action="list"><g:message code="default.list.label" args="['Doc']" /></g:link></li>
                <li><g:link action="trainNext"><g:message message="train next doc" /></g:link></li>
            </ul>
        </div>

        <div id="show-doc" class="content scaffold-show" role="main">
            <h1>Train doc id=${docToTrain.id}</h1>

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <g:hasErrors bean="${docToTrain}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${docToTrain}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
            </g:hasErrors>

            <ol class="property-list doc">

                <g:if test="${docToTrain?.id}">
                    <li class="fieldcontain">
                        <span id="id-label" class="property-label"><g:message code="doc.id.label" default="id" /></span>
                        <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${docToTrain}" field="id"/></span>
                    </li>
                </g:if>

                <g:if test="${docToTrain?.url}">
                    <li class="fieldcontain">
                        <span id="url-label" class="property-label"><g:message code="doc.url.label" default="Url" /></span>
                        <span class="property-value" aria-labelledby="url-label"><a href="${docToTrain.url}" target="_blank"><g:fieldValue bean="${docToTrain}" field="url"/></a></span>
                    </li>

                    <li class="fieldcontain">
                        <span id="htmlSource-label" class="property-label"><g:message code="doc.htmlSource.label" default="html source" /></span>
                        <span class="property-value" aria-labelledby="htmlSource-label"><a href="view-source:${docToTrain.url}" target="_blank"><g:fieldValue bean="${docToTrain}" field="url"/></a></span>
                    </li>
                </g:if>

                <g:if test="${docToTrain?.title}">
                    <li class="fieldcontain">
                        <span id="title-label" class="property-label"><g:message code="doc.title.label" default="Title" /></span>
                        <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${docToTrain}" field="title"/></span>
                    </li>
                </g:if>

                <g:if test="${docToTrain?.source}">
                    <li class="fieldcontain">
                        <span id="source-label" class="property-label"><g:message code="doc.source.label" default="Source" /></span>
                        <span class="property-value" aria-labelledby="source-label"><g:link controller="source" action="show" id="${docToTrain?.source?.id}">${docToTrain?.source?.name}</g:link></span>
                    </li>
                </g:if>

                <g:form controller="trainMeaningHtml" action="saveAndNext" method="PUT">
                    <g:hiddenField name="id" value="${docToTrain?.id}" />
                    <fieldset class="form">
                        <div class="fieldcontain ${hasErrors(bean: docToTrain, field: 'train_meaningHtml', 'error')} required">
                            <label for="train_meaningHtml">
                                <g:message code="process.train_meaningHtml.label" default="train meaning html" />
                                <span class="required-indicator">*</span>
                            </label>
                            <g:textArea name="train_meaningHtml" required="" value="${docToTrain?.train_meaningHtml}"/>
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
