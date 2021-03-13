package org.unidue.ub.unidue.almaregister.client;

import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByAccount;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByAccountResponse;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByRegistrationnumber;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByRegistrationnumberResponse;

@Service
public class AddressWebServiceClient extends WebServiceGatewaySupport {

    /**
     * retrieves the address data by the matrikel number
     * @param registrationNumber the matrikel number of the user
     * @return the ReadAddressByRegistrationnumberResponse object holding the address data
     */
    public ReadAddressByRegistrationnumberResponse getAddressByMatrikel(long registrationNumber) {
        ReadAddressByRegistrationnumber request = new ReadAddressByRegistrationnumber();
        request.setRegistrationnumber(registrationNumber);
        return (ReadAddressByRegistrationnumberResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }

    /**
     * retrieves the address data by the zim account ID
     * @param account the zim account ID of the user
     * @return ReadAddressByAccountResponse object holding the address data
     */
    public ReadAddressByAccountResponse getAddressByZimId(String account) {
        ReadAddressByAccount request = new ReadAddressByAccount();
        request.setAccount(account);
        return (ReadAddressByAccountResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }
}
