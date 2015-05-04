package satb.controller;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.controller.functions.ClusteringFunctions;
import satb.controller.functions.KMLFunctions;
import satb.model.ClusterPoints;
import satb.model.Coordinate;
import satb.model.dao.TrajectoryClusteringDAO;

/**Classe que representa a camada de controle para Agrupamento de Trajetórias.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class TrajectoryClusteringController 
{    
    //Atributo
    private int numberElements;
    private int outline;
    private ObservableList<Coordinate> pointList;
    private ClusteringFunctions clustering;
    private ObservableList<ClusterPoints> clusters;
    private String path, pathkml;
    private KMLFunctions kml;
    
    /**Construtor Default. */
    public TrajectoryClusteringController()
    {
        kml = new KMLFunctions();
        clustering = new ClusteringFunctions();
        path = "C:/Program Files (x86)/Google/Google Earth/client/googleearth.exe";
        pathkml = "C:/Users/Marcel/Documents/Mestrado/";
        outline = 0;
        numberElements = 0;
        clusters = FXCollections.observableArrayList();
    }
     
    
    /**Retorna o conjunto de clusters criados pelo DBSCAN. */
    public ObservableList<ClusterPoints> getDBSCAN()
    {
        return clusters;
    }
    
    
    /**Método verifica se o DBSCAN já foi executado alguma vez.
    @param true se sim ou false se não.*/
    private boolean isDBSCANCreated()
    {
        if(clusters == null)
        {
            return false;
        }       
        return true;
    }
    
    
    /**Método verifica se algum dos algoritmos já foi executado alguma vez.
    @param true se sim ou false se não. */
    public boolean isCreated()
    {
        if(isDBSCANCreated())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
  
    /**Retorna o número de elementos que o colar possui. */
    public int getNumberElements() 
    {
         return numberElements;
    } 
   
    
    /**Retorna o número de ruídos (outlines). */
    public int getOutline() 
    {
         return outline;
    } 
   
    
    /**Retorna o número de elementos presentes nos clusters. */
    public int getClustersTotal()
    {
        return (numberElements - outline);
    }
    
    
    /**Invoca o método selectCollar() da classe TrajectoryClusteringDAO. */
    public ObservableList<String> selectCollar() throws Exception
    {  
        TrajectoryClusteringDAO  td = new TrajectoryClusteringDAO();  
        return td.selectCollar();  
    }
   
    
    /**Método que configura o algoritmo DBSCAN. */
    public void DBSCAN(String collar, String eps, String min, String date, String dateBegin, String dateEnd, String hourBefore, String hourAfter, String hourBegin, String hourEnd) throws Exception
    {
        TrajectoryClusteringDAO td = new TrajectoryClusteringDAO();  
        td.setHourSQL(hourBefore, hourAfter, hourBegin, hourEnd);
        td.setDateSQL(date, dateBegin, dateEnd);
        double epsValue = Double.parseDouble(eps);
        int minValue = Integer.parseInt(min);
        
        outline = 0;
        if(!clusters.isEmpty())
        {
            clusters.clear();            
        }
   
        pointList = td.selectTrajCoordinates(td.selectIds(collar));
        numberElements = pointList.size();
 
        ObservableList<Coordinate> neighborPts;
        
        int clusterId = 1;
        int i = 0;
        while(i < pointList.size())
        {
            Coordinate c = pointList.get(i);
            if(!c.getVisited())
            {
                c.setVisited(true);
                neighborPts = clustering.getNeighbours(pointList, c, epsValue);
                if(neighborPts.size() >= minValue)
                {
                    int index = 0;
                    while(index < neighborPts.size())
                    {
			Coordinate r = neighborPts.get(index);
			if(!r.getVisited())
                        {
                            r.setVisited(true);				
                            ObservableList<Coordinate> neigh = clustering.getNeighbours(pointList, r, epsValue);
                            if (neigh.size() >= minValue)
                            {
				neighborPts = clustering.merge(neighborPts, neigh);								
                            }
                            
			}
                        index++; 
                    }   
                    System.out.println("N "+ neighborPts.size()); 
                    clustering.setClusterId(clusterId, neighborPts);
                    clusterId++;
		}
                    
             }
             i++;    
        }

        for(Coordinate c : pointList)
        {
            System.out.println("Cluster Id: " + c.getClusterId());
        }

        clusters = clustering.createClusters(pointList, clusterId);
        System.out.println("Número de Clusters: " + clusters.size()); 

        this.outline = numberElements - clustering.numberElementsInClusters(clusters);
 
        for(ClusterPoints c : clusters)
        {
            System.out.println("Número de Elementos: " + c.points.size());
        }    
   }

    
   /**Mostrar o resultado do DBSCAN no mapa. */
   public void showDBSCAN(String collar) throws Exception
   {
        String pathfile = pathkml;
                
        //Invoca o método de construção do KML
        File file = kml.buildCBSMOT(kml.buildDBSCANKMLName(collar), clusters);
        
        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path,pathfile.concat(file.getPath())});   
   } 
    
    
}
