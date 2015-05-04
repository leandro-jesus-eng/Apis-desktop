package satb.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import satb.model.Database;
import satb.model.DensityPoints;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**Classe com manipulação de métodos de inserção e consulta de dados da classe Question.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class QuestionsDAO 
{
    //Atributos
    private Database base;
    private String dateSQL;
    
    /**Construtor Default. */
    public QuestionsDAO(){} 
    
    /**Retorna o atributo dateSQL. */
    public String getDateSQL()
    {
        return this.dateSQL;
    }
    
    /**Seta o atributo dateSQL. */
    public void setDateSQL(String d)
    {
        this.dateSQL = d;
    }
    
    /**Adicionando a clausula whereDate na SQL. */
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
           this.setDateSQL("");
        }
    }
   
    
    private boolean compareIntervals(String intervalBegin, String intervalEnd, String hourBegin, String hourEnd)
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime shadowBegin = formatter.parseDateTime(intervalBegin);
        DateTime shadowEnd = formatter.parseDateTime(intervalEnd);
        DateTime begin = formatter.parseDateTime(hourBegin);
        DateTime end = formatter.parseDateTime(hourEnd);
        
        
        Interval shadowInterval = new Interval(shadowBegin, shadowEnd); 
        Interval densityInterval = new Interval(begin, end);
        
        if(shadowInterval.contains(begin) || shadowInterval.contains(end) || densityInterval.contains(shadowBegin) || densityInterval.contains(shadowEnd))
        {
            return true;
        }
                
        return false;
    }
    
    
    
    /**Descobre onde há intersecção entre as áreas de sombra e os pontos de baixa velocidade. */
    public ObservableList<DensityPoints> densityPointsInShadow (String collar, int trajectoryId) throws Exception 
    {
        ObservableList<DensityPoints> points = FXCollections.observableArrayList();
        base = new Database();
        // iniciando a conexao
        Statement stmt = null; 
        try 
        {
            stmt = base.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT area_shadow.name AS name, area_shadow.hour_begin AS intervalBegin, "
            + "area_shadow.hour_end AS intervalEnd, trajectory_hot_spots.begin_hour AS hourBegin, trajectory_hot_spots.end_hour AS hourEnd "
            + "FROM trajectory_hot_spots, area_shadow WHERE ST_Intersects(trajectory_hot_spots.trajectory_line, "
            + "area_shadow.area_coordinates) = true AND trajectory_hot_spots.trajectory_id = '" + trajectoryId + "'");
            
            while (rs.next()) 
            { 
               if(compareIntervals(rs.getString("intervalBegin"), rs.getString("intervalEnd"), rs.getString("hourBegin"), rs.getString("hourEnd")))
               {
                   DensityPoints density = new DensityPoints(rs.getString("name"), rs.getString("hourBegin"), rs.getString("hourEnd"));
                   points.add(density);                   
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
        return points;			
    } 
}
