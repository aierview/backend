package com.aierview.backend.auth.infra.adapter.google;

import com.aierview.backend.auth.domain.contact.google.IExtractUserDetails;
import com.aierview.backend.auth.domain.model.google.GoogleAccountModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleServiceAdapter implements IExtractUserDetails {
    private final RestTemplate restTemplate;

    @Value("${google.token-info-url}")
    private String tokenIfoUrl;

    @Override
    public Optional<GoogleAccountModel> extractUserDetails(String token) {
        ResponseEntity<Map> tokenInfo = restTemplate.getForEntity(this.tokenIfoUrl + token, Map.class);
        Map<String, Object> userInfo = tokenInfo.getBody();
        if(tokenInfo.getBody().isEmpty()) return Optional.empty();

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String picture = (String) userInfo.get("picture");
        GoogleAccountModel googleAccountModel = new GoogleAccountModel(email, name, picture);
        return Optional.of(googleAccountModel);
    }
}
