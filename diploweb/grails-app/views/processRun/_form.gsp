<%@ page import="com.github.dipodoc.diploweb.diploexec.ProcessRun" %>



<div class="fieldcontain ${hasErrors(bean: processRunInstance, field: 'endTime', 'error')} required">
	<label for="endTime">
		<g:message code="processRun.endTime.label" default="End Time" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="endTime" required="" value="${processRunInstance?.endTime}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: processRunInstance, field: 'exitStatus', 'error')} required">
	<label for="exitStatus">
		<g:message code="processRun.exitStatus.label" default="Exit Status" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="exitStatus" required="" value="${processRunInstance?.exitStatus}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: processRunInstance, field: 'parameters', 'error')} ">
	<label for="parameters">
		<g:message code="processRun.parameters.label" default="Parameters" />
		
	</label>
	
<ul class="one-to-many">
<g:each in="${processRunInstance?.parameters?}" var="p">
    <li><g:link controller="processRunParameter" action="show" id="${p.id}">${p?.encodeAsHTML()}</g:link></li>
</g:each>
<li class="add">
<g:link controller="processRunParameter" action="create" params="['processRun.id': processRunInstance?.id]">${message(code: 'default.add.label', args: [message(code: 'processRunParameter.label', default: 'ProcessRunParameter')])}</g:link>
</li>
</ul>


</div>

<div class="fieldcontain ${hasErrors(bean: processRunInstance, field: 'process', 'error')} required">
	<label for="process">
		<g:message code="processRun.process.label" default="Process" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="process" name="process.id" from="${com.github.dipodoc.diploweb.diploexec.Process.list()}" optionKey="id" required="" value="${processRunInstance?.process?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: processRunInstance, field: 'startTime', 'error')} required">
	<label for="startTime">
		<g:message code="processRun.startTime.label" default="Start Time" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="startTime" required="" value="${processRunInstance?.startTime}"/>

</div>

