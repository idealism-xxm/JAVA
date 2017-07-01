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
	private String passwordMD5; //����Ա����
	private Date created; //����Աע������
	
	private Administrator() {}
	
	private Administrator(String adminName, String passwordMD5, Date created) {
		this.adminName = adminName;
		this.passwordMD5 = passwordMD5;
		this.created = (Date) (created.clone()); //���
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
		return (Date) (created.clone()); //���;
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
				
				if(adminName.equals(tmpAdmin.adminName)) {//�������Ա���൱����Ҫ����ľ��Ǳ��û�����Ϣ
					tmpAdmin = this;//�����º�Ķ�����Ϣ����tmpAdmin
				}
				
				//�������Ա��Ϣ
				output.println(tmpAdmin.adminName + " " + tmpAdmin.passwordMD5 + " "
						+ tmpAdmin.created.getTime());//�������println�����ܼ���"\n"����Ҫ��ȡϵͳ�Ļ��з������
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
	
	public static void appendAdmin(Administrator admin) {//׷�ӹ���Ա
		try {
			File src = new File(Filename.ADMINISTRATOR);
			PrintWriter printWriter = new PrintWriter(new FileWriter(src, true));//true��ʾ׷��ģʽ
			
			printWriter.println(admin.adminName + " " + admin.passwordMD5 + " "
					+ admin.created.getTime());//׷�ӹ���Ա��Ϣ
			
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Administrator creatAdministrator(String name, String passwordMD5) {
		return new Administrator(name, passwordMD5, new Date(System.currentTimeMillis()));
	}
	
	public static boolean exists(String name) throws FileNotFoundException {
		//����try�Զ��ر��ļ�
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
		//����try�Զ��ر��ļ�
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
		if(!file.exists()) {//����ļ������ڣ��򴴽��ļ�
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
