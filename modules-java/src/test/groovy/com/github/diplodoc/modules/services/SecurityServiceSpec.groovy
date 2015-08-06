package com.github.diplodoc.modules.services

import com.github.diplodoc.domain.mongodb.User
import com.github.diplodoc.domain.repository.mongodb.UserRepository
import org.bson.types.ObjectId
import spock.lang.Specification

/**
 * @author yaroslav.yermilov
 */
class SecurityServiceSpec extends Specification {

    UserRepository userRepository = Mock(UserRepository)
    SecurityService securityService = Spy(SecurityService)

    def 'User authenticate(String authProvider, String authType, String authToken) - old user with id_token'() {
        setup:
            securityService.userRepository = userRepository
            securityService.authWithGoogleIdToken('TOKEN') >> googleId

            userRepository.findOneByGoogleId(googleId) >> user

        when:
            User actual = securityService.authenticate('google', 'id_token', 'TOKEN')

        then:
            actual == user

        where:
            googleId                   | user
            'user-id'                  | new User(id: new ObjectId('111111111111111111111111'))
            null                       | null
    }

    def 'User authenticate(String authProvider, String authType, String authToken) - old user with access_token'() {
        setup:
            securityService.userRepository = userRepository
            securityService.authWithGoogleAccessToken('TOKEN') >> googleId

            userRepository.findOneByGoogleId(googleId) >> user

        when:
            User actual = securityService.authenticate('google', 'access_token', 'TOKEN')

        then:
            actual == user

        where:
            googleId                   | user
            'user-id'                  | new User(id: new ObjectId('111111111111111111111111'))
            null                       | null
    }

    def 'User authenticate(String authProvider, String authType, String authToken) - new user with id_token'() {
        setup:
            securityService.userRepository = userRepository
            securityService.authWithGoogleIdToken('TOKEN') >> 'user-id'

            userRepository.findOneByGoogleId('user-id') >> null

        when:
            1 * userRepository.save(new User(googleId: 'user-id')) >> new User(id: new ObjectId('111111111111111111111111'), googleId: 'user-id')

            User actual = securityService.authenticate('google', 'id_token', 'TOKEN')

        then:
            actual == new User(id: new ObjectId('111111111111111111111111'), googleId: 'user-id')
    }

    def 'User authenticate(String authProvider, String authType, String authToken) - new user with access_token'() {
        setup:
            securityService.userRepository = userRepository
            securityService.authWithGoogleAccessToken('TOKEN') >> 'user-id'

            userRepository.findOneByGoogleId('user-id') >> null

        when:
            1 * userRepository.save(new User(googleId: 'user-id')) >> new User(id: new ObjectId('111111111111111111111111'), googleId: 'user-id')

            User actual = securityService.authenticate('google', 'access_token', 'TOKEN')

        then:
            actual == new User(id: new ObjectId('111111111111111111111111'), googleId: 'user-id')
    }

    def 'User authenticate(String authProvider, String authType, String authToken) - unknown provider'() {
        when:
            User actual = securityService.authenticate('self', 'some_token', 'TOKEN')

        then:
            actual == null
    }

    def 'User authenticate(String authProvider, String authType, String authToken) - unknown type'() {
        when:
            User actual = securityService.authenticate('google', 'some_token', 'TOKEN')

        then:
            actual == null
    }
}
