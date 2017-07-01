package ATM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

//只记录存款，取款和交易的明细
public class Detail {
	private int type = Type.DEPOSIT;
	private String srcNumber = null;
	private String desNumber = null;
	private double money = 0.0;
	private Date date;
	
	private static final String TYPE[] = {"存款", "取款", "转账"};
	
	public static class Type {
		public static final int DEPOSIT = 0;
		public static final int DRAW = 1;
		public static final int TRANSFER = 2;
	}
	
	private Detail(int type, String srcNumber,String desNumber, double money, Date date) {
		this.type = type;
		this.srcNumber = srcNumber;
		this.desNumber = desNumber;
		this.money = money;
		this.date = date;
	}
	
	public int getType() {
		return type;
	}
	
	public String getTypeName() {
		return TYPE[type];
	}

	public String getSrcNumber() {
		return srcNumber;
	}

	public String getDesNumber() {
		return desNumber;
	}

	public double getMoney() {
		return money;
	}

	public Date getDate() {
		return date;
	}
	
	@Override
	public String toString() {
		return      "操作类型：" + TYPE[type] + 
				"    源账户：" + srcNumber + 
				"    目的账户：" + desNumber + 
				"    金额：" + String.format("%.2f", money) + 
				"    时间：" + date;
	}
	
	/*
	 * 创建一条明细
	 */
	public static Detail creatDetail(int type, String srcNumber,String desNumber, double money, Date date) {
		return new Detail(type, srcNumber, desNumber, money, date);
	}
	
	/*
	 * 向数据库中追加明细
	 */
	public static void appendDetail(Detail detail) {
		try {
			File src = new File(Filename.DETAIL);
			PrintWriter output = new PrintWriter(new FileWriter(src, true));//true表示追加模式
			
			output.println(detail.type + " " + detail.srcNumber + " "
					+ detail.desNumber + " " + detail.money + " "
					+ detail.date.getTime());//追加用户信息
			//如果不用println，不能简单用"\n"，需要读取系统的换行符并输出
			
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Detail> getDetail(String number) throws FileNotFoundException {
		ArrayList<Detail> details = new ArrayList<Detail>();
		if (!Account.isCardNumber(number)) {
			return null;
		}
		//利用try自动关闭文件
		try(Scanner input = new Scanner(new File(Filename.DETAIL))) {
			while(input.hasNextLine()) {//读完全部文件
				String[] detailInfo = input.nextLine().split("[ ]");
				if(detailInfo[1].equals(number) || detailInfo[2].equals(number)) {//如果该条明细和number有关
					details.add(new Detail(Integer.parseInt(detailInfo[0]), 
										   detailInfo[1], 
										   detailInfo[2], 
										   Double.parseDouble(detailInfo[3]), 
										   new Date(Long.parseLong(detailInfo[4]))));
				}
			}
			return details;
		}
	}
	
	public static void init() {
		File file = new File(Filename.DETAIL);
		if(!file.exists()) {//如果文件不存在，则创建文件
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}