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
	private Button btQueryMoney = new Button("��ѯ���");
	private Button btQueryDetail = new Button("��ѯ��ϸ");
	private Button btChangePassword = new Button("�޸�����");
	private Button btExit = new Button("��      ��");
	
	public User_GUI(User user) {
		this.user = user;
		txtUserInfo = new Text("�û�����" + user.getUsername());
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
	    primaryStage.setTitle("���˵�"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
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
					if(user.isPasswordCorrect(MD5.getMD5(tfPassword.getText()))) {//������ȷ
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
						alert.setContentText("����������������룡");
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
					user.setPasswordMD5(MD5.getMD5(tfRepeatPassword.getText()));//�޸�����
					user.save();
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

	private void queryMoney() {
		if(user.getAccounts().size() == 0) {//�û�û�����п�
			spCenter.getChildren().clear(); //�����һ�β����Ľ���
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("������ʾ");
			alert.setContentText("��û�����п��˻�����������п����ѯ��");
			alert.showAndWait();
			
			return ;
		}
		
		Text txtAccountSelection = new Text("ѡ���˻���");
		ComboBox<String> cbAccountSelection = new ComboBox<String>();
		Text txtMoney = new Text("");
		Button btOK = new Button("ȷ��");
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
		cbAccountSelection.getSelectionModel().select(0);//Ĭ��ѡ���һ�����п�
		
		txtMoney.setFont(Font.font("Times", 20));
		txtMoney.setFill(Color.CORNFLOWERBLUE);
		
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

	private void queryDetail() {
		if(user.getAccounts().size() == 0) {//�û�û�����п�
			spCenter.getChildren().clear(); //�����һ�β����Ľ���
			
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("������ʾ");
			alert.setContentText("��û�����п��˻�����������п����ѯ��");
			alert.showAndWait();
			
			return ;
		}
		
		Text txtAccountSelection = new Text("ѡ���˻���");
		ComboBox<String> cbAccountSelection = new ComboBox<String>();
		Button btOK = new Button("ȷ��");
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
		cbAccountSelection.getSelectionModel().select(0);//Ĭ��ѡ���һ�����п�
		
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

	public static void main(String[] args) {
		launch(args);
	}
}
