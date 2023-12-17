package permission;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author zhengbizhong 571457082@qq.com
 * @since 2023-11-26 22:13:27
 */
@Aspect
@Component
// @DependsOn({"springContextUtil"})
@Order(2)
@Slf4j
public class PermissionAspect {

    @Pointcut("@annotation(Permission)")
    private void annotationPointCut() {}

    @Around("annotationPointCut()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            throw new ClassCastException();
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();

        Signature signature = joinPoint.getSignature();

        if (!(signature instanceof MethodSignature)) {
            throw new ClassCastException();
        }

        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        // 获取当前访问人信息
        UserContext.UserInfo userInfo = UserContext.getInstance().getCurrentUser();
        if (userInfo == null) {
            throw new Exception();
        }

        // 如实是SuperAdmin直接放行
        // TODO

        Permission annotation = method.getAnnotation(Permission.class);

        // 方法配置的角色
        Permission.Member.Role[] roles = annotation.roles();

        // 方法指定的类型
        Permission.Type type = annotation.type();

        // 核心校验逻辑
        permissionCheck(request, roles, type, userInfo);
        return joinPoint.proceed();

    }

    /**
     * 人员角色鉴权
     *
     * @param request 请求
     * @param permittedRoles 配置的授权角色数组
     * @param type 配置的类型
     * @param userInfo 当前用户信息
     */
    private void permissionCheck(HttpServletRequest request, Permission.Member.Role[] permittedRoles, Permission.Type type, UserContext.UserInfo userInfo) throws Exception {
        boolean hasPermission = false;
        // TODO 只需要校验
//        List<Role> roleList =  xxx.getMemberRole(uuid, spaceId);
//        hasPermission = CollectionUtil.containsAny(currentMemberRoles, Arrays.asList(permittedRoles));
//        if (!hasPermission) {
//            // "没有权限"
//            throw new Exception();
//        }
    }

    /**
     * // TODO 伪代码: 模拟用户上下文
     */
    @Data
    static class UserContext {

        UserInfo currentUser;

        private UserContext(){

        }

        public static UserContext getInstance() {

            return null;
        }

        class UserInfo {

        }
    }
}
