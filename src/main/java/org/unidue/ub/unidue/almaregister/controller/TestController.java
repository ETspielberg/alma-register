package org.unidue.ub.unidue.almaregister.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.unidue.ub.unidue.almaregister.client.AddressWebServiceClient;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByRegistrationnumberResponse;
import org.unidue.ub.unidue.almaregister.service.AlmaUserService;
import org.unidue.ub.unidue.almaregister.service.ScheduledService;

@Controller
@RequestMapping("/test")
public class TestController {

    private final AddressWebServiceClient addressWebServiceClient;

    private final ScheduledService scheduledService;

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    private final AlmaUserService almaUserService;



    TestController(AddressWebServiceClient addressWebServiceClient,
                   AlmaUserService almaUserService,
                   ScheduledService scheduledService) {
        this.addressWebServiceClient = addressWebServiceClient;
        this.almaUserService = almaUserService;
        this.scheduledService = scheduledService;
    }

    @GetMapping("/WS")
    public ResponseEntity<?> testWebService(Long identifier) {
        log.info("requesting user " + identifier);
        ReadAddressByRegistrationnumberResponse response = this.addressWebServiceClient.getAddressByMatrikel(identifier);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/analytics")
    public ResponseEntity<?> testAnalyticsService() {
        log.info("testing user update");
        this.almaUserService.updateUserAdresses();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/import")
    public ResponseEntity<?> testImportJob() throws Exception {
        log.info("testing import job");
        this.scheduledService.runImportJob();
        return ResponseEntity.ok().build();
    }
}
