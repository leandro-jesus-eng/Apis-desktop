package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.Classifier;
import satb.model.Coordinate;
import satb.model.Database;

/**Classe com manipulação de métodos de inserção e consulta de dados da classe Classificação.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class ClassifierDAO 
{
    //Atributos
    private Database base;  
    private String hourSQL;
    private String dateSQL;
    
    /** Construtor Default. */
    public ClassifierDAO(){}
  
    /**Retorna o atributo hourSQL.*/
    public String getHourSQL()
    {
        return this.hourSQL;
    }
    
    /**Seta o atributo hourSQL.*/
    public void setHourSQL(String s)
    {
        this.hourSQL = s;
    }
    
    /**Retorna o atributo dateSQL.*/
    public String getDateSQL()
    {
        return this.dateSQL;
    }
    
    /**Seta o atributo dateSQL.*/
    public void setDateSQL(String d)
    {
        this.dateSQL = d;
    }
  
    /**Adicionando a clausula whereHour na SQL.*/
    public void whereHour(String s1, String s2, String s3, String s4)
    {
        if(!s1.isEmpty())
        {
            String hour = "AND hour <= '";
            hour = hour.concat(s1);
            hour = hour.concat("'");
            this.setHourSQL(hour);
        }
        else if(!s2.isEmpty())
        {
            String hour = "AND hour >= '";
            hour = hour.concat(s2);
            hour = hour.concat("'");
            this.setHourSQL(hour);
        }
        else if(!s3.isEmpty() && !s4.isEmpty())
        {
            String hour = "AND hour BETWEEN '";
            hour = hour.concat(s3);
            hour = hour.concat("' AND '");
            hour = hour.concat(s4);
            hour = hour.concat("'");
            this.setHourSQL(hour);
        }
        else
        {
           this.setHourSQL(" ");
        }
    }
    
    /**Adicionando a claúsula whereDate na SQL.*/
    public void whereDate(String s1, String s2, String s3)
    {
        
        if(s1 != null)
        {           
            String date = "AND date = '";
            date = date.concat(s1);
            date = date.concat("'");
            this.setDateSQL(date);
        }
        else if(!s2.isEmpty() && !s3.isEmpty())
        {
            String date = "AND to_timestamp(date, 'DD/MM/YY')::date BETWEEN to_timestamp('";
            date = date.concat(s2);
            date = date.concat("', 'DD/MM/YY')::date AND to_timestamp('");
            date = date.concat(s3);
            date = date.concat("', 'DD/MM/YY')::date");
            this.setDateSQL(date);        
        }
        else
        {
           this.setDateSQL(" ");
        }
    }
    
    
    /**Retorna o número de pontos no Pasto.*/
    public int selectInPasture(String collar) throws Exception 
    {
         int points = 0;
         base = new Database();
         // iniciando a conexao
         System.out.println("Conectado e preparado os elementos de classificação");
         Statement stmt = null;
         String sql;
         try 
         {
             stmt = base.getConnection().createStatement();
             sql = "SELECT COUNT(animal_position) AS points FROM animal_position WHERE animal_position.collar = '" + collar + "' " + this.getDateSQL() + " " + this.getHourSQL() + "";
             System.out.println(sql);
             ResultSet rs = stmt.executeQuery(sql);
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
    
    
    /**Retorna o número de pontos do colar que estão no pasto.
    @param pastureid - o id do pasto.
    @param collar - o nome do colar.*/
    public int selectInPastureTrue(int pastureid, String collar) throws Exception 
    {
         int points = 0;
         base = new Database();
         // iniciando a conexao
         System.out.println("Conectado e preparado os elementos de classificação");
         Statement stmt = null;
         String sql;
         try 
         {
             stmt = base.getConnection().createStatement();
             sql = "SELECT COUNT(ST_Intersects(animal_position.coordinate, pasture.area_polygon)) AS points "
                  + "FROM animal_position, pasture WHERE animal_position.collar = '" + collar + "' AND pasture.id = '" + pastureid + "' "
                  + "AND ST_Intersects(animal_position.coordinate, pasture.area_coordinates) = '" + true + "' " + this.getDateSQL() + " " + this.getHourSQL() + "";    
         
             System.out.println(sql);
             ResultSet rs = stmt.executeQuery(sql);             
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
    
    
    /**Retorna o número de pontos do colar que estão no pasto.
    @param pastureid - o id do pasto.
    @param collar - o nome do colar.*/
    public ObservableList<Classifier> selectInAreas(int pastureid, String collar) throws Exception 
    {
         ObservableList<Classifier> list = FXCollections.observableArrayList();
         base = new Database();
         // iniciando a conexao
         System.out.println("Conectado e preparado os elementos de classificação");
         Statement stmt = null; 
         String sql;
         try 
         {
             stmt = base.getConnection().createStatement();
             sql = "SELECT area.name AS name, COUNT(ST_Intersects(animal_position.coordinate, area.area_polygon)) AS points "
                     + "FROM animal_position, area WHERE animal_position.collar = '" + collar + "' AND area.pasture_id = '" + pastureid + "' AND "
                     + "ST_Intersects(animal_position.coordinate, area.area_coordinates) = '" + true + "'  " + this.getDateSQL() + " " + this.getHourSQL() + " GROUP BY area.name";

             System.out.println(sql);
             ResultSet rs = stmt.executeQuery(sql);
             while (rs.next()) 
             { 
               Classifier c = new Classifier(rs.getString("name"), rs.getInt("points"));
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
    
    
    /**Mostra a data e o horário de um ponto quando estava na área em questão.*/
    public ObservableList<Coordinate> showTime (int pastureid, String area, String collar) throws Exception 
    {
         ObservableList<Coordinate> list = FXCollections.observableArrayList();
         base = new Database();
         // iniciando a conexao
         Statement stmt = null; 
         String sql;
         try 
         {
             stmt = base.getConnection().createStatement();
             sql = "SELECT animal_position.hour AS hour, animal_position.date AS date FROM animal_position, area "
                     + "WHERE animal_position.collar = '" + collar + "' AND area.pasture_id = '" + pastureid + "' AND "
                     + "area.name = '" + area + "' AND ST_Intersects(animal_position.coordinate, area.area_coordinates) = true  "
                     + "" + this.getDateSQL() + " " + this.getHourSQL() + "";
             
             System.out.println(sql);
             ResultSet rs = stmt.executeQuery(sql);
             while (rs.next()) 
             { 
               Coordinate c = new Coordinate(rs.getString("date"), rs.getString("hour"));
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
    
    
    /**Retorna o número de pontos do colar que estão na área sombreada.*/
    public int selectInAreasShadow(int pastureid, String collar) throws Exception 
    {
         int points = 0;
         base = new Database();
         // iniciando a conexao
         Statement stmt = null; 
         String sql;
         try 
         {
             stmt = base.getConnection().createStatement();
             sql = "SELECT COUNT(ST_Intersects(animal_position.coordinate, area_shadow.area_polygon)) AS points FROM animal_position, area_shadow "
                         + "WHERE animal_position.collar = '" + collar + "' AND ST_Intersects(animal_position.coordinate, area_shadow.area_coordinates) = true "
                         + "AND animal_position.hour between area_shadow.hour_begin and area_shadow.hour_end  " + this.getDateSQL() + " " + this.getHourSQL() + "";

             System.out.println(sql);
             ResultSet rs = stmt.executeQuery(sql);
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
    
    /**Retorna a data e o horário em que o animal esteve na sombra.*/
    public ObservableList<Coordinate> selectAreaInShadow(String collar) throws Exception 
    {
         ObservableList<Coordinate> list = FXCollections.observableArrayList();
         base = new Database();
         // iniciando a conexao
         System.out.println("Conectado e preparado");
         Statement stmt = null; 
         String sql;
         try 
         {
             stmt = base.getConnection().createStatement();
             sql = "SELECT animal_position.date AS date, animal_position.hour AS hour FROM animal_position, area_shadow WHERE animal_position.collar = '" + collar + "' " + this.getDateSQL() + " "
                 + "AND ST_Intersects(animal_position.coordinate, area_shadow.area_coordinates) = true AND animal_position.hour between area_shadow.hour_begin and area_shadow.hour_end";

             System.out.println(sql);
             ResultSet rs = stmt.executeQuery(sql);
             while (rs.next()) 
             { 
               Coordinate c = new Coordinate(rs.getString("date"), rs.getString("hour"));
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