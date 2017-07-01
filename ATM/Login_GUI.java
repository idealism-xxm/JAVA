package ATM;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Login_GUI extends Application {
	private GridPane gridPane =  new GridPane();
	private HBox hBox = new HBox();
	private VBox vBox = new VBox();
	
	private Text txtLoginType = new Text("登录方式：");
	private ComboBox<String> cbLoginType = new ComboBox<String>();
	private Text txtUsername = new Text("银行卡号：");
	private TextField tfUsername = new TextField();
	private Text txtPassword = new Text("密码：");
	private TextField tfPassword = new TextField();
	private Button btRegist = new Button("注册");
	private Button btLogin = new Button("登录");

	@Override
	public void start(Stage primaryStage) throws Exception {
		btRegist.setVisible(false); //银行卡不允许注册
		btLogin.setOnAction(btLoginE -> {
			if(accountLogin()) {//如果登录成功，则关掉登录窗口
				primaryStage.close();
			}
		});
		
		cbLoginType.getItems().addAll("银行卡", "用户", "管理员");
		cbLoginType.getSelectionModel().select(0);//默认选择银行卡
		cbLoginType.setOnAction(e -> setLoginType(primaryStage));
		
		
		gridPane.add(txtLoginType, 0, 0);
		gridPane.add(cbLoginType, 1, 0);
		gridPane.add(txtUsername, 0, 1);
		gridPane.add(tfUsername, 1, 1);
		gridPane.add(txtPassword, 0, 2);
		gridPane.add(tfPassword, 1, 2);
		
		GridPane.setHalignment(cbLoginType, HPos.CENTER);
		gridPane.setPadding(new Insets(5, 5, 5, 5));
		gridPane.setHgap(5);
		gridPane.setVgap(5);
		
		
		
		hBox.getChildren().addAll(btLogin, btRegist);
		hBox.setPadding(new Insets(5, 5, 5, 5));
		hBox.setAlignment(Pos.BASELINE_CENTER);
		HBox.setMargin(btLogin, new Insets(5, 20, 0, 5));
		HBox.setMargin(btRegist, new Insets(5, 5, 0, 20));
		
		vBox.getChildren().addAll(gridPane, hBox);
		
		Scene scene = new Scene(vBox);
	    
		primaryStage.setResizable(false);
	    primaryStage.setTitle("登录"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	}
	
	private void setLoginType(Stage primaryStage) {
		int index = cbLoginType.getSelectionModel().getSelectedIndex();
		if(index == 0) {
			btRegist.setVisible(false); //如果选择银行卡，则不允许注册
			txtUsername.setText("银行卡号：");
			btLogin.setOnAction(btLoginE -> {
				if(accountLogin()) {//如果登录成功，则关掉登录窗口
					primaryStage.close();
				}
			});
		}
		else if(index == 1) { //选择用户登录
			btRegist.setVisible(true);
			txtUsername.setText("用户名：");
			btLogin.setOnAction(btLoginE -> {
				if(userLogin()) {//如果登录成功，则关掉登录窗口
					primaryStage.close();
				}
			});
			btRegist.setOnAction(btRegistE -> userRegsit());
		}
		else { //选择管理员登录
			btRegist.setVisible(true);
			txtUsername.setText("管理员名：");
			btLogin.setOnAction(btLoginE -> {
				if(adminLogin()) {//如果登录成功，则关掉登录窗口
					primaryStage.close();
				}
			});
			btRegist.setOnAction(btRegistE -> adminRegsit());
		}
	}
	
	private boolean accountLogin() {
		String number = tfUsername.getText();
		String passwordMD5 = MD5.getMD5(tfPassword.getText());
		try {
			if(!Account.exists(number)) {
				//银行卡号不存在
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("错误提示");
				alert.setContentText("银行卡号或密码错误，请重新输入！");
				alert.showAndWait();
				return false;
			}
			
			Account account = Account.getAccount(number);
			if(account.isPasswordCorrect(passwordMD5)) {//成功登录
				new Account_GUI(account).start(new Stage());
				return true;
			}
			
			//密码不正确
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("错误提示");
			alert.setContentText("银行卡号或密码错误，请重新输入！");
			alert.showAndWait();
			
			
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("错误提示");
			alert.setContentText("发生未知错误，请重启程序！");
			alert.showAndWait();
			
			e1.printStackTrace();
		}
		return false;
	}
	
	private boolean userLogin() {//返回true表示成功登录
		String username = tfUsername.getText();
		try {
			if(User.exists(username)) {
				User user = User.getUser(username);
				if(user.isPasswordCorrect(MD5.getMD5(tfPassword.getText()))) {//成功登录
					new User_GUI(user).start(new Stage());
					return true;
				}
				
				//密码不正确
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("错误提示");
				alert.setContentText("用户名或密码错误，请重新输入！");
				alert.showAndWait();
				return false;
			}
			
			//用户名不存在
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("错误提示");
			alert.setContentText("用户名或密码错误，请重新输入！");
			alert.showAndWait();
		}
		catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("错误提示");
			alert.setContentText("发生未知错误，请重启程序！");
			alert.showAndWait();
			
			e1.printStackTrace();
		}
		return false;
	}
	
	private void userRegsit() {
		String username = tfUsername.getText();
		String password = tfPassword.getText();
		try {
			if(User.exists(username)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("提示");
				alert.setContentText("用户名已存在，请重新输入！");
				alert.showAndWait();
			}
			else {//用户名不存在
				if(password.length() < 8) {//密码长度不能小于8位
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("提示");
					alert.setContentText("密码长度不能小于8位，请重新输入！");
					alert.showAndWait();
				}
				else { //成功注册用户
					User.appendUser(User.creatUser(username, MD5.getMD5(password)));
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("提示");
					alert.setContentText("用户" + username + "注册成功！");
					alert.showAndWait();
				}
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("错误提示");
			alert.setContentText("发生未知错误，请重启程序！");
			alert.showAndWait();
			
			e1.printStackTrace();
		}
	}
	
	private boolean adminLogin() {
		String adminname = tfUsername.getText();
		String passwordMD5 = MD5.getMD5(tfPassword.getText());
		try {
			if(Administrator.exists(adminname)) {
				Administrator admin = Administrator.getAdministrator(adminname);
				if(admin.isPasswordCorrect(passwordMD5)) {//成功登录
					new Administrator_GUI(admin).start(new Stage());
					return true;
				}
				
				//密码不正确
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("错误提示");
				alert.setContentText("管理员名或密码错误，请重新输入！");
				alert.showAndWait();
				return false;
			}
			
			//管理员名不存在
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("错误提示");
			alert.setContentText("管理员名或密码错误，请重新输入！");
			alert.showAndWait();
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("错误提示");
			alert.setContentText("发生未知错误，请重启程序！");
			alert.showAndWait();
			
			e1.printStackTrace();
		}
		return false;
	}
	
	private void adminRegsit() {
		String adminname = tfUsername.getText();
		String password = tfPassword.getText();
		try {
			if(Administrator.exists(adminname)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("提示");
				alert.setContentText("管理员名已存在，请重新输入！");
				alert.showAndWait();
			}
			else {//管理员名不存在
				if(password.length() < 8) {//密码长度不能小于8位
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("提示");
					alert.setContentText("密码长度不能小于8位，请重新输入！");
					alert.showAndWait();
				}
				else { //成功注册管理员
					Administrator.appendAdmin(Administrator.creatAdministrator(adminname, MD5.getMD5(password)));
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("提示");
					alert.setContentText("管理员" + adminname + "注册成功！");
					alert.showAndWait();
				}
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("错误提示");
			alert.setContentText("发生未知错误，请重启程序！");
			alert.showAndWait();
			
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
