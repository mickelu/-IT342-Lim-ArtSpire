package edu.cit.lim.artspire.file;

import edu.cit.lim.artspire.config.SupabaseConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SupabaseService {

    private final SupabaseConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    public SupabaseService(SupabaseConfig config) {
        this.config = config;
    }

    public String testConnection() {
        String url = config.getSupabaseUrl() + "/rest/v1/";

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", config.getSupabaseKey());
        headers.set("Authorization", "Bearer " + config.getSupabaseKey());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}
