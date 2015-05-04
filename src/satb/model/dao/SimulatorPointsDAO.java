package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import satb.model.Coordinate;
import satb.model.Database;
import org.postgis.Point;


public class SimulatorPointsDAO 
{
    //Atributos
    private Database base;
    
    /**Verifica se a trajetória passa pela área passada pelo parâmetro. */
    public boolean intersectPointInPasture (Coordinate point, int pastureId) throws Exception 
    {         
         boolean intersect = false;
         base = new Database();
         // iniciando a conexao
         Statement stmt = null;

         try 
         {
             stmt = base.getConnection().createStatement();
             String sql = "SELECT ST_Intersects('" + point.getCoordinate() + "', area_coordinates) "
                     + "AS intersection FROM pasture WHERE id = '" + pastureId + "'";

             System.out.println(sql);
             ResultSet rs = stmt.executeQuery(sql);
             while (rs.next()) 
             { 
                 intersect = rs.getBoolean("intersection");
             }
             System.out.println(intersect);
         }
         catch (SQLException e) 
         {
              System.out.println(e.getMessage());
              return false;
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
          return intersect;			
    }
    
    
    /**Pega um ponto qualquer dentro da área de um pasto e retorna o ponto. */
    public Point generatePointInPasture(int pastureId) throws Exception 
    {
        Point point = null;
        base = new Database();
        System.out.println("Gerando um ponto pertencente ao pasto.");
        Statement stmt = null;
        try 
        {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ST_AsText(RandomPoint(pasture.area_coordinates)) AS point "
                    + "FROM pasture WHERE pasture.id = '" + pastureId + "'");
            while (rs.next()) 
            {
                point = new Point(rs.getString("point"));
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
        return point;
    } 
   
    
    
}
