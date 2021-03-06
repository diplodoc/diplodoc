<%@ page import="com.github.dipodoc.webui.admin.domain.orchestration.ModuleMethodRun" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'moduleMethodRun.label', default: 'Module method run')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>

	<body>
		<a href="#list-moduleMethodRun" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
			</ul>
		</div>

		<div id="list-moduleMethodRun" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
				<thead>
					<tr>
						<g:sortableColumn property="id" title="${message(code: 'moduleMethodRun.id.label', default: 'id')}" />

						<th><g:message code="moduleMethodRun.moduleMethod.label" default="Module Method" /></th>

						<g:sortableColumn property="startTime" title="${message(code: 'moduleMethodRun.startTime.label', default: 'start time')}" />
					
						<g:sortableColumn property="endTime" title="${message(code: 'moduleMethodRun.endTime.label', default: 'end time')}" />
					</tr>
				</thead>

				<tbody>
					<g:each in="${moduleMethodRunList}" status="i" var="moduleMethodRun">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

							<td><g:link action="show" id="${moduleMethodRun.id}">${fieldValue(bean: moduleMethodRun, field: "id")}</g:link></td>

							<td>${moduleMethodRun?.moduleMethod?.module?.name}::${moduleMethodRun?.moduleMethod?.name}</td>

							<td>${fieldValue(bean: moduleMethodRun, field: "startTime")}</td>

							<td>${fieldValue(bean: moduleMethodRun, field: "endTime")}</td>

						</tr>
					</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${moduleMethodRunCount ?: 0}" />
			</div>
		</div>

	</body>
</html>
