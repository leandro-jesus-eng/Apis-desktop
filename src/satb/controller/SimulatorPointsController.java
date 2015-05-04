package satb.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.controller.functions.GeoFunctions;
import satb.controller.functions.KMLFunctions;
import satb.controller.functions.TrajectoryFuntions;
import satb.model.Coordinate;
import satb.model.dao.PastureDAO;
import satb.model.dao.SimulatorPointsDAO;
import org.postgis.Point;

/**Classe que representa a camada de controle para SimulatorPoints.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class SimulatorPointsController 
{
    //Atributo
    private Coordinate initialPoint;
    private TrajectoryFuntions trajFunction;
    private GeoFunctions geoFunction;
    private String path;
    private String pathkml;
    private KMLFunctions kml;
    
    /**Construtor Default. */
    public SimulatorPointsController()
    {
        trajFunction = new TrajectoryFuntions();
        geoFunction = new GeoFunctions();
        kml = new KMLFunctions();
        path = "C:/Program Files (x86)/Google/Google Earth/client/googleearth.exe";
        pathkml = "C:/Users/Marcel/Documents/Mestrado/";
    }
    
    /**Invoca o método generatePointInPasture do SimulatorPointsDAO. */
    public Point generatePointInPasture(String pasture) throws Exception
    {
        PastureDAO pd = new PastureDAO();
        SimulatorPointsDAO spd = new SimulatorPointsDAO();
        
        return spd.generatePointInPasture(pd.selectId(pasture));
    }
    
    
    /**Cria o ponto inicial do Ponto. */
    public void createInitalPoint(String longitude, String latitude, String date, String hour)
    {
        this.initialPoint = new Coordinate(Double.parseDouble(longitude), Double.parseDouble(latitude), date, hour);
    }
    
    
    /**Retorna um valor randomico de distância entre um intervalo. */
    public double distanceInterval(double ini, double end)
    {
        Random generator = new Random();
        double dist = ini + (end - ini) * generator.nextDouble();

        return dist;
    }
    
  
    /**Procura o próximo ponto da trajetória que esteja contido em um pasto. */
    public Coordinate nextPoint(Coordinate point, double dist, int pastureId) throws Exception
    {
        SimulatorPointsDAO spd = new SimulatorPointsDAO();
        Random generator = new Random();
        double degree;
        Coordinate c = null;
        boolean intersection = false;
        
        while(!intersection)
        {    
            degree = generator.nextInt(360);
            c = geoFunction.findNextPoint(point.getCoordinate(), dist, geoFunction.toRad(degree));
            
            intersection = spd.intersectPointInPasture(c, pastureId);
        }
            
        return c;
        
    }        
    
    public void simulateTrajectory(String gapTime, int tam, String pasture, String collar, String distIni, String distEnd) throws Exception
    {
        PastureDAO pd = new PastureDAO();
        ObservableList<Coordinate> trajectory = FXCollections.observableArrayList();
        trajectory.add(this.initialPoint);
        double ini = Double.parseDouble(distIni);
        double end = Double.parseDouble(distEnd);
        int pastureId = pd.selectId(pasture);
        
        for(int i = 0; i < tam - 1; i++)
        {
           
            double dist = distanceInterval(ini, end);
            Coordinate coord = nextPoint(trajectory.get(i), dist, pastureId);
            coord.setDate(trajectory.get(i).getDate());
            coord.setHour(trajFunction.plusHours(trajectory.get(i).getHour(), gapTime));
            trajectory.add(coord);    
        }
        
        System.out.println(trajectory.size());
        for(Coordinate c : trajectory)
        {
            System.out.println(collar + " " + c.getLongitudeX() + " " + c.getLatitudeY() + " " + c.getDate() + " " + c.getHour());
        }
        
        showCoordinates(trajectory, collar, trajectory.get(0).getDate());
        
    }
    
    
    /**Mostra o rastro do animal obtido com o colar. */
    public void showCoordinates(ObservableList<Coordinate> trajectory, String collar, String date) throws FileNotFoundException, IOException 
    {
        String pathfile = pathkml;
        
        
        //Invoca o método de construção do KML
        File file = kml.buildTrackKML(kml.buildKmlName(collar,date), trajectory);
    
        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path,pathfile.concat(file.getPath())});    
    }

    
    
}
