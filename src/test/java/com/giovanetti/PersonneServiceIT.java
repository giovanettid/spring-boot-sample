package com.giovanetti;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"}) //random unassigned port
public class PersonneServiceIT {

    @Value("${local.server.port}") //actual random port
    private int port;

    @Inject
    private PersonneRepository personneRepository;

    private String baseUrl = "http://localhost:";
    private RestTemplate template;

    @Before
    public void setUp() {
        this.baseUrl = "http://localhost:" + port;
        template = new TestRestTemplate();
        personneRepository.deleteAll();
        personneRepository.save(new Personne("prenom1","nom1"));
        personneRepository.save(new Personne("prenom2","nom2"));
    }

    @Test
    public void findByNom() {
        List<Personne> response = Arrays.asList(template.getForObject(UriBuilder.
                fromUri(baseUrl).path("/personnes/search").queryParam("nom", "nom1").build(), Personne[].class));

        assertThat(response).hasSize(1).extracting("nom", "prenom").contains(tuple("nom1", "prenom1"));
    }

    @Test
    public void findByNom_MinSizeViolation() {
        ResponseEntity<ArrayList> response = template.getForEntity(UriBuilder.
                fromUri(baseUrl).path("/personnes/search").queryParam("nom", "x").build(), ArrayList.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasSize(1).hasOnlyElementsOfType(Map.class);
        assertThat((Map)response.getBody().get(0)).containsEntry("messageTemplate", "{javax.validation.constraints.Size.message}");
    }

    @Test
    public void postPersonne() {
        ResponseEntity response = template.postForEntity(UriBuilder.
                fromUri(baseUrl).path("/personnes").build(), new Personne("prenom3", "nom3"),null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(personneRepository.findAll()).hasSize(3).extracting("nom", "prenom").contains(tuple("nom3", "prenom3"));
    }

    @Test
    public void postPersonne_MinSizeViolation() {
        ResponseEntity<ArrayList> response = template.postForEntity(UriBuilder.
                fromUri(baseUrl).path("/personnes").build(), new Personne("prenom3", "x"), ArrayList.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasSize(1).hasOnlyElementsOfType(Map.class);
        assertThat((Map)response.getBody().get(0)).containsEntry("messageTemplate",
                "{javax.validation.constraints.Size.message}");
    }

}
