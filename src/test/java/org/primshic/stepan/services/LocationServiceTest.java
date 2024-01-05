package org.primshic.stepan.services;

import org.primshic.stepan.dao.LocationRepositoryTest;
import org.primshic.stepan.model.TestLocation;
import org.primshic.stepan.model.TestUser;

public class LocationServiceTest {
    private final LocationRepositoryTest locationRepositoryTest = new LocationRepositoryTest();

    public void delete(int userId, double lat, double lon) {
        locationRepositoryTest.delete(userId,lat,lon);
    }

    public void add(TestUser user, String name, double lat, double lon) {
        TestLocation testLocation = new TestLocation();
        testLocation.setUser(user);
        testLocation.setName(name);
        testLocation.setLat(lat);
        testLocation.setLon(lon);
        locationRepositoryTest.add(testLocation);
    }
}
