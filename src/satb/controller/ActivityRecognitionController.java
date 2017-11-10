/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satb.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import satb.behavior.ActivityDataStructure;
import satb.behavior.ActivityRecognition;
import satb.behavior.MovementDataStructure;
import satb.behavior.Observation;
import satb.behavior.ObservationTime;
import satb.behavior.SegmentDataStructure;
import satb.behavior.WekaTest;
import satb.model.CoordinateVenus;
import satb.model.MeteorologicalData;
import satb.model.dao.CollarDataDAO;
import weka.classifiers.Evaluation;
import weka.core.Instances;

/**
 *
 * @author Leandro
 */
public class ActivityRecognitionController implements Runnable {

    CollarDataDAO collarDataDAO;

    static private Hashtable<String, LinkedList<CoordinateVenus>> hashCoordinateCollar = new Hashtable(15);
    static private Hashtable<String, LinkedList<Observation>> hashObservationCollar = new Hashtable(15);

    private ActivityRecognition activityRecognition;
    volatile static Integer countActivityRecognitionRunnable = 0;
    static Double maxCorrectAll = 0.0;
    
    Hashtable<Long,LinkedList<MeteorologicalData>> hashMeteorologicalData;

    public ActivityRecognitionController() {
        collarDataDAO = new CollarDataDAO();
    }

    public ActivityRecognitionController(ActivityRecognition ar) {        
        this();
        this.activityRecognition = ar;
    }

