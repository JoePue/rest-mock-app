package de.puettner.rest_mock_app.matcherconfig.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMatcherConfig {

	/** Null is ok, but not empty */
	private RequestMethod method;
	/** Null is ok, but not empty */
	private String urlRegEx;
	/** Null is ok, but not empty */
	private List<HeaderMatcherConfig> headers;
	/** Null is ok, but not empty */
	private String bodyRegEx;
	/** Null is ok, but not empty */
	private String bodyContains;

	@JsonIgnore
	private transient Pattern urlRegExMatchPattern;
	@JsonIgnore
	private transient Pattern bodyRegExMatchPattern;

}
