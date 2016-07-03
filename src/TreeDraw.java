import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class TreeDraw extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JScrollPane scroller;
	private final int WIDTH = 900;
	private final int HEIGHT = 600;
	private final int xOffset = 50;
	private final int yOffset = 150;
	private final int ovalSize = 30;
	private final int ovalOffset = 20;
	private final int fontSize = 14;
	private Color defaultColor = Color.blue;
	private Individual individual;
	public TreeDraw(Individual individual){
		super();
		this.individual = individual;
		// initialize the JFrame
		frame = new JFrame("Strategy Tree of " +
				individual.getPlayerName() + " Fitness: " + individual.getFitness());
		// set default operation when closing the window
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// add the JPanel to the JFrame container
		frame.add(this);
		// set the bounds of the JFrame
		frame.setBounds(450, 50, WIDTH, HEIGHT);

		// initialize a scroller
		scroller = new JScrollPane(this);
		// unsure what this does exactly.... obsolete garbage maybe?
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// set the scrollable area of the JFrame
		this.setPreferredSize(new Dimension(WIDTH*2, 1800));
		// add the scroller to the JFrame in the center
		frame.add(scroller, BorderLayout.CENTER);
		// set the JFrame visible
		frame.setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(defaultColor);
		g.setFont(new Font ("Arial", Font.ITALIC | Font.BOLD ,fontSize));
		// call the tree drawing recursive method
		// parameters are (Graphic object, root node, x position, y position)
		drawTree(g,individual.getStrategyRoot(),(int)(frame.getWidth()*0.6),45);
	}

	public void drawTree(Graphics g,GPNode root, int x, int y){
		//g.drawString(root.toString(), x, y);
		if(root == null)
			return;

		// set the color to the node color
		g.setColor(root.color);
		// draw a circle around the node
		g.fillOval(x-ovalOffset/4, y-ovalOffset, ovalSize, ovalSize);
		// reset to the default node color
		g.setColor(defaultColor);

		// set color for the Predicate/Terminal draw
		g.setColor(Color.black);
		// draw either P(predicate) or T(Terminal) according to the current node type
		if(root instanceof Function)
			g.drawString("F:" + ((Function)root).toString(), x, y);
		else if(root instanceof Terminal)
			g.drawString("T:" + ((Terminal)root).toString(), x, y);
		// reset the default node color
		g.setColor(defaultColor);

		//		// set the color to the node color
		//		g.setColor(Color.RED);
		//		// draw a circle around the node
		//		g.drawOval(x-12, y-30, 45, 45);
		//		// reset to the default node color
		//		g.setColor(defaultColor);

		// draw and iterate the nodes recursively
		for(int i=0;i<root.getNumChildren();i++){
			if(root.getChildAtIndex(i) != null){
				if(root.getNumChildren() > 2){
					if(i == 0){
						// draw connecting line
						g.drawLine(x, y, x-xOffset*4, y+yOffset);
						// recursive call to the next left child
						drawTree(g, root.getChildAtIndex(i),x-xOffset*4, y+yOffset);	
					}
					else if(i == 1){
						// draw connecting line
						g.drawLine(x, y, x-xOffset*2, y+yOffset);
						// recursive call to the next left child
						drawTree(g, root.getChildAtIndex(i),x-xOffset*2, y+yOffset);	
					}
					else if(i == 2){
						// draw connecting line
						g.drawLine(x, y, x, y+yOffset);
						// recursive call to the next left child
						drawTree(g, root.getChildAtIndex(i),x, y+yOffset);	
					}
					else if(i == 3){
						// draw connecting line
						g.drawLine(x, y, x+xOffset*4, y+yOffset);
						// recursive call to the next left child
						drawTree(g, root.getChildAtIndex(i),x+xOffset*4, y+yOffset);	
					}
					//					if(i == 0){
					//						// draw connecting line
					//						g.drawLine(x, y, x-xOffset*root.getHeight(), y+yOffset);
					//						// recursive call to the next left child
					//						drawTree(g, root.getChildAtIndex(i),x-xOffset*root.getHeight(), y+yOffset);	
					//					}
					//					else if(i == 1){
					//						// draw connecting line
					//						g.drawLine(x, y, x-xOffset*2, y+yOffset);
					//						// recursive call to the next left child
					//						drawTree(g, root.getChildAtIndex(i),x-xOffset*2, y+yOffset);	
					//					}
					//					else if(i == 2){
					//						// draw connecting line
					//						g.drawLine(x, y, x, y+yOffset);
					//						// recursive call to the next left child
					//						drawTree(g, root.getChildAtIndex(i),x, y+yOffset);	
					//					}
					//					else if(i == 3){
					//						// draw connecting line
					//						g.drawLine(x, y, x+xOffset*root.getHeight(), y+yOffset);
					//						// recursive call to the next left child
					//						drawTree(g, root.getChildAtIndex(i),x+xOffset*root.getHeight(), y+yOffset);	
					//					}
				}
				else{
					if(i == 0){
						// draw connecting line
						g.drawLine(x, y, x-xOffset*2, y+yOffset);
						// recursive call to the next left child
						drawTree(g, root.getChildAtIndex(i),x-xOffset*2, y+yOffset);	
					}
					else if(i == 1){
						// draw connecting line
						g.drawLine(x, y, x+xOffset*4, y+yOffset);
						// recursive call to the next left child
						drawTree(g, root.getChildAtIndex(i),x+xOffset*4, y+yOffset);	
					}
					//					if(i == 0){
					//						// draw connecting line
					//						g.drawLine(x, y, x-xOffset*root.getHeight(), y+yOffset);
					//						// recursive call to the next left child
					//						drawTree(g, root.getChildAtIndex(i),x-xOffset*root.getHeight(), y+yOffset);	
					//					}
					//					else if(i == 1){
					//						// draw connecting line
					//						g.drawLine(x, y, x+xOffset*root.getHeight(), y+yOffset);
					//						// recursive call to the next left child
					//						drawTree(g, root.getChildAtIndex(i),x+xOffset*root.getHeight(), y+yOffset);	
					//					}	
				}
			}
		}
	}
}
