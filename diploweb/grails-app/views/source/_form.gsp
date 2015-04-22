<%@ page import="com.github.dipodoc.diploweb.domain.diplodata.Source" %>

<div class="fieldcontain ${hasErrors(bean: sourceInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="source.name.label" default="name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${sourceInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sourceInstance, field: 'newDocsFinderModule', 'error')} required">
	<label for="newDocsFinderModule">
		<g:message code="source.newDocsFinderModule.label" default="new docs finder module" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="newDocsFinderModule" required="" value="${sourceInstance?.newDocsFinderModule}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: sourceInstance, field: 'rssUrl', 'error')} required">
	<label for="rssUrl">
		<g:message code="source.rssUrl.label" default="rss url" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="rssUrl" required="" value="${sourceInstance?.rssUrl}"/>
</div>

