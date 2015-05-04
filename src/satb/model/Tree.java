package satb.model;

import org.postgis.Point;

/**Classe representa uma árvore localizada em um pasto.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class Tree 
{
    //Atributos
    private int id;
    private int pasture;
    private String numberTree;
    private double latitude;
    private double longitude;
    private Point coordinate;
    
    /**Construtor Default. */
    public Tree() {}
    
     /**Construtor de dois parâmetros. */
    public Tree(double lon, double lat)
    {
        this.longitude = lon;
        this.latitude = lat;
        this.coordinate = new Point(lon, lat);
    }
    
    /**Construtor de três parâmetros. */
    public Tree(String n, double lon, double lat)
    {
        this.numberTree = n;
        this.longitude = lon;
        this.latitude = lat;
        this.coordinate = new Point(lon, lat);
    }
    
    /**Construtor de quatro parâmetros. */
    public Tree(int p, String n, double lon, double lat)
    {
        this.pasture = p;
        this.numberTree = n;
        this.longitude = lon;
        this.latitude = lat;
        this.coordinate = new Point(lon, lat);
    }
    
    
    /**Retorna o id. */
    public int getId()
    {
        return id;
    }
    
    /**Seta o valor do id. */
    public void setId(int id)
    {
        this.id = id;
    }
    
    
    /**Retorna o pasture. */
    public int getPasture()
    {
        return pasture;
    }
    
    /**Seta o atributo pasture. */
    public void setPasture(int pasture)
    {
        this.pasture = pasture;
    }
    
    /**Retorna o valor do atributo numberTree. */
    public String getNumberTree()
    {
        return numberTree;
    }
    
    /**Seta o valor do atributo numberTree. */
    public void setNumberTree(String n)
    {
        this.numberTree = n;
    }
    
    /**Retorna o valor do atributo latitude. */
    public double getLatitude()
    {
        return latitude;
    }
    
    /**Seta o valor do atributo latitude. */
    public void setLatitude(double lat)
    {
        this.latitude = lat;
    }
    
    /**Retorna o valor do atributo longitude. */
    public double getLongitude()
    {
        return longitude;
    }
    
    /**Seta o valor do atributo longitude. */
    public void setLongitude(double lon)
    {
        this.longitude = lon;
    }
    
    /**Retorna o valor do atributo coordinate. */
    public Point getCoordinate()
    {
        return coordinate;
    }
    
    /**Seta o valor do atributo coordinate. */
    public void setCoordinate(Point c)
    {
        this.coordinate = c;
    }
    
}
