package satb.model;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.postgis.Point;
import org.postgis.Polygon;

/**Classe que representa a Área Sombreada.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class AreaShadow 
{
    //Atributos
    private int id;
    private int pasture;
    private int tree;
    private String name; 
    private String hourBegin; 
    private String hourEnd;
    private String minTime;
    private Polygon areaCoordinates;
    
    /**Construtor Default.*/
    public AreaShadow() {}
      
    /**Construtor com 1 parâmetro. */
    public AreaShadow(String begin)
    {
        this.hourBegin = begin;
        this.areaCoordinates = new Polygon();
    }
       
    /**Construtor com 2 parâmetros. */
    public AreaShadow(String begin, String end)
    {
        this.hourBegin = begin;
        this.hourEnd = end;
        this.areaCoordinates = new Polygon();
    }
    
    
    /**Construtor com 3 parâmetros. */
    public AreaShadow(String begin, String end, String min)
    {
        this.hourBegin = begin;
        this.hourEnd = end;
        this.minTime = min;
        this.areaCoordinates = new Polygon();
    }
    
    /**Construtor com 5 parâmetros. */
    public AreaShadow(int p, int t, String name, String begin, String end, String area) throws SQLException
    {
        this.pasture = p;
        this.tree = t;
        this.name = name;
        this.hourBegin = begin;
        this.hourEnd = end;
        this.areaCoordinates = new Polygon(area);
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
    
    /**Retorna o valor de pasture. */
    public int getPasture()
    {
        return pasture;
    }
       
    /**Seta o valor de pasture. */
    public void setPasture(int pasture)
    {
        this.pasture = pasture;
    }
       
    /**Retorna o valor do tree. */
    public int getTree()
    {
        return tree;
    }
       
    /**Seta o valor do tree. */
    public void setTree(int tree)
    {
        this.tree = tree;
    }
      
    /**Retorna o valor de name. */
    public String getName()
    {
        return name;
    }
       
    /**Seta o valor do name. */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**Retorna o valor de minTime. */
    public String getMinTime()
    {
        return this.minTime;
    }
        
    /**Seta o valor de minTime. */
    public void setMinTime(String min)
    {
        this.minTime = min;
    }
    
    /**Retorna o valor de hourBegin. */
    public String getHourBegin()
    {
        return hourBegin;
    }
        
    /**Seta o valor de hourBegin. */
    public void setHourBegin(String h)
    {
        this.hourBegin = h;
    }
    
    /**Retorna o valor de hourEnd. */
    public String getHourEnd()
    {
        return hourEnd;
    }
    
    /**Seta o valor de hourEnd. */
    public void setHourEnd(String h)
    {
        this.hourEnd = h;
    }
    
    /**Retorna o valor de areaCoordinates. */
    public Polygon getAreaCoordinates() 
    {
      return areaCoordinates;
    } 
        
    /**Seta o valor de areaCoordinates. */
    public void setAreaCoordinates(Polygon areaCoordinates) 
    {
      this.areaCoordinates = areaCoordinates;
    }  
   
    /**Retorna os pontos de uma Polygon em uma coleção ObservableList. */
    public ObservableList<Coordinate> getCoordinates()
    {
       ObservableList<Coordinate> coords = FXCollections.observableArrayList();
       int tam = this.areaCoordinates.numPoints();
       
       for(int i = 0; i < tam; i++)
       {
           Point p = areaCoordinates.getPoint(i);
           Coordinate c = new Coordinate (p.getX(), p.getY());
           coords.add(c);
       }
       
       return coords;
    }
     
    /**Retorna os pontos de uma Polygon em uma coleção ObservableList. */
    public void setCoordinates(ObservableList<Coordinate> coord) throws SQLException
    {
       Polygon area = new Polygon(getPolygon(coord));
       setAreaCoordinates(area);
            
    }
   
    /**Recebe latitude e longitude e retorna uma coordenada do tipo Geometry. */     
    private String getPolygon(ObservableList<Coordinate> p)
    {
        String polygon = "POLYGON((";
        int i = 0, tam = p.size();
        
        for(Coordinate c : p)
        {
            i++;
            polygon = polygon.concat(String.valueOf(c.getLongitudeX()));
            polygon = polygon.concat(" ");
            polygon = polygon.concat(String.valueOf(c.getLatitudeY()));
            if(i != tam)
            {
                 polygon = polygon.concat(",");       
            }
        }
        polygon = polygon.concat("))");
        
        return polygon;
    }
    
}