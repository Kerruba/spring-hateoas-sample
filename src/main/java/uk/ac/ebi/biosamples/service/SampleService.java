package uk.ac.ebi.biosamples.service;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import uk.ac.ebi.biosamples.model.Sample;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Service
public class SampleService {
    private Map<String, String> mockCharacteristics;

    private Map<String, Sample> allSamples;

    @PostConstruct
    public void createMockCharacteristics() {
        this.mockCharacteristics = new HashMap<>();
        this.mockCharacteristics.put("type", "value");

        allSamples = new HashMap<>();
        Stream.of("test1", "test2","test3","test4")
                .forEach(name -> {
                    Sample sample = new Sample();
                    sample.setAccession(name);
                    sample.setCharacteristics(mockCharacteristics);
                    allSamples.put(name, sample);
                });
        allSamples.get("test1").setDerivedTo(asList(
                allSamples.get("test2"),
                allSamples.get("test3"))
        );
        allSamples.get("test2").setDerivedFrom(allSamples.get("test1"));
        allSamples.get("test3").setDerivedFrom(allSamples.get("test1"));
        allSamples.get("test2").setDerivedTo(singletonList(allSamples.get("test4")));
        allSamples.get("test4").setDerivedFrom(allSamples.get("test2"));


    }

    public List<Sample> getAllSamples() {
        return new ArrayList<>(allSamples.values());
    }

    public Sample getSampleById(String id) throws NotFoundException {
        Sample sample = allSamples.get(id);
        if (sample == null) {
            throw new NotFoundException(String.format("Sample %s not found", id));
        }
        return sample;
    }
}
