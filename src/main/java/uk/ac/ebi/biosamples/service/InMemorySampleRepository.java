package uk.ac.ebi.biosamples.service;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

@Service
public class InMemorySampleRepository implements SampleRepository {

    private final Map<String, String> mockCharacteristics = new HashMap<>();
    private final List<Sample> samples = new ArrayList<>();



    @PostConstruct
    public void createMockCharacteristics() {

        this.mockCharacteristics.put("type", "value");

        Sample sample1 = new Sample("test1", mockCharacteristics);
        Sample sample2 = new Sample("test2", mockCharacteristics);
        Sample sample3 = new Sample("test3", mockCharacteristics);
        Sample sample4 = new Sample("test4", mockCharacteristics);
        Sample sample5 = new Sample("test5", mockCharacteristics);
        Sample sample6 = new Sample("test6", mockCharacteristics);
        Sample sample7 = new Sample("test7", mockCharacteristics);

        sample2.setDerivedFrom(sample1);
        sample2.setDerivedTo(sample3, sample4);
        sample5.setDerivedTo(sample7);
        sample1.setDerivedTo(sample6);

        samples.addAll(asList(sample1, sample2, sample3, sample4, sample5, sample6));


    }

    @Override
    public List<Sample> findAll() {
        return samples;
    }

    @Override
    public Sample findById(String accession) {
        for(Sample sample: this.samples) {
            if (ObjectUtils.nullSafeEquals(sample.getAccession(), accession)) {
                return sample;
            }
        }
        return null;
    }
}
