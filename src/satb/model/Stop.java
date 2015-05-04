package satb.model;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**Classe que representa o objeto Stop.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class Stop 
{
    //Atributos
    private int areaId;
    private String areaName;
    private String startTime, endTime;
    
    /**Cria uma nova instância de Stop*/
    public Stop(int areaId, String nameStop, String start, String end) 
    {
        this.areaId = areaId;
        this.areaName = nameStop;
        this.startTime = start;
        this.endTime = end;
    }
    
    /**Construtor com 3 parâmetros.*/
    public Stop(String nameStop, String start, String end) 
    {
        this.areaName = nameStop;
        this.startTime = start;
        this.endTime = end;
    }
    
    
    /**Retorna a variável areaId.*/
    public int getAreaId()
    {
        return this.areaId;
    }
    
    
    /**Seta o valor a variável areaId.*/
    public void setAreaId(int id)
    {
        this.areaId = id;
    }
    
    
    /**Retorna a variavel areaName.*/
    public String getAreaName()
    {
        return this.areaName;
    }
    
    /**Seta o valor a variável areaName.*/
    public void setAreaName(String area)
    {
        this.areaName = area;
    }
    
    
    /**Retorna a variavel startTime.*/
    public String getStartTime()
    {
        return this.startTime;
    }
    
    /**Seta o valor a variável startTime.*/
    public void setStartTime(String time)
    {
        this.startTime = time;
    }
    
    /**Retorna a variavel endTime.*/
    public String getEndTime()
    {
        return this.endTime;
    }
    
    /**Seta o valor a variável endTime.*/
    public void setEndTime(String time)
    {
        this.endTime = time;
    }
    
    
    /**Método que mostra a duração do objeto no ponto de parada.*/
    public String stopDuration()
    {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm:ss");
        DateTime dateTime = formatter.parseDateTime(this.endTime);
        
        String[] parts = this.startTime.split(":");
        dateTime = dateTime.minusSeconds(Integer.parseInt(parts[2]));
        dateTime = dateTime.minusMinutes(Integer.parseInt(parts[1]));
        dateTime = dateTime.minusHours(Integer.parseInt(parts[0]));
        
        return dateTime.toString(formatter);        
    }
    
}
