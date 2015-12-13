import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener;

/*
 * 未设置不同数字的大小
 * 未设置实时显示分数
 * ……总之就是最简易的版本
 */

public class Game extends JFrame {
	private final int LEN=100,MAXN=4;//LEN是一个方块的边长，MAXN是方阵的阶数
	private JLabel[][] jlb=new JLabel[MAXN][MAXN];
	private int num[][]=new int[MAXN][MAXN];
	private int filled,score;
	
	public Game() {
		getContentPane().setBackground(new Color(187,173,160));//【注意】JFrame应该这样改变颜色
		setLayout(new GridLayout(MAXN,MAXN,10,10));
		for(int i=0;i<MAXN;++i) {
			for(int j=0;j<MAXN;++j) {
				num[i][j]=0;
				add(jlb[i][j]=new JLabel());
				jlb[i][j].setBackground(getBkgColor(0));
				jlb[i][j].setForeground(getFrgColor(0));
				jlb[i][j].setOpaque(true);//【注意】JLabel默认情况下是透明，所以只能先将其设成不透明才能显示颜色
				jlb[i][j].setText(null);
				jlb[i][j].setHorizontalAlignment(JLabel.CENTER);//居中显示
			}
		}
		
		addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				
				case KeyEvent.VK_LEFT: {
					if(moveLeft()) {
						next();
						display();
						if(!canMove()) {
							JOptionPane.showMessageDialog(null,"Score:"+score, "Game Over", JOptionPane.PLAIN_MESSAGE, null); 
							reset();
						}
					}
					return;
				}
				case KeyEvent.VK_UP: {
					turnClockwise(3);
					if(moveLeft()) {
						turnClockwise(1);
						next();
						display();
						if(!canMove()) {
							JOptionPane.showMessageDialog(null,"Score:"+score, "Game Over", JOptionPane.PLAIN_MESSAGE, null);
							reset();
						}
					}
					else {
						turnClockwise(1);
					}
					return;
				}
				case KeyEvent.VK_RIGHT: {
					turnClockwise(2);
					if(moveLeft()) {
						turnClockwise(2);
						next();
						display();
						if(!canMove()) {
							JOptionPane.showMessageDialog(null,"Score:"+score, "Game Over", JOptionPane.PLAIN_MESSAGE, null);
							reset();
						}
					}
					else {
						turnClockwise(2);
					}
					return;
				}
				case KeyEvent.VK_DOWN: {
					turnClockwise(1);
					if(moveLeft()) {
						turnClockwise(3);
						next();
						display();
						if(!canMove()) {
							JOptionPane.showMessageDialog(null,"Score:"+score, "Game Over", JOptionPane.PLAIN_MESSAGE, null);
							reset();
						}
					}
					else {
						turnClockwise(3);
					}
					return;
				}
				
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		});
		reset();
		setTitle(""+2048);
		setSize(LEN*MAXN+10,LEN*MAXN+10);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	private void reset() {
		score=filled=0;
		for(int r=0;r<MAXN;++r)
			for(int c=0;c<MAXN;++c)
				num[r][c]=0;
		next();
		next();
		display();
	}
	
	private boolean canMove() {//判断能否继续移动
		if(filled!=MAXN*MAXN)
			return true;
		int ori[][]=new int[4][4],tmp=filled,temp=score;
		myCopy(num,ori);
		if(!moveLeft()) {//无法向左移动
			myCopy(ori,num);
			turnClockwise(3);
			if(!moveLeft()) {//无法向上移动
				myCopy(ori,num);
				turnClockwise(2);
				if(!moveLeft()) {//无法向右移动
					myCopy(ori,num);
					turnClockwise(1);
					if(!moveLeft()) {//无法向下移动
						myCopy(ori,num);
						return false;
					}
				}
			}
		}
		filled=tmp;
		score=temp;
		myCopy(ori,num);
		return true;
	}
	
	private void display() {//将数字与JLabel对应
		for(int i=0;i<MAXN;++i)
			for(int j=0;j<MAXN;++j) {
				jlb[i][j].setText(Integer.toString(num[i][j]));
				jlb[i][j].setBackground(getBkgColor(num[i][j]));
				jlb[i][j].setForeground(getFrgColor(num[i][j]));
			}
	}
	
	private boolean moveLeft() {//按下左箭头时，内部矩阵的计算
		boolean moved=false;
		for(int r=0;r<MAXN;++r)
			if(moveRow(r))
				moved=true;
		return moved;
	}
	
	private boolean moveRow(int r) {//按下左箭头时，内部矩阵第r行的计算
		boolean moved=fillUp(r);
		for(int c=1;c<MAXN;++c) {//合并
			if(num[r][c]==num[r][c-1]&&num[r][c]!=0) {
				--filled;
				moved=true;
				num[r][c-1]<<=1;
				num[r][c]=0;
				score+=num[r][c-1];
			}
		}
		return fillUp(r)||moved;
	}
	
	private boolean fillUp(int r) {//当前行向左移动
		int c=0,t;
		boolean moved=false;
		while(c<MAXN&&num[r][c]!=0)//找到第一个空位
			++c;
		t=c;
		for(;t<MAXN;++t) {//补齐空位
			if(num[r][t]!=0) {
				moved=true;
				num[r][c++]=num[r][t];
			}
		}
		for(;c<MAXN;++c)
			if(num[r][c]!=0) {//若c~MAXN-1存在非0方块，则必定产生移动
				moved=true;
				num[r][c]=0;
			}
		return moved;
	}
	
	private void next() {//填入一个方块2或4
		++filled;
		int r=((int)(Math.random()*MAXN))%MAXN,c=((int)(Math.random()*MAXN))%MAXN,n=((int)(Math.random()*2))%2;
		while(num[r][c]!=0) {
			r=((int)(Math.random()*MAXN))%MAXN;
			c=((int)(Math.random()*MAXN))%MAXN;
		}
		num[r][c]=2<<n;
	}
	
	private void turnClockwise(int times) {//将矩阵顺时针旋转times*90°
		times%=4;
		while((times--)>0) {
			int tmp[][]=new int[MAXN][MAXN],r,c,t;
			myCopy(num,tmp);
			for(r=0;r<MAXN;++r)//顺时针旋转90°
				for(c=0,t=MAXN-1;c<MAXN;++c,--t)
						num[r][c]=tmp[t][r];
		}
	}
	
	private void myCopy(int[][] src,int[][] dest) {
		for(int r=0;r<MAXN;++r)
			for(int c=0;c<MAXN;++c)
				dest[r][c]=src[r][c];
	}
	
	private Color getBkgColor(int num) {
		switch(num) {
		case 0:
			return new Color(205,193,180);
		case 2:
			return new Color(238,228,218);
		case 4:
			return new Color(224,237,200);
		case 8:
			return new Color(242,117,121);
		case 16:
			return new Color(249,149,99);
		case 32:
			return new Color(246,124,95);
		case 64:
			return new Color(246,94,59);
		case 128:
			return new Color(237,217,114);
		case 256:
			return new Color(237,204,97);
		case 512:  
			return new Color(236,176,77);   
		case 1024:  
			return new Color(236,148,55);
		case 2048:  
			return new Color(236,120,33);

		}
		return new Color(236,120,33);
	}
	
	private Color getFrgColor(int num) {
		switch(num) {
		case 0:
			return new Color(205,193,180);
		case 2:
		case 4:
			return new Color(119,110,101);
		case 8:
		case 16:
		case 32:
		case 64:
		case 128:
		case 256:
			return new Color(249,246,242);
		}
		return new Color(0,0,0);
	}
	
	public static void main(String[] argv) {
		new Game();
	}
}
