package satb.controller;

import javafx.collections.ObservableList;
import satb.model.Trajectory;
import satb.model.dao.QuestionsDAO;
import satb.model.dao.TrajectoryDAO;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**Classe que implementa algoritmos de Questions.
* @author Marcel Tolentino Pinheiro de Oliveira
*/
public class QuestionsController 
{
    //Atributo
    private ObservableList<Trajectory> trajectories;
    
    /**Construtor Default. */
    public QuestionsController(){}
    
 
    /**Método que retorna o atributo trajectories. */
    public ObservableList<Trajectory> getTrajectories()
    {
        return this.trajectories;
    }
    
    
    /**Verifica se há stops presentes na lista de trajetórias. */
    public boolean hasStops()
    {     
        for(Trajectory t : trajectories)
        {
           if(!t.getStops().isEmpty())
           {
               return true;
           }
        }               
        return false;
    }
    
    
    /**Método principal da classe Question. */
    public void answerTheQuestions(String collar, String questions, String date, String dateBegin, String dateEnd, String pasture, String place) throws Exception
    {
        TrajectoryDAO td = new TrajectoryDAO();
        QuestionsDAO qd = new QuestionsDAO();
        qd.whereDate(date, dateBegin, dateEnd);
        
        switch(questions)
        {       
            case "O animal foi para...":
                trajectories = td.selectAll(collar);
                
                for(Trajectory t : trajectories)
                {
                    t.setStops(td.selectStops(t.getId(), place));
                }
                break;
                
                
            case "O animal esteve na Sombra?":
                trajectories = td.selectAll(collar);
                
                for(Trajectory t : trajectories)
                {
                    t.setStops(td.selectShadowStops(t.getId()));
                }
                break;
                
                
            case "Qual a distância percorrida, a duração e a velocidade média do animal na trajetória?":  
                ObservableList<String> dates = td.selectDate(collar);
                trajectories = td.selectTrajectories(collar, dates);
                
                for(Trajectory t : trajectories)
                {
                   t.setDistance(t.distanceTotal());
                }
                break;
                
                
            case "Existe pontos se concentrando na Sombra?":
                trajectories = td.selectAll(collar);
                
                for(Trajectory t : trajectories)
                {
                   t.setDensityPoints(qd.densityPointsInShadow(collar, t.getId()));
                }                
                break;
            
        }
        
    }
    
    
    /**Método que executa a operação hourA + hourB. */
    public String plusHours(String hourA, String hourB)
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(hourA);
        
        String[] parts = hourB.split(":");
        dateTime = dateTime.plusSeconds(Integer.parseInt(parts[2]));
        dateTime = dateTime.plusMinutes(Integer.parseInt(parts[1]));
        dateTime = dateTime.plusHours(Integer.parseInt(parts[0]));
        
        return dateTime.toString(formatter);        
    } 
    
    
 }