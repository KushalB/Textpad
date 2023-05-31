package com.pad.gui;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;
class Textpad{
	
	JFrame frame,aboutFrame;
	JMenuBar menuBar;
	JMenu menu,Tools,Credentials;
	JMenuItem clearAll,saveFile,openFile, saveAs,New,Credits,VersionInfo,fontPlus,fontMinus,undoClear,reload;
	JCheckBox chkwrap,bold,autoSave;
	JTextArea text;
	JScrollPane scroller;
	String flag,unClr;
	File file;
	int fontSize,bolder;
	Font ab;
	
	public static void main(String args[]){
		Textpad tp1 = new Textpad();
		tp1.build();
		
	}
	public void build(){
		ab = new Font("century gothic",0,25);
		chkwrap = new JCheckBox("Reading mode");
		bold = new JCheckBox("BOLD");
		autoSave = new JCheckBox("Auto Save");
		fontPlus = new JMenuItem("Font +");
		fontMinus = new JMenuItem("Font -");
		undoClear = new JMenuItem("Undo Clear");
		flag = "ON";
		frame=new JFrame("SoftPad");
		menuBar = new JMenuBar();
		menu = new JMenu("File");
		Credentials = new JMenu("Abouts");
		Tools = new JMenu("Tools");
		reload = new JMenuItem("Reload File");
		New = new JMenuItem("New file");
		clearAll = new JMenuItem("Clear");
		Credits = new JMenuItem("About Developer");
		VersionInfo = new JMenuItem("About SoftPad");
		saveFile = new JMenuItem("Save");
		openFile = new JMenuItem("Open file");
		saveAs = new JMenuItem("Save as");
		autoSave.setEnabled(false);
		saveFile.setEnabled(false);
		saveAs.setEnabled(false);
		reload.setEnabled(false);
		menu.add(saveFile);
		menu.add(saveAs);
		menu.add(New);
		menu.add(openFile);
		menu.add(reload);
		Credentials.add(Credits);
		Credentials.add(VersionInfo);
		Tools.add(autoSave);
		Tools.add(clearAll);
		Tools.add(undoClear);
		Tools.add(fontPlus);
		Tools.add(fontMinus);
		Tools.add(bold);
		Tools.add(chkwrap);
		undoClear.setEnabled(false);
		menuBar.add(menu);
		menuBar.add(Tools);
		menuBar.add(Credentials);
		frame.setJMenuBar(menuBar);
		frame.setResizable(false);
		text = new JTextArea();
		scroller = new JScrollPane(text);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		frame.setContentPane(scroller);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		try{
			/*File f = new File("C:/Temp/Sizeoftextpad.txt");
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);*/
			frame.setSize(500,500);
			/*FileReader loc = new FileReader(f);
			BufferedReader loca = new BufferedReader(loc);
			frame.setLocation((d.width-Integer.parseInt(loca.readLine()))/2,(d.height-Integer.parseInt(loca.readLine()))/2);
			f = new File("C:/Temp/fonts.txt");
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			fontSize = Integer.parseInt(br.readLine());
			//bolder = Integer.parseInt(br.readLine());*/
		}catch(Exception E){
			E.printStackTrace();
		}
		text.setFont(new Font("consolas",bolder,fontSize));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setIconImage(new ImageIcon("Textpad_ico.png").getImage());
		try{
			Thread T = new Thread(new Check());
			T.setName("Check Thread");
			T.start();
			//Thread T5 = new Thread(new Size());
			//T5.start();
		}catch(Exception E){
			E.printStackTrace();
		}
		registerToActionListeners();
	}
	public class Save implements Runnable{
		Save(File f){
			file=f;
		}
		public void run(){
			frame.setTitle(file.getAbsolutePath()+" - SoftPad");
			try{
				do{
					String saveText="";
					FileWriter fw = new FileWriter(file);
					BufferedWriter writer = new BufferedWriter(fw);
					String[] token = text.getText().split("\n");
					for(int i=0;i<token.length;i++){
						saveText=saveText+token[i]+"\r\n";
					}
					writer.write(saveText);
					writer.close();
					Thread.sleep(500);
				}while(flag=="OFF");
			}catch(Exception E){
					E.printStackTrace();
			}
		}
	}
	public void registerToActionListeners(){
		saveFile.addActionListener(new Savefile());
		autoSave.addItemListener(new Autosave());
		openFile.addActionListener(new Openfile());
		saveAs.addActionListener(new Saveas());
		New.addActionListener(new Newfile(true));
		clearAll.addActionListener(new Newfile(false));
		bold.addItemListener(new FontManagement());
		fontPlus.addActionListener(new FontManagement("+"));
		fontMinus.addActionListener(new FontManagement("-"));
		undoClear.addActionListener(new Undoclear());
		chkwrap.addItemListener(new Linewrap());
		reload.addActionListener(new Reload());
		Credits.addActionListener(new Credit());
		VersionInfo.addActionListener(new Version());
	}
	public class Linewrap implements ItemListener{
		public void itemStateChanged(ItemEvent ie){
			if(chkwrap.isSelected()){
				scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				text.setLineWrap(true);
				text.setWrapStyleWord(true);
			}
			else{
				scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
				text.setLineWrap(false);
				text.setWrapStyleWord(false);
			}
			}
	}
	public class Saveas extends Savefile{
		public void actionPerformed(ActionEvent AV){
			file=null;
			super.actionPerformed(AV);
		}
	}
	public class Savefile implements ActionListener{
		public void actionPerformed(ActionEvent S){
		Thread T2;
			saveAs.setEnabled(true);
			try{
			if(file==null){
				JFileChooser fileSave = new JFileChooser();
				fileSave.showSaveDialog(frame);
				T2 = new Thread(new Save(fileSave.getSelectedFile()));
			}else{
				T2 = new Thread(new Save(file));
			}
			T2.start();
			autoSave.setEnabled(true);
			reload.setEnabled(true);
			}catch(Exception E){
				E.printStackTrace();
			}
		}
	}
	public class Openfile implements ActionListener{
		public void actionPerformed(ActionEvent S){
				saveAs.setEnabled(true);
				autoSave.setEnabled(true);
				JFileChooser files = new JFileChooser();
				files.showOpenDialog(frame);
				File load = files.getSelectedFile();
				frame.setTitle(load.getAbsolutePath()+" - SoftPad");
				Thread T4 = new Thread(new Loadfile(load));
				T4.start();
				reload.setEnabled(true);
		}
	}
	public class Autosave implements ActionListener, ItemListener{
		public void actionPerformed(ActionEvent S){
			if(flag=="ON"){
					flag="OFF";
					saveFile.setEnabled(false);
					try{
						Thread T3 = new Thread(new Save(file));
						T3.start();
					}catch(Exception E){
						E.printStackTrace();
					}
			}else{
				flag="ON";	
			}
		}
		public void itemStateChanged(ItemEvent iv){
			if(autoSave.isSelected())
				flag="ON";
			else
				flag="OFF";
			actionPerformed(new ActionEvent(new Object(),0,"XYZ"));
		}	
	}
	public class Check implements Runnable{
		public void run(){
		String emptyOrNot="";
			while(true){
				if(text.getText().equals(emptyOrNot)){
					saveFile.setEnabled(false);
					autoSave.setEnabled(false);
					clearAll.setEnabled(false);
				}else{
					saveFile.setEnabled(true);
					clearAll.setEnabled(true);
				}
				try{
					Thread.sleep(1000);
				}catch(Exception E){
					E.printStackTrace();
				}
			}
		}
	}
	public class Loadfile implements Runnable{
	File load;
		Loadfile(File f){
			load=f;
			file=f;
		}
		public void run(){
			String line;
			String fl="";
				try{
					BufferedReader read = new BufferedReader(new FileReader(load));
					while((line=read.readLine())!=null){
						fl=fl+line+"\n";
					}
					read.close();
					text.setText(fl);
				}catch(Exception E){
					E.printStackTrace();
				}
		}
	}
	public class Newfile implements ActionListener{
		boolean flag1;
		Newfile(boolean b){
			flag1=b;
		}
		public void actionPerformed(ActionEvent E){
			if(flag1){
				file=null;
				text.setText("");
			}else{
				unClr = text.getText();
				text.setText("");
				undoClear.setEnabled(true);
			}
		}
	}
	public class Size implements Runnable{
		public void run(){
		while(true){
			try{
				File f = new File("C:/Temp/Sizeoftextpad.txt");
				FileWriter w = new FileWriter(f);
				BufferedWriter bw = new BufferedWriter(w);
				bw.write(frame.getWidth()+"\r\n"+frame.getHeight());
				bw.close();
				File f1 = new File("C:/Temp/fonts.txt");
				FileWriter w1 = new FileWriter(f1);
				BufferedWriter bw1 = new BufferedWriter(w1);
				bw1.write(fontSize+"\r\n");
				bw1.close();
				Thread.sleep(1000);
			}catch(Exception E){
				E.printStackTrace();
			}
		}
		}
	}
	public class FontManagement implements ActionListener, ItemListener{
		String fnt;
		FontManagement(){
		}
		FontManagement(String s){
			fnt = s;
		}
		public void actionPerformed(ActionEvent E){
			if(fnt=="+")
				fontSize++;
			else if(fnt=="-")
				fontSize--;
			text.setFont(new Font("consolas",bolder,fontSize));
		}
		public void itemStateChanged(ItemEvent iv){
			if(bold.isSelected())
				bolder=1;
			else
				bolder=0;
			text.setFont(new Font("consolas",bolder,fontSize));
		}
	}
	public class Undoclear implements ActionListener{
		public void actionPerformed(ActionEvent E){
			text.setText(unClr);
			undoClear.setEnabled(false);
		}	
	}
	public class Reload implements ActionListener{
		public void actionPerformed(ActionEvent E){
			try{
				Thread T7 = new Thread(new Loadfile(file));
				T7.start();
			}catch(Exception Ex){
				Ex.printStackTrace();
			}
		}
	}
	public class Credit implements ActionListener{
		public void actionPerformed(ActionEvent E){
			JOptionPane.showMessageDialog(null, "Developed by: Kushal Basu\nContact: kushalbasu2@gmail.com");
		}
	}
	public class Version implements ActionListener{
		public void actionPerformed(ActionEvent E){
			JOptionPane.showMessageDialog(null, "Released: 02-02-2016\nVersion: v.1.0.3");
		}
	}
}