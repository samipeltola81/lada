package fi.turskacreations.ladaapi.domain;

import lombok.Data;
import org.springframework.data.annotation.*;

import java.time.Instant;

@Data
public abstract class AuditEntity {

    @Id
    private String id;

    @CreatedBy
    private String user;

    @CreatedDate
    private Instant createdDate;

    @LastModifiedBy
    private String lastModifiedUser;

    @LastModifiedDate
    private Instant lastModifiedDate;

}
