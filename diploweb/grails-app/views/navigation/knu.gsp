<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="default.knu.label" default="knu" /></title>
</head>

<body>

<div class="nav" role="navigation">
    <ul>
        <li><a href="${createLink(controller: 'knuDocuments', action: 'list')}"><g:message code="default.knuDocuments.label" default="documents" /></a></li>
        <li><a href="${createLink(controller: 'knuPosts', action: 'list')}"><g:message code="default.knuPosts.label" default="posts" /></a></li>
    </ul>
</div>

</body>
</html>
