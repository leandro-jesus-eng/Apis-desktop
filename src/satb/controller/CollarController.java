package satb.controller;

import javafx.collections.ObservableList;
import satb.model.Collar;
import satb.model.dao.CollarDAO;

/**Classe que representa a camada de controle para Colares.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class CollarController 
{  
    //Atributo
    private Collar collar;  
    
    /**Construtor.*/
    public CollarController()
    {  
       collar = new Collar();   
    }  
    
    /**Retorna o atributo collar.*/
    public Collar getCollar()
    {
        return collar;
    }
      

    /**Seta um objeto collar 
    @param collar - o valor do colar que irá setar o atributo collar.*/  
    public void setCollar (Collar collar) 
    {  
        this.collar = collar;  
    }
     
    
    /**Invoca o metodo selectAll() da classe CollarDAO.*/
    public ObservableList<Collar> selectAll() throws Exception
    {  
       CollarDAO pd = new CollarDAO();  
       return pd.selectAll();  
    }
    
    
    /**Invoca o metodo selectItens() da classe CollarDAO.*/
    public ObservableList<String> selectItens() throws Exception
    {  
       CollarDAO pd = new CollarDAO();  
       return pd.selectItens();  
    }
    
    
    /**Invoca o metodo selecCollarDates() da classe CollarDAO.*/
    public ObservableList<String> selectCollarDates(String collar) throws Exception
    {  
       CollarDAO pd = new CollarDAO();  
       return pd.selectCollarDates(collar);  
    }
    
    
    /**Invoca o metodo selectCollar() da classe CollarDAO.*/
    public void selectCollar(String collar) throws Exception
    {  
       CollarDAO pd = new CollarDAO();  
       this.setCollar(pd.selectCollar(collar));  
    }
    
    
    /**Método que recebe um color e o envia para a função insertData() de CollarDAO.*/
    public void insertData(Collar c) throws Exception
    {
       CollarDAO pd = new CollarDAO();
       pd.insertData(c);
    }
    
    
    /**Método que invoca updateCollar() da classe CollarDAO.*/
    public void updateCollar(String collar, String company) throws Exception
    {  
       CollarDAO pd = new CollarDAO();  
        pd.updateCollar(new Collar(collar, company));  
    }
    
    
    /**Método que recebe um colar e o envia para a função insertData() de CollarDAO.*/
    public void removeCollar (String collar) throws Exception
    {
       CollarDAO pd = new CollarDAO();
       pd.removeCollar(collar);
    }
  
}
