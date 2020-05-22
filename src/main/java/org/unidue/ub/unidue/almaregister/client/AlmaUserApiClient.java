package org.unidue.ub.unidue.almaregister.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.unidue.ub.alma.shared.user.AlmaUser;

@FeignClient(name="almaUser", url="https://api-eu.hosted.exlibrisgroup.com", configuration=FeignConfiguration.class)
@Service
public interface AlmaUserApiClient {

    /**
     * Create user
     * This Web service creates a new user.
     * @param body This method takes a User object. See [here](/alma/apis/docs/xsd/rest_user.xsd?tags&#x3D;POST) (required)
     * @return Object
     */
    // @RequestMapping(method= RequestMethod.POST, value="/almaws/v1/users?social_authentication={social_authentication}&send_pin_number_letter={send_pin_number_letter}&source_institution_code={source_institution_code}&source_user_id={source_user_id}")
    @RequestMapping(method= RequestMethod.POST, value="/almaws/v1/users")
    AlmaUser postUsers(@RequestHeader("Accept") String accept, AlmaUser body);

}
