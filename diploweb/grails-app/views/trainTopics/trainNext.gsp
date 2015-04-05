<%@ page import="com.github.dipodoc.diploweb.diplodata.Topic; com.github.dipodoc.diploweb.diplodata.Post" %>

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

                <g:if test="${postToTrain?.train_topics}">
                    <li class="fieldcontain">
                        <span id="train_topics-label" class="property-label"><g:message code="post.train_topics.label" default="Train topics" /></span>
                        <diplo:topics topics="${postToTrain.train_topics}" divClass="property-value" />
                    </li>

                    <li class="fieldcontain">
                        <span id="train_topics-remove-label" class="property-label"><g:message code="processRun.train_topics-remove.label" default="Click to remove from train set" /></span>

                        <g:each in="${postToTrain.train_topics}" var="t">
                            <div class="property-value" aria-labelledby="topic-label">
                                <g:link controller="trainTopics" action="removeTopicFromTrainingSet" params="[ postId: postToTrain.id, topicId: t.id, redirectTo: 'trainNext' ]">
                                    ${t.label}
                                </g:link>
                            </div>
                        </g:each>
                    </li>
                </g:if>

                <li class="fieldcontain">
                    <span id="train_topics-add-label" class="property-label"><g:message code="processRun.train_topics-add.label" default="Click to add to train set" /></span>

                    <g:each in="${(Topic.list() - postToTrain.train_topics).sort { it.label }}" var="t">
                        <div class="property-value" aria-labelledby="topic-label">
                            <g:link controller="trainTopics" action="addTopicToTrainingSet" params="[ postId: postToTrain.id, topicId: t.id, redirectTo: 'trainNext' ]">
                                ${t.label}
                            </g:link>
                        </div>
                    </g:each>
                </li>

            </ol>

        </div>
    </body>
</html>
