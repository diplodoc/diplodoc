<%@ page import="com.github.dipodoc.diploweb.diploexec.ModuleMethod" %>

<div class="fieldcontain ${hasErrors(bean: moduleMethodInstance, field: 'module', 'error')} required">
	<label for="module">
		<g:message code="moduleMethod.module.label" default="Module" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="module" name="module.id" from="${com.github.dipodoc.diploweb.diploexec.Module.list()}" optionKey="id" optionValue="name" readonly="true" required="" value="${moduleMethodInstance?.module?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: moduleMethodInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="moduleMethod.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${moduleMethodInstance?.name}"/>
</div>

