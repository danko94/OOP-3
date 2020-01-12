package gui;
import javax.swing.*;
import javax.swing.border.Border;

import algorithms.Graph_Algo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SecondWindow extends JFrame{
	
	private static JLabel label;
	private static JButton load;
	private static JButton draw;
	private static JButton exit;
	
	public SecondWindow() {
		
		setSize(400,300);
		setTitle("Graph GUI");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());

		pane.setBackground(Color.gray);
		
		label = new JLabel("Graph GUI");
		label.setFont(new Font("Comic Sans MS", Font.ITALIC, 30));
		
		load = new JButton("load graph from a file");
		draw = new JButton("draw a graph");
		exit = new JButton("QUIT");
		
		load.setBackground(Color.green);
		draw.setBackground(Color.blue);
		load.setForeground(Color.red);
		draw.setForeground(Color.red);
		
		load.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				LoadingWindow t = new LoadingWindow();
				t.setVisible(true);
			}
		});
		
		exit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		
		draw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Graph_Algo algo = new Graph_Algo();
				DrawingWindow f = new DrawingWindow(algo);
				f.setVisible(true);
				
			}
		});
		
		load.setPreferredSize(new Dimension(50, 110));
		pane.add(label, BorderLayout.PAGE_START);
		pane.add(exit, BorderLayout.EAST);
		pane.add(draw, BorderLayout.CENTER);
		pane.add(load, BorderLayout.SOUTH);
	}

}
