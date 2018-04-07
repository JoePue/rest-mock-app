package de.puettner.rest_mock_app.matcher.rest;

import de.puettner.rest_mock_app.matcher.model.RestRequest;
import de.puettner.rest_mock_app.matcher.service.RequestMatcherService;
import de.puettner.rest_mock_app.matcherconfig.model.ResponseConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class AnyRequestRestController {

    private static final String PATH = "/**";
    private final RequestMatcherService matcherService;

    @Autowired
    public AnyRequestRestController(RequestMatcherService matcherService) {
        this.matcherService = matcherService;
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

        RestRequest restRequest = RestRequest.builder().method(method).body(Optional.ofNullable(body)).url(restOfTheUrl).header
                (createHeaderList(request)).build();

        ResponseConfiguration result = matcherService.findResponse(restRequest);

        return ResponseEntity.status(result.getStatusCode()).body(result.getBody().getValue());
    }

    private Optional<Map<String, String>> createHeaderList(final HttpServletRequest request) {
        Enumeration<String> names = request.getHeaderNames();
        Map<String, String> headers = new HashMap<>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            headers.put(name, request.getHeader(name));
        }
        if (headers.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(headers);
    }

}