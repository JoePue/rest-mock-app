package de.puettner.rest_mock_app.matcher.model;

import javax.annotation.Nullable;

import lombok.*;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.Optional;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class RestRequest {

    /** Always set */
    @NonNull
    private RequestMethod method;

    /** Always set */
    @NonNull
    private String url;

    @Nullable
    private Optional<String> body;

    @NonNull
    private Optional<Map<String, String>> header;

}
