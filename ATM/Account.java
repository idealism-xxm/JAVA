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
	private static final String PREFIX = "62226002";//银行卡号前缀
	
	private Account() {}
	
	private Account(String cardNumber, String passwordMD5, String username, double money, Date created) {
		this.cardNumber = cardNumber;
		this.passwordMD5 = passwordMD5;
		this.username = username;
		this.money = money;
		this.created = (Date) (created.clone()); //深拷贝
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
		return (Date) (created.clone()); //深拷贝
	}

	public boolean isPasswordCorrect(String passwordMD5) {
		return this.passwordMD5.equals(passwordMD5);
	}
	
	public boolean draw(double money) {
		if(money + EPS < 0) {//要取的钱小于0，则不合法
			return false;
		}
		if(this.money + EPS < money) {//余额不足
			return false;
		}
		
		setMoney(getMoney() - money);
		save();
		Detail.appendDetail(Detail.creatDetail(Detail.Type.DRAW, cardNumber, "无", money, new Date( System.currentTimeMillis())));
		return true;
	}
	
	public boolean deposit(double money) {
		if(money + EPS < 0) {//要存的钱小于0，则不合法
			return false;
		}
		
		setMoney(getMoney() + money);
		save();
		Detail.appendDetail(Detail.creatDetail(Detail.Type.DEPOSIT, cardNumber, "无", money, new Date( System.currentTimeMillis())));
		return true;
	}
	
	/*
	 * 返回值说明：
	 * 			0 表示转账成功
	 * 			1 表示目标帐号不存在
	 * 			2 表示余额不足
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
	 * 保存账户信息
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
				
				if(cardNumber.equals(tmpAccount.cardNumber)) {//如果帐号相等，则要保存的就是本帐号的信息
					tmpAccount = this;//将更新后的对象信息赋给tmpAccount
				}
				
				//输出帐号信息
				output.println(tmpAccount.cardNumber + " " + tmpAccount.passwordMD5 + " " + 
						tmpAccount.username + " " + tmpAccount.money + " " + 
						tmpAccount.created.getTime());//如果不用println，不能简单用"\n"，需要读取系统的换行符并输出
			}
			
			input.close();
			output.close();
			//对文件进行操作时，必须先关闭输入输出的绑定
			src.delete();
			tmp.renameTo(src);//将临时文件改名为原文件名
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void appendAccount(Account account) {//添加账户
		try {
			File src = new File(Filename.ACCOUNT);
			PrintWriter output = new PrintWriter(new FileWriter(src, true));//true表示追加模式
			
			//输出帐号信息
			output.println(account.cardNumber + " " + account.passwordMD5 + " " + 
					account.username + " " + account.money + " " + 
					account.created.getTime());//如果不用println，不能简单用"\n"，需要读取系统的换行符并输出
			
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isCardNumber(String number) {
		//第一行正则表达式匹配精确的8位数，表示卡的固定前缀
		//第二行正则表达式匹配随机的数字(8位数字)
		return number.matches(PREFIX + 
							  "[0-9]{8}");
	}
	
	public static boolean exists(String number) throws FileNotFoundException {
		if (!isCardNumber(number)) {
			return false;
		}
		//利用try自动关闭文件
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
	 * 创建一个新的账户，随机生成卡号
	 */
	public static Account creatAccount(String passwordMD5, String username) throws FileNotFoundException {
		String number = PREFIX;
		do {
			for(int i = 0; i < 8; ++i) {//随机添加8个数字
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

		//利用try自动关闭文件
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
		//利用try自动关闭文件
		try(Scanner input = new Scanner(new File(Filename.ACCOUNT))) {
			while(input.hasNextLine()) {
				cardNumbers.add(input.nextLine().split("[ ]")[0]);
			}
		}
		return cardNumbers;
	}
	
	public static void init() {//程序运行时初始化
		File file = new File(Filename.ACCOUNT);
		if(!file.exists()) {//如果文件不存在，则创建文件
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
