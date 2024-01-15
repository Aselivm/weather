package org.primshic.stepan.weather.locations;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.primshic.stepan.web.auth.user.User;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.primshic.stepan.web.servlets.search.LocationRequestDTO;

import javax.persistence.PersistenceException;
import java.math.BigDecimal;
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

    public void delete(int userId, BigDecimal lat, BigDecimal lon) {
        log.info("Deleting location: lat {}, lon {}", lat,lon);
        locationRepository.delete(userId,lat,lon);
        log.info("Location lat {}, lon {} deleted", lat,lon);
    }

    public void add(LocationRequestDTO location) {
        if(location.getLocationName().length()>100){
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
        log.info("Adding location: Lat={}, Lon={}", location.getLat(), location.getLon());
        try{
            locationRepository.add(new ModelMapper().map(location, Location.class));
        }catch (PersistenceException e){
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
        log.info("Location added successfully");
    }
}
