package ATM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class User {
	private String username;
	private String passwordMD5; //用户密码
	private Date created; //用户注册日期
	private ArrayList<String> accounts = new ArrayList<String>();
	
	private static final int MAX_ACCOUNT = 5; //一个用户最多持有5张银行卡
	
	private User() {}
	
	private User(String username, String passwordMD5, Date created, ArrayList<String> accounts) {
		this.username = username;
		this.passwordMD5 = passwordMD5;
		this.created = (Date) (created.clone()); //深拷贝
		this.accounts = accounts;
	}
	
	public String getUsername() {
		return username;
	}

	public void setName(String username) {
		this.username = username;
	}

	public String getPasswordMD5() {
		return passwordMD5;
	}

	public void setPasswordMD5(String passwordMD5) {
		this.passwordMD5 = passwordMD5;
	}

	public Date getCreated() {
		return (Date) (created.clone()); //深拷贝
	}

	public ArrayList<String> getAccounts() {
		return accounts;
	}

	public static int getMaxAccount() {
		return MAX_ACCOUNT;
	}

	public boolean addAccount(String cardNumber) {
		if(accounts.size() >= MAX_ACCOUNT) {
			return false;
		}
		accounts.add(cardNumber);
		return true;
	}
	
	public boolean isPasswordCorrect(String passwordMD5) {
		return this.passwordMD5.equals(passwordMD5);
	}
	
	public void save() {
		try {
			File src = new File(Filename.USER);
			File tmp = File.createTempFile(Filename.USER, ".tmp");
			
			Scanner input = new Scanner(src);
			PrintWriter output = new PrintWriter(tmp);
			
			while (input.hasNext()) {
				User tmpUser = new User();
				tmpUser.username = input.next();
				tmpUser.passwordMD5 = input.next();
				tmpUser.created = new Date(input.nextLong());
				for(int i = input.nextInt(); i > 0; --i) {
					String account = input.next();
					tmpUser.accounts.add(account);
				}
				if(username.equals(tmpUser.username)) {//如果用户名相等，则要保存的就是本用户的信息
					tmpUser = this;//将更新后的对象信息赋给tmpUser
				}
				
				//输出用户信息
				output.print(tmpUser.username + " " + tmpUser.passwordMD5 + " "
						+ tmpUser.created.getTime() + " " + tmpUser.accounts.size());
				for(int i = 0; i < tmpUser.accounts.size(); ++i) {
					output.print(" " + tmpUser.accounts.get(i));
				}
				output.println();//如果不用println，不能简单用"\n"，需要读取系统的换行符并输出
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
	
	public static void appendUser(User user) {//添加用户
		try {
			File src = new File(Filename.USER);
			PrintWriter output = new PrintWriter(new FileWriter(src, true));//true表示追加模式
			
			output.print(user.username + " " + user.passwordMD5 + " "
					+ user.created.getTime() + " " + user.accounts.size());//追加用户信息
			for(int i = 0; i < user.accounts.size(); ++i) {//追加银行卡号
				output.print(user.accounts.get(i));
			}
			output.println();//如果不用println，不能简单用"\n"，需要读取系统的换行符并输出
			
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static User creatUser(String name, String passwordMD5) {
		return new User(name, 
						passwordMD5, 
						new Date(System.currentTimeMillis()), 
						new ArrayList<String>());
	}
	
	public static boolean exists(String name) throws FileNotFoundException {
		//利用try自动关闭文件
		try(Scanner input = new Scanner(new File(Filename.USER))) {
			while(input.hasNextLine()) {
				String[] accountInfo = input.nextLine().split("[ ]");
				if(accountInfo[0].equals(name)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public static User getUser(String name) throws FileNotFoundException {
		//利用try自动关闭文件
		try(Scanner input = new Scanner(new File(Filename.USER))) {
			while(input.hasNextLine()) {
				String[] userInfo = input.nextLine().split("[ ]");
				if(userInfo[0].equals(name)) {
					ArrayList<String> accounts = new ArrayList<String>();
					for(int i = Integer.parseInt(userInfo[3]); i > 0; --i) {
						accounts.add(userInfo[3 + i]);
					}
					return new User(userInfo[0], 
									userInfo[1], 
									new Date(Long.parseLong(userInfo[2])), 
									accounts);
				}
			}
			return null;
		}
	}
	
	public static ArrayList<String> getAllUsernames() throws FileNotFoundException {
		ArrayList<String> usernames = new ArrayList<String>();
		//利用try自动关闭文件
		try(Scanner input = new Scanner(new File(Filename.USER))) {
			while(input.hasNextLine()) {
				usernames.add(input.nextLine().split("[ ]")[0]);
				
			}
		}
		return usernames;
	}
	
	public static void init() {
		File file = new File(Filename.USER);
		if(!file.exists()) {//如果文件不存在，则创建文件
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
