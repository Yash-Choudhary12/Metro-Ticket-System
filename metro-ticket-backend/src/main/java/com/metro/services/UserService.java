package com.metro.services;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.metro.config.HibernateUtil;
import com.metro.models.User;

public class UserService {

	public void saveUser(User user) {
	    Transaction transaction = null;
	    Session session = null;

	    try {
	        session = HibernateUtil.getSessionFactory().openSession();
	        transaction = session.beginTransaction();
	        session.save(user);
	        transaction.commit();
	    } catch (Exception e) {
	        if (transaction != null && transaction.isActive()) {
	            transaction.rollback();
	        }
	        throw new RuntimeException("Error saving user: " + e.getMessage(), e);
	    } finally {
	        if (session != null && session.isOpen()) {
	            session.close();
	        }
	    }
	}


}
