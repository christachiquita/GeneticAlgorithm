package gui;

/**
 * @author christachiquita, john wilshire
 */

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Random;



import com.sun.org.glassfish.external.statistics.AverageRangeStatistic;

import main.*;
import GeneticAlgorithm.*;
import data.*;

public class GADrawer extends Component {

    private GACV ga;
    private int width;
    private int height;
    private Image bufferImage;
    private int lowerBuffer;
	private int leftBuffer;
    private Point oldAverage;
    private int count;
    // width of the border
    private static int BORDER = 5;
    
    public GADrawer(GACV ga, int w, int h) {
        super();
        init(ga, w, h);
    }
    
    public void init(GACV ga, int w, int h) {
        
        this.ga = ga;
        width  = w;
        height = h;
	    lowerBuffer = 50;
	    leftBuffer = 30;
	    count = 0;
        setSize(width, height);
        
        setBackground(Color.WHITE);
        setVisible(true);
        bufferImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
        
        clear();
    }
    
    public void paint(Graphics g) {
        if (bufferImage != null)
	    g.drawImage(bufferImage, 0, 0, this);
    }
    

    public void update(Graphics g) {
        paint(g);
    }
    
    private void drawPoint (Point p, Color col) {
        Graphics g = bufferImage.getGraphics();
        g.setColor(col);
        g.fillRect((int)(p.x), (int)(p.y), 2, 2);
        repaint();
    }
    


	public void setGA(GACV ga) {
		this.ga = ga;
	}

	public void drawPopulation(int gen) {
		if(count > 200){
			clear();
			drawBox(gen - (gen%200));
			count = 0;
		}
		
		int xCoord  = leftBuffer + 3 * count;
        for(Hypothesis hyp : ga.getPopulation()){
        	Point point = new Point(xCoord, getYCoord(hyp.getFitness()));
        	drawPoint(point, Color.black);
        }
        
        Hypothesis best = ga.getPopulation().get(0);
        Point point = new Point(xCoord, getYCoord(best.getFitness()));
    	drawPoint(point, Color.BLUE);
    	count++;
	}
	
	private int getYCoord(double fit){
		int bottom = height - lowerBuffer;
		double max = height - lowerBuffer*2;
		return bottom - (int) (max * fit);
		
	}
	
	public void clear (){
		Graphics g = bufferImage.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
	}
	
	public void drawBox(int gen){
		Graphics2D g = (Graphics2D)bufferImage.getGraphics();
		g.setColor(Color.BLACK);
		g.drawLine(leftBuffer, lowerBuffer, width,lowerBuffer);
		g.drawLine(leftBuffer, lowerBuffer, leftBuffer,height - lowerBuffer);
		g.drawLine(leftBuffer, height - lowerBuffer, width, height - lowerBuffer);
		g.drawString("Generations", width/2, 10 +  height - lowerBuffer/2);
		
		g.drawString(String.valueOf(gen), leftBuffer , - 10 + height - lowerBuffer/2);
		g.drawString(String.valueOf(gen + 100), leftBuffer + width /2 , - 10 +  height - lowerBuffer/2);
		
		g.drawLine(leftBuffer + width /2 ,10 + height - lowerBuffer, leftBuffer + width /2 , height - lowerBuffer - 10);
		g.setColor(Color.GREEN);
		g.drawString("Average Fitness", width/4, height - lowerBuffer/2);
		
		g.setColor(Color.RED);
		int threshold = getYCoord(ga.getFitnessTH());
		g.drawLine(leftBuffer, threshold, width,threshold);
		
		g.rotate(Math.PI /2);
		g.drawString("Threshold", threshold - 30, -15);
		g.setColor(Color.BLACK);
		g.drawString("Fitness", height/2, -15);
	}
	
	public void resetCount(){
		count = 0;
	}
	
	public void drawAverage (int gen) {

		if ( gen == 0 ){
			oldAverage = new Point(leftBuffer + 3* count , getYCoord(ga.getTotalFitness()/((double)ga.getP() )));
		} else {
			
			Graphics g = bufferImage.getGraphics();
			Point average = new Point(leftBuffer + 3* count , getYCoord(ga.getTotalFitness()/((double)ga.getP())));
			if(count == 0) average.x = leftBuffer;
			g.setColor(Color.GREEN);
			g.drawLine((int)oldAverage.getX(), (int)oldAverage.getY(),(int)average.getX() , (int)average.getY());
			oldAverage = average;
		}	
	}
}

