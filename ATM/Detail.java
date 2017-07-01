package ATM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

//ֻ��¼��ȡ��ͽ��׵���ϸ
public class Detail {
	private int type = Type.DEPOSIT;
	private String srcNumber = null;
	private String desNumber = null;
	private double money = 0.0;
	private Date date;
	
	private static final String TYPE[] = {"���", "ȡ��", "ת��"};
	
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
		return      "�������ͣ�" + TYPE[type] + 
				"    Դ�˻���" + srcNumber + 
				"    Ŀ���˻���" + desNumber + 
				"    ��" + String.format("%.2f", money) + 
				"    ʱ�䣺" + date;
	}
	
	/*
	 * ����һ����ϸ
	 */
	public static Detail creatDetail(int type, String srcNumber,String desNumber, double money, Date date) {
		return new Detail(type, srcNumber, desNumber, money, date);
	}
	
	/*
	 * �����ݿ���׷����ϸ
	 */
	public static void appendDetail(Detail detail) {
		try {
			File src = new File(Filename.DETAIL);
			PrintWriter output = new PrintWriter(new FileWriter(src, true));//true��ʾ׷��ģʽ
			
			output.println(detail.type + " " + detail.srcNumber + " "
					+ detail.desNumber + " " + detail.money + " "
					+ detail.date.getTime());//׷���û���Ϣ
			//�������println�����ܼ���"\n"����Ҫ��ȡϵͳ�Ļ��з������
			
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
		//����try�Զ��ر��ļ�
		try(Scanner input = new Scanner(new File(Filename.DETAIL))) {
			while(input.hasNextLine()) {//����ȫ���ļ�
				String[] detailInfo = input.nextLine().split("[ ]");
				if(detailInfo[1].equals(number) || detailInfo[2].equals(number)) {//���������ϸ��number�й�
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
		if(!file.exists()) {//����ļ������ڣ��򴴽��ļ�
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}