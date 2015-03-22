<%@ page import="com.github.dipodoc.diploweb.diplodata.Topic" %>



<div class="fieldcontain ${hasErrors(bean: topicInstance, field: 'label', 'error')} required">
	<label for="label">
		<g:message code="topic.label.label" default="Label" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="label" required="" value="${topicInstance?.label}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: topicInstance, field: 'parent', 'error')} required">
	<label for="parent">
		<g:message code="topic.parent.label" default="Parent" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="parent" name="parent.id" from="${com.github.dipodoc.diploweb.diplodata.Topic.list()}" optionKey="id" required="" value="${topicInstance?.parent?.id}" class="many-to-one"/>

</div>

