/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satb.controller;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import satb.behavior.ActivityRecognition;
import satb.behavior.Observation;
import satb.model.CoordinateVenus;
import satb.model.dao.CollarDataDAO;

/**
 *
 * @author Leandro
 */
public class ActivityRecognitionController {
    
    CollarDataDAO collarDataDAO;
    
    public ActivityRecognitionController () {
        collarDataDAO = new CollarDataDAO();
    }
    
    
    public void  trainClassifier () {
        
        long tempoInicial = System.currentTimeMillis();
        
        LinkedList<CoordinateVenus> listCoordinate = collarDataDAO.selectAllDataVenus();
        
        System.out.println("listCoordinate (millis) = " + (System.currentTimeMillis() - tempoInicial));
        tempoInicial = System.currentTimeMillis();
                
        LinkedList<Observation> listObservations = collarDataDAO.selectAllObservation();
        
        System.out.println("listObservations (millis) = " + (System.currentTimeMillis() - tempoInicial));
        tempoInicial = System.currentTimeMillis();
        
        try {
            
            new ActivityRecognition(listCoordinate, listObservations);
            
            System.out.println("ActivityRecognition (millis) = " + (System.currentTimeMillis() - tempoInicial));
        } catch (Exception ex) {
            Logger.getLogger(ActivityRecognitionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void  trainClassifier (Integer degreesForSameDirectionA, Integer historyLengthA, Double minSpeedA, Double segmentSecondsA,
            Integer degreesForSameDirectionB, Integer historyLengthB, Double minSpeedB, Double segmentSecondsB) {
        
        LinkedList<CoordinateVenus> listCoordinate = collarDataDAO.selectAllDataVenus();
        LinkedList<Observation> listObservations = collarDataDAO.selectAllObservation();
        
        try {
            
            new ActivityRecognition(listCoordinate, listObservations, degreesForSameDirectionA, degreesForSameDirectionB, historyLengthA, historyLengthB, minSpeedA, minSpeedB, segmentSecondsA, segmentSecondsB);

        } catch (Exception ex) {
            Logger.getLogger(ActivityRecognitionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
