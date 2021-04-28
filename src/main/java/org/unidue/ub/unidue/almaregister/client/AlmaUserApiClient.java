package org.unidue.ub.unidue.almaregister.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.unidue.ub.alma.shared.user.AlmaUser;
import org.unidue.ub.alma.shared.user.AlmaUsers;

import java.util.List;

@FeignClient(name="almaUser", url="https://api-eu.hosted.exlibrisgroup.com/almaws/v1", configuration=FeignConfiguration.class)
@Service
public interface AlmaUserApiClient {

    /**
     * Create user
     * This Web service creates a new user.
     * @param body This method takes a User object. See [here](/alma/apis/docs/xsd/rest_user.xsd?tags&#x3D;POST) (required)
     * @return Object
     */
    // @RequestMapping(method= RequestMethod.POST, value="/almaws/v1/users?social_authentication={social_authentication}&send_pin_number_letter={send_pin_number_letter}&source_institution_code={source_institution_code}&source_user_id={source_user_id}")
    @RequestMapping(method= RequestMethod.POST, value="/users")
    AlmaUser postUsers(@RequestHeader("Accept") String accept, AlmaUser body, @RequestParam("send_pin_number_letter") boolean sendPinLetter);

    @RequestMapping(method= RequestMethod.GET, value="/users/{identifier}")
    AlmaUser getUser(@PathVariable String identifier, @RequestHeader("Accept") String accept);


    @RequestMapping(method= RequestMethod.PUT, value="/users/{identifier}")
    AlmaUser updateUser(@PathVariable String identifier, @RequestBody AlmaUser user);

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    AlmaUsers retrieveAlmaUsers(@RequestHeader("Accept") String accept, @RequestParam("q") String q, @RequestParam("limit") int limit, @RequestParam("offset") int offset);
}
