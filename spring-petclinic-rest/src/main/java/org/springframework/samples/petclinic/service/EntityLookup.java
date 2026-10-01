package org.springframework.samples.petclinic.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.function.Supplier;

/**
 * Shared not-found handling for the per-aggregate services: the Jdbc/Jpa
 * realization throws when a single entity isn't found, callers expect null.
 */
final class EntityLookup {

    private EntityLookup() {
    }

    static <T> T findOrNull(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (ObjectRetrievalFailureException | EmptyResultDataAccessException e) {
            return null;
        }
    }
}
