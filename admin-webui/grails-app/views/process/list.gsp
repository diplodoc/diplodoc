<%@ page import="com.github.dipodoc.webui.admin.domain.orchestration.Process" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'process.label', default: 'Process')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>

	<body>
		<a href="#list-process" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

		<div id="list-process" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
				<thead>
					<tr>

						<g:sortableColumn property="name" title="${message(code: 'process.name.label', default: 'name')}" />

						<g:sortableColumn property="lastUpdate" title="${message(code: 'process.lastUpdate.label', default: 'last update')}" />
					
						<g:sortableColumn property="active" title="${message(code: 'process.active.label', default: 'state')}" />
					
					</tr>
				</thead>

				<tbody>
					<g:each in="${processList}" status="i" var="processInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

							<td><g:link action="show" id="${processInstance.id}">${fieldValue(bean: processInstance, field: "name")}</g:link></td>

							<td>${fieldValue(bean: processInstance, field: "lastUpdate")}</td>

							<td>${processInstance.active ? 'active' : 'disabled'}</td>

						</tr>
					</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${processInstanceCount ?: 0}" />
			</div>
		</div>

	</body>
</html>
