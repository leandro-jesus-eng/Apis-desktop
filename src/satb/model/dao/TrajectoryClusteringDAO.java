package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.Coordinate;
import satb.model.Database;
import org.postgis.LineString;
import org.postgis.Point;

/**Classe com manipulação de métodos de inserção e consulta 
* de dados da classe TrajectoryClusteringDAO.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class TrajectoryClusteringDAO 
{
    //Atributos
    private Database base;  
    private String hourSQL;
    private String dateSQL;
    
    /**Construtor Default. */
    public TrajectoryClusteringDAO(){}
  
    /**Retorna o atributo hourSQL. */
    public String getHourSQL()
    {
        return this.hourSQL;
    }
    
    /**Seta o atributo hourSQL.*/
    public void setHourSQL(String s1, String s2, String s3, String s4)
    {
        if(!s1.isEmpty())
        {
            String hour = "AND begin_hour <= '";
            hour = hour.concat(s1);
            hour = hour.concat("'");
            this.hourSQL = hour;
        }
        else if(!s2.isEmpty())
        {
            String hour = "AND begin_hour >= '";
            hour = hour.concat(s2);
            hour = hour.concat("'");
            this.hourSQL = hour;
        }
        else if(!s3.isEmpty() && !s4.isEmpty())
        {
            String hour = "AND begin_hour BETWEEN '";
            hour = hour.concat(s3);
            hour = hour.concat("' AND '");
            hour = hour.concat(s4);
            hour = hour.concat("'");
            this.hourSQL = hour;
        }
        else
        {
           this.hourSQL = " ";
        }
    }
    
    
    /**Retorna o atributo dateSQL. */
    public String getDateSQL()
    {
        return this.dateSQL;
    }
    
    
    /**Seta o atributo dateSQL. */
    public void setDateSQL(String s1, String s2, String s3)
    {      
        if(!s1.isEmpty())
        {           
            String date = "AND date = '";
            date = date.concat(s1);
            date = date.concat("'");
            this.dateSQL = date;
        }
        else if(!s2.isEmpty() && !s3.isEmpty())
        {
            String date = "AND to_timestamp(date, 'DD/MM/YY')::date BETWEEN to_timestamp('";
            date = date.concat(s2);
            date = date.concat("', 'DD/MM/YY')::date AND to_timestamp('");
            date = date.concat(s3);
            date = date.concat("', 'DD/MM/YY')::date");
            this.dateSQL = date;
        }
        else
        {
            this.dateSQL = " ";
        }
    }


    public ObservableList<Coordinate> getPointsInLine(String Linestring) throws SQLException
    {
       ObservableList<Coordinate> pointList = FXCollections.observableArrayList();
       LineString line = new LineString(Linestring); 
       
       Point[] p = line.getPoints();

       for(int i = 0; i < p.length; i++)
       {
           Point point = p[i];
           Coordinate coord = new Coordinate(point.getX(), point.getY());
           pointList.add(coord);
       }

       return pointList;
    }
    
    
    /**Método que retorna o Id de uma trajetória. */
    public int selectId(String collar) throws Exception 
    {
          int trajectoryId = 0;
          base = new Database();
          // iniciando a conexao
          System.out.println("Conectado e preparado para retornar o id de uma trajetória");
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              ResultSet rs = stmt.executeQuery("SELECT id FROM trajectory WHERE collar = '" + collar + "'");
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
    
    
 
    public ObservableList<String> selectIds(String collar) throws Exception 
    {
          ObservableList<String> idList = FXCollections.observableArrayList();
          base = new Database();
          // iniciando a conexao
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              ResultSet rs = stmt.executeQuery("SELECT id FROM trajectory WHERE collar = '" + collar + "'");
              while (rs.next()) 
              {
                   idList.add(rs.getString("id"));
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
         return idList;			
    }
      
    
    /**Método busca todos os colares cadastrados. */
    public ObservableList<String> selectCollar() throws Exception 
    {
        ObservableList<String> collars = FXCollections.observableArrayList();
	base = new Database();
	// iniciando a conexao
	System.out.println("Conectado e preparando listagem dos colares");
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
  
    
   /**Método que seleciona as coordenadas. */
   public ObservableList<Coordinate> selectTrajCoordinates(ObservableList<String> idList) throws Exception 
   {
        ObservableList<Coordinate> pointsList = FXCollections.observableArrayList();
        base = new Database();
        // iniciando a conexao
        Statement stmt = null;
        ResultSet rs;
        try 
        {
            for(String id : idList)
            {
                stmt = base.getConnection().createStatement();
                
                rs = stmt.executeQuery("SELECT ST_AsText(trajectory_line) AS points FROM "
                + "trajectory_hot_spots WHERE trajectory_id = '" + id  + "'");

                while (rs.next()) 
                {
                    pointsList.addAll(getPointsInLine(rs.getString("points")));           
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
         return pointsList;		
    }
    
}
