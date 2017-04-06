package uk.ac.ebi.biosamples;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resource;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.biosamples.service.Sample;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestSerializationApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void hasHalLinks() throws Exception {
		ResponseEntity<String> entity = this.restTemplate.getForEntity("/samples/test1",
				String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).startsWith(
				"{\"accession\":\"test1\",\"characteristics\":{");
		assertThat(entity.getBody()).contains("_links\":{\"self\":{\"href\"");
	}


	@Test
	public void producesJsonWhenXmlIsPreferred() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, "application/xml;q=0.9,application/json;q=0.8");
		HttpEntity<?> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = this.restTemplate.exchange("/samples/test1",
				HttpMethod.GET, request, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getHeaders().getContentType())
				.isEqualTo(MediaType.parseMediaType("application/json;charset=UTF-8"));
	}

	@Test
    public void producesObject() {
        ResponseEntity<Resource<Sample>> response = this.restTemplate.exchange("/samples/test1",
                HttpMethod.GET, null, new ParameterizedTypeReference<Resource<Sample>>(){});
        Resource<Sample> body = response.getBody();
        assertThat(body.getContent().getAccession()).isEqualTo("test1");
    }

}
