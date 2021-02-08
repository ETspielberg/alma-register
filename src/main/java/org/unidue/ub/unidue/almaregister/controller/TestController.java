package org.unidue.ub.unidue.almaregister.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.unidue.ub.unidue.almaregister.client.AddressWebServiceClient;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByRegistrationnumberResponse;
import org.unidue.ub.unidue.almaregister.service.AlmaUserService;

@Controller
public class TestController {

    private final AddressWebServiceClient addressWebServiceClient;

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    private final AlmaUserService almaUserService;

    TestController(AddressWebServiceClient addressWebServiceClient,
                   AlmaUserService almaUserService) {
        this.addressWebServiceClient = addressWebServiceClient;
        this.almaUserService = almaUserService;
    }

    @GetMapping("/test/WS")
    public ResponseEntity<?> testWebService(Long identifier) {
        log.info("requesting user " + identifier);
        ReadAddressByRegistrationnumberResponse response = this.addressWebServiceClient.getAddress(identifier);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test/analytics")
    public ResponseEntity<?> testAnalyticsService() {
        log.info("testing user update");
        this.almaUserService.updateUserAdresses();
        return ResponseEntity.ok().build();
    }
}
