package com.cooker.rs;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.cooker.dao.PersonDao;
import com.cooker.resource.Person;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

@Path( "/person" )
@Api( value = "/person", description = "Manage person (include User and Master roles)" )
public class PersonRestService {
	@Inject private PersonDao personDao;
	
	@Produces( { MediaType.APPLICATION_JSON } )
	@GET
	@ApiOperation( value = "List all people", notes = "List all people using paging. One page is 10 people",
            response = Person.class, responseContainer = "List")
	public Collection< Person > getPerson(  @ApiParam( value = "Page to fetch", required = true )
                                                @QueryParam( "page") @DefaultValue( "1" ) final int page ) {
        int pageSize = 10;
		return personDao.listAllPerson(pageSize * (page - 1), pageSize);
	}

	@Produces( { MediaType.APPLICATION_JSON } )
	@Path( "/{email}" )
	@GET
	@ApiOperation( value = "Find person by e-mail", notes = "Find person by e-mail", response = Person.class )
	@ApiResponses( {
	    @ApiResponse( code = 404, message = "Person with such e-mail doesn't exists" )			 
	} )
	public Person getPerson( @ApiParam( value = "E-Mail address to lookup for", required = true )
                                 @PathParam( "email" ) final String email ) {
		return personDao.findByEmail( email );
	}

	@Produces( { MediaType.APPLICATION_JSON  } )
	@POST
	@ApiOperation( value = "Create new person", notes = "Create new person" )
	@ApiResponses( {
	    @ApiResponse( code = 201, message = "Person created successfully" ),
	    @ApiResponse( code = 409, message = "Person with such e-mail already exists" )
	} )
	public Response addPerson( @Context final UriInfo uriInfo,
			@ApiParam( value = "E-Mail", required = true ) @FormParam( "email" ) final String email,
            @ApiParam( value = "Password", required = true ) @FormParam( "password" ) final String password,
			@ApiParam( value = "First Name", required = true ) @FormParam( "firstName" ) final String firstName,
            @ApiParam( value = "Middle Name", required = false ) @FormParam( "middleName" ) final String middleName,
			@ApiParam( value = "Last Name", required = false ) @FormParam( "lastName" ) final String lastName,
            @ApiParam( value = "Role", required = true ) @FormParam( "role" ) final String role ) {
        Person p = personDao.findByEmail(email);
        if(p == null) {
            p = new Person(firstName, middleName, lastName, email, password, role);
            personDao.save(p);
            return Response.created( uriInfo.getRequestUriBuilder().path( email ).build() ).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
	}
	
	@Produces( { MediaType.APPLICATION_JSON  } )
	@Path( "/{email}" )
	@PUT
	@ApiOperation( value = "Update existing person", notes = "Update existing person", response = Person.class )
	@ApiResponses( {
	    @ApiResponse( code = 404, message = "Person with such e-mail doesn't exists" )			 
	} )
	public Person updatePerson(
            @ApiParam( value = "E-Mail", required = true ) @PathParam( "email" ) final String email,
            @ApiParam( value = "Password", required = false ) @FormParam( "password" ) final String password,
            @ApiParam( value = "First Name", required = false ) @FormParam( "firstName" ) final String firstName,
            @ApiParam( value = "Middle Name", required = false ) @FormParam( "middleName" ) final String middleName,
            @ApiParam( value = "Last Name", required = false ) @FormParam( "lastName" ) final String lastName,
            @ApiParam( value = "Role (user/master)", required = false ) @FormParam( "role" ) final String role ) {
		
		final Person person = personDao.findByEmail(email);
        if(person == null) {
            return null;
        }

        Datastore ds = personDao.getDatastore();
        Query<Person> updateQuery = ds.createQuery(Person.class).field(Mapper.ID_KEY).equal(person.getId());
        UpdateOperations<Person> ops;

		if( firstName != null ) {
			person.setFirstName( firstName );
            ops = ds.createUpdateOperations(Person.class).set("firstName", person.getFirstName());
            ds.update(updateQuery, ops);
		}
        if( middleName != null ) {
            person.setMiddleName( middleName );
            ops = ds.createUpdateOperations(Person.class).set("middleName", person.getMiddleName());
            ds.update(updateQuery, ops);
        }
		if( lastName != null ) {
			person.setLastName( lastName );
            ops = ds.createUpdateOperations(Person.class).set("lastName", person.getLastName());
            ds.update(updateQuery, ops);
		}
        if( role != null ) {
            person.setRole(role);
            ops = ds.createUpdateOperations(Person.class).set("role", person.getRole());
            ds.update(updateQuery, ops);
        }
        if( password != null ) {
            person.setPassword( password );
            ops = ds.createUpdateOperations(Person.class).set("password", person.getPassword());
            ds.update(updateQuery, ops);
        }

		return person; 				
	}
	
	@Path( "/{email}" )
	@DELETE
	@ApiOperation( value = "Delete existing person", notes = "Delete existing person", response = Person.class )
	@ApiResponses( {
	    @ApiResponse( code = 404, message = "Person with such e-mail doesn't exists" )			 
	} )
	public Response deletePerson( @ApiParam( value = "E-Mail", required = true ) @PathParam( "email" ) final String email ) {
        final Person person = personDao.findByEmail(email);
        personDao.delete(person);
		return Response.ok().build();
	}

}
