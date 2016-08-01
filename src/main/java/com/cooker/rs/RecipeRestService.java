package com.cooker.rs;

import com.cooker.dao.RecipeDao;
import com.cooker.resource.Images;
import com.cooker.resource.Language;
import com.cooker.resource.Recipe;
import com.cooker.services.RecipeService;
import com.cooker.util.ImageScaling;
import com.cooker.util.MD5;
import com.wordnik.swagger.annotations.*;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by thinhly on 7/8/16.
 */
@Path( "/recipe" )
@Api( value = "/recipe", description = "Manage food recipe" )
public class RecipeRestService {
    private final static Logger logger = LoggerFactory.getLogger(RecipeRestService.class);

    @Autowired private RecipeService recipeService;
    @Autowired private RecipeDao recipeDao;

    @Produces( { MediaType.APPLICATION_JSON } )
    @GET
    @ApiOperation( value = "List all recipes", notes = "List all existed recipes",
            response = Recipe.class, responseContainer = "List")
    public Collection<Recipe> getRecipes(@ApiParam( value = "Page to fetch", required = true )
                                                @QueryParam( "page") @DefaultValue( "1" ) final int page) {
        int pageSize = 10;
        return recipeDao.createQuery().offset(pageSize * (page - 1)).limit(pageSize).asList();
    }

    @Produces( { MediaType.APPLICATION_JSON } )
    @Path( "/{index}" )
    @GET
    @ApiOperation( value = "Get recipe", notes = "Get specified recipe with its index",
            response = Recipe.class )
    @ApiResponses( {
            @ApiResponse( code = 204, message = "Food recipe with such index doesn't exists" )
    } )
    public Recipe getRecipe(@Context final UriInfo uriInfo,
            @ApiParam( value = "Index of recipe to lookup for", required = true )
            @PathParam( "index" ) final int index) {
        return recipeDao.findByIndex(index);
    }

    @Produces( { MediaType.APPLICATION_JSON  } )
    @POST
    @ApiOperation( value = "Create new recipe", responseContainer = "List",
            notes = "Language index is VIETNAM(0), ENGLISH(1), THAI(2), KOREAN(3), JAPAN(4), CHINA(5)" )
    @ApiResponses( {
            @ApiResponse( code = 409, message = "Something wrong... Please inform API owner to check the log" )
    } )
    public Response addRecipe(@Context final UriInfo uriInfo,
                                @ApiParam( value = "Language Index", required = true)
                                    @PathParam("lang") final int langIndex,
                                @ApiParam( value = "Recipe Name", required = true )
                                    @FormParam( "text" ) final String text,
                                @ApiParam( value = "List of ingredients", required = true )
                                    @FormParam( "ingredients" ) final String ingredients,
                                @ApiParam( value = "List of preparations", required = true )
                                    @FormParam( "preparations" ) final String preparations,
                                @ApiParam( value = "List of categories", required = true, allowMultiple = true )
                                    @FormParam( "categories" ) final List<Integer> categories,
                                @ApiParam( value = "Author Email", required = true )
                                    @FormParam( "author" ) final String author
                                ){
        try {
            Recipe recipe = recipeService.create(langIndex, text, ingredients, preparations, categories, author);
            // TODO upload and process the recipe picture
            Images images = new Images();
            String regconizeText = MD5.getMD5(author);
            //BufferedImage originalImage = ImageIO.read();
            //images.setTiny(ImageScaling.resizeTinyImage(originalImage, regconizeText));
            return Response.created( uriInfo.getRequestUriBuilder().path( String.valueOf(recipe.getIndex()))
                    .build() ).build();
        } catch (Exception e) {
            logger.error(e.toString());
            return Response.status(Response.Status.CONFLICT).build();
        }

    }

