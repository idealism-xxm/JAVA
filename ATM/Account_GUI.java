package ATM;

import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Account_GUI extends Application {
	private Account account = null;
	
	private Text txtAccountInfo = null;
	private BorderPane borderPane =  new BorderPane();
	private StackPane spCenter = new StackPane();
	private VBox vbLeft = new VBox();
	private VBox vbRight = new VBox();
	private Button btQueryMoney = new Button("��ѯ���");
	private Button btQueryDetail = new Button("��ѯ��ϸ");
	private Button btDeposit = new Button("��      ��");
	private Button btDraw = new Button("ȡ      ��");
	private Button btTransfer = new Button("ת      ��");
	private Button btExit = new Button("��      ��");
	
	public Account_GUI(Account account) {
		this.account = account;
		txtAccountInfo = new Text("���ţ�" + account.getCardNumber() +
								  "\t�ֿ��ˣ�" + account.getUserName());
	}

	@Override
	public void start(Stage primaryStage) {
		
		btQueryMoney.setOnAction(e -> {
			try {
				queryMoney(spCenter, account.getCardNumber());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		btQueryDetail.setOnAction(e -> {
			queryDetail(spCenter, account.getCardNumber());
		});
		btExit.setOnAction(e -> {
			closeInfo();
			primaryStage.close();
		});
		btDeposit.setOnAction(e -> deposit());
		btDraw.setOnAction(e -> draw());
		btTransfer.setOnAction(e -> transfer());
		
		vbLeft.getChildren().addAll(btQueryMoney, btQueryDetail, btExit);
		VBox.setMargin(btQueryMoney, new Insets(40, 5, 25, 5));
		VBox.setMargin(btQueryDetail, new Insets(25, 5, 25, 5));
		VBox.setMargin(btExit, new Insets(25, 5, 5, 5));
		
		
		vbRight.getChildren().addAll(btDeposit, btDraw, btTransfer);
		VBox.setMargin(btDeposit, new Insets(40, 5, 25, 5));
		VBox.setMargin(btDraw, new Insets(25, 5, 25, 5));
		VBox.setMargin(btTransfer, new Insets(25, 5, 5, 5));
		
		borderPane.setTop(txtAccountInfo);
		borderPane.setLeft(vbLeft);
		borderPane.setRight(vbRight);
		borderPane.setCenter(spCenter);
		
		
		
		BorderPane.setAlignment(txtAccountInfo, Pos.BOTTOM_CENTER);
		
		BorderPane.setMargin(txtAccountInfo, new Insets(5, 5, 5, 5));
		BorderPane.setMargin(vbLeft, new Insets(5, 5, 5, 5));
		BorderPane.setMargin(vbRight, new Insets(5, 5, 5, 5));
		
		Scene scene = new Scene(borderPane, 480, 320);
	    
		primaryStage.setOnCloseRequest(e -> closeInfo());
		primaryStage.setResizable(false);
	    primaryStage.setTitle("���˵�"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	}
	
	public static void queryMoney(Pane pane, String number) throws FileNotFoundException {
		Text txtMoney = new Text("��" + String.format("%.2f", Account.getAccount(number).getMoney()) + "Ԫ");
		txtMoney.setFont(Font.font("Times", 20));
		txtMoney.setFill(Color.CORNFLOWERBLUE);
		pane.getChildren().clear(); //�����һ�β����Ľ���
		pane.getChildren().add(txtMoney);
	}
	
	public static void queryDetail(Pane pane, String number) {
		pane.getChildren().clear(); //�����һ�β����Ľ���
		
		try {
			new Detail_GUI(Detail.getDetail(number)).start(new Stage());
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("������ʾ");
			alert.setContentText("����δ֪��������������");
			alert.showAndWait();
			
			e.printStackTrace();
		};
	}
	
	private void deposit() {
		Text txtMoney = new Text("����");
		TextField tfMoney = new TextField();
		Button btOK = new Button("ȷ��");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			try {
				double money = Double.parseDouble(tfMoney.getText());
				if(money + Account.EPS < 0) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("������ʾ");
					alert.setContentText("��������ȷ�Ľ�");
					alert.showAndWait();
					return ;
				}
				
				account.deposit(money);
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("��Ϣ��ʾ");
				alert.setContentText(String.format("%.2fԪ�ѳɹ������˻���", money));
				alert.showAndWait();
			}
			catch(Exception e1) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("������ʾ");
				alert.setContentText("��������ȷ�Ľ�");
				alert.showAndWait();
			}
		});
		
		hBox.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		
		hBox.getChildren().addAll(txtMoney, tfMoney);
		vBox.getChildren().addAll(hBox, btOK);
		HBox.setMargin(txtMoney, new Insets(5, 5, 5, 5));
		HBox.setMargin(tfMoney, new Insets(5, 5, 5, 5));
		VBox.setMargin(btOK, new Insets(5, 5, 5, 5));
		
		spCenter.getChildren().clear(); //�����һ�β����Ľ���
		spCenter.getChildren().add(vBox);
	}
	
	private void draw() {
		Text txtMoney = new Text("ȡ���");
		TextField tfMoney = new TextField();
		Button btOK = new Button("ȷ��");
		HBox hBox = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			try {
				double money = Double.parseDouble(tfMoney.getText());
				if(money + Account.EPS < 0) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("������ʾ");
					alert.setContentText("��������ȷ�Ľ�");
					alert.showAndWait();
					return ;
				}
				
				if(account.draw(money)) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("��Ϣ��ʾ");
					alert.setContentText(String.format("%.2fԪ�ѳɹ�ȡ���˻���", money));
					alert.showAndWait();
				}
				else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("��Ϣ��ʾ");
					alert.setContentText("ȡ��ʧ�ܣ�");
					alert.showAndWait();
				}
			}
			catch(Exception e1) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("������ʾ");
				alert.setContentText("��������ȷ�Ľ�");
				alert.showAndWait();
			}
		});
		
		hBox.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		
		hBox.getChildren().addAll(txtMoney, tfMoney);
		vBox.getChildren().addAll(hBox, btOK);
		HBox.setMargin(txtMoney, new Insets(5, 5, 5, 5));
		HBox.setMargin(tfMoney, new Insets(5, 5, 5, 5));
		VBox.setMargin(btOK, new Insets(5, 5, 5, 5));
		
		spCenter.getChildren().clear(); //�����һ�β����Ľ���
		spCenter.getChildren().add(vBox);
	}
	
	private void transfer() {
		Text txtGoalAccount = new Text("Ŀ���˻���");
		TextField tfGoalAccount = new TextField();
		Text txtMoney = new Text("ȡ���");
		TextField tfMoney = new TextField();
		Button btOK = new Button("ȷ��");
		HBox hbGoalAccount = new HBox();
		HBox hbMoney = new HBox();
		VBox vBox = new VBox();
		
		btOK.setOnAction(e -> {
			try {
				String goalAccountNumber = tfGoalAccount.getText();
				if(!Account.exists(goalAccountNumber)) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("������ʾ");
					alert.setContentText("����" + goalAccountNumber + "�����ڣ�����ϸ�˶ԣ�");
					alert.showAndWait();
					return ;
				}
				double money = Double.parseDouble(tfMoney.getText());
				if(money + Account.EPS < 0) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("������ʾ");
					alert.setContentText("��������ȷ�Ľ�");
					alert.showAndWait();
					return ;
				}
				
				account.transfer(goalAccountNumber, money);
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("��Ϣ��ʾ");
				alert.setContentText(String.format("%.2fԪ�ѳɹ�ת�����˻�%s��", money, goalAccountNumber));
				alert.showAndWait();
			}
			catch(Exception e1) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("������ʾ");
				alert.setContentText("��������ȷ�Ľ�");
				alert.showAndWait();
			}
		});
		
		hbGoalAccount.setAlignment(Pos.CENTER);
		hbMoney.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		
		hbGoalAccount.getChildren().addAll(txtGoalAccount, tfGoalAccount);
		hbMoney.getChildren().addAll(txtMoney, tfMoney);
		vBox.getChildren().addAll(hbGoalAccount, hbMoney, btOK);
		HBox.setMargin(txtGoalAccount, new Insets(5, 5, 5, 5));
		HBox.setMargin(tfGoalAccount, new Insets(5, 5, 5, 5));
		HBox.setMargin(txtMoney, new Insets(5, 5, 5, 5));
		HBox.setMargin(tfMoney, new Insets(5, 5, 5, 5));
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
