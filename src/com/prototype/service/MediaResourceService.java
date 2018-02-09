package com.prototype.service;

import com.prototype.dao.MediaResourceDao;
import com.prototype.model.MediaResource;

public class MediaResourceService {
	
	//autowire later
	MediaResourceDao mediaResourceDao = new MediaResourceDao();

	public MediaResource saveMediaResource(MediaResource mediaResource) {
		// TODO Auto-generated method stub
		return mediaResourceDao.saveMediaResource(mediaResource);
	}

}
