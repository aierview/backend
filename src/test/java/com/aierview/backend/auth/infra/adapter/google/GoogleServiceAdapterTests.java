package com.aierview.backend.auth.infra.adapter.google;

import com.aierview.backend.auth.domain.contact.google.IExtractUserDetails;
import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;
import com.aierview.backend.auth.domain.model.google.GoogleSignupRequest;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GoogleServiceAdapterTests {
    private final String tokenInfoUrl = "any_token_info_url";


    private IExtractUserDetails extractUserDetails;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        this.restTemplate = Mockito.mock(RestTemplate.class);
        this.extractUserDetails = new GoogleServiceAdapter(restTemplate);


        var tokenInfoUrlField = GoogleServiceAdapter.class.getDeclaredField("tokenIfoUrl");
        tokenInfoUrlField.setAccessible(true);
        tokenInfoUrlField.set(extractUserDetails, tokenInfoUrl);
    }

    @Test
    @DisplayName("Should return optional of empty when request returns null body")
    void shouldReturnOptionalOfEmptyWhenRequestReturnsNullBody() {
        GoogleSignupRequest request = AuthTestFixture.anyGoogleSignupRequest();
        URI uri = UriComponentsBuilder.fromHttpUrl(this.tokenInfoUrl).queryParam("id_token", request.idToken()).build().toUri();
        when(this.restTemplate.getForEntity(uri, GoogleAccountModel.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        Optional<GoogleAccountModel> result = this.extractUserDetails.extractUserDetails(request);

        assertTrue(result.isEmpty());
        verify(this.restTemplate, Mockito.times(1)).getForEntity(uri, GoogleAccountModel.class);
    }

    @Test
    @DisplayName("Should return optional of google account model when request succeeds")
    void shouldReturnOptionalOfGoogleAccountModelWhenRequestSucceeds() {
        GoogleSignupRequest request = AuthTestFixture.anyGoogleSignupRequest();
        GoogleAccountModel googleAccountModel = AuthTestFixture.anyGoogleAccountModel();

        URI uri = UriComponentsBuilder.fromHttpUrl(this.tokenInfoUrl).queryParam("id_token", request.idToken()).build().toUri();
        when(this.restTemplate.getForEntity(uri, GoogleAccountModel.class))
                .thenReturn(new ResponseEntity<>(googleAccountModel, HttpStatus.OK));

        Optional<GoogleAccountModel> result = this.extractUserDetails.extractUserDetails(request);

        assertTrue(result.isPresent());
        assertEquals(googleAccountModel, result.get());
        verify(this.restTemplate, Mockito.times(1)).getForEntity(uri, GoogleAccountModel.class);
    }


}
