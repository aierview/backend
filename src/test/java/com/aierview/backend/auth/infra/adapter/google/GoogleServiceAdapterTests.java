package com.aierview.backend.auth.infra.adapter.google;

import com.aierview.backend.auth.domain.contact.google.IExtractUserDetails;
import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;
import com.aierview.backend.auth.infra.adapter.token.JwtTokenAdapter;
import com.aierview.backend.auth.usecase.contract.google.IGoogleSignup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class GoogleServiceAdapterTests {
    private final String tokenInfoUrl = "any_token";


    private IExtractUserDetails extractUserDetails;
    private  RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        this.restTemplate =  Mockito.mock(RestTemplate.class);
        this.extractUserDetails = new GoogleServiceAdapter(restTemplate);


        var tokenInfoUrlField = GoogleServiceAdapter.class.getDeclaredField("tokenIfoUrl");
        tokenInfoUrlField.setAccessible(true);
        tokenInfoUrlField.set(extractUserDetails,tokenInfoUrl);
    }

    @Test
    @DisplayName("Should return optional of empty when request returns empty body")
    void shouldReturnOptionalOfEmptyWhenDoesNotReturnAnyData() {
        String token = "any_token";

        Mockito.when(this.restTemplate.getForEntity(this.tokenInfoUrl + token, Map.class))
                .thenReturn(new ResponseEntity<>(Collections.emptyMap(), HttpStatus.OK));

        Optional<GoogleAccountModel>  ressult = this.extractUserDetails.extractUserDetails(token);

        Assertions.assertTrue(ressult.isEmpty());
        Mockito.verify(this.restTemplate,Mockito.times(1)).getForEntity(this.tokenInfoUrl + token, Map.class);
    }
}
