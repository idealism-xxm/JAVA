package ATM;

import java.security.MessageDigest;

public class MD5 {
	public static String getMD5(String s) {//����MD5����ʮ�����Ʊ�ʾ
		try {
			StringBuilder sBuilder = new StringBuilder();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(s.getBytes());
			byte[] result = md5.digest();
			for(int i = 0; i < result.length; ++i) {
				int tmp = result[i];
				if(tmp < 0) {
					tmp += 256;
				}
				if(tmp < 16) {//�����һ�ֽڵ���С��16��Ϊ��ʹʮ��������������2λ��ʾ����λ��0
					sBuilder.append("0");
				}
				sBuilder.append(Integer.toHexString(tmp));
			}
			return sBuilder.toString().toUpperCase();
		}
		catch(Exception e) {
			return null;
		}
	}
}
