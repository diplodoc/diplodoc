<%@ page import="com.github.dipodoc.webui.admin.domain.orchestration.Process" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'process.label', default: 'Process')}" />
        <title><g:message message="Run process name=${processInstance.name}" /></title>
    </head>

    <body>
        <a href="#show-process" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>

        <div id="show-process" class="content scaffold-show" role="main">
            <h1><g:message message="Run process name=${processInstance.name}" /></h1>

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <ol class="property-list process">

                <g:if test="${processInstance?.id}">
                    <li class="fieldcontain">
                        <span id="id-label" class="property-label"><g:message code="process.id.label" default="id" /></span>

                        <span class="property-value" aria-labelledby="id-label"><g:fieldValue bean="${processInstance}" field="id"/></span>
                    </li>
                </g:if>

                <g:if test="${processInstance?.name}">
                    <li class="fieldcontain">
                        <span id="name-label" class="property-label"><g:message code="process.name.label" default="name" /></span>

                        <span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${processInstance}" field="name"/></span>
                    </li>
                </g:if>

                <g:if test="${processInstance?.definition}">
                    <li class="fieldcontain">
                        <span id="definition-label" class="property-label"><g:message code="process.definition.label" default="definition" /></span>

                        <span class="property-value" aria-labelledby="definition-label">${processInstance.definition}</span>
                    </li>
                </g:if>

            </ol>

            <g:form url="[ resource: processInstance, action: 'start' ]" method="POST">
                <fieldset class="buttons">
                    <g:link action="start" resource="${processInstance}"><g:message message="Start" /></g:link>
                </fieldset>
            </g:form>
        </div>

    </body>
</html>
