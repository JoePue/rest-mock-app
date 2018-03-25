package de.puettner.rest_mock_app.matcherconfig.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.File;

@NoArgsConstructor
@ToString
@AllArgsConstructor
@Data
public class ResponseMatcherConfig {

	private int statusCode;
	private String filename;

	private transient File file;

}
