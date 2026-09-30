package org.springframework.samples.petclinic.rest.controller.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.rest.api.OopsApi;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Always throws, so callers can see what the API's error response (via
 * {@link org.springframework.samples.petclinic.rest.advice.ExceptionControllerAdvice}) looks like.
 */
@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api")
public class OopsRestControllerV1 implements OopsApi {

    @Override
    public ResponseEntity<String> failingRequest() {
        throw new RuntimeException("This endpoint always fails, to demonstrate the API's error response format.");
    }
}
