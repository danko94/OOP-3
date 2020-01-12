package gui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import algorithms.Graph_Algo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadingWindow extends JFrame{
	
	JButton fChooser;
	JButton loadByText;
	JLabel loadGraphLabel;
	
	File graphFile;
	String graphFilename;
	
	Graph_Algo graph;
	
	public LoadingWindow() {
		Container pane = getContentPane();
		
		pane.setLayout(new BorderLayout());
		setSize(400,300);
		setTitle("Load");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		loadGraphLabel = new JLabel("Load graph");
		fChooser = new JButton("Load using file chooser");
		loadByText = new JButton("Load graph with file name");
		
		loadGraphLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
		
		//file chooser button
		fChooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("graphs", "txt");
				fileChooser.addChoosableFileFilter(filter);
				
				int input = fileChooser.showDialog(getContentPane(), "open file");
				String path = "";
				
				if(input==JFileChooser.APPROVE_OPTION) {
					graphFile = fileChooser.getSelectedFile();
					path=fileChooser.getSelectedFile().getAbsolutePath();

					graph = new Graph_Algo();
					try {
						graph.init(path);
						DrawingWindow f = new DrawingWindow(graph);
						f.setVisible(true);
						

					} catch (Exception e2) {
						JOptionPane.showMessageDialog(pane, "File not found", "ERROR",JOptionPane.ERROR_MESSAGE);

					}

				}

			}
		});
		
		loadByText.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				graphFilename = JOptionPane.showInputDialog("enter filename");
				
				
				try {
					graph = new Graph_Algo();
					graph.init(graphFilename);
					DrawingWindow f = new DrawingWindow(graph);
					f.setVisible(true);

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(pane, "File not found", "ERROR",JOptionPane.ERROR_MESSAGE);
				}
				
				
			}
		});
		
		fChooser.setPreferredSize(new Dimension(50, 110));
		pane.add(loadGraphLabel, BorderLayout.NORTH);
		pane.add(loadByText, BorderLayout.CENTER);
		pane.add(fChooser, BorderLayout.SOUTH);
		
		
		
	}

}
