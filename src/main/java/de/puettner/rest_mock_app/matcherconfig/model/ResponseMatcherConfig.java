package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.*;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMatcherConfig {

	private int statusCode;
	private ResponseElementValueExpression body;

}
