package Manin;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.table.DefaultTableModel;

import Database.Database;

public class ReadFile {
	
	public Database db = new Database();
	private MyForm form;
	private File file;
	
	public ReadFile(MyForm instance){
		this.form = instance;
	}
		
		public void runReadFile(){
			try {
				BufferedReader br = new BufferedReader(new FileReader(form.file));
				br.readLine();
				br.readLine();
				br.readLine();//read first line to no read header column in .CSV
				String line =null;
				int column = form.dm.getColumnCount();
				int row = 0;
				while ((line = br.readLine()) != null) {
					String[] arr = line.split(",",-1);
					form.dm.addRow(new Object[0]);
					String day = arr[0];
					String month = arr[1];
					String year = arr[2];
					String time =arr[3];
					if(time.equals("")){
						form.dm.removeRow(row);
						break;
					}		
					if(day.equals("")&&month.equals("")&&year.equals("")){
						if(time.equals("")){
							break;
						}
						out.print("row");
						form.dm.setValueAt(form.dm.getValueAt(row-1, 0), row, 0);
						form.dm.setValueAt(form.dm.getValueAt(row-1, 1), row, 1);
						form.dm.setValueAt(form.dm.getValueAt(row-1, 2), row, 2);
						for(int i=3 ; i< column ; i++){
							if(arr[i].equals("") ){
								if(db.DataType(i+1, form.type)){
									form.dm.setValueAt(0, row, i);
								}else{form.dm.setValueAt(arr[i], row, i);}
							}else{
								form.dm.setValueAt(arr[i], row, i);
							}
						}
					}else{
						for(int i=0 ; i< column ; i++){
							if(arr[i].equals("") ){
								if(db.DataType(i+1, form.type)){
									form.dm.setValueAt(0, row, i);
								}
								else{
									form.dm.setValueAt(arr[i], row, i);
								}
							}else{
								form.dm.setValueAt(arr[i], row, i);
							}
						}
					}
					out.println("WHAT");
					row++;
				}
				br.close();
				
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}	
		}
}
