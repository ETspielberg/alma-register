package org.unidue.ub.unidue.almaregister.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.unidue.ub.alma.shared.user.AlmaUser;
import org.unidue.ub.alma.shared.user.AlmaUsers;


@FeignClient(name="almaUser", url="https://api-eu.hosted.exlibrisgroup.com/almaws/v1", configuration=FeignConfiguration.class)
@Service
public interface AlmaUserApiClient {

    /**
     * Create user
     * This Web service creates a new user.
     * @param body This method takes a User object. See [here](/alma/apis/docs/xsd/rest_user.xsd?tags&#x3D;POST) (required)
     * @return Object
     */
    @RequestMapping(method= RequestMethod.POST, value="/users")
    AlmaUser postUsers(@RequestHeader("Accept") String accept, AlmaUser body, @RequestParam("send_pin_number_letter") boolean sendPinLetter);

    /**
     * retrieve user by primary identifier
     * @param identifier the identifier of the requested user
     * @param accept the response type
     * @return an alma user object
     */
    @RequestMapping(method= RequestMethod.GET, value="/users/{identifier}")
    AlmaUser getUser(@PathVariable String identifier, @RequestHeader("Accept") String accept);


    /**
     * updates an user object
     * @param identifier the identifier of the user to be updated
     * @param user the user object with the updated fields
     * @return the updated user object
     */
    @RequestMapping(method= RequestMethod.PUT, value="/users/{identifier}")
    AlmaUser updateUser(@PathVariable String identifier, @RequestBody AlmaUser user);

    /**
     * retrieves a list of users
     * @param accept the response type
     * @param q the query
     * @param limit the number of results per page
     * @param offset the start index in the results page
     * @return an AlmaUsers object holding a list of users
     */
    @RequestMapping(method = RequestMethod.GET, value = "/users")
    AlmaUsers retrieveAlmaUsers(@RequestHeader("Accept") String accept, @RequestParam("q") String q, @RequestParam("limit") int limit, @RequestParam("offset") int offset);
}
