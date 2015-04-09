import com.github.dipodoc.diploweb.domain.user.Role
import com.github.dipodoc.diploweb.domain.user.User
import com.github.dipodoc.diploweb.domain.user.UserRole

/**
 * @author yaroslav.yermilov
 */
class BootStrap {

    def init = { servletContext ->
        Role adminRole = Role.findByAuthority('ROLE_ADMIN')
        if (!adminRole) {
            adminRole = new Role(authority: 'ROLE_ADMIN').save flush:true
        }
        Role userRole = Role.findByAuthority('ROLE_USER')
        if (!userRole) {
            userRole = new Role(authority: 'ROLE_USER').save flush:true
        }

        User testUser = User.findByUsername('user')
        if (!testUser) {
            testUser = new User(username: 'user', password: 'user').save flush:true
        }
        User testAdmin = User.findByUsername('admin')
        if (!testAdmin) {
            testAdmin = new User(username: 'admin', password: 'admin').save flush:true
        }

        if (!UserRole.exists(testUser, userRole)) {
            UserRole.create(testUser, userRole, true)
        }
        if (!UserRole.exists(testAdmin, adminRole)) {
            UserRole.create(testAdmin, adminRole, true)
        }

        assert User.count() >= 2
        assert Role.count() >= 2
        assert UserRole.count() >= 2
    }
}