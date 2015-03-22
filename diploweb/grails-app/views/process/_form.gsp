<%@ page import="com.github.dipodoc.diploweb.diploexec.Process" %>



<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'active', 'error')} ">
	<label for="active">
		<g:message code="process.active.label" default="Active" />
		
	</label>
	<g:checkBox name="active" value="${processInstance?.active}" />

</div>

<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'definition', 'error')} required">
	<label for="definition">
		<g:message code="process.definition.label" default="Definition" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="definition" required="" value="${processInstance?.definition}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'lastUpdate', 'error')} required">
	<label for="lastUpdate">
		<g:message code="process.lastUpdate.label" default="Last Update" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="lastUpdate" required="" value="${processInstance?.lastUpdate}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: processInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="process.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${processInstance?.name}"/>

</div>

