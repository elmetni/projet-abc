/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package projet_abc;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;

import com.google.api.services.tasks.TasksScopes;
import com.google.api.services.tasks.model.Task;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.security.GeneralSecurityException;

import java.util.Collections;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.misc.BASE64Decoder;



/** <h2> projet : ineraction entre  Microsoft project server 2010 et Google apps </h2>
* Cette application a pour l’objectif lire les informations concernant des taches dans la base de donnée "interraction_google_apps"
* de les créer, mise à jour, supprimer ou compléter des taches dans des « google calendar » des utilisateurs 
* @author <strong> elmetni hamza </strong>
* @author elmetni.hamza@gmail.com
* @version 1.0
*/

public class Projet_abc {

   
   
   static private String server_name;
   static private String data_base_name;
   static  private String user_name;
   static  private String password;
   static  private String service_account_email;
   static  private String path_secrect_key;
   static  private String application_name;
   static  private String smtp_email;
   static  private String smtp_password;
   static  private int duree_alerte;
   static  private int lignes_historique;
   static  private long rerun;
   static private boolean safe_exit;
   static CountDownLatch countDownLatch = new CountDownLatch(1);
   static private Historique_frame historique_page ;
   
                 
    static public String decrypt(String sValue) throws Exception {
       /** <h3> String decrypt(String )</h3>
        * <Strong> Description :</Strong><p>Cette fonction décrypte un texte en utilisant l'algorithme de chiffrement <a hred="http://fr.wikipedia.org/wiki/Advanced_Encryption_Standard">AES</a></p>
        * @param svalue String : c'est le texte chiffré
        * @return deStr String : le texte Originale 
        */
        
        String AlgoName = "AES";
    
        String keyString = "elmetni hamza no";// La clé de chiffrement (16 letters) 
        
        SecretKeySpec skeySpec = new SecretKeySpec(keyString.getBytes(), AlgoName);
        Cipher cipher = Cipher.getInstance(AlgoName);
        
        BASE64Decoder bASE64Decoder = new BASE64Decoder();
        byte decrytByt[] = bASE64Decoder.decodeBuffer(sValue);
        
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        
        byte decrypted[] = cipher.doFinal(decrytByt);
        
        String deStr = new String(decrypted).trim();
        
        return deStr;
    }
    
    
    
    
    static public void lire_xml() throws ParserConfigurationException, SAXException, IOException, Exception
    {
        /** <h3> void lire_xml()</h3>
        * <Strong> Description :</Strong><p>cette fonction lit les donnée a partir d'un fichier XML (config.xml) 
        * et les décrypte en utilisant la fonction <Strong>decrypt</Strong> puis elle les stocke dans des variables static </p>
        * @see Projet_abc#decrypt
        */
        
        
        File fXmlFile = new File("config.xml"); //lire le fichier XMl
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
 
	
	doc.getDocumentElement().normalize();
 
                NodeList nList = doc.getElementsByTagName("information");// lire le nœud  "information"
                Node nNode = nList.item(0);
 
                Element eElement = (Element) nNode;

              // lire et decrypter les variables de fichier XML et les stocker dans les variables membres
              server_name=new String(decrypt(eElement.getElementsByTagName("variable1").item(0).getTextContent()));
              data_base_name=new String(decrypt(eElement.getElementsByTagName("variable2").item(0).getTextContent()));
              user_name=new String(decrypt(eElement.getElementsByTagName("variable3").item(0).getTextContent()));
              password=new String(decrypt(eElement.getElementsByTagName("variable4").item(0).getTextContent()));
              service_account_email=new String(decrypt(eElement.getElementsByTagName("variable5").item(0).getTextContent()));
              path_secrect_key=new String(decrypt(eElement.getElementsByTagName("variable6").item(0).getTextContent()));
              application_name=new String(decrypt(eElement.getElementsByTagName("variable7").item(0).getTextContent()));
           smtp_email=new String(decrypt(eElement.getElementsByTagName("variable10").item(0).getTextContent()));
              smtp_password=new String(decrypt(eElement.getElementsByTagName("variable11").item(0).getTextContent()));
             String rerun_string =new String(decrypt(eElement.getElementsByTagName("variable12").item(0).getTextContent()));
             float rerun_float=Float.parseFloat(rerun_string);
             
             rerun=(long)(rerun_float*60);
              String duree_alerte_string =new String(decrypt(eElement.getElementsByTagName("variable8").item(0).getTextContent()));
            String lignes_historique_string=new String(decrypt(eElement.getElementsByTagName("variable9").item(0).getTextContent()));
            duree_alerte=Integer.parseInt(duree_alerte_string); 
            lignes_historique=Integer.parseInt(lignes_historique_string); 
            
            
            
    }
    
    
   
    static void tostring()
    {
        System.out.println("server_name :"+server_name+"\n"+
                           "data_base_name :"+data_base_name+"\n"+
                            "user_name :"+user_name+"\n"+
                            "password :"+password+"\n"+
                            "service_account_email :"+service_account_email+"\n"+
                            "path_secrect_key :"+path_secrect_key+"\n"+
                            "duree_alerte :"+duree_alerte+"\n"+
                               "lignes_historique :"+lignes_historique+"\n"+
                            "application_name :"+application_name+"\n Rerun:"+rerun+"\n");
    }
    
    
    static com.google.api.services.tasks.Tasks identification(String utilisateur) throws IOException, GeneralSecurityException
    {
        
         /** <h3> Tasks identification(String )</h3>
        * <Strong> Description :</Strong><p>Cette fonction crée Une requête  HTTP pour identifier un utilisateur dans les serveur de Google apps </p>
        * @param utilisateur String : l'email d'utilisateur
        * @return service  com.google.api.services.tasks.Tasks : c'est la reponse de serveur (du demande d'identification) de type "task" 
        */
        
        //creation d'une nouvelle requête http
         HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
         final  JsonFactory JSON_FACTORY = new JacksonFactory();

         // preparation du demande d'identification
         GoogleCredential credential = new GoogleCredential.Builder()    
        .setTransport(HTTP_TRANSPORT)
        .setJsonFactory(JSON_FACTORY)
        .setServiceAccountId(service_account_email.replaceAll("\\s+",""))
        .setServiceAccountUser(utilisateur.replaceAll("\\s+",""))
        .setServiceAccountScopes(  Collections.singleton(TasksScopes.TASKS))
        .setServiceAccountPrivateKeyFromP12File(new File(path_secrect_key.replaceAll("\\s+","")))
        .build();
   
    // envoyer la demande  d'identification
    credential.refreshToken();
    
    // stocker la reponse de serveur 
    com.google.api.services.tasks.Tasks service = new com.google.api.services.tasks.Tasks.Builder(HTTP_TRANSPORT,
    JSON_FACTORY, credential).setApplicationName(application_name)
            .build();
    
    return service;
        
    }
            
    
    static String inserer_tache(String utilisateur_email, String tache_titre, String tache_remarque , java.sql.Timestamp  tache_datetime) throws IOException, GeneralSecurityException
    {
        /** <Strong> Description :</Strong><p>Cette fonction insére une tache dans "google calendar" d'un utilisateur donnée </p>
        * @param utilisateur_email String  l'email d'utilisateur
        * @param tache_titre String : titre de la tache
        * @param tache_remarque String : une remarque sur la tache
        * @param tache_datetime String : la date de debut de la tache
        * @return result.getId()  String : ID de la tache dans les serveurs de Google apps
        * @see Projet_abc#identification
        */
        
        //indetification 
         com.google.api.services.tasks.Tasks service=identification(utilisateur_email);
         
         //creation d'un variable task qui contient les informations du tache
        Task task = new Task();
        task.setTitle(tache_titre);
        task.setNotes(tache_remarque);
        
        java.sql.Date  tache_date  = new java.sql.Date(tache_datetime.getTime()); 
       
    
     
    DateTime start = new DateTime(tache_date, TimeZone.getTimeZone("UTC"));

        
        task.setDue(start);

         //creation de la tache dans "Google calendar" : @default signifie la list pricipale des taches dans Google calendar
        Task result = service.tasks().insert("@default", task).execute();
        
       
        return result.getId();
    }
    
    static void update_tache(String utilisateur_email ,String id_tache, String tache_titre, String tache_remarque , java.sql.Timestamp tache_datetime) throws IOException, GeneralSecurityException
    {
        /** <Strong> Description :</Strong><p>Cette fonction mit a jour  une tache dans "google calendar" d'un utilisateur donnée </p>
        * @param utilisateur_email String : l'email d'utilisateur
        * @param id_tache String : la clé de la tache dans les server de google apps
        * @param tache_titre String : titre de la tache
        * @param tache_remarque String : une remarque sur la tache
        * @param tache_datetime String : la date de debut de la tache
        * @see Projet_abc#identification
        */
        
        //identification
         com.google.api.services.tasks.Tasks service=identification(utilisateur_email);
         
         //obtenir de serveur de Google apps la tache du clé "id_tache" et stocker dans une variable de type "task"
         Task task = service.tasks().get("@default", id_tache).execute();
         
         java.sql.Date  tache_date  = new java.sql.Date(tache_datetime.getTime()); 
         
         //mettre à jour toutes les informations de la tache
        task.setTitle(tache_titre);
        task.setNotes(tache_remarque);
        
        DateTime start = new DateTime(tache_date, TimeZone.getTimeZone("UTC"));
        task.setDue(start);
        
        //mettre à jour toutes les informations dans google calendar
       Task result = service.tasks().update("@default", task.getId(), task).execute();
    
    }
    
    static void delete_tache(String utilisateur_email ,String id_tache) throws IOException, GeneralSecurityException
    {
        /** <Strong> Description :</Strong><p>Cette fonction supprime  une tache dans "google calendar" d'un utilisateur donnée </p>
        * @param utilisateur_email String : l'email d'utilisateur
        * @param id_tache String : la clé de la tache dans les server de google apps
        * @see Projet_abc#identification
        */
        
        
        //identification : pour email utilisateur_email
         com.google.api.services.tasks.Tasks service=identification(utilisateur_email);
         
         //supprimer Supprimer la tâche qui a la clé id_tache
          service.tasks().delete("@default", id_tache).execute();
        
    
    
    }
    
    
    static void completer_tache(String utilisateur_email ,String id_tache) throws IOException, GeneralSecurityException
    {
        /** <Strong> Description :</Strong><p>Cette fonction marque  une tache comme complet dans "google calendar" d'un utilisateur donnée </p>
        * @param utilisateur_email String : l'email d'utilisateur
        * @param id_tache String : la clé de la tache dans les server de google apps
        * @see Projet_abc#identification
        */
        
        //identification pour l'email utilisateur_email
         com.google.api.services.tasks.Tasks service=identification(utilisateur_email);
         
         
         // obtenir la tache du clé "id_tache"
         Task task = service.tasks().get("@default", id_tache).execute(); 
         
        //marquer comme complete localement
       task.setStatus("completed");
        
       // marquer comment complete dans google calendar 
       Task result = service.tasks().update("@default", task.getId(), task).execute();
    
    }
    
    
    static boolean tache_est_complet(String utilisateur_email ,String id_tache) throws IOException, GeneralSecurityException
    {
        /** <Strong> Description :</Strong><p>Cette fonction returne "true" si la tache est complete et "false" sinon</p>
        * @param utilisateur_email String : l'email d'utilisateur
        * @param id_tache String : la clé de la tache dans les server de google apps
        * @see Projet_abc#identification
        */
        
        String statue=new String();
        
        //identification pour utilisateur_email
        com.google.api.services.tasks.Tasks service=identification(utilisateur_email);
         
        //stocker la tache dans la variable task
         Task task = service.tasks().get("@default", id_tache).execute(); 
         
         
         //stocker la statue du tache dans la variable statue
         statue=task.getStatus();
         
         if("completed".equals(statue))
         return true;
         
         else return false;
    }
    
