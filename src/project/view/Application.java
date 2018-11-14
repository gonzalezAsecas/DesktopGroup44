/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.view;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import project.logic.Logic;
import project.logic.LogicFactory;
import project.view.controller.LogInController;

/**
 *
 * @author Gorka
 */
public class Application extends javafx.application.Application{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
    /**
     * 
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
        "/project/view/xml/LogInXML.fxml"));
        Parent root = (Parent)loader.load();
        LogInController controller = loader.getController();
        controller.setStage(stage);
        Logic logic= LogicFactory.getLogic();
        controller.setLogic(logic);
        controller.initStage(root);
    }
    
}
