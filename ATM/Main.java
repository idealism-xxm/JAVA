package ATM;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		new Login_GUI().start(primaryStage);//���е�¼����
	}
	
	public static void main(String[] args) {
		//�Ƚ��г�ʼ������ȷ�����ļ�������
		Account.init();
		User.init();
		Administrator.init();
		Detail.init();
		//����������
		launch(args);
	}
	
}