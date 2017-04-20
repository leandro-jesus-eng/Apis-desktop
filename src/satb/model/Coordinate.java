package satb.model;

import java.sql.SQLException;
import org.postgis.Point;

/**Classe representa um ponto no mapa onde Y é latitude e X é longitude.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class Coordinate
{
    //Atributo Statics
    public static final int NULL_CLUSTER_ID = -1;
    
    //Atributos
    private int id;
    private String collar;
    
    private double latitudeY;
    private double longitudeX;
    private Point coordinate;
    private String hour;
    private String date;    
    private double speed;    
    private int clusterId;
    private boolean visited;
    private int timeIndex;

    /**Construtor Default. */
    public Coordinate()
    {
        this.clusterId = NULL_CLUSTER_ID;
        this.visited = false;
        this.coordinate = new Point();
    }
    
    /**Construtor com 1 parâmetro. */
    public Coordinate(String point) throws SQLException
    {
        this.coordinate = new Point(point);
        this.latitudeY = this.coordinate.getY();
        this.longitudeX = this.coordinate.getX();
        this.clusterId = NULL_CLUSTER_ID;
        this.visited = false;
    }
    
    
    /**Construtor com 1 parâmetro. */
    public Coordinate(Point point) throws SQLException
    {
        this.coordinate = point;
        this.latitudeY = this.coordinate.getY();
        this.longitudeX = this.coordinate.getX();
        this.clusterId = NULL_CLUSTER_ID;
        this.visited = false;
    }
        
    /**Construtor com dois parâmetros. */
    public Coordinate (String date, String hour)
    {
        this.date = date;
        this.hour = hour;
        this.clusterId = NULL_CLUSTER_ID;
        this.visited = false;
    }

    /**Construtor com dois parâmetros. */
    public Coordinate (double lon, double lat)
    {
        this.longitudeX = lon;
        this.latitudeY = lat;        
        this.coordinate = new Point(lon, lat);
        this.clusterId = NULL_CLUSTER_ID;
        this.visited = false;
    }
    
    /**Construtor com três parâmetros. */
    public Coordinate (String collar, String date, String hour)
    {
        this.collar = collar;
        this.date = date;
        this.hour = hour;
        this.clusterId = NULL_CLUSTER_ID;
        this.visited = false;
    }

    /**Construtor com três parâmetros. */
    public Coordinate (double lon, double lat, String hour)
    {
        this.longitudeX = lon;
        this.latitudeY = lat;
        this.hour = hour;
        this.coordinate = new Point(lon, lat);
        this.clusterId = NULL_CLUSTER_ID;
        this.visited = false;
    }

    /**Construtor com quatro parâmetros. */
    public Coordinate (double lon, double lat, String date, String hour)
    {
        this.longitudeX = lon;
        this.latitudeY = lat;
        this.hour = hour;
        this.date = date;
        this.coordinate = new Point(lon, lat);
        this.clusterId = NULL_CLUSTER_ID;
        this.visited = false;
    }
    
    /**Retorna o atributo id. */
    public int getId()
    {
        return this.id;
    }

    /**Seta o atributo id. */
    public void setId(int id)
    {
        this.id = id;
    }

    /**Retorna o valor do atributo collar. */
    public String getCollar()
    {
        return this.collar;
    }
    
    public void setCollar(String collar)
    {
        this.collar = collar;
    }
    
    /**Retorna o hora em que o ponto foi captado. */
    public String getHour()
    {
        return this.hour;
    }

    /**Seta o valor da hora. */
    public void setHour(String h)
    {
        this.hour = h;
    }

    /**Retorna o hora que o ponto foi captado. */
    public String getDate()
    {
        return date;
    }

    /**Seta o valor da hora. */
    public void setDate(String d)
    {
        this.date = d;
    }

    /**Retorna o valor do grau da latitude. */
    public double getLatitudeY()
    {
        return latitudeY;
    }

    /**Seta o valor do grau da latitude. */
    public void setLatitudeY(double l)
    {
        this.latitudeY = l;
        this.coordinate.setY(l);
    }

    /**Retorna o valor do grau da longitude. */
    public double getLongitudeX()
    {
        return longitudeX;
    }

    /**Seta o valor do grau da longitude. */
    public void setLongitudeX(double l)
    {
        this.longitudeX = l;
        this.coordinate.setX(l);
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
        this.longitudeX = c.getX();
        this.latitudeY = c.getY();
    }

    /**Retorna o valor do atributo speed. */
    public double getSpeed()
    {
        return this.speed;
    }

    /**Seta o valor do atributo speed. */
    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    /**Retorna o valor do atributo clusterId. */
    public int getClusterId()
    {
        return this.clusterId;
    }

    /**Seta o valor do atributo clusterId. */
    public void setClusterId(int id)
    {
        this.clusterId = id;
    }
    
    /**Retorna o valor do atributo visited. */
    public boolean getVisited()
    {
        return this.visited;
    }

    /**Seta o valor do atributo visited. */
    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }
    
    /**Retorna o valor do atributo timeIndex. */
    public int getTimeIndex()
    {
        return this.timeIndex;
    }

    /**Seta o valor do atributo timeIndex. */
    public void setTimeIndex(int index)
    {
        this.timeIndex = index;
    }
    
    
    
 
    
    /**Converter o grau para radianos. 
    @param value - o valor em graus.
    @return o valor convertido em radianos.*/
    public double toRad(double value) 
    {
       return value * Math.PI/180;
    }
    
    /**Mostra a distância entre a a coordenada (this) e a recebida pelo parâmetro em metros. */
    public double distance(Coordinate c)
    {
        int EARTH_RADIUS_KM = 6371;
        double dLat = toRad(c.latitudeY) - toRad(this.latitudeY);
        double dLong = toRad(c.longitudeX) - toRad(this.longitudeX);
	            
	double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(toRad(this.latitudeY)) * Math.cos(toRad(c.latitudeY)) * Math.sin(dLong / 2) * Math.sin(dLong / 2);
        double d = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        
        return (EARTH_RADIUS_KM * d * 1000);
    }        
    
}