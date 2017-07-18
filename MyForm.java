package Manin;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

import static java.lang.System.out;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import Database.Upload;
import GroupTable.ColumnGroup;
import GroupTable.GroupableTableColumnModel;
import GroupTable.GroupableTableHeader;


public class MyForm extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	public JProgressBar loading;
	public DefaultTableModel dm;
	public JTable table;
	public String textSection ="";
	public String type="";
	public JFileChooser fileopen;
	public static MyForm instance = new MyForm();
	public File file;
	public JButton btnButton;
	
	/**
	 *Created by Meranote on 7/13/2017.
	 * **/
	private File currentFile = null;
    private File targetFile = null;
    private boolean newFileSelectedFlag = false;

	private MyForm() {

		// Create Form Frame
		super("CSV Import");
		// Get look & feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFrame.setDefaultLookAndFeelDecorated(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
		setBackground(Color.WHITE);
		getContentPane().setForeground(Color.GRAY);
		setSize(1200, 600);
		setLocation(500, 280);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(null);
		getContentPane().setBackground(Color.WHITE); 
		
		//loading Bar
		loading = new JProgressBar();
		loading.setBounds(0, 0, 1194, 19);
		loading.setValue(0);
		loading.setStringPainted(true);
		getContentPane().add(loading);

		// Label Result file name
		final JTextField lblResult = new JTextField(JTextField.CENTER);
		lblResult.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblResult.setBounds(408, 138, 376, 33);
		getContentPane().add(lblResult);
		
		//Drop down list section
		JComboBox<String> section = new JComboBox<String>();
		section.setBackground(SystemColor.inactiveCaption);
		section.setFont(new Font("Tahoma", Font.PLAIN, 16));
		String[] sectionSet = {"","Stock Preparation", "Wet End", "Dryend", "Chemical Control", "Stock Approaching","Winder"};
		int count=0;
		for (int i = 0; i < 7; i++)
		      section.addItem(sectionSet[count++]);
		section.setBounds(172, 141, 208, 27);
		getContentPane().add(section);
		
		//Create log sheet radio button
		JRadioButton log = new JRadioButton("Log Sheet");
		log.setBackground(Color.WHITE);
		log.setForeground(Color.BLACK);
		log.setFont(new Font("Tahoma", Font.PLAIN, 16));
		log.setBounds(172, 178, 109, 23);
		//getContentPane().add(log);
		
		//Create unit use Radio Button
		JRadioButton unit = new JRadioButton("Unit Use");
		unit.setBackground(Color.WHITE);
		unit.setForeground(Color.BLACK);
		unit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		unit.setBounds(283, 178, 135, 23);
		//getContentPane().add(unit);
		
		//Group Button
		ButtonGroup group = new ButtonGroup();
		group.add(unit);
		group.add(log);
		
		//Table Model
		dm = new DefaultTableModel();
		
		//scroll pane
		JScrollPane scroll = new JScrollPane(table);
		scroll.setBounds(37, 208, 1105, 285);
		getContentPane().add(scroll);
		
		//Create Table
		 table = new JTable();
         table.setColumnModel(new GroupableTableColumnModel());
         table.setTableHeader(new GroupableTableHeader((GroupableTableColumnModel)table.getColumnModel()));
         table.setModel(dm);
         
        // Create Button Open JFileChooser
 		btnButton = new JButton("Browse");
 		btnButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
 		btnButton.setBounds(794, 131, 135, 47);
 		btnButton.setBackground(SystemColor.activeCaption);
		fileopen = new JFileChooser();
 		getContentPane().add(btnButton);
 		
 		// Button Save
 		JButton btnSave = new JButton("Upload");
 		btnSave.setBackground(SystemColor.activeCaption);
 		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 16));	
 		btnSave.setBounds(441, 504, 115, 47);
		getContentPane().add(btnSave);
		
		//Logo SCG Packaging
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(MyForm.class.getResource("/Manin/logo-03.png")));
		lblNewLabel.setBounds(474, 11, 214, 127);
		getContentPane().add(lblNewLabel);
		
		//Button Cancel
		JButton Cancel = new JButton("Cancel");
		Cancel.setBackground(new Color(255, 99, 71));
		Cancel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		Cancel.setBounds(628, 504, 115, 47);
		getContentPane().add(Cancel);
		
		//table change depend on value from drop down list
		section.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textSection = String.valueOf(section.getSelectedItem());
				    		if(textSection.equals("Stock Preparation")){
				    			getContentPane().add(unit);
				    			getContentPane().add(log);
				    			
				    			log.addActionListener(new ActionListener(){
					    			public void actionPerformed(ActionEvent e){
					    			scroll.setViewportView(null);
							    	dm.setRowCount(0);
							    	textSection = "StockLog";
							    	type="stockpreparation";
				    			//Table Model Stock Preparation
				    			dm.setColumnIdentifiers(new Object[]{"Day","Month","Year","Time","Garde"
				    					,"TAQ Gram","Reel Speed","Product","Brand","Furnish Ratio","Freeness"
				    					,"Cons.","Flow rate","Power","SEC","Power","SEC"
				    					,"Brand","Furnish Ratio","Freeness"
				    					,"Cons.","Flow rate","Power","SEC","Power","SEC"
				    					,"Brand","Furnish Ratio","Freeness"
				    					,"Cons.","Flow rate","Power","SEC","Power","SEC"
				    					,"Furnish Ratio","Freeness","Cons.","Dirt"
				    					,"Furnish Ratio","Freeness","Cons."
				    					,"Furnish Ratio","Freeness","Cons."
				    					,"Freeness","Cons.","Flow","Power"
				    					,"Ash","Cons."});

				               GroupableTableColumnModel cm = (GroupableTableColumnModel)table.getColumnModel();
				    		    
				    		    ColumnGroup g_lf = new ColumnGroup("Long Fiber line");
				    		    g_lf.add(cm.getColumn(8));
				    		    g_lf.add(cm.getColumn(9));
				    		    g_lf.add(cm.getColumn(10));
				    		    g_lf.add(cm.getColumn(11));
				    		    g_lf.add(cm.getColumn(12));
				    		    ColumnGroup g_lfrefine1 = new ColumnGroup("Refiner#1");
				    		    g_lfrefine1.add(cm.getColumn(13));
				    		    g_lfrefine1.add(cm.getColumn(14));
				    		    ColumnGroup g_lfrefine2 = new ColumnGroup("Refiner#2");
				    		    g_lfrefine2.add(cm.getColumn(15));
				    		    g_lfrefine2.add(cm.getColumn(16));
				    		    g_lf.add(g_lfrefine1);
				    		    g_lf.add(g_lfrefine2);
				    		    
				    		    ColumnGroup g_hw1 = new ColumnGroup("Hard Wood#1 line");
				    		    g_hw1.add(cm.getColumn(17));
				    		    g_hw1.add(cm.getColumn(18));
				    		    g_hw1.add(cm.getColumn(19));
				    		    g_hw1.add(cm.getColumn(20));
				    		    g_hw1.add(cm.getColumn(21));
				    		    ColumnGroup g_hw1refine1 = new ColumnGroup("Refiner#1");
				    		    g_hw1refine1.add(cm.getColumn(22));
				    		    g_hw1refine1.add(cm.getColumn(23));
				    		    ColumnGroup g_hw1refine2 = new ColumnGroup("Refiner#2");
				    		    g_hw1refine2.add(cm.getColumn(24));
				    		    g_hw1refine2.add(cm.getColumn(25));
				    		    g_hw1.add(g_hw1refine1);
				    		    g_hw1.add(g_hw1refine2);
				    		    
				    		    ColumnGroup g_hw2 = new ColumnGroup("Hard Wood#2 line");
				    		    g_hw2.add(cm.getColumn(26));
				    		    g_hw2.add(cm.getColumn(27));
				    		    g_hw2.add(cm.getColumn(28));
				    		    g_hw2.add(cm.getColumn(29));
				    		    g_hw2.add(cm.getColumn(30));
				    		    ColumnGroup g_hw2refine3 = new ColumnGroup("Refiner#3");
				    		    g_hw2refine3.add(cm.getColumn(31));
				    		    g_hw2refine3.add(cm.getColumn(32));
				    		    ColumnGroup g_hw2refine4 = new ColumnGroup("Refiner#4");
				    		    g_hw2refine4.add(cm.getColumn(33));
				    		    g_hw2refine4.add(cm.getColumn(34));
				    		    g_hw2.add(g_hw2refine3);
				    		    g_hw2.add(g_hw2refine4);
				    		    
				    		    ColumnGroup g_eco = new ColumnGroup("Eco Fiber line");
				    		    g_eco.add(cm.getColumn(35));
				    		    g_eco.add(cm.getColumn(36));
				    		    g_eco.add(cm.getColumn(37));
				    		    g_eco.add(cm.getColumn(38));
				    		    
				    		    ColumnGroup g_ub = new ColumnGroup("Uncoated Broke line");
				    		    g_ub.add(cm.getColumn(39));
				    		    g_ub.add(cm.getColumn(40));
				    		    g_ub.add(cm.getColumn(41));
				    		    
				    		    ColumnGroup g_cb = new ColumnGroup("Coated Broke line");
				    		    g_cb.add(cm.getColumn(42));
				    		    g_cb.add(cm.getColumn(43));
				    		    g_cb.add(cm.getColumn(44));
				    		    
				    		    ColumnGroup g_postRe = new ColumnGroup("Post Refiner");
				    		    g_postRe.add(cm.getColumn(45));
				    		    g_postRe.add(cm.getColumn(46));
				    		    g_postRe.add(cm.getColumn(47));
				    		    g_postRe.add(cm.getColumn(48));
				    		    
				    		    ColumnGroup g_hb = new ColumnGroup("Head Box");
				    		    g_hb.add(cm.getColumn(49));
				    		    g_hb.add(cm.getColumn(50));
				    		    
				    		    cm.addColumnGroup(g_lf);
				    			cm.addColumnGroup(g_hw1);
				    			cm.addColumnGroup(g_hw2);
				    			cm.addColumnGroup(g_eco);
				    			cm.addColumnGroup(g_ub);
				    			cm.addColumnGroup(g_cb);
				    			cm.addColumnGroup(g_postRe);
				    			cm.addColumnGroup(g_hb);
				    			scroll.setViewportView(table);
				    			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					    			}
				    			});
				    			
				    			unit.addActionListener(new ActionListener(){
					    			public void actionPerformed(ActionEvent e){
					    			scroll.setViewportView(null);
							    	dm.setRowCount(0);
							    	loading.setValue(0);
							    	textSection = "StockUnit";
							    	type="unit_use";
							    	dm.setColumnIdentifiers(new Object[]{"Day","Month","Year","OSP","Unit"
					    					,"CaCO3","Sizing A Gent (AKD)","Retention AID","Cationic Starch (CATO)","Size Press Starch","O.B.A / Wet End"
					    					,"B","V","R","Long Fiber","HW1","HW2","Eco Fiber"
					    					,"Water","Steam","Power"});
					               GroupableTableColumnModel cm = (GroupableTableColumnModel)table.getColumnModel();
					    		    
					    		    ColumnGroup g_pro = new ColumnGroup("Production");
					    		    g_pro.add(cm.getColumn(3));
					    		    g_pro.add(cm.getColumn(4));
					    		    ColumnGroup g_chem = new ColumnGroup("Chemical");
					    		    g_chem.add(cm.getColumn(5));
					    		    g_chem.add(cm.getColumn(6));
					    		    g_chem.add(cm.getColumn(7));
					    		    g_chem.add(cm.getColumn(8));
					    		    g_chem.add(cm.getColumn(9));
					    		    g_chem.add(cm.getColumn(10));
					    		    ColumnGroup g_dye = new ColumnGroup("DYE");
					    		    g_dye.add(cm.getColumn(11));
					    		    g_dye.add(cm.getColumn(12));
					    		    g_dye.add(cm.getColumn(13));
					    		    g_chem.add(g_dye);
					    		    
					    		    ColumnGroup g_pulp = new ColumnGroup("Pulp Consumption");
					    		    g_pulp.add(cm.getColumn(14));
					    		    g_pulp.add(cm.getColumn(15));
					    		    g_pulp.add(cm.getColumn(16));
					    		    ColumnGroup g_util = new ColumnGroup("Utility Consumption");
					    		    g_util.add(cm.getColumn(17));
					    		    g_util.add(cm.getColumn(18));
					    		    g_util.add(cm.getColumn(19));

					    		    cm.addColumnGroup(g_pro);
					    			cm.addColumnGroup(g_chem);
					    			cm.addColumnGroup(g_pulp);
					    			cm.addColumnGroup(g_util);
					    			scroll.setViewportView(table);
					    			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					    		    
					    			}
				    			});
				    	   }else if(textSection.equals("Wet End")){
				    		   scroll.setViewportView(null);
				    		   dm.setRowCount(0);
				    		   getContentPane().remove(unit);
				    		   getContentPane().remove(log);
				    		   type="wet_end";
				    		   //Table model Wet end
				    		   dm.setColumnIdentifiers(new Object[]{"Day","Month","Year","Time","Garde"
				    					,"TAQ Gram","Wire Speed","Product","1","2","1","2","3","4","H","L"
				    					,"Pick up roll","H","L","1","2","1P (1)","3P (1)","3P (2)"
				    					,"TS","DS","CCR","TS","DS","TS","DS","CCR"
				    					,"1P","2P","3P"});

				               GroupableTableColumnModel cm = (GroupableTableColumnModel)table.getColumnModel();
				    		    
				    		    ColumnGroup g_vc = new ColumnGroup("Vacuum");
				    		    ColumnGroup g_vcMulti = new ColumnGroup("Multi Shoe");
				    		    g_vcMulti.add(cm.getColumn(8));
				    		    g_vcMulti.add(cm.getColumn(9));
				    		    ColumnGroup g_vcSuc = new ColumnGroup("Suction Box");
				    		    g_vcSuc.add(cm.getColumn(10));
				    		    g_vcSuc.add(cm.getColumn(11));
				    		    g_vcSuc.add(cm.getColumn(12));
				    		    g_vcSuc.add(cm.getColumn(13));
				    		    ColumnGroup g_vcCou = new ColumnGroup("Couch");
				    		    g_vcCou.add(cm.getColumn(14));
				    		    g_vcCou.add(cm.getColumn(15));
				    		    ColumnGroup g_vcSucP = new ColumnGroup("Suction Press");
				    		    g_vcSucP.add(cm.getColumn(17));
				    		    g_vcSucP.add(cm.getColumn(18));
				    		    g_vc.add(g_vcMulti);
				    		    g_vc.add(g_vcSuc);
				    		    g_vc.add(g_vcCou);
				    		    g_vc.add(cm.getColumn(16));
				    		    g_vc.add(g_vcSucP);
				    		    
				    		    ColumnGroup g_uh = new ColumnGroup("U-HLE Box");
				    		    ColumnGroup g_uhP = new ColumnGroup("Pick Up");
				    		    g_uhP.add(cm.getColumn(19));
				    		    g_uhP.add(cm.getColumn(20));
				    		    g_uh.add(g_uhP);
				    		    g_uh.add(cm.getColumn(21));
				    		    g_uh.add(cm.getColumn(22));
				    		    g_uh.add(cm.getColumn(23));
				    		    
				    		    ColumnGroup g_nip = new ColumnGroup("Nip Pressure");
				    		    ColumnGroup g_nip1 = new ColumnGroup("1P");
				    		    g_nip1.add(cm.getColumn(24));
				    		    g_nip1.add(cm.getColumn(25));
				    		    g_nip1.add(cm.getColumn(26));
				    		    ColumnGroup g_nip2 = new ColumnGroup("2P");
				    		    g_nip2.add(cm.getColumn(27));
				    		    g_nip2.add(cm.getColumn(28));
				    		    ColumnGroup g_nip3 = new ColumnGroup("3P");
				    		    g_nip3.add(cm.getColumn(29));
				    		    g_nip3.add(cm.getColumn(30));
				    		    g_nip3.add(cm.getColumn(31));
				    		    g_nip.add(g_nip1);
				    		    g_nip.add(g_nip2);
				    		    g_nip.add(g_nip3);
				    		    
				    		    ColumnGroup g_hp = new ColumnGroup("High Pressure Cleaning");
				    		    g_hp.add(cm.getColumn(32));
				    		    g_hp.add(cm.getColumn(33));
				    		    g_hp.add(cm.getColumn(34));
				    		    
				    		    cm.addColumnGroup(g_vc);
				    		    cm.addColumnGroup(g_uh);
				    		    cm.addColumnGroup(g_nip);
				    		    cm.addColumnGroup(g_hp);
				    		    scroll.setViewportView(table);
				    			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	
				    	   }else if(textSection.equals("Dryend")){
				    		   scroll.setViewportView(null);
				    		   dm.setRowCount(0);
				    		   getContentPane().remove(unit);
				    		   getContentPane().remove(log);
				    		   type="dryend";
				    		   //Table model Dryend
				    		   dm.setColumnIdentifiers(new Object[]{"Day","Month","Year","Time","Garde"
				    					,"TAQ Gram","Reel Speed","#1","#2","#3","#4","TOP","BTM"
				    					,"Steam Flow","TOP","BTM","TOP","BTM","TOP","BTM"
				    					,"TS","DS","TS","DS","CCR","Temp"
				    					,"Time","No. Reel","Grade","Gram","Lenght","Width"
				    					," ","2Sig#1"," ","2Sig#1","Cont Weight","Ash"
				    					,"F#3","2Sig#1","Mois. F#2","Onreel","Reject","To Proc.","Top","Btm"});

				               GroupableTableColumnModel cm = (GroupableTableColumnModel)table.getColumnModel();
				    		    
				    		    ColumnGroup g_st = new ColumnGroup("Steam Pressure");
				    		    g_st.add(cm.getColumn(7));
				    		    g_st.add(cm.getColumn(8));
				    		    g_st.add(cm.getColumn(9));
				    		    g_st.add(cm.getColumn(10));
				    		    ColumnGroup g_st5 = new ColumnGroup("#5");
				    		    g_st5.add(cm.getColumn(11));
				    		    g_st5.add(cm.getColumn(12));
				    		    ColumnGroup g_sta = new ColumnGroup("Starch");
				    		    ColumnGroup g_sol = new ColumnGroup("Solid");
				    		    g_sol.add(cm.getColumn(14));
				    		    g_sol.add(cm.getColumn(15));
				    		    ColumnGroup g_temp = new ColumnGroup("Tempareture");
				    		    g_temp.add(cm.getColumn(16));
				    		    g_temp.add(cm.getColumn(17));
				    		    g_st.add(g_st5);
				    		    g_sta.add(g_sol);
				    		    g_sta.add(g_temp);
				    		    
				    		    ColumnGroup g_rod = new ColumnGroup("ROD");
				    		    ColumnGroup g_loadRod = new ColumnGroup("Loading");
				    		    g_loadRod.add(cm.getColumn(18));
				    		    g_loadRod.add(cm.getColumn(19));
				    		    g_rod.add(g_loadRod);
				    		    
				    		    ColumnGroup g_sym = new ColumnGroup("Sym Sizer");
				    		    ColumnGroup g_loadSym = new ColumnGroup("Loading");
				    		    g_loadSym.add(cm.getColumn(20));
				    		    g_loadSym.add(cm.getColumn(21));
				    		    g_sym.add(g_loadSym);
				    		    
				    		    ColumnGroup g_cal = new ColumnGroup("Calender");
				    		    ColumnGroup g_loadCal = new ColumnGroup("Loading");
				    		    g_loadCal.add(cm.getColumn(22));
				    		    g_loadCal.add(cm.getColumn(23));
				    		    g_loadCal.add(cm.getColumn(24));
				    		    g_cal.add(g_loadCal);
				    		    g_cal.add(cm.getColumn(25));
				    		    
				    		    ColumnGroup g_pope = new ColumnGroup("Pope Reel (Reel Report)");
				    		    g_pope.add(cm.getColumn(26));
				    		    g_pope.add(cm.getColumn(27));
				    		    g_pope.add(cm.getColumn(28));
				    		    g_pope.add(cm.getColumn(29));
				    		    g_pope.add(cm.getColumn(30));
				    		    g_pope.add(cm.getColumn(31));
				    		    ColumnGroup g_wei = new ColumnGroup("Weight");
				    		    g_wei.add(cm.getColumn(32));
				    		    g_wei.add(cm.getColumn(33));
				    		    g_pope.add(g_wei);
				    		    ColumnGroup g_call = new ColumnGroup("Callper");
				    		    g_call.add(cm.getColumn(34));
				    		    g_call.add(cm.getColumn(35));
				    		    g_pope.add(g_call);
				    		    g_pope.add(cm.getColumn(36));
				    		    g_pope.add(cm.getColumn(37));
				    		    ColumnGroup g_mo = new ColumnGroup("Mois.");
				    		    g_mo.add(cm.getColumn(38));
				    		    g_mo.add(cm.getColumn(39));
				    		    g_pope.add(g_mo);
				    		    g_pope.add(cm.getColumn(40));
				    		    g_pope.add(cm.getColumn(41));
				    		    g_pope.add(cm.getColumn(42));
				    		    g_pope.add(cm.getColumn(43));
				    		    ColumnGroup g_stFlow = new ColumnGroup("Starch Flow");
				    		    g_stFlow.add(cm.getColumn(44));
				    		    g_stFlow.add(cm.getColumn(45));
				    		    g_pope.add(g_stFlow);
				    		    
				    		    cm.addColumnGroup(g_st);
				    		    cm.addColumnGroup(g_sta);
				    		    cm.addColumnGroup(g_rod);
				    		    cm.addColumnGroup(g_sym);
				    		    cm.addColumnGroup(g_cal);
				    		    cm.addColumnGroup(g_pope);
				    		    scroll.setViewportView(table);
				    			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	
				    		}else if(textSection.equals("Chemical Control")){
					    		   scroll.setViewportView(null);
					    		   dm.setRowCount(0);
					    		   getContentPane().remove(unit);
					    		   getContentPane().remove(log);
					    		   type="chemical_control";
					    		   //Table Model Stock Approaching
					    		   dm.setColumnIdentifiers(new Object[]{"Day","Month","Year","Time","Garde","TAQ Gram"
					    				   ,"Filler Flow","Ratio","Flow","Ratio","Flow","Ratio","Flow","Ratio","Flow","Ratio","Flow"
					    				   ,"F.P. Solid","F.P. Ash","Ash","Bot Solid"
					    				   ,"Speed Pump","Stroke","Rate","Stroke","Rate","Stroke","Rate"
					    					,"Speed Pump","Stroke","Rate","S/P Stroke"
					    					,"Time","No. Reel","Grade","Gram","L*","n*","b*","UV","W/N","B/N"
					    					,"Top","Bot","Top","Bot","Opacity","L*","n*","b*","W/N","B/N"});

					               GroupableTableColumnModel cm = (GroupableTableColumnModel)table.getColumnModel();
					    		    
					    		    ColumnGroup g_ch = new ColumnGroup("อัตราการใช้สารเคมี");
					    		    g_ch.add(cm.getColumn(6));
					    		    ColumnGroup g_cato = new ColumnGroup("Cato");
					    		    g_cato.add(cm.getColumn(7));
					    		    g_cato.add(cm.getColumn(8));
					    		    ColumnGroup g_aicd = new ColumnGroup("AICD");
					    		    g_aicd.add(cm.getColumn(9));
					    		    g_aicd.add(cm.getColumn(10));
					    		    ColumnGroup g_ra = new ColumnGroup("RA (CPAM)");
					    		    g_ra.add(cm.getColumn(11));
					    		    g_ra.add(cm.getColumn(12));
					    		    ColumnGroup g_sil = new ColumnGroup("Silica (NPHH2)");
					    		    g_sil.add(cm.getColumn(13));
					    		    g_sil.add(cm.getColumn(14));
					    		    ColumnGroup g_apam = new ColumnGroup("A-PAM");
					    		    g_apam.add(cm.getColumn(15));
					    		    g_apam.add(cm.getColumn(16));
					    		    g_ch.add(g_cato);
					    		    g_ch.add(g_aicd);
					    		    g_ch.add(g_ra);
					    		    g_ch.add(g_sil);
					    		    g_ch.add(g_apam);
					    		    
					    		    ColumnGroup g_reten = new ColumnGroup("ผลการวัด Retention");
					    		    g_reten.add(cm.getColumn(17));
					    		    g_reten.add(cm.getColumn(18));
					    		    g_reten.add(cm.getColumn(19));
					    		    g_reten.add(cm.getColumn(20));
					    		    
					    		    ColumnGroup g_color = new ColumnGroup("อัตราการใช้สี");
					    		    g_color.add(cm.getColumn(21));
					    		    ColumnGroup g_blue = new ColumnGroup("สีน้ำเงิน (Blue)");
					    		    g_blue.add(cm.getColumn(22));
					    		    g_blue.add(cm.getColumn(23));
					    		    ColumnGroup g_violet = new ColumnGroup("สีม่วง (Violet)");
					    		    g_violet.add(cm.getColumn(24));
					    		    g_violet.add(cm.getColumn(25));
					    		    ColumnGroup g_red = new ColumnGroup("สีแดง (Red)");
					    		    g_red.add(cm.getColumn(26));
					    		    g_red.add(cm.getColumn(27));
					    		    g_color.add(g_blue);
					    		    g_color.add(g_violet);
					    		    g_color.add(g_red);
					    		    
					    		    ColumnGroup g_oba = new ColumnGroup("อัตราการใช้  OBA");
					    		    g_oba.add(cm.getColumn(28));
					    		    ColumnGroup g_wet = new ColumnGroup("Wet end");
					    		    g_wet.add(cm.getColumn(29));
					    		    g_wet.add(cm.getColumn(30));
					    		    g_oba.add(g_wet);
					    		    g_oba.add(cm.getColumn(31));
					    		    
					    		    ColumnGroup g_dcs = new ColumnGroup("ผลการวัดสี DCS");
					    		    ColumnGroup g_dcsTop = new ColumnGroup("DCS (TOP UV INC) วัดรวมแสง UV");
					    		    g_dcsTop.add(cm.getColumn(36));
					    		    g_dcsTop.add(cm.getColumn(37));
					    		    g_dcsTop.add(cm.getColumn(38));
					    		    g_dcsTop.add(cm.getColumn(39));
					    		    g_dcsTop.add(cm.getColumn(40));
					    		    g_dcsTop.add(cm.getColumn(41));
					    		    g_dcs.add(g_dcsTop);
					    		    
					    		    ColumnGroup g_test = new ColumnGroup("ผล Test Lab");
					    		    ColumnGroup g_rou = new ColumnGroup("Roughness");
					    		    g_rou.add(cm.getColumn(42));
					    		    g_rou.add(cm.getColumn(43));
					    		    g_test.add(g_rou);
					    		    ColumnGroup g_cubb = new ColumnGroup("Cubb's");
					    		    g_cubb.add(cm.getColumn(44));
					    		    g_cubb.add(cm.getColumn(45));
					    		    g_test.add(g_cubb);
					    		    g_test.add(cm.getColumn(46));
					    		    ColumnGroup g_colorPaper = new ColumnGroup("สีกระดาษ");
					    		    g_colorPaper.add(cm.getColumn(47));
					    		    g_colorPaper.add(cm.getColumn(48));
					    		    g_colorPaper.add(cm.getColumn(49));
					    		    g_colorPaper.add(cm.getColumn(50));
					    		    g_colorPaper.add(cm.getColumn(51));
					    		    g_test.add(g_colorPaper);
					    		    
					    		    cm.addColumnGroup(g_ch);
					    		    cm.addColumnGroup(g_reten);
					    		    cm.addColumnGroup(g_color);
					    		    cm.addColumnGroup(g_oba);
					    		    cm.addColumnGroup(g_dcs);
					    		    cm.addColumnGroup(g_test);
					    		    scroll.setViewportView(table);
					    			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	
					       }else if(textSection.equals("Stock Approaching")){
				    		   scroll.setViewportView(null);
				    		   dm.setRowCount(0);
				    		   getContentPane().remove(log);
				    			getContentPane().remove(unit);
				    			type="stock_apprpaching";
				    		   //Table model Chemical Control
				    		   dm.setColumnIdentifiers(new Object[]{"Day","Month","Year","Time","Garde","TAQ Gram"
				    				   ,"IN","OUT","REJ","IN","OUT","REJ","IN","OUT","REJ","IN","OUT","REJ","IN","OUT","REJ"
				    				   ,"Decu Vacuum","IN","OUT","IN","OUT","IN","OUT"
				    					,"Fan Pump","Jet/Wire Ratio","Total Head","Level","Ver.","Hor."});

				               GroupableTableColumnModel cm = (GroupableTableColumnModel)table.getColumnModel();
				    		    
				    		    ColumnGroup g_pre = new ColumnGroup("Pressure");
				    		    ColumnGroup g_clean1 = new ColumnGroup("Cleaner1");
				    		    g_clean1.add(cm.getColumn(6));
				    		    g_clean1.add(cm.getColumn(7));
				    		    g_clean1.add(cm.getColumn(8));
				    		    ColumnGroup g_clean2 = new ColumnGroup("Cleaner2");
				    		    g_clean2.add(cm.getColumn(9));
				    		    g_clean2.add(cm.getColumn(10));
				    		    g_clean2.add(cm.getColumn(11));
				    		    ColumnGroup g_clean3 = new ColumnGroup("Cleaner3");
				    		    g_clean3.add(cm.getColumn(12));
				    		    g_clean3.add(cm.getColumn(13));
				    		    g_clean3.add(cm.getColumn(14));
				    		    ColumnGroup g_clean4 = new ColumnGroup("Cleaner4");
				    		    g_clean4.add(cm.getColumn(15));
				    		    g_clean4.add(cm.getColumn(16));
				    		    g_clean4.add(cm.getColumn(17));
				    		    ColumnGroup g_clean5 = new ColumnGroup("Cleaner5");
				    		    g_clean5.add(cm.getColumn(18));
				    		    g_clean5.add(cm.getColumn(19));
				    		    g_clean5.add(cm.getColumn(20));
				    		    ColumnGroup g_screen1 = new ColumnGroup("Screen1");
				    		    g_screen1.add(cm.getColumn(22));
				    		    g_screen1.add(cm.getColumn(23));
				    		    ColumnGroup g_screen2 = new ColumnGroup("Screen2");
				    		    g_screen2.add(cm.getColumn(24));
				    		    g_screen2.add(cm.getColumn(25));
				    		    ColumnGroup g_screen3 = new ColumnGroup("Screen3");
				    		    g_screen3.add(cm.getColumn(26));
				    		    g_screen3.add(cm.getColumn(27));
				    		    g_pre.add(g_clean1);
				    		    g_pre.add(g_clean2);
				    		    g_pre.add(g_clean3);
				    		    g_pre.add(g_clean4);
				    		    g_pre.add(g_clean5);
				    		    g_pre.add(cm.getColumn(21));
				    		    g_pre.add(g_screen1);
				    		    g_pre.add(g_screen2);
				    		    g_pre.add(g_screen3);
				    		    
				    		    ColumnGroup g_head = new ColumnGroup("Headbox");
				    		    g_head.add(cm.getColumn(28));
				    		    g_head.add(cm.getColumn(29));
				    		    g_head.add(cm.getColumn(30));
				    		    g_head.add(cm.getColumn(31));
				    		    ColumnGroup g_pos = new ColumnGroup("Slice Pos.");
				    		    g_pos.add(cm.getColumn(32));
				    		    g_pos.add(cm.getColumn(33));
				    		    g_head.add(g_pos);
				    		    
				    		    cm.addColumnGroup(g_pre);
				    		    cm.addColumnGroup(g_head);
				    		    scroll.setViewportView(table);
				    			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);	
				    			   
				       }else if(textSection.equals("Winder")){
			    		   scroll.setViewportView(null);
			    		   dm.setRowCount(0);
			    		   getContentPane().remove(unit);
			    		   getContentPane().remove(log);
			    		   type="winder";
			    		   dm.setColumnIdentifiers(new Object[]{"Section","Day","Month","Year","Paper Machine","Winder","Design"
			    				   ,"Plan","Actual","P Rate","Winder Opt.","CD","CR","CO","CS","DR","EC","HL","LS","OD","PV","RI","SB"
			    				   ,"SD","SL","SP","ST","SM","WK","OT","ความยาวโคน Spoon","Sheet Break PM/SCD"
			    				   ,"Winder Trim","Q Rate","SH/B","Opt it.","BD/Elec&Ir","M/C Idle","Uncon"
			    				   ,"Clean","Plan SD","A Rate","OEE"});
					               GroupableTableColumnModel cm = (GroupableTableColumnModel)table.getColumnModel();
					    		    
					    		    ColumnGroup g_speed = new ColumnGroup("Speed");
					    		    g_speed.add(cm.getColumn(6));
					    		    g_speed.add(cm.getColumn(7));
					    		    g_speed.add(cm.getColumn(8));
					    		    g_speed.add(cm.getColumn(9));
					    		    
					    		    ColumnGroup g_step = new ColumnGroup("Step Loss");
					    		    g_step.add(cm.getColumn(10));
					    		    g_step.add(cm.getColumn(11));
					    		    g_step.add(cm.getColumn(12));
					    		    g_step.add(cm.getColumn(13));
					    		    g_step.add(cm.getColumn(14));
					    		    g_step.add(cm.getColumn(15));
					    		    g_step.add(cm.getColumn(16));
					    		    g_step.add(cm.getColumn(17));
					    		    g_step.add(cm.getColumn(18));
					    		    g_step.add(cm.getColumn(19));
					    		    g_step.add(cm.getColumn(20));
					    		    g_step.add(cm.getColumn(21));
					    		    g_step.add(cm.getColumn(22));
					    		    g_step.add(cm.getColumn(23));
					    		    g_step.add(cm.getColumn(24));
					    		    g_step.add(cm.getColumn(25));
					    		    g_step.add(cm.getColumn(26));
					    		    g_step.add(cm.getColumn(27));
					    		    g_step.add(cm.getColumn(28));
					    		    g_step.add(cm.getColumn(29));
					    		    g_step.add(cm.getColumn(30));
					    		    g_step.add(cm.getColumn(31));
					    		    g_step.add(cm.getColumn(32));
					    		    g_step.add(cm.getColumn(33));
					    		    
					    		    ColumnGroup g_loss = new ColumnGroup("Loss Time");
					    		    g_loss.add(cm.getColumn(34));
					    		    g_loss.add(cm.getColumn(35));
					    		    g_loss.add(cm.getColumn(36));
					    		    g_loss.add(cm.getColumn(37));
					    		    g_loss.add(cm.getColumn(38));
					    		    g_loss.add(cm.getColumn(39));
					    		    g_loss.add(cm.getColumn(40));
					    		    g_loss.add(cm.getColumn(41));
					    		    g_loss.add(cm.getColumn(42));
					    		    
					    		    cm.addColumnGroup(g_speed);
					    		    cm.addColumnGroup(g_step);
					    		    cm.addColumnGroup(g_loss);
					    		    scroll.setViewportView(table);
					    			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			    			   
			       }
					       else{
				    	   scroll.setViewportView(null);
			    		   dm.setRowCount(0);
				       }
				       }
				     });
		
		/**
		 * File Chooser
		 */
		btnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.setRowCount(0);
				btnButton.setEnabled(false);

				FileFilter filter = new FileNameExtensionFilter(
						"CSV file", "csv");
				fileopen.addChoosableFileFilter(filter);

				int ret = fileopen.showOpenDialog(frame);

				if (ret == JFileChooser.APPROVE_OPTION) {
					out.println("File Choose");
					/*
					setTargetFile(fileopen.getSelectedFile());
					lblResult.setText(fileopen.getSelectedFile().toString());
		            newFileSelectedFlag = true;*/

					
					file = fileopen.getSelectedFile();
					lblResult.setText(fileopen.getSelectedFile().toString());
					ReadFile rd = new ReadFile(instance);
					rd.runReadFile();
					
				}

			}
		});
		
		/**
		 * Upload Button
		 */
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textSection.equals("StockLog")){
					UploadStockPre(); // Upload file Stock Preparation
				}else if(textSection.equals("Wet End")){
					UploadWetEnd();// Upload file Wet End
				}else if(textSection.equals("Dryend")){
					UploadDryend();
				}else if(textSection.equals("Chemical Control")){
					UploadChemical();
				}else if(textSection.equals("Stock Approaching")){
					UploadStockApp();
				}else if(textSection.equals("Winder")){
					UploadWinder();
				}else if(textSection.equals("StockUnit")){
					UploadStockUnit();
				}
			}
		});
		
		//Cancel Table
		Cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				scroll.setViewportView(null);
			    dm.setRowCount(0);
			    lblResult.setText("");
			}
		});
				
	}
	
	public void showWindow(){
		setVisible(true);
	}
	
	public void UploadStockPre(){
		new Upload(instance).StockLog();
		out.println("Upload stock Preparation log sheet");
	}
	
	public void UploadWetEnd(){
		new Upload(instance).WetEnd();
		out.println("UploadWet End log sheet");
	}
	
	public void UploadDryend(){
		new Upload(instance).dryend();
		out.println("Upload Dryend log sheet");
	}
	
	public void UploadChemical(){
		new Upload(instance).chemical();
		out.println("Upload Chemical Control log sheet");
	}
	
	public void UploadStockApp(){
		new Upload(instance).StockApp();
		out.println("Upload Stock Approaching log sheet");
	}
	
	public void UploadWinder(){
		new Upload(instance).Winder();
		out.println("Upload Winder log sheet");
	}
	
	public void UploadStockUnit(){
		new Upload(instance).StockUnit();
		out.println("Upload Stock Unit use");
	}
	
	 /**
     * Get current file
     * @return
     */
    public File getCurrentFile() {
        return currentFile;
    }

    /**
     * Set current file
     * @param currentFile
     */
    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    /**
     * Get target file
     * @return
     */
    public File getTargetFile() {
        return targetFile;
    }

    /**
     * Set target file
     * @param targetFile
     */
    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    /**
     * Is new file has been selected
     * @return
     */
    public boolean isNewFileSelected() {
        return newFileSelectedFlag;
    }

    /**
     * Set new file selected flag
     * @param flag
     */
    public void setNewFileSelectedFlag(boolean flag) {
        this.newFileSelectedFlag = flag;
    }
    
	public static void main(String[] args) {
		instance.showWindow();
		/*BackgroundWorker work = new BackgroundWorker(instance);
		work.execute();*/
	}
 }
