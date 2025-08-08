package com.aierview.backend.auth.infra.adapter.google;

import com.aierview.backend.auth.domain.contact.google.IExtractUserDetails;
import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleServiceAdapter implements IExtractUserDetails {
    private final RestTemplate restTemplate;

    @Value("${google.token-info-url}")
    private String tokenIfoUrl;

    @Override
    public Optional<GoogleAccountModel> extractUserDetails(String idToken) {
        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(this.tokenIfoUrl).queryParam("id_token", idToken).build().toUri();
            ResponseEntity<GoogleAccountModel> tokenInfo = restTemplate.getForEntity(uri, GoogleAccountModel.class);
            return Optional.ofNullable(tokenInfo.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
