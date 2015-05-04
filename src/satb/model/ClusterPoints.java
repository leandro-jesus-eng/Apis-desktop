package satb.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**Classe que representa o objeto ClusterPoints.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class ClusterPoints 
{
    public ObservableList<Coordinate> points;
    private int clusterId;
    private int minPoints = 2;
    
    /**Construtor Default. */
    public ClusterPoints() 
    {
        points = FXCollections.observableArrayList();
    }
    
     
    /**Retorna o valor do atributo clusterId. */
    public int getClusterId() 
    {
        return this.clusterId;
    } 
    
    
    /**Seta o valor do atributo clusterId. */
    public void setClusterId (int id) 
    {
        this.clusterId = id;
    }
        
}
