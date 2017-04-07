package uk.ac.ebi.biosamples.controller;

import javassist.NotFoundException;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.ebi.biosamples.service.Sample;
import uk.ac.ebi.biosamples.service.SampleRepository;
import uk.ac.ebi.biosamples.service.SampleResourceAssembler;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@ExposesResourceFor(Sample.class)
@RequestMapping("/samples")
public class SampleController {

//    @Autowired
//    private RestTemplate restTemplate;
    private SampleRepository repository;
    private SampleResourceAssembler resourceAssembler;

    public SampleController(SampleRepository sampleService,
                            SampleResourceAssembler resourceAssembler) {

        this.repository = sampleService;
        this.resourceAssembler = resourceAssembler;
    }


    @GetMapping(produces = { MediaTypes.HAL_JSON_VALUE })
    public HttpEntity<Resources<Resource<Sample>>> samples() {
        List<Sample> allSamples = repository.findAll();
        List<Resource<Sample>> sampleResourceList = allSamples.stream().map(sample -> resourceAssembler.toResource(sample)).collect(Collectors.toList());
        Resources<Resource<Sample>> sampleResources = new Resources<>(sampleResourceList);
        return ResponseEntity.ok(sampleResources);

    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public HttpEntity<Resource<Sample>> base(@PathVariable String id) throws NotFoundException {
        Sample sample = repository.findById(id);
        return ResponseEntity.ok(resourceAssembler.toResource(sample));
    }

    @GetMapping(value = "/{id}/deriveTo", produces = MediaTypes.HAL_JSON_VALUE)
    public HttpEntity<Resources<Sample>> getDerivedTo(@PathVariable String id) throws NotFoundException {
        Sample sample = repository.findById(id);
        Resources<Sample> sampleResources = new Resources<>(sample.getDerivedTo());
        return ResponseEntity.ok(sampleResources);
    }





}
