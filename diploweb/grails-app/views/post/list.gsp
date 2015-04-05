<%@ page import="com.github.dipodoc.diploweb.diplodata.Post" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'post.label', default: 'Post')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>

	<body>
		<a href="#list-post" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;" /></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
			</ul>
		</div>

		<div id="list-post" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>

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

                            <td><g:link action="show" id="${postInstance.id}">${fieldValue(bean: postInstance, field: 'id')}</g:link></td>

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
