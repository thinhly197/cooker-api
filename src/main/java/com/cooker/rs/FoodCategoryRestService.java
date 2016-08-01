package com.cooker.rs;

import com.cooker.dao.FoodCategoryDao;
import com.cooker.resource.FoodCategory;
import com.cooker.resource.Language;
import com.wordnik.swagger.annotations.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.List;

/**
 * Created by thinhly on 7/8/16.
 */
@Path( "/category" )
@Api( value = "/category", description = "Manage food categories" )
public class FoodCategoryRestService {
    private final static Logger logger = LoggerFactory.getLogger(FoodCategoryRestService.class);
    @Autowired private FoodCategoryDao categoriesDao;

    @Produces( { MediaType.APPLICATION_JSON } )
    @GET
    @ApiOperation( value = "List all categories", notes = "List all existed categories",
            response = FoodCategory.class, responseContainer = "List")
    public Collection<FoodCategory> getCategories() {
        return categoriesDao.find().asList();
    }

    @Produces( { MediaType.APPLICATION_JSON } )
    @Path( "/{index}" )
    @GET
    @ApiOperation( value = "Get category", notes = "Get specified category with its index",
            response = FoodCategory.class )
    @ApiResponses( {
            @ApiResponse( code = 204, message = "Food category with such index doesn't exists" )
    } )
    public FoodCategory getCategory(@Context final UriInfo uriInfo,
            @ApiParam( value = "Index of category to lookup for", required = true )
            @PathParam( "index" ) final int index) {
        return categoriesDao.findByIndex(index);
    }

    @Produces( { MediaType.APPLICATION_JSON  } )
    @POST
    @ApiOperation( value = "Create new category",
            notes = "Language index is VIETNAM(0), ENGLISH(1), THAI(2), KOREAN(3), JAPAN(4), CHINA(5)" )
    @ApiResponses( {
            @ApiResponse( code = 409, message = "Something wrong... Please inform API owner to check the log" )
    } )
    public Response addCategory(@Context final UriInfo uriInfo,
                                @ApiParam( value = "Category Text", required = true )
                                    @FormParam( "text" ) final String text,
                                @ApiParam( value = "Language Index", required = true)
                                    @PathParam("lang") final int langIndex){
        try {
            FoodCategory category = categoriesDao.addByLanguage(langIndex, text);
            return Response.created(uriInfo.getRequestUriBuilder().path(String.valueOf(category.getIndex()))
                    .build()).build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @Produces( { MediaType.APPLICATION_JSON  } )
    @Path( "/{index}" )
    @PUT
    @ApiOperation( value = "Update existing category",
            notes = "Language index is VIETNAM(0), ENGLISH(1), THAI(2), KOREAN(3), JAPAN(4), CHINA(5)",
            response = FoodCategory.class )
    @ApiResponses( {
            @ApiResponse( code = 404, message = "Food category with such index doesn't exists" )
    } )
    public Response updateCategory(@Context final UriInfo uriInfo,
            @ApiParam( value = "Index of category to lookup for", required = true ) @PathParam( "index" ) final int index,
            @ApiParam( value = "Category Text", required = true ) @FormParam( "text" ) final String text,
            @ApiParam( value = "Language Index", required = true ) @FormParam( "lang" ) final int langIndex) {

        final FoodCategory category = categoriesDao.findByIndex(index);
        if (category == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Datastore ds = categoriesDao.getDatastore();
        Query<FoodCategory> updateQuery = ds.createQuery(FoodCategory.class).field(Mapper.ID_KEY).equal(category.getId());
        UpdateOperations<FoodCategory> ops;

        if(langIndex >= 0 && langIndex <= Language.values().length) {
            List<String> values = category.getName();
            values.set(langIndex, text);
            category.setName(values);
            ops = ds.createUpdateOperations(FoodCategory.class).set("name", category.getName());
            ds.update(updateQuery, ops);

            categoriesDao.updateByLanguage(langIndex, text, index);
        }

        return Response.created(uriInfo.getRequestUriBuilder().path(String.valueOf(category.getIndex()))
                .build()).build();
    }

    @Path( "/{index}" )
    @DELETE
    @ApiOperation( value = "Delete existing category", notes = "Delete existing category",
            response = FoodCategory.class )
    @ApiResponses( {
            @ApiResponse( code = 404, message = "Category with such index doesn't exists" )
    } )
    public Response deletePerson( @ApiParam( value = "Index", required = true )
                                      @PathParam( "index" ) final int index ) {
        final FoodCategory category = categoriesDao.findByIndex(index);
        if (category == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        categoriesDao.delete(category);
        return Response.ok().build();
    }
}
