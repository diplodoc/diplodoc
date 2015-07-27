package com.github.diplodoc.modules.services

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory

/**
 * @author yaroslav.yermilov
 */
class SecurityService {

    private final static String CLIENT_ID
    private final static String APPS_DOMAIN_NAME

    private HttpTransport transport
    private JsonFactory jsonFactory

    def authenticate(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Arrays.asList(CLIENT_ID))
                .build()

        GoogleIdToken idToken = verifier.verify(idTokenString)
        if (idToken) {
            GoogleIdToken.Payload payload = idToken.getPayload()
            if (payload.getHostedDomain().equals(APPS_DOMAIN_NAME)) {
                assert null : 'user found but what should I do?'
            } else {
                return null
            }
        } else {
            return null
        }
    }
}
