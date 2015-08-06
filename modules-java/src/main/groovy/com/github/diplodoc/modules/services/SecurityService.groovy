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
import com.google.api.services.plus.Plus
import com.google.api.services.plus.model.Person
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

    User authenticate(String authProvider, String authType, String authToken) {
        log.debug "Going to authenticate authProvider:${authProvider}, authType:${authType}, authToken:${authToken}"

        User user = null

        if (authProvider == 'google') {
            String googleId = null

            if (authType == 'id_token') {
                googleId = authWithGoogleIdToken(authToken)
            }

            if (authType == 'access_token') {
                googleId = authWithGoogleAccessToken(authToken)
            }
            
            if (googleId) {
                user = userRepository.findOneByGoogleId(googleId)
                log.debug "Corresponding user ${user}"

                if (!user) {
                    user = new User(googleId: googleId)
                    user = userRepository.save user

                    log.debug "Create new user ${user}"
                }
            }
        }

        return user
    }

    String authWithGoogleIdToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Arrays.asList(CLIENT_ID))
                    .build()

            return verifier.verify(idTokenString)?.getPayload()
        } catch (e) {
            log.warn "Exception during authentication ${e}", e
            return null
        }
    }

    String authWithGoogleAccessToken(String accessToken) {
        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken)
            Plus plus = new Plus.Builder(transport, jsonFactory, credential).build()

            Person profile = plus.people().get('me').execute()
            log.debug "Receive profile id:${profile.getId()}"

            return profile?.getId()
        } catch (e) {
            log.warn "Exception during authentication ${e}", e
            return null
        }
    }
}
