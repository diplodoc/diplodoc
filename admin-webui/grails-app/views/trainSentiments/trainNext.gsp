<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title><g:message message="train bigrams" /></title>
    </head>

    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link action="trainNext"><g:message message="train next bigram" /></g:link></li>
            </ul>
        </div>

        <div id="show-bigram" class="content scaffold-show" role="main">
            <h1>train bigram</h1>

            <span class="property-value" aria-labelledby="bigram-label">${bigramToTrain}</span>

            <g:form controller="trainSentiments" action="saveAndNext" method="PUT">
                <g:hiddenField name="bigram" value="${bigramToTrain}" />
                <fieldset class="form">
                    <div class="fieldcontain required">
                        <label for="sentimentScore">
                            <g:message code="process.train_bigram.label" default="sentiment score" />
                            <span class="required-indicator">*</span>
                        </label>
                        <g:textField name="sentimentScore" required="" value=""/>
                    </div>
                </fieldset>
                <fieldset class="buttons">
                    <g:actionSubmit class="save" action="saveAndNext" value="${message(code: 'default.button.saveAndNext.label', default: 'Save and next')}"  />
                </fieldset>
            </g:form>
        </div>

    </body>
</html>
