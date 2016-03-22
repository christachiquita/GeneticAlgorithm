package gui;

import java.awt.*;
import java.awt.image.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;

import main.*;
import GeneticAlgorithm.*;
import data.*;
import main.GACV;

public class GAResults extends JPanel {
	private GACV ga;
    private int width;
    private JTable table;
    private int BORDER = 10;
   

	public GAResults(GACV ga, int w, int h) {
        super();
        init(ga, w, h);
    }
    
    public void init(GACV ga, int w, int h) {
        
        this.ga = ga;
        width  = w;
    }
    
	public void drawResults () {
		this.removeAll();
		DecimalFormat formatter = new DecimalFormat("0.000");
		Object[] dataNames = {"Fold","Generations", "Training Set Result","Test Set Result", "Change in fitness"};
		Object[][] data = new Object[ga.getFolds()][dataNames.length];
        
		Object[][] statsResults = new Object[3][3];
		
        ArrayList<Hypothesis> best = ga.getBestHyps();
        int i = 0;
        
        Object[] statsNames = {"","Average", "Standard Dev."};
        statsResults[0][0] = "Generations";
        statsResults[1][0] = "Training set";
        statsResults[2][0] = "Test set";
        double averageGen = 0;
        double averageTrain = 0;
        double averageTest = 0;
        
        double[][] doubleVals= new double[ga.getFolds()][3];
        
		for(Hypothesis h : best){
			data[i][0] = String.valueOf(i + 1);
			data[i][1] = String.valueOf(ga.getGenerations().get(i));
			doubleVals[i][0] = (double)ga.getGenerations().get(i);
			averageGen += Double.valueOf(ga.getGenerations().get(i));
			double fitness = h.getFitness();
			averageTrain += fitness;
			doubleVals[i][1] = fitness;
			data[i][2] = formatter.format(fitness);
			double testFitness = ga.getFitnessFuncs().get(i).evalTestSet(h);
			averageTest += testFitness;
			data[i][3] = formatter.format(testFitness);
			doubleVals[i][2] = testFitness;
			data[i][4] = formatter.format(fitness -testFitness);
			i++;
		}
		
		averageGen /= (double)i;
		averageTrain /= (double)i;
		averageTest /= (double)i;
		
		
		// calculating the standard deviation
		double tempGen = 0;
		double tempTrain = 0;
		double tempTest = 0;
		for(int j = 0; j < i; j++){
			tempGen += Math.pow((averageGen - doubleVals[j][0]), 2);
			tempTrain += Math.pow((averageTrain - doubleVals[j][1]), 2);
			tempTest += Math.pow((averageTest - doubleVals[j][2]), 2);
		}
		
		tempGen = Math.sqrt(tempGen/i);
		tempTrain = Math.sqrt(tempTrain/i);
		tempTest = Math.sqrt(tempTest/i);
	
		
		statsResults[0][1] = formatter.format(averageGen);
        statsResults[1][1] = formatter.format(averageTrain);
        statsResults[2][1] = formatter.format(averageTest);
        statsResults[0][2] = formatter.format(tempGen);
        statsResults[1][2] = formatter.format(tempTrain);
        statsResults[2][2] = formatter.format(tempTest);
		
		table = new JTable(data, dataNames);
		table.setPreferredSize(new Dimension(width - BORDER, table.getRowHeight()*ga.getFolds()));
		table.setDragEnabled(false);
		table.getColumn("Fold").setPreferredWidth(10);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.getTableHeader().setPreferredSize(new Dimension(width - BORDER, 20));;
		this.add(table.getTableHeader());
		
		this.add(table);
		
		// table that will display the MEAN and STANDARD DEVIATION of the collected values swag swag 
		JTable statsTable = new JTable(statsResults, statsNames);
		statsTable.setDragEnabled(false);
		statsTable.setPreferredSize(new Dimension(width/2,table.getRowHeight()*3));
		statsTable.getTableHeader().setPreferredSize(new Dimension(width/2,table.getRowHeight()));
		this.add(statsTable.getTableHeader());
		this.add(statsTable);
	}

	public void setGA(GACV ga2) {
		ga = ga2;
		
	}
}
