/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package satb.behavior;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import satb.model.CoordinateVenus;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Leandro
 */
public class ActivityRecognition implements Runnable {
    
    public static boolean DEBUG = false;
    
    public static String FORWARD = "forward";
    public static String U_TURN = "uturn";
    public static String RIGHT = "right";
    public static String LEFT = "left";
    public static String NON_MOVING = "nonmoving";
    
    private Hashtable<String, Integer> qtdAlteracoesMovimentoTable;

    private LinkedList<CoordinateVenus> listCoordinate;
    private LinkedList<Observation> listObservations;

    LinkedList<MovementDataStructure> listMDS = null;
    
    public Integer degreesForSameDirection = 40;
    public Integer historyLength = 9;
    public Double minSpeed = 0.2;
    public Double likelihoodNonmovingUnder = 0.1;
    public Double likelihoodNonmovingOver = 0.6;
    
    public Double segmentSeconds = 160.0;
    
    Integer sincCoordinateObservationSeconds = 0;
    
    public static String filenameARFF = "CowActivity.arff";
    
    static Double maxCorrectAll = 0.0;
    
    volatile static Integer countActivityRecognitionRunnable = 0;
    
    public LinkedList<String> observationsTypes = new LinkedList<>();
    
    
    public ActivityRecognition() {
    }
    
    public ActivityRecognition(LinkedList<CoordinateVenus> listCoordinate, LinkedList<Observation> listObservations) throws Exception {
        this.listCoordinate = listCoordinate;
        this.listObservations = listObservations;
        
        String configurationPrint = "minSpeed \t\t="+ this.minSpeed +"\n";
            configurationPrint += "degreesForSameDirection \t\t="+this.degreesForSameDirection +"\n";
            configurationPrint += "historyLength \t\t="+this.historyLength +"\n";
            configurationPrint += "segmentSeconds \t\t="+this.segmentSeconds +"\n";

        LinkedList<MovementDataStructure> listMDS = doMovementAnalyzer();        
        LinkedList<SegmentDataStructure> listSDS = doSegmentAnalyzer( listMDS );        
        LinkedList<ActivityDataStructure> listADS = doActivityAnalyzer( listSDS );        
        WekaTest wt = new WekaTest();
        Instances data = createARFFData(listADS);
        createARFF(data, filenameARFF);        // cria o arquivo se precisar
        wt.go2(data, configurationPrint);
    }
    
    
    public ActivityRecognition(LinkedList<CoordinateVenus> listCoordinate, LinkedList<Observation> listObservations,
        Integer degreesForSameDirectionA, Integer degreesForSameDirectionB, 
        Integer historyLengthA, Integer historyLengthB,
        Double minSpeedA, Double minSpeedB,
        Double segmentSecondsA, Double segmentSecondsB) {
        
        this.listCoordinate = listCoordinate;
        this.listObservations = listObservations;
        
        maxCorrectAll = 0.0;
        countActivityRecognitionRunnable = 0;
        /*Double minSpeedTemp = 0.0;
        Integer degreesForSameDirectionTemp = 0;
        Integer historyLengthTemp = 0;
        Double segmentSecondsTemp = 0.0;*/
        Integer countRunnable = 0;
        
        for( this.minSpeed = minSpeedA; this.minSpeed <=minSpeedB ; this.minSpeed += 0.1) {
            for( this.historyLength = historyLengthA;  this.historyLength <= historyLengthB; this.historyLength +=1) {
                for( this.degreesForSameDirection = degreesForSameDirectionA; this.degreesForSameDirection<=degreesForSameDirectionB; this.degreesForSameDirection +=10 ) {// heading threshold

                    LinkedList<MovementDataStructure> listMDS = doMovementAnalyzer();

                    for(this.segmentSeconds = segmentSecondsA; this.segmentSeconds <= segmentSecondsB; this.segmentSeconds +=10 ) {

                       /* System.out.println("minSpeed \t\t="+ this.minSpeed);
                        System.out.println("degreesForSameDirection \t\t="+this.degreesForSameDirection);
                        System.out.println("historyLength \t\t="+this.historyLength);
                        System.out.println("segmentSeconds \t\t="+this.segmentSeconds);*/
                        
                        while(countActivityRecognitionRunnable > 6);
                        
                        countActivityRecognitionRunnable ++;
                        ActivityRecognition ar = new ActivityRecognition(listCoordinate, listObservations, listMDS, 
                                this.degreesForSameDirection, this.historyLength, this.minSpeed, this.segmentSeconds);
                        Thread t = new Thread (ar);
                        t.setName(t.getName()+"-ActivityRecognition-"+ (countRunnable++) + "-"+countActivityRecognitionRunnable);
                        t.start();                        
                    }
                }
            }
        }
        
        // espera todas as threads
        while(countActivityRecognitionRunnable > 0);
        
        System.out.println("====================================================================================");
        System.out.println("====================================================================================");
        System.out.println("maxCorrect \t\t="+ maxCorrectAll);
        /*System.out.println("minSpeed \t\t="+ minSpeedTemp);
        System.out.println("degreesForSameDirection \t\t="+degreesForSameDirectionTemp);
        System.out.println("historyLength \t\t="+historyLengthTemp);
        System.out.println("segmentSeconds \t\t="+segmentSecondsTemp);             */
    }
    
    
    public ActivityRecognition(LinkedList<CoordinateVenus> listCoordinate, LinkedList<Observation> listObservations, LinkedList<MovementDataStructure> listMDS,
        Integer degreesForSameDirection, Integer historyLength, Double minSpeed, Double segmentSeconds ) {
        
        this.listCoordinate = listCoordinate;
        this.listObservations = listObservations;
        
        this.degreesForSameDirection = degreesForSameDirection;
        this.historyLength = historyLength;
        this.minSpeed = minSpeed;
        this.segmentSeconds = segmentSeconds;
        
        this.listMDS = listMDS;
    }    
    
