package ATM;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		new Login_GUI().start(primaryStage);//运行登录窗口
	}
	
	public static void main(String[] args) {
		//先进行初始化，以确保各文件均存在
		Account.init();
		User.init();
		Administrator.init();
		Detail.init();
		//运行主窗口
		launch(args);
	}
	
}