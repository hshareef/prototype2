package com.prototype.service;

import com.prototype.dao.InstitutionDao;
import com.prototype.model.Institution;

public class InstitutionService {
	
	InstitutionDao institutionDao = new InstitutionDao();

	public Institution saveInstitution(Institution institution) {
		institution = institutionDao.saveInstitution(institution);
		return institution;
	}

}
