package satb.controller;

import java.io.File;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import satb.controller.functions.KMLFunctions;
import satb.model.Coordinate;
import satb.model.Pasture;
import satb.model.dao.PastureDAO;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**Classe que representa a camada de controle para Pastos.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class PastureController 
{   
    //Atributo
    private Pasture pasture; 
    private KMLFunctions kml;
    private FileChooser fileChooser;
    private File file;
    
    /**Construtor. */
    public PastureController()
    {  
       fileChooser = new FileChooser(); 
       pasture = new Pasture();
       kml = new KMLFunctions();
    }
    
    /**Retorna o atributo pasture. */
    public Pasture getPasture()
    {
        return pasture;
    }
    
    /**Seta um objeto pasture com informações de outro passado por parâmetro. */  
    public void setPasture(Pasture p) 
    {  
        this.pasture = p;  
    } 
   
    /**Invoca o método selectItens() da classe PastureDAO. */
    public ObservableList<String> selectItens() throws Exception
    {  
        PastureDAO pd = new PastureDAO();  
        return pd.selectItens();  
    }

    /**Invoca o metodo selectPasture() da classe PastureDAO. */
    public void selectPasture (String name) throws Exception
    {  
       PastureDAO pd = new PastureDAO();  
       this.setPasture(pd.selectPasture(name));  
    }
    
 
    /**Insere os dados de um novo pasto e as coordenadas da área. */ 
    public void insertPasture(String n, float a, ObservableList<Coordinate> p) throws Exception
    {
       PastureDAO pd = new PastureDAO();
       Pasture past = new Pasture(0, n, a);
       past.setCoordinates(p);
       
       pd.insertPasture(past);

    }
    
    
    /**Remove o pasto e as coordenadas da área. */ 
    public void removePasture(String n) throws Exception
    {
       PastureDAO pd = new PastureDAO();
       pd.removePasture(n);
    }
   
    
    /**Edita os dados de um pasto e as suas coordenadas. */ 
    public void updatePasture (String name, double hec, ObservableList<Coordinate> points) throws Exception
    {
       PastureDAO pd = new PastureDAO();
       
       this.getPasture().setName(name);
       this.getPasture().setHectars(hec);
       this.getPasture().setCoordinates(points);
       
       pd.updatePasture(this.getPasture());
        
    }
    
    
    /**Extrai as coordenadas de área do arquivo Arff. */
    public ObservableList<Coordinate> extractARFFPoints(Instances datas) throws Exception 
    {
        ObservableList<Coordinate> areaPoints = FXCollections.observableArrayList();
        
        for(int k=0 ; k < datas.numInstances(); k++) {
            Instance data = datas.instance(k);        
        //for(Instance data : datas)  {         
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
                System.out.println("Lalala");
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
        String pathkml = "C:/Users/Marcel/Documents/Mestrado/";
                
        //Invoca o método de construção do KML
        File fileArea = kml.buildArea(kml.buildAreaKmlName(name), name, area);

        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path, pathkml.concat(fileArea.getPath())});   
    }
    
}