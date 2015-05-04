package satb.model;

/**Classe que representa o objeto Clustering.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class Clustering 
{
    //Atributos
    private int clusterId;
    private int size;
    private String centroid;
    private String medoid;
    
    /**Contrutor de 2 parâmetros. */
    public Clustering (int id, int size)
    {
        this.clusterId = id;
        this.size = size;
    }
    
    /**Contrutor de 3 parâmetros. */
    public Clustering (int id, int size, String center)
    {
        this.clusterId = id;
        this.size = size;
        this.centroid = center;
    }
   
    /**Retorna o valor do atributo clusterId. */
    public int getClusterId()
    {
        return clusterId;
    }
    
    /**Seta o valor do atributo clusterId. */
    public void setClusterId(int c)
    {
        this.clusterId = c;
    }
    
    /**Retorna o valor do tamanho do cluster. */
    public int getSize()
    {
        return size;
    }
    
    /**Seta o valor do atributo size. */
    public void setSize(int s)
    {
        this.size = s;
    }
    
    /**Retorna o valor do atributo centroid. */
    public String getCentroid()
    {
        return centroid;
    }
    
    /**Seta o valor do atributo centroid. */
    public void setCentroid(String c)
    {
        this.centroid = c;
    }
    
    
    /**Retorna o valor do atributo medoid. */
    public String getMedoid()
    {
        return medoid;
    }
    
    
    /**Seta o valor do atributo medoid. */
    public void setMedoid(String m)
    {
        this.medoid = m;
    }
}
