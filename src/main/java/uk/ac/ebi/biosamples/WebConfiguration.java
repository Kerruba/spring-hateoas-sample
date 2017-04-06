package uk.ac.ebi.biosamples;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfiguration {

//    @Bean
//    public Jackson2ObjectMapperBuilder jacksonBuilder() {
//        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//        builder.mixIn(Resource.class, ResourceMixin.class );
//        return builder;
//    }
//
//    private interface ResourceMixin {
//
//        @JsonProperty("_links")
//        abstract List<Link> getLinks();
//
//    }
//
    /*
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
    */


}
