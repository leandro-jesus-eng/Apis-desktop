package satb.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**Classe que representa o objeto SetOfPoints.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class SetOfPoints 
{
    //Atributos
    private static final int FIRST_INDEX = 0;   
    private ObservableList<Coordinate> points;

    /**Construtor Default. */
    public SetOfPoints() 
    {
        points = FXCollections.observableArrayList();
    }
    
    /**Método que retorna o atributo points. */
    public ObservableList<Coordinate> getPoints()
    {
        return this.points;
    }
    
    /**Método que seta o valor do atributo points. */
    public void setPoints(ObservableList<Coordinate> points)
    {
        this.points = points;
    }
    
    public int getFirstPointTimeIndex() 
    {
        return points.get(FIRST_INDEX).getTimeIndex();
    }

    public int getLastPointTimeIndex() 
    {
        return points.get(points.size()-1).getTimeIndex();
    }

    /**Retorna o tamanho de SetOfPoints. */
    public int size()
    {
        return this.points.size();
    }

    /**Verifica se o elemento foi adicionado a SetOfPoints. */
    public boolean contain(Coordinate point)
    {
        if(this.points.contains(point))
        {
            return true;
        }
        
        return false;
    }
    
    /**Adiciona o elemento no inicio da trajetótia. */
    public void addToBegin(Coordinate point) 
    {
        points.add(FIRST_INDEX, point);
    }

    /**Adiciona o elemento no final da trajetótia. */
    public void addToEnd(Coordinate point) 
    {
        points.add(point);
    }
    
    /**Método que seta os clusterId das coordinates. */
    public void setClusterId(int clusterId) 
    {
        for (Coordinate p: points) 
        {
            p.setClusterId(clusterId);
        }
    }
    

    /**Método que retorna a duração da trajetória em segundos. */
    public int durationInSeconds() 
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(points.get(points.size()-1).getHour());
        
        String[] parts = points.get(0).getHour().split(":");
        dateTime = dateTime.minusSeconds(Integer.parseInt(parts[2]));
        dateTime = dateTime.minusMinutes(Integer.parseInt(parts[1]));
        dateTime = dateTime.minusHours(Integer.parseInt(parts[0]));
        
       return dateTime.getSecondOfDay();
    }
    

    /**Retorna a velocidade média da Trajetória. */
    public double meanSpeed() 
    {
        if(points.size() == 1)
        {
            return 0;
        }
       
        int i = 1;
        double totalDist = 0.0;
    	while (i < points.size()) 
        {
            Coordinate lastPoint = points.get(i-1);
            Coordinate currentPoint = points.get(i);
            totalDist = totalDist + lastPoint.distance(currentPoint);
            i++;
    	}

        double meanSpeed = totalDist/durationInSeconds();
  
        return meanSpeed;
    }
     
}
