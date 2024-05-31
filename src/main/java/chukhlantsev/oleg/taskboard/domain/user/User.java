package chukhlantsev.oleg.taskboard.domain.user;

import chukhlantsev.oleg.taskboard.domain.task.Task;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String password;

    //todo возможно тут нужна дтошка. @Transient значит, что мы не переводим это поле в БД. ( а transient  - что не переводим в json, не сериализуем)
    @Transient
    private String passwordConfirmation;


    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name ="users_roles")
    @Enumerated(value = EnumType.STRING)
    private Set<Role> roles;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Task> tasks;


}
