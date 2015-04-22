<%@ page import="com.github.dipodoc.diploweb.domain.diploexec.ModuleMethod; com.github.dipodoc.diploweb.domain.diploexec.Module" %>

<div class="fieldcontain ${hasErrors(bean: moduleInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="module.name.label" default="name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${moduleInstance?.name}"/>
</div>

<div class="fieldcontain">
	<span id="methods-label" class="property-label"><g:message code="module.methods.label" default="methods" /></span>
	<div class="property-value" aria-labelledby="methods-label">
		<g:each in="${moduleMethodsList}" var="moduleMethod">
			<div class="property-value" aria-labelledby="moduleMethod-label">
				<g:link class="edit" controller="moduleMethod" action="edit" id="${moduleMethod.id}">${moduleMethod.name}</g:link>
			</div>
		</g:each>
	</div>
</div>

