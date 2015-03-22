<%@ page import="com.github.dipodoc.diploweb.diplodata.Post" %>



<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'description', 'error')} required">
	<label for="description">
		<g:message code="post.description.label" default="Description" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="description" required="" value="${postInstance?.description}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'loadTime', 'error')} required">
	<label for="loadTime">
		<g:message code="post.loadTime.label" default="Load Time" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="loadTime" required="" value="${postInstance?.loadTime}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'publishTime', 'error')} required">
	<label for="publishTime">
		<g:message code="post.publishTime.label" default="Publish Time" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="publishTime" required="" value="${postInstance?.publishTime}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'source', 'error')} required">
	<label for="source">
		<g:message code="post.source.label" default="Source" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="source" name="source.id" from="${com.github.dipodoc.diploweb.diplodata.Source.list()}" optionKey="id" required="" value="${postInstance?.source?.id}" class="many-to-one"/>

</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'title', 'error')} required">
	<label for="title">
		<g:message code="post.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" required="" value="${postInstance?.title}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: postInstance, field: 'url', 'error')} required">
	<label for="url">
		<g:message code="post.url.label" default="Url" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="url" required="" value="${postInstance?.url}"/>

</div>

