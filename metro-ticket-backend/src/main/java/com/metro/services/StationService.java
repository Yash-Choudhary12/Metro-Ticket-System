package com.metro.services;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.metro.models.Station;
import com.metro.config.HibernateUtil;

public class StationService {

    public List<Station> getAllStations() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Station> query = session.createQuery("FROM Station", Station.class);
            return query.list();
        }
    }

    public int calculatePrice(String source, String destination) {
    	List<Station> stations = getAllStations();
        
       
        Station sourceStation = null;
        Station destinationStation = null;
        
        for (Station station : stations) {
            if (station.getStation_name().equals(source)) {
                sourceStation = station;
            }
            if (station.getStation_name().equals(destination)) {
                destinationStation = station;
            }
        }

        if (sourceStation == null || destinationStation == null) {
            throw new IllegalArgumentException("Source or destination station not found");
        }

        int idDifference = Math.abs(sourceStation.getStation_id() - destinationStation.getStation_id());

        if (idDifference <= 3) {
            return 20;
        } 
        
        else if (idDifference <= 5 && idDifference > 3) {
            return 40; 
        } 
        
        else {
            return 50;
        }
    }
}
