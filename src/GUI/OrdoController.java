/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Entity.Ordonnance;
import Service.ServiceOrdo;
import Utils.Database;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import com.itextpdf.*;
import static com.itextpdf.kernel.pdf.PdfName.Color;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;


/**
 * FXML Controller class
 *
 * @author ASSOUMA
 */
public class OrdoController implements Initializable {
    public static final String IMAGE = "/Users/ASSOUMA/Documents/NetBeansProjects/SoftHealthFX/src/Images/Logo1.jpg";

    public static Ordonnance or;
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private Button add;
    @FXML
    private Button Convert;
    @FXML
    private TextField sarch;

    @FXML
    private Button search;

    @FXML
    private Button List;
    @FXML
    private TableView<Ordonnance> Table;
    @FXML
    private TableColumn<Ordonnance, String> medic;
    @FXML
    private TableColumn<Ordonnance, String> dateCons;
    @FXML
    private TableColumn<Ordonnance, String> nomPat;
    @FXML
    private TableColumn<Ordonnance, String> nomMed;
    @FXML
    private TableColumn<Ordonnance, Integer> descrip;
    @FXML
    private TableColumn<Ordonnance, Integer> nbj;
    @FXML
    private TableColumn<Ordonnance, Integer> dose;
    @FXML
    private TableColumn<Ordonnance, Integer> nbfois;
    @FXML
    private TableColumn<Ordonnance, Integer> nbpak;
    @FXML
    private TableColumn<Ordonnance, String> categm;

    private Connection cnx;
    private Statement stmt;
    private PreparedStatement pst;
    private ResultSet res;

    ServiceOrdo sOrd = new ServiceOrdo();
    private ObservableList<Ordonnance> fichData = FXCollections.observableArrayList();
    ObservableList<Ordonnance> ordlist = FXCollections.observableArrayList();
    @FXML
    private Button delete;
    @FXML
    private ImageView img;
    @FXML
    private ImageView home;

    public OrdoController() {
        cnx = Database.getInstance().getCon();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        img.setVisible(true);
        try {
            ShowOrdo();

        } catch (SQLException ex) {
            Logger.getLogger(OrdoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void Add_Ordonnance(MouseEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(getClass().getResource("Add_Ordonnance.fxml"));
            anchorpane.getChildren().setAll(root);

        } catch (IOException ex) {

        }
    }

    @FXML
    private void ConvertPDF(MouseEvent event ) throws FileNotFoundException, DocumentException, BadElementException, IOException {
         Ordonnance o = Table.getSelectionModel().getSelectedItem();
        try {

           Document document = new Document(PageSize.A4);
           

            PdfWriter.getInstance(document, new FileOutputStream("D://Saved/Ordonnancie.pdf"));
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
             Font fo = FontFactory.getFont(FontFactory.COURIER, 24, BaseColor.BLUE);
             
           Image image = Image.getInstance(IMAGE);
              document.add(image);
                 document.add(new Paragraph("\n\n"));
              
            Chunk chunk = new Chunk("Ordonnance", fo);
            document.add(chunk);
               document.add(new Paragraph("\n\n"));
           
           
       
           
            document.add(new Paragraph("\n"));
            chunk = new Chunk("Nom de M??decin: " + o.getUsers(), font);
            document.add(chunk);
            document.add(new Paragraph("\n"));
            chunk = new Chunk("Date de Consultation :" + o.getConsultation(), font);
            document.add(chunk);
            document.add(new Paragraph("\n"));
            chunk = new Chunk("Nom Patient :" + o.getPatient(), font);
            document.add(chunk);
            document.add(new Paragraph("\n\n\n\n\n\n\n\n\n"));
            

            chunk = new Chunk("M??dicament :" + o.getMedicaments()+" sous forme de " + o.getCategorie(), font);
            
            document.add(chunk);
            document.add(new Paragraph("\n"));
            chunk = new Chunk("" + o.getNbr_doses() + " doses " + o.getNbr_fois() + " fois par jour " + o.getDescription() , font);
            document.add(chunk);
            document.add(new Paragraph("\n"));
            chunk = new Chunk(" Pendant " + o.getNbr_jrs() + " jours ", font);
            document.add(chunk);
            

           document.close();
                       JOptionPane.showConfirmDialog(null,"PDF Ajout?? avec Succ??s",  "message", JOptionPane.PLAIN_MESSAGE);

        }catch(Exception e){
                       JOptionPane.showConfirmDialog(null,e,  "ERROR", JOptionPane.ERROR_MESSAGE);

        
        }
    
        }

    
    
    private void List(MouseEvent event) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("Ordo.fxml"));
        anchorpane.getChildren().setAll(root);
    }

