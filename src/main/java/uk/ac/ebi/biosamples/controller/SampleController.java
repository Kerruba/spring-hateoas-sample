package uk.ac.ebi.biosamples.controller;

import javassist.NotFoundException;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
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


    @RequestMapping(produces = { MediaTypes.HAL_JSON_VALUE })
    public List<Resource<Sample>> samples() {
        List<Sample> allSamples = sampleService.getAllSamples();
        return allSamples.stream().map(sample -> resourceAssembler.toResource(sample)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public Resource<Sample> base(@PathVariable String id) throws NotFoundException {
        Sample sample = sampleService.getSampleById(id);
        return resourceAssembler.toResource(sample);
    }

    @RequestMapping(value = "/{id}/deriveTo", produces = MediaTypes.HAL_JSON_VALUE)
    public PagedResources<Resource<Sample>> getDerivedTo(@PathVariable String id) throws NotFoundException {
        Sample sample = sampleService.getSampleById(id);
        List<Sample> deriveToList = sample.getDerivedTo();/*.stream()
              .map(derivedToSample -> resourceAssembler.toResource(derivedToSample))
              .collect(Collectors.toList());
              */
        return PagedResources.wrap(deriveToList,
                new PagedResources.PageMetadata(deriveToList.size(),1, deriveToList.size(),1));
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
