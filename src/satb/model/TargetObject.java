package satb.model;

import org.postgis.Polygon;

/**Classe representa um TargetObject.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class TargetObject 
{
    //Atributos
    private int targetId;
    private double percentAvoidance;
    private Coordinate center;
    private Polygon areaObject;
    private Polygon areaInterest;
    private double radius;
    private double radiusInterest;

    /**Construtor de 2 parâmetros. */
    public TargetObject(Coordinate center, double radius, double radiusInterest)
    {
        this.radius = radius;
        this.radiusInterest = radiusInterest;
        this.center = center;
        this.targetId = center.getTimeIndex();
    }
    
    /**Retorna o valor de targetId. */
    public int getTargetId()
    {
        return this.targetId;
    }
    
    /**Retorna o valor de center. */
    public Coordinate getCenter()
    {
        return this.center;
    }
    
    /**Seta o valor de center. */
    public void setCenter(Coordinate center)
    {
        this.center = center;
    }
    
    
    /**Retorna o valor de areaObject. */
    public Polygon getAreaObject()
    {
        return this.areaObject;
    }
    
    /**Seta o valor de areaObject. */
    public void setAreaObject(Polygon areaObject)
    {
        this.areaObject = areaObject;
    }
    
    /**Retorna o valor de radius. */
    public double getRadius()
    {
        return this.radius;
    }
    
    /**Seta o valor de radius. */
    public void setRadius(double radius)
    {
        this.radius = radius;
    }
    
    /**Retorna o valor de areaInterest. */
    public Polygon getAreaInterest()
    {
        return this.areaInterest;
    }
    
    /**Seta o valor de areaObject. */
    public void setAreaInterest(Polygon areaInterest)
    {
        this.areaInterest = areaInterest;
    }
    
    /**Retorna o valor de radiusInterest. */
    public double getRadiusInterest()
    {
        return this.radiusInterest;
    }
    
    /**Seta o valor de radiusInterest. */
    public void setRadiusInterest(double radius)
    {
        this.radiusInterest = radius;
    }  
    
    /**Retorna o valor de percentAvoidance. */
    public double getPercentAvoidance()
    {
        return this.percentAvoidance;
    }
    
    /**Seta o valor de percentAvoidance. */
    public void setPercentAvoidance(double percent)
    {
        this.percentAvoidance = percent;
    } 
}
