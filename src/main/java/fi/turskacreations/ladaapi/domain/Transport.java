package fi.turskacreations.ladaapi.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Document(collection = "transports")
public class Transport extends AuditEntity {

    @Indexed
    private Long transportId;

    private String driver;

    private List<String> registeredTrackingCodes = new ArrayList<>();
}
