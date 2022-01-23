package fi.turskacreations.ladaapi.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "application_user")
public class ApplicationUser extends AuditEntity {

    @Indexed
    private String username;
    private String password;
    private String activationKey;

}
