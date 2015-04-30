<%@ page import="com.github.dipodoc.webui.admin.domain.data.Topic" %>
<%@ page import="com.github.dipodoc.webui.admin.domain.data.Doc" %>

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

                <g:if test="${docToTrain?.train_topics}">
                    <li class="fieldcontain">
                        <span id="train_topics-label" class="property-label"><g:message code="doc.train_topics.label" default="Train topics" /></span>
                        <diplo:topics topics="${docToTrain.train_topics}" divClass="property-value" />
                    </li>

                    <li class="fieldcontain">
                        <span id="train_topics-remove-label" class="property-label"><g:message code="processRun.train_topics-remove.label" default="Click to remove from train set" /></span>

                        <g:each in="${docToTrain.train_topics}" var="t">
                            <div class="property-value" aria-labelledby="topic-label">
                                <g:link controller="trainTopics" action="removeTopicFromTrainingSet" params="[ docId: docToTrain.id, topicId: t.id, redirectTo: 'trainNext' ]">
                                    ${t.label}
                                </g:link>
                            </div>
                        </g:each>
                    </li>
                </g:if>

                <li class="fieldcontain">
                    <span id="train_topics-add-label" class="property-label"><g:message code="processRun.train_topics-add.label" default="Click to add to train set" /></span>

                    <g:each in="${(Topic.list() - docToTrain.train_topics).sort { it.label }}" var="t">
                        <div class="property-value" aria-labelledby="topic-label">
                            <g:link controller="trainTopics" action="addTopicToTrainingSet" params="[ docId: docToTrain.id, topicId: t.id, redirectTo: 'trainNext' ]">
                                ${t.label}
                            </g:link>
                        </div>
                    </g:each>
                </li>

            </ol>

        </div>
    </body>
</html>
