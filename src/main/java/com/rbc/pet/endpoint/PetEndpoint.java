package com.rbc.pet.endpoint;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.rbc.pet.domain.Pet;
import com.rbc.pet.service.PetService;

//@Api(value = "/api")
@Component
@Path("/pet")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PetEndpoint {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private PetService petService;

	@Autowired
	public PetEndpoint(PetService petService) {
		this.petService = petService;
	}

	@POST
	public Pet create(Pet pet) {
		return petService.create(pet);
	}

	@GET
	@Path("/{petId}")
	public Pet read(@PathParam("petId") Integer petId) {
		return petService.read(petId);
	}

	@DELETE
	@Path("/{petId}")
	public void delete(@PathParam("petId") Integer petId) {
		petService.delete(petId);
	}

	@GET
	public List<Pet> readAll() {
		return petService.readAll();
	}
	
	@GET
	@Path("/healthcheck")
	public String healthCheck() {
		logger.info("........ Hello!!! ......");
		return "Hello!!!";
	}
}
