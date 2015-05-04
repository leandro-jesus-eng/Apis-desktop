package satb.controller;

import javafx.collections.ObservableList;
import satb.model.Tree;
import satb.model.dao.PastureDAO;
import satb.model.dao.TreeDAO;
import org.postgis.Point;

/**Classe que implementa a parte lógica de Tree.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class TreeController 
{
    //Atributo
    private Tree tree; 
    
    /**Construtor Default. */
    public TreeController()
    {  
       tree = new Tree();   
    }
    
    /**Retorna o atributo tree. */
    public Tree getTree()
    {
        return tree;
    }
      

    /**Seta um objeto tree com informações de outro passado por parâmetro. */  
    public void setTree (Tree t) 
    {  
        this.tree = t;  
    }  
    
  
    /**Invoca o método selectAll() da classe TreeDAO. */
    public ObservableList<Tree> selectAll(String name) throws Exception
    {  
       TreeDAO pd = new TreeDAO();
       PastureDAO pDAO = new PastureDAO(); 
       
       int id = pDAO.selectId(name);       
       return pd.selectAll(id);  
    }
  
    
    /**Invoca o método insertTree da classe TreeDAO. */
    public void insertTree (String p, String n, double lat, double lon) throws Exception
    {  
       TreeDAO tDAO = new TreeDAO();
       PastureDAO pDAO = new PastureDAO(); 
            
       int id = pDAO.selectId(p);       
       tDAO.insertTree(new Tree (id, n, lon, lat));  
    }
    
    /**Invoca o método selectItem() da classe TreeDAO. */
    public ObservableList<String> selectItem(String n) throws Exception
    {  
        PastureDAO pDAO = new PastureDAO(); 
        TreeDAO td = new TreeDAO();
        
        int id = pDAO.selectId(n); 
          
        return td.selectItem(id);  
    }
    
    /**Invoca o método selectTree() da classe TreeDAO. */
    public void selectTree (String pasture, String tree) throws Exception
    {  
        PastureDAO pDAO = new PastureDAO(); 
        TreeDAO td = new TreeDAO();
        
        int id = pDAO.selectId(pasture); 
          
        setTree(td.selectTree(id, tree));  
    }
    
    /**Método que remove a árvore de um pasto. */ 
    public void removeTree (String n) throws Exception
    {
       TreeDAO td = new TreeDAO();
       td.removeTree(n);     
    }
    
    
    /**Método que edita os dados de uma árvore. */
    public void updateTree (String p, String n, double lat, double lon) throws Exception
    {
       TreeDAO td = new TreeDAO();
       PastureDAO pd = new PastureDAO(); 
        
       int id = pd.selectId(p); 
       
       this.getTree().setPasture(id);
       this.getTree().setNumberTree(n);
       this.getTree().setLatitude(lat);
       this.getTree().setLongitude(lon);
       this.getTree().setCoordinate(new Point(lon, lat));
             
       td.updateTree(this.getTree());     
    }
     
     
}
