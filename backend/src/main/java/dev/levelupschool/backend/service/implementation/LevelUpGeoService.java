package dev.levelupschool.backend.service.implementation;

import com.google.gson.Gson;

import dev.levelupschool.backend.exception.GeoException;
import dev.levelupschool.backend.service.interfaces.GeoService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LevelUpGeoService implements GeoService {
    @Value("${geo.gatewayUrl}")
    private String geoGatewayUrl;
    @Value("${geo.gatewayPort}")
    private String geoGatewayPort;
    @Value("${BANNED_COUNTRIES}")
    private String bannedCountries;

    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    @Override
    public boolean isCountryBanned(String country) {
        if (bannedCountries == null || bannedCountries.trim().isEmpty()) {
            return false;
        }
        List<String> bannedList = Arrays.asList(bannedCountries.split(","));

        return bannedList.stream()
            .map(String::trim)
            .map(String::toLowerCase)
            .anyMatch(bannedCountry -> bannedCountry.equalsIgnoreCase(country.trim().toLowerCase()));
    }

    @Override
    public String getLocationInfo(String ipAddress) {
        String url = geoGatewayUrl + ":" + geoGatewayPort + "/geolocation/" + ipAddress;

        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {

                throw new GeoException("Failed to get location information: HTTP " + response.code(),
                    HttpStatus.valueOf(response.code()));
            }

            if (response.body() == null) {
                throw new GeoException("No response body received from the geolocation service",
                    HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Map<String, Object> responseMap = gson.fromJson(response.body().charStream(), Map.class);
            String country = responseMap.getOrDefault("country", "Unknown").toString();


            if ("Unknown".equals(country)) {
                throw new GeoException("Unable to determine country from IP address",
                    HttpStatus.BAD_REQUEST);
            }
            return country;
        } catch (IOException e) {
            throw new GeoException("Error while calling the geolocation service: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
