/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.view.controller;

import exceptions.EmailNotUniqueException;
import exceptions.LoginExistingException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import project.logic.Logic;
import message.Privilege;
import message.Status;
import message.User;

/**
 * Clase que define los manejadores de eventos de la interfaz
 * @author Gorka
 */
public class SignUpController {
    
    private static final Logger LOG = Logger.getLogger("view.controller.SignUpController");
    
    @FXML
    private TextField txtFName;
    @FXML
    private TextField txtFEmail;
    @FXML
    private TextField txtFUser;
    @FXML
    private TextField txtFPassword;
    @FXML
    private TextField txtFRpPassword;
    @FXML
    private PasswordField pwPassword;
    @FXML
    private PasswordField pwRpPassword;
    @FXML
    private Button btnSignUp;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnPassShow;
    @FXML
    private Button btnRpPassShow;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblPassword;
    @FXML
    private Label lblRpPassword;
    
    private String errorMessage;
    
    private Logic logic;
    
    private Stage stage;
    public Stage getStage(){
        return stage;
    }
    public void setStage(Stage stage){
        this.stage=stage;
    }
    
    /**
     * Inilization of the window
     * @param root 
     */
    public void initStage(Parent root){
        //Create a scene associated to the node grap root
        Scene scene = new Scene(root);
        //Associate scene to Stage(Window)
        stage.setScene(scene);
        //Set window properties
        stage.setTitle("Sign up");
        stage.setResizable(false);
        //Set window's event handlers
        stage.setOnShowing(this::handleWindowShowing);
        //Set control events
        btnSignUp.setOnAction(this::pushSignUp);
        btnPassShow.setOnMousePressed(this::pressPassShow);
        btnPassShow.setOnMouseReleased(this::relasePassShow);
        btnRpPassShow.setOnMousePressed(this::pressPassShow);
        btnRpPassShow.setOnMouseReleased(this::relasePassShow);
        btnCancel.setOnAction(this::pushCancel);
        txtFName.textProperty().addListener(this::textChanged);
        txtFEmail.textProperty().addListener(this::textChanged);
        txtFUser.textProperty().addListener(this::textChanged);
        pwPassword.textProperty().addListener(this::textChanged);
        pwRpPassword.textProperty().addListener(this::textChanged);
        //Show window
        stage.show();
    }
    
    /**
     * 
     * @param event 
     */
    public void handleWindowShowing(WindowEvent event){
        LOG.info("Beginning handleWindowShowing");
        //Set the mnemonic parse
        btnSignUp.setMnemonicParsing(true);
        btnCancel.setMnemonicParsing(true);
        //Set the mnemonic character and the text
        btnSignUp.setText("_SignUp");
        btnCancel.setText("_Cancel");
    }
    
    /**
     * Text changed of the fields Full name, Email, Username, Password y
     * Repeat Password
     * @param observable
     * @param oldValue
     * @param newValue 
     */
     private void textChanged(ObservableValue observable, String oldValue,
            String newValue) {
         if(!txtFName.getText().trim().isEmpty() && !txtFEmail.getText().trim().isEmpty()
                && !txtFUser.getText().trim().isEmpty() && !pwPassword.getText().trim().isEmpty()
                && !pwRpPassword.getText().trim().isEmpty()){
            btnSignUp.setDisable(false);
         }else {
            btnSignUp.setDisable(true);
         }
         
         if(txtFName.getLength() == 101){
            txtFName.setText(txtFName.getText().substring(0,100));
            new Alert(AlertType.INFORMATION,"Name too long",ButtonType.OK).show();
         }
         
         
         if(txtFEmail.getLength() == 51){
            txtFEmail.setText(txtFEmail.getText().substring(0,50));
            new Alert(AlertType.INFORMATION,"Email too long",ButtonType.OK).show();
         }
         
         if(txtFUser.getLength() == 21){
            txtFUser.setText(txtFUser.getText().substring(0,20));
            new Alert(AlertType.INFORMATION,"User too long",ButtonType.OK).show();
         }
         
         if(pwPassword.getLength() == 17){
            pwPassword.setText(pwPassword.getText().substring(0,16));
            new Alert(AlertType.INFORMATION,"Password too long",ButtonType.OK).show();
         }
         
         if(pwRpPassword.getLength() == 17){
            pwRpPassword.setText(pwRpPassword.getText().substring(0,16));
            new Alert(AlertType.INFORMATION,"Password too long",ButtonType.OK).show();
         } 
     }
    
