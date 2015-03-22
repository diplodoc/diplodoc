
<%@ page import="com.github.dipodoc.diploweb.diploexec.ProcessRun" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'processRun.label', default: 'ProcessRun')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-processRun" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
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
					
						<g:sortableColumn property="endTime" title="${message(code: 'processRun.endTime.label', default: 'End Time')}" />
					
						<g:sortableColumn property="exitStatus" title="${message(code: 'processRun.exitStatus.label', default: 'Exit Status')}" />
					
						<th><g:message code="processRun.process.label" default="Process" /></th>
					
						<g:sortableColumn property="startTime" title="${message(code: 'processRun.startTime.label', default: 'Start Time')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${processRunInstanceList}" status="i" var="processRunInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${processRunInstance.id}">${fieldValue(bean: processRunInstance, field: "endTime")}</g:link></td>
					
						<td>${fieldValue(bean: processRunInstance, field: "exitStatus")}</td>
					
						<td>${fieldValue(bean: processRunInstance, field: "process")}</td>
					
						<td>${fieldValue(bean: processRunInstance, field: "startTime")}</td>
					
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
