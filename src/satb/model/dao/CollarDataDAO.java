package satb.model.dao;

import java.sql.*;
import satb.model.Coordinate;
import satb.model.Database;
import org.postgis.Point;

/**Classe com manipulação de métodos de inserção de dados capturados pelos colares.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class CollarDataDAO 
{
    //Atributos
    private Database base;
       
    /**Construtor default. */
    public CollarDataDAO(){}
    
    
    /**Recebe latitude e longitude e retorna uma coordenada do tipo Geometry. */     
    private static String getPoint(String x, String y)
    {
        String point = "POINT(";
        point = point.concat(x);
        point = point.concat(" ");
        point = point.concat(y);
        point = point.concat(")");
        
        return point;
    }
       
    /**Insere no banco os dados obtidos pelos colares. */
    public void insertData(String collar, String longitude, String latitude, String date, String hour) throws Exception 
    {
           base = new Database();
	// iniciando a conexao
	System.out.println("Conectado e preparando a inserção dos dados capturados pelos colares");
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            stmt.execute("INSERT INTO animal_position (collar, longitude, latitude, coordinate, date, hour) "
            + "VALUES ('" + collar + "', '" + longitude + "', '" + latitude + "','" + getPoint(longitude, latitude) +  "', '" + date + "', '" + hour + "')");                
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
   

    
   /**Insere no banco os dados obtidos pelos colares e armazenados em ARFFs. */
   public void insertDataARFF(String collar, Coordinate c) throws Exception 
   {
        base = new Database();
	System.out.println("Conectado e preparando a inserção dos dados do ARFF");
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            stmt.execute("INSERT INTO animal_position (collar, longitude, latitude, coordinate, date, hour) "
            + "VALUES ('" + collar + "', '" + c.getLongitudeX() + "', '" + c.getLatitudeY() + "','" + getPoint(String.valueOf(c.getLongitudeX()), String.valueOf(c.getLatitudeY())) +  "', '" + c.getDate() + "', '" + c.getHour() + "')");                
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
  
   
   /**Insere no banco os dados as trajetórias contidas no ARFFs. */
   public void insertTrajectoryARFF(String collar, String date, String gap, String hourBegin, String hourEnd) throws Exception 
   {
        base = new Database();
	System.out.println("Conectado e preparando a inserção das trajetórias");
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            stmt.execute("INSERT INTO trajectory (collar, date, gap_time, begin_hour, end_hour) "
            + "VALUES ('" + collar + "', '" + date + "', '" + gap + "','" + hourBegin +  "', '" + hourEnd + "')");                
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
          
}