package fi.turskacreations.ladaapi.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "transportation_units")
public class TransportationUnit extends AuditEntity {

    @Indexed
    private String trackingCode;

    private List<Event> events = new ArrayList<>();

}
