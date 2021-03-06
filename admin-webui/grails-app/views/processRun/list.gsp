<%@ page import="com.github.dipodoc.webui.admin.domain.orchestration.ProcessRun" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'processRun.label', default: 'Process Run')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>

	<body>
		<a href="#list-processRun" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
			</ul>
		</div>

		<div id="list-processRun" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
				<thead>
					<tr>

						<g:sortableColumn property="id" title="${message(code: 'processRun.id.label', default: 'id')}" />

						<th><g:message code="processRun.process.label" default="process" /></th>

						<g:sortableColumn property="startTime" title="${message(code: 'processRun.startTime.label', default: 'start')}" />
					
						<g:sortableColumn property="endTime" title="${message(code: 'processRun.endTime.label', default: 'end')}" />
					
						<g:sortableColumn property="exitStatus" title="${message(code: 'processRun.exitStatus.label', default: 'exit status')}" />
					
					</tr>
				</thead>

				<tbody>
					<g:each in="${processRunList}" status="i" var="processRun">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

							<td><g:link action="show" id="${processRun.id}">${fieldValue(bean: processRun, field: "id")}</g:link></td>

							<td>${fieldValue(bean: processRun.process, field: "name")}</td>

							<td>${fieldValue(bean: processRun, field: "startTime")}</td>

							<td>${fieldValue(bean: processRun, field: "endTime")}</td>

							<td>${fieldValue(bean: processRun, field: "exitStatus")}</td>

						</tr>
					</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${processRunCount ?: 0}" />
			</div>
		</div>

	</body>
</html>
