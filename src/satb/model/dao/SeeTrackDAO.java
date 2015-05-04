package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.Coordinate;
import satb.model.Database;

/**Classe para a manipulação de dados do banco de dados da classes colares e coordenadas.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class SeeTrackDAO 
{
    //Atributos
    private Database base;  
    private String hourSQL;
    
    /**Construtor Default. */
    public SeeTrackDAO(){} 
    
    /**Retorna o atributo hourSQL. */
    public String getHourSQL()
    {
        return this.hourSQL;
    }
    
    /**Seta o atributo hourSQL. */
    public void setHourSQL(String s)
    {
        this.hourSQL = s;
    }
    
    /**Adicionando a clausula whereHour na SQL. */
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
    
    
    /**Método busca todos os colares cadastrados. */
    public ObservableList<String> selectCollar() throws Exception 
    {
        ObservableList<String> collarList = FXCollections.observableArrayList();
	base = new Database();
	// iniciando a conexao

	System.out.println("Conectado e preparando listagem dos colares");
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT collar FROM Collar");
            while (rs.next()) 
            {
                String word = rs.getString("collar");
                collarList.add(word);
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
	return collarList;		
    }
    
    
    /**Método busca todas as datas relacionadas ao colar passado por parâmetro. */
    public ObservableList<String> selectDate(String collar) throws Exception 
    {
        ObservableList<String> dateList = FXCollections.observableArrayList();
        base = new Database();
        // iniciando a conexao
        System.out.println("Conectado e preparando listagem das datas.");
        Statement stmt = null;
        try 
        {
           stmt = base.getConnection().createStatement();
           ResultSet rs = stmt.executeQuery("SELECT date FROM animal_position WHERE collar = '" + collar + "'");  
           while (rs.next()) 
           {
               String word = rs.getString("date");
               if(!dateList.contains(word))
               {
                   dateList.add(word);
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
         return dateList;		
    }   
    
    
    /**Método seleciona as coordenadas de uma trajetória. */
    public ObservableList<Coordinate> selectCoordinates(String collar, String date) throws Exception 
    {
          ObservableList<Coordinate> trajectory = FXCollections.observableArrayList();
          base = new Database();
          // Iniciando a conexao
          System.out.println("Conectado e preparando listagem das coordenadas");
          Statement stmt = null;
          ResultSet rs;
          try 
          {
              stmt = base.getConnection().createStatement();
              rs = stmt.executeQuery("SELECT id, longitude, latitude, hour FROM animal_position WHERE collar = '" + collar + "' "
              + "AND date = '" + date + "' " + this.getHourSQL() + "ORDER by id");            

              while (rs.next()) 
              {
                  Coordinate c = new Coordinate(rs.getDouble("longitude"), rs.getDouble("latitude"), rs.getString("hour"));
                  c.setId(rs.getInt("id"));
                  trajectory.add(c);            
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
