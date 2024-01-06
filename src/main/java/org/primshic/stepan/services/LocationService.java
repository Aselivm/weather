package org.primshic.stepan.services;

import org.primshic.stepan.dao.LocationRepository;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

public class LocationService {

    private Logger log = Logger.getLogger(LocationService.class.getName());
    private final LocationRepository locationRepository = new LocationRepository();

    public List<Location> getUserLocations(User user){
        return locationRepository.getUserLocations(user);
    }
    public void delete(int databaseId) {
        locationRepository.delete(databaseId);
    }

    public void add(User user, String name, BigDecimal lat, BigDecimal lon) {
        Location location = new Location();
        location.setUser(user);
        location.setName(name);
        location.setLat(lat);
        location.setLon(lon);

        log.info("Lat from entity: "+location.getLat());
        log.info("Lon from entity: "+location.getLon());

        locationRepository.add(location);
    }
}
