package de.puettner.rest_mock_app.matcher.rest;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcher.service.RequestMatcherService;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseMatcherConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@Slf4j
public class AppRestController {

    private static final String PATH = "/**";
    private final RequestMatcherService matcher;

    @Autowired
    public AppRestController(RequestMatcherService matcher) {
        this.matcher = matcher;
    }

    @RequestMapping(method = RequestMethod.GET, path = PATH)
    public ResponseEntity<String> get(HttpServletRequest request) {
        return findMatchingResponse(request, null);
    }

    @RequestMapping(method = RequestMethod.HEAD, path = PATH)
    public ResponseEntity<String> head(HttpServletRequest request) {
        return findMatchingResponse(request, null);
    }

    @RequestMapping(method = RequestMethod.POST, path = PATH)
    public ResponseEntity<String> post(@RequestBody String body, HttpServletRequest request) {
        return findMatchingResponse(request, body);
    }

    @RequestMapping(method = RequestMethod.PUT, path = PATH)
    public ResponseEntity<String> put(@RequestBody String body, HttpServletRequest request) {
        return findMatchingResponse(request, body);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = PATH)
    public ResponseEntity<String> delete(@RequestBody String body, HttpServletRequest request) {
        return findMatchingResponse(request, body);
    }
    
    private ResponseEntity<String> findMatchingResponse(HttpServletRequest request, String body) {
        log.info("Incoming request");
        String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        if (request.getQueryString() != null) {
            restOfTheUrl = restOfTheUrl + "?" + request.getQueryString();
        }
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        RestRequest restRequest = RestRequest.builder().method(method).body(Optional.ofNullable(body)).url(restOfTheUrl).build();

        ResponseMatcherConfig result = matcher.findResponse(restRequest);
        
        return ResponseEntity.status(result.getStatusCode()).body(result.getBody().getContent());
    }

}