    /**
     * Pulse SignUp button
     * @param event 
     */
    private void pushSignUp(ActionEvent event){
        errorMessage = "";
        if(verifyEmail() && verifyUser() && verifyPassword()){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            User user = new User(Status.enabled, Privilege.user);
            user.setFullName(txtFName.getText());
            user.setLogin(txtFUser.getText());
            user.setEmail(txtFEmail.getText());
            user.setPassword(pwPassword.getText());
            user.setLastAcces(timestamp);
            user.setLastPasswordChange(timestamp);
            try {
                logic.signUpUser(user);
                new Alert(AlertType.CONFIRMATION,"Register confirmed",ButtonType.OK).showAndWait();
                openLogIn();
            } catch (LoginExistingException lee) {
                LOG.log(Level.SEVERE, lee.getMESSAGE(), lee);
                lblUser.setTextFill(Color.web("#FF0000"));
                new Alert(AlertType.ERROR,lee.getMESSAGE(), ButtonType.OK).showAndWait();
            } catch (EmailNotUniqueException enue) {
                LOG.log(Level.SEVERE, enue.getMESSAGE(), enue);
                lblEmail.setTextFill(Color.web("#FF0000"));
                new Alert(AlertType.ERROR,enue.getMESSAGE(), ButtonType.OK).showAndWait();
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }else {
            new Alert(AlertType.ERROR,errorMessage,ButtonType.OK).showAndWait();
        }
    }
    
    /**
     * Mouse down PassShow button
     * @param event 
     */
    private void pressPassShow(MouseEvent event){
        if(btnPassShow.isPressed()){
            txtFPassword.setText(pwPassword.getText());
            txtFPassword.setVisible(true);
            pwPassword.setVisible(false);
        }else if(btnRpPassShow.isPressed()){
            txtFRpPassword.setText(pwRpPassword.getText());
            txtFRpPassword.setVisible(true);
            pwRpPassword.setVisible(false);
        }
    }
    
    /**
     * Mouse up PassShow button
     * @param event 
     */
    private void relasePassShow(MouseEvent event){
        pwPassword.setVisible(true);
        txtFPassword.setVisible(false);
        
        pwRpPassword.setVisible(true);
        txtFRpPassword.setVisible(false);
    }
    
    /**
     * Pulse Cancel button
     * @param event 
     */
    private void pushCancel(ActionEvent event){
        openLogIn();
    }
    /**
     * Method to open LogIn window and close SignUp
     * @return boolean
     */
    private void openLogIn() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/project/view/xml/LogInXML.fxml"));
            Parent root = (Parent)loader.load();
            Stage logInStage=new Stage();
            LogInController controller = ((LogInController)loader.getController());
            controller.setStage(logInStage);
            controller.setLogic(logic);
            controller.initStage(root);
            stage.hide();
        }catch(IOException e){
            LOG.log(Level.SEVERE, e.getMessage(), e);
            new Alert(AlertType.ERROR,e.getMessage(),ButtonType.OK).show();
        }
    }
    
    /**
     * Method to validate email
     * @return boolean
     */
    private boolean verifyEmail() {
        boolean a = false;
        boolean b = false;
        for(int i = 0; i < txtFEmail.getLength(); i++){
            if(!a && txtFEmail.getText().substring(i, i+1).equalsIgnoreCase("@")){
               a = true; 
            }
            if(a && txtFEmail.getText().substring(i, i+1).equalsIgnoreCase(".")){
                b = true;
            }
        }
        if(b){
            lblEmail.setTextFill(Color.web("#237bf7"));
            return true;
        }else {
            lblEmail.setTextFill(Color.web("#FF0000"));
            errorMessage = "Your email have wrong format. Correct format is: "
                    + "example@example.example";
            return false;
        }
    }
    
    /**
     * Method to validate user
     * @return boolean
     */
    private boolean verifyUser() {
        if(txtFUser.getLength() >= 4){
            lblUser.setTextFill(Color.web("#237bf7"));
            return true;
        }else {
            lblUser.setTextFill(Color.web("#FF0000"));
            if(errorMessage.isEmpty()){
                errorMessage = "Username too short";
            }else {
                errorMessage = errorMessage + "\nUsername too short";
            }
            return false;
        }
    }
    
    /**
     * Method to validate password
     * @return boolean
     */
    private boolean verifyPassword() {
        if(pwPassword.getLength() >=4){
            if(pwPassword.getText().equals(pwRpPassword.getText())){
                lblPassword.setTextFill(Color.web("#237bf7"));
                lblRpPassword.setTextFill(Color.web("#237bf7"));
                return true;
            }else {
                lblPassword.setTextFill(Color.web("#FF0000"));
                lblRpPassword.setTextFill(Color.web("#FF0000"));
                if(errorMessage.isEmpty()){
                    errorMessage = "Password no coinciden";
                }else {
                    errorMessage = errorMessage + "\nPassword no coinciden";
                }
                return false;
            }
        }else {
            pwPassword.clear();
            pwRpPassword.clear();
            lblPassword.setTextFill(Color.web("#FF0000"));
            lblRpPassword.setTextFill(Color.web("#FF0000"));
            if(errorMessage.isEmpty()){
                errorMessage = "Password too shot";
            }else {
                errorMessage = errorMessage + "\nPassword too short";
            }
            return false;
        }
        
        
    }
    
    /**
     * 
     * @param logic 
     */
    public void setLogic(Logic logic) {
        this.logic=logic;
    }
}
