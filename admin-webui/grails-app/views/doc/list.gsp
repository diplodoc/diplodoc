<%@ page import="com.github.dipodoc.webui.admin.domain.data.Doc" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'doc.label', default: 'Doc')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>

	<body>
		<a href="#list-doc" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;" /></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
			</ul>
		</div>

		<div id="list-doc" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
			    <thead>
					<tr>

						<g:sortableColumn property="id" title="${message(code: 'doc.id.label', default: 'id')}" />

                        <g:sortableColumn property="uri" title="${message(code: 'doc.uri.label', default: 'uri')}" />
					
						<g:sortableColumn property="title" title="${message(code: 'doc.title.label', default: 'title')}" />

					</tr>
				</thead>

				<tbody>
				    <g:each in="${docInstanceList}" status="i" var="docInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                            <td><g:link action="show" id="${docInstance.id}">${fieldValue(bean: docInstance, field: 'id')}</g:link></td>

							<td><a href="${docInstance.uri}" target="_blank"><g:fieldValue bean="${docInstance}" field="uri"/></a></td>

                            <td>${fieldValue(bean: docInstance, field: 'title')}</td>

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
