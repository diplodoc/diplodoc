<%@ page import="com.github.dipodoc.webui.admin.domain.data.Topic" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'topic.label', default: 'Topic')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>

	<body>
		<a href="#list-topic" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<g:render template="/navigation/base-navigation"/>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>

		<div id="list-topic" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>

			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>

			<table>
				<thead>
					<tr>
						<th><g:message code="topic.id.label" default="id" /></th>

						<th><g:message code="topic.label.label" default="label" /></th>
					</tr>
				</thead>

				<tbody>
					<g:each in="${topicList}" status="i" var="topic">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

							<td><g:link action="show" id="${topic.id}">${fieldValue(bean: topic, field: "id")}</g:link></td>

							<td><diplo:topics topics="${topic}" /></td>

						</tr>
					</g:each>
				</tbody>
			</table>

			<div class="pagination">
				<g:paginate total="${topicCount ?: 0}" />
			</div>
		</div>

	</body>
</html>
