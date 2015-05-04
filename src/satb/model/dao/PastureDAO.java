package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.Database;
import satb.model.Pasture;

/**Classe com manipulação de métodos de inserção e consulta de dados da classe Pasture.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class PastureDAO 
{
    //Atributos
    private Database base;  

    /**Método que seleciona o nome de todos os pastos cadastrados. */
    public ObservableList<String> selectItens() throws Exception 
    {
        ObservableList<String> list = FXCollections.observableArrayList();
        base = new Database();
        // iniciando a conexao
        System.out.println("conectado e preparando listagem de Pastos");
        Statement stmt = null;
        try 
        {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM pasture");
            while (rs.next()) 
            {
                    String word = rs.getString("name");
                    list.add(word);
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

    /**Seleciona o pasto desejado através do nome do mesmo. */
    public Pasture selectPasture(String name) throws Exception 
    {
        Pasture p = null;
        base = new Database();
        // iniciando a conexao
        System.out.println("Conectado e preparando a listagem dos dados de um pasto");
        Statement stmt = null;
        try 
        {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, hectars, ST_AsText(area_coordinates) AS area FROM pasture WHERE name = '" + name + "'");
            while(rs.next())
            {
               p = new Pasture(rs.getInt("id"), rs.getString("name"), rs.getFloat("hectars"), rs.getString("area"));

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

    /**Retorna o id do pasto. */
    public int selectId(String name) throws Exception 
    {
        int pastureId = 0;
        base = new Database();
        // iniciando a conexao
        System.out.println("Conectado e preparado para retornar o id do pasto");
        Statement stmt = null;
        try 
        {
          stmt = base.getConnection().createStatement();
          ResultSet rs = stmt.executeQuery("SELECT id FROM pasture WHERE name = '" + name + "'");
          while (rs.next()) 
          {
               pastureId = rs.getInt("id");
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
        return pastureId;			
    }


    /**Insere os dados de um novo pasto no banco de dados.*/
    public void insertPasture(Pasture p) throws Exception
    {      
        base = new Database();
        // iniciando a conexao
        System.out.println("conectado e preparando a inserção do Pasto");
        Statement stmt = null;
        try 
        {
          stmt = base.getConnection().createStatement();
          stmt.execute("INSERT INTO pasture (name, hectars, area_coordinates)"
                 +" VALUES ('"+ p.getName() + "', '" + p.getHectars() + "', '" + p.getAreaCoordinates() + "')");                
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


    /**Remove os dados de um pasto no banco de dados.*/
    public void removePasture(String n) throws Exception
    {      
        base = new Database();
        // iniciando a conexao
        System.out.println("conectado e preparando a remoção do pasto");
        Statement stmt = null;
        try 
        {
            stmt = base.getConnection().createStatement();
            stmt.execute("DELETE FROM pasture WHERE name = '"+ n + "'");               
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


    /**Edita os dados de um pasto no banco de dados.*/
    public void updatePasture (Pasture p) throws Exception
    {      
        base = new Database();
        // iniciando a conexao
        System.out.println("conectado e preparando a edição dos dados de um Pasto");
        Statement stmt = null;
        try 
        {
          stmt = base.getConnection().createStatement();
          stmt.execute("UPDATE pasture SET name = '" + p.getName() + "',  hectars = '" + p.getHectars() + "', area_coordinates = '" +  p.getAreaCoordinates() + "'  WHERE id = '" + p.getId() + "'");
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
