<%@ page import="com.github.dipodoc.diploweb.domain.diplodata.Post" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message code="default.diploclient.label" default="diploclient" /></title>
    </head>

    <body>
        <a href="#list-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;" /></a>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
            </ul>
        </div>

        <div id="list-post" class="content scaffold-list" role="main">
            <table>
                <tbody>
                    <g:each in="${postInstanceList}" status="i" var="postInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td>
                                <h3><g:link action="postShow" id="${postInstance.id}">${fieldValue(bean: postInstance, field: 'title')}</g:link></h3>

                                <div><a href="${postInstance.url}" target="_blank"><g:fieldValue bean="${postInstance}" field="url"/></a> at ${fieldValue(bean: postInstance, field: 'publishTime')}</div>

                                <g:if test="${postInstance?.predicted_topics}">
                                    <div><diplo:topics topics="${postInstance.predicted_topics}" hierarchy="all" maxTopicsCount="3" /></div>
                                </g:if>

                                <div>${fieldValue(bean: postInstance, field: 'description')}</div>
                            </td>
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
