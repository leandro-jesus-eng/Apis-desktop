package satb.model;

import java.util.ArrayList;
import java.util.List;

/**Classe representa uma Subtrajetória.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class SubTrajectory 
{
    //Atributos
    private double distance;
    private Coordinate initialPoint;
    public List<Coordinate> subPoints;
    
    /**Construtor com 1 parâmetro. */
    public SubTrajectory(List<Coordinate> points) 
    {
        this.subPoints = points;
    }
    
    /**Construtor com 2 parâmetros. */
    public SubTrajectory(double dist, Coordinate point) 
    {
        this.distance = dist;
        this.initialPoint = point;
        this.subPoints = new ArrayList<>();
        this.subPoints.add(point);
    }
    
    
    /**Retorna o valor do atributo distance. */
    public double getDistance()
    {
        return this.distance;
    }
    
    /**Seta o valor do atributo distance. */
    public void setDistance(double dist)
    {
        this.distance = dist;
    }
    
    /**Retorna o valor do atributo initialPoint. */
    public Coordinate getInitialPoint()
    {
        return this.initialPoint;
    }
    
    /**Seta o valor do atributo initialPoint. */
    public void setInitialPosition(Coordinate point)
    {
        this.initialPoint = point;
    }
    
    /**Retorna o valor o tamanho da subtrajetória. */
    public int size()
    {
       return this.subPoints.size();
    }
    
    /**Retorna a Coordinate através do index informado. */
    public Coordinate get(int index)
    {
       return this.subPoints.get(index);
    }
       
}
