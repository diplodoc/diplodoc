<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message code="default.diplodata.label" default="diplodata" /></title>
    </head>

    <body>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><a href="${createLink(controller: 'doc', action: 'list')}"><g:message code="default.doc.label" default="docs" /></a></li>
                <li><a href="${createLink(controller: 'topic', action: 'list')}"><g:message code="default.topic.label" default="topics" /></a></li>
                <li><a href="${createLink(controller: 'source', action: 'list')}"><g:message code="default.source.label" default="sources" /></a></li>
                <li><a href="${createLink(uri: '/train-sets')}"><g:message code="default.trainSets.label" default="train sets" /></a></li>
            </ul>
        </div>

    </body>
</html>
