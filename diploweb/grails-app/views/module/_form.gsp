<%@ page import="com.github.dipodoc.diploweb.domain.diploexec.Module" %>

<div class="fieldcontain ${hasErrors(bean: moduleInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="module.name.label" default="name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${moduleInstance?.name}"/>
</div>

