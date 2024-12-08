package com.metro.dao;

import com.metro.config.HibernateUtil;
import com.metro.models.ScannedTicket;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ScannedTicketDAO {

	public ScannedTicket save(ScannedTicket scannedTicket) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(scannedTicket);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return scannedTicket;
	}

	public ScannedTicket saveOrUpdate(ScannedTicket scannedTicket) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			scannedTicket = (ScannedTicket) session.merge(scannedTicket);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return scannedTicket;
	}

	public List<ScannedTicket> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<ScannedTicket> scannedTickets = null;
		try {
			scannedTickets = session.createQuery("FROM ScannedTicket", ScannedTicket.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return scannedTickets;
	}

	// Find a scanned ticket by its ID
	public ScannedTicket findById(int scanId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		ScannedTicket scannedTicket = null;
		try {
			scannedTicket = session.get(ScannedTicket.class, scanId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return scannedTicket;
	}

	public void delete(int scanId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			ScannedTicket scannedTicket = session.get(ScannedTicket.class, scanId);
			if (scannedTicket != null) {
				session.remove(scannedTicket);
			}
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public List<ScannedTicket> findByTicketId(int ticketId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<ScannedTicket> scannedTickets = null;
		try {
			Query<ScannedTicket> query = session.createQuery("FROM ScannedTicket WHERE ticket_id = :ticketId",
					ScannedTicket.class);
			query.setParameter("ticketId", ticketId);
			scannedTickets = query.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return scannedTickets;
	}

}
