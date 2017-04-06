package uk.ac.ebi.biosamples.service;

import javassist.NotFoundException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import uk.ac.ebi.biosamples.controller.SampleController;

import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class SampleResourceAssembler implements ResourceAssembler<Sample, Resource<Sample>> {

    public SampleResourceAssembler() {}

    @Override
    public Resource<Sample> toResource(Sample entity) {
        Resource<Sample> resource = new Resource<>(entity);

        resource.add(linkTo(SampleController.class).slash(entity.getAccession()).withSelfRel());
        getDerivedFromLink(entity).ifPresent(resource::add);
        getDerivedToLink(entity).ifPresent(resource::add);
        return resource;
    }

    private Optional<Link> getDerivedFromLink(Sample sample) {
        if (sample.getDerivedFrom() != null) {
            return Optional.of(linkTo(SampleController.class).slash(sample.getDerivedFrom().getAccession()).withRel("deriveFrom"));
        }
        return Optional.empty();
    }

    private Optional<Link> getDerivedToLink(Sample sample) {
        if (sample.getDerivedTo() != null && !sample.getDerivedTo().isEmpty()) {
            try {
                return Optional.of(linkTo(methodOn(SampleController.class).getDerivedTo(sample.getAccession())).withRel("deriveTo"));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

}
