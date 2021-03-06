<%@ page import="com.github.dipodoc.webui.admin.domain.data.Topic" %>

<div class="fieldcontain ${hasErrors(bean: topic, field: 'label', 'error')} required">
	<label for="label">
		<g:message code="topic.label.label" default="Label" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="label" required="" value="${topic?.label}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: topic, field: 'parent', 'error')}">
	<label for="parent">
		<g:message code="topic.parent.label" default="Parent" />
	</label>
	<g:select id="parent" name="parent.id" from="${Topic.list()}" optionKey="id" optionValue="label" noSelection="[ key: 'root', value: null ]" value="" class="many-to-one"/>
</div>

