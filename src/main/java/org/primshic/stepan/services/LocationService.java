package org.primshic.stepan.services;

import org.modelmapper.ModelMapper;
import org.primshic.stepan.dao.LocationRepository;
import org.primshic.stepan.dto.UserLocationDTO;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.User;

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

    public void add(UserLocationDTO location) {

        log.info("Lat from entity: "+location.getLat());
        log.info("Lon from entity: "+location.getLon());

        locationRepository.add(new ModelMapper().map(location,Location.class));
    }
}
