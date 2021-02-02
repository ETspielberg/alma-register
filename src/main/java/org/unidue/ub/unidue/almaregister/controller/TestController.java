package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.unidue.ub.unidue.almaregister.client.AddressWebServiceClient;
import org.unidue.ub.unidue.almaregister.client.AlmaAnalyticsReportClient;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByRegistrationnumberResponse;
import org.unidue.ub.unidue.almaregister.service.AlmaUserService;

import java.io.IOException;

@Controller
public class TestController {

    private final AddressWebServiceClient addressWebServiceClient;

    private final AlmaAnalyticsReportClient almaAnalyticsReportClient;

    private final AlmaUserService almaUserService;

    TestController(AddressWebServiceClient addressWebServiceClient,
                   AlmaAnalyticsReportClient almaAnalyticsReportClient,
                   AlmaUserService almaUserService) {
        this.addressWebServiceClient = addressWebServiceClient;
        this.almaAnalyticsReportClient = almaAnalyticsReportClient;
        this.almaUserService = almaUserService;
    }

    @GetMapping("/test/WS")
    public ResponseEntity<?> testWebService(Long identifier) {
        ReadAddressByRegistrationnumberResponse response = this.addressWebServiceClient.getAddress(identifier);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test/analytics")
    public ResponseEntity<?> testAnalyticsService() throws IOException {
        this.almaUserService.updateUserAdresses();
        return ResponseEntity.ok().build();
    }
}
