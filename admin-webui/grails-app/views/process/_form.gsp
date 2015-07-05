<%@ page import="com.github.dipodoc.webui.admin.domain.orchestration.Process" %>

<div class="fieldcontain ${hasErrors(bean: process, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="process.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${process?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: process, field: 'definition', 'error')} required">
	<label for="definition">
		<g:message code="process.definition.label" default="Definition" />
		<span class="required-indicator">*</span>
	</label>
	<g:textArea name="definition" required="" value="${process?.definition}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: process, field: 'active', 'error')} ">
	<label for="active">
		<g:message code="process.active.label" default="Active" />
		
	</label>
	<g:checkBox name="active" value="${process?.active}" />
</div>

