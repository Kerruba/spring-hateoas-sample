package uk.ac.ebi.biosamples.controller;

import javassist.NotFoundException;
import org.springframework.hateoas.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.biosamples.model.Sample;
import uk.ac.ebi.biosamples.service.SampleResourceAssembler;
import uk.ac.ebi.biosamples.service.SampleService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@ExposesResourceFor(Sample.class)
@RequestMapping("/samples")
public class SampleController {

//    @Autowired
//    private RestTemplate restTemplate;
    private SampleService sampleService;
    private SampleResourceAssembler resourceAssembler;

    public SampleController(SampleService sampleService,
                            SampleResourceAssembler resourceAssembler) {

        this.sampleService = sampleService;
        this.resourceAssembler = resourceAssembler;
    }


    @GetMapping(produces = { MediaTypes.HAL_JSON_VALUE })
    public HttpEntity<Resources<Resource<Sample>>> samples() {
        List<Sample> allSamples = sampleService.getAllSamples();
        List<Resource<Sample>> sampleResourceList = allSamples.stream().map(sample -> resourceAssembler.toResource(sample)).collect(Collectors.toList());
        Resources<Resource<Sample>> sampleResources = new Resources<>(sampleResourceList);

//
        return ResponseEntity.ok(sampleResources);

    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public HttpEntity<Resource<Sample>> base(@PathVariable String id) throws NotFoundException {
        Sample sample = sampleService.getSampleById(id);
        return ResponseEntity.ok(resourceAssembler.toResource(sample));
    }

    @GetMapping(value = "/{id}/deriveTo", produces = MediaTypes.HAL_JSON_VALUE)
    public HttpEntity<Resources<Sample>> getDerivedTo(@PathVariable String id) throws NotFoundException {
        Sample sample = sampleService.getSampleById(id);
        Resources<Sample> sampleResources = new Resources<>(sample.getDerivedTo());
        return ResponseEntity.ok(sampleResources);
    }

//    @RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
//    public Resources<Sample> testSampleMarshal(HttpServletRequest request, HttpServletResponse response) {
//        RequestEntity<Void> req = RequestEntity.get(URI.create("http://localhost:8080/samples"))
//                .accept(MediaType.parseMediaType(MediaType.APPLICATION_JSON_VALUE))
//                .build();
//        ResponseEntity<Resources<Sample>> re = restTemplate.exchange(req, new ParameterizedTypeReference<Resources<Sample>>() {});
//        return re.getBody();
//    }





}
