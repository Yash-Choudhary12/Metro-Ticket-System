package com.metro.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "scanned_tickets")
public class ScannedTicket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scan_id")
	private int scanId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ticket_id", nullable = false)
	private Ticket ticket;

	@Column(name = "scan_time", nullable = false)
	private Timestamp scanTime;

	public ScannedTicket() {
	}

	public ScannedTicket(Ticket ticket) {
		this.ticket = ticket;
		this.scanTime = new Timestamp(System.currentTimeMillis());
	}

	public int getScanId() {
		return scanId;
	}

	public void setScanId(int scanId) {
		this.scanId = scanId;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Timestamp getScanTime() {
		return scanTime;
	}

	public void setScanTime(Timestamp scanTime) {
		this.scanTime = scanTime;
	}
}
