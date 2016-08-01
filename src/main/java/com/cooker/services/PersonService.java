package com.cooker.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.cooker.dao.FavouriteDao;
import com.cooker.dao.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cooker.exceptions.PersonAlreadyExistsException;
import com.cooker.exceptions.PersonNotFoundException;
import com.cooker.resource.Person;

@Service
public class PersonService {
	@Autowired private PersonDao personDao;
	@Autowired private FavouriteDao favouriteDao;

	public boolean login(String email, String password){
		Person person = personDao.findByEmail(email);
		if(!person.getPassword().equals(password)) {
			return false;
		}
		return true;
	}

	public void addFavourite(String email, long recipeIndex) {
		favouriteDao.add(email, recipeIndex);
	}
}
