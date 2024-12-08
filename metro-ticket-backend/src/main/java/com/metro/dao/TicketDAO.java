package com.metro.dao;

import com.metro.config.HibernateUtil;
import com.metro.models.Ticket;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class TicketDAO {

	public Ticket save(Ticket ticket) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(ticket);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ticket;
	}

	public Ticket saveOrUpdate(Ticket ticket) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			ticket = (Ticket) session.merge(ticket);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ticket;
	}

	public List<Ticket> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Ticket> tickets = null;
		try {
			tickets = session.createQuery("FROM Ticket", Ticket.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return tickets;
	}

	public Ticket findById(int ticketId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Ticket ticket = null;
		try {
			ticket = session.get(Ticket.class, ticketId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ticket;
	}

	public void delete(int ticketId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Ticket ticket = session.get(Ticket.class, ticketId);
			if (ticket != null) {
				session.remove(ticket);
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

	public Ticket findByQrCode(String qrCode) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		try {
			Query<Ticket> query = session.createQuery("FROM Ticket WHERE qrCode = :qrCode", Ticket.class);
			query.setParameter("qrCode", qrCode);
			return query.uniqueResult();
		} finally {
			session.close();
		}
	}
}
