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
	private Button btQueryUser = new Button("��ѯ�û���Ϣ");
	private Button btQueryAccount = new Button("��ѯ�˻���Ϣ");
	private Button btCreatAccount = new Button("�������п�");
	private Button btChangePassword = new Button("�޸�����");
	private Button btExit = new Button("��      ��");
	
	User user = null;
	String username = null;
	
	public Administrator_GUI(Administrator admin) {
		this.admin = admin;
		txtAdminInfo = new Text("����Ա����" + admin.getAdminName());
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
	    primaryStage.setTitle("���˵�"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	}
	
	private void queryUser() {
		ArrayList<String> usernames = null;
		try {
			usernames = User.getAllUsernames();
		} catch (FileNotFoundException e2) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("������ʾ");
			alert.setContentText("����δ֪��������������");
			alert.showAndWait();
			
			e2.printStackTrace();
			return ;
		}
		
		if(usernames.size() == 0) {//Ŀǰû���û�
			spCenter.getChildren().clear(); //�����һ�β����Ľ���
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("������ʾ");
			alert.setContentText("Ŀǰû���û���");
			alert.showAndWait();
			
			return ;
		}
		
		Text txtAccountSelection = new Text("ѡ���û���");
		ComboBox<String> cbAccountSelection = new ComboBox<String>();
		Text txtContent = new Text("");
		Button btOK = new Button("ȷ��");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			try {
				User user = User.getUser(cbAccountSelection.getSelectionModel().getSelectedItem());
				String content = "�û�����" + user.getUsername() + System.lineSeparator() + 
						"���п���Ŀ��" + user.getAccounts().size() + System.lineSeparator();
				for(int i = 0; i < user.getAccounts().size(); ++i) {
					content += user.getAccounts().get(i) + System.lineSeparator();
				}
				txtContent.setText(content);
				spCenter.getChildren().clear(); //�����һ�β����Ľ���
				spCenter.getChildren().add(txtContent);
			} catch (Exception e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("������ʾ");
				alert.setContentText("����δ֪��������������");
				alert.showAndWait();
				
				e1.printStackTrace();
			}
		});
		
		cbAccountSelection.getItems().addAll(usernames);
		cbAccountSelection.getSelectionModel().select(0);//Ĭ��ѡ���һ���û�
		
		hBox.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		
		hBox.getChildren().addAll(txtAccountSelection, cbAccountSelection);
		vBox.getChildren().addAll(hBox, btOK);
		HBox.setMargin(txtAccountSelection, new Insets(5, 5, 5, 5));
		HBox.setMargin(cbAccountSelection, new Insets(5, 5, 5, 5));
		VBox.setMargin(btOK, new Insets(5, 5, 5, 5));
		
		spCenter.getChildren().clear(); //�����һ�β����Ľ���
		spCenter.getChildren().add(vBox);
	}

	private void queryAccount() {
		ArrayList<String> cardNumbers = null;
		try {
			cardNumbers = Account.getAllCardNumbers();
		} catch (FileNotFoundException e2) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("������ʾ");
			alert.setContentText("����δ֪��������������");
			alert.showAndWait();
			
			e2.printStackTrace();
			return ;
		}
		
		if(cardNumbers.size() == 0) {//Ŀǰû�����п�
			spCenter.getChildren().clear(); //�����һ�β����Ľ���
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("������ʾ");
			alert.setContentText("Ŀǰû���˻���");
			alert.showAndWait();
			
			return ;
		}
		
		Text txtAccountSelection = new Text("ѡ���˻���");
		ComboBox<String> cbAccountSelection = new ComboBox<String>();
		Text txtContent = new Text("");
		Button btOK = new Button("ȷ��");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			try {
				Account account = Account.getAccount(cbAccountSelection.getSelectionModel().getSelectedItem());
				String content = "���ţ�" + account.getCardNumber() + System.lineSeparator() + 
						"�ֿ��ˣ�" + account.getUserName() + System.lineSeparator() + 
						"��" + String.format("%.2f", account.getMoney()) + System.lineSeparator();
				txtContent.setText(content);
				spCenter.getChildren().clear(); //�����һ�β����Ľ���
				spCenter.getChildren().add(txtContent);
			} catch (Exception e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("������ʾ");
				alert.setContentText("����δ֪��������������");
				alert.showAndWait();
				
				e1.printStackTrace();
			}
		});
		
		cbAccountSelection.getItems().addAll(cardNumbers);
		cbAccountSelection.getSelectionModel().select(0);//Ĭ��ѡ���һ���û�
		
		hBox.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		
		hBox.getChildren().addAll(txtAccountSelection, cbAccountSelection);
		vBox.getChildren().addAll(hBox, btOK);
		HBox.setMargin(txtAccountSelection, new Insets(5, 5, 5, 5));
		HBox.setMargin(cbAccountSelection, new Insets(5, 5, 5, 5));
		VBox.setMargin(btOK, new Insets(5, 5, 5, 5));
		
		spCenter.getChildren().clear(); //�����һ�β����Ľ���
		spCenter.getChildren().add(vBox);
	}

	private void creatAccount() {
		Text txtPassword = new Text("������������п����û�����");
		TextField tfPassword = new TextField();
		Text txtRepeatPassword = new Text("���ظ������룺");
		TextField tfRepeatPassword = new TextField();
		Button btOK = new Button("ȷ��");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			
			if(txtPassword.getText().equals("������������п����û�����")) {
				username = tfPassword.getText();
				try {
					if(User.exists(username)) {//�û�����
						user = User.getUser(username);
						if(user.getAccounts().size() >= User.getMaxAccount()) {//�û��ֿ������ﵽ����
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("������ʾ");
							alert.setContentText("�û� " + username + "�ѳ���" + User.getMaxAccount() + 
									"�ſ����޷������������п������������룡");
							alert.showAndWait();
							
							return ;
						}
						
						txtPassword.setText("���������룺   ");
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
						alert.setTitle("������ʾ");
						alert.setContentText("�û� " + username + "�����ڣ����������룡");
						alert.showAndWait();
					}
				}
				catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("������ʾ");
					alert.setContentText("����δ֪��������������");
					alert.showAndWait();
					
					e1.printStackTrace();
				}
			}
			else if(txtPassword.getText().equals("���������룺   ")) {//�������룺
				if(tfPassword.getText().length() < 8) {//���볤�Ȳ���С��8λ
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("��ʾ");
					alert.setContentText("���볤�Ȳ���С��8λ�����������룡");
					alert.showAndWait();
					return ;
				}
				if(MD5.getMD5(tfPassword.getText()).equals(MD5.getMD5(tfRepeatPassword.getText()))) {//������ͬ
					try {
						Account account = Account.creatAccount(MD5.getMD5(tfRepeatPassword.getText()), username);
						Account.appendAccount(account);
						user.getAccounts().add(account.getCardNumber());
						user.save();
					} catch (Exception e1) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("������ʾ");
						alert.setContentText("����δ֪��������������");
						alert.showAndWait();
						
						e1.printStackTrace();
						return ;
					}
					vBox.getChildren().clear();
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("��Ϣ��ʾ");
					alert.setContentText("���п�����ɹ���");
					alert.showAndWait();
				}
				else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("������ʾ");
					alert.setContentText("�������벻һ�£����������룡");
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
		
		spCenter.getChildren().clear(); //�����һ�β����Ľ���
		spCenter.getChildren().add(vBox);
	}

	private void changePassword() {
		Text txtPassword = new Text("�����뵱ǰ���룺");
		TextField tfPassword = new TextField();
		Text txtRepeatPassword = new Text("���ظ������룺");
		TextField tfRepeatPassword = new TextField();
		Button btOK = new Button("ȷ��");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			if(txtPassword.getText().equals("�����뵱ǰ���룺")) {
				try {
					if(admin.isPasswordCorrect(MD5.getMD5(tfPassword.getText()))) {//������ȷ
						txtPassword.setText("�����������룺");
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
						alert.setTitle("������ʾ");
						alert.setContentText("�û���������������������룡");
						alert.showAndWait();
					}
				}
				catch (Exception e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("������ʾ");
					alert.setContentText("����δ֪��������������");
					alert.showAndWait();
					
					e1.printStackTrace();
				}
			}
			else if(txtPassword.getText().equals("�����������룺")) {//���������룺
				if(tfPassword.getText().length() < 8) {//���볤�Ȳ���С��8λ
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("��ʾ");
					alert.setContentText("���볤�Ȳ���С��8λ�����������룡");
					alert.showAndWait();
					return ;
				}
				if(MD5.getMD5(tfPassword.getText()).equals(MD5.getMD5(tfRepeatPassword.getText()))) {//��������ͬ
					admin.setPasswordMD5(MD5.getMD5(tfRepeatPassword.getText()));//�޸�����
					admin.save();
					vBox.getChildren().clear();
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("��Ϣ��ʾ");
					alert.setContentText("�����޸ĳɹ���");
					alert.showAndWait();
				}
				else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("������ʾ");
					alert.setContentText("�������벻һ�£����������룡");
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
		
		spCenter.getChildren().clear(); //�����һ�β����Ľ���
		spCenter.getChildren().add(vBox);
	}
	
	private void closeInfo() { //�ر�ʱ����ʾ
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("��Ϣ��ʾ");
		alert.setContentText("��л��ʹ�ñ�ATM����ӭ�´ι��٣�");
		alert.showAndWait(); //�������Ӧ��ʾ�����ܼ�������
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
