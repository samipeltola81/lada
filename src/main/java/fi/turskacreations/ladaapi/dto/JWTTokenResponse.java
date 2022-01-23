package fi.turskacreations.ladaapi.dto;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JWTTokenResponse {

    String jwt;
}