    private void seaa(ActionEvent event) {

        ObservableList<Ordonnance> ordl = FXCollections.observableArrayList();
        if (event.getSource().equals(KeyCode.ENTER)) {

            try {

                cnx = Database.getInstance().getCon();
                String sql = "select * from ordonnance limit 5";
                stmt = cnx.createStatement();
                res = stmt.executeQuery(sql);

                while (res.next()) {
                    ordl.add(new Ordonnance(res.getInt(1), res.getInt(2), res.getInt(3), res.getInt(4), res.getInt(5), res.getInt(6), res.getString(7), res.getInt(8), res.getFloat(9), res.getInt(10), res.getInt(11)));

                

}
            } catch (SQLException ex) {
                Logger.getLogger(ServiceOrdo.class.getName()).log(Level.SEVERE, null, ex);

            }

            FilteredList<Ordonnance> filteredData = new FilteredList<>(ordl, b -> true);
            sarch.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(emp -> {

                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    int s = emp.getDescription().toLowerCase().indexOf(lowerCaseFilter);

                    if (s != -1) {
                        return true; // Filter matches first name.
                    } else {
                        return false; // Does not match.
                    }
                });
            });

            SortedList<Ordonnance> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(Table.comparatorProperty());
            Table.setItems(sortedData);

        }
        while (add.isPressed());
    }

    @FXML
        private void take(MouseEvent event) {
    }

    private void Social_Media(MouseEvent event) {
        try {
            AnchorPane root = FXMLLoader.load(getClass().getResource("Social_Media.fxml"));
            anchorpane.getChildren().setAll(root);

        } catch (IOException ex) {

        }

    }

    private void Stocks(MouseEvent event) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/GUI/Stocks_AddFXML.fxml"));
        anchorpane.getChildren().setAll(root);
    }

    private void load(MouseEvent event) {
        try {

            AnchorPane root = FXMLLoader.load(getClass().getResource("/GUI/Ordo.fxml"));
            anchorpane.getChildren().setAll(root);

        } catch (IOException ex) {

        }

    }

    @FXML
        private void ShowOrdo() throws SQLException {
        List<Ordonnance> listOrd = new ArrayList<Ordonnance>();

        listOrd = sOrd.afficher();
        fichData.clear();

        for (Ordonnance ls : listOrd) {
            fichData.add(ls);

        }

        descrip.setCellValueFactory(new PropertyValueFactory<>("description"));
        nbj.setCellValueFactory(new PropertyValueFactory<>("nbr_jrs"));
        dose.setCellValueFactory(new PropertyValueFactory<>("nbr_doses"));
        nbfois.setCellValueFactory(new PropertyValueFactory<>("nbr_fois"));
        nbpak.setCellValueFactory(new PropertyValueFactory<>("nbr_paquets"));

        medic.setCellValueFactory(new PropertyValueFactory<Ordonnance, String>("Medicaments"));
        categm.setCellValueFactory(new PropertyValueFactory<Ordonnance, String>("Categorie"));
        dateCons.setCellValueFactory(new PropertyValueFactory<Ordonnance, String>("Consultation"));
        nomPat.setCellValueFactory(new PropertyValueFactory<Ordonnance, String>("Patient"));
        nomMed.setCellValueFactory(new PropertyValueFactory<Ordonnance, String>("Users"));

        Table.setItems(fichData);

    }

    @FXML
        private void update(ActionEvent event) {
        Ordonnance o = Table.getSelectionModel().getSelectedItem();
        System.out.println("hhh" + o);
        or = o;
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("EditOrdonnance.fxml"));
            anchorpane.getChildren().setAll(root);

        } catch (IOException ex) {
            JOptionPane.showConfirmDialog(null, ex, "", JOptionPane.ERROR_MESSAGE);

        }

    }

    @FXML
        private void delete(MouseEvent event) throws SQLException {
        Ordonnance o = Table.getSelectionModel().getSelectedItem();
        ServiceOrdo sO = new ServiceOrdo();
        sO.supprimer(o.getId());
        JOptionPane.showConfirmDialog(null, "Ordonnance Supprim??e avec succ??e", "", JOptionPane.PLAIN_MESSAGE);

        ShowOrdo();
    }

    @FXML
        private void Rechercher(MouseEvent event) {

        do {
            FilteredList<Ordonnance> filteredData = new FilteredList<>(fichData, b -> true);

            sarch.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(ord -> {

                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();

                    if (ord.getMedicaments().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (ord.getUsers().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (ord.getConsultation().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (ord.getPatient().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (ord.getCategorie().indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (String.valueOf(ord.getNbr_doses()).indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (String.valueOf(ord.getNbr_fois()).indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (String.valueOf(ord.getNbr_paquets()).indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (String.valueOf(ord.getNbr_doses()).indexOf(lowerCaseFilter) != -1) {
                        return true;
                    } else if (ord.getDescription().indexOf(lowerCaseFilter) != -1) {
                        return true;

                    } else {
                        return false;
                    }
                });
            });

            SortedList<Ordonnance> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(Table.comparatorProperty());
            Table.setItems(sortedData);

        } while (search.isPressed());

    }

    @FXML
    private void home(MouseEvent event) {
          try {

            AnchorPane root = FXMLLoader.load(getClass().getResource("AcceuilAsma.fxml"));
            anchorpane.getChildren().setAll(root);

        } catch (IOException ex) {

        }
    }
}
