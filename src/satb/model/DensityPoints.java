package satb.model;

/**Classe que representa o Conjunto de Pontos de Densidade.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class DensityPoints 
{
    //Atributos
    private String areaShadow;
    private String hourBegin;
    private String hourEnd;
    
    /**Construtor de três parâmetros. */
    public DensityPoints(String areaShadow, String hourBegin, String hourEnd)
    {
        this.areaShadow = areaShadow;
        this.hourBegin = hourBegin;
        this.hourEnd = hourEnd;
    }
    
    /**Retorna o valor do atributo areaShadow. */
    public String getAreaShadow()
    {
        return this.areaShadow;
    }
    
    /**Seta o valor do atributo areaShadow. */
    public void setAreaShadow(String areaShadow)
    {
        this.areaShadow = areaShadow;
    }
   
    /**Retorna o valor do atributo hourBegin. */
    public String getHourBegin()
    {
        return this.hourBegin;
    }
    
    /**Seta o valor do atributo hourBegin. */
    public void setHourBegin(String hourBegin)
    {
        this.hourBegin = hourBegin;
    }
    
    /**Retorna o valor do atributo hourEnd. */
    public String getHourEnd()
    {
        return this.hourEnd;
    }
    
    /**Seta o valor do atributo hourEnd. */
    public void setHourEnd(String hourEnd)
    {
        this.hourEnd = hourEnd;
    }
    
}
