package ATM;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class User_GUI extends Application {

	private User user = null;
	
	private Text txtUserInfo = null;
	private BorderPane borderPane =  new BorderPane();
	private StackPane spCenter = new StackPane();
	private VBox vbLeft = new VBox();
	private VBox vbRight = new VBox();
	private Button btQueryMoney = new Button("查询余额");
	private Button btQueryDetail = new Button("查询明细");
	private Button btChangePassword = new Button("修改密码");
	private Button btExit = new Button("退      出");
	
	public User_GUI(User user) {
		this.user = user;
		txtUserInfo = new Text("用户名：" + user.getUsername());
	}

	@Override
	public void start(Stage primaryStage) {
		
		btChangePassword.setOnAction(e -> changePassword());
		btExit.setOnAction(e -> {
			closeInfo();
			primaryStage.close();
		});
		btQueryMoney.setOnAction(e -> queryMoney());
		btQueryDetail.setOnAction(e -> queryDetail());
		
		vbLeft.getChildren().addAll(btChangePassword, btExit);
		VBox.setMargin(btChangePassword, new Insets(60, 5, 30, 5));
		VBox.setMargin(btExit, new Insets(30, 5, 5, 5));
		
		vbRight.getChildren().addAll(btQueryMoney, btQueryDetail);
		VBox.setMargin(btQueryMoney, new Insets(60, 5, 30, 5));
		VBox.setMargin(btQueryDetail, new Insets(30, 5, 5, 5));
		
		borderPane.setTop(txtUserInfo);
		borderPane.setLeft(vbLeft);
		borderPane.setRight(vbRight);
		borderPane.setCenter(spCenter);
		
		BorderPane.setAlignment(txtUserInfo, Pos.BOTTOM_CENTER);

		BorderPane.setMargin(txtUserInfo, new Insets(5, 5, 5, 5));
		BorderPane.setMargin(vbLeft, new Insets(5, 5, 5, 5));
		BorderPane.setMargin(vbRight, new Insets(5, 5, 5, 5));
		
		Scene scene = new Scene(borderPane, 480, 320);
	    
		primaryStage.setOnCloseRequest(e -> closeInfo());
		primaryStage.setResizable(false);
	    primaryStage.setTitle("主菜单"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	}
	
	private void changePassword() {
		Text txtPassword = new Text("请输入当前密码：");
		TextField tfPassword = new TextField();
		Text txtRepeatPassword = new Text("请重复新密码：");
		TextField tfRepeatPassword = new TextField();
		Button btOK = new Button("确认");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			if(txtPassword.getText().equals("请输入当前密码：")) {
				try {
					if(user.isPasswordCorrect(MD5.getMD5(tfPassword.getText()))) {//密码正确
						txtPassword.setText("请输入新密码：");
						tfPassword.setText("");
						HBox hbRepeatPassword = new HBox();
						hbRepeatPassword.getChildren().addAll(txtRepeatPassword, tfRepeatPassword);
						
						hbRepeatPassword.setAlignment(Pos.CENTER);
						HBox.setMargin(txtRepeatPassword, new Insets(5, 5, 5, 5));
						HBox.setMargin(tfRepeatPassword, new Insets(5, 5, 5, 5));
						
						vBox.getChildren().add(1, hbRepeatPassword);
						tfPassword.setText("");
					}
					else {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("错误提示");
						alert.setContentText("密码错误，请重新输入！");
						alert.showAndWait();
					}
				}
				catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("错误提示");
					alert.setContentText("发生未知错误，请重启程序！");
					alert.showAndWait();
					
					e1.printStackTrace();
				}
			}
			else if(txtPassword.getText().equals("请输入新密码：")) {//输入新密码：
				if(tfPassword.getText().length() < 8) {//密码长度不能小于8位
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("提示");
					alert.setContentText("密码长度不能小于8位，请重新输入！");
					alert.showAndWait();
					return ;
				}
				if(MD5.getMD5(tfPassword.getText()).equals(MD5.getMD5(tfRepeatPassword.getText()))) {//新密码相同
					user.setPasswordMD5(MD5.getMD5(tfRepeatPassword.getText()));//修改密码
					user.save();
					vBox.getChildren().clear();
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("信息提示");
					alert.setContentText("密码修改成功！");
					alert.showAndWait();
				}
				else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("错误提示");
					alert.setContentText("两次密码不一致，请重新输入！");
					alert.showAndWait();
				}
			}
		});
		
		hBox.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		
		hBox.getChildren().addAll(txtPassword, tfPassword);
		vBox.getChildren().addAll(hBox, btOK);
		HBox.setMargin(txtPassword, new Insets(5, 5, 5, 5));
		HBox.setMargin(tfPassword, new Insets(5, 5, 5, 5));
		VBox.setMargin(btOK, new Insets(5, 5, 5, 5));
		
		spCenter.getChildren().clear(); //清除上一次操作的界面
		spCenter.getChildren().add(vBox);
	}

	private void closeInfo() { //关闭时的提示
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("信息提示");
		alert.setContentText("感谢您使用本ATM，欢迎下次光临！");
		alert.showAndWait(); //如果不响应提示框，则不能继续处理
	}

	private void queryMoney() {
		if(user.getAccounts().size() == 0) {//用户没有银行卡
			spCenter.getChildren().clear(); //清除上一次操作的界面
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("错误提示");
			alert.setContentText("您没有银行卡账户，请办理银行卡后查询！");
			alert.showAndWait();
			
			return ;
		}
		
		Text txtAccountSelection = new Text("选择账户：");
		ComboBox<String> cbAccountSelection = new ComboBox<String>();
		Text txtMoney = new Text("");
		Button btOK = new Button("确认");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			try {
				Account_GUI.queryMoney(vBox, cbAccountSelection.getSelectionModel().getSelectedItem());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		cbAccountSelection.getItems().addAll(user.getAccounts());
		cbAccountSelection.getSelectionModel().select(0);//默认选择第一张银行卡
		
		txtMoney.setFont(Font.font("Times", 20));
		txtMoney.setFill(Color.CORNFLOWERBLUE);
		
		hBox.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		
		hBox.getChildren().addAll(txtAccountSelection, cbAccountSelection);
		vBox.getChildren().addAll(hBox, btOK);
		HBox.setMargin(txtAccountSelection, new Insets(5, 5, 5, 5));
		HBox.setMargin(cbAccountSelection, new Insets(5, 5, 5, 5));
		VBox.setMargin(btOK, new Insets(5, 5, 5, 5));
		
		spCenter.getChildren().clear(); //清除上一次操作的界面
		spCenter.getChildren().add(vBox);
	}

	private void queryDetail() {
		if(user.getAccounts().size() == 0) {//用户没有银行卡
			spCenter.getChildren().clear(); //清除上一次操作的界面
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("错误提示");
			alert.setContentText("您没有银行卡账户，请办理银行卡后查询！");
			alert.showAndWait();
			
			return ;
		}
		
		Text txtAccountSelection = new Text("选择账户：");
		ComboBox<String> cbAccountSelection = new ComboBox<String>();
		Button btOK = new Button("确认");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			try {
				new Detail_GUI(Detail.getDetail(cbAccountSelection.getSelectionModel().getSelectedItem())).start(new Stage());;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		cbAccountSelection.getItems().addAll(user.getAccounts());
		cbAccountSelection.getSelectionModel().select(0);//默认选择第一张银行卡
		
		hBox.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		
		hBox.getChildren().addAll(txtAccountSelection, cbAccountSelection);
		vBox.getChildren().addAll(hBox, btOK);
		HBox.setMargin(txtAccountSelection, new Insets(5, 5, 5, 5));
		HBox.setMargin(cbAccountSelection, new Insets(5, 5, 5, 5));
		VBox.setMargin(btOK, new Insets(5, 5, 5, 5));
		
		spCenter.getChildren().clear(); //清除上一次操作的界面
		spCenter.getChildren().add(vBox);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
