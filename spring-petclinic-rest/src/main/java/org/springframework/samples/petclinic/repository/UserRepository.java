package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.User;

/**
 * Spring Data JPA repository for <code>User</code> domain objects.
 */
public interface UserRepository extends Repository<User, String> {

    void save(User user) throws DataAccessException;
}
