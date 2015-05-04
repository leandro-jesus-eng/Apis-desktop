package satb.model;

/**Classe representa um colar.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class Collar 
{
    //Atributo
    private int id;
    private String collar;
    private String company;

    /**Construtor Default. */
    public Collar(){}

    /**Construtor com dois parâmetros.
    @param collar - a identificação do colar.
    @param company - o nome da empresa.*/
    public Collar (String collar, String company)
    {
        this.collar = collar;
        this.company = company;
    }

    /**Método que retorna o atributo id. */ 
    public int getId() 
    {
        return this.id;
    } 

    /**Método que seta o valor do atributo id. */ 
    public void setId(int id) 
    {
        this.id = id;
    } 

    /**Método que retorna o valor do colar. */
    public String getCollar() 
    {
        return collar;
    } 

    /**Método que seta o valor do atributo colar. */
    public void setCollar(String collar) 
    {
        this.collar = collar;
    } 

    /**Método que seta o valor do atributo company. */
    public String getCompany() 
    {
        return company;
    } 

    /**Método que seta o valor do atributo company. */
    public void setCompany(String company) 
    {
        this.company = company;
    }
}
