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
	
	private Text txtLoginType = new Text("��¼��ʽ��");
	private ComboBox<String> cbLoginType = new ComboBox<String>();
	private Text txtUsername = new Text("���п��ţ�");
	private TextField tfUsername = new TextField();
	private Text txtPassword = new Text("���룺");
	private TextField tfPassword = new TextField();
	private Button btRegist = new Button("ע��");
	private Button btLogin = new Button("��¼");

	@Override
	public void start(Stage primaryStage) throws Exception {
		btRegist.setVisible(false); //���п�������ע��
		btLogin.setOnAction(btLoginE -> {
			if(accountLogin()) {//�����¼�ɹ�����ص���¼����
				primaryStage.close();
			}
		});
		
		cbLoginType.getItems().addAll("���п�", "�û�", "����Ա");
		cbLoginType.getSelectionModel().select(0);//Ĭ��ѡ�����п�
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
	    primaryStage.setTitle("��¼"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	}
	
	private void setLoginType(Stage primaryStage) {
		int index = cbLoginType.getSelectionModel().getSelectedIndex();
		if(index == 0) {
			btRegist.setVisible(false); //���ѡ�����п���������ע��
			txtUsername.setText("���п��ţ�");
			btLogin.setOnAction(btLoginE -> {
				if(accountLogin()) {//�����¼�ɹ�����ص���¼����
					primaryStage.close();
				}
			});
		}
		else if(index == 1) { //ѡ���û���¼
			btRegist.setVisible(true);
			txtUsername.setText("�û�����");
			btLogin.setOnAction(btLoginE -> {
				if(userLogin()) {//�����¼�ɹ�����ص���¼����
					primaryStage.close();
				}
			});
			btRegist.setOnAction(btRegistE -> userRegsit());
		}
		else { //ѡ�����Ա��¼
			btRegist.setVisible(true);
			txtUsername.setText("����Ա����");
			btLogin.setOnAction(btLoginE -> {
				if(adminLogin()) {//�����¼�ɹ�����ص���¼����
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
				//���п��Ų�����
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("������ʾ");
				alert.setContentText("���п��Ż�����������������룡");
				alert.showAndWait();
				return false;
			}
			
			Account account = Account.getAccount(number);
			if(account.isPasswordCorrect(passwordMD5)) {//�ɹ���¼
				new Account_GUI(account).start(new Stage());
				return true;
			}
			
			//���벻��ȷ
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("������ʾ");
			alert.setContentText("���п��Ż�����������������룡");
			alert.showAndWait();
			
			
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("������ʾ");
			alert.setContentText("����δ֪��������������");
			alert.showAndWait();
			
			e1.printStackTrace();
		}
		return false;
	}
	
	private boolean userLogin() {//����true��ʾ�ɹ���¼
		String username = tfUsername.getText();
		try {
			if(User.exists(username)) {
				User user = User.getUser(username);
				if(user.isPasswordCorrect(MD5.getMD5(tfPassword.getText()))) {//�ɹ���¼
					new User_GUI(user).start(new Stage());
					return true;
				}
				
				//���벻��ȷ
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("������ʾ");
				alert.setContentText("�û���������������������룡");
				alert.showAndWait();
				return false;
			}
			
			//�û���������
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("������ʾ");
			alert.setContentText("�û���������������������룡");
			alert.showAndWait();
		}
		catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("������ʾ");
			alert.setContentText("����δ֪��������������");
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
				alert.setTitle("��ʾ");
				alert.setContentText("�û����Ѵ��ڣ����������룡");
				alert.showAndWait();
			}
			else {//�û���������
				if(password.length() < 8) {//���볤�Ȳ���С��8λ
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("��ʾ");
					alert.setContentText("���볤�Ȳ���С��8λ�����������룡");
					alert.showAndWait();
				}
				else { //�ɹ�ע���û�
					User.appendUser(User.creatUser(username, MD5.getMD5(password)));
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("��ʾ");
					alert.setContentText("�û�" + username + "ע��ɹ���");
					alert.showAndWait();
				}
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("������ʾ");
			alert.setContentText("����δ֪��������������");
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
				if(admin.isPasswordCorrect(passwordMD5)) {//�ɹ���¼
					new Administrator_GUI(admin).start(new Stage());
					return true;
				}
				
				//���벻��ȷ
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("������ʾ");
				alert.setContentText("����Ա��������������������룡");
				alert.showAndWait();
				return false;
			}
			
			//����Ա��������
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("������ʾ");
			alert.setContentText("����Ա��������������������룡");
			alert.showAndWait();
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("������ʾ");
			alert.setContentText("����δ֪��������������");
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
				alert.setTitle("��ʾ");
				alert.setContentText("����Ա���Ѵ��ڣ����������룡");
				alert.showAndWait();
			}
			else {//����Ա��������
				if(password.length() < 8) {//���볤�Ȳ���С��8λ
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("��ʾ");
					alert.setContentText("���볤�Ȳ���С��8λ�����������룡");
					alert.showAndWait();
				}
				else { //�ɹ�ע�����Ա
					Administrator.appendAdmin(Administrator.creatAdministrator(adminname, MD5.getMD5(password)));
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("��ʾ");
					alert.setContentText("����Ա" + adminname + "ע��ɹ���");
					alert.showAndWait();
				}
			}
		} catch (Exception e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("������ʾ");
			alert.setContentText("����δ֪��������������");
			alert.showAndWait();
			
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
