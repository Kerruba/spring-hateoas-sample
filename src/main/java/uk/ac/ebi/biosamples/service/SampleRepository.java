package uk.ac.ebi.biosamples.service;

import java.util.List;

public interface SampleRepository {

    List<Sample> findAll();

    Sample findById(String id);

}
