import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class ProgressImage extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image progressImg;
	private JPanel parentPanel;
	private int percent;

	public ProgressImage(JPanel parentPanel){
		super();
		this.parentPanel = parentPanel;
		this.percent = 0;
		try {
			progressImg = ImageIO.read(new File("Images\\human_evolution.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(progressImg, 0,0,parentPanel.getWidth(), parentPanel.getHeight(), null);
		g.setColor(new Color(228,238,238));
		g.fillRect((int)((parentPanel.getWidth()*percent)/100), 0, parentPanel.getWidth(), parentPanel.getHeight());
		g.setColor(Color.blue);
		g.drawLine((int)((parentPanel.getWidth()*percent)/100), 0, (int)((parentPanel.getWidth()*percent)/100), parentPanel.getHeight());
	}

	public void setPercentage(int percent){
		this.percent = percent;
		repaint();
	}

	public void setImageSize() {
		progressImg = progressImg.getScaledInstance(this.parentPanel.getWidth(), this.parentPanel.getHeight(), Image.SCALE_SMOOTH);
	}
}
