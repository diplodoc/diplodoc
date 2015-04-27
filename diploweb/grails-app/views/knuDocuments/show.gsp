<%@ page import="com.github.dipodoc.diploweb.domain.diplodata.Doc" %>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'doc.label', default: 'Doc')}" />
		<title><g:message code="default.show.label" args='[ "Doc id=${docInstance.id}" ]' /></title>
	</head>

	<body>
		<a href="#show-doc" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>

		<div class="nav" role="navigation">
			<ul>
				<li><g:link class="list" action="list"><g:message message="document list" /></g:link></li>
			</ul>
		</div>

		<div id="show-doc" class="content scaffold-show" role="main">
            <h1>${docInstance.id}</h1>

            <div class="content">
                <a href="${docInstance.uri}" target="_blank"><g:fieldValue bean="${docInstance}" field="uri"/></a> at <g:fieldValue bean="${docInstance}" field="loadTime"/>
            </div>

            <div class="content">
                ${raw(docInstance.meaningHtml)}
            </div>

			<g:if test="${similar}">
				<br/>
				<div id="similar-label" class="property-label"><g:message message="SIMILAR" /></div>

				<div class="property-value" aria-labelledby="similar-label">
					<g:each in="${similar}" var="similarItem">
						<div class="property-value" aria-labelledby="similarItem-label">
							<g:link action="show" id="${similarItem.id}">${fieldValue(bean: similarItem, field: 'uri')}</g:link>
						</div>
					</g:each>
				</div>
			</g:if>

			<g:form url="[resource:docInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>

		</div>
	</body>
</html>
