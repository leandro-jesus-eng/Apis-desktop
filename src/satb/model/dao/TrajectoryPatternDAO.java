package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.Coordinate;
import satb.model.Database;
import org.postgis.LineString;
import org.postgis.MultiPoint;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;

/**Classe com manipulação de métodos de inserção e consulta de dados da classe TrajectoryPatternDAO.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class TrajectoryPatternDAO 
{
     //Atributos
    private Database base;   

    /**Construtor Default. */
    public TrajectoryPatternDAO() {}
    
     /**Recebe os pontos de um ClusterPoints e retorna uma Linestring formatada. */     
    private static String getLineString(ObservableList<Coordinate> trajectory)
    {
        String line = "LINESTRING(";
        Iterator<Coordinate> iterator = trajectory.iterator();
        
        while(iterator.hasNext())
        {
            Coordinate c = iterator.next();
            line = line.concat(String.valueOf(c.getLongitudeX())); 
            line = line.concat(" ");
            line = line.concat(String.valueOf(c.getLatitudeY()));
            
            if(iterator.hasNext())
            {
                line = line.concat(",");

            }
            else
            {
                line = line.concat(")");
            }
           
        }
        
        return line;
    }
    
    
    /**Retorna todas as áreas de um pasto. */    
    public Polygon buildAreaObject(Point point, double radius) throws Exception 
    {
         Polygon area = null;
         base = new Database();
         // iniciando a conexao
         Statement stmt = null;
         try 
         {
              stmt = base.getConnection().createStatement();  
              String sql = "SELECT ST_AsText(ST_Buffer(ST_GeomFromText('" + point +"'), '" + radius + "', 'quad_segs=8')) as targetArea";
              ResultSet rs = stmt.executeQuery(sql);
              while (rs.next()) 
              {
                area = new Polygon(rs.getString("targetArea"));
              }
         }
         catch (SQLException e) 
         {
              System.out.println(e.getMessage());
              return null;
         } 
         finally 
         {
              try 
              {
                  stmt.close();
                  base.closeConnection();
              } 
              catch (SQLException e) 
              {
                  System.out.println("Erro ao desconectar");
              }
          } 
          return area;			
    }
    
    
    /**Retorna uma área ao redor de uma linha. */    
    public Polygon buildLineArea(LineString line, double radius) throws Exception 
    {
         Polygon lineArea = null;
         base = new Database();
         // iniciando a conexao
         Statement stmt = null;
         try 
         {
              stmt = base.getConnection().createStatement();  
              String sql = "SELECT ST_AsText(ST_Buffer(ST_GeomFromText('SRID=4326;" + line + "')"
              + ", 0.00001, 'endcap=square join=round')) AS lineArea";
              
              ResultSet rs = stmt.executeQuery(sql);
              while (rs.next()) 
              {
                lineArea = new Polygon(rs.getString("lineArea"));
              }
         }
         catch (SQLException e) 
         {
              System.out.println(e.getMessage());
              return null;
         } 
         finally 
         {
              try 
              {
                  stmt.close();
                  base.closeConnection();
              } 
              catch (SQLException e) 
              {
                  System.out.println("Erro ao desconectar");
              }
          } 
          return lineArea;			
    }
    
    
    /**Retorna todas as áreas de um pasto. */    
    public ObservableList<Coordinate> buildPointGrid(Polygon area, int distGrid) throws Exception 
    {
         ObservableList<Coordinate> grid = FXCollections.observableArrayList();
         base = new Database();
         // iniciando a conexao
         Statement stmt = null;
         try 
         {
              stmt = base.getConnection().createStatement();  
              String sql = "SELECT ST_AsText((ST_Dump(makegrid('" + area + "', '" + distGrid*10 + "', 4326))).geom) "
              + "as the_geom;";
              ResultSet rs = stmt.executeQuery(sql);
              int index = 0;
              while (rs.next()) 
              {
                    Coordinate c = new Coordinate(rs.getString("the_geom"));
                    c.setTimeIndex(index);
                    index++;
                    grid.add(c);
              }
         }
         catch (SQLException e) 
         {
              System.out.println(e.getMessage());
              return null;
         } 
         finally 
         {
              try 
              {
                  stmt.close();
                  base.closeConnection();
              } 
              catch (SQLException e) 
              {
                  System.out.println("Erro ao desconectar");
              }
          } 
          return grid;			
    }

    
    /**Verifica se a trajetória passa pela área passada pelo parâmetro. */
    public boolean intersectTrajectInArea (ObservableList<Coordinate> trajectory, Polygon targetArea) throws Exception 
    {         
         boolean intersect = false;
         base = new Database();
         // iniciando a conexao
         Statement stmt = null;

         try 
         {
             stmt = base.getConnection().createStatement();
             String sql = "SELECT ST_Intersects('" + getLineString(trajectory) + "', '" + targetArea + "') "
                     + "AS intersection";

             ResultSet rs = stmt.executeQuery(sql);
             while (rs.next()) 
             { 
                 intersect = rs.getBoolean("intersection");
             }
         }
         catch (SQLException e) 
         {
              System.out.println(e.getMessage());
              return false;
         } 
         finally 
         {
              try 
              {
                  stmt.close();
                  base.closeConnection();
              } 
              catch (SQLException e) 
              {
                  System.out.println("Erro ao desconectar");
              }
          } 
          return intersect;			
    }
    
    
    /**Verifica se a trajetória passa pela área Objeto. */
    public ObservableList<Coordinate> pointsInArea(ObservableList<Coordinate> trajectory, Polygon area) throws Exception 
    {    
         ObservableList<Coordinate> pointsIn = FXCollections.observableArrayList();

         base = new Database();
         // iniciando a conexao
         String sql;
         Statement stmt = null; 
         try 
         {
             //para cada ponto na trajetoria, verifica se ela faz intersecção com a área.
             for(Coordinate c : trajectory)
             {
                 stmt = base.getConnection().createStatement();
                 sql ="SELECT ST_Intersects('" + c.getCoordinate() + "','" + area + "') AS subTrajectory";

                 ResultSet rs = stmt.executeQuery(sql);
                 while(rs.next())
                 {
                     if(rs.getBoolean("subTrajectory"))
                     {
                         pointsIn.add(c);
                     }
                 }
             }    
            
         }
         catch (SQLException e) 
         {
              System.out.println(e.getMessage());
              return null;
         } 
         finally 
         {
              try 
              {
                  stmt.close();
                  base.closeConnection();
              } 
              catch (SQLException e) 
              {
                  System.out.println("Erro ao desconectar");
              }
          } 
          return pointsIn;		
    } 
    

    /**Verifica se a trajetória entre dois pontos passa pela área passada pelo parâmetro. */
    public boolean intersectSubInTarget(LineString line, Polygon area) throws Exception 
    {         
         boolean intersect = false;
         base = new Database();
         // iniciando a conexao
         String sql;
         Statement stmt = null; 
         try 
         {
             stmt = base.getConnection().createStatement();
             sql ="SELECT ST_Intersects('" + line + "', '" + area + "') AS intersect";

             ResultSet rs = stmt.executeQuery(sql);
             while (rs.next()) 
             { 
                 intersect = rs.getBoolean("intersect");
             }
         }
         catch (SQLException e) 
         {
              System.out.println(e.getMessage());
              return false;
         } 
         finally 
         {
              try 
              {
                  stmt.close();
                  base.closeConnection();
              } 
              catch (SQLException e) 
              {
                  System.out.println("Erro ao desconectar");
              }
          } 
          return intersect;			
    }
    
    
    /**Verifica se a trajetória entre dois pontos passa pela área passada pelo parâmetro. */
    public Point intersection(ObservableList<Coordinate> trajectory, LineString line) throws Exception 
    {         
         PGgeometry points = null;
         Point p = null;
         MultiPoint multi = null;
         base = new Database();
         // iniciando a conexao
         String sql;
         Statement stmt = null; 
         try 
         {
             stmt = base.getConnection().createStatement();
             sql ="SELECT ST_AsText(ST_Intersection('" + getLineString(trajectory) + "', '" + line + "')) AS point";

             ResultSet rs = stmt.executeQuery(sql);
             while(rs.next())
             {
                 points = new PGgeometry(rs.getString("point"));
             }             
             
             if(points.getGeoType() == 4)
             {
                 multi = new MultiPoint(points.getValue());
                 p =  multi.getFirstPoint();
             }
             else
             {
                 if(points.getGeoType() == 1)
                 {
                      p =  new Point(points.getValue());
                 }
             }
             
         }
         catch (SQLException e) 
         {
              System.out.println(e.getMessage());
              return null;
         } 
         finally 
         {
              try 
              {
                  stmt.close();
                  base.closeConnection();
              } 
              catch (SQLException e) 
              {
                  System.out.println("Erro ao desconectar");
              }
          } 
          return p;			
    }
    
    
    
    /**Retorna o azimute entre dois pontos em graus. */
    public double azimute (Point pointA, Point pointB) throws Exception 
    {         
        double azimute = 0;
        base = new Database();
        // iniciando a conexao
        Statement stmt = null; 
        String sql;
        try 
        {
            stmt = base.getConnection().createStatement();
            sql = "SELECT ST_Azimuth(ST_GeographyFromText('SRID=4326;" + pointA + "'), "
            + "ST_GeographyFromText('SRID=4326;" + pointB + "')) AS azimute";

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) 
            { 
                azimute = rs.getDouble("azimute");
            }

        }
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
            return 0;
        } 
        finally 
        {
            try 
            {
                stmt.close();
                base.closeConnection();
            } 
            catch (SQLException e) 
            {
                System.out.println("Erro ao desconectar");
            }
        } 
        return azimute;			
    } 
    
    
    
    /**Verifica o ponto central (centróide) de uma área(poligono). */
    public String centroid (Polygon area) throws Exception 
    {         
        String centroid = null;
        base = new Database();
        // iniciando a conexao
        Statement stmt = null; 
        String sql;
        try 
        {
            stmt = base.getConnection().createStatement();
            sql = "SELECT ST_AsText(ST_Centroid('" + area  + "')) AS centroid";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) 
            { 
                centroid = rs.getString("centroid");
            }
        }
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
            return null;
        } 
        finally 
        {
            try 
            {
                stmt.close();
                base.closeConnection();
            } 
            catch (SQLException e) 
            {
                System.out.println("Erro ao desconectar");
            }
        } 
        return centroid;			
    } 
   
   
    /**Retorna uma uma LineString do exterior de um polígono. */
    public String exteriorRing (Polygon area) throws Exception 
    {         
        String lineString = null;
        base = new Database();
        // iniciando a conexao
        Statement stmt = null; 
        try 
        {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ST_AsText(ST_ExteriorRing('" + area + "')) AS exteriorLine");
            while (rs.next()) 
            { 
                lineString = rs.getString("exteriorLine");
            }                       
        }
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
            return null;
        } 
        finally 
        {
            try 
            {
                stmt.close();
                base.closeConnection();
            } 
            catch (SQLException e) 
            {
                System.out.println("Erro ao desconectar");
            }
        } 
        return  lineString;			
    }  
   
   
    /**Retorna uma LineString entre dois pontos. */
    public String makeLine (Point p1, Point p2) throws Exception 
    {         
        String makeLine = null;
        base = new Database();
        // iniciando a conexao
        Statement stmt = null; 
        String sql;
        
        try 
        {
            stmt = base.getConnection().createStatement();
            sql = "SELECT ST_AsText(ST_MakeLine('" + p1 + "', '" + p2 + "')) AS makeline";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) 
            { 
                makeLine = rs.getString("makeline");
            }
        }
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
            return null;
        } 
        finally 
        {
            try 
            {
                stmt.close();
                base.closeConnection();
            } 
            catch (SQLException e) 
            {
                System.out.println("Erro ao desconectar");
            }
        } 
        return  makeLine;			
    } 
    
    
    /**Retorna a diferença entre duas áreas. */
    public Polygon diffBetweenAreas (Polygon areaA, Polygon areaB) throws Exception 
    {         
        Polygon diffPolygon = null;
        base = new Database();
        // iniciando a conexao
        Statement stmt = null; 
        String sql;
        try 
        {
            stmt = base.getConnection().createStatement();
            sql = "SELECT ST_AsText(ST_Difference(ST_GeomFromText"
            + "('"+ areaA +"'), ST_GeomFromText('"+ areaB +"'))) AS diffPolygon";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) 
            { 
                diffPolygon = new Polygon(rs.getString("diffPolygon"));
            }
        }
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
            return null;
        } 
        finally 
        {
            try 
            {
                stmt.close();
                base.closeConnection();
            } 
            catch (SQLException e) 
            {
                System.out.println("Erro ao desconectar");
            }
        } 
        return diffPolygon;			
    } 
 
}
