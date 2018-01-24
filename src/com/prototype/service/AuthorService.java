package com.prototype.service;

import com.prototype.dao.AuthorDao;
import com.prototype.model.Author;

public class AuthorService {
	
	//make this a spring bean and autowire it
	AuthorDao authorDao = new AuthorDao();
	
	public Author saveAuthor(Author author){
		return authorDao.saveAuthor(author);
	}

}
