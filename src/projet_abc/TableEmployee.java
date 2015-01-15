package projet_abc;

import java.sql.*;
import javax.swing.*;
import java.io.File;
import javax.swing.table.TableModel;


public class TableEmployee{

  

   
    
    Connection conn=null;
    
    public static Connection ConnecrDb(String server_name, String data_base_name, String user_name, String password) {
        
        try{
         String url = "jdbc:sqlserver://"+server_name+";databaseName="+data_base_name+";user="+user_name+";password="+password+"";
           
           
           Connection con = DriverManager.getConnection(url);
           
         return con;
                    
         }
            catch(Exception e)
            {
                
                JOptionPane.showMessageDialog(null,e);
                return null;
            }
       

    }
    
}
