<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message message="sentiment train set" /></title>
    </head>

    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link action="trainNext"><g:message message="train next sentiment" /></g:link></li>
            </ul>
        </div>

        <div id="list-doc" class="content scaffold-list" role="main">
            <h1><g:message message="sentiment train set, total ${trainingBigrams.keySet().size()} bigrams" /></h1>

            <table>
                <thead>
                    <tr>
                        <g:sortableColumn property="bigram" title="bigram" />

                        <g:sortableColumn property="score" title="score" />
                    </tr>
                </thead>

                <tbody>
                    <g:each in="${trainingBigrams.keySet()}" status="i" var="bigram">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td>${bigram}</td>

                            <td>${trainingBigrams.get(bigram)}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>

            <div class="pagination">
                <g:paginate total="${trainingBigrams.keySet().size() ?: 0}" />
            </div>
        </div>

    </body>
</html>
