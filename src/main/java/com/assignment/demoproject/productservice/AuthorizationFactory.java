package com.assignment.demoproject.productservice;

import com.assignment.demoproject.userservice.JwtTokenProvider;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

/**
 This can be used to add header when one service needs to connect with another, if deployed as separate microservices
 */
public class AuthorizationFactory implements ClientHeadersFactory {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizationFactory(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> multivaluedMap, MultivaluedMap<String, String> multivaluedMap1) {
        MultivaluedMap<String, String> result = new MultivaluedHashMap<>();
        result.add("Authorization", "Bearer " + jwtTokenProvider.generateToken());
        return result;
    }
}
