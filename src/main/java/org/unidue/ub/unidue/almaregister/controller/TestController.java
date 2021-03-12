package org.unidue.ub.unidue.almaregister.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.unidue.ub.unidue.almaregister.client.AddressWebServiceClient;
import org.unidue.ub.unidue.almaregister.model.HisExport;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByAccountResponse;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByRegistrationnumberResponse;
import org.unidue.ub.unidue.almaregister.service.AlmaUserService;
import org.unidue.ub.unidue.almaregister.service.HisService;
import org.unidue.ub.unidue.almaregister.service.ScheduledService;

import java.util.List;

@Controller
@RequestMapping("secure/test")
public class TestController {

    private final AddressWebServiceClient addressWebServiceClient;

    private final ScheduledService scheduledService;

    private final HisService hisService;

    private final Logger log = LoggerFactory.getLogger(TestController.class);

    private final AlmaUserService almaUserService;



    TestController(AddressWebServiceClient addressWebServiceClient,
                   AlmaUserService almaUserService,
                   ScheduledService scheduledService,
                   HisService hisService) {
        this.addressWebServiceClient = addressWebServiceClient;
        this.almaUserService = almaUserService;
        this.scheduledService = scheduledService;
        this.hisService = hisService;
    }

    @GetMapping("/ws/byMatrikel/{identifier}")
    public ResponseEntity<?> testWebService(@PathVariable Long identifier) {
        log.info("requesting user " + identifier);
        ReadAddressByRegistrationnumberResponse response = this.addressWebServiceClient.getAddressByMatrikel(identifier);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ws/byZimId/{zimId}")
    public ResponseEntity<?> testWebServiceByZimId(@PathVariable String zimId) {
        log.info("requesting user " + zimId);
        ReadAddressByAccountResponse response = this.addressWebServiceClient.getAddressByZimId(zimId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ws/hisExport/{zimId}")
    public ResponseEntity<?> testHisExport(@PathVariable String zimId) {
        log.info("requesting user " + zimId);
        List<HisExport> hisExport = this.hisService.getByZimId(zimId);
        return ResponseEntity.ok(hisExport);
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
