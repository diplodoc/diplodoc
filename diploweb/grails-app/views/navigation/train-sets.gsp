<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message code="default.trainSets.label" default="train sets" /></title>
    </head>

    <body>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><a href="${createLink(controller: 'trainMeaningHtml', action: 'list')}"><g:message code="default.trainMeaningHtml.label" default="train meaning html" /></a></li>
                <li><a href="${createLink(controller: 'trainTopics', action: 'list')}"><g:message code="default.trainTopics.label" default="train topics" /></a></li>
            </ul>
        </div>

    </body>
</html>
