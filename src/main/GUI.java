package main;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import neuralNetwork.Network;
import neuralNetwork.OutputNeuron;

import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.border.LineBorder;

public class GUI {

	private Shot shot;
	private JFrame frame;
	private JLabel arrow,
				   target;
	private int H = 308,
				W = 450,
				X,
				Y;
	private JPanel panel_1;
	private JTextField textFieldX;
	private JTextField textFieldY;

	public GUI() {
		initialize();
	}

	private void initialize() {
		Network network = new Network();
		GUI gui = this;
		frame = new JFrame();
		frame.setBounds(100, 100, W, H);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(new Color(255, 140, 0));
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(0, 248, 450, 38);
		panel.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		H -= panel_1.getHeight() + 32;

		arrow = new JLabel("");
		arrow.setSize(new Dimension(10, 10));
		arrow.setLocation(0, H);
		ImageIcon arrowLogo = new ImageIcon("img/arrow.png");
		arrow.setIcon(arrowLogo);
		panel.add(arrow);
		
		target = new JLabel("");
		target.setSize(new Dimension(10, 10));
		ImageIcon targetLogo = new ImageIcon("img/target.png");
		target.setIcon(targetLogo);
		target.setLocation(400, H-50);
		panel.add(target);
		
		
		JButton startButton = new JButton("Start");
		startButton.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(startButton);
		
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton b = ((JButton) e.getSource());
				if(b.getText().equals("Start")) {
					b.setText("Reset");
					shot = new Shot(gui);
					OutputNeuron answers = network.result(target.getX(), H - target.getY());
					shot.makeShot(answers.v0, answers.angle, target.getX(), H - target.getY());
				} else {
					if(b.getText().equals("Change")) {
						target.setLocation(X, H-Y);
					}
					b.setText("Start");
					shot.interrupt();
					arrow.setLocation(0, H);
				}
			}
		});
		
		JLabel lblX = new JLabel("x:");
		panel_1.add(lblX);
		
		textFieldX = new JTextField();
		textFieldX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField field = (JTextField) e.getSource();
				X = Integer.parseInt(field.getText());
				startButton.setText("Change");
			}
		});
		
		panel_1.add(textFieldX);
		textFieldX.setColumns(10);
		
		JLabel lblY = new JLabel("y:");
		panel_1.add(lblY);
		
		textFieldY = new JTextField();
		textFieldY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField field = (JTextField) e.getSource();
				Y = Integer.parseInt(field.getText());
				startButton.setText("Change");
			}
		});
		panel_1.add(textFieldY);
		textFieldY.setColumns(10);
		
		frame.setVisible(true);
	}
	
	public void setPos(int x, int y) {
		arrow.setLocation(x, H - y);
		arrow.repaint();
	}
}
