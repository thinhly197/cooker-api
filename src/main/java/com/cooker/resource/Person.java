package com.cooker.resource;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexDirection;

@Entity
@ApiModel( value = "Person", description = "Person resource representation" )
public class Person {
    @Id private ObjectId id;
	@Property @ApiModelProperty( value = "Person's first name", required = true ) private String firstName;
    @Property @ApiModelProperty( value = "Person's middle name", required = false ) private String middleName;
    @Property @ApiModelProperty( value = "Person's last name", required = true ) private String lastName;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @Indexed(value= IndexDirection.ASC, name="person_email_indx", background=true, unique=true,
            dropDups=true, sparse = false, expireAfterSeconds = -1)
    @Property @ApiModelProperty( value = "Person's e-mail address", required = true ) private String email;

    @Property @ApiModelProperty( value = "Person's password (MD5)", required = true ) private String password;
    @Property @ApiModelProperty( value = "Person's role, it can be User/Cooker", required = true ) private String role;

	public Person() {
	}
	
	public Person( final String email ) {
		this.email = email;
	}

    public Person(String firstName, String middleName, String lastName, String email, String password, String role) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Person(final String firstName, final String lastName ) {

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void setMiddleName(String middleName) {

        this.middleName = middleName;
    }

    public String getPassword() {

        return password;
    }

    public String getMiddleName() {

        return middleName;
    }

	public String getEmail() {
		return email;
	}
		
	public void setEmail( final String email ) {
		this.email = email;
	}
		
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
		
	public void setFirstName( final String firstName ) {
		this.firstName = firstName;
	}
		
	public void setLastName( final String lastName ) {
		this.lastName = lastName;
	}		
}
