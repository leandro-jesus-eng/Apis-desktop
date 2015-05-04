package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.Collar;
import satb.model.Database;
      
/**Classe com manipulação de métodos de inserção e consulta de dados da classe Collar.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class CollarDAO 
{  
    //Atributos
    Database base;  
    ObservableList<Collar> data = FXCollections.observableArrayList();

    public void insertData(Collar c) throws Exception
    {      
	base = new Database();
	// iniciando a conexao
	//conn = base.getConnection();
	System.out.println("conectado e preparando a inserção");
	Statement stmt = null;
	try 
	{
		stmt = base.getConnection().createStatement();
		stmt.execute("INSERT INTO collar (collar, company)"
                   +" VALUES ('"+ c.getCollar() + "', '" + c.getCompany()+"')");
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
        
    public ObservableList<Collar> selectAll() throws Exception 
    {		
       ObservableList<Collar> list = FXCollections.observableArrayList();
       base = new Database();
       System.out.println("Conectado e preparando listagem");
       Statement stmt = null;
       try 
       {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Collar");
            while (rs.next()) 
            {
                Collar c = new Collar(rs.getString("collar"), rs.getString("company"));
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
    
    public ObservableList<String> selectItens() throws Exception 
    {
       ObservableList<String> list = FXCollections.observableArrayList();
       base = new Database();
       // iniciando a conexao
       System.out.println("conectado e preparando listagem do nome dos colares");
       Statement stmt = null;
       try 
       {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT collar FROM Collar");
            // Incluindo Collars na lista que vai ser retornada
            while (rs.next()) 
            {	
                    list.add(rs.getString("collar"));
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
    
    public ObservableList<String> selectCollarDates (String collar) throws Exception 
    {
       ObservableList<String> list = FXCollections.observableArrayList();
       base = new Database();
       // iniciando a conexao
       System.out.println("conectado e preparando listagem das datas de coleta de presente no colar");
       Statement stmt = null;
       try 
       {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT date FROM animal_position WHERE collar = '" + collar + "'");
            while (rs.next()) 
            {	
                if(!list.contains(rs.getString("date")))
                {
                    list.add(rs.getString("date")); 
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
        return list;			
    }
    
    
    /**Seleciona todas as informações de um colar.*/
    public Collar selectCollar(String collar) throws Exception 
    {
       Collar c = null;
       base = new Database();
       // iniciando a conexao
       System.out.println("conectado e preparando listagem do colar");
       Statement stmt = null;
       try 
       {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, company FROM collar WHERE collar = '" + collar + "'");
            // Incluindo Collars na lista que vai ser retornada
            while (rs.next()) 
            {	
                   c = new Collar(collar, rs.getString("company"));
                   c.setId(rs.getInt("id"));
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
        return c;			
    }
    
    /**Remove os dados de um colar no banco de dados.*/
    public void removeCollar(String c) throws Exception
    {      
          base = new Database();
          // iniciando a conexao
          System.out.println("conectado e preparando a remoção de um collar");
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              stmt.execute("DELETE FROM collar WHERE collar = '"+ c + "'");               
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
  
    /**Edita os dados de um collar no banco de dados.*/
    public void updateCollar(Collar c) throws Exception
    {      
          base = new Database();
          // iniciando a conexao
          System.out.println("conectado e preparando a edição dos dados de uma Árvore");
          Statement stmt = null;
          try 
          {
              stmt = base.getConnection().createStatement();
              stmt.execute("UPDATE collar SET collar = '" + c.getCollar() + "',  company = '" + c.getCompany() + "' WHERE id = '" + c.getId() + "'");

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