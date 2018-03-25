package de.puettner.rest_mock_app.matcher.model;

import lombok.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class RestRequest {
    
    /** Always set */
    private RequestMethod method;
    /** Always set */
    private String url;
    private Optional<String> body;

}
