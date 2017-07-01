package ATM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Account {
	private String cardNumber = null;
	private String passwordMD5 = null;
	private String username = null;
	private double money = 0.0;
	private Date created;
	
	public static final double EPS = 1e-14;
	private static final String PREFIX = "62226002";//���п���ǰ׺
	
	private Account() {}
	
	private Account(String cardNumber, String passwordMD5, String username, double money, Date created) {
		this.cardNumber = cardNumber;
		this.passwordMD5 = passwordMD5;
		this.username = username;
		this.money = money;
		this.created = (Date) (created.clone()); //���
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getPasswordMD5() {
		return passwordMD5;
	}

	public void setPasswordMD5(String password) {
		this.passwordMD5 = password;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}
	
	public Date getCreated() {
		return (Date) (created.clone()); //���
	}

	public boolean isPasswordCorrect(String passwordMD5) {
		return this.passwordMD5.equals(passwordMD5);
	}
	
	public boolean draw(double money) {
		if(money + EPS < 0) {//Ҫȡ��ǮС��0���򲻺Ϸ�
			return false;
		}
		if(this.money + EPS < money) {//����
			return false;
		}
		
		setMoney(getMoney() - money);
		save();
		Detail.appendDetail(Detail.creatDetail(Detail.Type.DRAW, cardNumber, "��", money, new Date( System.currentTimeMillis())));
		return true;
	}
	
	public boolean deposit(double money) {
		if(money + EPS < 0) {//Ҫ���ǮС��0���򲻺Ϸ�
			return false;
		}
		
		setMoney(getMoney() + money);
		save();
		Detail.appendDetail(Detail.creatDetail(Detail.Type.DEPOSIT, cardNumber, "��", money, new Date( System.currentTimeMillis())));
		return true;
	}
	
	/*
	 * ����ֵ˵����
	 * 			0 ��ʾת�˳ɹ�
	 * 			1 ��ʾĿ���ʺŲ�����
	 * 			2 ��ʾ����
	 */
	public int transfer(String number, double money) throws FileNotFoundException {
		if(!exists(number)) {
			return 1;
		}
		if(this.money + EPS < money) {
			return 2;
		}
		Account goal = getAccount(number);
		setMoney(getMoney() - money);
		goal.setMoney(goal.getMoney() + money);
		save();
		goal.save();
		
		Detail.appendDetail(Detail.creatDetail(Detail.Type.TRANSFER, cardNumber, number, money, new Date( System.currentTimeMillis())));
		return 0;
	}
	
	/*
	 * �����˻���Ϣ
	 */
	public void save() {
		try {
			File src = new File(Filename.ACCOUNT);
			File tmp = File.createTempFile(Filename.ACCOUNT, ".tmp");
			
			Scanner input = new Scanner(src);
			PrintWriter output = new PrintWriter(tmp);
			
			while (input.hasNext()) {
				Account tmpAccount = new Account();
				tmpAccount.cardNumber = input.next();
				tmpAccount.passwordMD5 = input.next();
				tmpAccount.username = input.next();
				tmpAccount.money = input.nextDouble();
				tmpAccount.created = new Date(input.nextLong());
				
				if(cardNumber.equals(tmpAccount.cardNumber)) {//����ʺ���ȣ���Ҫ����ľ��Ǳ��ʺŵ���Ϣ
					tmpAccount = this;//�����º�Ķ�����Ϣ����tmpAccount
				}
				
				//����ʺ���Ϣ
				output.println(tmpAccount.cardNumber + " " + tmpAccount.passwordMD5 + " " + 
						tmpAccount.username + " " + tmpAccount.money + " " + 
						tmpAccount.created.getTime());//�������println�����ܼ���"\n"����Ҫ��ȡϵͳ�Ļ��з������
			}
			
			input.close();
			output.close();
			//���ļ����в���ʱ�������ȹر���������İ�
			src.delete();
			tmp.renameTo(src);//����ʱ�ļ�����Ϊԭ�ļ���
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void appendAccount(Account account) {//����˻�
		try {
			File src = new File(Filename.ACCOUNT);
			PrintWriter output = new PrintWriter(new FileWriter(src, true));//true��ʾ׷��ģʽ
			
			//����ʺ���Ϣ
			output.println(account.cardNumber + " " + account.passwordMD5 + " " + 
					account.username + " " + account.money + " " + 
					account.created.getTime());//�������println�����ܼ���"\n"����Ҫ��ȡϵͳ�Ļ��з������
			
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isCardNumber(String number) {
		//��һ��������ʽƥ�侫ȷ��8λ������ʾ���Ĺ̶�ǰ׺
		//�ڶ���������ʽƥ�����������(8λ����)
		return number.matches(PREFIX + 
							  "[0-9]{8}");
	}
	
	public static boolean exists(String number) throws FileNotFoundException {
		if (!isCardNumber(number)) {
			return false;
		}
		//����try�Զ��ر��ļ�
		try(Scanner input = new Scanner(new File(Filename.ACCOUNT))) {
			while(input.hasNextLine()) {
				String[] accountInfo = input.nextLine().split("[ ]");
				if(accountInfo[0].equals(number)) {
					return true;
				}
			}
			return false;
		}
	}
	
	/*
	 * ����һ���µ��˻���������ɿ���
	 */
	public static Account creatAccount(String passwordMD5, String username) throws FileNotFoundException {
		String number = PREFIX;
		do {
			for(int i = 0; i < 8; ++i) {//������8������
				number += (int) (Math.random() * 10);
			}
		} while(!isCardNumber(number) || exists(number));
		return new Account(number, 
						   passwordMD5, 
						   username, 
						   0.0, 
						   new Date(System.currentTimeMillis()));
	}
	
	public static Account getAccount(String number) throws FileNotFoundException {
		if (!isCardNumber(number)) {
			return null;
		}

		//����try�Զ��ر��ļ�
		try(Scanner input = new Scanner(new File(Filename.ACCOUNT))) {
			while(input.hasNextLine()) {
				String[] accountInfo = input.nextLine().split("[ ]");
				if(accountInfo[0].equals(number)) {
					return new Account(accountInfo[0], 
									   accountInfo[1], 
									   accountInfo[2], 
									   Double.parseDouble(accountInfo[3]), 
									   new Date(Long.parseLong(accountInfo[4])));
				}
			}
			return null;
		}
	}
	
	public static ArrayList<String> getAllCardNumbers() throws FileNotFoundException {
		ArrayList<String> cardNumbers = new ArrayList<String>();
		//����try�Զ��ر��ļ�
		try(Scanner input = new Scanner(new File(Filename.ACCOUNT))) {
			while(input.hasNextLine()) {
				cardNumbers.add(input.nextLine().split("[ ]")[0]);
			}
		}
		return cardNumbers;
	}
	
	public static void init() {//��������ʱ��ʼ��
		File file = new File(Filename.ACCOUNT);
		if(!file.exists()) {//����ļ������ڣ��򴴽��ļ�
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
