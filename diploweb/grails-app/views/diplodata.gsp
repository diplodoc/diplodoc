<%@ page import="com.github.dipodoc.diploweb.diplodata.Post" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message code="default.diplodata.label" default="diplodata" /></title>
    </head>

    <body>

        <div class="nav" role="navigation">
            <ul>
                <li><a href="${createLink(controller: 'post', action: 'list')}"><g:message code="default.post.label" default="posts" /></a></li>
            </ul>
        </div>

    </body>
</html>
