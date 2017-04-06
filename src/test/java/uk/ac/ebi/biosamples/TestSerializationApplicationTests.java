package uk.ac.ebi.biosamples;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSerializationApplicationTests {

//	@Autowired
//	private TestRestTemplate restTemplate;
//
//	@Test
//	public void hasHalLinks() throws Exception {
//		ResponseEntity<String> entity = this.restTemplate.getForEntity("/customers/1",
//				String.class);
//		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(entity.getBody()).startsWith(
//				"{\"id\":1,\"firstName\":\"Oliver\"" + ",\"lastName\":\"Gierke\"");
//		assertThat(entity.getBody()).contains("_links\":{\"self\":{\"href\"");
//	}
//
//
//	@Test
//	public void producesJsonWhenXmlIsPreferred() throws Exception {
//		HttpHeaders headers = new HttpHeaders();
//		headers.set(HttpHeaders.ACCEPT, "application/xml;q=0.9,application/json;q=0.8");
//		HttpEntity<?> request = new HttpEntity<>(headers);
//		ResponseEntity<String> response = this.restTemplate.exchange("/customers/1",
//				HttpMethod.GET, request, String.class);
//		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//		assertThat(response.getHeaders().getContentType())
//				.isEqualTo(MediaType.parseMediaType("application/json;charset=UTF-8"));
//	}

}
