<%@ page import="com.github.dipodoc.webui.admin.domain.data.Topic" %>
<%@ page import="com.github.dipodoc.webui.admin.domain.data.Doc" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'doc.label', default: 'Doc')}" />
        <title><g:message code="default.show.label" args='[ "Doc id=${doc.id}" ]' /></title>
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
            <h1>Doc id=${doc.id}</h1>

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <ol class="property-list doc">

                <g:if test="${doc?.id}">
                    <li class="fieldcontain">
                        <span id="id-label" class="property-label"><g:message code="doc.id.label" default="id" /></span>
                        <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${doc}" field="id"/></span>
                    </li>
                </g:if>

                <g:if test="${doc?.url}">
                    <li class="fieldcontain">
                        <span id="url-label" class="property-label"><g:message code="doc.url.label" default="Url" /></span>
                        <span class="property-value" aria-labelledby="url-label"><a href="${doc.url}" target="_blank"><g:fieldValue bean="${doc}" field="url"/></a></span>
                    </li>
                </g:if>

                <g:if test="${doc?.title}">
                    <li class="fieldcontain">
                        <span id="title-label" class="property-label"><g:message code="doc.title.label" default="Title" /></span>
                        <span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${doc}" field="title"/></span>
                    </li>
                </g:if>

                <g:if test="${doc?.source}">
                    <li class="fieldcontain">
                        <span id="source-label" class="property-label"><g:message code="doc.source.label" default="Source" /></span>
                        <span class="property-value" aria-labelledby="source-label"><g:link controller="source" action="show" id="${doc?.source?.id}">${doc?.source?.name}</g:link></span>
                    </li>
                </g:if>

                <g:if test="${doc?.train_topics}">
                    <li class="fieldcontain">
                        <span id="train_topics-label" class="property-label"><g:message code="doc.train_topics.label" default="Train topics" /></span>
                        <diplo:topics topics="${doc.train_topics}" divClass="property-value" />
                    </li>

                    <li class="fieldcontain">
                        <span id="train_topics-remove-label" class="property-label"><g:message code="processRun.train_topics-remove.label" default="Click to remove from train set" /></span>

                        <g:each in="${doc.train_topics}" var="t">
                            <div class="property-value" aria-labelledby="topic-label">
                                <g:link controller="trainTopics" action="removeTopicFromTrainingSet" params="[ docId: doc.id, topicId: t.id, redirectTo: 'edit' ]">
                                    ${t.label}
                                </g:link>
                            </div>
                        </g:each>
                    </li>
                </g:if>

                <li class="fieldcontain">
                    <span id="train_topics-add-label" class="property-label"><g:message code="processRun.train_topics-add.label" default="Click to add to train set" /></span>

                    <g:each in="${(Topic.list() - doc.train_topics).sort { it.label }}" var="t">
                        <div class="property-value" aria-labelledby="topic-label">
                            <g:link controller="trainTopics" action="addTopicToTrainingSet" params="[ docId: doc.id, topicId: t.id, redirectTo: 'edit' ]">
                                ${t.label}
                            </g:link>
                        </div>
                    </g:each>
                </li>

            </ol>

            <g:form controller="trainTopics" action="removeFromTrain" method="DELETE">
                <g:hiddenField name="id" value="${doc?.id}" />
                <fieldset class="buttons">
                    <g:actionSubmit class="delete" action="removeFromTrain" value="${message(code: 'default.button.removeFromTrain.label', default: 'Remove from train set')}"  />
                </fieldset>
            </g:form>

        </div>
    </body>
</html>
