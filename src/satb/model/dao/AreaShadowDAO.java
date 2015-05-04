package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.AreaShadow;
import satb.model.Database;

/**Classe com manipulação de métodos de inserção e consulta de dados da classe Area Shadow.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class AreaShadowDAO 
{
    //Atributos
    private Database base;  

    /**Retorna as informações de uma área sombreada. */    
    public AreaShadow selectAreaShadow(String name) throws Exception 
    {
         AreaShadow area = null;
         base = new Database();
         // iniciando a conexao
         System.out.println("Preparando a listagem dos dados de uma area sombreada");
         Statement stmt = null;
         try 
         {
              stmt = base.getConnection().createStatement();
              ResultSet rs = stmt.executeQuery("SELECT id, pasture_id, tree_id, minimum_time, hour_begin, hour_end, ST_AsText(area_coordinates) AS area FROM area_shadow WHERE name = '" + name + "'"); 
              while (rs.next()) 
              {
                  area = new AreaShadow (rs.getInt("pasture_id"), rs.getInt("tree_id"), name, rs.getString("hour_begin"), rs.getString("hour_end"), rs.getString("area"));
                  area.setId(rs.getInt("id"));
                  area.setMinTime(rs.getString("minimum_time"));
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
    
 
    /**Retorna todas as áreas sombreadas de um pasto. */    
    public ObservableList<AreaShadow> selectAll (int id) throws Exception 
    {
         ObservableList<AreaShadow> areaList = FXCollections.observableArrayList();
         base = new Database();
         // iniciando a conexao
         System.out.println("Preparando a listagem das áreas sombreadas");
         Statement stmt = null;
         try 
         {
              stmt = base.getConnection().createStatement();
              ResultSet rs = stmt.executeQuery("SELECT id, name, minimum_time, hour_begin, hour_end FROM area_shadow WHERE tree_id = '" + id + "'");
              while (rs.next()) 
              {
                  AreaShadow area = new AreaShadow (rs.getString("hour_begin"), rs.getString("hour_end"), rs.getString("minimum_time"));
                  area.setName(rs.getString("name"));
                  area.setId(rs.getInt("id"));
                  areaList.add(area);
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
    
    
    /**Retorna o nome das áreas pertecentes a árvore. */
    public ObservableList<String> selectItens(int id) throws Exception 
    {
        ObservableList<String> nameList = FXCollections.observableArrayList();
        base = new Database();
        // iniciando a conexao
        System.out.println("Preparando listagem dos nomes das areas sombreadas");
        Statement stmt = null;
        try 
        {
           stmt = base.getConnection().createStatement();
           ResultSet rs = stmt.executeQuery("SELECT name FROM area_shadow WHERE tree_id = '" + id + "'");
           while (rs.next()) 
           {
               String word = rs.getString("name");
               if(!nameList.contains(word))
               {
                   nameList.add(word);
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
        return nameList;		
    }     
    
    
    /**Retorna o id da area shadow. */ 
    public int selectId(String hour) throws Exception 
    {
         int areaId = 0;
         base = new Database();
         // iniciando a conexao
         System.out.println("Conectado e preparado para retornar o id da área sombreada");
         Statement stmt = null;
         try 
         {
             stmt = base.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM area_shadow WHERE hour_begin = '" + hour + "'");
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
    
  
    /**Insere os dados de uma área sombreada em um pasto no BD. */
    public void insertAreaShadow(AreaShadow a) throws Exception
    {      
        base = new Database();
        // iniciando a conexao
        System.out.println("Preparando a inserção de uma Área Sombreada");
        Statement stmt = null;
        try 
        {
            stmt = base.getConnection().createStatement();
            stmt.execute("INSERT INTO area_shadow (pasture_id, tree_id, name, minimum_time, hour_begin, hour_end, area_coordinates)"
            +" VALUES ('"+ a.getPasture() + "', '" + a.getTree() + "', '" + a.getName() + "', '" + a.getMinTime()  + "', '" + a.getHourBegin() + "', '" + a.getHourEnd() + "', '" + a.getAreaCoordinates() + "')");                
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
  
  
    /**Remove os dados de uma área no Banco de Dados. */
    public void removeAreaShadow(String areaShadow) throws Exception
    {      
        base = new Database();
        // iniciando a conexao
        System.out.println("Preparando a remoção de uma área sombreada");
        Statement stmt = null;
        try 
        {
            stmt = base.getConnection().createStatement();
            stmt.execute("DELETE FROM area_shadow WHERE name = '" + areaShadow + "'");
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
  
    
    /**Edita os dados de uma area de sombra no banco de dados. */
    public void updateAreaShadow (AreaShadow area) throws Exception
    {      
        base = new Database();
        // iniciando a conexao
        System.out.println("Preparando a edição dos dados de uma Área");
        Statement stmt = null;
        try 
        { 
            stmt = base.getConnection().createStatement();
            stmt.execute("UPDATE area_shadow SET pasture_id = '" + area.getPasture() + "', tree_id = '" + area.getTree() + "',  name = '" + area.getName() + "', "
            + "hour_begin = '" + area.getHourBegin() +  "', hour_end = '" + area.getHourEnd() +  "', area_coordinates = '" + area.getAreaCoordinates() + "', minimum_time = '" + area.getMinTime() + "' WHERE id = '" + area.getId() + "'");
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
