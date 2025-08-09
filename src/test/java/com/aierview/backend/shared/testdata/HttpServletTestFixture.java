package com.aierview.backend.shared.testdata;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class HttpServletTestFixture {
    public static MockHttpServletRequestBuilder anyPostMockMvcRequestBuilder(String url, String json) {
        return MockMvcRequestBuilders
                .post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

    }
}
