package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.Area;
import satb.model.Database;

/**Classe com manipulação de métodos de inserção e consulta de dados da classe Area.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class AreaDAO 
{
    //Atributos
    private Database base;  
 
    /**Retorna todas as áreas de um pasto. */    
    public ObservableList<Area> selectAreasFromPasture(int id) throws Exception 
    {
         ObservableList<Area> list = FXCollections.observableArrayList();
         base = new Database();
         // iniciando a conexao
         System.out.println("Conectado e preparando a listagem das areas do pastos");
         Statement stmt = null;
         try 
         {
              stmt = base.getConnection().createStatement();
              ResultSet rs = stmt.executeQuery("SELECT id, name, minimum_time, ST_AsText(area_coordinates) AS coords FROM area WHERE pasture_id = '" + id + "'");
              while (rs.next()) 
              {
                    Area a = new Area(rs.getString("name"), rs.getString("minimum_time"), rs.getString("coords"));
                    a.setId(rs.getInt("id"));
                    list.add(a);
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
   
    
    /**Seleciona a area desejado através do nome do mesmo. */
    public Area selectArea (String name) throws Exception 
    {
        Area a = null;
        base = new Database();
        // iniciando a conexao
        System.out.println("Conectado e preparando a listagem dos dados de uma área");
        Statement stmt = null;
        try 
        {
           stmt = base.getConnection().createStatement();
           ResultSet rs = stmt.executeQuery("SELECT id, pasture_id, minimum_time, ST_AsText(area_coordinates) AS area FROM area WHERE name = '" + name + "'");
           while(rs.next())
           {
               a = new Area(rs.getInt("pasture_id"), name, rs.getString("minimum_time"), rs.getString("area"));
               a.setId(rs.getInt("id"));
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
        return a;			
    }
    
    
    /**Metodo que retorna o nome das áreas de um pasto, através do id do pasto. */
    public ObservableList<String> selectItens(int id) throws Exception 
    {
       ObservableList<String> areaList = FXCollections.observableArrayList();
       base = new Database();
       // iniciando a conexao
       System.out.println("conectado e preparando listagem do nome das areas");
       Statement stmt = null;
       try 
       {
           stmt = base.getConnection().createStatement();
           ResultSet rs = stmt.executeQuery("SELECT name FROM area WHERE pasture_id = '" + id + "'");

           while (rs.next()) 
           {
               String word = rs.getString("name");
               if(!areaList.contains(word))
               {
                   areaList.add(word);
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
       return areaList;		
    }     
    
    
    /**Método que retorna o id da área. */ 
    public int selectId(String name) throws Exception 
    {
       int areaId = 0;
       base = new Database();
       // iniciando a conexao
       System.out.println("Conectado e preparado para retornar o id do pasto");
       Statement stmt = null;
       try 
       {
           stmt = base.getConnection().createStatement();
           ResultSet rs = stmt.executeQuery("SELECT id FROM area WHERE name = '" + name + "'");
           while (rs.next()) 
           {          
                areaId = rs.getInt("id");
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
        return areaId;			
    }
    
  
    /**Método que insere os dados de uma área em um pasto no BD. */
    public void insertArea(Area a) throws Exception
    {      
        base = new Database();
        // iniciando a conexao
        System.out.println("conectado e preparando a inserção de uma Área");
        Statement stmt = null;
        try 
        {
            stmt = base.getConnection().createStatement();
            stmt.execute("INSERT INTO area (pasture_id, name, minimum_time, area_coordinates)"
            +" VALUES ('"+ a.getPasture() + "', '" + a.getName() + "', '" + a.getMinimumTime() + "', '" + a.getAreaCoordinates() + "')");                
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
  
   
    /**Remove os dados de uma área no Banco de dados. */
    public void removeArea(String areaName) throws Exception
    {      
        base = new Database();
        // iniciando a conexao
        System.out.println("Preparando a remoção de uma área.");
        Statement stmt = null;
        try 
        {
            stmt = base.getConnection().createStatement();
            stmt.execute("DELETE FROM area WHERE name = '" + areaName + "'");               
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


    /**Edita os dados de uma área no Bando de Dados. */
    public void updateArea (Area a) throws Exception
    {      
        base = new Database();
        // iniciando a conexao
        System.out.println("conectado e preparando a edição dos dados de uma Área");
        Statement stmt = null;
        try 
        { 
            stmt = base.getConnection().createStatement();
            stmt.execute("UPDATE area SET pasture_id = '" + a.getPasture() + "',  name = '" + a.getName() + "', "
              + "minimum_time = '" + a.getMinimumTime() +  "', area_coordinates = '" + a.getAreaCoordinates() + "'  WHERE id = '" + a.getId() + "'");
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