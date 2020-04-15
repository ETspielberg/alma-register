package org.unidue.ub.unidue.almaregister.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.unidue.ub.unidue.almaregister.model.AlmaUser;

@FeignClient(name="almaUser", url="https://api-eu.hosted.exlibrisgroup.com", configuration=FeignConfiguration.class)
@Component
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
    @RequestMapping(method= RequestMethod.POST, value="/almaws/v1/users?social_authentication={socialAuthentication}&send_pin_number_letter={sendPinNumberLetter}&source_institution_code={sourceInstitutionCode}&source_user_id={sourceUserId}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    AlmaUser postAlmawsV1Users(AlmaUser body, @PathVariable("social_authentication") String socialAuthentication, @PathVariable("send_pin_number_letter") String sendPinNumberLetter, @PathVariable("source_institution_code") String sourceInstitutionCode, @PathVariable("source_user_id") String sourceUserId);

}
