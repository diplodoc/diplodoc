<%@ page import="com.github.dipodoc.diploweb.diploexec.ProcessRunParameter" %>



<div class="fieldcontain ${hasErrors(bean: processRunParameterInstance, field: 'key', 'error')} required">
	<label for="key">
		<g:message code="processRunParameter.key.label" default="Key" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="key" required="" value="${processRunParameterInstance?.key}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: processRunParameterInstance, field: 'processRun', 'error')} required">
	<label for="processRun">
		<g:message code="processRunParameter.processRun.label" default="Process Run" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="processRun" name="processRun.id" from="${com.github.dipodoc.diploweb.diploexec.ProcessRun.list()}" optionKey="id" required="" value="${processRunParameterInstance?.processRun?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: processRunParameterInstance, field: 'type', 'error')} required">
	<label for="type">
		<g:message code="processRunParameter.type.label" default="Type" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="type" required="" value="${processRunParameterInstance?.type}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: processRunParameterInstance, field: 'value', 'error')} required">
	<label for="value">
		<g:message code="processRunParameter.value.label" default="Value" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="value" required="" value="${processRunParameterInstance?.value}"/>

</div>

