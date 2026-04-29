package com.nitin.enrollhub.persistence;

import com.nitin.enrollhub.domain.Enrollee;

import java.util.List;
import java.util.Optional;

/**
 * Abstraction for enrollee data-store operations.
 *
 * @author Nitin
 */
public interface EnrolleeLedger {

    Enrollee persist(Enrollee enrollee);

    List<Enrollee> retrieveAll();

    Optional<Enrollee> retrieveById(long enrolleeId);

    Enrollee overwrite(long enrolleeId, Enrollee enrollee);

    int erase(long enrolleeId);
}
