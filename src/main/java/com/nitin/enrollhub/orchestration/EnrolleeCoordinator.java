package com.nitin.enrollhub.orchestration;

import com.nitin.enrollhub.domain.Enrollee;

import java.util.List;

/**
 * Business-layer contract — coordinates enrollee workflows.
 *
 * @author Nitin
 */
public interface EnrolleeCoordinator {

    Enrollee registerNew(Enrollee enrollee);

    List<Enrollee> fetchAll();

    Enrollee fetchSingle(long enrolleeId);

    Enrollee revise(long enrolleeId, Enrollee enrollee);

    void drop(long enrolleeId);
}
