package gui;

import java.awt.EventQueue;

import javax.swing.*;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Font;

import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JRadioButton;

import main.*;
import GeneticAlgorithm.*;
import data.*;

import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;

public class GAGUI implements Runnable {

	private GACV ga;
	
	private JFrame frame;
	private JTextField textField_p;
	private JTextField textField_r;
	private JTextField textField_Fold;
	private JTextField textField_TH;
	private JTextField textField_m;
	private JTextPane PanePop;
	private JButton btnNext;
	private JLabel lblGenerationNumber;
	private JLabel lblFold;
	private GADrawer graph;
	private GAResults gaRes;	
	private int currentFold;
	Thread thread = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GAGUI window = new GAGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GAGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Genetic Algorithm Machine Learning Project");
		frame.setBounds(100, 100, 605, 850);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel.setBackground(SystemColor.WHITE);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 158, 600, 650);
		tabbedPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tabbedPane.setBackground(Color.WHITE);
		panel.add(tabbedPane);
		
		graph = new GADrawer(ga, 600, 600);
		
		
		tabbedPane.addTab("Graph", null, graph, null);
		tabbedPane.setEnabledAt(0, true);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Information", null, panel_1, null);
		gaRes = new GAResults(ga, 600, 600);
		tabbedPane.addTab("Results", null, gaRes, null);
		
		JLabel lblInfoTitle = new JLabel("Number of Hypothesis : ");
		lblInfoTitle.setVerticalAlignment(SwingConstants.TOP);
		lblInfoTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		lblInfoTitle.setBounds(10, 74, 154, 21);
		panel_1.add(lblInfoTitle);
		
		final JLabel lblHyp = new JLabel("");
		lblHyp.setVerticalAlignment(SwingConstants.TOP);
		lblHyp.setHorizontalAlignment(SwingConstants.LEFT);
		lblHyp.setBounds(202, 74, 367, 21);
		panel_1.add(lblHyp);
		
		JLabel label_1 = new JLabel("Crossover rate : ");
		label_1.setVerticalAlignment(SwingConstants.TOP);
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(10, 109, 154, 21);
		panel_1.add(label_1);
		
		JLabel label_2 = new JLabel("Mutation rate : ");
		label_2.setVerticalAlignment(SwingConstants.TOP);
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(10, 142, 154, 21);
		panel_1.add(label_2);
		
		JLabel label_3 = new JLabel("Fitness Threshold : ");
		label_3.setVerticalAlignment(SwingConstants.TOP);
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(10, 175, 154, 21);
		panel_1.add(label_3);
		
		final JLabel label_COrate = new JLabel("");
		label_COrate.setVerticalAlignment(SwingConstants.TOP);
		label_COrate.setHorizontalAlignment(SwingConstants.LEFT);
		label_COrate.setBounds(202, 109, 367, 21);
		panel_1.add(label_COrate);
		
		final JLabel lblTask = new JLabel("");
		lblTask.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		lblTask.setVerticalAlignment(SwingConstants.TOP);
		lblTask.setHorizontalAlignment(SwingConstants.CENTER);
		lblTask.setBounds(6, 6, 563, 21);
		panel_1.add(lblTask);
		
		final JLabel label_mutrate = new JLabel("");
		label_mutrate.setVerticalAlignment(SwingConstants.TOP);
		label_mutrate.setHorizontalAlignment(SwingConstants.LEFT);
		label_mutrate.setBounds(202, 142, 367, 21);
		panel_1.add(label_mutrate);
		
		final JLabel label_TH = new JLabel("");
		label_TH.setVerticalAlignment(SwingConstants.TOP);
		label_TH.setHorizontalAlignment(SwingConstants.LEFT);
		label_TH.setBounds(202, 175, 367, 21);
		panel_1.add(label_TH);
		
		JLabel label_10 = new JLabel("");
		label_10.setVerticalAlignment(SwingConstants.TOP);
		label_10.setHorizontalAlignment(SwingConstants.LEFT);
		label_10.setBounds(202, 6, 367, 21);
		panel_1.add(label_10);
		
		final JLabel lblCO = new JLabel("");
		lblCO.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblCO.setVerticalAlignment(SwingConstants.TOP);
		lblCO.setHorizontalAlignment(SwingConstants.CENTER);
		lblCO.setBounds(105, 28, 367, 21);
		panel_1.add(lblCO);
		
		final JLabel lblPop = new JLabel("");
		lblPop.setVerticalAlignment(SwingConstants.TOP);
		lblPop.setHorizontalAlignment(SwingConstants.CENTER);
		lblPop.setBounds(105, 208, 450, 21);
		panel_1.add(lblPop);
		

		JPanel panel_Data = new JPanel();
		tabbedPane.addTab("Data", null, panel_Data, null);
		panel_Data.setLayout(null);
		
		JTree tree_balance = new JTree();
		tree_balance.setBounds(0, 0, 293, 312);
		tree_balance.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Balance-Scale") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("Left-weight");
						node_1.add(new DefaultMutableTreeNode("1"));
						node_1.add(new DefaultMutableTreeNode("2"));
						node_1.add(new DefaultMutableTreeNode("3"));
						node_1.add(new DefaultMutableTreeNode("4"));
						node_1.add(new DefaultMutableTreeNode("5"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Left-distance");
						node_1.add(new DefaultMutableTreeNode("1"));
						node_1.add(new DefaultMutableTreeNode("2"));
						node_1.add(new DefaultMutableTreeNode("3"));
						node_1.add(new DefaultMutableTreeNode("4"));
						node_1.add(new DefaultMutableTreeNode("5"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Right-weight");
						node_1.add(new DefaultMutableTreeNode("1"));
						node_1.add(new DefaultMutableTreeNode("2"));
						node_1.add(new DefaultMutableTreeNode("3"));
						node_1.add(new DefaultMutableTreeNode("4"));
						node_1.add(new DefaultMutableTreeNode("5"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Right-distance");
						node_1.add(new DefaultMutableTreeNode("1"));
						node_1.add(new DefaultMutableTreeNode("2"));
						node_1.add(new DefaultMutableTreeNode("3"));
						node_1.add(new DefaultMutableTreeNode("4"));
						node_1.add(new DefaultMutableTreeNode("5"));
					add(node_1);
				}
			}
		));
		panel_Data.add(tree_balance);
		
		final JTextPane PaneData = new JTextPane();
		PaneData.setBounds(12, 361, 567, 139);
		panel_Data.add(PaneData);
		
		JTree tree_mushroom = new JTree();
		tree_mushroom.setBounds(298, 0, 293, 312);
		tree_mushroom.setVisibleRowCount(0);
		
		JButton btnDataPreprocessing = new JButton("Data Preprocessing");
		btnDataPreprocessing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String Atts = "Instances i : 1,3,2,1,L";
				start();
			}
		});
		btnDataPreprocessing.setBounds(50, 324, 187, 25);
		panel_Data.add(btnDataPreprocessing);
		tree_mushroom.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Mushroom") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("cap-shape");
						node_1.add(new DefaultMutableTreeNode("bell=b"));
						node_1.add(new DefaultMutableTreeNode("conical=c"));
						node_1.add(new DefaultMutableTreeNode("convex=x"));
						node_1.add(new DefaultMutableTreeNode("flat=f"));
						node_1.add(new DefaultMutableTreeNode("knobbed=k"));
						node_1.add(new DefaultMutableTreeNode("sunken=s"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("cap-surface");
						node_1.add(new DefaultMutableTreeNode("fibrous=f"));
						node_1.add(new DefaultMutableTreeNode("grooves=g"));
						node_1.add(new DefaultMutableTreeNode("scaly=y"));
						node_1.add(new DefaultMutableTreeNode("smooth=s"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("cap-color");
						node_1.add(new DefaultMutableTreeNode("brown=n"));
						node_1.add(new DefaultMutableTreeNode("buff=b"));
						node_1.add(new DefaultMutableTreeNode("cinnamon=c"));
						node_1.add(new DefaultMutableTreeNode("gray=g"));
						node_1.add(new DefaultMutableTreeNode("green=r"));
						node_1.add(new DefaultMutableTreeNode("pink=p"));
						node_1.add(new DefaultMutableTreeNode("purple=u"));
						node_1.add(new DefaultMutableTreeNode("red=e"));
						node_1.add(new DefaultMutableTreeNode("white=w"));
						node_1.add(new DefaultMutableTreeNode("yellow=y"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Right-distance");
						node_1.add(new DefaultMutableTreeNode("1"));
						node_1.add(new DefaultMutableTreeNode("2"));
						node_1.add(new DefaultMutableTreeNode("3"));
						node_1.add(new DefaultMutableTreeNode("4"));
						node_1.add(new DefaultMutableTreeNode("5"));
					add(node_1);
				}
			}
		));
		panel_Data.add(tree_mushroom);
		
		JPanel panel_Process = new JPanel();
		tabbedPane.addTab("Selection", null, panel_Process, null);
		panel_Process.setLayout(null);
		
		PanePop = new JTextPane();
		PanePop.setBounds(12, 12, 567, 282);
		panel_Process.add(PanePop);
		
		JLabel lblGeneticAlgorithm = new JLabel("Genetic Algorithm");
		lblGeneticAlgorithm.setBounds(0, 0, 600, 22);
		lblGeneticAlgorithm.setFont(new Font("Luminari", Font.BOLD, 16));
		lblGeneticAlgorithm.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblGeneticAlgorithm);
		
		JLabel lblNewLabel_1 = new JLabel("Number of Hypothesis : ");
		lblNewLabel_1.setBounds(10, 67, 154, 16);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel_1);
		
		textField_p = new JTextField();
		textField_p.setText("10");
		
		textField_p.setBounds(164, 61, 83, 28);
		panel.add(textField_p);
		textField_p.setColumns(10);
		
		JLabel lblR = new JLabel("Crossover rate : ");
		lblR.setBounds(260, 67, 103, 16);
		panel.add(lblR);
		
		textField_r = new JTextField();
		textField_r.setText("0.6");
		textField_r.setBounds(363, 61, 84, 28);
		panel.add(textField_r);
		textField_r.setColumns(10);
		
		JLabel lblNumOfFold = new JLabel("k :");
		lblNumOfFold.setBounds(460, 61, 20, 16);
		lblNumOfFold.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblNumOfFold);
		
		textField_Fold = new JTextField();
		textField_Fold.setText("10");
		textField_Fold.setBounds(490, 61, 45, 16);
		panel.add(textField_Fold);
		textField_Fold.setColumns(10);
		
		lblGenerationNumber = new JLabel("Generation: 0");
		lblGenerationNumber.setBounds(460, 90, 129, 16);
		lblGenerationNumber.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblGenerationNumber);
		
		lblFold = new JLabel("Fold: 0");
	    lblFold.setHorizontalAlignment(SwingConstants.LEFT);
	    lblFold.setBounds(460, 112, 70, 15);
	    panel.add(lblFold);
		
		JLabel lblFitnessThreshold = new JLabel("Fitness threshold : ");
		lblFitnessThreshold.setBounds(43, 95, 121, 16);
		lblFitnessThreshold.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblFitnessThreshold);
		
		textField_TH = new JTextField();
		textField_TH.setText("0.9");
		textField_TH.setBounds(164, 89, 83, 28);
		panel.add(textField_TH);
		textField_TH.setColumns(10);
		
		JLabel lblM = new JLabel("Mutation rate : ");
		lblM.setBounds(267, 95, 96, 16);
		panel.add(lblM);
		
		textField_m = new JTextField();
		textField_m.setText("0.2");
		textField_m.setBounds(363, 89, 84, 28);
		panel.add(textField_m);
		textField_m.setColumns(10);

		JButton btnOK = new JButton("Initialise");
		btnOK.setBounds(125, 129, 100, 29);
		btnOK.setForeground(SystemColor.controlHighlight);
		btnOK.setBackground(SystemColor.activeCaption);
		panel.add(btnOK);
		
		btnNext = new JButton("Next Fold");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNext.setBounds(250, 129, 100, 29);
		btnNext.setForeground(SystemColor.controlHighlight);
		btnNext.setBackground(SystemColor.activeCaption);
		panel.add(btnNext);
		btnNext.setVisible(false);
		
		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				graph.clear();
				currentFold++;
				if(currentFold < ga.getFolds()){
					findBestHype(currentFold);
				}
			}
				
		});
		
		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(375, 129, 80, 29);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnReset.setForeground(SystemColor.controlHighlight);
		btnReset.setBackground(SystemColor.activeCaption);
		panel.add(btnReset);
		
		final JRadioButton rdbtnBalancescale = new JRadioButton("Balance-Scale");
		rdbtnBalancescale.setSelected(true);
		rdbtnBalancescale.setBounds(10, 34, 141, 23);
		panel.add(rdbtnBalancescale);
		
		JRadioButton rdbtnMushroom = new JRadioButton("Mushroom");
		rdbtnMushroom.setSelected(true);
		rdbtnMushroom.setBounds(133, 34, 141, 23);
		panel.add(rdbtnMushroom);
		
		JLabel lblCrossover = new JLabel("Crossover : ");
		lblCrossover.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCrossover.setBounds(260, 34, 103, 16);
		panel.add(lblCrossover);
		
		JScrollPane scroll = new JScrollPane(PanePop);
		scroll.setSize(574, 466);
		scroll.setLocation(10, 12);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_Process.add(scroll);
		
		final JComboBox ComboBoxCO = new JComboBox();
		ComboBoxCO.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		ComboBoxCO.addItem("Single Point");
		ComboBoxCO.addItem("Two Points");
		ComboBoxCO.addItem("Uniform");
		ComboBoxCO.setBounds(365, 35, 150, 16);
		panel.add(ComboBoxCO);
		
		ButtonGroup filegroup = new ButtonGroup();
	    filegroup.add(rdbtnMushroom);
	    filegroup.add(rdbtnBalancescale);
		
		btnOK.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				label_COrate.setText(textField_r.getText());
				label_mutrate.setText(textField_m.getText());
				label_TH.setText(textField_TH.getText());
				lblHyp.setText(textField_p.getText());
				int task;
				if (rdbtnBalancescale.isSelected()){
					lblTask.setText("Balance-scale Classification");
					task = 1;
				}
				else{
					lblTask.setText("Mushroom Classification");
					task = 2;
				}
				String S = initGA(textField_p.getText(), textField_r.getText(), 
						textField_m.getText(), task, textField_TH.getText(), ComboBoxCO.getSelectedItem().toString(), Integer.parseInt(textField_Fold.getText()));
				lblCO.setText("Using "+ComboBoxCO.getSelectedItem()+" Crossover");
				lblPop.setText(S);
			}
		});
	}
	
	public String initGA(String p, String r, String m, int task, String fitnessTH, String CO, int folds){
		ga = new GACV(Integer.parseInt(p), Double.parseDouble(r), Double.parseDouble(m), task,
				new Accuracy(), Double.parseDouble(fitnessTH),folds);
		gaRes.setGA(ga);
		if (CO.equals("Single Point")){ga.setCrossOverMethod(1);}
		else if (CO.equals("Uniform")){ga.setCrossOverMethod(0);}
		else {ga.setCrossOverMethod(2);}
		currentFold = 0;
		String print = findBestHype( 0);
		btnNext.setVisible(true);
		return print;
	}

	private String findBestHype(int forFold) {
		
		int maxGenerations = 1500;
		ga.generatePopulation(forFold);
		String print = "Successfully Generated " + ga.getP() + " hypothesis as the new population";
		graph.setGA(ga);
		graph.clear();
		
		int i = 0; 
		double maxFitness = ga.getPopulation().get(0).getFitness();
		while (maxFitness <= ga.getFitnessTH() && i < maxGenerations){
			ga.evolution(forFold);
			graph.drawPopulation(i);
			graph.drawAverage(i);
			String pop = ga.printedPopulation();
			PanePop.setText(pop);
			i++;
			maxFitness = ga.getPopulation().get(0).getFitness();
		}
		Integer gen = ga.getGenerations().set(forFold, i);
		graph.resetCount();
		gen = i;
		
		ga.getBestHyps().add(ga.getPopulation().get(0));
		graph.drawBox((i -( i % 200)));
		lblFold.setText("Fold: " + (forFold + 1));
		lblGenerationNumber.setText("Generations: " + gen);
		gaRes.drawResults();
		return print;
	}

	public void start() {
	    if (thread == null) {
	      thread = new Thread(this);
	      thread.start();
	    }
	  }

	  public void stop() {
	    thread = null;
	  }

	  public void run() {
	    while (thread != null) {
	      try {
	        Thread.sleep(1000);
	      } catch (InterruptedException e) {
	      }
	      System.out.println("ok");
	    }
	    thread = null;
	  }
}
