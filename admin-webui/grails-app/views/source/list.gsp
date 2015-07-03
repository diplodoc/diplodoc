<%@ page import="com.github.dipodoc.webui.admin.domain.data.Source" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'source.label', default: 'Source')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>

	<body>
		<a href="#list-source" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

		<div id="list-source" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
				<thead>
					<tr>
						<g:sortableColumn property="id" title="${message(code: 'source.id.label', default: 'id')}" />

						<g:sortableColumn property="name" title="${message(code: 'source.name.label', default: 'name')}" />
					</tr>
				</thead>

				<tbody>
					<g:each in="${sourceList}" status="i" var="sourceInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

							<td><g:link action="show" id="${sourceInstance.id}">${fieldValue(bean: sourceInstance, field: "id")}</g:link></td>

							<td>${fieldValue(bean: sourceInstance, field: "name")}</td>

						</tr>
					</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${sourceInstanceCount ?: 0}" />
			</div>
		</div>

	</body>
</html>
