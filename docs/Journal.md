# How did I build the Test Serialization application

## Some background
The application is a SpringBoot application including spring hateoas.
The reason why I've decided to start this application is to better understand how to work with Spring and also trying to understand the BioSamples v4.

I'll write here the steps and tests that I've done

### Add the Hateoas message converter
I've created a new class and annotated that with `@Configuration` so it's automatically read by Spring Boot
```java
    @Bean
    MappingJackson2HttpMessageConverter createHalConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        mapper.registerModule(new Jackson2HalModule());
        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(
                MediaType.parseMediaTypes("application/hal+json")
        );
        converter.setObjectMapper(mapper);

        return converter;
    }
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        builder = builder.additionalMessageConverters(
                createHalConverter()
        );
        return builder.build();

    }
```
This two classes are actually necessary only if you decide to query your resources with a rest template. Otherwise you probably will not be able to read the resources properly.

**Note**: check out the [related error](#error-with-converter)

### RestTemplate
Everytime that you need to query an API you should use a RestTemplate. That's why the configuration is needed

### ResourceAssembler
There's a way to decouple the creation of the resource associated with an entity with the from the entity itself and is the `ResourceAssembler` interface (or `ResourceAssemblerSupport` class, that usually is linked to the `ResourceSupport` class included with HATEOAS)

In the ResourceAssembler you can compose your resource using also the `ControllerLinkBuilder` to build the links to other resources (or even the self).
THe problem I'm facing now is that I want to not display a link if an entity is missing (link a derivation). At the moment I'm not able to do that

### Remove unwanted (null / empty) property from the serialization
In order to remove properties from the model, I had to add a `@JsonInclude(JsonInclude.Include.NON_EMPTY)` annotation on the model.
This way I was able to hide those properties that were empty in the model
- [Link Documentation](http://fasterxml.github.io/jackson-annotations/javadoc/2.0.0/com/fasterxml/jackson/annotation/JsonInclude.Include.html)
- [Link Stackoverflow](http://stackoverflow.com/questions/16089651/jackson-serialization-ignore-empty-values-or-null)


### Condense multiple links 
At the moment the solution that I choose to condense multiple links togheter is to use a separate endpoint for the derivedTo samples. This way, the relation is condensed to a single endpoint containing the values. The downside is that you can't read the values of the link directly, so you need all the time to go to the page and check

### Nice way to express optional links
If you have links that are optional (derivation for example), one thing that you can do is wrap the link creation in an optional object and use the `ifPresent` method of the optional to make the code really readable
```java
resource.add(linkTo(SampleController.class).slash(entity.getAccession()).withSelfRel());
    getDerivedFromLink(entity).ifPresent(resource::add);
    getDerivedToLink(entity).ifPresent(resource::add);
    return resource;
}

private Optional<Link> getDerivedFromLink(Sample sample) {
    if (sample.getDerivedFrom() != null) {
        return Optional.of(
            linkTo(SampleController.class).slash(sample.getDerivedFrom().getAccession())
            .withRel("deriveFrom"));
    }
    return Optional.empty();
}
```

### Solving the problem with double content
I've finally managed to solve the problem with the double content serialization.
It was related to the `SampleRestController.search()` method, where the code was returning
`ResponseEntity.ok().body(pagedResources)`. Using instead `ResponseEntity.ok(pagedResource)` there's no more problem 

### Error With Converter
The message converter provided is working properly when is time to *deserialize* the object. Should be necessary then
just for the rest template and nothing else. Indeed, that converter is not configured properly, so the serialization
is raising an unwanted error. I'll comment it out to avoid this

### Back to the origin
I've tried to remove every single configuration from the project, and updated the 
signatures of the methods in the controller to return HttpEntity of Resource and Resources.
Actually, looking at [this question](http://stackoverflow.com/questions/21346387/how-to-correctly-use-pagedresourcesassembler-from-spring-data)
what come out is that Spring HATEOAS ships with 3 different representation classes:
- Resource: to express a single resource object
- Resources: to express a **collection** of object
- PagedResources: an *extension* of resources that include some metadata for the page.

Checking the [HAL specification](https://tools.ietf.org/html/draft-kelly-json-hal-08#section-6), you can find that the plain serialization provided by Spring is already doing all the work.
What I need to do for the other project that I'm working on is check which serialization we're putting in place that is sabotaging everything 

### RestTemplate not deserializing Links
Apparently, using the restTemplate provided by Spring Hateoas is enough
to deserialize resource content. Unfortunately though is not enough to deserialize
resource links. Indeed, trying the test `producesObject()` in [TestSerializationApplicationTests](../src/test/java/uk/ac/ebi/biosamples/TestSerializationApplicationTests.java)
it was returning no links.
The **solution** was to add the `@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)`
annotation to the application, and the links are now deserialized correctly

### @RestController vs @Controller
RestController is imposing a response of type `application/json` or `application/hal+json`. So if you require 
an xml, is going to reply with status code `406 Not Acceptable`

### Change name of collections inside `_embedded` objects
In order to change the name of the collection you just need to annotate the Entity (Sample in this case) 
with `@Relation(value="sample", collectionRelation="samples")`
