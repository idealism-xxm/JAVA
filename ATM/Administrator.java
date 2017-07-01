package ATM;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

public class Administrator {
	private String adminName;
	private String passwordMD5; //管理员密码
	private Date created; //管理员注册日期
	
	private Administrator() {}
	
	private Administrator(String adminName, String passwordMD5, Date created) {
		this.adminName = adminName;
		this.passwordMD5 = passwordMD5;
		this.created = (Date) (created.clone()); //深拷贝
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getPasswordMD5() {
		return passwordMD5;
	}

	public void setPasswordMD5(String passwordMD5) {
		this.passwordMD5 = passwordMD5;
	}

	public Date getCreated() {
		return (Date) (created.clone()); //深拷贝;
	}

	public boolean isPasswordCorrect(String passwordMD5) {
		return this.passwordMD5.equals(passwordMD5);
	}
	
	public void save() {
		try {
			File src = new File(Filename.ADMINISTRATOR);
			File tmp = File.createTempFile(Filename.ADMINISTRATOR, ".tmp");
			
			Scanner input = new Scanner(src);
			PrintWriter output = new PrintWriter(tmp);
			
			while (input.hasNext()) {
				Administrator tmpAdmin = new Administrator();
				tmpAdmin.adminName = input.next();
				tmpAdmin.passwordMD5 = input.next();
				tmpAdmin.created = new Date(input.nextLong());
				
				if(adminName.equals(tmpAdmin.adminName)) {//如果管理员名相当，则要保存的就是本用户的信息
					tmpAdmin = this;//将更新后的对象信息赋给tmpAdmin
				}
				
				//输出管理员信息
				output.println(tmpAdmin.adminName + " " + tmpAdmin.passwordMD5 + " "
						+ tmpAdmin.created.getTime());//如果不用println，不能简单用"\n"，需要读取系统的换行符并输出
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
	
	public static void appendAdmin(Administrator admin) {//追加管理员
		try {
			File src = new File(Filename.ADMINISTRATOR);
			PrintWriter printWriter = new PrintWriter(new FileWriter(src, true));//true表示追加模式
			
			printWriter.println(admin.adminName + " " + admin.passwordMD5 + " "
					+ admin.created.getTime());//追加管理员信息
			
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Administrator creatAdministrator(String name, String passwordMD5) {
		return new Administrator(name, passwordMD5, new Date(System.currentTimeMillis()));
	}
	
	public static boolean exists(String name) throws FileNotFoundException {
		//利用try自动关闭文件
		try(Scanner input = new Scanner(new File(Filename.ADMINISTRATOR))) {
			while(input.hasNextLine()) {
				String[] adminInfo = input.nextLine().split("[ ]");
				if(adminInfo[0].equals(name)) {
					return true;
				}
			}
			return false;
		}
	}
	
	public static Administrator getAdministrator(String name) throws FileNotFoundException {
		//利用try自动关闭文件
		try(Scanner input = new Scanner(new File(Filename.ADMINISTRATOR))) {
			while(input.hasNextLine()) {
				String[] adminInfo = input.nextLine().split("[ ]");
				if(adminInfo[0].equals(name)) {
					return new Administrator(adminInfo[0], 
											 adminInfo[1], 
											 new Date(Long.parseLong(adminInfo[2])));
				}
			}
			return null;
		}
	}
	
	public static void init() {
		File file = new File(Filename.ADMINISTRATOR);
		if(!file.exists()) {//如果文件不存在，则创建文件
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
