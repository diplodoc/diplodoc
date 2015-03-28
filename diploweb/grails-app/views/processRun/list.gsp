<%@ page import="com.github.dipodoc.diploweb.diploexec.ProcessRun" %>

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
				<g:render template="/base-navigation"/>
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
					<g:each in="${processRunInstanceList}" status="i" var="processRunInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

							<td><g:link action="show" id="${processRunInstance.id}">${fieldValue(bean: processRunInstance, field: "id")}</g:link></td>

							<td>${fieldValue(bean: processRunInstance.process, field: "name")}</td>

							<td>${fieldValue(bean: processRunInstance, field: "startTime")}</td>

							<td>${fieldValue(bean: processRunInstance, field: "endTime")}</td>

							<td>${fieldValue(bean: processRunInstance, field: "exitStatus")}</td>

						</tr>
					</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${processRunInstanceCount ?: 0}" />
			</div>
		</div>

	</body>
</html>
