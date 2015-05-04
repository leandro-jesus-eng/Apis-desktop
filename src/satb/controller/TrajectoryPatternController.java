package satb.controller;

import java.io.File;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.controller.functions.KMLFunctions;
import satb.controller.functions.TrajectoryFuntions;
import satb.model.Avoidance;
import satb.model.Coordinate;
import satb.model.Pasture;
import satb.model.SubTrajectory;
import satb.model.TargetObject;
import satb.model.Trajectory;
import satb.model.dao.PastureDAO;
import satb.model.dao.TrajectoryDAO;
import satb.model.dao.TrajectoryPatternDAO;
import org.postgis.Polygon;

/**Classe que representa a camada de controle para os Padrões de Trajetórias.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class TrajectoryPatternController 
{
    private Pasture pasture;
    private String path, pathkml;
    private KMLFunctions kml;
    private ObservableList<TargetObject> targetObjects;
    private ObservableList<Trajectory> trajectories;
    private TrajectoryFuntions functions;
    
    /**Construtor Default. */
    public TrajectoryPatternController()
    {
        kml = new KMLFunctions();
        functions = new TrajectoryFuntions();
        path = "C:/Program Files (x86)/Google/Google Earth/client/googleearth.exe";
        pathkml = "C:/Users/Marcel/Documents/Mestrado/";
        this.targetObjects = FXCollections.observableArrayList();
        this.trajectories = FXCollections.observableArrayList();
    }
    
    /**Retorna o número de targets criados no pasto. */
    public int numberOfTargets()
    {
        return this.targetObjects.size();
    }
    
    
    /**Retorna o atributo trajectories. */
    public ObservableList<Trajectory> getTrajectories()
    {
        return this.trajectories;
    }
   
    /**Método que constrói um grid com a área da pastagem. */
    public void buildGrid(String pastureName, String collar, String radius, String radiusROI, String length, String distanceGrid) throws Exception
    {
        PastureDAO pd = new PastureDAO();
        TrajectoryDAO td = new TrajectoryDAO();
        TrajectoryPatternDAO tpd = new TrajectoryPatternDAO();  

        this.pasture = pd.selectPasture(pastureName);        
        int distGrid = Integer.parseInt(distanceGrid);
        ObservableList<Coordinate> centerPoints = tpd.buildPointGrid(pasture.getAreaCoordinates(), distGrid);

        if(!this.targetObjects.isEmpty() || !this.trajectories.isEmpty())
        {
            this.targetObjects.clear();
            this.trajectories.clear();
        }

        this.trajectories = td.selectTrajectories(collar, td.selectDate(collar));
        
        System.out.println("CenterPoints:" + centerPoints.size());
        Iterator<Coordinate> iterator = centerPoints.iterator();   
        while(iterator.hasNext())
        {      
            TargetObject target = new TargetObject(iterator.next(), Double.valueOf(radius), Double.valueOf(radiusROI));
            target.setAreaObject(tpd.buildAreaObject(target.getCenter().getCoordinate(), (target.getRadius()/100000)));            
            target.setAreaInterest(tpd.buildAreaObject(target.getCenter().getCoordinate(), ((target.getRadiusInterest())/100000)));            
            this.targetObjects.add(target);
        }

        double subPathLength = (Double.valueOf(length));
        
       // showTargetObject();
        avoidance(this.targetObjects, subPathLength);
    }
      
  
    /**Algoritmo do padrão avoidance. */
    public void avoidance(ObservableList<TargetObject> targetObjects, double length) throws Exception
    {
        TrajectoryPatternDAO tpd = new TrajectoryPatternDAO();
        ObservableList<Avoidance> avoid = FXCollections.observableArrayList();
                    
        for(Trajectory t : this.trajectories)
        {    
            System.out.println("Trajetória: " + t.getDate());
            ObservableList<Coordinate> trajectory = t.getGPSPoints();
            for(TargetObject target : this.targetObjects)
            {
                           
                if(tpd.intersectTrajectInArea(trajectory, target.getAreaInterest()))
                {
                    if(tpd.intersectTrajectInArea(trajectory, target.getAreaObject()))
                    {
                        Avoidance avk = new Avoidance(t.getId(), target.getTargetId()); 
                        avk.setAvoidance(0); //avik = none,  não é avoidance
                        avk.setPonderation(1); // ajusta ponderação
                        avoid.add(avk);                        
                    }
                    else
                    {
                        ObservableList<Coordinate> pointsIn = tpd.pointsInArea(trajectory, target.getAreaInterest());
                        //Se o conjunto de pontos que intersectam a área de interesse for maior que 0.
                        if(pointsIn.size() > 1)
                        {    
                            ObservableList<SubTrajectory> sub = functions.SubtrajDT(pointsIn, target);
                            for(SubTrajectory subt : sub)
                            {    
                                if(subt.getDistance() >= length) // é subtrajetória direcionada ao alvo?
                                {
                                    Polygon cit = functions.regionOfIncrement(target, trajectory, subt);
                                    if(tpd.intersectTrajectInArea(trajectory, cit))
                                    {
                                        Avoidance avk = new Avoidance(t.getId(), target.getTargetId()); 
                                        avk.setAvoidance(1); // avik = strong, avoidance forte
                                        avoid.add(avk);    
                                    }
                                    else
                                    {
                                        Avoidance avk = new Avoidance(t.getId(), target.getTargetId()); 
                                        avk.setAvoidance(0.5); // avik = weak, avoidance fraco
                                        avoid.add(avk); 
                                    } 
                                }
                                else
                                {
                                    Avoidance avk = new Avoidance(t.getId(), target.getTargetId()); 
                                    avk.setAvoidance(0); //avik = none, não é avoidance
                                    avoid.add(avk);
                                }
                           }
                        }
                    }
                }    
            }
        }
        
       
        //Calculo do Percentual de Avoidance por região de interesse
        for(TargetObject target : targetObjects)
        {
            double Prk = 0, tam = 0;
            for(Avoidance avk : avoid)
            {
                if(avk.getIndexTargetObject() == target.getTargetId())
                {
                    
                    if(avk.getAvoidance() > 0)
                    {
                        System.out.println(avk.getIndexTrajectory() + " / " + target.getTargetId() + " / " + avk.getIndexTargetObject() + " / " + avk.getAvoidance());
                        Prk = Prk + Math.pow(avk.getAvoidance(), 0);  

                    }
                    tam++;
                }
            }

            if(tam == 0)
            {
                System.out.println("Quant.: " + tam + "  Percent: -1. \n");
                target.setPercentAvoidance(-1);
            }
            else
            {
                System.out.println("Quant.: " + tam + "  Percent: " + (Prk/tam) + "\n");
                target.setPercentAvoidance(Prk/tam);
            }
            
        }
        
        //Calculo da Confiança global Avt
        for(Trajectory t : trajectories)
        { 
            double Prk = 0;
            double Avik = 0;
            for(Avoidance avk : avoid)
            {
                if(avk.getIndexTrajectory() == t.getId())
                {
                    System.out.println(t.getId() + " / " + avk.getIndexTrajectory() + " / " + avk.getAvoidance() + " / " + avk.getPonderation());
                    Avik = Avik + (avk.getAvoidance() * avk.getPonderation());
                    Prk = Prk + avk.getPonderation();
                }
                
            }
            System.out.println("Avoidance " + Avik/Prk + "\n");
            t.setAvoidance(Avik/Prk);            
        }        
    }

    
    /**Mostra os elementos no mapa. */ 
    public void showTargetObject() throws Exception
    {
        String pathfile = pathkml;
                
        //Invoca o método de construção do KML
        File file = kml.buildTargetArea("kml/avoidanceAreas.kml", this.targetObjects);
                
        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path,pathfile.concat(file.getPath())});   
    } 
      
}
