<%@ page import="com.github.dipodoc.diploweb.domain.diplodata.Post" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message message="${postInstance.title}" /></title>
    </head>

    <body>
        <a href="#show-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><g:link action="postList"><g:message message="back to posts list" /></g:link></li>
            </ul>
        </div>

        <div id="show-post" class="content" role="main">
            <h1>${postInstance.title}</h1>

            <div class="content">
                <a href="${postInstance.url}" target="_blank"><g:fieldValue bean="${postInstance}" field="url"/></a> at <g:fieldValue bean="${postInstance}" field="publishTime"/>
            </div>

            <div class="content">
                <div><diplo:topics topics="${postInstance.predicted_topics}" hierarchy="all" maxTopicsCount="5" /></div>
            </div>

            <div class="content">
                <g:fieldValue bean="${postInstance}" field="meaningText"/>
            </div>
        </div>

    </body>
</html>
