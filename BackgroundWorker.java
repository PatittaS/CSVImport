package Manin;

import javax.swing.SwingWorker;

import Database.Database;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BackgroundWorker extends SwingWorker<Void, Integer> {

			private MyForm application;
		    private String lastProcessedChecksum = "";
		    public long total=0;
			public long currentNum=0;
			public int progress = 0;
			private Database db = new Database();

		    /**
		     * File watcher constructor
		     * @param instance
		     */
    
		    public BackgroundWorker(MyForm instance){
		        this.application = instance;
		        addPropertyChangeListener(evt -> {
		           application.loading.setValue(getProgress());
		        });
		    }


		/*@Override
		protected void done() {
				JOptionPane.showMessageDialog(null,"Import Data Successfully");
			//btnButton.setEnabled(true);
			
		}*/

		protected Void doInBackground() throws Exception {
			
			 // Forever loop
	        while(true) {
	            // Is new file have been selected
	            if(application.isNewFileSelected()) {
	                out.println("New file selected!");
	                application.setNewFileSelectedFlag(false);
	                setProgress(0);
	                // Check is the last same processed file
	                String targetChecksum = MD5Checksum.getMD5Checksum(application.getTargetFile());
	                out.println(lastProcessedChecksum + " : " + targetChecksum);

	                if(!targetChecksum.equals(lastProcessedChecksum)) {
	                    out.println("File updated!");

	                    // if not, process new file
	                    lastProcessedChecksum = targetChecksum;
	                    application.setCurrentFile(application.getTargetFile());
	            		try {
	        				BufferedReader br = new BufferedReader(new FileReader(application.getTargetFile()));
	        				br.readLine();
	        				out.print("TEst");
	        				int one = br.readLine().length();
	        				br.readLine();
	        				int two = br.readLine().length();
	        				br.readLine();//read first line to no read header column in .CSV
	        				int three = br.readLine().length();
	        				String line =null;
	        				int column = application.dm.getColumnCount();
	        				int row = 0;
	        				while ((line = br.readLine()) != null) {
	        					total = application.getTargetFile().length();
	        					total = total - (one+two+three);
	        					progress = (int)((currentNum*100/total))+1;
	        					currentNum += (int)line.length();
	        					setProgress((int)((currentNum*100/total))+1);
	        					out.println("WHATTT");
	        					Thread.sleep(50);
	        					String[] arr = line.split(",",-1);
	        					application.dm.addRow(new Object[0]);
	        					String day = arr[0];
	        					String month = arr[1];
	        					String year = arr[2];
	        					String time =arr[3];
	        					if(time.equals("")){
	        						application.dm.removeRow(row);
	        						break;
	        					}		
	        					if(day.equals("")&&month.equals("")&&year.equals("")){
	        						if(time.equals("")){
	        							break;
	        						}
	        						application.dm.setValueAt(application.dm.getValueAt(row-1, 0), row, 0);
	        						application.dm.setValueAt(application.dm.getValueAt(row-1, 1), row, 1);
	        						application.dm.setValueAt(application.dm.getValueAt(row-1, 2), row, 2);
	        						for(int i=3 ; i< column ; i++){
	        							if(arr[i].equals("") ){
	        								if(db.DataType(i+1, application.type)){
	        									application.dm.setValueAt(0, row, i);
	        								}else{application.dm.setValueAt(arr[i], row, i);}
	        							}else{
	        							application.dm.setValueAt(arr[i], row, i);
	        							}
	        						}
	        					}else{
	        						for(int i=0 ; i< column ; i++){
	        							if(arr[i].equals("") ){
	        								if(db.DataType(i+1, application.type)){
	        									application.dm.setValueAt(0, row, i);
	        								}
	        								else{
	        									application.dm.setValueAt(arr[i], row, i);
	        								}
	        							}else{
	        								application.dm.setValueAt(arr[i], row, i);
	        							}
	        						}
	        					}
	        					out.println("WHAT");
	        					row++;
	        				}
	        				setProgress(100);
	        				br.close();
	        				
	        			} catch (IOException ex) {
	        				// TODO Auto-generated catch block
	        				ex.printStackTrace();
	        			}

	                } else {
	                    out.println("Same file selected!");
	                }
	                Thread.sleep(10);
	            }
	        }

		}
	}
 
