package org.unidue.ub.unidue.almaregister.client;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByRegistrationnumber;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByRegistrationnumberResponse;

@Service
public class AddressWebServiceClient extends WebServiceGatewaySupport {

    public ReadAddressByRegistrationnumberResponse getAddress(long registrationNumber) {
        ReadAddressByRegistrationnumber request = new ReadAddressByRegistrationnumber();
        request.setRegistrationnumber(registrationNumber);
        ReadAddressByRegistrationnumberResponse response = (ReadAddressByRegistrationnumberResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        return response;
    }
}
