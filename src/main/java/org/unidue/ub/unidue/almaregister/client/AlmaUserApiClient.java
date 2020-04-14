package org.unidue.ub.unidue.almaregister.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="almaUser", url="https://api-eu.hosted.exlibrisgroup.com", configuration=FeignConfiguration.class)
public class AlmaUserApiClient {


}
