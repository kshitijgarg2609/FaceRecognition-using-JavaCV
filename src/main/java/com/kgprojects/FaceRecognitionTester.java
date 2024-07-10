package com.kgprojects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FaceRecognitionTester extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8182529271127897005L;
	private int width = 800,height = 400;
    private JPanel jp;
    private JLabel main_screen,fd_screen;
    private JTextField fd_label;
    private JButton trainer,test;

	public FaceRecognitionTester()
	{
		super("Face Recognition Tester");
		int w=Toolkit.getDefaultToolkit().getScreenSize().width;
		int h=Toolkit.getDefaultToolkit().getScreenSize().height;
		setBounds((w - width) / 2, (h - height) / 2, width, height);
		setResizable(false);
        getContentPane().setLayout(null);
        
        jp = new JPanel();
        jp.setBounds(10, 11, 774, 350);
        jp.setLayout(null);
        getContentPane().add(jp);
        
        main_screen = new JLabel();
        main_screen.setBackground(Color.CYAN);
        main_screen.setOpaque(true);
        main_screen.setBounds(10, 11, 582, 328);
        jp.add(main_screen);
        
        fd_screen = new JLabel();
        fd_screen.setBounds(602, 11, 162, 101);
        fd_screen.setBackground(Color.CYAN);
        fd_screen.setOpaque(true);
        jp.add(fd_screen);
        
        fd_label = new JTextField();
        fd_label.setBounds(602, 123, 162, 22);
        jp.add(fd_label);
        
        trainer = new JButton("train");
        trainer.setBounds(640, 156, 89, 23);
        jp.add(trainer);
        
        test = new JButton("test");
        test.setBounds(640, 190, 89, 23);
        jp.add(test);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        setVisible(true);
        repaint();
        revalidate();
	}
	public BufferedImage scaledImage(Image a, int w2, int h2)
	{
        BufferedImage b = new BufferedImage(w2, h2, 2);
        Graphics2D g2 = b.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(a, 0, 0, w2, h2, (ImageObserver) null);
        g2.dispose();
        return b;
    }
	public void updateMainScreen(BufferedImage icon)
	{
        main_screen.setIcon(new ImageIcon(scaledImage(icon, main_screen.getWidth(), main_screen.getHeight())));
    }
	public void updateFaceDetectionScreen(BufferedImage icon)
	{
        fd_screen.setIcon(new ImageIcon(scaledImage(icon, fd_screen.getWidth(), fd_screen.getHeight())));
    }
	public JLabel getMain_screen() {
		return main_screen;
	}
	public JButton getTrainer() {
		return trainer;
	}
	public JButton getTest() {
		return test;
	}
	public JTextField getFd_label() {
		return fd_label;
	}
	
}