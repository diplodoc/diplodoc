<%@ page import="com.github.dipodoc.diploweb.domain.diplodata.Doc" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message message="${docInstance.title}" /></title>
    </head>

    <body>
        <a href="#show-doc" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><g:link action="docList"><g:message message="back to docs list" /></g:link></li>
            </ul>
        </div>

        <div id="show-doc" class="content" role="main">
            <h1>${docInstance.title}</h1>

            <div class="content">
                <a href="${docInstance.url}" target="_blank"><g:fieldValue bean="${docInstance}" field="url"/></a> at <g:fieldValue bean="${docInstance}" field="publishTime"/>
            </div>

            <div class="content">
                <div><diplo:topics topics="${docInstance.predicted_topics}" hierarchy="all" maxTopicsCount="5" /></div>
            </div>

            <div class="content">
                <g:fieldValue bean="${docInstance}" field="meaningText"/>
            </div>
        </div>

    </body>
</html>
