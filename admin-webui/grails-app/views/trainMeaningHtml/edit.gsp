<%@ page import="com.github.dipodoc.webui.admin.domain.data.Doc" %>

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

                <g:if test="${docInstance?.url}">
                    <li class="fieldcontain">
                        <span id="url-label" class="property-label"><g:message code="doc.url.label" default="Url" /></span>
                        <span class="property-value" aria-labelledby="url-label"><a href="${docInstance.url}" target="_blank"><g:fieldValue bean="${docInstance}" field="url"/></a></span>
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

            </ol>

            <g:form controller="trainMeaningHtml" action="save" method="PUT">
                <g:hiddenField name="id" value="${docInstance?.id}" />
                <fieldset class="form">
                    <div class="fieldcontain ${hasErrors(bean: docInstance, field: 'train_meaningHtml', 'error')} required">
                        <label for="train_meaningHtml">
                            <g:message code="process.train_meaningHtml.label" default="train meaning html" />
                            <span class="required-indicator">*</span>
                        </label>
                        <g:textArea name="train_meaningHtml" required="" value="${docInstance?.train_meaningHtml}"/>
                    </div>
                </fieldset>
                <fieldset class="buttons">
                    <g:actionSubmit class="save" action="save" value="${message(code: 'default.button.saveAndNext.label', default: 'Save')}"  />
                </fieldset>
            </g:form>

            <g:form controller="trainMeaningHtml" action="removeFromTrain" method="DELETE">
                <g:hiddenField name="id" value="${docInstance?.id}" />
                <fieldset class="buttons">
                    <g:actionSubmit class="delete" action="removeFromTrain" value="${message(code: 'default.button.removeFromTrain.label', default: 'Remove from train set')}"  />
                </fieldset>
            </g:form>

        </div>
    </body>
</html>
