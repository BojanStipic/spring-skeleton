package bojanstipic.skeleton.users;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_user")
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
public class User {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "application_user_id_seq"
    )
    private Long id;

    @NaturalId
    private String email;

    private String password;

    private String name;

    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "user_role")
    @Type(type = "pgsql_enum")
    @Builder.Default
    private Role role = Role.USER;

    public enum Role {
        USER,
        ADMIN,
    }
}
