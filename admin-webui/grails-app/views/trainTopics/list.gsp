<%@ page import="com.github.dipodoc.webui.admin.domain.data.Doc" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message message="Topics train set" /></title>
    </head>

    <body>
        <a href="#list-doc" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;" /></a>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><g:link action="trainNext"><g:message message="train next doc" /></g:link></li>
            </ul>
        </div>

        <div id="list-doc" class="content scaffold-list" role="main">
            <h1><g:message message="Topics train set, total ${docCount} docs" /></h1>

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <table>
                <thead>
                    <tr>

                        <g:sortableColumn property="id" title="${message(code: 'doc.id.label', default: 'id')}" />

                        <g:sortableColumn property="url" title="${message(code: 'doc.url.label', default: 'url')}" />

                        <g:sortableColumn property="title" title="${message(code: 'doc.title.label', default: 'title')}" />

                    </tr>
                </thead>

                <tbody>
                    <g:each in="${docList}" status="i" var="doc">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                            <td><g:link action="edit" id="${doc.id}">${fieldValue(bean: doc, field: 'id')}</g:link></td>

                            <td><a href="${doc.url}" target="_blank"><g:fieldValue bean="${doc}" field="url"/></a></td>

                            <td>${fieldValue(bean: doc, field: 'title')}</td>

                        </tr>
                    </g:each>
                </tbody>
            </table>

            <div class="pagination">
                <g:paginate total="${docCount ?: 0}" />
            </div>
        </div>

    </body>
</html>
