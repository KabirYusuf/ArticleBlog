package dev.levelupschool.backend.service.interfaces;

public interface GeoService {
    boolean isCountryBanned(String country);
    String getLocationInfo(String ipAddress);
}
