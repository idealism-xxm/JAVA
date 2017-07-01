package gui;

import bean.ValueBean;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lexer.Lexer;
import parser.Parser;

import java.util.ArrayList;

/**
 * Created by idealism on 2017/6/18.
 */
public class Calculator extends Application{

    Text txtResult = new Text("结果");
    private TextArea taResult = new TextArea();
    private VBox vbTop = new VBox(txtResult, taResult);

    Text txtInput = new Text("输入");
    private TextField tfInput = new TextField();
    private VBox vbBottom = new VBox(txtInput, tfInput);

    private VBox vbLeft = new VBox(vbTop, vbBottom);

    Text txtValueBean = new Text("变量表");
    private TableColumn tcName = new TableColumn("变量名");
    private TableColumn tcType = new TableColumn("类型");
    private TableColumn tcValue = new TableColumn("值");
    private ArrayList<ValueBean> valueBeans = new ArrayList<ValueBean>();
    TableView<ValueBean> tvValueBean = new TableView<ValueBean>();
    private VBox vbRight = new VBox(txtValueBean, tvValueBean);

    private HBox hBox = new HBox(vbLeft, vbRight);

    Lexer lexer = new Lexer();
    Parser parser = new Parser(lexer);

    @Override
    public void start(Stage primaryStage) throws Exception {

        //绑定列和其值域
        tcName.setMinWidth(40);
        tcName.setCellValueFactory(new PropertyValueFactory("name"));
        tcType.setMinWidth(40);
        tcType.setCellValueFactory(new PropertyValueFactory("type"));
        tcValue.setMinWidth(40);
        tcValue.setCellValueFactory(new PropertyValueFactory("value"));

        //将列和列表绑定并
        tvValueBean.getColumns().addAll(tcName, tcType, tcValue);

        //设置结果部分
        taResult.setEditable(false); //结果部分不可编辑
        taResult.setMinHeight(300);

        //设置输入部分的动作
        tfInput.setOnKeyPressed(tfInputEvent -> {
            //如果用户按下了 Enter 键，且输入不为空
            if(tfInputEvent.getCode().equals(KeyCode.ENTER) && !"".equals(tfInput.getText())) {
                handleInput();
                //传入属性值
                tvValueBean.setItems(FXCollections.observableArrayList(parser.getValueBeans()));
            }
        });

        //设置各部分说明文本的间隔
        VBox.setMargin(txtResult, new Insets(5, 5, 5, 5));
        VBox.setMargin(txtValueBean, new Insets(5, 5, 5, 5));
        VBox.setMargin(txtInput, new Insets(5, 5, 5, 5));

        //设置左右两个部分的间隔
        HBox.setMargin(vbLeft, new Insets(5, 5, 5, 5));
        HBox.setMargin(vbRight, new Insets(5, 5, 5, 5));

        Scene scene = new Scene(hBox, 705, 371);

        primaryStage.setResizable(false);
        primaryStage.setTitle("计算器"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        tfInput.requestFocus(); //输入部分获得焦点
    }

    private void handleInput() {
        String src = tfInput.getText(); //获取输入串
        taResult.appendText(">>\t" + src + "\n");
        parser.setSrc(src);
        try {
            String ans = parser.stmt();
            taResult.appendText("\tans =\n\t\t  " + ans + "\n\n");
        }
        catch (Exception | Error e) {
            taResult.appendText("\tERROR: " + e.getMessage() + "\n\n");
        }

        tfInput.setText(""); //清空输入
    }
}
