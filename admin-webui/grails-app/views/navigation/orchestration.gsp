<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message code="default.orchestration.label" default="orchestration" /></title>
    </head>

    <body>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><a href="${createLink(controller: 'process', action: 'list')}"><g:message code="default.process.label" default="processes" /></a></li>
                <li><a href="${createLink(controller: 'processRun', action: 'list')}"><g:message code="default.processRun.label" default="process runs" /></a></li>
                <li><a href="${createLink(controller: 'module', action: 'list')}"><g:message code="default.module.label" default="modules" /></a></li>
                <li><a href="${createLink(controller: 'moduleMethodRun', action: 'list')}"><g:message code="default.moduleMethodRun.label" default="module method runs" /></a></li>
            </ul>
        </div>

    </body>
</html>
