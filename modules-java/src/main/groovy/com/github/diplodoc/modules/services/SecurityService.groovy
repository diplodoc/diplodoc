package com.github.diplodoc.modules.services

import com.github.diplodoc.domain.mongodb.User
import com.github.diplodoc.domain.repository.mongodb.UserRepository
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author yaroslav.yermilov
 */
@Service
@Slf4j
class SecurityService {

    private final static String CLIENT_ID = System.getProperty('google_clientId')

    private HttpTransport transport = new NetHttpTransport()
    private JsonFactory jsonFactory = JacksonFactory.getDefaultInstance()

    @Autowired
    UserRepository userRepository

    def authenticate(String authProvider, String authType, String authToken) {
        log.info "Going to authenticate authProvider:${authProvider}, authType:${authType}, authToken:${authToken}"

        if (authProvider == 'google') {
            if (authType == 'id_token') {
                return authWithGoogleIdToken(authToken)
            }

            if (authType == 'access_token') {
                return authWithGoogleAccessToken(authToken)
            }
        }

        return null
    }

    def authWithGoogleIdToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Arrays.asList(CLIENT_ID))
                    .build()

            GoogleIdToken idToken = verifier.verify(idTokenString)
            if (idToken) {
                GoogleIdToken.Payload payload = idToken.getPayload()
                log.info "Receive payload ${payload.getSubject()}"

                User user = userRepository.findOneByGoogleId(payload.getSubject())
                log.info "Corresponding user ${user}"

                if (!user) {
                    user = new User(googleId: payload.getSubject())
                    userRepository.save user

                    log.info "Create new user ${user}"
                }

                return user
            } else {
                log.info "Google return null"
                return null
            }
        } catch (e) {
            log.info "Exception during authentication ${e}", e
            return null
        }
    }

    def authWithGoogleAccessToken(String accessToken) {
        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

            log.info "Receive serviceAccountId:${credential.serviceAccountId}, serviceAccountPrivateKey:${credential.serviceAccountPrivateKey}, serviceAccountPrivateKeyId:${credential.serviceAccountPrivateKeyId}"

            return null
        } catch (e) {
            log.info "Exception during authentication ${e}", e
            return null
        }
    }
}