    public void trainClassifier() {

        LinkedList<CoordinateVenus> listCoordinate = collarDataDAO.selectAllDataVenus();
        LinkedList<Observation> listObservations = collarDataDAO.selectAllObservation();

        try {
            new ActivityRecognition(listCoordinate, listObservations);
        } catch (Exception ex) {
            Logger.getLogger(ActivityRecognitionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void trainClassifier(Integer degreesForSameDirectionA, Integer historyLengthA, Double minSpeedA, Double segmentSecondsA,
            Integer degreesForSameDirectionB, Integer historyLengthB, Double minSpeedB, Double segmentSecondsB) {

        LinkedList<CoordinateVenus> listCoordinate = collarDataDAO.selectAllDataVenus();
        LinkedList<Observation> listObservations = collarDataDAO.selectAllObservation();

        try {
            new ActivityRecognition(listCoordinate, listObservations, degreesForSameDirectionA, degreesForSameDirectionB, 
                    historyLengthA, historyLengthB, minSpeedA, minSpeedB, segmentSecondsA, segmentSecondsB);
        } catch (Exception ex) {
            Logger.getLogger(ActivityRecognitionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void trainClassifierLomba() {

        // 20170828185377553 22:50 28/08/2017 habilitação cartão
        
        ActivityRecognition arThread = new ActivityRecognition(); 
        arThread.historyLength = 3;
        
        String configurationPrint = "historyLength \t\t="+arThread.historyLength +"\n";            
        
        hashMeteorologicalData = collarDataDAO.selectMeteorologicalData();
        
        LinkedList<MovementDataStructure> listMDSClassification = new LinkedList<MovementDataStructure>();
        LinkedList<MovementDataStructure> listMDSAll = new LinkedList<MovementDataStructure>();
        
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00A2", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00A3", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00B2", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00B3", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00C3", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00C4", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00D1", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00D2", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00D3", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00D4", arThread, listMDSAll) );
        
        // adicionar 2017
        /* listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00D4", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00D4", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00D4", arThread, listMDSAll) );
        listMDSClassification.addAll( createMovementDataStructureCollarWithObservation ("00D4", arThread, listMDSAll) );
        */
        
        
        System.out.println("createARFF: "+Calendar.getInstance().getTime());        
        WekaTest wt = new WekaTest();
        //arThread.observationsTypes.add("NaoObservado");
        Instances data = arThread.createARFFDataFromMDS(listMDSClassification, false);        
        Instances dataAll = arThread.createARFFDataFromMDS(listMDSAll, true);
        
        
        //for(int i=0; i<data.numInstances(); i++) {
            /*if ( data.instance(i).stringValue( data.classAttribute() ).equals("BebendoAgua") ) {
                data.instance(i).setValue(data.classAttribute(), "EmPe-Parado");                                
            } */                      
            /*if ( data.instance(i).stringValue( data.classAttribute() ).equals("Deitado-Ruminando") ) {
                data.instance(i).setValue(data.classAttribute(), "EmPe-Ruminando");                
            }*/
            /*if ( data.instance(i).stringValue( data.classAttribute() ).equals("EmPe-Ruminando") ) {
                data.instance(i).setValue(data.classAttribute(), "EmPe-Parado");                
            }*/
            /*if ( data.instance(i).stringValue( data.classAttribute() ).equals("Deitado-Parado") ) {
                data.instance(i).setValue(data.classAttribute(), "EmPe-Parado");                
            }*/
            /*if ( data.instance(i).stringValue( data.classAttribute() ).equals("EmPe-Parado") ) {
                data.instance(i).setValue(data.classAttribute(), "EmPe-Parado");                
            } */                       
        //}
        
        // remover angle,         
        //ActivityRecognition.createARFF(data, ActivityRecognition.filenameARFF );
        //ActivityRecognition.createARFF(dataAll, "tudo_"+ActivityRecognition.filenameARFF );
        
        try {
            wt.go3(data, configurationPrint, dataAll);
            
        } catch (Exception ex) {
            Logger.getLogger(ActivityRecognitionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public LinkedList<MovementDataStructure> createMovementDataStructureCollarWithObservation (String collar, ActivityRecognition arThread, LinkedList<MovementDataStructure> listMDSAll) {
        
        
        //System.out.println("=============  "+collar+" ================");        
        //System.out.println("selectDataVenusWithLdr: "+Calendar.getInstance().getTime());        
        LinkedList<CoordinateVenus> listCoordinate = collarDataDAO.selectDataVenusWithLdr(collar);
        
        //System.out.println("selectAllObservation: "+Calendar.getInstance().getTime());
        LinkedList<Observation> listObservations = collarDataDAO.selectObservation(collar);
                
        arThread.setListCoordinate(listCoordinate);
        arThread.setListObservations(listObservations);
        
        //System.out.println("doMovementAnalyzer: "+Calendar.getInstance().getTime());
        LinkedList<MovementDataStructure> listMDS = arThread.doMovementAnalyzer();
        listMDSAll.addAll(listMDS);        
        
        Integer lastIntersecs = 0;
        LinkedList<MovementDataStructure> listMDSClassification = new LinkedList<MovementDataStructure>();
        
        
        //System.out.println("getObservationIntersecs: "+Calendar.getInstance().getTime());
        //System.out.println("listMDS size: "+listMDS.size());
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH");
        
        // cria lista com os tipos de observações
        if ( ! arThread.observationsTypes.contains("NaoObservado") ) {
            arThread.observationsTypes.add("NaoObservado");
        }
        
        for(MovementDataStructure mds : listMDS) {

            Long begin = mds.getCoordinatePoint().getDateObject().getTime();
            Long end = begin + 1*1000;
            
            ObservationTime ot = arThread.getObservationIntersecs(begin, end, lastIntersecs);
            
            if(ot != null) {
                
                // cria lista com os tipos de observações
                if ( ! arThread.observationsTypes.contains(ot.observation.getObservation()) ) {
                    arThread.observationsTypes.add(ot.observation.getObservation());
                }
                
                lastIntersecs = ot.lastIntersecs;
                mds.setClassification(ot.observation.getObservation());
                
                listMDSClassification.add(mds);
            } else {
                mds.setClassification("NaoObservado");
            }
            
            try {
                /// busca no banco
                //mds.setListMeteorologicalData(collarDataDAO.selectMeteorologicalData(
                ///        sdf.parse( sdf.format( mds.getCoordinatePoint().getDateObject() ) )));

                /// busca em memória
                // "NaoObservado" ????
                mds.setListMeteorologicalData(
                    hashMeteorologicalData.get( 
                        sdf.parse( sdf.format( mds.getCoordinatePoint().getDateObject() ) ).getTime() 
                    )
                );

            } catch (ParseException ex) {
                Logger.getLogger(ActivityRecognitionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return listMDSClassification;
    }

    public void trainClassifierLomba(Integer degreesForSameDirectionA, Integer historyLengthA, Double minSpeedA, Double segmentSecondsA,
            Integer degreesForSameDirectionB, Integer historyLengthB, Double minSpeedB, Double segmentSecondsB) {

        Double minSpeed;
        Integer historyLength;
        Integer degreesForSameDirection;
        Double segmentSeconds;
        
        boolean cont = false;
        Integer countRunnable = 0;
        
        countActivityRecognitionRunnable = 0;
        maxCorrectAll = 0.0;

        for (minSpeed = minSpeedA; minSpeed <= minSpeedB; minSpeed += 0.1) {
            for (historyLength = historyLengthA; historyLength <= historyLengthB; historyLength += 1) {
                for (degreesForSameDirection = degreesForSameDirectionA; degreesForSameDirection <= degreesForSameDirectionB; degreesForSameDirection += 10) {
                    for (segmentSeconds = segmentSecondsA; segmentSeconds <= segmentSecondsB; segmentSeconds += 10) {

                        if (cont) {
                            minSpeed = 0.3;
                            degreesForSameDirection = 40;
                            historyLength = 9;
                            segmentSeconds = 110.0;
                            
                            cont = false;
                        }

                        while (countActivityRecognitionRunnable > 6);

                        countActivityRecognitionRunnable++;
                        ActivityRecognition arThread = new ActivityRecognition();
                        arThread.minSpeed = minSpeed;
                        arThread.historyLength = historyLength;
                        arThread.degreesForSameDirection = degreesForSameDirection;
                        arThread.segmentSeconds = segmentSeconds;
                        ActivityRecognitionController activityRecognitionController = new ActivityRecognitionController(arThread);
                        Thread t = new Thread(activityRecognitionController);
                        t.setName(t.getName() + "-activityRecognitionController-" + (countRunnable++) + "-" + countActivityRecognitionRunnable);
                        t.start();
                    }
                }
            }
        }

        // espera todas as threads
        while (countActivityRecognitionRunnable > 0);

        System.out.println("====================================================================================");
        System.out.println("====================================================================================");
        System.out.println("maxCorrect \t\t=" + maxCorrectAll);
    }
    
   
    @Override
    public void run() {

        String configurationPrint = "minSpeed \t\t=" + this.activityRecognition.minSpeed + "\n";
        configurationPrint += "degreesForSameDirection \t\t=" + this.activityRecognition.degreesForSameDirection + "\n";
        configurationPrint += "historyLength \t\t=" + this.activityRecognition.historyLength + "\n";
        configurationPrint += "segmentSeconds \t\t=" + this.activityRecognition.segmentSeconds + "\n";

        LinkedList<ActivityDataStructure> listADS = new LinkedList<>();

        String collar;

        collar = "00A2";
        listADS.addAll(createActivityDataStructureCollar(collar, this.activityRecognition));

        collar = "00A3";
        listADS.addAll(createActivityDataStructureCollar(collar, this.activityRecognition));

        collar = "00B2";
        listADS.addAll(createActivityDataStructureCollar(collar, this.activityRecognition));

        collar = "00B3";
        listADS.addAll(createActivityDataStructureCollar(collar, this.activityRecognition));

        collar = "00C3";
        listADS.addAll(createActivityDataStructureCollar(collar, this.activityRecognition));

        collar = "00C4";
        listADS.addAll(createActivityDataStructureCollar(collar, this.activityRecognition));

        collar = "00D1";
        listADS.addAll(createActivityDataStructureCollar(collar, this.activityRecognition));

        collar = "00D2";
        listADS.addAll(createActivityDataStructureCollar(collar, this.activityRecognition));

        collar = "00D3";
        listADS.addAll(createActivityDataStructureCollar(collar, this.activityRecognition));

        collar = "00D4";
        listADS.addAll(createActivityDataStructureCollar(collar, this.activityRecognition));

        Instances data = this.activityRecognition.createARFFData(listADS);

        for(int i=0; i<data.numInstances(); i++) {                        
            if ( data.instance(i).stringValue( data.classAttribute() ).equals("BebendoAgua") ) {
                data.instance(i).setValue(data.classAttribute(), "EmPe-Parado");                                
            }                       
            if ( data.instance(i).stringValue( data.classAttribute() ).equals("Deitado-Ruminando") ) {
                data.instance(i).setValue(data.classAttribute(), "Deitado-Parado");                
            }    
            if ( data.instance(i).stringValue( data.classAttribute() ).equals("EmPe-Ruminando") ) {
                data.instance(i).setValue(data.classAttribute(), "EmPe-Parado");                
            }
            /*if ( data.instance(i).stringValue( data.classAttribute() ).equals("Deitado-Parado") ) {
                data.instance(i).setValue(data.classAttribute(), "EmPe-Parado");                
            } */           
        }
        WekaTest wt = new WekaTest();
        try {
            Double maxCorrect = wt.go2(data, configurationPrint);
            if (maxCorrect > maxCorrectAll) {
                maxCorrectAll = maxCorrect;
            }
            countActivityRecognitionRunnable--;
        } catch (Exception ex) {
            Logger.getLogger(ActivityRecognitionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected LinkedList<ActivityDataStructure> createActivityDataStructureCollar(String collar, ActivityRecognition ar) {

        LinkedList<CoordinateVenus> listCoordinate;
        LinkedList<Observation> listObservations;

        try {

            synchronized (this) {
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
