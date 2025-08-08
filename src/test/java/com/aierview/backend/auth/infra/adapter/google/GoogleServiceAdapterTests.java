package com.aierview.backend.auth.infra.adapter.google;

import com.aierview.backend.auth.domain.contact.google.IExtractUserDetails;
import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;
import com.aierview.backend.auth.infra.adapter.token.JwtTokenAdapter;
import com.aierview.backend.auth.usecase.contract.google.IGoogleSignup;
import com.aierview.backend.shared.testdata.AuthTestFixture;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    @DisplayName("Should return optional of empty when request returns null body")
    void shouldReturnOptionalOfEmptyWhenRequestReturnsNullBody() {
        String token = "any_token";

        when(this.restTemplate.getForEntity(this.tokenInfoUrl + token, GoogleAccountModel.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        Optional<GoogleAccountModel>  result = this.extractUserDetails.extractUserDetails(token);

        assertTrue(result.isEmpty());
        verify(this.restTemplate,Mockito.times(1)).getForEntity(this.tokenInfoUrl + token, GoogleAccountModel.class);
    }

    @Test
    @DisplayName("Should return optional of google account model when request succeeds")
    void shouldReturnOptionalOfGoogleAccountModelWhenRequestSucceeds() {
        String token = "any_token";
        GoogleAccountModel googleAccountModel = AuthTestFixture.anyGoogleAccountModel();

        when(this.restTemplate.getForEntity(this.tokenInfoUrl + token, GoogleAccountModel.class))
                .thenReturn(new ResponseEntity<>(googleAccountModel, HttpStatus.OK));

        Optional<GoogleAccountModel>  result = this.extractUserDetails.extractUserDetails(token);

        assertTrue(result.isPresent());
        assertEquals(googleAccountModel,result.get());
        verify(this.restTemplate,Mockito.times(1)).getForEntity(this.tokenInfoUrl + token, GoogleAccountModel.class);
    }



}
