package ATM;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Detail_GUI extends Application {

	private ArrayList<Detail> details = null;
	
	public Detail_GUI(ArrayList<Detail> details) {
		this.details = details;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void start(Stage primaryStage) {
		if(details == null || details.size() == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("信息提示");
			alert.setContentText("该帐号交易明细为0条！");
			alert.showAndWait();
			
			return ;
		}
		
		//绑定列和其值域
		TableColumn tcType = new TableColumn("交易类型");
		tcType.setMinWidth(40);
		tcType.setCellValueFactory(new PropertyValueFactory("typeName"));
		
		TableColumn tcSrcNumber = new TableColumn("源账户");
		tcSrcNumber.setMinWidth(140);
		tcSrcNumber.setCellValueFactory(new PropertyValueFactory("srcNumber"));
		
		TableColumn tcDesNumber = new TableColumn("目的账户");
		tcDesNumber.setMinWidth(140);
		tcDesNumber.setCellValueFactory(new PropertyValueFactory("desNumber"));
		
		TableColumn tcMoney = new TableColumn("金额");
		tcMoney.setMinWidth(100);
		tcMoney.setCellValueFactory(new PropertyValueFactory("money"));
		
		TableColumn tcDate = new TableColumn("时间");
		tcDate.setMinWidth(200);
		tcDate.setCellValueFactory(new PropertyValueFactory("date"));
		
		TableView<Detail> tvDetail = new TableView<Detail>();
		tvDetail.getColumns().addAll(tcType, tcSrcNumber, tcDesNumber, tcMoney, tcDate);
		tvDetail.setItems(FXCollections.observableArrayList(details));
		
		StackPane pane = new StackPane();
		pane.getChildren().add(tvDetail);
		
		
		Scene scene = new Scene(pane);
	    
		primaryStage.setResizable(false);
	    primaryStage.setTitle("交易明细"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	}
	
}
