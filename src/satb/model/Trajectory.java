package satb.model;

import java.util.Collections;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**Classe que representa o objeto Trajectory.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class Trajectory 
{
    //Atributo
    public int id;
    private String collar;
    private String date;
    private double distance;
    private double avoidance;
    private String beginHour;
    private String endHour;
    private String gap;
    private ObservableList<Coordinate> gpsPoints;
    private ObservableList<Stop> stopList;
    private ObservableList<Move> moveList;
    private ObservableList<DensityPoints> densityPoints;
    
    /**Cria uma nova instância da Trajetória. */
    public Trajectory() 
    {
        this.stopList = FXCollections.observableArrayList();
        this.moveList = FXCollections.observableArrayList();
    }

    /**Construtor com 1 parâmetro. */
    public Trajectory (ObservableList<Coordinate> points)
    {
        this.gpsPoints = points;
        this.date = points.get(0).getDate();
        this.beginHour = points.get(0).getHour();
        this.endHour = points.get(points.size() - 1).getHour();
    }
    
    
    /**Construtor com 2 parâmetros. */
    public Trajectory (String date, double dist)
    {
        this.date = date;
        this.distance = dist;
        this.stopList = FXCollections.observableArrayList();
        this.moveList = FXCollections.observableArrayList();
    }
    
    /**Construtor com 5 parâmetros. */
    public Trajectory (String collar, String hourBegin, String hourEnd, String date, String gap)
    {
        this.collar = collar;
        this.beginHour = hourBegin;
        this.endHour = hourEnd;
        this.date = date;
        this.gap = gap;
        this.stopList = FXCollections.observableArrayList();
        this.moveList = FXCollections.observableArrayList();
    }
    
    /**Retorna o valor do atributo id. */
    public int getId() 
    {
        return this.id;
    } 
    
    /**Seta o valor do atributo id. */
    public void setId (int id) 
    {
        this.id = id;
    } 
    
    
    /**Retorna o valor do atributo collar. */
    public String getCollar() 
    {
        return this.collar;
    } 
     
    
    /**Seta o valor do atributo collar. */
    public void setCollar (String collar) 
    {
        this.collar = collar;
    } 
    
    /**Retorna o valor do atributo date. */
    public String getDate() 
    {
        return this.date;
    } 
        
    /**Seta o valor do atributo date. */
    public void setDate (String date) 
    {
        this.date = date;
    } 
    
    /**Retorna a variavel beginHour. */
    public String getBeginHour()
    {
        return this.beginHour;
    }
    
    /**Seta o valor a variável beginHour. */
    public void setBeginHour(String hour)
    {
        this.beginHour = hour;
    }
    
    /**Retorna a variável endHour. */
    public String getEndHour()
    {
        return this.endHour;
    }
    
    /**Seta o valor a variável endHour. */
    public void setEndHour(String hour)
    {
        this.endHour = hour;
    }
		
    /**Retorna o valor do atributo distance. */
    public double getDistance() 
    {      
        return  Math.round(distance*100.0)/100.0;
    } 
        
    /**Seta o valor do atributo distance. */
    public void setDistance(double dist) 
    {
        this.distance = dist;
    }
    
    /**Retorna o valor do atributo avoidance. */
    public double getAvoidance() 
    {      
        return  this.avoidance;
    } 
        
    /**Seta o valor do atributo avoidance. */
    public void setAvoidance(double avoid) 
    {
        this.avoidance = avoid;
    }

    /**Retorna o valor do atributo GPSPoints. */
    public ObservableList<Coordinate> getGPSPoints() 
    {
        return this.gpsPoints;
    } 
    
    
    /**Seta o valor do atributo GPSPoints. */
    public void setGPSPoints(ObservableList<Coordinate> points) 
    {
        this.gpsPoints = points;
    }
    
    
    
    /**Retorna o valor do atributo stopList. */
    public ObservableList<Stop> getStops() 
    {
        return this.stopList;
    } 
    
    
    /**Seta o valor do atributo stopList. */
    public void setStops(ObservableList<Stop> stop) 
    {
        this.stopList = stop;
    }
    
    
    /**Retorna o valor do atributo moveList. */
    public ObservableList<Move> getMoves() 
    {
        return this.moveList;
    } 
    
    /**Seta o valor do atributo densityPoints. */
    public void setDensityPoints(ObservableList<DensityPoints> densityPoints) 
    {
        this.densityPoints = densityPoints;
    }
    
    /**Retorna o valor do atributo densityPoints. */
    public ObservableList<DensityPoints> getDensityPoints() 
    {
        return this.densityPoints;
    } 
    
    /**Seta o valor do atributo moveList. */
    public void setMoves(ObservableList<Move> move) 
    {
        this.moveList = move;
    }
    
            
    /**Retorna o valor do atributo gap. */
    public String getGap() 
    {
        return this.gap;
    } 
        
    /**Seta o valor do atributo gap. */
    public void setGap (String gapTime) 
    {
        this.gap = gapTime;
    }
    
    /**Calcula em metros a distância percorrida pela trajetória. */
    public double distanceTotal()
    {
        double dist = 0;
        
        for(int i = 0; i < this.gpsPoints.size() - 1; i++)
        {
            dist = dist + this.gpsPoints.get(i).distance(this.gpsPoints.get(i+1));
        }
        
        return dist;
    }
    
    /**Obter a duração do tempo um conjunto de pontos.
    * @return duration of time in miliseconds. */
    public long trajectoryDurationInSeconds() 
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(this.endHour);
        
        String[] parts = this.beginHour.split(":");
        dateTime = dateTime.minusSeconds(Integer.parseInt(parts[2]));
        dateTime = dateTime.minusMinutes(Integer.parseInt(parts[1]));
        dateTime = dateTime.minusHours(Integer.parseInt(parts[0]));
        
       return dateTime.getSecondOfDay();
    }

        
    /**Método que mostra a duração da trajetória. */
    public String trajectoryDuration()
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(this.endHour);
        
        String[] parts = this.beginHour.split(":");
        dateTime = dateTime.minusSeconds(Integer.parseInt(parts[2]));
        dateTime = dateTime.minusMinutes(Integer.parseInt(parts[1]));
        dateTime = dateTime.minusHours(Integer.parseInt(parts[0]));
                
        return dateTime.toString(formatter);        
    }
    
    
    /**Retorna a diferença horária em segundos. */
    public int timeDiferenceInSeconds(String hourA, String hourB) 
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(hourB);
        
        String[] parts = hourA.split(":");
        dateTime = dateTime.minusSeconds(Integer.parseInt(parts[2]));
        dateTime = dateTime.minusMinutes(Integer.parseInt(parts[1]));
        dateTime = dateTime.minusHours(Integer.parseInt(parts[0]));
        
       return dateTime.getSecondOfDay();
    }
    
    
    /**Retorna a velocidade média da Trajetória. */
    public double meanSpeed() 
    {      
        double totalDist = 0.0;
        Coordinate c2,c1 = gpsPoints.get(0);
        for (int i = 1; i < gpsPoints.size(); i++) 
        {
            c2 = gpsPoints.get(i);
            totalDist += c1.distance(c2);
            c1 = c2;
        }        

        double meanSpeed = totalDist/trajectoryDurationInSeconds();
        
        return meanSpeed;
    }
    
  
    /**Calcula a velocidade dos pontos da trajetória. */
    public void calculatePointsSpeed() 
    {
    	int i = 1;
    	while (i < this.gpsPoints.size()) 
        {
            Coordinate previous = gpsPoints.get(i-1);
            Coordinate current = gpsPoints.get(i);
            double dist = previous.distance(current);
            
            int time = timeDiferenceInSeconds(previous.getHour(), current.getHour());
            
            this.gpsPoints.get(i).setSpeed(dist/time);
            i++;
    	}
    }
    
    
    /**Método que retorna uma lista de pontos de velocidade. */
    public ObservableList<Coordinate> sortBySpeed() 
    {
        ObservableList<Coordinate> points = FXCollections.observableArrayList();
        ObservableList<Coordinate> gps = this.getGPSPoints();
        for (Coordinate c: gps) 
        {
            points.add(c);
        }

        Collections.sort(points, new Comparator<Coordinate>() 
        {
            @Override
            public int compare(Coordinate p1, Coordinate p2) 
            {
                 double s1 = p1.getSpeed();
                 double s2 = p2.getSpeed();
                 return s1 < s2 ? -1 : (s1 > s2 ? +1 : 0);
            }
        });
        
        return points;
    }
    
    
    /**Dado um ponto, retorna o vizinho de sua esquerda. */
    public Coordinate getLeftPoint(Coordinate c)
    {
       int index = this.gpsPoints.lastIndexOf(c);
       
       if(index <= 0)
       {
           return null;
       }
       else
       {
           return this.gpsPoints.get(index - 1);
       }
    }
    
    
    /**Dado um ponto, retorna o vizinho de sua direita. */
    public Coordinate getRightPoint(Coordinate c)
    {
       int index = this.gpsPoints.lastIndexOf(c);
       
       if(index >= this.gpsPoints.size())
       {
           return null;
       }
       else
       {
           return this.gpsPoints.get(index + 1);
       }
    }

}