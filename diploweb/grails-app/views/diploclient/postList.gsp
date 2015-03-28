<%@ page import="com.github.dipodoc.diploweb.diplodata.Post" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'post.label', default: 'Post')}" />
        <title><g:message code="default.diploclient.label" default="diploclient" /></title>
    </head>

    <body>
        <a href="#list-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;" /></a>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/base-navigation"/>
            </ul>
        </div>

        <div id="list-post" class="content scaffold-list" role="main">
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <table>
                <thead>
                <tr>

                    <g:sortableColumn property="title" title="${message(code: 'post.title.label', default: 'title')}" />

                    <g:sortableColumn property="url" title="${message(code: 'post.url.label', default: 'url')}" />

                    <g:sortableColumn property="publishTime" title="${message(code: 'post.publishTime.label', default: 'publishTime')}" />

                </tr>
                </thead>

                <tbody>
                <g:each in="${postInstanceList}" status="i" var="postInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                        <td><g:link action="show" id="${postInstance.id}">${fieldValue(bean: postInstance, field: 'title')}</g:link></td>

                        <td><a href="${postInstance.url}" target="_blank"><g:fieldValue bean="${postInstance}" field="url"/></a></td>

                        <td>${fieldValue(bean: postInstance, field: 'publishTime')}</td>

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
