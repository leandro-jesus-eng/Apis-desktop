package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.Database;
import satb.model.Tree;

/**Classe para a manipulação de dados do BD da classe Tree.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class TreeDAO 
{
    //Atributos
    private Database base;  
    
    /**Retorna o Id do de uma árvore em um pasto recebendo como parâmetro pasto. */
    public int selectId(String name) throws Exception 
    {
       int pastureId = 0;
       base = new Database();
       // iniciando a conexao
       System.out.println("Conectado e preparado para retornar o id de uma árvore");
       Statement stmt = null;
       try 
       {
           stmt = base.getConnection().createStatement();
           ResultSet rs = stmt.executeQuery("SELECT id FROM tree_position WHERE tree_number = '" + name + "'");
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
    
   
    /**Método que seleciona o nome de todos as Árvores. */
    public ObservableList<String> selectItem(int id) throws Exception 
    {
        ObservableList<String> treeList = FXCollections.observableArrayList();
	base = new Database();
	// iniciando a conexao
	System.out.println("conectado e preparando listagem das Árvores do Pasto");
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT tree_number FROM tree_position WHERE pasture_id = '" + id + "'");
            while (rs.next()) 
            {
                    String word = rs.getString("tree_number");
                    treeList.add(word);
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
	return treeList;		
   }
   

   /**Remove os dados de uma árvore no BD. */
   public void removeTree(String n) throws Exception
   {      
	base = new Database();
	// iniciando a conexao
	System.out.println("conectado e preparando a remoção de uma árvore");
	Statement stmt = null;
	try 
	{
            stmt = base.getConnection().createStatement();
            stmt.execute("DELETE FROM tree_position WHERE tree_number = '"+ n + "'");               
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
    
   
   /**Insere os dados de um nova árvore no BD. */
   public void insertTree(Tree t) throws Exception
   {      
      base = new Database();
      // iniciando a conexao
      System.out.println("conectado e preparando a inserção de uma Árvore");
      Statement stmt = null;
      try 
      {
          stmt = base.getConnection().createStatement();
          stmt.execute("INSERT INTO tree_position (pasture_id, tree_number, latitude, longitude, coordinate)"
          +" VALUES ('"+ t.getPasture() + "', '" + t.getNumberTree() + "', '" + t.getLatitude() + "', '" + t.getLongitude() + "', '" + t.getCoordinate() +"')");  

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
    
    
   /**Retorna todas as árvores coordenadas do pasto. */  
   public ObservableList<Tree> selectAll(int id) throws Exception 
   {
       ObservableList<Tree> list = FXCollections.observableArrayList();
       base = new Database();
       // iniciando a conexao
       System.out.println("Conectado e preparando a listagem das árvores do pasto");
       Statement stmt = null;
       try 
       {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT tree_number, latitude, longitude FROM tree_position WHERE pasture_id = '" + id + "'");
            while (rs.next()) 
            {
                    Tree t = new Tree(rs.getString("tree_number"), rs.getDouble("longitude"), rs.getDouble("latitude"));
                    list.add(t);
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
  
  
  /**Retorna todas as árvores coordenadas do pasto. */  
  public Tree selectTree(int id, String tr) throws Exception 
  {
       Tree tree = null;
       base = new Database();
       // iniciando a conexao
       System.out.println("Conectado e preparando a listagem de uma árvore do pasto");
       Statement stmt = null;
       try 
       {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, latitude, longitude FROM tree_position "
                    + "WHERE  pasture_id = '" + id + "' AND tree_number = '" + tr + "'");
            while (rs.next()) 
            {
                tree = new Tree(tr, rs.getDouble("longitude"), rs.getDouble("latitude"));
                tree.setId(rs.getInt("id"));
                tree.setPasture(id);
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
        return tree;			
   }
    
  
    /**Edita os dados de uma árvore no BD. */
    public void updateTree(Tree t) throws Exception
    {      
        base = new Database();
        System.out.println("Preparando a edição dos dados de uma árvore.");
        Statement stmt = null;
        try 
        {
            stmt = base.getConnection().createStatement();
            stmt.execute("UPDATE tree_position SET pasture_id = '" + t.getPasture() + "',  tree_number = '" + t.getNumberTree() + "', "
            + "latitude = '" + t.getLatitude() + "', longitude = '" + t.getLongitude() + "', coordinate = '" + t.getCoordinate() + "' WHERE id = '" + t.getId() + "'");

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