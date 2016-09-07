package com.rbc.pet;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.wadl.internal.WadlResource;
import org.springframework.stereotype.Component;

import com.rbc.pet.endpoint.PetEndpoint;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;

@ApplicationPath("/api")
@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
        registerEndpoints();
        //configureSwagger();
    }
	
    private void registerEndpoints() {
    	register(WadlResource.class);
    	register(PetEndpoint.class);
    	//register(ApiListingResource.class);
		//register(SwaggerSerializers.class);
    }
    
    private void configureSwagger() {
    	register(ApiListingResource.class);
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.2");
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setHost("localhost:8080");
		beanConfig.setBasePath("/api");
		beanConfig.setResourcePackage("com.rbc.pet");
		beanConfig.setPrettyPrint(true);
		beanConfig.setScan(true);
    }
}
