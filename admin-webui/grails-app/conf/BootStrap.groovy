import com.github.dipodoc.webui.admin.domain.user.Role
import com.github.dipodoc.webui.admin.domain.user.User
import com.github.dipodoc.webui.admin.domain.user.UserRole

/**
 * @author yaroslav.yermilov
 */
class BootStrap {

    def init = { servletContext ->
        Role adminRole = Role.findByAuthority('ROLE_ADMIN')
        if (!adminRole) {
            adminRole = new Role(authority: 'ROLE_ADMIN').save flush:true
        }

        User testAdmin = User.findByUsername('admin')
        if (!testAdmin) {
            testAdmin = new User(username: 'admin', password: 'admin').save flush:true
        }

        if (!UserRole.exists(testAdmin, adminRole)) {
            UserRole.create(testAdmin, adminRole, true)
        }

        assert User.count() > 0
        assert Role.count() > 0
        assert UserRole.count() > 0
    }
}