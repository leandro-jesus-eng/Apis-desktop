package satb.controller;

import javafx.collections.ObservableList;
import satb.model.Classifier;
import satb.model.Coordinate;
import satb.model.dao.ClassifierDAO;
import satb.model.dao.PastureDAO;

/**Classe que implementa algoritmos de Classificação.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class ClassifierController 
{    
    //Atributos
    private int inPasture;
    private int inPastureTrue;
    private int inPastureFalse;
    private ObservableList<Classifier> area;
    private ObservableList<Coordinate> areaShadowList;
    private int inShadow;
    
    /**Construtor Default. */
    public ClassifierController(){}

    public void ruleBased(String collar, String date, String dateBegin, String dateEnd, String pasture, String hourBefore, String hourAfter, String hourBegin, String hourEnd) throws Exception
    {
        ClassifierDAO cDAO = new ClassifierDAO();
        PastureDAO pDAO = new PastureDAO();  
        
        cDAO.whereHour(hourBefore, hourAfter, hourBegin, hourEnd);
        cDAO.whereDate(date, dateBegin, dateEnd);
        int pastureid = pDAO.selectId(pasture);
        
        this.inPasture = cDAO.selectInPasture(collar);
        this.inPastureTrue = cDAO.selectInPastureTrue(pastureid, collar); 
        this.area = cDAO.selectInAreas(pastureid, collar);
        this.inPastureFalse = inPasture - inPastureTrue;
        this.inShadow = cDAO.selectInAreasShadow(pastureid, collar);
        this.areaShadowList = cDAO.selectAreaInShadow(collar);
              
        for (Classifier c : area)
        {
            if(c.getPointsTrue() > 0)
            {
                c.setCoordinates(cDAO.showTime(pastureid, c.getArea() , collar));           
            }              
        }     
    }
    
    /**Retorna o valor de inShadow.*/
    public String getInShadow()
    {
        return String.valueOf(inShadow);
    } 
      
    
    /**Retorna o valor de inPasture.*/
    public String getInPasture()
    {
        return String.valueOf(inPasture);
    }
       
    
    /**Retorna o valor de inPastureTrue.*/
    public String getInPastureTrue()
    {
        return String.valueOf(inPastureTrue);
    } 
    
    
    /**Retorna o valor de inPastureFalse.*/
    public String getInPastureFalse()
    {
        return String.valueOf(inPastureFalse);
    } 
    
    
    /**Retorna a coleção de dados de área.*/
    public ObservableList<Classifier> getArea()
    {
        return area;
    }
    
    
    /**Retorna a coleção de dados presentes na sombra.*/
    public ObservableList<Coordinate> getAreaShadow()
    {
        return areaShadowList;
    }
    
}
