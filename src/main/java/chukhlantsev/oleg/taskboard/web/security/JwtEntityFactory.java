package chukhlantsev.oleg.taskboard.web.security;

import chukhlantsev.oleg.taskboard.domain.user.Role;
import chukhlantsev.oleg.taskboard.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

public class JwtEntityFactory {

    public static JwtEntity mapToJwtEntity(User user)
    {
        return new JwtEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                mapToGrandAuthorities(user.getRoles())
                );
    }

    private static List<? extends GrantedAuthority> mapToGrandAuthorities(Set<Role> roles) {

        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
