package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.Coordinate;
import satb.model.Database;

/**Classe com manipulação de métodos de inserção e consulta de dados da classe Clustering
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class ClusteringDAO 
{
    //Atributos
    private Database base;  
    private String hourSQL;
    private String dateSQL;
    private String collarSQL;
    
    /**Construtor Default.*/
    public ClusteringDAO(){}
  
    /**Retorna o atributo hourSQL.*/
    public String getHourSQL()
    {
        return this.hourSQL;
    }
    
    /**Seta o atributo hourSQL.*/
    public void setHourSQL(String s1, String s2, String s3, String s4)
    {
        if(!s1.isEmpty())
        {
            String hour = "AND hour <= '";
            hour = hour.concat(s1);
            hour = hour.concat("'");
            this.hourSQL = hour;
        }
        else if(!s2.isEmpty())
        {
            String hour = "AND hour >= '";
            hour = hour.concat(s2);
            hour = hour.concat("'");
            this.hourSQL = hour;
        }
        else if(!s3.isEmpty() && !s4.isEmpty())
        {
            String hour = "AND hour BETWEEN '";
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
    
    
    /**Retorna o atributo collarSQL. */
    public String getCollarSQL()
    {
        return this.collarSQL;
    }
    
    
   /**Seta o atributo collarSQL. */
   public void setCollarSQL(ObservableList<String> collars)
   {
        collarSQL = "";
        Iterator<String> iterator = collars.iterator();
        
        while(iterator.hasNext())
        {
            collarSQL = collarSQL.concat("collar = '");
            collarSQL = collarSQL.concat(iterator.next());
            
            if(iterator.hasNext())
            {
                collarSQL = collarSQL.concat("' OR ");
            }
            else
            {
                collarSQL = collarSQL.concat("' ");
            }           
        }   
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
    
    
   /**Método que retorna o número de elementos selecionados. */ 
   public int getNumberOfElements() throws Exception 
   {
          int points = 0;
          base = new Database();
          // iniciando a conexao
          System.out.println("Conectado e preparando o número de elementos presentes nos colares");
          Statement stmt = null;
          ResultSet rs;
          try 
          {
              stmt = base.getConnection().createStatement();
              String sql = "SELECT COUNT(*) AS points FROM animal_position WHERE "
              + this.getCollarSQL() + "" + this.getDateSQL() + " " + this.getHourSQL() + "";
              
              System.out.println(sql);
              
              rs = stmt.executeQuery(sql);          
 
              while (rs.next()) 
              {
                 points = rs.getInt("points");            
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
          return points;		
    } 
    
    
    /**Método que seleciona as coordenadas. */
    public ObservableList<Coordinate> selectCoordinates() throws Exception 
    {
          ObservableList<Coordinate> list = FXCollections.observableArrayList();
          base = new Database();
          // iniciando a conexao
          System.out.println("Conectado e preparando listagem das coordenadas");
          Statement stmt = null;
          ResultSet rs;
          try 
          {
              stmt = base.getConnection().createStatement();
              String sql = "SELECT id, longitude, latitude, hour FROM animal_position WHERE " 
              + this.getCollarSQL() + "" + this.getDateSQL() + " " + this.getHourSQL() + "ORDER by id"; 
              
              System.out.println(sql);
         
              rs = stmt.executeQuery(sql);

              while (rs.next()) 
              {
                  Coordinate c = new Coordinate(rs.getDouble("longitude"), rs.getDouble("latitude"), rs.getString("hour")); 
                  c.setId(rs.getInt("id"));
                  list.add(c);            
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
          return list;		
    }
   
}