    static public String readFile(String fileName) throws IOException {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = br.readLine();
                }
                return sb.toString();
            } finally {
                br.close();
            }
        }
    static String message_erreur(int etat_erreur,String email,String title, String message_erreur)
    {
        String e;
        if(email==null)
        {
            e="";
        }
        else
        {
           e="Email: "+email+"\n"; 
        }
         String content =etat_erreur(etat_erreur)+" \n" +e+"Message d'erreur :"+title+"\n\t"+ message_erreur;
        return content ;
    }
    static void erreur_log(int etat_erreur,String email,String title, String message_erreur, int a ) throws IOException
    {
        /** <Strong> Description :</Strong><p>Cette fonction écrit les exceptions obtenues au cas d'une erreur dans un fichier</p>
        * @param message_erreur String 
        */
        String e;
        if(email==null)
        {
            e="";
        }
        else
        {
           e=" Email: "+email+"\n"; 
        }
        if(a==0)
        {
        DateFormat dateFormat_erreur = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
               //la date d'Aujourd’hui  
               java.util.Date date_erreur = new java.util.Date();
              
               //stocker la date d'Aujourd’hui , type d'erreur et message d'erreur dans "content"
               String content = dateFormat_erreur.format(date_erreur)+" \n "+etat_erreur(etat_erreur)+" \n" +e+" \n Message d'erreur :"+title+"\n\t\t\t  "+ message_erreur;
                
               File file = new File("erreur_log.txt");

               if (!file.exists()) {
                                //créer le fichier d'erreur
				file.createNewFile();
			}
               
               
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                
                //ecrire dans le fichier 
                bw.write(content);
                bw.close();
        }
        else
        {
            
            String old=readFile("erreur_log.txt");
            old=old+"\n\n-------------------------------------------------------------------------------- \n";
            DateFormat dateFormat_erreur = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
               //la date d'Aujourd’hui  
               java.util.Date date_erreur = new java.util.Date();
              
               //stocker la date d'Aujourd’hui , type d'erreur et message d'erreur dans "content"
               
               String content = old+dateFormat_erreur.format(date_erreur)+" \n "+etat_erreur(etat_erreur)+" \n" +e+" Message d'erreur :"+title+"\n\t\t\t  "+ message_erreur;
                
               File file = new File("erreur_log.txt");

               if (!file.exists()) {
                                //créer le fichier d'erreur
				file.createNewFile();
			}
               
               
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                
                //ecrire dans le fichier 
                bw.write(content);
                bw.close();
        }
    } 
    
    
    static void historique(Connection con ,int id_tache, int id_person , String operation ) throws SQLException
    {
        
        /** <Strong> Description :</Strong><p>Cette fonction sauvegarde les operations effectué par l'application dans la table historique_taches</p>
        * @param con Connection : connexion de la base de données
        * @param id_tache int la clé de la tache dans la table [interraction_google_aps].[taches] 
        * @param id_person int la clé de la tache dans la table [interraction_google_aps].[person] 
        * @param operation String soit "creation" , "mise a jour" ou "Suppression " 
        */
        
        // si les lignes de la table "historique_taches" a depassé lignes_historique on Supprime 25% des lignes 
        
        // la requete pour compter les lignes du "historique_taches"
       String requet_count = " Select count(*) from historique_taches";
       
       // la requete pour supprimer les premiers N lignes de la table "historique_taches" "
       String requet0 = "DELETE TOP (?) from historique_taches" ;
       
       Statement stmt_count = con.createStatement();
       ResultSet rs_count = stmt_count.executeQuery(requet_count);
       rs_count.next();
       int number_lignes=rs_count.getInt(1);
       
       // si le nombre des lignes a depassé "lignes_historique" on execute la requete pour supprimer 25% des lignes
       if(number_lignes>=lignes_historique)
       {
            int ligne_delete=lignes_historique/4;
           PreparedStatement stmt0 = con.prepareStatement(requet0);
            stmt0.setInt(1, ligne_delete);
            stmt0.executeUpdate();
       }
       
       
        
       
       // selectinner tous les informations concernant la tache du clé "id_tache" et personnne du clé "id_person"
       String requet1 = "SELECT pr.projet_nom , "
                                            + "t.titre_tache ,"
                                            + "t.date_debut_tache ,"
                                            + "t.date_fin_tache ,"
                                            + "t.duree_tache ,"
                                            + "p.person_email ,"
                                            + "p.person_nom ,"
                                            + "c.chef_projet_email ,"
                                            + "c.chef_projet_nom "
                                         
                                            + " from taches t ,chef_projet c , person p , projet pr ,tache_person tp "
                                            + " where c.id_chef_projet=pr.id_chef_projet AND pr.id_projet=t.id_projet AND t.id_tache=tp.id_tache "
                                             + " AND tp.id_person=p.id_person AND p.id_person=? AND t.id_tache= ? ";
        
       String requet2 = "insert into historique_taches (historique_date,operations,projet_nom,titre_tache ,date_debut_tache ,date_fin_tache  ,duree_tache ,"
                            + " person_email , person_nom  , "
                            + "chef_projet_email , chef_projet_nom )" 
                          + "values(?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = con.prepareStatement(requet1);
        stmt.setInt(1, id_person);
        stmt.setInt(2, id_tache);
        ResultSet rs= stmt.executeQuery();  	
	rs.next();
          
        //stocker les resultats de la requete 
        String projet_nom = rs.getString(1) ;
        String titre_tache = rs.getString(2) ;
        java.sql.Timestamp date_debut_tache = rs.getTimestamp(3) ;
        java.sql.Timestamp date_fin_tache = rs.getTimestamp(4) ;
      
        int duree_tache = rs.getInt(5) ;
        String person_email = rs.getString(6) ;
        String person_nom = rs.getString(7) ;
      
     
        String chef_projet_email = rs.getString(8) ;
        String chef_projet_nom = rs.getString(9) ;
       
        
        PreparedStatement stmt2 = con.prepareStatement(requet2);
        stmt2.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
        stmt2.setString(2, operation);
        stmt2.setString(3, projet_nom);
        stmt2.setString(4, titre_tache);
        stmt2.setTimestamp(5, date_debut_tache);
        stmt2.setTimestamp(6, date_fin_tache);
        stmt2.setString(7, duree_tache/600+" Heures");
        stmt2.setString(8, person_email);
        stmt2.setString(9, person_nom);
       
        stmt2.setString(10, chef_projet_email);
        stmt2.setString(11, chef_projet_nom);
        
        
        //insertion des données dans la table "historique_taches"
        stmt2.executeUpdate();
        
        
        stmt.close();
        stmt2.close();
        stmt_count.close();
        
        
    }
    
   static public String etat_erreur(int a)
    {
        if(a==-1)
            return "Erreur N°:"+a+"\n Etape : masquer l'application dans la barre système de barre des tâches";
        
        if(a==0)
            return "Erreur N°:"+a+"\n Etape : initialisation "
                    + "\n Opération : Déchiffrement les donnée de fichier \"config.xml\" \n";
        
        else if(a==1)
            return "Erreur N°:"+a+"\n Etape : initialisation "
                    + "\n Opération : connexion avec base de données  \n";
        
        else if(a==2)
            return "Erreur N°:"+a+"\n Etape : Execution des curseurs  "
                    + "\n Opération : Execution du curseur d'insertion des tâches et des assignements   \n";
        
        else if(a==3)
            return "Erreur N°:"+a+"\n Etape : Execution des curseurs  "
                    + "\n Opération : Execution du curseur de la  suppression  des tâches   \n";
       
        else if(a==4)
            return "Erreur N°:"+a+"\n Etape : Execution des curseurs  "
                    + "\n Opération : Execution du curseur de la  mise a jour  des tâches  \n";
        
        else if(a==5)
            return "Erreur N°:"+a+"\n Etape : Execution des curseurs  "
                    + "\n Opération : Execution du curseur de la  suppression   des assignements   \n";
            
            else if(a==50)
            return "Erreur N°:"+a+"\n Etape : Execution des curseurs  "
                    + "\n Opération :  Execution du curseur de la  mise a jour des resources  \n";
        
        else if(a==6)
            return "Erreur N°:"+a+"\n Etape : Execution des curseurs  "
                    + "\n Opération : Execution du curseur de la  complétude  des tâches en Google apps \n";
        
        else if(a==7)
            return "Erreur N°:"+a+"\n Etape : Execution des curseurs  "
                    + "\n Opération : Execution du curseur de la  complétude  des tâches en Ms projet \n";
        
        else if(a==8)
            return "Erreur N°:"+a+"\n Etape : création des tâches  "
                    + "\n Opération : Lancement de la requête principale \n";
        
        else if(a==9)
            return "Erreur N°:"+a+"\n Etape : création des tâches  "
                    + "\n le stockage de resultats de la   requête \n";
        
        else if(a==10)
            return "Erreur N°:"+a+"\n Etape : création des tâches  "
                    + "\n Opération : insertion des tâches en Google Agenda \n";
        
        
        else if(a==11)
            return "Erreur N°:"+a+"\n Etape : création des tâches  "
                    + "\n Opération : insertion des tâches en base de donnée[tache_person] \n";
        
        else if(a==12)
            return "Erreur N°:"+a+"\n Etape : création des tâches  "
                    + "\n Opération : Historisation[historique_taches]  \n";
        
        else if(a==13)
            return "Erreur N°:"+a+"\n Etape : création des tâches  "
                    + "\n Opération : vidage du cache[operation_tache_person] \n";
        
        else if(a==14)
            return "Erreur N°:"+a+"\n Etape : La suppression des tâches   "
                    + "\n Opération : Lancement de la requête principale \n";
        
        else if(a==15)
            return "Erreur N°:"+a+"\n Etape : La suppression des tâches   "
                    + "\n Opération : le stockage de resultats du  requête \n";
        
        else if(a==16)
            return "Erreur N°:"+a+"\n Etape : La suppression des tâches   "
                    + "\n Opération : Suppression des tâches en Google Agenda \n";
        
        else if(a==17)
            return "Erreur N°:"+a+"\n Etape : La suppression des tâches   "
                    + "\n Opération : Historisation[historique_taches] \n";
        
        else if(a==18)
            return "Erreur N°:"+a+"\n Etape : La suppression des tâches   "
                    + "\n Opération : Suppression des tâches de la base de donnée[taches] \n";
        
        else if(a==19)
            return "Erreur N°:"+a+"\n Etape : La suppression des tâches   "
                    + "\n Opération : Suppression des tâches de la base de donnée[operation_tache] \n";
        
        else if(a==20)
            return "Erreur N°:"+a+"\n Etape : La suppression des tâches   "
                    + "\n Opération : Suppression des tâches de la base de donnée[operation_tache_person] \n";
        
        else if(a==21)
            return "Erreur N°:"+a+"\n Etape : La suppression des tâches   "
                    + "\n Opération : Suppression des tâches de la base de donnée[tache_person] \n";
        
        else if(a==22)
            return "Erreur N°:"+a+"\n Etape : La suppression des assignements  "
                    + "\n Opération : Lancement de la requête principale \n";
        
        else if(a==23)
            return "Erreur N°:"+a+"\n Etape : La suppression des assignements  "
                    + "\n Opération : le stockage de resultats du  requête \n";
        
        else if(a==24)
            return " Etape : La suppression des assignements  "
                    + "\n Opération : selectionement [id_tache_google] du base de données \n";
        
        else if(a==25)
            return "Erreur N°:"+a+"\n Etape : La suppression des assignements  "
                    + "\n Opération : Suppression des tâches en Google Agenda \n";
        
        else if(a==26)
            return "Erreur N°:"+a+"\n Etape : La suppression des assignements  "
                    + "\n Opération : Historisation[historique_taches] \n";
        
        else if(a==27)
            return "Erreur N°:"+a+"\n Etape : La suppression des assignements  "
                    + "\n Opération : vidage du cache[operation_tache_person] \n";
        
        else if(a==28)
            return "Erreur N°:"+a+"\n Etape : La suppression des assignements  "
                    + "\n Opération : Suppression des assignements du base de donnée[operation_tache_person]  \n";
        
        else if(a==29)
            return "Erreur N°:"+a+"\n Etape : La mise a jour des tâches  "
                    + "\n Opération : Lancement de la requête principale \n";
        
        else if(a==30)
            return "Erreur N°:"+a+"\n Etape : La mise a jour des tâches  "
                    + "\n Opération : le stockage de resultats du  requête \n";
        
        else if(a==31)
            return "Erreur N°:"+a+"\n Etape : La mise a jour des tâches  "
                    + "\n Opération : La mise a jour des tâches en Google Agenda \n";
        
        else if(a==32)
            return "Erreur N°:"+a+"\n Etape : La mise a jour des tâches  "
                    + "\n Opération : La mise a jour des tâches en Google Agenda \n";
        
        else if(a==33)
            return "Erreur N°:"+a+"\n Etape : La mise a jour des tâches  "
                    + "\n Opération : vidage du cache[operation_tache_person] \n";
        
        else if(a==34)
            return "Erreur N°:"+a+"\n Etape : La complétude des tâches   "
                    + "\n Opération : Lancement de la requête complétude des Taches en Ms-project \n";
        
        else if(a==35)
            return "Erreur N°:"+a+"\n Etape : La complétude des tâches   "
                    + "\n Opération : le stockage de resultats du  requête \n";
        
        else if(a==36)
            return "Erreur N°:"+a+"\n Etape : La complétude des tâches   "
                    + "\n Opération : Verification de la complétude du tache en Google Agenda \n";
        
        else if(a==37)
            return "Erreur N°:"+a+"\n Etape : La complétude des tâches   "
                    + "\n Opération : Mise a jour les taches en base de données \n";
        
        else if(a==38)
            return "Erreur N°:"+a+"\n Etape : La complétude des tâches   "
                    + "\n Opération :  Historisation[historique_taches]  \n";
        
        else if(a==39)
            return "Erreur N°:"+a+"\n Etape : La complétude des tâches   "
                    + "\n Opération : Lancement de la requête complétude des Taches en Google Agenda \n";
        
        else if(a==40)
            return "Erreur N°:"+a+"\n Etape : La complétude des tâches   "
                    + "\n Opération : le stockage de resultats du  requête \n";
        
        else if(a==41)
            return "Erreur N°:"+a+"\n Etape : La complétude des tâches   "
                    + "\n Opération : Historisation[historique_taches]  \n";
        
        else if(a==42)
            return "Erreur N°:"+a+"\n Etape : La complétude des tâches   "
                    + "\n Opération : idage du cache[operation_tache_person] \n";
        
        else if(a==43)
            return "Erreur N°:"+a+"\n Etape : La complétude des tâches   "
                    + "\n Opération : Enregistrement des Modification Dans la base de données[tache_person] \n";
        
         else if(a==44)
            return "Erreur N°:"+a+"\n Etape : L'envoie des alerts     "
                    + "\n Opération : préparation des variables de connexion SMTP \n";
        
         else if(a==45)
            return "Erreur N°:"+a+"\n Etape : L'envoie des alerts     "
                    + "\n Opération : le stockage de resultats du  requête \n";
        
         else if(a==46)
            return "Erreur N°:"+a+"\n Etape : L'envoie des alerts     "
                    + "\n Opération : L'envoie les messages des alertes  \n";
        
         else if(a==47)
            return "Erreur N°:"+a+"\n Etape : L'envoie des alerts     "
                    + "\n Opération : Enregistrement des Modification Dans la base de données[taches]  \n";
        
        
         else if(a==48)
            return "Erreur N°:"+a+"\n Etape : L'envoie des alerts    "
                    + "\n Opération : Historisation[historique_taches]  \n";
        
        
        
        
        
        
        
        
        
       return "";
        
    }
  
   static public String calcul_time(long  debut , long fin)
   {
      long a = fin-debut;
       if(a<60*1000)
       return (long)a/1000+" Sec";
       
       else if(a<60*1000*60)
           return (long)a/(1000*60)+" Min";
       
       else if(a<60*1000*60*60)
           return (long)a/(1000*60*60)+" H";
       else return null;
   }
   
  static public void change_statue_waiting(JTextField text,JProgressBar bar , JLabel image)
  {
        if(bar.isVisible())
        {
            bar.setVisible(false);
        }
        
        if(text.isVisible()==false)
        {
            text.setVisible(true);
        }
        
        if(image.isVisible())
        {
            image.setVisible(false);
        }
        
        Font font = new Font("Verdana", Font.ITALIC, 16);
        text.setFont(font);
        text.setForeground(Color.lightGray);
        text.setText("en attente d'exécution");
        text.setLocation(280, text.getY());
        
  }
  
  static public void change_statue_progress(JTextField text,JProgressBar bar , JLabel image , int value , String operation , int number_operation)
  {
        if(bar.isVisible()==false)
        {
            bar.setVisible(true);
        }
        
        if(text.isVisible())
        {
            text.setVisible(false);
        }
        
        if(image.isVisible())
        {
            image.setVisible(false);
        }
        if(text.isEditable()==true)
        {
            text.setEditable(false);
        }
        
        if(number_operation!=-1)
        {
            operation=operation+" ("+number_operation+")";
        }
        
        bar.setString(operation);
        bar.setValue(value);
  }
  
  
  static public void change_statue_done(JTextField text,JProgressBar bar , JLabel image, long a , long b ) throws IOException
  {
        if(bar.isVisible())
        {
            bar.setVisible(false);
        }
        
        if(text.isVisible()==false)
        {
            text.setVisible(true);
        }
        
        if(image.isVisible()==false)
        {
            image.setVisible(true);
        }
        
        if(text.isEditable()==true)
        {
            text.setEditable(false);
        }
        
        text.setBackground(Color.white);
        Font font = new Font("Verdana", Font.BOLD, 12);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");               
        java.util.Date date_now = new java.util.Date();
       
        image.setIcon(new ImageIcon(Projet_abc.class.getResource("/done.png")));
        text.setFont(font);
        text.setForeground(Color.decode("#1FB302"));
        text.setLocation(285, text.getY());
        text.setText("Terminé ["+dateFormat.format(date_now)+"]"+" ("+calcul_time(a,b)+")");
  }
  
   static public void change_statue_warning(JTextField text,JProgressBar bar , JLabel image, long a , long b ) throws IOException
  {
        if(bar.isVisible())
        {
            bar.setVisible(false);
        }
        
        if(text.isVisible()==false)
        {
            text.setVisible(true);
        }
        
        if(image.isVisible()==false)
        {
            image.setVisible(true);
        }
        
        if(text.isEditable()==true)
        {
            text.setEditable(false);
        }
        
        text.setBackground(Color.white);
        Font font = new Font("Verdana", Font.BOLD, 12);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");               
        java.util.Date date_now = new java.util.Date();
       
        image.setIcon(new ImageIcon(Projet_abc.class.getResource("/warning.png")));
        text.setFont(font);
        text.setForeground(Color.decode("#ff7f24"));
        text.setLocation(285, text.getY());
        text.setText("Terminé ["+dateFormat.format(date_now)+"]"+" ("+calcul_time(a,b)+")");
        text.setToolTipText("Terminé avec des Erreurs");
        image.setToolTipText("Terminé avec des Erreurs");
  }
   
  static public void change_statue_erreur(JTextField text,JProgressBar bar , JLabel image, int etat_erreur ) throws IOException
  {
        if(bar.isVisible())
        {
            bar.setVisible(false);
        }
        
        if(text.isVisible()==false)
        {
            text.setVisible(true);
        }
        
        if(image.isVisible()==false)
        {
            image.setVisible(true);
        }
        if(text.isEditable()==true)
        {
            text.setEditable(false);
        }
        
        image.setIcon(new ImageIcon(Projet_abc.class.getResource("/not_done.png")));
        text.setBackground(Color.white);
        Font font = new Font("Verdana", Font.BOLD, 12);
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");               
        java.util.Date date_now = new java.util.Date();
        
        text.setFont(font);
        text.setForeground(Color.decode("#cc0000"));
        text.setLocation(285, text.getY());
        text.setText("  Erreur N°:"+etat_erreur+" ["+dateFormat.format(date_now)+"]"+"");
  }
  
  
  
    
    public static void main(String[] args) throws IOException   {
        
           
           safe_exit=true;
            final JFrame f = new JFrame("Interaction: Google Apps et Ms project");
             f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
             
             f.setResizable(false);
            f.setLocation(100, 50);
              Image image = Toolkit.getDefaultToolkit().getImage(Projet_abc.class.getResource("/icon2.png"));
            final TrayIcon trayIcon = new TrayIcon(image);
            trayIcon.setToolTip("Interaction: Google Apps et Ms project");
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException ex) {
                JOptionPane.showMessageDialog(f, message_erreur(-1,null," AWTException",ex.getMessage()),"Erreur N°:-1",JOptionPane.ERROR_MESSAGE);
                erreur_log(-1,null," AWTException",ex.getMessage(), 1 );
            }
    

               
                trayIcon.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        trayIcon.displayMessage("Bonjour", "vous m'aviez afficher à nouveau \n" +
                                                "Pour quitter l'application cliquez sur \"Quitter\"", TrayIcon.MessageType.INFO);
                        f.setVisible(true);
                    }
                });

            
            Toolkit kit = Toolkit.getDefaultToolkit();
           
            f.setIconImage(kit.getImage(Projet_abc.class.getResource("/icon.png")));
           
            Container content = f.getContentPane();
            f.setSize(600, 585);
            f.setVisible(true);         
            content.setLayout(null);
            content.setBackground(Color.white);
            
            
            
            JButton quitButton = new JButton("Quitter");
            
            
            quitButton.setLocation(490, 50);
            quitButton.setSize(100, 40);
            quitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    int replay;
                    replay=JOptionPane.showConfirmDialog(f,"êtes-vous sûr ?" ,"Quitter ",JOptionPane.YES_NO_OPTION);
                    if(replay==JOptionPane.YES_OPTION)
                    {
                   if(safe_exit==true)
                        {
                            
                            
                            
                            System.exit(0);
                         
                        }
                   else 
                            {
                                safe_exit=true;
                                JOptionPane.showMessageDialog(f, "L'application va se fermer automatiquement une fois qu'il atteint un point sûr");

                            
                             }
                     }
            
                }
               });
            content.add(quitButton);
            
            
            
            
            JButton view_error = new JButton("Erreur!");
            
            view_error.setLocation(380, 470);
            view_error.setBackground(Color.decode("#FF3300"));
            view_error.setSize(70, 40);
            view_error.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            view_error.setBorder(BorderFactory.createEtchedBorder(1));
           view_error.setForeground(Color.white);
           view_error.setVisible(false);
            view_error.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                   
                    try {                    
                        java.awt.Desktop.getDesktop().open(new File("erreur_log.txt"));
                    } catch (IOException ex) {
                        Logger.getLogger(Projet_abc.class.getName()).log(Level.SEVERE, null, ex);
                    }
            
                }
               });
            content.add(view_error);
            
            
            
            
            JButton rerun_now = new JButton("Re-exécuter");
           
            rerun_now.setFont(new Font("Tahoma", Font.BOLD, 13));
            rerun_now.setLocation(465, 470);
            rerun_now.setBorder(javax.swing.BorderFactory.createEtchedBorder());
            rerun_now.setSize(120, 80);
            rerun_now.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
           
           rerun_now.setVisible(true);
           
           rerun_now.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                  
                    
                   countDownLatch.countDown();
                   
            
                }
               });
           
            content.add(rerun_now);
            rerun_now.setEnabled(false);
            
            JButton help = new JButton();
            help.setText("Aide?");
            help.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            help.setLocation(415, 50);
            help.setSize(70, 40);
            help.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    try {                    
                        java.awt.Desktop.getDesktop().open(new File("setting\\index.html"));
                    } catch (IOException ex) {
                        Logger.getLogger(Projet_abc.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                   
                }
            
                
               });
            content.add(help);
            
            
            JButton history = new JButton();
            history.setText("Historique");
            history.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            history.setLocation(310, 50);
            history.setSize(95, 40);
            history.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                  
                    if(historique_page==null)
                    {
                    historique_page= new Historique_frame(server_name,data_base_name,user_name,password);
                    historique_page.setVisible(true);
                    }
                    else if(historique_page.isVisible()==false)
                    {
                        historique_page.setVisible(true);
                    }
                   
                }
            
                
               });
            
            content.add(history);
            history.setVisible(true);
            
            String color_a="#212902";
            String color_b="#9ac102";
            String color_c="#2c3a42";
            
            JProgressBar bar_curseur = new JProgressBar();
            JProgressBar bar_insertion = new JProgressBar();
            JProgressBar bar_delete_tache = new JProgressBar();
            JProgressBar bar_delete_tache_person = new JProgressBar();
            JProgressBar bar_update = new JProgressBar();
            JProgressBar bar_complet = new JProgressBar();
            JProgressBar bar_alert = new JProgressBar();
            
            
            JTextField header = new JTextField();
                    
            header.setHorizontalAlignment(JTextField.CENTER);
            JTextField text_plan_execution = new JTextField();
            JTextField text_curseur = new JTextField();
            JTextField text_insertion_tache = new JTextField();
            JTextField text_delete_tache = new JTextField();
            JTextField text_delete_tache_person = new JTextField();
            JTextField text_update = new JTextField();
            JTextField text_complet = new JTextField();
            JTextField text_last_use = new JTextField();
            JTextField text_next_use = new JTextField();
             JTextField text_alert = new JTextField();
            
            
            Font font = new Font("Verdana", Font.BOLD, 15);
            header.setFont(font);
           
            content.add(header);
             header.setEditable(false);
             header.setLocation(0, 0);
             header.setSize(600, 40);
             header.setBackground(Color.decode(color_b));
             header.setForeground(Color.white);
             
             font = new Font("Tahoma", Font.BOLD, 13);
             text_last_use.setFont(font);
             text_last_use.setHorizontalAlignment(JTextField.LEFT);
             content.add(text_last_use);
             text_last_use.setEditable(false);
             text_last_use.setLocation(0, 470);
             text_last_use.setSize(450, 40);
             text_last_use.setBackground(Color.decode(color_a));
             text_last_use.setForeground(Color.white);
             text_last_use.setBorder(null);
             
             
             
             text_next_use.setFont(font);
             text_next_use.setHorizontalAlignment(JTextField.LEFT);
             content.add(text_next_use);
             text_next_use.setEditable(false);
             text_next_use.setLocation(0, 510);
             text_next_use.setSize(450, 40);
             text_next_use.setBorder(null);
             text_next_use.setBackground(Color.decode(color_a));
             text_next_use.setForeground(Color.white);
             
             
             
             font = new Font("Verdana", Font.BOLD, 16);
             
             content.add(text_plan_execution);
             text_plan_execution.setEditable(false);
             text_plan_execution.setText("Plan d'exécution :");
             text_plan_execution.setFont(font);
             text_plan_execution.setBackground(Color.white);
             text_plan_execution.setLocation(20, 50);
             text_plan_execution.setSize(200, 30);
             text_plan_execution.setForeground(Color.decode(color_a));
             text_plan_execution.setBorder(null);
             
             
             font = new Font("Verdana", Font.BOLD, 12);
             
             content.add(text_curseur);
             text_curseur.setEditable(false);
             text_curseur.setText("L'exécution des curseurs ");
             text_curseur.setHorizontalAlignment(JTextField.CENTER);
             text_curseur.setFont(font);
             text_curseur.setLocation(5, 100);
             text_curseur.setSize(240, 40);
             text_curseur.setBackground(Color.decode(color_c));
              text_curseur.setForeground(Color.white);
             
             
             
            content.add(text_insertion_tache);
             text_insertion_tache.setEditable(false);
             text_insertion_tache.setText("La création des tâches  ");
             text_insertion_tache.setHorizontalAlignment(JTextField.CENTER);
             text_insertion_tache.setFont(font);
             text_insertion_tache.setLocation(5, 150);
             text_insertion_tache.setSize(240, 40);
             text_insertion_tache.setBackground(Color.decode(color_c));
              text_insertion_tache.setForeground(Color.white);
              
              
              content.add(text_delete_tache);
              text_delete_tache.setEditable(false);
             text_delete_tache.setText("La suppression des tâches ");
              text_delete_tache.setHorizontalAlignment(JTextField.CENTER);
             text_delete_tache.setFont(font);
             text_delete_tache.setLocation(5, 200);
             text_delete_tache.setSize(240, 40);
             text_delete_tache.setBackground(Color.decode(color_c));
              text_delete_tache.setForeground(Color.white);
              
              
              content.add(text_delete_tache_person);
              text_delete_tache_person.setEditable(false);
             text_delete_tache_person.setText("La suppression des assignements");
             text_delete_tache_person.setHorizontalAlignment(JTextField.CENTER);
             text_delete_tache_person.setFont(font);
             text_delete_tache_person.setLocation(5, 250);
             text_delete_tache_person.setSize(240, 40);
             text_delete_tache_person.setBackground(Color.decode(color_c));
             text_delete_tache_person.setForeground(Color.white);
              
              
             content.add(text_update);
              text_update.setEditable(false);
             text_update.setText("La mise a jour des tâches   ");
             text_update.setHorizontalAlignment(JTextField.CENTER);
             text_update.setFont(font);
             text_update.setLocation(5, 300);
             text_update.setSize(240, 40);
             text_update.setBackground(Color.decode(color_c));
             text_update.setForeground(Color.white);
              
              
              content.add(text_complet);
                text_complet.setEditable(false);
             text_complet.setText("La complétude des tâches ");
             text_complet.setHorizontalAlignment(JTextField.CENTER);
             text_complet.setFont(font);
             text_complet.setLocation(5, 350);
             text_complet.setSize(240, 40);
             text_complet.setBackground(Color.decode(color_c));
              text_complet.setForeground(Color.white);
             
              
              content.add(text_alert);
               text_alert.setEditable(false);
             text_alert.setText("L'envoie des alertes  ");
              text_alert.setHorizontalAlignment(JTextField.CENTER);
             text_alert.setFont(font);
             text_alert.setLocation(5, 400);
             text_alert.setSize(240, 40);
             text_alert.setBackground(Color.decode(color_c));
              text_alert.setForeground(Color.white);
              
              font = new Font("Tahoma", Font.BOLD, 12);
              content.add(bar_curseur);
               bar_curseur.setLocation(250, 100);
               bar_curseur.setSize(330, 40);
               bar_curseur.setStringPainted(true);
               bar_curseur.setFont(font);
               
               
               
               
               
              content.add(bar_insertion);
               bar_insertion.setLocation(250, 150);
               bar_insertion.setSize(330, 40);
               bar_insertion.setStringPainted(true);
               bar_insertion.setFont(font);
              
              content.add(bar_delete_tache);
               bar_delete_tache.setLocation(250, 200);
               bar_delete_tache.setSize(330, 40);
               bar_delete_tache.setStringPainted(true);
               bar_delete_tache.setFont(font);
              
               
              content.add(bar_delete_tache_person);
               bar_delete_tache_person.setLocation(250, 250);
               bar_delete_tache_person.setSize(330, 40);
               bar_delete_tache_person.setStringPainted(true);
               bar_delete_tache_person.setFont(font);
               
               
              content.add(bar_update);
               bar_update.setLocation(250, 300);
               bar_update.setSize(330, 40);
               bar_update.setStringPainted(true);
               bar_update.setFont(font);
               bar_update.setVisible(false);
              
               
              content.add(bar_complet);
               bar_complet.setLocation(250, 350);
               bar_complet.setSize(330, 40);
               bar_complet.setStringPainted(true);
               bar_complet.setFont(font);
               bar_complet.setVisible(false);
               
               
              content.add(bar_alert);
               bar_alert.setLocation(250, 400);
               bar_alert.setSize(330, 40);
               bar_alert.setStringPainted(true);
               bar_alert.setFont(font);
               bar_alert.setVisible(false);
               
               
              
               
               JLabel done_curseur = new JLabel(new ImageIcon(Projet_abc.class.getResource("/done.png")));
               content.add(done_curseur);
               done_curseur.setLocation(250, 105);
               done_curseur.setSize(30, 30);
               
               
               JLabel done_insertion = new JLabel(new ImageIcon(Projet_abc.class.getResource("/done.png")));
               content.add(done_insertion);
               done_insertion.setLocation(250, 155);
               done_insertion.setSize(30, 30);
                             
               JLabel done_delete_tache = new JLabel(new ImageIcon(Projet_abc.class.getResource("/done.png")));
               content.add(done_delete_tache);
               done_delete_tache.setLocation(250, 205);
               done_delete_tache.setSize(30, 30);
                             
               JLabel done_delete_tache_person = new JLabel(new ImageIcon(Projet_abc.class.getResource("/done.png")));
               content.add(done_delete_tache_person);
               done_delete_tache_person.setLocation(250, 255);
               done_delete_tache_person.setSize(30, 30);
                              
               JLabel done_update = new JLabel(new ImageIcon(Projet_abc.class.getResource("/done.png")));
               content.add(done_update);
               done_update.setLocation(250, 305);
               done_update.setSize(30, 30);
                              
               JLabel done_complet = new JLabel(new ImageIcon(Projet_abc.class.getResource("/done.png")));
               content.add(done_complet);
               done_complet.setLocation(250,355);
               done_complet.setSize(30, 30);
                              
               JLabel done_alert = new JLabel(new ImageIcon(Projet_abc.class.getResource("/done.png")));
               content.add(done_alert);
               done_alert.setLocation(250, 405);
               done_alert.setSize(30, 30);
                 
               JTextField text_statue_curseur = new JTextField();
                JTextField text_statue_insertion_tache = new JTextField();
                JTextField text_statue_delete_tache = new JTextField();
                JTextField text_statue_delete_tache_person = new JTextField();
                JTextField text_statue_update = new JTextField();
                JTextField text_statue_complet = new JTextField();
                JTextField text_statue_alert = new JTextField();
            
                content.add(text_statue_curseur);
                text_statue_curseur.setLocation(340, 105);
                text_statue_curseur.setSize(300, 30);
                text_statue_curseur.setBorder(null);
                
                content.add(text_statue_insertion_tache);
                text_statue_insertion_tache.setLocation(340, 155);
                text_statue_insertion_tache.setSize(300, 30);
                text_statue_insertion_tache.setBorder(null);
                
                content.add(text_statue_delete_tache);
                text_statue_delete_tache.setLocation(340, 205);
                text_statue_delete_tache.setSize(300, 30);
                text_statue_delete_tache.setBorder(null);
                
                content.add(text_statue_delete_tache_person);
                text_statue_delete_tache_person.setLocation(340, 255);
                text_statue_delete_tache_person.setSize(300, 30);
                text_statue_delete_tache_person.setBorder(null);
                
                content.add(text_statue_update);
                text_statue_update.setLocation(340, 305);
                text_statue_update.setSize(300, 30);
                text_statue_update.setBorder(null);
                
                content.add(text_statue_complet);
                text_statue_complet.setLocation(340, 355);
                text_statue_complet.setSize(300, 30);
                text_statue_complet.setBorder(null);
                
                content.add(text_statue_alert);
                text_statue_alert.setLocation(340, 405);
                text_statue_alert.setSize(300, 30);
                text_statue_alert.setBorder(null);
                        
                
                
                
                DateFormat dateFormat_use = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
               
               java.util.Date last_use=null;
               java.util.Date next_use=null;
               
                     
               
               f.setVisible(true);
               
               boolean local_erreur=false;
                boolean is_there_error=false;
                
          while(true)
          {
              rerun_now.setEnabled(false);
              File file = new File("erreur_log.txt");

               if (!file.exists()) {
                                //créer le fichier d'erreur
				file.createNewFile();
			}
               
               
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                
                //ecrire dans le fichier 
                bw.write("");
                bw.close();
                
               try {
                   long start_all=0;
                   long end_all=0;
                   is_there_error=false;
                   
                   int etat_erreur=0;
                 
                       
                   safe_exit=false;
                   if(safe_exit==true)  System.exit(-1);
                   long start=0;
                   long end=0;
                   
                       
                   start_all=System.currentTimeMillis();
                   change_statue_waiting(text_statue_curseur,bar_curseur,done_curseur);
                   change_statue_waiting(text_statue_insertion_tache,bar_insertion,done_insertion);
                   change_statue_waiting(text_statue_delete_tache,bar_delete_tache,done_delete_tache);
                   change_statue_waiting(text_statue_delete_tache_person,bar_delete_tache_person,done_delete_tache_person);
                   change_statue_waiting(text_statue_update,bar_update,done_update);
                   change_statue_waiting(text_statue_complet,bar_complet,done_complet);
                   change_statue_waiting(text_statue_alert,bar_alert,done_alert);
                   
                   File check_config=new File("config.xml");
                    if(check_config.exists()==false)
                    {
                        JOptionPane.showMessageDialog(f, " Le fichier de configuration \"Config.xml\" n'exist pas\n s'il vous plaît générer le fichier avec l'application \"Génération du fichier config.xml\" \n et  placez le dans même dossier que cette application  ","Erreur :config.xml n'exist pas",JOptionPane.ERROR_MESSAGE);
                         System.exit(2);
                    }
                      try {
                          lire_xml() ;
                      } catch (ParserConfigurationException ex) {
                          JOptionPane.showMessageDialog(f, message_erreur(etat_erreur,null," ParserConfigurationException",ex.getMessage()),"Erreur N°:"+etat_erreur,JOptionPane.ERROR_MESSAGE);
                          erreur_log(etat_erreur,null," ParserConfigurationException",ex.getMessage(), 0 );
                          System.exit(3);
                      } catch (SAXException ex) {
                          JOptionPane.showMessageDialog(f, message_erreur(etat_erreur,null," SAXException",ex.getMessage()),"Erreur N°:"+etat_erreur,JOptionPane.ERROR_MESSAGE);
                          erreur_log(etat_erreur,null," SAXException",ex.getMessage(), 0 );
                          System.exit(3);
                      } catch (Exception ex) {
                          JOptionPane.showMessageDialog(f, message_erreur(etat_erreur,null," Exception",ex.getMessage()),"Erreur N°:"+etat_erreur,JOptionPane.ERROR_MESSAGE); 
                          erreur_log(etat_erreur,null," Exception",ex.getMessage(), 0 );
                          System.exit(3);
                      }
                    File check_key=new File(path_secrect_key);
                    if(check_key.exists()==false)
                    {
                        JOptionPane.showMessageDialog(f, "Le Chemin de clé secrète que vous avez entré n'est pas valide \n s'il vous plaît assurez-vous du chemin et Regénérez le fichier \"config.xml\"","Erreur :clé secrète n'exist pas",JOptionPane.ERROR_MESSAGE);
                         System.exit(-12);
                    }
                   
                    
                    String duree_rerun;
                   if(rerun<0)
                   {
                       rerun=120;
                       duree_rerun="2 heures";
                   }
                   
                   else if(rerun<60)
                   {
                        duree_rerun=rerun+ " Min";
                   }
                   
                   else 
                   {
                        duree_rerun=rerun/60+ " Heures";
                   }
                   
                   
                   header.setText("Cette application s'exécute automatiquement chaque "+duree_rerun);
                    if(last_use==null)
                    {
                        text_last_use.setText("   Dernière exécution  : en cours d'exécution pour la première fois ");
                        text_next_use.setText("   L'exécution suivante: "+duree_rerun+" après la fin de celle-ci.");
                        
                    }
                    
                   
                  // lire les variable de connexion et les décrypter a partir de fichier "config.xlm"
                  
                  
                  //connecter a la base de donnée
                  if(safe_exit==true)  System.exit(-1);
                  String url = "jdbc:sqlserver://"+server_name+";databaseName="+data_base_name+";user="+user_name+";password="+password+"";
                  etat_erreur=1;
                  
                  Connection con=null;
                      try {
                          con = DriverManager.getConnection(url);
                      } catch (SQLException ex) {
                          JOptionPane.showMessageDialog(f, message_erreur(etat_erreur,null," SQLException",ex.getMessage()),"Erreur N°:"+etat_erreur,JOptionPane.ERROR_MESSAGE);
                          erreur_log(etat_erreur,null," SQLException",ex.getMessage(), 0 );
                          System.exit(3);
                      }
                  
                  start=System.currentTimeMillis();
                  
                  String curseur_insertion = "EXEC curseur_insertion";
                  String curseur_delete_tache = "EXEC curseur_delete_tache";
                  String curseur_update_tache = "EXEC curseur_update_tache";
                  String curseur_delete_tache_person = "EXEC curseur_delete_tache_person";
                  String curseur_complet_tache = "EXEC curseur_complet_tache";
                  String curseur_complet_tache_ms_projet = "EXEC curseur_complet_tache_ms_projet";
                  String curseur_update_resources = "EXEC curseur_update_resources";
                 
                  try {
                   etat_erreur=2;
                  change_statue_progress(text_statue_curseur,bar_curseur,done_curseur ,15, "Execution du curseur d'insertion des tâches et des assignements " ,-1);
                  
                  PreparedStatement ps_curseur_insertion;
                      
                          ps_curseur_insertion = con.prepareStatement(curseur_insertion);
                          ps_curseur_insertion.executeUpdate();
                      
                  
                  
                  if(safe_exit==true)  System.exit(-1);
                  
                   etat_erreur=3;
                   change_statue_progress(text_statue_curseur,bar_curseur,done_curseur ,30, "Execution du curseur de la  suppression  des tâches " ,-1);
                   PreparedStatement ps_curseur_delete_tache = con.prepareStatement(curseur_delete_tache);
                   ps_curseur_delete_tache.executeUpdate();
                  if(safe_exit==true)  System.exit(-1);
                    etat_erreur=4;
                   change_statue_progress(text_statue_curseur,bar_curseur,done_curseur ,45, "Execution du curseur de la  mise a jour  des tâches" ,-1);
                   PreparedStatement ps_curseur_update_tache = con.prepareStatement(curseur_update_tache);
                  ps_curseur_update_tache.executeUpdate();
                  if(safe_exit==true)  System.exit(-1);
                   etat_erreur=5;
                   change_statue_progress(text_statue_curseur,bar_curseur,done_curseur ,57, "Execution du curseur de la  suppression   des assignements " ,-1);
                   PreparedStatement ps_curseur_delete_tache_person = con.prepareStatement(curseur_delete_tache_person);
                  ps_curseur_delete_tache_person.executeUpdate();
                  
                  if(safe_exit==true)  System.exit(-1);
                   etat_erreur=6;
                   change_statue_progress(text_statue_curseur,bar_curseur,done_curseur ,69, "Execution du curseur de la  complétude  des tâches en Google apps  " ,-1);
                   PreparedStatement ps_curseur_complet_tache = con.prepareStatement(curseur_complet_tache);
                  ps_curseur_complet_tache.executeUpdate();
                  if(safe_exit==true)  System.exit(-1);
                  etat_erreur=7;
                   change_statue_progress(text_statue_curseur,bar_curseur,done_curseur ,80, "Execution du curseur de la  complétude  des tâches en Ms projet" ,-1);
                  PreparedStatement ps_curseur_complet_tache_ms_projet = con.prepareStatement(curseur_complet_tache_ms_projet);
                     ps_curseur_complet_tache_ms_projet.executeUpdate();
                   
                   etat_erreur=50;
                   change_statue_progress(text_statue_curseur,bar_curseur,done_curseur ,95, "Execution du curseur de la  mise a jour des resources " ,-1);
                   PreparedStatement ps_curseur_update_resources = con.prepareStatement(curseur_update_resources);
                  ps_curseur_update_resources.executeUpdate();
                   
                   
                   
                 change_statue_progress(text_statue_curseur,bar_curseur,done_curseur  ,100, " Terminé" ,-1);
                 end=System.currentTimeMillis();
                 change_statue_done(text_statue_curseur,bar_curseur,done_curseur , start , end );
                 if(safe_exit==true)  System.exit(-1);
                   } catch (SQLException ex) {
                          JOptionPane.showMessageDialog(f, message_erreur(etat_erreur,null," SQLException",ex.getMessage()),"Erreur N°:"+etat_erreur,JOptionPane.ERROR_MESSAGE);
                          erreur_log(etat_erreur,null," SQLException",ex.getMessage(), 1 );
                         if(safe_exit==true)  System.exit(-1);
                          change_statue_erreur(text_statue_curseur,bar_curseur, done_curseur, etat_erreur );
                          is_there_error=true;
                          
                      }
                 if(safe_exit==true)  System.exit(-1);   
                  
                  // la premiere opération dans cette application est de créer les taches 
                  
                  //selectionner tous les information des taches ou "operations" dans la table operation_tache_person égale a "c" ( c a d creation )
                 start=System.currentTimeMillis(); 
                  etat_erreur=8;
                  change_statue_progress(text_statue_insertion_tache,bar_insertion,done_insertion ,10, " Lancement de la requête principale " ,-1);
                 String requet_creation_taches = "SELECT pr.projet_nom , "
                                              + "t.titre_tache ,"
                                              + "t.date_debut_tache ,"
                                              + "t.date_fin_tache ,"
                                              + "t.duree_tache ,"
                                              + "p.person_email ,"
                                              + "p.person_nom ,"
                                              + "c.chef_projet_email ,"
                                              + "c.chef_projet_nom ,"
                                              + "pr.projet_description, "
                                              + "t.id_tache ,"
                                              + "p.id_person"
                                              
                                              + " from taches t ,chef_projet c , person p , projet pr ,operation_tache_person tp "
                                              + " where c.id_chef_projet=pr.id_chef_projet AND pr.id_projet=t.id_projet AND t.id_tache=tp.id_tache "
                                               + " AND tp.id_person=p.id_person AND tp.operations='c'";
                  
                 int count =1;
                   try
                   {
                   PreparedStatement stmt_creation_taches = con.prepareStatement(requet_creation_taches);
                   ResultSet rs_creation_taches= stmt_creation_taches.executeQuery();  	
                   count=1;   
                   local_erreur=false;
                   while(rs_creation_taches.next())
                                              {      
                                                   if(safe_exit==true)  System.exit(-1);
                                                  
                                                   etat_erreur=9;
                                                   change_statue_progress(text_statue_insertion_tache,bar_insertion,done_insertion ,15, " le stockage de resultats du  requête" ,count);
                  
                                                           // stocker les resultats de la requet stmt_creation_taches 
                                                          String projet_nom = rs_creation_taches.getString(1) ;
                                                          String titre_tache = rs_creation_taches.getString(2) ;
                                                          java.sql.Timestamp date_debut_tache = rs_creation_taches.getTimestamp(3) ;
                                                          java.sql.Timestamp date_fin_tache = rs_creation_taches.getTimestamp(4) ;

                                                           
                                                          int duree_tache = rs_creation_taches.getInt(5) ;
                                                          String person_email = rs_creation_taches.getString(6) ;
                                                          String person_nom = rs_creation_taches.getString(7) ;
                                                          
                                                          String chef_projet_email = rs_creation_taches.getString(8) ;
                                                          String chef_projet_nom = rs_creation_taches.getString(9) ;
                                                          String projet_description = rs_creation_taches.getString(10) ;
                                                          int id_tache = rs_creation_taches.getInt(11) ;
                                                          int id_person = rs_creation_taches.getInt(12) ;

                                                           
                                                           //calculer la date d'alert ( date d'alert =  date de fin de tache + duree_alerte(jours))
                                                           Calendar cal = Calendar.getInstance();
                                                           cal.setTime(date_fin_tache);
                                                           cal.add(Calendar.DAY_OF_YEAR,duree_alerte);
                                                           cal.set(Calendar.HOUR_OF_DAY, 0);
                                                           cal.set(Calendar.MINUTE, 0);
                                                           cal.set(Calendar.SECOND, 0);
                                                           cal.set(Calendar.MILLISECOND, 0);

                                                           java.sql.Date alter_date = new java.sql.Date(cal.getTimeInMillis());


                                                          //préparer la remarque sure la tache qui sera afficher en Google calendar
                                                          String body_tache="Projet :"+projet_nom+"\n"
                                                                             +" debut :"+date_debut_tache.toString()+"\n"
                                                                             +"fin :"+date_fin_tache.toString()+"\n"
                                                                             +"durée du taches :"+duree_tache/600+" heures"+"\n"
                                                                             +"assigné à :"+person_nom+"\n"
                                                                             +"chef de projet : "+chef_projet_nom+" ("+chef_projet_email+")"+"\n" 
                                                                             +"au cas d'un retard ...une alerte sera envoyée après :"+alter_date.toString()
                                                                             +"Description du projet :"+projet_description+"";
                                                          String id_google_tache;
                                                          try
                                                            {
                                                          etat_erreur=10;
                                                          change_statue_progress(text_statue_insertion_tache,bar_insertion,done_insertion ,30, " insertion des tâches en Google Agenda" ,count);
                                                   
                                                          //inserer la tache en google calendar et stocker leur google ID
                                                          id_google_tache=inserer_tache(person_email,titre_tache,body_tache,date_debut_tache);

                                                           etat_erreur=11;
                                                          change_statue_progress(text_statue_insertion_tache,bar_insertion,done_insertion ,60, " insertion des tâches en base de donnée[tache_person]" ,count);
                  
                                                          //insrer la tache dans la table tache_person
                                                          String requet_insert_tache_person="insert into tache_person(id_tache ,id_person,id_tache_google,est_complet)"
                                                                                           +" values(?,?,?,'false')";
                                                          
                                                           PreparedStatement stmt_insert_tache_person = con.prepareStatement(requet_insert_tache_person);

                                                           stmt_insert_tache_person.setInt(1,  id_tache);
                                                           stmt_insert_tache_person.setInt(2, id_person);
                                                           stmt_insert_tache_person.setString(3, id_google_tache);
                                                           stmt_insert_tache_person.executeUpdate();

                                                           //historisation
                                                            if(lignes_historique!=0)
                                                                      {
                                                                        etat_erreur=12;
                                                                        change_statue_progress(text_statue_insertion_tache,bar_insertion,done_insertion ,70, " Historisation[historique_taches] " ,count);
                                                                        
                                                                          historique(con ,id_tache, id_person ,"creation" );
                                                                          
                                                                      }
                                                            etat_erreur=13;
                                                            change_statue_progress(text_statue_insertion_tache,bar_insertion,done_insertion ,90, " vidage du cache[operation_tache_person] " ,count);
                                                                        
                                                           // supprimer la tache de table "operation_tache_person"
                                                           String requet_delete_operation_tache_person="delete from operation_tache_person where id_tache=? AND id_person =? AND operations='c' ";

                                                           PreparedStatement stmt_delete_operation_tache_person = con.prepareStatement(requet_delete_operation_tache_person);
                                                           stmt_delete_operation_tache_person.setInt(1, id_tache);
                                                           stmt_delete_operation_tache_person.setInt(2, id_person);

                                                           stmt_delete_operation_tache_person.executeUpdate();

                                                           stmt_delete_operation_tache_person.close();
                                                           stmt_insert_tache_person.close();
                                                           count++;
                                                  }
                                                  catch (SQLException ex) {
                                                   
                                                   erreur_log(etat_erreur,person_email," SQLException",ex.getMessage(), 1 );
                                                  if(safe_exit==true)  System.exit(-1);                                         
                                                  local_erreur=true;
                                                   
                                                   is_there_error=true;
                                                   continue;

                                               } catch (GeneralSecurityException ex) {
                                                  erreur_log(etat_erreur,person_email," GeneralSecurityException",ex.getMessage(), 1 );
                                                   if(safe_exit==true)  System.exit(-1);
                                                  local_erreur=true;
                                                   is_there_error=true;
                                                   continue;
                                                  
                                                   
                                               }catch (IOException ex) {
                                                  erreur_log(etat_erreur,person_email," IOException",ex.getMessage(), 1 );
                                                   if(safe_exit==true)  System.exit(-1);
                                                  local_erreur=true;
                                                   is_there_error=true;
                                                   continue;
                                               }
                                                   

                                            }            
               stmt_creation_taches.close();
               change_statue_progress(text_statue_insertion_tache,bar_insertion,done_insertion ,100, " Terminé" ,-1);
               end=System.currentTimeMillis();
               if(local_erreur==false)
               change_statue_done(text_statue_insertion_tache,bar_insertion,done_insertion , start , end );
               else
               change_statue_warning(text_statue_insertion_tache,bar_insertion,done_insertion , start , end ); 
                   }
                   catch (SQLException ex) {
                          JOptionPane.showMessageDialog(f, message_erreur(etat_erreur,null," SQLException",ex.getMessage()),"Erreur N°:"+etat_erreur,JOptionPane.ERROR_MESSAGE);
                          erreur_log(etat_erreur,null," SQLException",ex.getMessage(), 1 );
                          if(safe_exit==true)  System.exit(-1);
                          change_statue_erreur(text_statue_insertion_tache,bar_insertion, done_insertion, etat_erreur );
                          is_there_error=true;
                          
                      }
               if(safe_exit==true)  System.exit(-1);
               
             
                start=System.currentTimeMillis(); 
                etat_erreur=14;
                change_statue_progress(text_statue_delete_tache,bar_delete_tache,done_delete_tache ,10, " Lancement de la requête principale " ,-1);
                            
                      
                      // la 3éme opération dans cette application est de supprimer les taches 
                      String requet_delete_taches="select	t.id_tache ," +
                                                          "p.id_person ," +
                                                          "tp.id_tache_google ," +
                                                          "p.person_email" +
                                                          " from taches t , person p ,tache_person tp ,  operation_tache op" +
                                                          " where   t.id_tache=tp.id_tache AND p.id_person=tp.id_person" +
                                                          " AND t.id_tache=op.id_tache" +
                                                          " AND  op.operation_tache='d';" ;               
                     try
                     {
                     PreparedStatement stmt_delete_taches = con.prepareStatement(requet_delete_taches);
                   ResultSet rs_delete_taches= stmt_delete_taches.executeQuery();  
                    count=1;
                    local_erreur=false;
                      while(rs_delete_taches.next())
                                              {
                                                           if(safe_exit==true)  System.exit(-1);
                                                          etat_erreur=15;
                                                          change_statue_progress(text_statue_delete_tache,bar_delete_tache,done_delete_tache ,20, " le stockage de resultats du  requête" ,count);
                 
                                                          int id_tache = rs_delete_taches.getInt(1) ;
                                                          int id_person = rs_delete_taches.getInt(2) ;
                                                          String id_tache_google = rs_delete_taches.getString(3) ;
                                                           String person_email = rs_delete_taches.getString(4) ;
                                                           
                                                           etat_erreur=16;
                                                          change_statue_progress(text_statue_delete_tache,bar_delete_tache,done_delete_tache ,40, " Suppression des tâches en Google Agenda" ,count);
                 
                                                          try
                                                          {
                                                         delete_tache(person_email,id_tache_google);  
                                                         if(lignes_historique!=0)
                                                          {
                                                              etat_erreur=17;
                                                               change_statue_progress(text_statue_delete_tache,bar_delete_tache,done_delete_tache ,50, " Historisation[historique_taches] " ,count);
                 
                                                              historique(con ,id_tache, id_person ,"suppression" );
                                                          }
                                                         
                                                         String delete_tache_from_taches="delete from taches where id_tache=?";
                                                         String delete_tache_from_ot="delete from operation_tache where id_tache=?";
                                                         String delete_tache_from_opt="delete from operation_tache_person where id_tache=?";
                                                         String delete_tache_from_tp="delete from tache_person where id_tache=?";
                                                         
                                                          
                                                         etat_erreur=18;
                                                         change_statue_progress(text_statue_delete_tache,bar_delete_tache,done_delete_tache ,60, " Suppression des tâches de la base de donnée[taches] " ,count);
                 
                                                             PreparedStatement stmt_delete_tache_from_ot = con.prepareStatement(delete_tache_from_ot);
                                                           stmt_delete_tache_from_ot.setInt(1, id_tache);
                                                            stmt_delete_tache_from_ot.executeUpdate();
                                                           
                                                          etat_erreur=19;
                                                         change_statue_progress(text_statue_delete_tache,bar_delete_tache,done_delete_tache ,70, " Suppression des tâches de la base de donnée[operation_tache] " ,count);
                   
                                                            PreparedStatement stmt_delete_tache_from_opt = con.prepareStatement(delete_tache_from_opt);
                                                           stmt_delete_tache_from_opt.setInt(1, id_tache);
                                                           stmt_delete_tache_from_opt.executeUpdate();
                                                           
                                                            etat_erreur=20;
                                                         change_statue_progress(text_statue_delete_tache,bar_delete_tache,done_delete_tache ,80, " Suppression des tâches de la base de donnée[operation_tache_person] " ,count);
                 
                                                            PreparedStatement stmt_delete_tache_from_tp = con.prepareStatement(delete_tache_from_tp);
                                                           stmt_delete_tache_from_tp.setInt(1, id_tache);
                                                           stmt_delete_tache_from_tp.executeUpdate();
                                                           
                                                            etat_erreur=21;
                                                         change_statue_progress(text_statue_delete_tache,bar_delete_tache,done_delete_tache ,90, " Suppression des tâches de la base de donnée[tache_person] " ,count);
                 
                                                           PreparedStatement stmt_delete_tache_from_taches = con.prepareStatement(delete_tache_from_taches);
                                                           stmt_delete_tache_from_taches.setInt(1, id_tache);
                                                            stmt_delete_tache_from_taches.executeUpdate();
                                                          

                                                           stmt_delete_tache_from_ot.close();
                                                           stmt_delete_tache_from_opt.close();
                                                           stmt_delete_tache_from_tp.close();
                                                           stmt_delete_tache_from_taches.close();
                                                           count++;
                                                            }
                                                               catch (SQLException ex) {

                                                                erreur_log(etat_erreur,person_email," SQLException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;

                                                            } catch (GeneralSecurityException ex) {
                                                               erreur_log(etat_erreur,person_email," GeneralSecurityException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;

                                                            }catch (IOException ex) {
                                                               erreur_log(etat_erreur,person_email," IOException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;
                                                            }
                                                  
                                              }
                      stmt_delete_taches.close();
                     change_statue_progress(text_statue_delete_tache,bar_delete_tache,done_delete_tache ,100, " Terminé" ,-1);
                   end=System.currentTimeMillis();
                   if(local_erreur==false)
               change_statue_done(text_statue_delete_tache,bar_delete_tache,done_delete_tache , start , end );
               else
               change_statue_warning(text_statue_delete_tache,bar_delete_tache,done_delete_tache , start , end ); 
                     }catch (SQLException ex) {
                          JOptionPane.showMessageDialog(f, message_erreur(etat_erreur,null," SQLException",ex.getMessage()),"Erreur N°:"+etat_erreur,JOptionPane.ERROR_MESSAGE);
                          erreur_log(etat_erreur,null," SQLException",ex.getMessage(), 1 );
                          if(safe_exit==true)  System.exit(-1);
                          change_statue_erreur(text_statue_delete_tache,bar_delete_tache, done_delete_tache, etat_erreur );
                          is_there_error=true;
                          
                      }
                     if(safe_exit==true)  System.exit(-1);
                       start=System.currentTimeMillis(); 
                       etat_erreur=21;
                       change_statue_progress(text_statue_delete_tache_person,bar_delete_tache_person,done_delete_tache_person ,10, " Lancement de la requête principale " ,-1);
                 
                  
                      String requet_delete_taches_persons="select	t.id_tache ,p.id_person  ,p.person_email   " +
                                                          " from taches t , person p ,  operation_tache_person opt " +
                                                          "  where   t.id_tache=opt.id_tache AND p.id_person=opt.id_person " +
                                                          "  AND  opt.operations='d'; " ;
                      try
                      {
                      PreparedStatement stmt_delete_taches_persons = con.prepareStatement(requet_delete_taches_persons);
                      ResultSet rs_delete_taches_persons= stmt_delete_taches_persons.executeQuery();  	
                      count =1;
                    local_erreur=false;
                      while(rs_delete_taches_persons.next())
                                              {
                                                  if(safe_exit==true)  System.exit(-1);
                                                  etat_erreur=23;
                                                 change_statue_progress(text_statue_delete_tache_person,bar_delete_tache_person,done_delete_tache_person ,20, " le stockage de resultats du  requête" ,count);  
                                                   
                                                          int id_tache = rs_delete_taches_persons.getInt(1) ;
                                                          int id_person = rs_delete_taches_persons.getInt(2) ;
                                                           
                                                           String person_email = rs_delete_taches_persons.getString(3) ;
                                                           etat_erreur=24;
                                                           change_statue_progress(text_statue_delete_tache_person,bar_delete_tache_person,done_delete_tache_person ,30, "selectionement [id_tache_google] du base de données" ,count);
                 
                                                            PreparedStatement stmt_select_id_google = con.prepareStatement("select tp.id_tache_google from tache_person tp  "
                                                                                                                          + "  where tp.id_person=? AND tp.id_tache=?");
                                                           stmt_select_id_google.setInt(1, id_person);
                                                           stmt_select_id_google.setInt(2, id_tache);
                                                           ResultSet rs_select_id_google= stmt_select_id_google.executeQuery();
                                                           rs_select_id_google.next();
                                                            String id_tache_google = rs_select_id_google.getString(1) ;
                                                          etat_erreur=25;
                                                           change_statue_progress(text_statue_delete_tache_person,bar_delete_tache_person,done_delete_tache_person ,50, " Suppression des tâches en Google Agenda " ,count);
                 
                                                         try
                                                         {
                                                         delete_tache(person_email,id_tache_google);  
                                                          
                                                         if(lignes_historique!=0)
                                                              {
                                                                  etat_erreur=26;
                                                                change_statue_progress(text_statue_delete_tache_person,bar_delete_tache_person,done_delete_tache_person ,60, " Historisation[historique_taches]  " ,count);
                 
                                                                   historique(con ,id_tache, id_person ,"suppression" );
                                                              }
                                                         
                                                         
                                                        
                                                         String delete_tache_from_opt="delete from operation_tache_person where id_tache=? AND id_person=?";
                                                         String delete_tache_from_tp="delete from tache_person where id_tache=?  AND id_person=? ";
                                                              
                                                          
                                                           
                                                            etat_erreur=27;
                                                            change_statue_progress(text_statue_delete_tache_person,bar_delete_tache_person,done_delete_tache_person ,70, " vidage du cache[operation_tache_person]" ,count);
                 
                                                            PreparedStatement stmt_delete_tache_from_opt = con.prepareStatement(delete_tache_from_opt);
                                                           stmt_delete_tache_from_opt.setInt(1, id_tache);
                                                           stmt_delete_tache_from_opt.setInt(2, id_person);
                                                           stmt_delete_tache_from_opt.executeUpdate();
                                                           
                                                           etat_erreur=28;
                                                            change_statue_progress(text_statue_delete_tache_person,bar_delete_tache_person,done_delete_tache_person ,80, " Suppression des assignements du base de donnée[tache_person]  " ,count);
                 
                                                            PreparedStatement stmt_delete_tache_from_tp = con.prepareStatement(delete_tache_from_tp);
                                                           stmt_delete_tache_from_tp.setInt(1, id_tache);
                                                           stmt_delete_tache_from_tp.setInt(2, id_person);
                                                           stmt_delete_tache_from_tp.executeUpdate();
                                                       
                                                           stmt_delete_tache_from_opt.close();
                                                           stmt_delete_tache_from_tp.close();
                                                         count++;
                                                         }
                                                               catch (SQLException ex) {

                                                                erreur_log(etat_erreur,person_email," SQLException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;

                                                            } catch (GeneralSecurityException ex) {
                                                               erreur_log(etat_erreur,person_email," GeneralSecurityException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;

                                                            }catch (IOException ex) {
                                                               erreur_log(etat_erreur,person_email," IOException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;
                                                            }
                                                  
                                              }
                        stmt_delete_taches_persons.close();
                        
                            
                        change_statue_progress(text_statue_delete_tache_person,bar_delete_tache_person,done_delete_tache_person,100, " Terminé" ,-1);
                   end=System.currentTimeMillis();
                   if(local_erreur==false)
               change_statue_done(text_statue_delete_tache_person,bar_delete_tache_person,done_delete_tache_person , start , end );
               else
               change_statue_warning(text_statue_delete_tache_person,bar_delete_tache_person,done_delete_tache_person , start , end ); 
                   }catch (SQLException ex) {
                          JOptionPane.showMessageDialog(f, message_erreur(etat_erreur,null," SQLException",ex.getMessage()),"Erreur N°:"+etat_erreur,JOptionPane.ERROR_MESSAGE);
                          erreur_log(etat_erreur,null," SQLException",ex.getMessage(), 1 );
                          if(safe_exit==true)  System.exit(-1);
                          change_statue_erreur(text_statue_delete_tache_person,bar_delete_tache_person, done_delete_tache_person, etat_erreur );
                          is_there_error=true;
                          
                      }
                      if(safe_exit==true)  System.exit(-1);
                    start=System.currentTimeMillis(); 
                       etat_erreur=29;
                       change_statue_progress(text_statue_update,bar_update,done_update ,10, " Lancement de la requête principale " ,-1);
                 
               // la deuxiéme  opération dans cette application est de mettre a jour les taches  
               String requet_update_taches = "SELECT pr.projet_nom , "
                                              + "t.titre_tache ,"
                                              + "t.date_debut_tache ,"
                                              + "t.date_fin_tache ,"
                                              + "t.duree_tache ,"
                                              + "p.person_email ,"
                                              + "p.person_nom ,"
                                              + "c.chef_projet_email ,"
                                              + "c.chef_projet_nom ,"
                                              + "pr.projet_description , "
                                              + "t.id_tache ,"
                                              + "p.id_person,"
                                              + "tp.id_tache_google "
                                              + " from taches t ,chef_projet c , person p , projet pr ,tache_person tp , operation_tache op "
                                              + " where c.id_chef_projet=pr.id_chef_projet AND pr.id_projet=t.id_projet AND t.id_tache=tp.id_tache "
                                               + " AND tp.id_person=p.id_person AND op.operation_tache='u' AND t.id_tache=op.id_tache ";
               try
               {
                   PreparedStatement stmt_update_taches = con.prepareStatement(requet_update_taches);
                   ResultSet rs_update_taches= stmt_update_taches.executeQuery();  
                   count=1;
                    local_erreur=false;
                      while(rs_update_taches.next())
                                              {
                                                       if(safe_exit==true)  System.exit(-1);
                                                       etat_erreur=30;
                                                       change_statue_progress(text_statue_update,bar_update,done_update ,20, " le stockage de resultats du  requête " ,count);
                 
                       
                                                          String projet_nom = rs_update_taches.getString(1) ;
                                                          String titre_tache = rs_update_taches.getString(2) ;
                                                          java.sql.Timestamp date_debut_tache = rs_update_taches.getTimestamp(3) ;
                                                          java.sql.Timestamp date_fin_tache = rs_update_taches.getTimestamp(4) ;

                                                           
                                                          int duree_tache = rs_update_taches.getInt(5) ;
                                                          String person_email = rs_update_taches.getString(6) ;
                                                          String person_nom = rs_update_taches.getString(7) ;
                                                          
                                                          String chef_projet_email = rs_update_taches.getString(8) ;
                                                          String chef_projet_nom = rs_update_taches.getString(9) ;
                                                          String projet_description = rs_update_taches.getString(10) ;
                                                          int id_tache = rs_update_taches.getInt(11) ;
                                                          int id_person = rs_update_taches.getInt(12) ;
                                                          String id_tache_google = rs_update_taches.getString(13) ;

                                                           Calendar cal = Calendar.getInstance();
                                                           cal.setTime(date_fin_tache);
                                                           cal.add(Calendar.DAY_OF_YEAR,duree_alerte);
                                                           cal.set(Calendar.HOUR_OF_DAY, 0);
                                                           cal.set(Calendar.MINUTE, 0);
                                                           cal.set(Calendar.SECOND, 0);
                                                           cal.set(Calendar.MILLISECOND, 0);

                                                           java.sql.Date alter_date = new java.sql.Date(cal.getTimeInMillis());



                                                       String body_tache="Projet :"+projet_nom+"\n"
                                                                             +" debut :"+date_debut_tache.toString()+"\n"
                                                                             +"fin :"+date_fin_tache.toString()+"\n"
                                                                             +"durée du taches :"+duree_tache/600+" heures"+"\n"
                                                                             +"signe à :"+person_nom+"\n"
                                                                             +"chef de projet : "+chef_projet_nom+" ("+chef_projet_email+")"+"\n" 
                                                                             +"au cas d'un retard ...une alerte sera envoyée après :"+alter_date.toString()
                                                                             +"Description du projet :"+projet_description+"";
                                                               
                                                          etat_erreur=31;
                                                          try
                                                          {
                                                       change_statue_progress(text_statue_update,bar_update,done_update ,50, " La mise a jour des tâches en Google Agenda " ,count);
                 
                                                          update_tache(person_email,id_tache_google,titre_tache,body_tache,date_debut_tache);
                                                          
                                                           if(lignes_historique!=0)
                                                          {
                                                              etat_erreur=32;
                                                              change_statue_progress(text_statue_update,bar_update,done_update ,70, " Historisation[historique_taches]  " ,count);
                 
                                                              historique(con ,id_tache, id_person ,"mise a jour" );
                                                          }
                                                            etat_erreur=33;
                                                              change_statue_progress(text_statue_update,bar_update,done_update ,80, " vidage du cache[operation_tache_person]  " ,count);
                 
                                                           String requet_delete_operation_tache="delete from operation_tache where id_tache=? AND operation_tache='u' ";

                                                           PreparedStatement stmt_delete_operation_tache = con.prepareStatement(requet_delete_operation_tache);
                                                           stmt_delete_operation_tache.setInt(1, id_tache);
                                                        

                                                           stmt_delete_operation_tache.executeUpdate();

                                                         
                                                           stmt_delete_operation_tache.close();
                                                           count++;
                                                           }
                                                               catch (SQLException ex) {

                                                                erreur_log(etat_erreur,person_email," SQLException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;

                                                            } catch (GeneralSecurityException ex) {
                                                               erreur_log(etat_erreur,person_email," GeneralSecurityException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;

                                                            }catch (IOException ex) {
                                                               erreur_log(etat_erreur,person_email," IOException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;
                                                            }
                                                  
                                              }
                      rs_update_taches.close();
                      change_statue_progress(text_statue_update,bar_update,done_update,100, " Terminé" ,-1);
                   end=System.currentTimeMillis();
                   if(local_erreur==false)
               change_statue_done(text_statue_update,bar_update,done_update , start , end );
               else
               change_statue_warning(text_statue_update,bar_update,done_update , start , end );    
                   }catch (SQLException ex) {
                          JOptionPane.showMessageDialog(f, message_erreur(etat_erreur,null," SQLException",ex.getMessage()),"Erreur N°:"+etat_erreur,JOptionPane.ERROR_MESSAGE);
                          erreur_log(etat_erreur,null," SQLException",ex.getMessage(), 1 );
                          if(safe_exit==true)  System.exit(-1);
                          change_statue_erreur(text_statue_update,bar_update, done_update, etat_erreur );
                          is_there_error=true;
                          
                      }
                   
                      if(safe_exit==true)  System.exit(-1);
                      
                       start=System.currentTimeMillis(); 
                       etat_erreur=34;
                       change_statue_progress(text_statue_complet,bar_complet,done_complet ,10, " Lancement de la requête complétude des Taches en Ms-project " ,-1);

                        
                        String requet_tache_est_complet="select	t.id_tache ,p.id_person ,tp.id_tache_google ,p.person_email  " +
                                                          " from taches t , person p ,tache_person tp  " +
                                                          "  where   t.id_tache=tp.id_tache AND p.id_person=tp.id_person " +
                                                          " AND  tp.est_complet='false'; " ;
                        try
                        {
                        PreparedStatement stmt_tache_est_complet = con.prepareStatement(requet_tache_est_complet);
                      ResultSet rs_tache_est_complet= stmt_tache_est_complet.executeQuery();  	
                      count=1;
                    local_erreur=false;
                      while(rs_tache_est_complet.next())
                                              {
                                                  if(safe_exit==true)  System.exit(-1);
                                                  etat_erreur=35;
                                                  
                                                  change_statue_progress(text_statue_complet,bar_complet,done_complet ,20, " le stockage de resultats du  requête " ,count);
                                                          int id_tache = rs_tache_est_complet.getInt(1) ;
                                                          int id_person = rs_tache_est_complet.getInt(2) ;
                                                          String id_tache_google = rs_tache_est_complet.getString(3) ;
                                                           String person_email = rs_tache_est_complet.getString(4) ;
                                                  
                                                  etat_erreur=36;
                                                  try
                                                  {
                                                  change_statue_progress(text_statue_complet,bar_complet,done_complet ,30, " Verification de la complétude du tache en Google Agenda " ,count);
                                                           if(tache_est_complet(person_email,id_tache_google))
                                                           {
                                                               etat_erreur=37;
                                                  change_statue_progress(text_statue_complet,bar_complet,done_complet ,40, " Mise a jour les taches en base de données " ,count);
                                                               
                                                               String update_tache_complet="update tache_person "
                                                                                          + " set est_complet='true'"
                                                                                          + " where id_tache=? and id_person=? ;";
                                                               PreparedStatement stmt_tache_complet = con.prepareStatement(update_tache_complet);
                                                               stmt_tache_complet.setInt(1, id_tache);
                                                               stmt_tache_complet.setInt(2, id_person);
                                                               stmt_tache_complet.executeUpdate();
                                                               if(lignes_historique!=0)
                                                                  {
                                                                       etat_erreur=38;
                                                                       change_statue_progress(text_statue_complet,bar_complet,done_complet ,50, " Historisation[historique_taches] " ,count);
                                                              
                                                                      historique(con ,id_tache, id_person ,"complete" );
                                                                  }
                                                               
                                                               stmt_tache_complet.close();
                                                              
                                                               }
                                                              }
                                                               catch (SQLException ex) {

                                                                erreur_log(etat_erreur,person_email," SQLException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;

                                                            } catch (GeneralSecurityException ex) {
                                                               erreur_log(etat_erreur,person_email," GeneralSecurityException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;

                                                            }catch (IOException ex) {
                                                               erreur_log(etat_erreur,person_email," IOException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;
                                                            
                                                               
                                                           }
                                              }
                      stmt_tache_est_complet.close();
                      
                      if(safe_exit==true)  System.exit(-1);
                      
                       etat_erreur=39;
                       change_statue_progress(text_statue_complet,bar_complet,done_complet ,60, " Lancement de la requête complétude des Taches en Google Agenda" ,-1);
                                                              
                       String requet_complet_taches="select	t.id_tache ," +
                                                          "p.id_person ," +
                                                          "tp.id_tache_google ," +
                                                          "p.person_email" +
                                                          " from taches t , person p ,tache_person tp ,  operation_tache op" +
                                                          " where   t.id_tache=tp.id_tache AND p.id_person=tp.id_person" +
                                                          " AND t.id_tache=op.id_tache" +
                                                          " AND  op.operation_tache='com';" ;  
                       PreparedStatement stmt_complet_taches = con.prepareStatement(requet_complet_taches);
                   ResultSet rs_complet_taches= stmt_complet_taches.executeQuery();  	
                      while(rs_complet_taches.next())
                                              {    if(safe_exit==true)  System.exit(-1);
                                                   etat_erreur=40;
                                                   change_statue_progress(text_statue_complet,bar_complet,done_complet ,70, " le stockage de resultats du  requête " ,count);
                        
                                                          int id_tache = rs_complet_taches.getInt(1) ;
                                                          int id_person = rs_complet_taches.getInt(2) ;
                                                          String id_tache_google = rs_complet_taches.getString(3) ;
                                                           String person_email = rs_complet_taches.getString(4) ;
                                                              
                                                  try
                                                  {
                                                         completer_tache(person_email ,id_tache_google) ;  
                                                         if(lignes_historique!=0)
                                                          {
                                                              
                                                               etat_erreur=41;
                                                               change_statue_progress(text_statue_complet,bar_complet,done_complet ,80, " Historisation[historique_taches]  " ,count);
                        
                                                              historique(con ,id_tache, id_person ,"complete" );
                                                          }
                                                         
                                                      
                                                         String delete_tache_from_ot="delete from operation_tache where id_tache=?";
                                                         
                                                         
                                                          
                                                           etat_erreur=42;
                                                               change_statue_progress(text_statue_complet,bar_complet,done_complet ,85, " vidage du cache[operation_tache_person]  " ,count);
                        
                                                             PreparedStatement stmt_delete_tache_from_ot = con.prepareStatement(delete_tache_from_ot);
                                                           stmt_delete_tache_from_ot.setInt(1, id_tache);
                                                            stmt_delete_tache_from_ot.executeUpdate();
                                                           
                                                            stmt_delete_tache_from_ot.close();
                                                            
                                                            etat_erreur=43;
                                                               change_statue_progress(text_statue_complet,bar_complet,done_complet ,90, " Enregistrement des Modification Dans la base de données[tache_person]  " ,count);
                        
                                                            String update_tache_complet="update tache_person "
                                                                                          + " set est_complet='true'"
                                                                                          + " where id_tache=? and id_person=? ;";
                                                               PreparedStatement stmt_tache_complet = con.prepareStatement(update_tache_complet);
                                                               stmt_tache_complet.setInt(1, id_tache);
                                                               stmt_tache_complet.setInt(2, id_person);
                                                               stmt_tache_complet.executeUpdate();
                                                               
                                                               stmt_tache_complet.close();
                                                               etat_erreur=44;
                                                               change_statue_progress(text_statue_complet,bar_complet,done_complet ,95, " Enregistrement des Modification Dans la base de données[taches]  " ,count);
                        
                                                               String update_tache_complet_ms="update taches "
                                                                                          + " set est_complet_ms='true'"
                                                                                          + " where id_tache=? ;";
                                                               PreparedStatement stmt_tache_complet_ms = con.prepareStatement(update_tache_complet_ms);
                                                               stmt_tache_complet_ms.setInt(1, id_tache);
                                                               
                                                               stmt_tache_complet_ms.executeUpdate();
                                                               
                                                               stmt_tache_complet_ms.close();
                                                           
                                                  count++;
                                                               }
                                                               catch (SQLException ex) {

                                                                erreur_log(etat_erreur,person_email," SQLException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                               is_there_error=true;
                                                                continue;

                                                            } catch (GeneralSecurityException ex) {
                                                               erreur_log(etat_erreur,person_email," GeneralSecurityException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                                local_erreur=true;
                                                                is_there_error=true;
                                                                continue;

                                                            }catch (IOException ex) {
                                                               erreur_log(etat_erreur,person_email," IOException",ex.getMessage(), 1 );
                                                                if(safe_exit==true)  System.exit(-1);
                                                               local_erreur=true;
                                                               is_there_error=true;
                                                                continue;
                                                            
                                                               
                                                           }
                                              }
                      stmt_complet_taches.close();
                      change_statue_progress(text_statue_complet,bar_complet,done_complet,100, " Terminé" ,-1);
                   end=System.currentTimeMillis();
                   if(local_erreur==false)
               change_statue_done(text_statue_complet,bar_complet,done_complet , start , end );
               else
               change_statue_warning(text_statue_complet,bar_complet,done_complet , start , end );         
                        }catch (SQLException ex) {
                          JOptionPane.showMessageDialog(f, message_erreur(etat_erreur,null," SQLException",ex.getMessage()),"Erreur N°:"+etat_erreur,JOptionPane.ERROR_MESSAGE);
                          erreur_log(etat_erreur,null," SQLException",ex.getMessage(), 1 );
                         
                          if(safe_exit==true)  System.exit(-1);
                          change_statue_erreur(text_statue_complet,bar_complet, done_complet, etat_erreur );
                          is_there_error=true;
                          
                      }
                        
                        if(safe_exit==true)  System.exit(-1);
                           
                      
                      
                    start=System.currentTimeMillis(); 
                    etat_erreur=44;
                    change_statue_progress(text_statue_alert,bar_alert,done_alert ,10, " préparation des variables de connexion SMTP " ,-1);
                   
                       Properties props = new Properties();
                   props.put("mail.smtp.auth", "true");
                   props.put("mail.smtp.starttls.enable", "true");
                   props.put("mail.smtp.host", "smtp.gmail.com");
                   props.put("mail.smtp.port", "587");
                   
                   etat_erreur=45;
                    change_statue_progress(text_statue_alert,bar_alert,done_alert ,15, " Lancement de la requête principale " ,-1);
                   
                   String requet_alert = "SELECT pr.projet_nom , "
                                              + "t.titre_tache ,"
                                              + "t.date_debut_tache ,"
                                              + "t.date_fin_tache ,"
                                              + "t.duree_tache ,"
                                              + "p.person_email ,"
                                              + "p.person_nom ,"
                                              + "c.chef_projet_email ,"
                                              + "c.chef_projet_nom ,"
                                              + "t.id_tache ,"
                                              + "p.id_person ,"
                                              +"pr.projet_description "
                                              + " from taches t ,chef_projet c , person p , projet pr ,tache_person tp "
                                              + " where c.id_chef_projet=pr.id_chef_projet AND pr.id_projet=t.id_projet AND t.id_tache=tp.id_tache "
                                               + " AND tp.id_person=p.id_person AND t.date_fin_tache<? AND t.est_alter='false' AND tp.est_complet='false'";
                   Calendar cal = Calendar.getInstance();
                   cal.setTime(new java.util.Date());
                   cal.add(Calendar.DAY_OF_YEAR,-duree_alerte);
                   cal.set(Calendar.HOUR_OF_DAY, 0);
                   cal.set(Calendar.MINUTE, 0);
                   cal.set(Calendar.SECOND, 0);
                   cal.set(Calendar.MILLISECOND, 0);

                   java.sql.Timestamp alter_date = new java.sql.Timestamp(cal.getTimeInMillis());

                   try
                   {
                   PreparedStatement stmt_alert = con.prepareStatement(requet_alert);
                   stmt_alert.setTimestamp(1, alter_date);
                   ResultSet rs_alert= stmt_alert.executeQuery();  
                   
                   count=1;
                    local_erreur=false;
                   
                    while(rs_alert.next())
                                              {if(safe_exit==true)  System.exit(-1);
                                                            etat_erreur=45;
                                                            change_statue_progress(text_statue_alert,bar_alert,done_alert ,20, " le stockage de resultats du  requête " ,count);
                   
                                                          String projet_nom = rs_alert.getString(1) ;
                                                          String titre_tache = rs_alert.getString(2) ;
                                                          java.sql.Timestamp date_debut_tache = rs_alert.getTimestamp(3) ;
                                                          java.sql.Timestamp date_fin_tache = rs_alert.getTimestamp(4) ;


                                                          int duree_tache = rs_alert.getInt(5) ;
                                                          String person_email = rs_alert.getString(6) ;
                                                          String person_nom = rs_alert.getString(7) ;
                                                         
                                                          String chef_projet_email = rs_alert.getString(8) ;
                                                          String chef_projet_nom = rs_alert.getString(9) ;
                                                          
                                                          int id_tache = rs_alert.getInt(10) ;
                                                          int id_person = rs_alert.getInt(11) ;
                                                          
                                                          String projet_description = rs_alert.getString(12) ;
                                                          DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                      
                                                           java.util.Date date_now = new java.util.Date();

                                                    

                                                          
                                                          Session session = Session.getInstance(props,
                                                           new javax.mail.Authenticator() {
                                                                 protected PasswordAuthentication getPasswordAuthentication() {
                                                                         return new PasswordAuthentication(smtp_email, smtp_password);
                                                                 }
                                                           });
                                                          
                                                          etat_erreur=46;
                                                            change_statue_progress(text_statue_alert,bar_alert,done_alert ,40, " L'envoie les messages des alertes " ,count);
                                                         try
                                                         {
                                                          Message message = new MimeMessage(session);
                                                       message.setFrom(new InternetAddress(smtp_email));
                                                       message.setRecipients(Message.RecipientType.TO,
                                                               InternetAddress.parse(chef_projet_email));
                                                       message.setSubject("[alert "+id_tache+id_person+"] Retard du tache "+titre_tache+" ( "+projet_nom+" )");
                                                       message.setContent("<h2>Bonjour "+chef_projet_nom+" </h2>\n"
                                                               + "<br> On vous informe qu'il ya un retard d'une tache dans le projet <strong>\""+projet_nom+"\"</strong>"
                                                               +"<br> les informations du retard : <br><br>"
                                                               +"<ul><li><strong>  Nom du projet : </strong>&nbsp;&nbsp;"+projet_nom+"</li>"
                                                               +"<li><strong>  Titre de la tache : </strong>&nbsp;&nbsp;"+titre_tache+"</li>"
                                                               +"<li><strong>  Assigné à :</strong> &nbsp;&nbsp;"+person_nom+"  ( "+person_email+" )"+"</li>"
                                                               +"<li><strong>  Date de debut du tache : </strong>&nbsp;&nbsp;"+date_debut_tache.toString()+"</li>"
                                                               +"<li><strong>  Date de fin du tache : </strong>&nbsp;&nbsp;"+date_fin_tache.toString()+"</li>"
                                                               +"<li><strong>  La durée du tache  : </strong>&nbsp;&nbsp;"+duree_tache/600+" Heures</li>"
                                                               + "<li><strong> La description du projet  : </strong>&nbsp;&nbsp;"+projet_description+"</li></ul>"
                                                               +"<br>Vous avez reçu ce mail car vous êtes le chef de projet de \""+projet_nom+"\""
                                                               +"<br> Cette alerte a envoyé le : "+dateFormat.format(date_now), "text/html");

                                                       Transport.send(message);
                                                       
                                                        etat_erreur=46;
                                                            change_statue_progress(text_statue_alert,bar_alert,done_alert ,55, " Enregistrement des Modification Dans la base de données[taches] " ,count);
                   
                                                        String update_alert_complet="update taches "
                                                                                          + " set est_alter='true'"
                                                                                          + " where id_tache=? ;";
                                               PreparedStatement stmt_alert_complet = con.prepareStatement(update_alert_complet);
                                                   stmt_alert_complet.setInt(1, id_tache);

                                                   stmt_alert_complet.executeUpdate();
                                                 
                                                if(lignes_historique!=0)
                                                          {
                                                              
                                                               etat_erreur=47;
                                                            change_statue_progress(text_statue_alert,bar_alert,done_alert ,55, " Historisation[historique_taches]  " ,count);
                   
                                                              historique(con ,id_tache, id_person ,"Alerte" );
                                                          }
                                                
                            
                    
                  
                       
                      
                                                       count++;
                                                         } catch (AddressException ex) {
                                                erreur_log(etat_erreur,person_email," AddressException",ex.getMessage(), 1 );
                                                                                      if(safe_exit==true)  System.exit(-1);
                                                                                      local_erreur=true;
                                                                                      is_there_error=true;
                                                                                      continue;
                                                } catch (MessagingException ex) {
                                                    erreur_log(etat_erreur,person_email," MessagingException",ex.getMessage(), 1 );
                                                                                         if(safe_exit==true)  System.exit(-1);
                                                                                          local_erreur=true;
                                                                                          is_there_error=true;
                                                                                         continue;
                                                }
                                                         
                                                         catch (SQLException ex) {
                                                           erreur_log(etat_erreur,person_email," SQLException",ex.getMessage(), 1 );
                                                                                         if(safe_exit==true)  System.exit(-1);
                                                                                         local_erreur=true;
                                                                                          is_there_error=true;
                                                                                         continue;  
                                                             
                                                         }
                   
                                              }
                    
               if(safe_exit==true)  System.exit(-1);
               change_statue_progress(text_statue_alert,bar_alert,done_alert,100, " Terminé" ,-1);
                   end=System.currentTimeMillis();
                   
                   if(local_erreur==false)
               change_statue_done(text_statue_alert,bar_alert,done_alert , start , end );
               else
               change_statue_warning(text_statue_alert,bar_alert,done_alert , start , end ); 
                 }catch (SQLException ex) {
                          JOptionPane.showMessageDialog(f, message_erreur(etat_erreur,null," SQLException",ex.getMessage()),"Erreur N°:"+etat_erreur,JOptionPane.ERROR_MESSAGE);
                          erreur_log(etat_erreur,null," SQLException",ex.getMessage(), 1 );
                         
                          if(safe_exit==true)  System.exit(-1);
                          change_statue_erreur(text_statue_alert,bar_alert, done_alert, etat_erreur );
                          is_there_error=true;
                          
                      }
               safe_exit=true;
             
                  
          

                   
            // end of all
                   
                 
            last_use=new java.util.Date();
            
            Calendar cal_use = Calendar.getInstance();
           cal_use.setTime(new java.util.Date());
           if(rerun<60)
                                         
           {
               cal_use.add(Calendar.DAY_OF_YEAR,0);
               cal_use.add(Calendar.HOUR_OF_DAY, 0);
               cal_use.add(Calendar.MINUTE, (int) rerun);
               cal_use.add(Calendar.SECOND, 0);
               cal_use.add(Calendar.MILLISECOND, 0);
           }
           
           else if (rerun>60)
           {
                   cal_use.add(Calendar.DAY_OF_YEAR,0);
                   cal_use.add(Calendar.HOUR_OF_DAY,  (int) rerun);
                   cal_use.add(Calendar.MINUTE,0);
                   cal_use.add(Calendar.SECOND, 0);
                   cal_use.add(Calendar.MILLISECOND, 0);
               }
           next_use = cal_use.getTime();  
            
                      
                     
             end_all=System.currentTimeMillis();
             if(is_there_error==false)
             {
             text_last_use.setSize(450, text_last_use.getHeight());
             view_error.setVisible(false);
             text_last_use.setText("   Dernière exécution  : ["+dateFormat_use.format(last_use)+"] ("+calcul_time(start_all,end_all)+")");
             text_next_use.setText("   L'exécution suivante: ["+dateFormat_use.format(next_use)+"]");
             }
             
             else
             {
                   text_last_use.setSize(380, text_last_use.getHeight());
                   view_error.setVisible(true);
                   text_last_use.setText("   Dernière exécution  : ["+dateFormat_use.format(last_use)+"] ("+calcul_time(start_all,end_all)+")");
                   text_next_use.setText("   L'exécution suivante: ["+dateFormat_use.format(next_use)+"]");
                   
             }
             
             rerun_now.setEnabled(true);
            
             countDownLatch = new CountDownLatch(1);
             countDownLatch.await(rerun*60*1000, TimeUnit.MILLISECONDS);
               } catch (InterruptedException ex) {
                   Logger.getLogger(Projet_abc.class.getName()).log(Level.SEVERE, null, ex);
                   
               }
            
           
               
               
               
      
          }
      
                    
    }
}
