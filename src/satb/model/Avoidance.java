package satb.model;

public class Avoidance 
{
    //Atributo
    private double avoidance;
    private double ponderation;
    private int indexTargetObject;
    private int indexTrajectory;
    
    /**Construtor de 2 parâmetros. */
    public Avoidance(int indexTrajectory, int indexTarget)
    {
        this.indexTrajectory = indexTrajectory;
        this.indexTargetObject = indexTarget;
        this.ponderation = 0.5;
    }
    
    
    /**Retorna o valor de avoidance. */
    public double getAvoidance()
    {
        return this.avoidance;
    }
    
    /**Seta o valor de avoidance. */
    public void setAvoidance(double avoidance)
    {
        this.avoidance = avoidance;
    }
    
    /**Retorna o valor de ponderation. */
    public double getPonderation()
    {
        return this.ponderation;
    }
    
    /**Seta o valor de avoidance. */
    public void setPonderation(double ponderation)
    {
        this.ponderation = ponderation;
    }
   
    /**Retorna o valor de indexTargetObject. */
    public int getIndexTargetObject()
    {
        return this.indexTargetObject;
    }
    
    /**Seta o valor de indexTargetObject. */
    public void setIndexTargetObject(int index)
    {
        this.indexTargetObject = index;
    }
    
    /**Retorna o valor de indexTrajectory. */
    public int getIndexTrajectory()
    {
        return this.indexTrajectory;
    }
    
    /**Seta o valor de indexTrajectory. */
    public void setIndexTrajectory(int index)
    {
        this.indexTargetObject = index;
    }
    
}
