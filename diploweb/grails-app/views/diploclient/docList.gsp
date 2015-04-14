<%@ page import="com.github.dipodoc.diploweb.domain.diplodata.Doc" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message code="default.diploclient.label" default="diploclient" /></title>
    </head>

    <body>
        <a href="#list-doc" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;" /></a>

        <div class="nav" role="navigation">
            <ul>
                <g:render template="/navigation/base-navigation"/>
            </ul>
        </div>

        <div id="list-doc" class="content scaffold-list" role="main">
            <table>
                <tbody>
                    <g:each in="${docInstanceList}" status="i" var="docInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td>
                                <h3><g:link action="docShow" id="${docInstance.id}">${fieldValue(bean: docInstance, field: 'title')}</g:link></h3>

                                <div><a href="${docInstance.url}" target="_blank"><g:fieldValue bean="${docInstance}" field="url"/></a> at ${fieldValue(bean: docInstance, field: 'publishTime')}</div>

                                <g:if test="${docInstance?.predicted_topics}">
                                    <div><diplo:topics topics="${docInstance.predicted_topics}" hierarchy="all" maxTopicsCount="3" /></div>
                                </g:if>

                                <div>${fieldValue(bean: docInstance, field: 'description')}</div>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>

            <div class="pagination">
                <g:paginate total="${docInstanceCount ?: 0}" />
            </div>
        </div>

    </body>
</html>