    @Override
    public void run() {
        
        String configurationPrint = "minSpeed \t\t="+ this.minSpeed +"\n";
            configurationPrint += "degreesForSameDirection \t\t="+this.degreesForSameDirection +"\n";
            configurationPrint += "historyLength \t\t="+this.historyLength +"\n";
            configurationPrint += "segmentSeconds \t\t="+this.segmentSeconds +"\n";

        if(this.listMDS == null) {
            this.listMDS = doMovementAnalyzer();        
        }
        LinkedList<SegmentDataStructure> listSDS = doSegmentAnalyzer( this.listMDS );        
        LinkedList<ActivityDataStructure> listADS = doActivityAnalyzer( listSDS );       
        
        WekaTest wt = new WekaTest();
        Instances data = createARFFData(listADS);        
        try {
            
            Double maxCorrect = wt.go2(data, configurationPrint);
            countActivityRecognitionRunnable--;
            if(maxCorrect > maxCorrectAll) {
                maxCorrectAll = maxCorrect; 
            }
            
        } catch (Exception ex) {
            Logger.getLogger(ActivityRecognition.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            
        }
    }
    
    

    public LinkedList<MovementDataStructure> doMovementAnalyzer() {        
        
        

        LinkedList<MovementDataStructure> listMDS = new LinkedList();

        if (listCoordinate.size() > 3) {
            
            ListIterator<CoordinateVenus> listIterator = listCoordinate.listIterator(2); 
            CoordinateVenus anterior = listCoordinate.get(1);
            MovementDataStructure mdsAnterior = new MovementDataStructure(anterior);
            CoordinateVenus antAnterior = listCoordinate.get(0);
            
            while (listIterator.hasNext()) {
                    CoordinateVenus atual = listIterator.next() ;
                    MovementDataStructure mds = new MovementDataStructure(atual);
                    listMDS.add(mds);
                    
                    // calculo da velocidade
                    // nao é necessário porque o GPS dá a velocidade, mas, vamos testar a diferença
                    Double distancia = atual.distance(anterior);
                    mds.setMagnitude(distancia);                    
                    Double differenceSeconds = (anterior.getDateObject().getTime() - atual.getDateObject().getTime()) / 1000.0;
                    mds.setSpeed(distancia/differenceSeconds);
                    
                    // calculo da aceleracao =  delta V / delta t
                    Double acceleration = (mdsAnterior.getSpeed() - mds.getSpeed()) / differenceSeconds;
                    mds.setAcceleration(acceleration);
                    
                    // TODO - isso tem valor? pegou de onde?
                    Double angle = mdsAnterior.getHeading()- mds.getHeading();
                    mds.setAngle(Math.abs(angle));
                    
                    // a discrete representation of the latest type of movement performed
                    // left, right, forward, uturn, nonmoving
                    
                    // Calcula se está movendo
                    Double priorNonmoving = 0.5;
                    Double priorMoving = 1.0 - priorNonmoving;
                    Double accuracyGPS = 0.4;
                    
                    Double uncertainty;
                    if(distancia < accuracyGPS) {
                        uncertainty = 1.0;
                    } else {
                        uncertainty = Math.pow( (accuracyGPS / distancia), 2);
                    }
                    
                    for(int i=1; i <= historyLength && (listMDS.size()-i >= 0) ; i++) {
                        Double likelihoodNonmoving;                        
                        if( listMDS.get( listMDS.size() - i).getSpeed() >= minSpeed  ) {
                            likelihoodNonmoving = likelihoodNonmovingUnder;
                        } else if (uncertainty < 1) {
                            likelihoodNonmoving = Math.pow(likelihoodNonmovingOver * uncertainty, 2);
                        } else {
                            likelihoodNonmoving = likelihoodNonmovingOver;
                        }
                        
                        Double likelihoodMoving = 1.0 - likelihoodNonmoving;
                        
                        Double pNonmoving = (priorNonmoving * likelihoodNonmoving) / ( (priorNonmoving * likelihoodNonmoving)+(priorMoving * likelihoodMoving) );
                        
                        priorNonmoving = pNonmoving;
                        priorMoving = 1.0 - priorNonmoving;                        
                    }
                    Boolean isMoving = priorNonmoving < 0.5;                    
                                        
                    if(isMoving) {
                        if(Math.abs(angle) <= degreesForSameDirection) {
                           mds.setMovimentType(FORWARD); 
                        } else { 
                           if (Math.abs(angle) > degreesForSameDirection*2 ) {
                               mds.setMovimentType(U_TURN);
                           } else {
                               if(angle < 0)
                                   mds.setMovimentType(RIGHT);
                               else
                                   mds.setMovimentType(LEFT);
                           }
                        }
                    } else {
                        mds.setMovimentType(NON_MOVING);
                    }
                    
                    if(atual.getAx() != null && anterior.getAx() != null  ) {
                        mds.setDeltaAx(atual.getAx() - anterior.getAx());
                        mds.setDeltaAy(atual.getAy() - anterior.getAy());
                        mds.setDeltaAz(atual.getAz() - anterior.getAz());
                        mds.setDeltaGx(atual.getGx() - anterior.getGx());
                        mds.setDeltaGy(atual.getGy() - anterior.getGy());
                        mds.setDeltaGz(atual.getGz() - anterior.getGz());
                        mds.setDeltaMx(atual.getMx() - anterior.getMx());
                        mds.setDeltaMy(atual.getMy() - anterior.getMy());
                        mds.setDeltaMz(atual.getMz() - anterior.getMz());
                        
                        mds.setLdr(atual.getLdr());
                    }
                    
                    antAnterior = anterior;
                    anterior = atual;
                    
                    mdsAnterior = mds;
            }
         
            if(DEBUG)
            System.out.println("Lista de MDS criada. Tamanho = "+listMDS.size());
            return listMDS;
        }

        return null;
    }
    
    
    public LinkedList<SegmentDataStructure> doSegmentAnalyzer(LinkedList<MovementDataStructure> listMDS) {
        
        LinkedList<SegmentDataStructure> listSDS = new LinkedList();
        
        if (listMDS.size() > 0) {
            
            // dividir em segmentos
            ListIterator<MovementDataStructure> listIterator = listMDS.listIterator(1);        
            MovementDataStructure anterior = listMDS.get(0);
            LinkedList<MovementDataStructure> listMDSSegment = new LinkedList();
            listMDSSegment.add(anterior);
            Double tempoAcumulado = 0.0;
            
            while (listIterator.hasNext()) {
                MovementDataStructure atual = listIterator.next();                
                
                tempoAcumulado += (atual.getCoordinatePoint().getDateObject().getTime() - anterior.getCoordinatePoint().getDateObject().getTime()) / 1000.0;
                if(tempoAcumulado > segmentSeconds) {
                    
                    SegmentDataStructure sds = new SegmentDataStructure(listMDSSegment);
                    listSDS.add( sds  );
                    
                    listMDSSegment = new LinkedList();
                    tempoAcumulado = 0.0;                    
                }
                
                listMDSSegment.add(atual);                
                anterior = atual;
            }
            
            
            Double taxa;            
            
            // para cada seguimento
            //System.out.println("Processando "+listSDS.size()+" Segmentos");
            for (SegmentDataStructure sds : listSDS) {
                
                Integer countFORWARD = 0;
                Integer countU_TURN = 0;
                Integer countLEFT = 0;
                Integer countRIGHT = 0;
                Integer countNON_MOVING = 0;
                
                Double maxSpeed = sds.getListMDS().get(0).getSpeed();
                Double minSpeed = sds.getListMDS().get(0).getSpeed();
                Double meanSpeed = 0.0;
                
                
                Double maxAcceleration = sds.getListMDS().get(0).getAcceleration();
                Double minAcceleration = sds.getListMDS().get(0).getAcceleration();                
                Double accumulatedAccelerationPositive = 0.0;
                Integer countAccelerationPositive = 0;
                Double accumulatedAccelerationNegative = 0.0;
                Integer countAccelerationNegative = 0;
                Integer changeBetweenPositiveAndNegative = 0;
                Boolean positivo = null;
                
                
                Double accumulatedDistance = 0.0;
                Double maxDistanceMoving = 0.0;                
                Double atualDistanceMoving = 0.0;
                

                Double accumulatedTimeMoving = 0.0;
                Double accumulatedTimeNonMoving = 0.0;
                
                
                qtdAlteracoesMovimentoTable = new Hashtable<>(43);
                MovementDataStructure anteriorMDS = null;
                for(MovementDataStructure mds : sds.getListMDS()) {                    
                    if(anteriorMDS != null) { 
                        String s1 = anteriorMDS.getMovimentType(); 
                        String s2 = mds.getMovimentType();
                        
                        if(qtdAlteracoesMovimentoTable.containsKey(s1+s2)) {
                            Integer qtdAlteracoesMovimentoTemp = qtdAlteracoesMovimentoTable.get(s1+s2);
                            qtdAlteracoesMovimentoTemp++;
                            qtdAlteracoesMovimentoTable.put(s1+s2, qtdAlteracoesMovimentoTemp);
                        } else {
                            qtdAlteracoesMovimentoTable.put(s1+s2, new Integer(1));
                        }                        
                    }                    
                    
                    if( mds.getMovimentType().equals(FORWARD)) {
                        countFORWARD++;
                    } else if( mds.getMovimentType().equals(U_TURN)) {
                        countU_TURN++;                        
                    } else if( mds.getMovimentType().equals(LEFT)) {                                
                        countLEFT++;                        
                    } else if( mds.getMovimentType().equals(RIGHT)) {                
                        countRIGHT++;
                    } else if( mds.getMovimentType().equals(NON_MOVING)) {
                        countNON_MOVING++;                        
                    }
                                        
                    
                    meanSpeed += mds.getSpeed();                                        
                    if(mds.getSpeed() > maxSpeed) {
                        maxSpeed = mds.getSpeed();
                    }                    
                    if(mds.getSpeed() < minSpeed) {
                        minSpeed = mds.getSpeed();
                    }
                    
                    
                    if(mds.getAcceleration() > 0) {
                        if(positivo != null && positivo == false)
                            changeBetweenPositiveAndNegative++;
                        accumulatedAccelerationPositive += mds.getAcceleration();
                        countAccelerationPositive++;
                        positivo = true;
                    } else if (mds.getAcceleration() < 0) {
                        if(positivo != null && positivo == true)
                            changeBetweenPositiveAndNegative++;
                        accumulatedAccelerationNegative += mds.getAcceleration();
                        countAccelerationNegative++;
                        positivo = false;
                    }                    
                    if(mds.getAcceleration() > maxAcceleration) {
                        maxAcceleration = mds.getAcceleration();
                    }                    
                    if(mds.getAcceleration() < minAcceleration) {
                        minAcceleration = mds.getAcceleration();
                    }              
                    
                    
                    accumulatedDistance += mds.getMagnitude();
                    
                    if(mds.getMovimentType().equals(NON_MOVING)) {
                        if(atualDistanceMoving > maxDistanceMoving)
                            maxDistanceMoving = atualDistanceMoving;
                        
                        atualDistanceMoving = 0.0;
                    } else {
                        atualDistanceMoving += mds.getMagnitude();
                    }
                    
                    if(anteriorMDS != null) {
                        if(mds.getMovimentType().equals(NON_MOVING)) {
                            accumulatedTimeNonMoving += (mds.getCoordinatePoint().getDateObject().getTime() - anteriorMDS.getCoordinatePoint().getDateObject().getTime());
                        } else {
                            accumulatedTimeMoving += (mds.getCoordinatePoint().getDateObject().getTime() - anteriorMDS.getCoordinatePoint().getDateObject().getTime());
                        }
                    } 
                    
                    anteriorMDS = mds;
                }
                
                
                // ---------------
                // movement type
                // ---------------

                // Distribution % of FORWARD
                // percentual do total
                taxa = ( (double)countFORWARD / sds.getListMDS().size() ) * 100.0;
                sds.setDistributionForward(taxa);

                //Distribution % of U_TURN         
                taxa = ( (double)countU_TURN / sds.getListMDS().size() ) * 100.0;
                sds.setDistributionUturn(taxa);

                //Distribution % of LEFT         
                taxa = ( (double)countLEFT / sds.getListMDS().size() ) * 100.0;
                sds.setDistributionLeft(taxa);

                //Distribution % of RIGHT
                taxa = ( (double)countRIGHT / sds.getListMDS().size() ) * 100.0;
                sds.setDistributionRight(taxa);

                //Distribution % of NON_MOVING
                taxa = ( (double)countNON_MOVING / sds.getListMDS().size() ) * 100.0;
                sds.setDistributionNonMoving(taxa);


                Double qtdAlteracoesMovimento = -1.0;
                /*String movementType = "";
                for(MovementDataStructure mds : sds.getListMDS()) {
                    if(! mds.getMovimentType().equals(movementType)) {
                        qtdAlteracoesMovimento++;
                        movementType = mds.getMovimentType();
                    }
                }*/
                // Change rate between moving and non moving
                // Considera-se movimentando se for:
                // Distribution % of FORWARD, U_TURN, LEFT, RIGHT
                String movementType = "";
                for(MovementDataStructure mds : sds.getListMDS()) {
                    if(! mds.getMovimentType().equals(movementType)) {
                        qtdAlteracoesMovimento++;
                        movementType = mds.getMovimentType();
                    }
                }
                

                // Change rate between any type of movement
                // FORWARD -> NON_MOVING
                if(qtdAlteracoesMovimento != 0) {
                    //int countForwardToNonMoving = countMovementBetween(sds.getListMDS(), FORWARD, NON_MOVING);
                    Integer countForwardToNonMoving = qtdAlteracoesMovimentoTable.get(FORWARD+NON_MOVING);
                    if(countForwardToNonMoving == null) countForwardToNonMoving = 0;
                    Double changeRateForwardToNonMoving = countForwardToNonMoving / qtdAlteracoesMovimento;
                    sds.setChangeRateForwardToNonMoving(changeRateForwardToNonMoving);

                    // FORWARD -> U_TURN
                    Integer countForwardToUturn = qtdAlteracoesMovimentoTable.get(FORWARD+U_TURN);
                    if(countForwardToUturn == null) countForwardToUturn = 0;
                    Double changeRateForwardToUturn = countForwardToUturn / qtdAlteracoesMovimento;
                    sds.setChangeRateForwardToUturn(changeRateForwardToUturn);

                    // FORWARD -> LEFT
                    Integer countForwardToLeft = qtdAlteracoesMovimentoTable.get(FORWARD+LEFT);
                    if(countForwardToLeft == null) countForwardToLeft = 0;
                    Double changeRateForwardToLeft = countForwardToLeft / qtdAlteracoesMovimento;
                    sds.setChangeRateForwardToLeft(changeRateForwardToLeft);

                    // FORWARD -> RIGHT
                    Integer countForwardToRight = qtdAlteracoesMovimentoTable.get(FORWARD+RIGHT);
                    if(countForwardToRight == null) countForwardToRight = 0;
                    Double changeRateForwardToRight = countForwardToRight / qtdAlteracoesMovimento;
                    sds.setChangeRateForwardToRight(changeRateForwardToRight);


                    // NON_MOVING -> FORWARD
                    Integer countNonMovingToForward = qtdAlteracoesMovimentoTable.get(NON_MOVING+FORWARD);
                    if(countNonMovingToForward == null) countNonMovingToForward = 0;
                    Double changeRateNonMovingToForward = countNonMovingToForward / qtdAlteracoesMovimento;
                    sds.setChangeRateNonMovingToForward(changeRateNonMovingToForward);

                    // NON_MOVING -> U_TURN
                    Integer countNonMovingToUturn = qtdAlteracoesMovimentoTable.get(NON_MOVING+U_TURN);
                    if(countNonMovingToUturn == null) countNonMovingToUturn = 0;
                    Double changeRateNonMovingToUturn = countNonMovingToUturn / qtdAlteracoesMovimento;
                    sds.setChangeRateNonMovingToUturn(changeRateNonMovingToUturn);

                    // NON_MOVING -> LEFT
                    Integer countNonMovingToLeft = qtdAlteracoesMovimentoTable.get(NON_MOVING+LEFT);
                    if (countNonMovingToLeft == null) countNonMovingToLeft=0;
                    Double changeRateNonMovingToLeft = countNonMovingToLeft / qtdAlteracoesMovimento;
                    sds.setChangeRateNonMovingToLeft(changeRateNonMovingToLeft);

                    // NON_MOVING -> RIGHT
                    Integer countNonMovingToRight = qtdAlteracoesMovimentoTable.get(NON_MOVING+RIGHT);
                    if(countNonMovingToRight == null) countNonMovingToRight = 0;
                    Double changeRateNonMovingToRight = countNonMovingToRight / qtdAlteracoesMovimento;
                    sds.setChangeRateNonMovingToRight(changeRateNonMovingToRight);


                    // U_TURN -> FORWARD
                    Integer countUturnToForward = qtdAlteracoesMovimentoTable.get(U_TURN+FORWARD);
                    if(countUturnToForward == null) countUturnToForward = 0;
                    Double changeRateUturnToForward = countUturnToForward / qtdAlteracoesMovimento;
                    sds.setChangeRateUturnToForward(changeRateUturnToForward);

                    // U_TURN -> NON_MOVING
                    Integer countUturnToNonmoving = qtdAlteracoesMovimentoTable.get(U_TURN+NON_MOVING);
                    if(countUturnToNonmoving == null) countUturnToNonmoving = 0;
                    Double changeRateUturnToNonmoving = countUturnToNonmoving / qtdAlteracoesMovimento;
                    sds.setChangeRateUturnToNonmoving(changeRateUturnToNonmoving);

                    // U_TURN -> LEFT
                    Integer countUturnToLeft = qtdAlteracoesMovimentoTable.get(U_TURN+LEFT);
                    if(countUturnToLeft == null) countUturnToLeft = 0;
                    Double changeRateUturnToLeft = countUturnToLeft / qtdAlteracoesMovimento;
                    sds.setChangeRateUturnToLeft(changeRateUturnToLeft);

                    // U_TURN -> RIGHT
                    Integer countUturnToRight = qtdAlteracoesMovimentoTable.get(U_TURN+RIGHT);
                    if(countUturnToRight == null) countUturnToRight = 0;
                    Double changeRateUturnToRight = countUturnToRight / qtdAlteracoesMovimento;
                    sds.setChangeRateUturnToRight(changeRateUturnToRight);


                    // LEFT -> FORWARD
                    Integer countLeftToForward = qtdAlteracoesMovimentoTable.get(LEFT+FORWARD);
                    if(countLeftToForward == null) countLeftToForward = 0;
                    Double changeRateLeftToForward = countLeftToForward / qtdAlteracoesMovimento;
                    sds.setChangeRateLeftToForward(changeRateLeftToForward);

                    // LEFT -> NON_MOVING
                    Integer countLeftToNonmoving = qtdAlteracoesMovimentoTable.get(LEFT+NON_MOVING);
                    if(countLeftToNonmoving == null) countLeftToNonmoving = 0;
                    Double changeRateLeftToNonmoving = countLeftToNonmoving / qtdAlteracoesMovimento;
                    sds.setChangeRateLeftToNonmoving(changeRateLeftToNonmoving);

                    // LEFT -> U_TURN
                    Integer countLeftToUturn = qtdAlteracoesMovimentoTable.get(LEFT+U_TURN);
                    if( countLeftToUturn == null ) countLeftToUturn = 0;
                    Double changeRateLeftToUturn = countLeftToUturn / qtdAlteracoesMovimento;
                    sds.setChangeRateLeftToUturn(changeRateLeftToUturn);

                    // LEFT -> RIGHT
                    Integer countLeftToRight = qtdAlteracoesMovimentoTable.get(LEFT+RIGHT);
                    if(countLeftToRight == null) countLeftToRight = 0;
                    Double changeRateLeftToRight = countLeftToRight / qtdAlteracoesMovimento;
                    sds.setChangeRateLeftToRight(changeRateLeftToRight);


                    // RIGHT -> FORWARD
                    Integer countRightToForward = qtdAlteracoesMovimentoTable.get(RIGHT+FORWARD);
                    if(countRightToForward == null) countRightToForward = 0;
                    Double changeRateRightToForward = countRightToForward / qtdAlteracoesMovimento;
                    sds.setChangeRateRightToForward(changeRateRightToForward);

                    // RIGHT -> NON_MOVING
                    Integer countRightToNonmoving = qtdAlteracoesMovimentoTable.get(RIGHT+NON_MOVING);
                    if(countRightToNonmoving == null) countRightToNonmoving = 0;
                    Double changeRateRightToNonmoving = countRightToNonmoving / qtdAlteracoesMovimento;
                    sds.setChangeRateRightToNonmoving(changeRateRightToNonmoving);

                    // RIGHT -> U_TURN
                    Integer countRightToUturn = qtdAlteracoesMovimentoTable.get(RIGHT+U_TURN);
                    if(countRightToUturn == null) countRightToUturn = 0;
                    Double changeRateRightToUturn = countRightToUturn / qtdAlteracoesMovimento;
                    sds.setChangeRateRightToUturn(changeRateRightToUturn);

                    // RIGHT -> LEFT
                    Integer countRightToLeft = qtdAlteracoesMovimentoTable.get(RIGHT+LEFT);
                    if(countRightToLeft == null) countRightToLeft = 0;
                    Double changeRateRightToLeft = countRightToLeft / qtdAlteracoesMovimento;
                    sds.setChangeRateRightToLeft(changeRateRightToLeft);
                    
                    
                    
                    // ---------------
                    // heading
                    // ---------------

                    // Changes accumulated (forward, left, U_TURN, NON_MOVING, RIGHT)
                    Double changeRateAccumulated = (( 1 + changeRateForwardToNonMoving  ) * ( 1 + changeRateForwardToUturn     ) * 
                            ( 1 + changeRateForwardToLeft      ) * ( 1 + changeRateForwardToRight     ) * ( 1 + changeRateNonMovingToForward ) * 
                            ( 1 + changeRateNonMovingToUturn   ) * ( 1 + changeRateNonMovingToLeft    ) * ( 1 + changeRateNonMovingToRight   ) * 
                            ( 1 + changeRateUturnToForward     ) * ( 1 + changeRateUturnToNonmoving   ) * ( 1 + changeRateUturnToLeft        ) * 
                            ( 1 + changeRateUturnToRight       ) * ( 1 + changeRateLeftToForward      ) * ( 1 + changeRateLeftToNonmoving    ) * 
                            ( 1 + changeRateLeftToUturn        ) * ( 1 + changeRateLeftToRight        ) * ( 1 + changeRateRightToForward     ) * 
                            ( 1 + changeRateRightToNonmoving   ) * ( 1 + changeRateRightToUturn       ) * ( 1 + changeRateRightToLeft        ) ) - 1;
                    sds.setChangeRateAccumulated(changeRateAccumulated);
                }

                

                // Change rate (forward, left, etc.)
                // verificar a quantidade de alterações sobre a quantidade de movimentos
                Double changeRate = qtdAlteracoesMovimento / sds.getListMDS().size();
                sds.setChangeRate(changeRate);
                
                // Changes max (forward, left, etc.)
                

                // ---------------
                // speed
                // ---------------

                // speed max                
                sds.setMaxSpeed(maxSpeed);

                // speed min 
                sds.setMinSpeed(minSpeed);

                // speed mean
                meanSpeed = meanSpeed / sds.getListMDS().size();
                sds.setMeanSpeed(meanSpeed);


                // ---------------
                // acceleration
                // ---------------                                

                // acceleration Max
                sds.setMaxAcceleration(maxAcceleration);                

                // acceleration min
                sds.setMinAcceleration(minAcceleration);

                // accumulated acceleration positive
                sds.setAccumulatedAccelerationPositive(accumulatedAccelerationPositive);

                // accumulated acceleration negative
                sds.setAccumulatedAccelerationNegative(accumulatedAccelerationNegative);
                
                if(countAccelerationPositive != 0) {
                    // mean acceleration positive
                    sds.setMeanAccelerationPositive(accumulatedAccelerationPositive / countAccelerationPositive);

                    // mean acceleration negative
                    sds.setMeanAccelerationNegative(accumulatedAccelerationNegative / countAccelerationNegative);
                }

                // count acceleration positive
                sds.setCountAccelerationPositive(countAccelerationPositive);

                // count acceleration negative
                sds.setCountAccelerationNegative(countAccelerationNegative);

                // changes between positive and negative
                sds.setChangeBetweenPositiveAndNegative(changeBetweenPositiveAndNegative);


                // ---------------
                // distance
                // ---------------                                
                
                // distance Accumulated for 2D moving
                sds.setAccumulatedDistance(accumulatedDistance);
                
                // max distance moving
                sds.setMaxDistanceMoving(maxDistanceMoving);


                // ---------------
                // time
                // ---------------
                
                // Accumulated moving ... seconds
                sds.setAccumulatedTimeMoving(accumulatedTimeMoving / 1000);

                // Accumulated non moving
                sds.setAccumulatedTimeNonMoving(accumulatedTimeNonMoving / 1000);
            }
            
            if(DEBUG)
            System.out.println("Lista de SDS criada. Tamanho = "+listSDS.size());
            return listSDS;
        }
        
        return null;
    }
    
    
    public LinkedList<ActivityDataStructure> doActivityAnalyzer(LinkedList<SegmentDataStructure> listSDS) {
        
        LinkedList<ActivityDataStructure> listADS = new LinkedList<>();
        
        Integer lastIntersecs = 0;
        
        for(SegmentDataStructure sds : listSDS) {
            Long begin = sds.getListMDS().getFirst().getCoordinatePoint().getDateObject().getTime() + sincCoordinateObservationSeconds*1000;
            Long end = sds.getListMDS().getLast().getCoordinatePoint().getDateObject().getTime() + sincCoordinateObservationSeconds*1000;
            if(DEBUG) {
            System.out.println(sds.getListMDS().getFirst().getCoordinatePoint().getDate());
            System.out.println(sds.getListMDS().getLast().getCoordinatePoint().getDate());
            System.out.println("-------------------------------");
            }
            
            //String observation = getObservationIntersecs(begin, end);
            ObservationTime ot = getObservationIntersecs(begin, end, lastIntersecs);
            
            //if(observation != null) {
            if(ot != null) {
                
                // cria lista com os tipos de observações
                if ( ! observationsTypes.contains(ot.observation.getObservation()) ) {
                    observationsTypes.add(ot.observation.getObservation());
                }
                
                ActivityDataStructure adsTemp = new ActivityDataStructure(sds, ot.observation.getObservation());
                adsTemp.setTimeObservation(ot.observation.getTime());                
                listADS.add(adsTemp);
            }
        }
        
        Long tempoObservacao;
        if(DEBUG) {
            System.out.println("Lista de ADS criada. Tamanho = "+listADS.size());

            Map<String, Long> mapObservationsIntersec = new HashMap();        
            for(ActivityDataStructure ads : listADS) {
                Long begin = ads.getSegmentDataStructure().getListMDS().getFirst().getCoordinatePoint().getDateObject().getTime();
                Long end = ads.getSegmentDataStructure().getListMDS().getLast().getCoordinatePoint().getDateObject().getTime();
                tempoObservacao = end - begin;
                String observation = ads.getClassification();
                if( mapObservationsIntersec.containsKey(observation) ) {
                    Long tempo = mapObservationsIntersec.get(observation);
                    tempo = tempo + tempoObservacao;
                    mapObservationsIntersec.put(observation, tempo);
                } else {
                    mapObservationsIntersec.put(observation, new Long(tempoObservacao.longValue()));
                }
            }     
            Long tempoObservacaoTotal = 0L;
            for(String chave: mapObservationsIntersec.keySet()) {
                tempoObservacao = mapObservationsIntersec.get(chave);
                tempoObservacaoTotal+=tempoObservacao;
            
                System.out.println("Tempo observador para -"+chave+" = "+tempoObservacao/1000+" segundos");
            }

            System.out.println("Tempo total de observacao = "+tempoObservacaoTotal/1000+" segundos");
        }
        
        return listADS;
    }
    
    public ObservationTime getObservationIntersecs(Long beginSegment, Long endSegment, Integer lastIntersecs) {
        
        Map<String, ObservationTime> mapObservationsIntersec = new HashMap();
        Long tempoObservacao = 0L;
        
        for(int i=lastIntersecs ; i < listObservations.size() ; i++) {
            Observation observationObject = listObservations.get(i);
            Long beginObservation = observationObject.getData().getTime();
            Long endObservation;
            if(i+1 == listObservations.size() )                 
                endObservation = beginObservation + segmentSeconds.longValue()*1000;
            else
                endObservation = listObservations.get(i+1).getData().getTime();
            String observation = observationObject.getObservation();
            
            // Se a observação iniciou muito antes do segmento,
            if( (endSegment-beginObservation) > 4*(endSegment-beginSegment) )
                continue;
            
            //    |  segment    |
            //                           | observation  |
            // if( beginObservation > endObservation )
            if( beginObservation > endSegment )
                break;
            

            //                      |    segment ...  
            //    |     observation     |
            if(beginObservation <= beginSegment && endObservation > beginSegment ) {                    
                
                // se o segmento está dentro da observação
                //                      |    segment   |
                //    |     observation                      |
                if(endSegment <= endObservation) {
                    ObservationTime ot = new ObservationTime();
                    ot.observation = observationObject;
                    //ot.timeAcumulated = new Long(tempoObservacao.longValue());
                    ot.timeAcumulated = endSegment - beginSegment;                    
                    ot.lastIntersecs = i;
                    return ot;
                    //return observation;
                    
                //                      |    segment   |
                //    |     observation              |
                } else if(endObservation < endSegment) { // segmento termina depois do fim observacao                    
                    tempoObservacao = endObservation - beginSegment;
                }
            }
            
            //    |       segment      |
            //        | observation  |
            if(beginSegment <= beginObservation && endObservation <= endSegment) {                
                tempoObservacao = endObservation - beginObservation;
            }
            
            //    |       segment      |
            //        | observation       |
            if(beginObservation >= beginSegment //&& beginObservation < endSegment
                    && endObservation > endSegment) {
                tempoObservacao = endSegment - beginObservation;
            }
            
            if(tempoObservacao > 0) {
                if( mapObservationsIntersec.containsKey(observation) ) {
                    ObservationTime ot = mapObservationsIntersec.get(observation);                    
                    ot.timeAcumulated = ot.timeAcumulated + tempoObservacao;
                    ot.lastIntersecs = i;                    
                    mapObservationsIntersec.put(observation, ot); // acho q nem precisa, vai dar replace
                    
                    //Long tempo = mapObservationsIntersec.get(observation);
                    //tempo = tempo + tempoObservacao;
                    //mapObservationsIntersec.put(observation, tempo);
                } else {
                    ObservationTime ot = new ObservationTime();
                    ot.observation = observationObject;
                    ot.timeAcumulated = new Long(tempoObservacao.longValue());
                    ot.lastIntersecs = i;
                    mapObservationsIntersec.put(observation, ot);
                    //mapObservationsIntersec.put(observation, new Long(tempoObservacao.longValue()));
                }
            }
            
            tempoObservacao = 0L;
        }
        
        String maxObservation = null;
        Long maxTempoObservacao = 0L;
        ObservationTime ot = null;
        ObservationTime otMax = null;
        
        for(String chave: mapObservationsIntersec.keySet()) {
            ot = mapObservationsIntersec.get(chave);
            tempoObservacao = ot.timeAcumulated;
            //tempoObservacao = mapObservationsIntersec.get(chave);
            if(tempoObservacao > maxTempoObservacao) {
                maxTempoObservacao = tempoObservacao;
                maxObservation = chave;
                otMax = ot;
            }
        }
        
        return otMax;        
        //return maxObservation;        
    }

    
    public int countMovementType (List<MovementDataStructure> listMDS, String movement ) {
        int quantidade = 0;
        for(MovementDataStructure mds : listMDS) {
            if(mds.getMovimentType().equals(movement))
                quantidade++;
        }
        return quantidade;           
    }
    
    /*protected int countMovementBetween(List<MovementDataStructure> listMDS, String movement1, String movement2) {
        int qtdMovementBetween = 0;
        
        MovementDataStructure anteriorMDS = null;
        for(MovementDataStructure mds : listMDS) {                    
            if(anteriorMDS != null && 
                    anteriorMDS.getMovimentType().equals(movement1) && 
                    mds.getMovimentType().equals(movement2) ) {
                qtdMovementBetween++;                        
            }
            anteriorMDS = mds;
        }
        
        return qtdMovementBetween;
    }*/
    
    /**Cria um arquivo no formato ARFF.*/  
    /*public void createARFF(LinkedList<ActivityDataStructure> listADS, String filename)
    {
        FastVector atts;
        Instances data;
        double[] vals;

        // 1. Definir os atributos
        atts = new FastVector();
        atts.addElement(new Attribute("distributionForward"));
        atts.addElement(new Attribute("distributionUturn"));
        atts.addElement(new Attribute("distributionLeft"));
        atts.addElement(new Attribute("distributionRight"));
        atts.addElement(new Attribute("distributionNonMoving"));
        atts.addElement(new Attribute("changeRateForwardToNonMoving"));
        atts.addElement(new Attribute("changeRateForwardToUturn"));
        atts.addElement(new Attribute("changeRateForwardToLeft"));
        atts.addElement(new Attribute("changeRateForwardToRight"));
        atts.addElement(new Attribute("changeRateNonMovingToForward"));
        atts.addElement(new Attribute("changeRateNonMovingToUturn"));
        atts.addElement(new Attribute("changeRateNonMovingToLeft"));
        atts.addElement(new Attribute("changeRateNonMovingToRight"));
        atts.addElement(new Attribute("changeRateUturnToForward"));
        atts.addElement(new Attribute("changeRateUturnToNonmoving"));
        atts.addElement(new Attribute("changeRateUturnToLeft"));
        atts.addElement(new Attribute("changeRateUturnToRight"));
        atts.addElement(new Attribute("changeRateLeftToForward"));
        atts.addElement(new Attribute("changeRateLeftToNonmoving"));
        atts.addElement(new Attribute("changeRateLeftToUturn"));
        atts.addElement(new Attribute("changeRateLeftToRight"));
        atts.addElement(new Attribute("changeRateRightToForward"));
        atts.addElement(new Attribute("changeRateRightToNonmoving"));
        atts.addElement(new Attribute("changeRateRightToUturn"));
        atts.addElement(new Attribute("changeRateRightToLeft"));
        atts.addElement(new Attribute("changeRateAccumulated"));
        atts.addElement(new Attribute("changeRate"));
        atts.addElement(new Attribute("maxSpeed"));
        atts.addElement(new Attribute("minSpeed"));
        atts.addElement(new Attribute("meanSpeed"));
        atts.addElement(new Attribute("maxAcceleration"));
        atts.addElement(new Attribute("minAcceleration"));
        atts.addElement(new Attribute("accumulatedAccelerationPositive"));
        atts.addElement(new Attribute("accumulatedAccelerationNegative"));
        atts.addElement(new Attribute("meanAccelerationPositive"));
        atts.addElement(new Attribute("meanAccelerationNegative"));
        atts.addElement(new Attribute("countAccelerationPositive"));
        atts.addElement(new Attribute("countAccelerationNegative"));
        atts.addElement(new Attribute("changeBetweenPositiveAndNegative"));
        atts.addElement(new Attribute("accumulatedDistance"));
        atts.addElement(new Attribute("maxDistanceMoving"));
        atts.addElement(new Attribute("accumulatedTimeMoving"));
        atts.addElement(new Attribute("accumulatedTimeNonMoving"));
        //atts.addElement(new Attribute("timeId"));
        FastVector classificacao = new FastVector();
        classificacao.addElement("Comendo");
        classificacao.addElement("EmPeDeitado");
        classificacao.addElement("Andando");
        //classificacao.addElement("Deitado");
        atts.addElement(new Attribute("class", classificacao ));

        // 2. Criar o objeto Instances
        data = new Instances("CowActivity", atts, 0);
        
        
        // 3. Preencher com dados
        for(ActivityDataStructure ads : listADS)
        {
            vals = new double[data.numAttributes()];
                     
            vals[0] = ads.getSegmentDataStructure().getDistributionForward();
            vals[1] = ads.getSegmentDataStructure().getDistributionUturn();
            vals[2] = ads.getSegmentDataStructure().getDistributionLeft();
            vals[3] = ads.getSegmentDataStructure().getDistributionRight();
            vals[4] = ads.getSegmentDataStructure().getDistributionNonMoving();
            vals[5] = ads.getSegmentDataStructure().getChangeRateForwardToNonMoving();
            vals[6] = ads.getSegmentDataStructure().getChangeRateForwardToUturn();
            vals[7] = ads.getSegmentDataStructure().getChangeRateForwardToLeft();
            vals[8] = ads.getSegmentDataStructure().getChangeRateForwardToRight();
            vals[9] = ads.getSegmentDataStructure().getChangeRateNonMovingToForward();
            vals[10] = ads.getSegmentDataStructure().getChangeRateNonMovingToUturn();
            vals[11] = ads.getSegmentDataStructure().getChangeRateNonMovingToLeft();
            vals[12] = ads.getSegmentDataStructure().getChangeRateNonMovingToRight();
            vals[13] = ads.getSegmentDataStructure().getChangeRateUturnToForward();
            vals[14] = ads.getSegmentDataStructure().getChangeRateUturnToNonmoving();
            vals[15] = ads.getSegmentDataStructure().getChangeRateUturnToLeft();
            vals[16] = ads.getSegmentDataStructure().getChangeRateUturnToRight();
            vals[17] = ads.getSegmentDataStructure().getChangeRateLeftToForward();
            vals[18] = ads.getSegmentDataStructure().getChangeRateLeftToNonmoving();
            vals[19] = ads.getSegmentDataStructure().getChangeRateLeftToUturn();
            vals[20] = ads.getSegmentDataStructure().getChangeRateLeftToRight();
            vals[21] = ads.getSegmentDataStructure().getChangeRateRightToForward();
            vals[22] = ads.getSegmentDataStructure().getChangeRateRightToNonmoving();
            vals[23] = ads.getSegmentDataStructure().getChangeRateRightToUturn();
            vals[24] = ads.getSegmentDataStructure().getChangeRateRightToLeft();
            vals[25] = ads.getSegmentDataStructure().getChangeRateAccumulated();
            vals[26] = ads.getSegmentDataStructure().getChangeRate();
            vals[27] = ads.getSegmentDataStructure().getMaxSpeed();
            vals[28] = ads.getSegmentDataStructure().getMinSpeed();
            vals[29] = ads.getSegmentDataStructure().getMeanSpeed();
            vals[30] = ads.getSegmentDataStructure().getMaxAcceleration();
            vals[31] = ads.getSegmentDataStructure().getMinAcceleration();
            vals[32] = ads.getSegmentDataStructure().getAccumulatedAccelerationPositive();
            vals[33] = ads.getSegmentDataStructure().getAccumulatedAccelerationNegative();
            vals[34] = ads.getSegmentDataStructure().getMeanAccelerationPositive();
            vals[35] = ads.getSegmentDataStructure().getMeanAccelerationNegative();
            vals[36] = ads.getSegmentDataStructure().getCountAccelerationPositive();
            vals[37] = ads.getSegmentDataStructure().getCountAccelerationNegative();
            vals[38] = ads.getSegmentDataStructure().getChangeBetweenPositiveAndNegative();
            vals[39] = ads.getSegmentDataStructure().getAccumulatedDistance();
            vals[40] = ads.getSegmentDataStructure().getMaxDistanceMoving();
            vals[41] = ads.getSegmentDataStructure().getAccumulatedTimeMoving();
            vals[42] = ads.getSegmentDataStructure().getAccumulatedTimeNonMoving();            
            //vals[43] = ads.getTimeObservation();
            vals[43] = classificacao.indexOf(ads.getClassification());
            
            
            // Adicionar atributos no ARFF
            data.add(new Instance(1.0, vals)); 
        }
        
        //Criação e escrita de um arquivo
        FileWriter fileWriter = null;
        try 
        {
             File file = new File(filename);
             fileWriter = new FileWriter(file);
             fileWriter.write(data.toString());
             fileWriter.close();
        } 
        catch (IOException ex) 
        {
             ex.printStackTrace();
        } 
        finally 
        {
             try 
             {
                 fileWriter.close();
             } 
             catch (IOException ex) 
             {
                 ex.printStackTrace();
             }
        }
    }*/
    
    
    
    /**Cria um arquivo no formato ARFF.*/  
    public static void createARFF(Instances data, String filename)
    {
        //Criação e escrita de um arquivo
        FileWriter fileWriter = null;
        try 
        {
             File file = new File(filename);
             fileWriter = new FileWriter(file);
             fileWriter.write(data.toString());
             fileWriter.close();
        } 
        catch (IOException ex) 
        {
             ex.printStackTrace();
        } 
        finally 
        {
             try 
             {
                 fileWriter.close();
             } 
             catch (IOException ex) 
             {
                 ex.printStackTrace();
             }
        }
    }
    
    
    public Instances createARFFData(LinkedList<ActivityDataStructure> listADS)
    {
        FastVector atts;
        Instances data;
        double[] vals;

        // 1. Definir os atributos
        atts = new FastVector();
        atts.addElement(new Attribute("distributionForward"));
        atts.addElement(new Attribute("distributionUturn"));
        atts.addElement(new Attribute("distributionLeft"));
        atts.addElement(new Attribute("distributionRight"));
        atts.addElement(new Attribute("distributionNonMoving"));
        atts.addElement(new Attribute("changeRateForwardToNonMoving"));
        atts.addElement(new Attribute("changeRateForwardToUturn"));
        atts.addElement(new Attribute("changeRateForwardToLeft"));
        atts.addElement(new Attribute("changeRateForwardToRight"));
        atts.addElement(new Attribute("changeRateNonMovingToForward"));
        atts.addElement(new Attribute("changeRateNonMovingToUturn"));
        atts.addElement(new Attribute("changeRateNonMovingToLeft"));
        atts.addElement(new Attribute("changeRateNonMovingToRight"));
        atts.addElement(new Attribute("changeRateUturnToForward"));
        atts.addElement(new Attribute("changeRateUturnToNonmoving"));
        atts.addElement(new Attribute("changeRateUturnToLeft"));
        atts.addElement(new Attribute("changeRateUturnToRight"));
        atts.addElement(new Attribute("changeRateLeftToForward"));
        atts.addElement(new Attribute("changeRateLeftToNonmoving"));
        atts.addElement(new Attribute("changeRateLeftToUturn"));
        atts.addElement(new Attribute("changeRateLeftToRight"));
        atts.addElement(new Attribute("changeRateRightToForward"));
        atts.addElement(new Attribute("changeRateRightToNonmoving"));
        atts.addElement(new Attribute("changeRateRightToUturn"));
        atts.addElement(new Attribute("changeRateRightToLeft"));
        atts.addElement(new Attribute("changeRateAccumulated"));
        atts.addElement(new Attribute("changeRate"));
        atts.addElement(new Attribute("maxSpeed"));
        atts.addElement(new Attribute("minSpeed"));
        atts.addElement(new Attribute("meanSpeed"));
        atts.addElement(new Attribute("maxAcceleration"));
        atts.addElement(new Attribute("minAcceleration"));
        atts.addElement(new Attribute("accumulatedAccelerationPositive"));
        atts.addElement(new Attribute("accumulatedAccelerationNegative"));
        atts.addElement(new Attribute("meanAccelerationPositive"));
        atts.addElement(new Attribute("meanAccelerationNegative"));
        atts.addElement(new Attribute("countAccelerationPositive"));
        atts.addElement(new Attribute("countAccelerationNegative"));
        atts.addElement(new Attribute("changeBetweenPositiveAndNegative"));
        atts.addElement(new Attribute("accumulatedDistance"));
        atts.addElement(new Attribute("maxDistanceMoving"));
        atts.addElement(new Attribute("accumulatedTimeMoving"));
        atts.addElement(new Attribute("accumulatedTimeNonMoving"));
        //atts.addElement(new Attribute("timeId"));
        FastVector classificacao = new FastVector();
        /*classificacao.addElement("Comendo");
        classificacao.addElement("EmPeDeitado");
        classificacao.addElement("Andando");*/
        for(String observation : observationsTypes) {
            classificacao.addElement(observation);
        }
        //classificacao.addElement("Deitado");
        atts.addElement(new Attribute("class", classificacao ));

        // 2. Criar o objeto Instances
        data = new Instances("CowActivity", atts, 0);
        
        
        // 3. Preencher com dados
        for(ActivityDataStructure ads : listADS)
        {
            vals = new double[data.numAttributes()];
                     
            vals[0] = ads.getSegmentDataStructure().getDistributionForward();
            vals[1] = ads.getSegmentDataStructure().getDistributionUturn();
            vals[2] = ads.getSegmentDataStructure().getDistributionLeft();
            vals[3] = ads.getSegmentDataStructure().getDistributionRight();
            vals[4] = ads.getSegmentDataStructure().getDistributionNonMoving();
            vals[5] = ads.getSegmentDataStructure().getChangeRateForwardToNonMoving();
            vals[6] = ads.getSegmentDataStructure().getChangeRateForwardToUturn();
            vals[7] = ads.getSegmentDataStructure().getChangeRateForwardToLeft();
            vals[8] = ads.getSegmentDataStructure().getChangeRateForwardToRight();
            vals[9] = ads.getSegmentDataStructure().getChangeRateNonMovingToForward();
            vals[10] = ads.getSegmentDataStructure().getChangeRateNonMovingToUturn();
            vals[11] = ads.getSegmentDataStructure().getChangeRateNonMovingToLeft();
            vals[12] = ads.getSegmentDataStructure().getChangeRateNonMovingToRight();
            vals[13] = ads.getSegmentDataStructure().getChangeRateUturnToForward();
            vals[14] = ads.getSegmentDataStructure().getChangeRateUturnToNonmoving();
            vals[15] = ads.getSegmentDataStructure().getChangeRateUturnToLeft();
            vals[16] = ads.getSegmentDataStructure().getChangeRateUturnToRight();
            vals[17] = ads.getSegmentDataStructure().getChangeRateLeftToForward();
            vals[18] = ads.getSegmentDataStructure().getChangeRateLeftToNonmoving();
            vals[19] = ads.getSegmentDataStructure().getChangeRateLeftToUturn();
            vals[20] = ads.getSegmentDataStructure().getChangeRateLeftToRight();
            vals[21] = ads.getSegmentDataStructure().getChangeRateRightToForward();
            vals[22] = ads.getSegmentDataStructure().getChangeRateRightToNonmoving();
            vals[23] = ads.getSegmentDataStructure().getChangeRateRightToUturn();
            vals[24] = ads.getSegmentDataStructure().getChangeRateRightToLeft();
            vals[25] = ads.getSegmentDataStructure().getChangeRateAccumulated();
            vals[26] = ads.getSegmentDataStructure().getChangeRate();
            vals[27] = ads.getSegmentDataStructure().getMaxSpeed();
            vals[28] = ads.getSegmentDataStructure().getMinSpeed();
            vals[29] = ads.getSegmentDataStructure().getMeanSpeed();
            vals[30] = ads.getSegmentDataStructure().getMaxAcceleration();
            vals[31] = ads.getSegmentDataStructure().getMinAcceleration();
            vals[32] = ads.getSegmentDataStructure().getAccumulatedAccelerationPositive();
            vals[33] = ads.getSegmentDataStructure().getAccumulatedAccelerationNegative();
            vals[34] = ads.getSegmentDataStructure().getMeanAccelerationPositive();
            vals[35] = ads.getSegmentDataStructure().getMeanAccelerationNegative();
            vals[36] = ads.getSegmentDataStructure().getCountAccelerationPositive();
            vals[37] = ads.getSegmentDataStructure().getCountAccelerationNegative();
            vals[38] = ads.getSegmentDataStructure().getChangeBetweenPositiveAndNegative();
            vals[39] = ads.getSegmentDataStructure().getAccumulatedDistance();
            vals[40] = ads.getSegmentDataStructure().getMaxDistanceMoving();
            vals[41] = ads.getSegmentDataStructure().getAccumulatedTimeMoving();
            vals[42] = ads.getSegmentDataStructure().getAccumulatedTimeNonMoving();            
            //vals[43] = ads.getTimeObservation();
            vals[43] = classificacao.indexOf(ads.getClassification());
            
            
            // Adicionar atributos no ARFF
            data.add(new Instance(1.0, vals)); 
        }
        
        data.setClassIndex(data.numAttributes() - 1);
        
        return data;
    }
    
    
    public Instances createARFFDataFromMDS(LinkedList<MovementDataStructure> listMDSClassification)
    {
        FastVector atts;
        Instances data;
        double[] vals;

        // 1. Definir os atributos
        atts = new FastVector();
        atts.addElement(new Attribute("movimentType"));
        atts.addElement(new Attribute("angle"));
        atts.addElement(new Attribute("magnitude"));
        atts.addElement(new Attribute("speed"));
        atts.addElement(new Attribute("acceleration"));
        atts.addElement(new Attribute("ldr"));
        atts.addElement(new Attribute("deltaAx"));
        atts.addElement(new Attribute("deltaAy"));
        atts.addElement(new Attribute("deltaAz"));
        atts.addElement(new Attribute("deltaGx"));
        atts.addElement(new Attribute("deltaGy"));
        atts.addElement(new Attribute("deltaGz"));
        atts.addElement(new Attribute("deltaMx"));
        atts.addElement(new Attribute("deltaMy"));
        atts.addElement(new Attribute("deltaMz"));

        FastVector movimentTypeList = new FastVector();
        movimentTypeList.addElement(FORWARD);
        movimentTypeList.addElement(U_TURN);
        movimentTypeList.addElement(RIGHT);
        movimentTypeList.addElement(LEFT);
        movimentTypeList.addElement(NON_MOVING);
        
        FastVector classificacao = new FastVector();
        for(String observation : observationsTypes) {
            classificacao.addElement(observation);
        }
        //classificacao.addElement("Deitado");
        atts.addElement(new Attribute("class", classificacao ));

        // 2. Criar o objeto Instances
        data = new Instances("CowActivity", atts, 0);
        
        
        // 3. Preencher com dados
        for(MovementDataStructure mds : listMDSClassification)
        {
            vals = new double[data.numAttributes()];
                     
            vals[0] = movimentTypeList.indexOf(mds.getMovimentType());
            vals[1] = mds.getAngle();
            vals[2] = mds.getMagnitude();
            vals[3] = mds.getSpeed();
            vals[4] = mds.getAcceleration();
            vals[5] = mds.getLdr();
            vals[6] = mds.getDeltaAx();
            vals[7] = mds.getDeltaAy();
            vals[8] = mds.getDeltaAz();
            vals[9] = mds.getDeltaGx();
            vals[10] = mds.getDeltaGy();
            vals[11] = mds.getDeltaGz();
            vals[12] = mds.getDeltaMx();
            vals[13] = mds.getDeltaMy();
            vals[14] = mds.getDeltaMz();
            vals[15] = classificacao.indexOf(mds.getClassification());
            
            // Adicionar atributos no ARFF
            data.add(new Instance(1.0, vals)); 
        }
        
        data.setClassIndex(data.numAttributes() - 1);
        
        return data;
    }
    
    
    public LinkedList<CoordinateVenus> getListCoordinate() {
        return listCoordinate;
    }

    public void setListCoordinate(LinkedList<CoordinateVenus> listCoordinate) {
        this.listCoordinate = listCoordinate;
    }

    public LinkedList<Observation> getListObservations() {
        return listObservations;
    }

    public void setListObservations(LinkedList<Observation> listObservations) {
        this.listObservations = listObservations;
    }
}
