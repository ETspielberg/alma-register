package org.unidue.ub.unidue.almaregister.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.unidue.ub.unidue.almaregister.client.AddressWebServiceClient;
import org.unidue.ub.unidue.almaregister.model.wsclient.Address;

@Controller
@RequestMapping("/api")
public class ApiController {

    private final AddressWebServiceClient addressWebServiceClient;

    ApiController(AddressWebServiceClient addressWebServiceClient) {
        this.addressWebServiceClient = addressWebServiceClient;
    }

    @GetMapping(name="address/{zimId}", produces=MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Address> getAddressdata(@PathVariable String zimId) {
        return ResponseEntity.ok(addressWebServiceClient.getAddressByZimId(zimId).getAddress());
    }
}
