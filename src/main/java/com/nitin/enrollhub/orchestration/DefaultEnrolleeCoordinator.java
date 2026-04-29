package com.nitin.enrollhub.orchestration;

import com.nitin.enrollhub.domain.Enrollee;
import com.nitin.enrollhub.persistence.EnrolleeLedger;
import com.nitin.enrollhub.support.NoSuchEnrolleeException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default implementation — validates inputs before delegating to the ledger.
 *
 * @author Nitin
 */
@Service
public class DefaultEnrolleeCoordinator implements EnrolleeCoordinator {

    private final EnrolleeLedger ledger;

    public DefaultEnrolleeCoordinator(EnrolleeLedger ledger) {
        this.ledger = ledger;
    }

    @Override
    public Enrollee registerNew(Enrollee enrollee) {
        return ledger.persist(enrollee);
    }

    @Override
    public List<Enrollee> fetchAll() {
        return ledger.retrieveAll();
    }

    @Override
    public Enrollee fetchSingle(long enrolleeId) {
        return ledger.retrieveById(enrolleeId)
                .orElseThrow(() -> new NoSuchEnrolleeException(enrolleeId));
    }

    @Override
    public Enrollee revise(long enrolleeId, Enrollee enrollee) {
        // confirm the record exists first
        ledger.retrieveById(enrolleeId)
                .orElseThrow(() -> new NoSuchEnrolleeException(enrolleeId));
        return ledger.overwrite(enrolleeId, enrollee);
    }

    @Override
    public void drop(long enrolleeId) {
        int rowsAffected = ledger.erase(enrolleeId);
        if (rowsAffected == 0) {
            throw new NoSuchEnrolleeException(enrolleeId);
        }
    }
}
