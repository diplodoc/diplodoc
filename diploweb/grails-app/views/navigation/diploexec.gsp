<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message code="default.diploexec.label" default="diploexec" /></title>
    </head>

    <body>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><a href="${createLink(controller: 'process', action: 'list')}"><g:message code="default.process.label" default="processes" /></a></li>
                <li><a href="${createLink(controller: 'processRun', action: 'list')}"><g:message code="default.processRun.label" default="process runs" /></a></li>
            </ul>
        </div>

    </body>
</html>
