package org.primshic.stepan.services;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.primshic.stepan.dao.LocationRepository;
import org.primshic.stepan.dto.user.UserLocationDTO;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.model.Location;
import org.primshic.stepan.model.User;

import java.util.List;

@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;
    public LocationService(SessionFactory sessionFactory) {
        this.locationRepository = new LocationRepository(sessionFactory);
    }

    public List<Location> getUserLocations(User user){
        log.info("Getting locations for user: {}", user.getLogin());
        List<Location> userLocations = locationRepository.getUserLocations(user);
        log.info("Retrieved {} locations for user: {}", userLocations.size(), user.getLogin());
        return userLocations;
    }

    public void delete(int databaseId) {
        log.info("Deleting location with ID: {}", databaseId);
        locationRepository.delete(databaseId);
        log.info("Location with ID {} deleted", databaseId);
    }

    public void add(UserLocationDTO location) {
        if(location.getLocationName().length()>100){
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
        log.info("Adding location: Lat={}, Lon={}", location.getLat(), location.getLon());
        locationRepository.add(new ModelMapper().map(location, Location.class));
        log.info("Location added successfully");
    }
}
