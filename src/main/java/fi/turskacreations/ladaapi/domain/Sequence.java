package fi.turskacreations.ladaapi.domain;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "sequence")
public class Sequence extends AuditEntity {

    @Indexed
    private String sequenceKey;

    private long seq;

}
