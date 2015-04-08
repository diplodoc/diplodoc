<%@ page import="com.github.dipodoc.diploweb.domain.diplodata.Post" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message message="Topics train set" /></title>
    </head>

    <body>
        <a href="#list-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;" /></a>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
                <li><g:link action="trainNext"><g:message message="train next post" /></g:link></li>
            </ul>
        </div>

        <div id="list-post" class="content scaffold-list" role="main">
            <h1><g:message message="Topics train set, total ${postInstanceCount} posts" /></h1>

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <table>
                <thead>
                    <tr>

                        <g:sortableColumn property="id" title="${message(code: 'post.id.label', default: 'id')}" />

                        <g:sortableColumn property="url" title="${message(code: 'post.url.label', default: 'url')}" />

                        <g:sortableColumn property="title" title="${message(code: 'post.title.label', default: 'title')}" />

                    </tr>
                </thead>

                <tbody>
                    <g:each in="${postInstanceList}" status="i" var="postInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                            <td><g:link action="edit" id="${postInstance.id}">${fieldValue(bean: postInstance, field: 'id')}</g:link></td>

                            <td><a href="${postInstance.url}" target="_blank"><g:fieldValue bean="${postInstance}" field="url"/></a></td>

                            <td>${fieldValue(bean: postInstance, field: 'title')}</td>

                        </tr>
                    </g:each>
                </tbody>
            </table>

            <div class="pagination">
                <g:paginate total="${postInstanceCount ?: 0}" />
            </div>
        </div>

    </body>
</html>