    @Produces( { MediaType.APPLICATION_JSON  } )
    @Path( "/{index}" )
    @PUT
    @ApiOperation( value = "Update the name, ingredient, preparation of existing recipe with specific language",
            notes = "Update existing recipe", response = Recipe.class )
    @ApiResponses( {
            @ApiResponse( code = 404, message = "Food recipe with such index doesn't exists" )
    } )
    public Response updateRecipeWithLanguage(@Context final UriInfo uriInfo,
                    @ApiParam( value = "Index of recipe to lookup for", required = true )
                    @PathParam( "index" ) final int index,
                    @ApiParam( value = "Language Index", required = true)
                    @QueryParam("lang") final int langIndex,
                    @ApiParam( value = "Recipe Name", required = true )
                    @FormParam( "text" ) final String text,
                    @ApiParam( value = "List of ingredients", required = true )
                    @FormParam( "ingredients" ) final String ingredients,
                    @ApiParam( value = "List of preparations", required = true )
                    @FormParam( "preparations" ) final String preparations,
                    @ApiParam( value = "List of categories", required = true, allowMultiple = true )
                    @FormParam( "categories" ) final List<Integer> categories,
                    @ApiParam( value = "Author Email", required = true )
                    @FormParam( "author" ) final String author){
        final Recipe recipe = recipeDao.findByIndex(index);
        if (recipe == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Datastore ds = recipeDao.getDatastore();
        Query<Recipe> updateQuery = ds.createQuery(Recipe.class).field(Mapper.ID_KEY).equal(recipe.getId());
        UpdateOperations<Recipe> ops;

        if(langIndex >= 0 && langIndex <= Language.values().length) {
            List<String> values = recipe.getName();
            values.set(langIndex, text);
            recipe.setName(values);

            List<String> ingredient = recipe.getIngredients();
            ingredient.set(langIndex, ingredients);
            recipe.setIngredients(ingredient);

            List<String> preparation = recipe.getPreparations();
            preparation.set(langIndex, preparations);
            recipe.setPreparations(preparation);

            ops = ds.createUpdateOperations(Recipe.class).set("name", recipe.getName())
                    .set("ingredients", recipe.getIngredients())
                    .set("preparations", recipe.getPreparations());
            ds.update(updateQuery, ops);
        }
        return Response.created( uriInfo.getRequestUriBuilder().path( String.valueOf(recipe.getIndex()))
                .build() ).build();
    }

    @Produces( { MediaType.APPLICATION_JSON  } )
    @Path( "/{index}/increaseView" )
    @PUT
    @ApiOperation( value = "Increase the recipe's view", notes = "Update existing recipe", response = Recipe.class )
    @ApiResponses( {
            @ApiResponse( code = 404, message = "Food recipe with such index doesn't exists" )
    } )
    public Response updateRecipeView(@Context final UriInfo uriInfo,
                                     @ApiParam( value = "Index of recipe to lookup for", required = true )
                                     @PathParam( "index" ) final int index){
        final Recipe recipe = recipeDao.findByIndex(index);
        if (recipe == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Datastore ds = recipeDao.getDatastore();
        Query<Recipe> updateQuery = ds.createQuery(Recipe.class).field(Mapper.ID_KEY).equal(recipe.getId());
        UpdateOperations<Recipe> ops;
        ops = ds.createUpdateOperations(Recipe.class).inc("view");
        ds.update(updateQuery, ops);

        return Response.created( uriInfo.getRequestUriBuilder().path( String.valueOf(recipe.getIndex()))
                .build() ).build();
    }

    @Produces( { MediaType.APPLICATION_JSON  } )
    @Path( "/{index}/addCategories" )
    @PUT
    @ApiOperation( value = "Add existing recipe's categories",
            notes = "Update existing recipe's categories. Add more categories.", response = Recipe.class )
    @ApiResponses( {
            @ApiResponse( code = 404, message = "Food recipe with such index doesn't exists" )
    } )
    public Response updateRecipeAddingCategories(@Context final UriInfo uriInfo,
                                     @ApiParam( value = "Index of recipe to lookup for", required = true )
                                     @PathParam( "index" ) final int index,
                                     @ApiParam( value = "List of categories", required = true, allowMultiple = true )
                                     @FormParam( "categories" ) final List<Integer> categories){
        final Recipe recipe = recipeDao.findByIndex(index);
        if (recipe == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Datastore ds = recipeDao.getDatastore();
        Query<Recipe> updateQuery = ds.createQuery(Recipe.class).field(Mapper.ID_KEY).equal(recipe.getId());
        UpdateOperations<Recipe> ops;
        ops = ds.createUpdateOperations(Recipe.class).addAll("categoryIndex", categories, false);
        ds.update(updateQuery, ops);

        return Response.created( uriInfo.getRequestUriBuilder().path( String.valueOf(recipe.getIndex()))
                .build() ).build();
    }

    @Produces( { MediaType.APPLICATION_JSON  } )
    @Path( "/{index}/removeCategories" )
    @PUT
    @ApiOperation( value = "Remove existing recipe's categories",
            notes = "Update existing recipe's categories. Remove more categories.", response = Recipe.class )
    @ApiResponses( {
            @ApiResponse( code = 404, message = "Food recipe with such index doesn't exists" )
    } )
    public Response updateRecipeRemovingCategories(@Context final UriInfo uriInfo,
                                                 @ApiParam( value = "Index of recipe to lookup for", required = true )
                                                 @PathParam( "index" ) final int index,
                                                 @ApiParam( value = "List of categories", required = true, allowMultiple = true )
                                                 @FormParam( "categories" ) final List<Integer> categories){
        final Recipe recipe = recipeDao.findByIndex(index);
        if (recipe == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Datastore ds = recipeDao.getDatastore();
        Query<Recipe> updateQuery = ds.createQuery(Recipe.class).field(Mapper.ID_KEY).equal(recipe.getId());
        UpdateOperations<Recipe> ops;
        ops = ds.createUpdateOperations(Recipe.class).removeAll("categoryIndex", categories);
        ds.update(updateQuery, ops);

        return Response.created( uriInfo.getRequestUriBuilder().path( String.valueOf(recipe.getIndex()))
                .build() ).build();
    }

    @Produces( { MediaType.APPLICATION_JSON  } )
    @Path( "/{index}/removeAllCategories" )
    @PUT
    @ApiOperation( value = "Remove existing recipe's categories",
            notes = "Update existing recipe's categories. Remove more categories.", response = Recipe.class )
    @ApiResponses( {
            @ApiResponse( code = 404, message = "Food recipe with such index doesn't exists" )
    } )
    public Response updateRecipeRemovingAllCategories(@Context final UriInfo uriInfo,
                                                   @ApiParam( value = "Index of recipe to lookup for", required = true )
                                                   @PathParam( "index" ) final int index){
        final Recipe recipe = recipeDao.findByIndex(index);
        if (recipe == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Datastore ds = recipeDao.getDatastore();
        Query<Recipe> updateQuery = ds.createQuery(Recipe.class).field(Mapper.ID_KEY).equal(recipe.getId());
        UpdateOperations<Recipe> ops;
        ops = ds.createUpdateOperations(Recipe.class).set("categoryIndex", new ArrayList<Integer>());
        ds.update(updateQuery, ops);

        return Response.created( uriInfo.getRequestUriBuilder().path( String.valueOf(recipe.getIndex()))
                .build() ).build();
    }

    @Path( "/{index}" )
    @DELETE
    @ApiOperation( value = "Delete existing recipe", notes = "Delete existing recipe", response = Recipe.class )
    @ApiResponses( {
            @ApiResponse( code = 404, message = "This recipe doesn't exists" )
    } )
    public Response deletePerson( @ApiParam( value = "Index", required = true ) @PathParam( "index" ) final int index ) {
        final Recipe recipe = recipeDao.findByIndex(index);
        if (recipe == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        recipeDao.delete(recipe);
        return Response.ok().build();
    }
}
