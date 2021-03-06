package com.giovanetti;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) //random unassigned port
public class PersonneServiceIT {

    @Inject
    private PersonneRepository personneRepository;

    @Inject
    private TestRestTemplate template;

    @BeforeEach
    public void setUp() {
        personneRepository.deleteAll();
        personneRepository.save(new Personne("prenom1","nom1"));
        personneRepository.save(new Personne("prenom2","nom2"));
    }

    @Test
    public void findByNom() {
        List<Personne> response = Arrays.asList(template.getForObject(UriBuilder.
                fromPath("/personnes/search").queryParam("nom", "nom1").build(), Personne[].class));

        assertThat(response).hasSize(1).extracting("nom", "prenom")
                .contains(tuple("nom1", "prenom1"));
    }

    @Test
    public void findByNom_MinSizeViolation() {
        ResponseEntity<ArrayList> response = template.getForEntity(UriBuilder.
                fromPath("/personnes/search").queryParam("nom", "x").build(), ArrayList.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        checkSizeViolation(response.getBody());
    }


    @Test
    public void postPersonne() {
        ResponseEntity response = template.postForEntity(UriBuilder.
                fromPath("/personnes").build(), new Personne("prenom3", "nom3"), null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(personneRepository.findAll()).hasSize(3)
                .extracting("nom", "prenom")
                .contains(tuple("nom3", "prenom3"));
    }

    @Test
    public void postPersonne_MinSizeViolation() {
        ResponseEntity<ArrayList> response = template.postForEntity(UriBuilder.
                fromPath("/personnes").build(), new Personne("prenom3", "x"), ArrayList.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        checkSizeViolation(response.getBody());
    }

    private void checkSizeViolation(ArrayList<Map> body) {
        assertThat(body).hasSize(1).hasOnlyElementsOfType(Map.class);
        assertThat(body.get(0)).containsEntry("messageTemplate",
                "{javax.validation.constraints.Size.message}");
    }

}
