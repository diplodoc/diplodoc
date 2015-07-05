<%@ page import="com.github.dipodoc.webui.admin.domain.orchestration.Module" %>
<%@ page import="com.github.dipodoc.webui.admin.domain.orchestration.ModuleMethod" %>

<div class="fieldcontain ${hasErrors(bean: moduleMethod, field: 'module', 'error')} required">
	<label for="module">
		<g:message code="moduleMethod.module.label" default="Module" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="module" name="module.id" from="${Module.list()}" optionKey="id" optionValue="name" readonly="true" required="" value="${moduleMethod?.module?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: moduleMethod, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="moduleMethod.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${moduleMethod?.name}"/>
</div>

