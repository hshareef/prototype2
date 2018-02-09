package com.prototype.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.prototype.model.Claim;
import com.prototype.model.Person;
import com.prototype.model.PotentialName;

public class QuickSearchDao {

	public List<Person> getSearchedPeople(List<PotentialName> names) {
		String queryString = getQuery(names);
		SessionFactory sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.createSQLQuery(queryString).addEntity(Person.class);
		
		List<Person> potentialPersons = query.list();
//		for(Person person : potentialPersons){
//			person.setInstitution(new Institution();
//		}
		session.getTransaction().commit();
		session.close();
		sessionFactory.close();
		return potentialPersons;
	}
	
	public String getQuery(List<PotentialName> names){
		StringBuffer query = new StringBuffer();
		query.append("select * from person where ");
		for(int i=0; i<names.size(); i++){
			if(names.get(i).getFirstName() != null && names.get(i).getLastName() != null){
				query.append("(first_name like " + names.get(i).getFirstName() + "  and last_name like " + names.get(i).getLastName() + ")");
			}
			else if (names.get(i).getFirstName() != null){
				query.append("( first_name like " + names.get(i).getFirstName() + " ) ");
			}
			else if(names.get(i).getLastName() != null){
				query.append("( last_name like " + names.get(i).getLastName() + " ) " );
			}
			if(i < names.size()-1){
				query.append(" OR ");
			}
		}
		query.append(" limit 30");
		return query.toString();
	}

	public void getSearchedEntities(String table, List<String> param1, String col1, List<String> param2, String col2) {
		StringBuffer query = new StringBuffer();
		String param1AsString = convertListToString(param1, ",");
		String param2AsString = convertListToString(param2, ",");
		query.append("select * from " + table + " where " + col1 + " like " + param1AsString);
	}
	
	public String convertListToString(List<String> listItems, String delimiter){
		StringBuffer asString = new StringBuffer();
		for (int i = 0; i < listItems.size(); i++){
			asString.append(listItems.get(i));
			if(i != listItems.size()-1){
				asString.append(delimiter);
			}
		}
		return asString.toString();
	}
	
	public String convertListToLikeOrString(List<String> listItems){
		StringBuffer asString = new StringBuffer();
		for (int i = 0; i < listItems.size(); i++){
			asString.append("%" + listItems.get(i) + "%");
			if(i != listItems.size()-1){
				asString.append(",");
			}
		}
		return asString.toString();
	}
}

//select * from person where first_name like %martin% or %martin van%