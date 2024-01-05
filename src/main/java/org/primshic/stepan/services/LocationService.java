package org.primshic.stepan.services;

import org.primshic.stepan.dao.LocationRepository;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.User;

public class LocationService {
    private final LocationRepository locationRepository = new LocationRepository();
    public void delete(int userId, double lat, double lon) {
        locationRepository.delete(userId,lat,lon);
    }

    public void add(User user, String name, double lat, double lon) {
        Location location = new Location();
        location.setUser(user);
        location.setName(name);
        location.setLat(lat);
        location.setLon(lon);
        locationRepository.add(location);
    }
}
