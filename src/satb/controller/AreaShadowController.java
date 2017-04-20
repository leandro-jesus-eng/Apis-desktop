package satb.controller;

import java.io.File;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import satb.Util;
import satb.controller.functions.KMLFunctions;
import satb.model.AreaShadow;
import satb.model.Coordinate;
import satb.model.dao.AreaShadowDAO;
import satb.model.dao.PastureDAO;
import satb.model.dao.TreeDAO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**Classe que implementa a parte lógica de Área Shadow.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class AreaShadowController 
{
    //Atributo
    private AreaShadow areaShadow;
    private KMLFunctions kml;
    private FileChooser fileChooser;
    private File file;
    
    /**Construtor Default. */
    public AreaShadowController()
    {  
       areaShadow = new AreaShadow(); 
       kml = new KMLFunctions();
       fileChooser = new FileChooser();
    }
    
    /**Retorna o atributo areaShadow. */
    public AreaShadow getAreaShadow()
    {
        return areaShadow;
    }
    
    /**Seta o valor do atributo areaShadow. */
    public void setAreaShadow(AreaShadow a)
    {
        this.areaShadow = a;
    }
     
    /**Invoca o método selectAreaShadow() da classe AreaShadowDAO. */
    public void selectAreaShadow(String name) throws Exception
    {  
       AreaShadowDAO ad = new AreaShadowDAO();  
       this.setAreaShadow(ad.selectAreaShadow(name));  
    }
        
   
    /**Invoca o método selectAll() da classe AreaShadowDAO. */
    public ObservableList<AreaShadow> selectAll(String tree) throws Exception
    {  
       AreaShadowDAO ad = new AreaShadowDAO();  
       TreeDAO td = new TreeDAO();
       
       int id = td.selectId(tree);
       return ad.selectAll(id);  
    }
     
    
    /**Invoca o método selectId() da classe AreaShadowDAO. */
    public int selectId (String area) throws Exception
    {  
       AreaShadowDAO ad = new AreaShadowDAO();  
       return ad.selectId(area);  
    }
    
     
    /**Invoca o método selectItens() da classe AreaShadowDAO. */
    public ObservableList<String> selectItens (String tree) throws Exception
    {  
       AreaShadowDAO ad = new AreaShadowDAO();
       TreeDAO td = new TreeDAO();
       
       int id = td.selectId(tree);
       return ad.selectItens(id);  
    }
    
    
    /**Insere os dados de um novo pasto e as coordenadas da área. */ 
    public void insertAreaShadow(String name, String pasture, String tree, String hourBegin, String hourEnd, ObservableList<Coordinate> points, String minTime) throws Exception
    {
       AreaShadowDAO ad = new AreaShadowDAO();
       PastureDAO pd = new PastureDAO();
       TreeDAO td = new TreeDAO();
       int pastureid = pd.selectId(pasture);
       int treeid  = td.selectId(tree);
       
       AreaShadow area = new AreaShadow();
       area.setPasture(pastureid);
       area.setTree(treeid);
       area.setName(name);
       area.setHourBegin(hourBegin);
       area.setHourEnd(hourEnd);
       area.setCoordinates(points);
       area.setMinTime(minTime);
       
       ad.insertAreaShadow(area);

    }
    
    
    /**Invoca o método de AreaShadowDAO para remoção de uma área sombreada. */  
    public void removeAreaShadow(String areaShadow) throws Exception
    {
       AreaShadowDAO ad = new AreaShadowDAO();
       ad.removeAreaShadow(areaShadow);
    }
    
    
    /**Edita os dados de um novo pasto e as coordenadas da área. */ 
    public void updateAreaShadow(String pasture, String tree, String area, String hb, String he, ObservableList<Coordinate> points, String minTime) throws Exception
    {
       AreaShadowDAO ad = new AreaShadowDAO();
       PastureDAO pd = new PastureDAO();
       TreeDAO td = new TreeDAO();
       
       int id = pd.selectId(pasture);
       int treeid = td.selectId(tree);
       this.getAreaShadow().setPasture(id);
       this.getAreaShadow().setTree(treeid);
       this.getAreaShadow().setName(area);
       this.getAreaShadow().setHourBegin(hb);
       this.getAreaShadow().setHourEnd(he);
       this.getAreaShadow().setCoordinates(points);
       this.getAreaShadow().setMinTime(minTime);
       
       ad.updateAreaShadow(this.getAreaShadow());
    }
    
    
    /**Extrai as coordenadas de área do arquivo Arff. */
    public ObservableList<Coordinate> extractARFFPoints(Instances datas) throws Exception 
    {
        ObservableList<Coordinate> areaPoints = FXCollections.observableArrayList();
        
        for(int k=0 ; k < datas.numInstances(); k++) {
            Instance data = datas.instance(k);        
        //for(Instance data : datas)
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