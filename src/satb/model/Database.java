package satb.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**Classe que gera o acesso ao banco de dados.
* @author Marcel Tolentino Pinheiro de Oliveira.
*/
public final class Database 
{
    //Atributos
//  private static String url = "jdbc:sqlite:C:\\db\\satb.db";
    private static String url = "jdbc:postgresql://localhost:5432/mestrado";
    private static String user = "postgres";  
    private static String password = "123456"; 
    private Connection conn = null;
    private static Database instance = null;
           
    /**Construtor que abre a conexão com a base.*/
    public Database() throws Exception
    {
        openConnection();
    }
       
    
    /**Inicia a conexao com o banco.*/
    public Connection getConnection() throws Exception 
    {
        return conn;
    }
     
    
    /**Método que abre uma conexão com o banco de dados.*/   
    public void openConnection() throws ClassNotFoundException
    {
            try
            {
            //  Class.forName("org.sqlite.JDBC");
                Class.forName("org.postgresql.Driver");
                conn = DriverManager.getConnection(url, user, password);
            }
            catch (SQLException sqle) 
            {
                
                System.out.println("SQLException em Database.java "+ sqle.getMessage());
            }
    }
            
       
    public static Database getInstance() throws Exception 
    {
            if (instance == null) 
            {
               instance = new Database();
            }
            return instance;
    }
  
        
    /**Método utilizado para fechar a conexão com o banco de dados.*/
    public void closeConnection() 
    {
         try 
         {
                if (conn != null)
                {
                    conn.close();
                }
         } 
         catch (SQLException e) 
         {
                System.out.println("Erro ao fechar a conexao [" + e.getMessage()+ "]");
         }
    }
    
}
