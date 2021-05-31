package bojanstipic.skeleton.users;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_user")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NaturalId
    private String email;

    private String password;

    private String name;

    private String lastName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.USER;

    public enum Role {
        USER, ADMIN;
    }
}
