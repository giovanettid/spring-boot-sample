package com.giovanetti;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/personnes")
@Produces({MediaType.APPLICATION_JSON})
public class PersonneService {

    @Inject
    private PersonneRepository personneRepository;

    @GET
    @Path("/search")
    public Iterable<Personne> findPersonnesByNom(@Size(min=2) @QueryParam("nom") String nom) {
        return personneRepository.findPersonneByNom(nom);
    }

    @POST
    public Response savePersonne(@Valid Personne personne) {
        personneRepository.save(personne);
        return Response.status(Response.Status.CREATED).build();
    }

}
