package org.primshic.stepan.repo_mock.services;

import org.hibernate.SessionFactory;
import org.primshic.stepan.repo_mock.dao.LocationRepositoryTest;
import org.primshic.stepan.repo_mock.model.TestLocation;
import org.primshic.stepan.repo_mock.model.TestUser;

public class LocationServiceTest {
    private final LocationRepositoryTest locationRepositoryTest;

    public LocationServiceTest(SessionFactory sessionFactory) {
        locationRepositoryTest = new LocationRepositoryTest(sessionFactory);
    }

    public void delete(int locationId) {
        locationRepositoryTest.delete(locationId);
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
