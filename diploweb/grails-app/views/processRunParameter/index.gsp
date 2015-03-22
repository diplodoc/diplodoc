
<%@ page import="com.github.dipodoc.diploweb.diploexec.ProcessRunParameter" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'processRunParameter.label', default: 'ProcessRunParameter')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-processRunParameter" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-processRunParameter" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="key" title="${message(code: 'processRunParameter.key.label', default: 'Key')}" />
					
						<th><g:message code="processRunParameter.processRun.label" default="Process Run" /></th>
					
						<g:sortableColumn property="type" title="${message(code: 'processRunParameter.type.label', default: 'Type')}" />
					
						<g:sortableColumn property="value" title="${message(code: 'processRunParameter.value.label', default: 'Value')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${processRunParameterInstanceList}" status="i" var="processRunParameterInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${processRunParameterInstance.id}">${fieldValue(bean: processRunParameterInstance, field: "key")}</g:link></td>
					
						<td>${fieldValue(bean: processRunParameterInstance, field: "processRun")}</td>
					
						<td>${fieldValue(bean: processRunParameterInstance, field: "type")}</td>
					
						<td>${fieldValue(bean: processRunParameterInstance, field: "value")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${processRunParameterInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>
