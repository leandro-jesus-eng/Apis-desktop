package satb;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

/**Classe que oferece metódo para abertura e execução de arquivos .kml e .kmz.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class OpenFile 
{
    //Atributos
    private FileChooser fileChooser;
    private File file;
    private final Label labelFile;
    private String path;
    
    /**Construtor.*/
    public OpenFile()
    {
        fileChooser = new FileChooser();
        labelFile = new Label();
        path = "C:\\Program Files (x86)\\Google\\Google Earth\\client\\googleearth.exe";
    }
    
    /**Método.*/
    public void execute()
    {
        
        //Abre um diretório de um já existente
        if(file != null)
        {
               File existDirectory = file.getParentFile();
               fileChooser.setInitialDirectory(existDirectory);
        }
        //Define o filtro da extensão (.kml e .kmz)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("KML files (*.kml)", "*.kml");
        FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("KMZ files (*.kmz)", "*.kmz");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.getExtensionFilters().add(extFilter2);
        
        //Abrir e mostrar a janela de diretórios
        file = fileChooser.showOpenDialog(null); 
        //Caso algum arquivo tenha sido selecionado
        if(file != null)
        {
            try 
            {
                labelFile.setText(file.getPath());  
                Runtime.getRuntime().exec(new String[] {path,file.getPath()});
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(OpenFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
