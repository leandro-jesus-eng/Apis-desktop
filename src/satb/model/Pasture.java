package satb.model;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.postgis.Point;
import org.postgis.Polygon;

/**Classe representa o pasto/ambiente.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class Pasture 
{
    //Atributos
    private int id;
    private String name;
    private double hectars;
    private Polygon areaCoordinates;
    
    /**Construtor Default. */
    public Pasture(){}
    
    /**Construtor com 3 parâmetros. */
    public Pasture(int id, String name, double hec)
    {
        this.id = id;
        this.name = name;
        this.hectars = hec;
        this.areaCoordinates = new Polygon();
    }
    
   /**Construtor com 4 parâmetros. */
    public Pasture(int id, String name, double hec, String area) throws SQLException
    {
        this.id = id;
        this.name = name;
        this.hectars = hec;
        this.areaCoordinates = new Polygon(area);
    } 
   
   /**Retorna o valor de id. */
   public int getId() 
   {
        return id;
   } 
   
   /**Seta o valor de id. */
   public void setId(int id) 
   {
        this.id = id;
   }
    
   /**Retorna o valor do atributo name. */
   public String getName() 
   {
        return name;
   } 
        
   /**Seta o valor de name. */
   public void setName(String name) 
   {
       this.name = name;
   } 

   /**Retorna o valor de hectars. */
   public double getHectars() 
   {
      return hectars;
   } 
        
   /**Seta o valor de hectars. */
   public void setHectars(double hec) 
   {
      this.hectars = hec;
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
