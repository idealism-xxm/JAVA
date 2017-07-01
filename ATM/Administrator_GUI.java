package ATM;

import java.io.FileNotFoundException;
import java.util.ArrayList;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Administrator_GUI extends Application {

	private Administrator admin = null;
	
	private Text txtAdminInfo = null;
	private BorderPane borderPane =  new BorderPane();
	private StackPane spCenter = new StackPane();
	private VBox vbLeft = new VBox();
	private VBox vbRight = new VBox();
	private Button btQueryUser = new Button("查询用户信息");
	private Button btQueryAccount = new Button("查询账户信息");
	private Button btCreatAccount = new Button("办理银行卡");
	private Button btChangePassword = new Button("修改密码");
	private Button btExit = new Button("退      出");
	
	User user = null;
	String username = null;
	
	public Administrator_GUI(Administrator admin) {
		this.admin = admin;
		txtAdminInfo = new Text("管理员名：" + admin.getAdminName());
	}

	@Override
	public void start(Stage primaryStage) {
		
		btChangePassword.setOnAction(e -> changePassword());
		btExit.setOnAction(e -> {
			closeInfo();
			primaryStage.close();
		});
		btQueryUser.setOnAction(e -> queryUser());
		btQueryAccount.setOnAction(e -> queryAccount());
		btCreatAccount.setOnAction(e -> creatAccount());
		
		vbLeft.getChildren().addAll(btChangePassword, btExit);
		VBox.setMargin(btChangePassword, new Insets(60, 5, 30, 5));
		VBox.setMargin(btExit, new Insets(30, 5, 5, 5));
		
		vbRight.getChildren().addAll(btQueryUser, btQueryAccount, btCreatAccount);
		VBox.setMargin(btQueryUser, new Insets(40, 5, 20, 5));
		VBox.setMargin(btQueryAccount, new Insets(20, 5, 20, 5));
		VBox.setMargin(btCreatAccount, new Insets(20, 5, 5, 5));
		
		borderPane.setTop(txtAdminInfo);
		borderPane.setLeft(vbLeft);
		borderPane.setRight(vbRight);
		borderPane.setCenter(spCenter);
		
		BorderPane.setAlignment(txtAdminInfo, Pos.BOTTOM_CENTER);

		BorderPane.setMargin(txtAdminInfo, new Insets(5, 5, 5, 5));
		BorderPane.setMargin(vbLeft, new Insets(5, 5, 5, 5));
		BorderPane.setMargin(vbRight, new Insets(5, 5, 5, 5));
		
		Scene scene = new Scene(borderPane, 480, 320);
	    
		primaryStage.setOnCloseRequest(e -> closeInfo());
		primaryStage.setResizable(false);
	    primaryStage.setTitle("主菜单"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	}
	
	private void queryUser() {
		ArrayList<String> usernames = null;
		try {
			usernames = User.getAllUsernames();
		} catch (FileNotFoundException e2) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("错误提示");
			alert.setContentText("发生未知错误，请重启程序！");
			alert.showAndWait();
			
			e2.printStackTrace();
			return ;
		}
		
		if(usernames.size() == 0) {//目前没有用户
			spCenter.getChildren().clear(); //清除上一次操作的界面
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("错误提示");
			alert.setContentText("目前没有用户！");
			alert.showAndWait();
			
			return ;
		}
		
		Text txtAccountSelection = new Text("选择用户：");
		ComboBox<String> cbAccountSelection = new ComboBox<String>();
		Text txtContent = new Text("");
		Button btOK = new Button("确认");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			try {
				User user = User.getUser(cbAccountSelection.getSelectionModel().getSelectedItem());
				String content = "用户名：" + user.getUsername() + System.lineSeparator() + 
						"银行卡数目：" + user.getAccounts().size() + System.lineSeparator();
				for(int i = 0; i < user.getAccounts().size(); ++i) {
					content += user.getAccounts().get(i) + System.lineSeparator();
				}
				txtContent.setText(content);
				spCenter.getChildren().clear(); //清除上一次操作的界面
				spCenter.getChildren().add(txtContent);
			} catch (Exception e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("错误提示");
				alert.setContentText("发生未知错误，请重启程序！");
				alert.showAndWait();
				
				e1.printStackTrace();
			}
		});
		
		cbAccountSelection.getItems().addAll(usernames);
		cbAccountSelection.getSelectionModel().select(0);//默认选择第一个用户
		
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

	private void queryAccount() {
		ArrayList<String> cardNumbers = null;
		try {
			cardNumbers = Account.getAllCardNumbers();
		} catch (FileNotFoundException e2) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("错误提示");
			alert.setContentText("发生未知错误，请重启程序！");
			alert.showAndWait();
			
			e2.printStackTrace();
			return ;
		}
		
		if(cardNumbers.size() == 0) {//目前没有银行卡
			spCenter.getChildren().clear(); //清除上一次操作的界面
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("错误提示");
			alert.setContentText("目前没有账户！");
			alert.showAndWait();
			
			return ;
		}
		
		Text txtAccountSelection = new Text("选择账户：");
		ComboBox<String> cbAccountSelection = new ComboBox<String>();
		Text txtContent = new Text("");
		Button btOK = new Button("确认");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			try {
				Account account = Account.getAccount(cbAccountSelection.getSelectionModel().getSelectedItem());
				String content = "卡号：" + account.getCardNumber() + System.lineSeparator() + 
						"持卡人：" + account.getUserName() + System.lineSeparator() + 
						"余额：" + String.format("%.2f", account.getMoney()) + System.lineSeparator();
				txtContent.setText(content);
				spCenter.getChildren().clear(); //清除上一次操作的界面
				spCenter.getChildren().add(txtContent);
			} catch (Exception e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("错误提示");
				alert.setContentText("发生未知错误，请重启程序！");
				alert.showAndWait();
				
				e1.printStackTrace();
			}
		});
		
		cbAccountSelection.getItems().addAll(cardNumbers);
		cbAccountSelection.getSelectionModel().select(0);//默认选择第一个用户
		
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

	private void creatAccount() {
		Text txtPassword = new Text("请输入办理银行卡的用户名：");
		TextField tfPassword = new TextField();
		Text txtRepeatPassword = new Text("请重复新密码：");
		TextField tfRepeatPassword = new TextField();
		Button btOK = new Button("确认");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			
			if(txtPassword.getText().equals("请输入办理银行卡的用户名：")) {
				username = tfPassword.getText();
				try {
					if(User.exists(username)) {//用户存在
						user = User.getUser(username);
						if(user.getAccounts().size() >= User.getMaxAccount()) {//用户持卡数量达到上限
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("错误提示");
							alert.setContentText("用户 " + username + "已持有" + User.getMaxAccount() + 
									"张卡，无法继续办理银行卡，请重新输入！");
							alert.showAndWait();
							
							return ;
						}
						
						txtPassword.setText("请输入密码：   ");
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
						alert.setContentText("用户 " + username + "不存在，请重新输入！");
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
			else if(txtPassword.getText().equals("请输入密码：   ")) {//输入密码：
				if(tfPassword.getText().length() < 8) {//密码长度不能小于8位
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("提示");
					alert.setContentText("密码长度不能小于8位，请重新输入！");
					alert.showAndWait();
					return ;
				}
				if(MD5.getMD5(tfPassword.getText()).equals(MD5.getMD5(tfRepeatPassword.getText()))) {//密码相同
					try {
						Account account = Account.creatAccount(MD5.getMD5(tfRepeatPassword.getText()), username);
						Account.appendAccount(account);
						user.getAccounts().add(account.getCardNumber());
						user.save();
					} catch (Exception e1) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("错误提示");
						alert.setContentText("发生未知错误，请重启程序！");
						alert.showAndWait();
						
						e1.printStackTrace();
						return ;
					}
					vBox.getChildren().clear();
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("信息提示");
					alert.setContentText("银行卡办理成功！");
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
					if(admin.isPasswordCorrect(MD5.getMD5(tfPassword.getText()))) {//密码正确
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
						alert.setContentText("用户名或密码错误，请重新输入！");
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
					admin.setPasswordMD5(MD5.getMD5(tfRepeatPassword.getText()));//修改密码
					admin.save();
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
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
