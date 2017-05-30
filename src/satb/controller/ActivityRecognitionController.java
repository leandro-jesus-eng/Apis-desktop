/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satb.controller;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import satb.behavior.ActivityDataStructure;
import satb.behavior.ActivityRecognition;
import satb.behavior.MovementDataStructure;
import satb.behavior.Observation;
import satb.behavior.SegmentDataStructure;
import satb.behavior.WekaTest;
import satb.model.CoordinateVenus;
import satb.model.dao.CollarDataDAO;
import weka.core.Instances;

/**
 *
 * @author Leandro
 */
public class ActivityRecognitionController {

    CollarDataDAO collarDataDAO;

    private Hashtable<String, LinkedList<CoordinateVenus>> hashCoordinateCollar = new Hashtable(15);
    private Hashtable<String, LinkedList<Observation>> hashObservationCollar = new Hashtable(15);

    public ActivityRecognitionController() {
        collarDataDAO = new CollarDataDAO();
    }

    public void trainClassifier() {

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

    public void trainClassifier(Integer degreesForSameDirectionA, Integer historyLengthA, Double minSpeedA, Double segmentSecondsA,
            Integer degreesForSameDirectionB, Integer historyLengthB, Double minSpeedB, Double segmentSecondsB) {

        LinkedList<CoordinateVenus> listCoordinate = collarDataDAO.selectAllDataVenus();
        LinkedList<Observation> listObservations = collarDataDAO.selectAllObservation();

        try {

            new ActivityRecognition(listCoordinate, listObservations, degreesForSameDirectionA, degreesForSameDirectionB, historyLengthA, historyLengthB, minSpeedA, minSpeedB, segmentSecondsA, segmentSecondsB);

        } catch (Exception ex) {
            Logger.getLogger(ActivityRecognitionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void trainClassifierLomba(Integer degreesForSameDirectionA, Integer historyLengthA, Double minSpeedA, Double segmentSecondsA,
            Integer degreesForSameDirectionB, Integer historyLengthB, Double minSpeedB, Double segmentSecondsB) {

        ActivityRecognition ar = new ActivityRecognition();

        boolean cont = true;

        for (ar.minSpeed = minSpeedA; ar.minSpeed <= minSpeedB; ar.minSpeed += 0.1) {
            for (ar.historyLength = historyLengthA; ar.historyLength <= historyLengthB; ar.historyLength += 1) {
                for (ar.degreesForSameDirection = degreesForSameDirectionA; ar.degreesForSameDirection <= degreesForSameDirectionB; ar.degreesForSameDirection += 10) {// heading threshold
                    for (ar.segmentSeconds = segmentSecondsA; ar.segmentSeconds <= segmentSecondsB; ar.segmentSeconds += 10) {

                        if (cont) {
                            ar.minSpeed = 0.1;
                            ar.degreesForSameDirection = 40;
                            ar.historyLength = 4;
                            ar.segmentSeconds = 160.0;
                            cont = false;
                        }

                        System.out.println("minSpeed \t\t=" + ar.minSpeed);
                        System.out.println("degreesForSameDirection \t\t=" + ar.degreesForSameDirection);
                        System.out.println("historyLength \t\t=" + ar.historyLength);
                        System.out.println("segmentSeconds \t\t=" + ar.segmentSeconds);

                        //LinkedList<CoordinateVenus> listCoordinate;
                        //LinkedList<Observation> listObservations;        
                        LinkedList<ActivityDataStructure> listADS = new LinkedList<>();

                        String collar;

                        collar = "00A2";
                        System.out.println(collar);
                        listADS.addAll(createActivityDataStructureCollar(collar, ar));

                        collar = "00A3";
                        System.out.println(collar);
                        listADS.addAll(createActivityDataStructureCollar(collar, ar));

                        collar = "00B2";
                        System.out.println(collar);
                        listADS.addAll(createActivityDataStructureCollar(collar, ar));

                        collar = "00B3";
                        System.out.println(collar);
                        listADS.addAll(createActivityDataStructureCollar(collar, ar));

                        collar = "00C3";
                        System.out.println(collar);
                        listADS.addAll(createActivityDataStructureCollar(collar, ar));

                        collar = "00C4";
                        System.out.println(collar);
                        listADS.addAll(createActivityDataStructureCollar(collar, ar));

                        collar = "00D1";
                        System.out.println(collar);
                        listADS.addAll(createActivityDataStructureCollar(collar, ar));

                        collar = "00D2";
                        System.out.println(collar);
                        listADS.addAll(createActivityDataStructureCollar(collar, ar));

                        collar = "00D3";
                        System.out.println(collar);
                        listADS.addAll(createActivityDataStructureCollar(collar, ar));

                        collar = "00D4";
                        System.out.println(collar);
                        listADS.addAll(createActivityDataStructureCollar(collar, ar));

                        Instances data = ar.createARFFData(listADS);

                        /*for(int i=0; i<data.numInstances(); i++) {
                         if ( data.instance(i).stringValue( data.classAttribute() ).equals("Bebendo") ) {
                         data.instance(i).setClassValue("BebendoÁgua");
                         }                       
                         if ( data.instance(i).stringValue( data.classAttribute() ).equals("EmPe") ) {
                         data.instance(i).setClassValue("Deitado");
                         }            
                         }*/
                        WekaTest wt = new WekaTest();
                        try {
                            wt.go2(data, "configuração padrão classe");
                        } catch (Exception ex) {
                            Logger.getLogger(ActivityRecognitionController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
            }
        }
    }

    protected LinkedList<ActivityDataStructure> createActivityDataStructureCollar(String collar, ActivityRecognition ar) {

        LinkedList<CoordinateVenus> listCoordinate;
        LinkedList<Observation> listObservations;

        try {

            if (hashCoordinateCollar.containsKey(collar)) {
                listCoordinate = hashCoordinateCollar.get(collar);
            } else {
                listCoordinate = collarDataDAO.selectDataVenus(collar);
                hashCoordinateCollar.put(collar, listCoordinate);
            }

            if (hashObservationCollar.containsKey(collar)) {
                listObservations = hashObservationCollar.get(collar);
            } else {
                listObservations = collarDataDAO.selectObservation(collar);
                hashObservationCollar.put(collar, listObservations);
            }

            ar.setListCoordinate(listCoordinate);
            ar.setListObservations(listObservations);

            LinkedList<MovementDataStructure> listMDS = ar.doMovementAnalyzer();
            LinkedList<SegmentDataStructure> listSDS = ar.doSegmentAnalyzer(listMDS);
            LinkedList<ActivityDataStructure> listADS = ar.doActivityAnalyzer(listSDS);

            return listADS;

        } catch (Exception ex) {
            Logger.getLogger(ActivityRecognitionController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
