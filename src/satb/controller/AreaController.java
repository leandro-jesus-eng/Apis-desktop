package satb.controller;

import java.io.File;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import satb.Util;
import satb.controller.functions.KMLFunctions;
import satb.model.Area;
import satb.model.Coordinate;
import satb.model.dao.AreaDAO;
import satb.model.dao.PastureDAO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**Classe que implementa a parte lógica de Área.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class AreaController 
{
    //Atributo
    private Area area;
    private KMLFunctions kml;
    private FileChooser fileChooser;
    private File file;

    
    /**Construtor. */
    public AreaController()
    {  
       area = new Area(); 
       kml = new KMLFunctions();
       fileChooser = new FileChooser();
    }
    
    /**Retorna o atributo area. */
    public Area getArea()
    {
        return area;
    }
    
    /*Seta o valor do atributo area. */ 
    public void setArea (Area a) 
    {  
        this.area = a;  
    } 

    /**Invoca o método selectAreas() da classe AreaDAO. */
    public ObservableList<Area> selectAreasFromPasture(int id) throws Exception
    {  
       AreaDAO ad = new AreaDAO();  
       return ad.selectAreasFromPasture(id);  
    }
    
    /**Invoca o método selectItens() da classe AreaDAO. */
    public ObservableList<String> selectItens(String pasture) throws Exception
    {  
       AreaDAO ad = new AreaDAO(); 
       PastureDAO pDAO = new PastureDAO(); 
       
       int id = pDAO.selectId(pasture); 
       
       return ad.selectItens(id);  
    }
    
    
    /**Invoca o método selectId() da classe AreaDAO. */
    public int selectId (String area) throws Exception
    {  
       AreaDAO ad = new AreaDAO();  
       return ad.selectId(area);  
    }
    
    
    /**Invoca o método selectArea() da classe AreaDAO. */
    public void selectArea (String name) throws Exception
    {  
       AreaDAO ad = new AreaDAO();  
       this.setArea(ad.selectArea(name));  
    }
    
 
    /**Método que insere os dados de um novo pasto e as coordenadas da área. */ 
    public void insertArea(String pasture, String n, String i, ObservableList<Coordinate> p) throws Exception
    {
       AreaDAO ad = new AreaDAO();
       PastureDAO pd = new PastureDAO();
       int pastureid = pd.selectId(pasture);
       
       Area a = new Area(pastureid, n, i);
       a.setCoordinates(p);
       
       ad.insertArea(a);
       
    }
    
    
    /**Remove a área e as suas coordenadas.*/  
    public void removeArea(String n) throws Exception
    {
       AreaDAO ad = new AreaDAO();
       int id = ad.selectId(n);
       ad.removeArea(n);
    }
    
    /**Método que edita os dados de um novo pasto e as coordenadas da área. */ 
    public void updateArea(String pasture, String area, String minimumTime, ObservableList<Coordinate> points) throws Exception
    {
       AreaDAO ad = new AreaDAO();
       PastureDAO pd = new PastureDAO();
       
       int id = pd.selectId(pasture);
       this.getArea().setPasture(id);
       this.getArea().setName(area);
       this.getArea().setMinimumTime(minimumTime);
       this.getArea().setCoordinates(points);
       
       ad.updateArea(this.getArea());
    }
    
    
    /**Extrai as coordenadas de área do arquivo Arff. */
    public ObservableList<Coordinate> extractARFFPoints(Instances datas) throws Exception 
    {
        ObservableList<Coordinate> areaPoints = FXCollections.observableArrayList();
        
        for(int k=0 ; k < datas.numInstances(); k++) {
            Instance data = datas.instance(k);        
        //for(Instance data : datas.)
        //{
            
            Coordinate coord = new Coordinate(data.value(0), data.value(1));
            areaPoints.add(coord);           
        }
        
        return areaPoints;
         
    }
    
    
    /**Método que efetua a leitura de um .txt com os dados dos colares. */
    public ObservableList<Coordinate> readARFF() throws IOException, Exception
    {    
        //Abre um diretório de um já existente
        if(file != null)
        {
               File existDirectory = file.getParentFile();
               fileChooser.setInitialDirectory(existDirectory);
               fileChooser.getExtensionFilters().clear();
        }
        
        //Define o filtro da extensão (.arrd)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ARFF files (*.arff)", "*.arff");
        fileChooser.getExtensionFilters().add(extFilter);
        
        //Abrir e mostrar a janela de diretórios
        file = fileChooser.showOpenDialog(null); 

        //Caso algum arquivo tenha sido selecionado
        if(file != null)
        {    
            try
            {
                ConverterUtils.DataSource source = new ConverterUtils.DataSource(file.getPath());
           

                Instances data = source.getDataSet();
                System.out.println(data.toString());
                // setting class attribute if the data format does not provide this information
                // For example, the XRFF format saves the class attribute information as well
                if (data.classIndex() == -1) 
                {
                    data.setClassIndex(data.numAttributes() - 1);
                }
                
                 return extractARFFPoints(data);
                
            }
            catch(Exception e)
            {
                
                System.out.println("Erro: " + e.getMessage());
            }
                
        }
        
        return null;
        
    }
    
   
    /**Constrói um KML para a Área e o executa no Google Earth. */  
    public void showArea(String name, ObservableList<Coordinate> area) throws Exception
    {
        String path = "C:/Program Files (x86)/Google/Google Earth/client/googleearth.exe";
        String pathkml = Util.getCurrentRelativePath();

        //Invoca o método de construção do KML
        File fileArea = kml.buildArea(kml.buildAreaKmlName(name), name, area);

        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path, pathkml.concat(fileArea.getPath())});   
    }
    
    
}
