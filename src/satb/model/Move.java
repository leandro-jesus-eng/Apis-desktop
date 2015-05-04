package satb.model;

/**Classe que representa o objeto Move.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class Move 
{
    private int id;
    private Stop startStop, endStop;
    private String startTime, endTime;
    
    /**Construtor Default*/
    public Move(){}
    
    /**Construtor com 4 parâmetros*/
    public Move(Stop startStop, Stop endStop, String startTime, String endTime) 
    {
        this.startStop = startStop;
        this.endStop = endStop;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    /**Retorna o valor de id.*/
    public int getId() 
    {
        return this.id;
    } 
   
    /**Seta o valor de id.*/
    public void setId(int id) 
    {
        this.id = id;
    }
    
    /**Retorna o valor do atributo startTime.*/
    public String getStartTime() 
    {
        return this.startTime;
    } 
        
    /**Seta o valor de startTime.*/
    public void setStartTime(String time) 
    {
       this.startTime = time;
    } 
    
    /**Retorna o valor do atributo endTime.*/
    public String getEndTime() 
    {
        return this.endTime;
    } 
        
    /**Seta o valor de endTime.*/
    public void setEndTime(String time) 
    {
       this.endTime = time;
    } 
}
