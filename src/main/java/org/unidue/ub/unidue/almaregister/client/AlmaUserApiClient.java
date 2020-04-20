package org.unidue.ub.unidue.almaregister.client;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.unidue.ub.unidue.almaregister.model.AlmaUser;

@FeignClient(name="almaUser", url="https://api-eu.hosted.exlibrisgroup.com", configuration=FeignConfiguration.class)
@Service
public interface AlmaUserApiClient {

    /**
     * Create user
     * This Web service creates a new user.
     * @param body This method takes a User object. See [here](/alma/apis/docs/xsd/rest_user.xsd?tags&#x3D;POST) (required)
     * @param socialAuthentication When customer parameter social_authentication&#x3D;&#39;True&#39;: Send social authentication email to patron. Default value: False. (optional, default to &quot;false&quot;)
     * @param sendPinNumberLetter The email notification for PIN setting change will be sent (optional, default to &quot;false&quot;)
     * @param sourceInstitutionCode The code of the source institution from which the user was linked. Optional (optional, default to &quot;&quot;)
     * @param sourceUserId The ID of the user in the source institution. Optional. (optional, default to &quot;&quot;)
     * @return Object
     */
    // @RequestMapping(method= RequestMethod.POST, value="/almaws/v1/users?social_authentication={social_authentication}&send_pin_number_letter={send_pin_number_letter}&source_institution_code={source_institution_code}&source_user_id={source_user_id}")
    @RequestMapping(method= RequestMethod.POST, value="/almaws/v1/users")
    AlmaUser postAlmawsV1Users(@RequestHeader("Accept") String accept, AlmaUser body);

}
