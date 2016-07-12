package com.cooker.config;

import java.util.Arrays;

import javax.ws.rs.ext.RuntimeDelegate;

import com.cooker.dao.FoodCategoryDao;
import com.cooker.dao.PersonDao;
import com.cooker.resource.FoodCategory;
import com.cooker.rs.FoodCategoryRestService;
import com.cooker.util.MongoDBConnector;
import com.mongodb.MongoClient;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import com.cooker.resource.Person;
import com.cooker.rs.JaxRsApiApplication;
import com.cooker.rs.PersonRestService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;

@Configuration
public class AppConfig {
	public static final String SERVER_PORT = "server.port";
	public static final String SERVER_HOST = "server.host";
	public static final String CONTEXT_PATH = "context.path";

	public static final String DB_NAME = "cooker";
	private static final MongoClient mongo = MongoDBConnector.getMongo();

	@Bean( destroyMethod = "shutdown" )
	public SpringBus cxf() {
		return new SpringBus();
	}
	
	@Bean @DependsOn( "cxf" )
	public Server jaxRsServer() {
		JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance()
				.createEndpoint( jaxRsApiApplication(), JAXRSServerFactoryBean.class );
		factory.setServiceBeans(
				Arrays.< Object >asList(
						personRestService(),
						foodCategoriesRestService(),
						apiListingResourceJson() ) );
		factory.setAddress( factory.getAddress() );
		factory.setProviders( Arrays.< Object >asList( jsonProvider(), resourceListingProvider(), apiDeclarationProvider() ) );
		return factory.create();
	}
	
	@Bean @Autowired
	public BeanConfig swaggerConfig( Environment environment ) {
		final BeanConfig config = new BeanConfig();

		config.setVersion( "1.0.0" );
		config.setScan( true );
		config.setResourcePackage( Person.class.getPackage().getName() );
		config.setBasePath( 
			String.format( "http://%s:%s/%s%s",
				environment.getProperty( SERVER_HOST ),
				environment.getProperty( SERVER_PORT ),
				environment.getProperty( CONTEXT_PATH ),
				jaxRsServer().getEndpoint().getEndpointInfo().getAddress() 
			) 
		);
		
		return config;
	}

	@Bean
	public ApiDeclarationProvider apiDeclarationProvider() {
		return new ApiDeclarationProvider();
	}
	
	@Bean
	public ApiListingResourceJSON apiListingResourceJson() {
		return new ApiListingResourceJSON();
	}
	
	@Bean
	public ResourceListingProvider resourceListingProvider() {
		return new ResourceListingProvider();
	}
	
	@Bean 
	public JaxRsApiApplication jaxRsApiApplication() {
		return new JaxRsApiApplication();
	}
	
	@Bean 
	public PersonRestService personRestService() {
		return new PersonRestService();
	}
	
	@Bean 
	public PersonDao personDao() {
		Morphia morphia = new Morphia();
		morphia.map(Person.class);
		return new PersonDao(mongo, morphia, DB_NAME);
	}

	@Bean
	public FoodCategoryRestService foodCategoriesRestService() {
		return new FoodCategoryRestService();
	}

	@Bean
	public FoodCategoryDao foodCategoriesDao() {
		Morphia morphia = new Morphia();
		morphia.map(FoodCategory.class);
		return new FoodCategoryDao(mongo, morphia, DB_NAME);
	}
		
	@Bean
	public JacksonJsonProvider jsonProvider() {
		return new JacksonJsonProvider();
	}
}
