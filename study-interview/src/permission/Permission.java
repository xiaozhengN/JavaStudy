package permission;

import java.lang.annotation.*;

/**
 * 不同的用户角色, 对应不同的权限
 *
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-11-26 22:07:34
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Permission {

    Member.Role [] roles();

    Type type();

    enum Type {
        /**
         * 项目类型
         */
        PROJECT,

        /**
         * 团队类型
         */
        TEAM,

        /**
         * 系统管理类型
         */
        SYSTEM
    }

    class Member {
        enum  Role {
            /**
             * 角色001
             */
            ROLE_001,

            /**
             * 角色002
             */
            ROLE_002
        }
    }
}
