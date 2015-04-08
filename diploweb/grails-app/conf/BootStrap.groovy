import com.github.dipodoc.diploweb.domain.user.Role
import com.github.dipodoc.diploweb.domain.user.User
import com.github.dipodoc.diploweb.domain.user.UserRole

/**
 * @author yaroslav.yermilov
 */
class BootStrap {

    def init = { servletContext ->
        def adminRole = new Role(authority: 'ROLE_ADMIN').save flush:true
        def userRole = new Role(authority: 'ROLE_USER').save flush:true

        def testUser = new User(username: 'user', password: 'user')
        def testAdmin = new User(username: 'admin', password: 'admin')
        [ testUser, testAdmin ].each { it.save flush:true }

        UserRole.create testUser, userRole, true
        UserRole.create testAdmin, adminRole, true

        assert User.count() == 2
        assert Role.count() == 2
        assert UserRole.count() == 2
    }
}