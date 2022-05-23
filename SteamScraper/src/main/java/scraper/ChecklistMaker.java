package scraper;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ChecklistMaker {

	private JFrame frmAchievementChecklistCreator;
	private JTextField urlTextField;
	private JTextField directoryTextField;
	private JButton selectDirectory;
	private JButton create;
	private SteamAchievementScraper s;
	private String lastDir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChecklistMaker window = new ChecklistMaker();
					window.frmAchievementChecklistCreator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChecklistMaker() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		s = new SteamAchievementScraper();
		
		frmAchievementChecklistCreator = new JFrame();
		frmAchievementChecklistCreator.setResizable(false);
		frmAchievementChecklistCreator.setTitle("Achievement Checklist Creator");
		frmAchievementChecklistCreator.setBounds(100, 100, 500, 170);
		frmAchievementChecklistCreator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAchievementChecklistCreator.getContentPane().setLayout(null);
		
		JLabel urlLabel = new JLabel("URL");
		urlLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		urlLabel.setBounds(34, 31, 47, 14);
		frmAchievementChecklistCreator.getContentPane().add(urlLabel);
		
		JLabel instructionLabel = new JLabel("Type a TrueSteamAchievements URL and press ENTER");
		instructionLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
		instructionLabel.setBounds(137, 11, 286, 14);
		frmAchievementChecklistCreator.getContentPane().add(instructionLabel);
		
		JLabel directoryLabel = new JLabel("Save Directory");
		directoryLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		directoryLabel.setBounds(10, 61, 82, 14);
		frmAchievementChecklistCreator.getContentPane().add(directoryLabel);
		
		create = new JButton("Create");
		create.setFont(new Font("Tahoma", Font.PLAIN, 10));
		create.setBounds(272, 89, 109, 23);
		frmAchievementChecklistCreator.getContentPane().add(create);
		create.setEnabled(false);
		create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(urlTextField.getText() == "" || urlTextField.getText().length() < 34) {
					JOptionPane.showMessageDialog(frmAchievementChecklistCreator, "Enter a valid URL", "URL Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (!urlTextField.getText().contains("truesteamachievements") || !urlTextField.getText().contains("/achievements")) {
					JOptionPane.showMessageDialog(frmAchievementChecklistCreator, "Enter a URL from TrueSteamAchievements", "URL Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					s.setURL(urlTextField.getText());
					s.getGameTitle();
					s.setSaveDirectory(directoryTextField.getText());
					directoryTextField.setText(s.getSaveDirectory());
					urlTextField.setText(s.getURL());
					
					ArrayList<String> titles = s.getAchievementTitles();
					ArrayList<String> conditions = s.getAchievementConditions();
					ArrayList<Checklist> data = s.compileData(titles, conditions);
					s.createSheet(data);
					selectDirectory.setEnabled(false);
					create.setEnabled(false);
					s = new SteamAchievementScraper();
					if (s.getSaveDirectory() != lastDir) {
						s.setSaveDirectory(lastDir);
					}
					urlTextField.setText("");
					JOptionPane.showMessageDialog(frmAchievementChecklistCreator, "Checklist created. Enter another URL to continue", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		selectDirectory = new JButton("Select Directory");
		selectDirectory.setFont(new Font("Tahoma", Font.PLAIN, 10));
		selectDirectory.setBounds(153, 89, 109, 23);
		frmAchievementChecklistCreator.getContentPane().add(selectDirectory);
		selectDirectory.setEnabled(false);
		selectDirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int option = fc.showOpenDialog(null);
				if (option == JFileChooser.APPROVE_OPTION) {
					s.setSaveDirectory(fc.getSelectedFile().getAbsolutePath() + "/" + s.filename);
					lastDir = fc.getSelectedFile().getAbsolutePath() + "/";
					directoryTextField.setText(s.getSaveDirectory());
				}
			}
		});
		
		directoryTextField = new JTextField();
		directoryTextField.setEditable(false);
		directoryTextField.setBounds(91, 58, 383, 20);
		frmAchievementChecklistCreator.getContentPane().add(directoryTextField);
		directoryTextField.setColumns(10);
		directoryTextField.setText(s.getSaveDirectory() + s.filename);
		
		urlTextField = new JTextField();
		urlTextField.setBounds(91, 28, 383, 20);
		frmAchievementChecklistCreator.getContentPane().add(urlTextField);
		urlTextField.setColumns(10);
		urlTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(urlTextField.getText() == "" || urlTextField.getText().length() < 34) {
					JOptionPane.showMessageDialog(frmAchievementChecklistCreator, "Enter a valid URL", "URL Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (!urlTextField.getText().contains("truesteamachievements") || !urlTextField.getText().contains("/achievements")) {
					JOptionPane.showMessageDialog(frmAchievementChecklistCreator, "Enter a URL from TrueSteamAchievements", "URL Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					s.setURL(urlTextField.getText());
					s.getGameTitle();
					lastDir = s.getSaveDirectory();
					directoryTextField.setText(s.getSaveDirectory() + s.filename);
					urlTextField.setText(s.getURL());
					selectDirectory.setEnabled(true);
					create.setEnabled(true);
					JOptionPane.showMessageDialog(frmAchievementChecklistCreator, "URL Accepted", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
	}
}
