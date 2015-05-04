package satb.controller.functions;

import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.ClusterPoints;
import satb.model.Coordinate;

/**Classe com métodos para auxiliar algoritmos de Clustering.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class ClusteringFunctions 
{
    /**Construtor Default. */
    public ClusteringFunctions(){}

    /**Retorna os vizinhos mais próximos do ponto segundo a a distância definida por eps. */    
    public ObservableList<Coordinate> getNeighbours(ObservableList<Coordinate> pointList, Coordinate c, double eps)
    {
        ObservableList<Coordinate> points = FXCollections.observableArrayList();
        Iterator<Coordinate> iterator = pointList.iterator();

        while(iterator.hasNext())
        {
            Coordinate q = iterator.next();
            if(c.distance(q) <= eps)
            {
                points.add(q);
            }
        }

        return points;
    } 
    
   
    /**Mescla duas ObservableList. */
    public ObservableList<Coordinate> merge(ObservableList<Coordinate> a, ObservableList<Coordinate> b)
    {	        
        Iterator<Coordinate> iterator = b.iterator();
        while(iterator.hasNext())
        {
            Coordinate t = iterator.next();
            if(!a.contains(t))
            {
                a.add(t);
            }
        }
        return a;
    }


    /**Método que seta os clusterId das coordinates de uma coleção. */
    public void setClusterId(int clusterId, ObservableList<Coordinate> points) 
    {
        for (Coordinate p: points) 
        {
            p.setClusterId(clusterId);
        }
    }


    
    /**Método que retorna o número de elementos presentes nos clusters. */
    public int numberElementsInClusters(ObservableList<ClusterPoints> clusters)
    {
        int count = 0;
        for(ClusterPoints c : clusters)
        {
            count = count + c.points.size();
        }

        return count;
    }
   
    
    /**Cria os clusters com base nos dados processados. */
    public ObservableList<ClusterPoints> createClusters(ObservableList<Coordinate> points, int numberOfClusters) 
    {
        ObservableList<ClusterPoints> clusters = FXCollections.observableArrayList();
       
        for(int i = 1; i < numberOfClusters; i++)
        {
            ClusterPoints cluster = new ClusterPoints();
            cluster.setClusterId(i);
            
            for(Coordinate c : points)
            {
                if(c.getClusterId() == i)
                {
                    cluster.points.add(c);
                }
            }
            clusters.add(cluster);
            
        }

        return clusters;
    }
    

}
