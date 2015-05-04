package satb.controller;

import java.io.File;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import satb.controller.functions.KMLFunctions;
import satb.model.Coordinate;
import satb.model.dao.SeeTrackDAO;

/**Classe que implementa a função para mostrar a trajetória.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class SeeTrackController 
{
    //Atributos
    private String path, pathkml;
    private KMLFunctions kml;
    private FileChooser fileChooser;
    private File fileKML;
    
    /**Construtor Default. */
    public SeeTrackController()
    {
        path = "C:/Program Files (x86)/Google/Google Earth/client/googleearth.exe";
        pathkml = "C:/Users/Marcel/Documents/Mestrado/";
        kml = new KMLFunctions();
        fileChooser = new FileChooser();
    }
    
    /**Invoca o método selectCollar() da classe SeeTrackDAO. */
    public ObservableList<String> selectCollar() throws Exception
    {  
        SeeTrackDAO pd = new SeeTrackDAO();  
        return pd.selectCollar();  
    }
    
    
    /**Invoca o método selectDate() da classe CollarDAO. */
    public ObservableList<String> selectDate(String collar) throws Exception
    { 
        SeeTrackDAO pd = new SeeTrackDAO();  
        return pd.selectDate(collar);  
    }
    
    
    /**Abre um diretório para pegar o caminho de um arquivo arff. */
    public String createFile() throws Exception 
    {        
        if(fileKML != null)
        {
               File existDirectory = fileKML.getParentFile();
               fileChooser.setInitialDirectory(existDirectory);
        }
        
         //Define o filtro da extensão (.kml e .kmz)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KML files (*.kml)", "*.kml");
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("KMZ files (*.kmz)", "*.kmz");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.getExtensionFilters().add(extFilter2);
        
        //Abrir e mostrar a janela de diretórios
        fileKML = fileChooser.showOpenDialog(null); 
        
        if(fileKML != null)
        {    
            try
            {
                return fileKML.getPath();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                
            }
        }
        return null;
    }
    
    
    /**Manda as informações armazenadas em fileKML no Google Earth. */
    public void callGoogleEarth() throws IOException
    {
        Runtime.getRuntime().exec(new String[] {path,fileKML.getPath()});
    }

    
    /**Mostra o rastro do animal obtido com o colar. */
    public void showCoordinates(String collar, String date, String hourBefore, String hourAfter, String hourBegin, String hourEnd) throws Exception
    {
        SeeTrackDAO pd = new SeeTrackDAO();
        pd.whereHour(hourBefore, hourAfter, hourBegin, hourEnd);
        String pathfile = pathkml;
        
        ObservableList<Coordinate> list = pd.selectCoordinates(collar, date);  
        
        //Invoca o método de construção do KML
        File file = kml.buildTrackKML(kml.buildKmlName(collar,date), list);
    
        //Executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path,pathfile.concat(file.getPath())});    
    }
    
    
    /**Mostra dois rastros obtidos com o colar. */
    public void showCoordinates(String collar, String date, String secondCollar, String secondDate, String hourBefore, String hourAfter, String hourBegin, String hourEnd, String hourBeforeB, String hourAfterB, String hourBeginB, String hourEndB) throws Exception
    {
        SeeTrackDAO pd = new SeeTrackDAO();
        String pathfile = pathkml;
        
        pd.whereHour(hourBefore, hourAfter, hourBegin, hourEnd);
        ObservableList<Coordinate> list = pd.selectCoordinates(collar, date); 
        
        pd.whereHour(hourBeforeB, hourAfterB, hourBeginB, hourEndB);
        ObservableList<Coordinate> secondList = pd.selectCoordinates(secondCollar, secondDate);      
        
        //invoca o método de construção do KML
        File file = kml.build2TracksKML(kml.build2KmlName(collar, date, secondCollar, secondDate), list, secondList);
    
        //executa o google earth com o kml gerado
        Runtime.getRuntime().exec(new String[] {path,pathfile.concat(file.getPath())});    
    }
  
}