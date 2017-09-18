package satb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import satb.controller.CollarDataController;
import satb.view.About;
import satb.view.ActivityRecognitionView;
import satb.view.AreaShadowView;
import satb.view.AreaView;
import satb.view.CollarDataView;
import satb.view.CollarView;
import satb.view.PastureView;
import satb.view.QuestionsView;
import satb.view.SeeTrackView;
import satb.view.SimulatorPointsView;
import satb.view.TrajectoryClusteringView;
import satb.view.TrajectoryPatternView;
import satb.view.TrajectoryView;
import satb.view.TreeView;

/**Classe referente a barra de ferramentas principal do software.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public class ToolBar 
{ 
    //Atributos
    private MenuBar menuBar;
    
    /**Construtor que constroi o menu. */
    public ToolBar()
    {
         menuBar = new MenuBar();
         menuBar.setId("menu");
 
         //MENU 1
         Menu menu1 = new Menu("Coletor");       
         MenuItem menuItem1A = new MenuItem("Submissão de Trajetórias");
         menuItem1A.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {                
                try 
                {
                     CollarDataView cd = new CollarDataView();
                     cd.execute();
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                }              
            }
         });
         
         MenuItem menuItem1B = new MenuItem("Simular Trajetórias");
         menuItem1B.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {                
                try 
                {
                     SimulatorPointsView s = new SimulatorPointsView();
                     s.execute();
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                }               
            }
         });
         
         MenuItem menuItem1C = new MenuItem("Importar Dados Meteorológicos ");
         menuItem1C.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {                
                try 
                {
                     new CollarDataController().readTxtINMET();
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                }               
            }
         });
         
         
         
 
         //MENU 2
         Menu menu2 = new Menu("Cadastro");       
         MenuItem menuItem2A = new MenuItem("Áreas de Interesse");
         menuItem2A.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {                
                try 
                {
                     AreaView area = new AreaView();
                     area.execute();
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
         });
         
         
         MenuItem menuItem2B = new MenuItem("Áreas Sombreadas");
         menuItem2B.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {                
                try 
                {
                     AreaShadowView areaShadow = new AreaShadowView();
                     areaShadow.execute();
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
         });
         
         MenuItem menuItem2C = new MenuItem("Árvores");
         menuItem2C.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {
                try 
                {
                     TreeView tree = new TreeView();
                     tree.execute();
                } 
                catch (Exception ex) 
                {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
         });
         
         MenuItem menuItem2D = new MenuItem("Colares");
         menuItem2D.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {
                 try 
                 {
                     CollarView c = new CollarView();
                     c.execute();
                 } 
                 catch (Exception ex) 
                 {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                 }
                
            }
         });
         
         MenuItem menuItem2E = new MenuItem("Pastagens");
         menuItem2E.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {
                 try 
                 {
                     PastureView p = new PastureView();
                     p.execute();
                 } catch (Exception ex) {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                 }
                
            }
         });
         
        
         //MENU 2
         Menu menu3 = new Menu("Trajetórias");

         MenuItem menuItem3A = new MenuItem("Observar Trajetória");
         menuItem3A.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {
                 try 
                 {
                    SeeTrackView s = new SeeTrackView();
                    s.execute();
                 } 
                 catch (Exception ex) 
                 {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                 }
                
            }
         });
         
         MenuItem menuItem3B = new MenuItem("Adicionar Semântica");
         menuItem3B.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {
                 try 
                 {
                     TrajectoryView t = new TrajectoryView();
                     t.execute();
                 } 
                 catch (Exception ex) 
                 {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                 }
              
            }
         });
   
         MenuItem menuItem3C = new MenuItem("Agrupamento em Trajetória");
         menuItem3C.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {
                 try 
                 {
                     TrajectoryClusteringView tcv = new TrajectoryClusteringView();
                     tcv.execute();
                 } 
                 catch (Exception ex) 
                 {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
            }
         });
         
         MenuItem menuItem3D = new MenuItem("Padrões em Trajetória");
         menuItem3D.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {
                 try 
                 {
                     TrajectoryPatternView tpv = new TrajectoryPatternView();
                     tpv.execute();
                 } 
                 catch (Exception ex) 
                 {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
            }
         });
         
         
         MenuItem menuItem3E = new MenuItem("Perguntas e Respostas");
         menuItem3E.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {
                 try 
                 {
                     QuestionsView q = new QuestionsView();
                     q.execute();
                 } 
                 catch (Exception ex) 
                 {
                     Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
            }
         });
         
         
         //MENU 3
         Menu menu5 = new Menu("Comportamento");
         
         MenuItem menuItem5A = new MenuItem("Treinar classificador");
         menuItem5A.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {
                try {                
                    new ActivityRecognitionView().execute();
                } catch (Exception ex) {
                    Logger.getLogger(ToolBar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
         });
         
      
         //MENU 4
         Menu menu4 = new Menu("Ajuda");
         MenuItem menuItem4A = new MenuItem("Sobre o Sistema");
         menuItem4A.setOnAction(new EventHandler<ActionEvent>() 
         {
            @Override public void handle(ActionEvent e) 
            {
                About about = new About();
                about.execute();
            }
         });
        
         menu1.getItems().add(menuItem1A);
         menu1.getItems().add(menuItem1B);
         menu1.getItems().add(menuItem1C);
         
         menu2.getItems().add(menuItem2A);
         menu2.getItems().add(menuItem2B);
         menu2.getItems().add(menuItem2C);
         menu2.getItems().add(menuItem2D);
         menu2.getItems().add(menuItem2E);
         
         menu3.getItems().add(menuItem3A);
         menu3.getItems().add(menuItem3B);
         menu3.getItems().add(menuItem3C);
         menu3.getItems().add(menuItem3D); 
         menu3.getItems().add(menuItem3E);
         
         menu5.getItems().add(menuItem5A);
         
         menu4.getItems().add(menuItem4A);
         menuBar.getMenus().add(menu1);
         menuBar.getMenus().add(menu2);
         menuBar.getMenus().add(menu3);
         menuBar.getMenus().add(menu5);
         menuBar.getMenus().add(menu4);
    }
    
    /**Retorna o menu criado pela classe. */
    public MenuBar getMenu()
    {
        return menuBar;
    }
   
}
