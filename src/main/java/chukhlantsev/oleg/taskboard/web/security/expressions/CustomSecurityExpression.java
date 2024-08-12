package chukhlantsev.oleg.taskboard.web.security.expressions;

import chukhlantsev.oleg.taskboard.domain.task.Task;
import chukhlantsev.oleg.taskboard.domain.user.Role;
import chukhlantsev.oleg.taskboard.service.TaskService;
import chukhlantsev.oleg.taskboard.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CustomSecurityExpression {

    //private final UserService userService;
    private final TaskService taskService;

    public boolean canAccessUser(Long id) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        JwtEntity jwtEntity = (JwtEntity) authentication.getPrincipal();

        Long userId = jwtEntity.getId();
        return id.equals(userId) || hasAnyRole(Role.ROLE_ADMIN);
    }

    public boolean canAccessTask(Long taskId) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Task task = taskService.getTaskById(taskId);

        return  task.getUser().getId()
                .equals(user.getId()) || hasAnyRole(Role.ROLE_ADMIN);
    }

    private boolean hasAnyRole(Role... roles) {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities();

        for (Role role : roles) {
            GrantedAuthority authority = new SimpleGrantedAuthority(role.name());

            if (authorities.contains(authority))
                return true;
        }
        return false;

    }
}
