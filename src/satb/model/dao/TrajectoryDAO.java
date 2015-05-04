package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.Area;
import satb.model.AreaShadow;
import satb.model.ClusterPoints;
import satb.model.Coordinate;
import satb.model.Database;
import satb.model.Move;
import satb.model.Stop;
import satb.model.Trajectory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.postgis.Point;

/**Classe com manipulação de métodos de inserção e consulta de dados da classe Trajectory.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class TrajectoryDAO 
{
    //Atributos
    private Database base;
    
    /**Construtor Default. */
    public TrajectoryDAO(){}
    
    
    /**Recebe os pontos de um ClusterPoints e retorna uma Linestring formatada. */     
    private static String getLineString(ClusterPoints cluster)
    {
        String line = "LINESTRING(";
        Iterator<Coordinate> iterator = cluster.points.iterator();
        
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
    
    
    /**Método busca todos os colares cadastrados. */
    public ObservableList<String> selectCollar() throws Exception 
    {
        ObservableList<String> collars = FXCollections.observableArrayList();
	base = new Database();
	// iniciando a conexao
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT collar FROM trajectory");
            while (rs.next()) 
            {
                String word = rs.getString("collar");
                if(!collars.contains(word))
                {
                    collars.add(word);
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
	return collars;		
    }
    
    
    /**Método que retorna o Id de uma trajetória. */
    public int selectId(String collar, String date) throws Exception 
    {
          int trajectoryId = 0;
          base = new Database();
          // iniciando a conexao
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              ResultSet rs = stmt.executeQuery("SELECT id FROM trajectory WHERE "
              + "collar = '" + collar + "' AND date = '" + date + "'");
              while (rs.next()) 
              {
                   trajectoryId = rs.getInt("id");
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
           return trajectoryId;			
    }
    
    
    /**Método que retorna as datas relacionadas ao colar. */
    public ObservableList<String> selectDate(String collar) throws Exception 
    {
           ObservableList<String> dates = FXCollections.observableArrayList();
           base = new Database();
           // iniciando a conexao
           Statement stmt = null;
           try 
           {
                stmt = base.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("SELECT date FROM animal_position WHERE collar = '" + collar + "'");
                   
                while (rs.next()) 
                {
                    String word = rs.getString("date");
                    if(!dates.contains(word))
                    {
                        dates.add(word);
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
           return dates;		
     }
    
    
    /**Seleciona no Banco todas as trajetórias semânticas relacionadas ao colar. */
    public ObservableList<Trajectory> selectAll(String collar) throws Exception
    {
       ObservableList<Trajectory> trajectoryList = FXCollections.observableArrayList();
       base = new Database();
       // iniciando a conexao
       Statement stmt = null;
       try 
       {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM trajectory WHERE collar = '" + collar + "'");
            while (rs.next()) 
            {
                Trajectory t = new Trajectory(rs.getString("collar"), rs.getString("begin_hour"), rs.getString("end_hour"), rs.getString("date"), rs.getString("gap_time"));                
                t.setId(rs.getInt("id"));
                t.setGPSPoints(this.selectTrajectory(rs.getString("collar"), rs.getString("date")));
                trajectoryList.add(t);
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
        return trajectoryList;			
    }
        

    /**Método que retorna os Stops cadastrados. */
    public ObservableList<Stop> selectStops(int trajectoryId, String place) throws Exception 
    {
       ObservableList<Stop> stops = FXCollections.observableArrayList();
       base = new Database();
       // iniciando a conexão
       Statement stmt = null;
       try 
       {
            stmt = base.getConnection().createStatement();
            String sql = "SELECT name, begin_hour, end_hour FROM trajectory_stop "
            + "WHERE trajectory_id = '" + trajectoryId + "' AND name = '" + place + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) 
            {
                Stop st = new Stop(rs.getString("name"), rs.getString("begin_hour"), rs.getString("end_hour"));
                stops.add(st);
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
        return stops;		
   }
   
   
   /**Método que retorna os Stops relacionados a sombra cadastrados. */
   public ObservableList<Stop> selectShadowStops(int trajectoryId) throws Exception 
   {
       ObservableList<Stop> shadowStops = FXCollections.observableArrayList();
       base = new Database();
       // iniciando a conexão
       Statement stmt = null;
       try 
       {
            stmt = base.getConnection().createStatement();
            String sql = "SELECT name, begin_hour, end_hour FROM trajectory_shadow "
            + "WHERE trajectory_id = '" + trajectoryId + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) 
            {
                Stop st = new Stop(rs.getString("name"), rs.getString("begin_hour"), rs.getString("end_hour"));
                shadowStops.add(st);
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
        return shadowStops;		
   } 
   
    
    /**Método que insere uma trajetória com a semântica implantada. */
    public void insertTrajectory(Trajectory t) throws Exception
    {
          base = new Database();
          // iniciando a conexao
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              String sql = "INSERT INTO trajectory (collar, date, gap_time, begin_hour, end_hour)"
              +" VALUES ('"+ t.getCollar()  + "', '" + t.getDate() + "', '" + t.getGap() + "', '" + t.getBeginHour() + "', '" + t.getEndHour() + "')";
              stmt.execute(sql);                
          }
          catch (SQLException e) 
          {
              System.out.println(e.getMessage());
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
    }
    
    
    /**Método que insere um move com a semântica implantada. */
    public void insertMove(Move m, int trajectoryId) throws Exception
    {
          base = new Database();
          // iniciando a conexao
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              stmt.execute("INSERT INTO trajectory_move (trajectory_id, begin_hour, end_hour)"
              +" VALUES ('"+ trajectoryId  + "', '" + m.getStartTime() + "', '" + m.getEndTime() + "')");                
          }
          catch (SQLException e) 
          {
              System.out.println(e.getMessage());
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
    }      
    
    
    /**Método que insere um stop com a semântica implantada. */
    public void insertStop(Stop s, int trajectoryId) throws Exception
    {
          base = new Database();
          // iniciando a conexao
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              String sql = "INSERT INTO trajectory_stop (trajectory_id, area_id, name, begin_hour, end_hour)"
              +" VALUES ('"+ trajectoryId  + "', '" + s.getAreaId() + "', '" + s.getAreaName() + "', '" + s.getStartTime() + "', '" + s.getEndTime() + "')";
              stmt.execute(sql);                
          }
          catch (SQLException e) 
          {
              System.out.println(e.getMessage());
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
    } 
    
    
    /**Método que insere um stop com a semântica implantada. */
    public void insertShadowStop(Stop s, int trajectoryId) throws Exception
    {
          base = new Database();
          // iniciando a conexao
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              String sql = "INSERT INTO trajectory_shadow (trajectory_id, area_shadow_id, name, begin_hour, end_hour)"
              +" VALUES ('"+ trajectoryId  + "', '" + s.getAreaId() + "', '" + s.getAreaName() + "', '" + s.getStartTime() + "', '" + s.getEndTime() + "')";
              stmt.execute(sql);                
          }
          catch (SQLException e) 
          {
              System.out.println(e.getMessage());
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
    }
    
    
    /**Método que insere um stop com a semântica implantada. */
    public void insertHotSpot(ClusterPoints cluster, int trajectoryId) throws Exception
    {
          base = new Database();
          // iniciando a conexao
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              String sql = "INSERT INTO trajectory_hot_spots (trajectory_id, trajectory_line, begin_hour, end_hour)"
              +" VALUES ('"+ trajectoryId  + "', '" + getLineString(cluster) + "', '" + cluster.points.get(0).getHour() + "', '" + cluster.points.get(cluster.points.size() - 1).getHour() + "')";
              stmt.execute(sql);                
          }

          catch (SQLException e) 
          {
              System.out.println(e.getMessage());
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
   }
    
 
   /**Remove os dados de uma trajetória no banco de dados. */
   public void removeTrajectory(int id) throws Exception
   {      
          base = new Database();
          // iniciando a conexao
          System.out.println("conectado e preparando a remoção de uma trajetória com semântica");
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              stmt.execute("DELETE FROM trajectory WHERE id = '"+ id + "'");               
          }
          catch (SQLException e) 
          {
                  System.out.println(e.getMessage());
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
    }
    
    
    /**Remove os dados dos moves de uma trajetória no banco de dados. */
    public void removeMoves (int trajectoryId) throws Exception
    {      
          base = new Database();
          // iniciando a conexao
          System.out.println("Preparando a remoção de moves de uma trajetória com semântica");
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              stmt.execute("DELETE FROM trajectory_move WHERE trajectory_id = '"+ trajectoryId + "'");               
          }
          catch (SQLException e) 
          {
                  System.out.println(e.getMessage());
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
    }
    
    
    
    /**Remove os dados dos stops de uma trajetória no banco de dados. */
    public void removeStops (int trajectoryId) throws Exception
    {      
          base = new Database();
          // iniciando a conexao
          System.out.println("Preparando a remoção de stops de uma trajetória com semântica");
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              stmt.execute("DELETE FROM trajectory_stop WHERE trajectory_id = '"+ trajectoryId + "'");               
          }
          catch (SQLException e) 
          {
              System.out.println(e.getMessage());
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
    }
    
   
    /**Remove os dados dos shadows de uma trajetória no banco de dados. */
    public void removeShadow (int trajectoryId) throws Exception
    {      
          base = new Database();
          // iniciando a conexao
          System.out.println("Preparando a remoção de stops de uma trajetória com semântica");
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              stmt.execute("DELETE FROM trajectory_shadow WHERE trajectory_id = '"+ trajectoryId + "'");               
          }
          catch (SQLException e) 
          {
              System.out.println(e.getMessage());
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
    }
    
    
    /**Remove os hotspots de uma trajetória no banco de dados. */
    public void removeHotSpots (int trajectoryId) throws Exception
    {      
          base = new Database();
          // iniciando a conexao
          System.out.println("Preparando a remoção de moves de uma trajetória com semântica");
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              stmt.execute("DELETE FROM trajectory_hot_spots WHERE trajectory_id = '"+ trajectoryId + "'");               
          }
          catch (SQLException e) 
          {
                  System.out.println(e.getMessage());
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
    }

    
    /**Verifica se um ponto está contido em uma área. */
    public Area intersectArea (Point point, int pastureId) throws Exception 
    {         
         Area area = null;
         base = new Database();
         // iniciando a conexao
         System.out.println("Conectado e preparado para ver se o ponto se intersecta em uma das áreas.");
         Statement stmt = null; 
         try 
         {
             stmt = base.getConnection().createStatement();
             String sql = "SELECT area.id, area.name, area.minimum_time, ST_AsText(area.area_coordinates) AS coordinates FROM area WHERE area.pasture_id = '" + pastureId + "' "
             + "AND ST_Intersects('" + point  + "', area.area_coordinates) = true";
             
             ResultSet rs = stmt.executeQuery(sql);
             while (rs.next()) 
             { 
                area = new Area(pastureId, rs.getString("name"), rs.getString("minimum_time"), rs.getString("coordinates"));
                area.setId(rs.getInt("id"));
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
    

    /**Verifica se um ponto está contido em uma área. */
    public boolean intersects (Point point, int areaId) throws Exception 
    {         
         boolean intersect = false;
         base = new Database();
         // iniciando a conexao
         System.out.println("Preparado a função de interseção entre ponto e área");
         Statement stmt = null; 
         try 
         {
             stmt = base.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ST_Intersects('" + point  + "', area.area_coordinates) AS intersection "
             + "FROM area WHERE  area.id = '" + areaId + "'");
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
    
    
    /**Verifica se um ponto está contido em uma área. */
    public AreaShadow intersectAreaShadow (Coordinate point, int pastureId) throws Exception 
    {         
         AreaShadow area = null;
         base = new Database();
         // iniciando a conexao
         System.out.println("Preparado a função para intersecção entre um ponto e uma áreas de sombra.");
         Statement stmt = null; 
         try 
         {
             stmt = base.getConnection().createStatement();
             String sql = "SELECT id, tree_id, name, hour_begin, hour_end, minimum_time, ST_AsText(area_coordinates) AS coords FROM area_shadow "
             + "WHERE pasture_id = '" + pastureId + "' AND ST_Intersects('" + point.getCoordinate()  + "', area_coordinates) = true "
             + "AND '" + point.getHour() + "' BETWEEN hour_begin AND hour_end";
             
             ResultSet rs = stmt.executeQuery(sql);
             while (rs.next()) 
             { 
                area = new AreaShadow(pastureId, rs.getInt("tree_id"), rs.getString("name"), rs.getString("hour_begin"), rs.getString("hour_end"), rs.getString("coords"));
                area.setMinTime(rs.getString("minimum_time"));
                area.setId(rs.getInt("id"));
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
    
   
    /**Verifica se um ponto está contido em uma área. */
    public boolean intersectShadow (Coordinate point, AreaShadow shadow) throws Exception 
    {         
         boolean intersect = false;
         base = new Database();
         // iniciando a conexao
         System.out.println("Preparado a função de interseção entre ponto e área de sombra");
         Statement stmt = null; 
         try 
         {
             stmt = base.getConnection().createStatement();
             String sql = "SELECT ST_Intersects('" + point.getCoordinate() + "', area_coordinates) AS intersection "
             + "FROM area_shadow WHERE  id = '" + shadow.getId() + "'";
             ResultSet rs = stmt.executeQuery(sql);

             while (rs.next()) 
             { 
                 intersect = rs.getBoolean("intersection");
             }    
             
             if(intersect)
             {
                 DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
                 DateTime dateTime = formatter.parseDateTime(point.getHour());
                 DateTime secondDateTime = formatter.parseDateTime(shadow.getHourEnd());
                 
                 if(dateTime.isAfter(secondDateTime))
                 {
                     intersect = false;
                 }
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
    
    
    
   /**Método a menor duração de um conjunto de tuplas candidatos a parada. */ 
   public String getMinimunTimeValue(int pastureid) throws Exception 
   {
       String minTime = null;
       base = new Database();
       // iniciando a conexao
       System.out.println("Preparado para retornar valor mínimo de tempo");
       Statement stmt = null;
       try 
       {
           stmt = base.getConnection().createStatement();
           ResultSet rs = stmt.executeQuery("SELECT MIN(minimum_time) AS minTime FROM area WHERE pasture_id = '" + pastureid + "'");
           while (rs.next()) 
           {          
                minTime = rs.getString("minTime");
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
       return minTime;			
  }
   
   /**Retorna a data e o horário em que o animal na área. */
   public double selectDistance (String collar, String date) throws Exception 
   {
        double distance = 0;
        base = new Database();
        // iniciando a conexao
        Statement stmt = null; 
        try 
        {
            stmt = base.getConnection().createStatement();
            String sql = "SELECT ST_Length(ST_MakeLine(t.coordinate::geometry)::geography) as distance FROM "
            + "(SELECT date, coordinate FROM animal_position WHERE collar = '" + collar + "' AND date = '" + date + "') as t GROUP BY t.date";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) 
            { 
                 distance = rs.getDouble("distance");
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
        return distance;			
   } 
  
   
  /**Método selectTrajectories através de um parâmetro. */
  public ObservableList<Trajectory> selectTrajectories (String collar, ObservableList<String> dates) throws Exception 
  {
      ObservableList<Trajectory> trajectories = FXCollections.observableArrayList(); 
        
      base = new Database();
      // iniciando a conexao
      System.out.println("Conectado e preparando listagem das trajetórias");
      Statement stmt = null;
      ResultSet rs;
      try 
      {
          stmt = base.getConnection().createStatement();
          int id = 1;
          for(String date : dates)
          {
              ObservableList<Coordinate> trajectory = FXCollections.observableArrayList(); 
              rs = stmt.executeQuery("SELECT id, latitude, longitude, hour FROM animal_position "
              + "WHERE collar = '" + collar + "' AND date = '" + date + "'  ORDER by id");          
          
              while (rs.next()) 
              {                
                Coordinate c = new Coordinate(rs.getDouble("longitude"), rs.getDouble("latitude"), date, rs.getString("hour"));
                c.setId(rs.getInt("id"));
                trajectory.add(c);                              
              }
              Trajectory t = new Trajectory(trajectory);
              t.setId(id);
              trajectories.add(t);
              id++;
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
      return trajectories;		
  } 
    
  
  /**Método selectTrajectory através de dois parâmetros. */
  public ObservableList<Coordinate> selectTrajectory(String collar, String date) throws Exception 
  {
      ObservableList<Coordinate> trajectory = FXCollections.observableArrayList();
      base = new Database();
      // iniciando a conexao
      System.out.println("conectado e preparando listagem das coordenadas de uma trajetória");
      Statement stmt = null;
      ResultSet rs;
      try 
      {
          stmt = base.getConnection().createStatement();
          rs = stmt.executeQuery("SELECT id, latitude, longitude, hour FROM animal_position "
          + "WHERE collar = '" + collar + "' AND date = '" + date + "' ORDER by id");          

          int timeIndex = 0;
          while (rs.next()) 
          {
              Coordinate c = new Coordinate(rs.getDouble("longitude"), rs.getDouble("latitude"), rs.getString("hour"));
              c.setId(rs.getInt("id"));
              c.setDate(date);
              c.setTimeIndex(timeIndex);
              trajectory.add(c);   
              timeIndex++;
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
      return trajectory;		
   }
  
}
