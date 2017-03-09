package WaysideController;


import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.script.ScriptException;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.io.*;
import java.awt.Color;
import java.util.Set;
import TrackModel.*;

public class WS extends JPanel {
	//GUI ELEMENTS
	private JTextArea blockConsole;
	private JTextArea trainConsole;
	private final JTextArea notifConsole = new JTextArea();
	TextAreaOutputStream notifStream;
	TextAreaOutputStream blockStream;
	private String name;
	private PLC plc;

	//WS ELEMENTS
	//Train
	//Block

	/**
	 * Create the panel.
	 * @throws IOException
	 */


	public WS(String name, PLC plcinit, TrackModel track) throws IOException {


		Set<Integer> blockInts = track.trackList.get("Red").get("A").keySet();
		Integer[] intArr = blockInts.toArray(new Integer[blockInts.size()]);
		Set<String> lineSet = track.trackList.keySet();
		String[] lineStrings = lineSet.toArray(new String[lineSet.size()]);


		setLayout(null);
		this.name = name;
		JComboBox<String> lineDropdown = new JComboBox<String>();
		lineDropdown.setModel(new DefaultComboBoxModel<String>(lineStrings));
		lineDropdown.setToolTipText("");
		lineDropdown.setBounds(10, 11, 137, 20);
		add(lineDropdown);

		String[] segmentStrings = {};
		JComboBox<String> segmentDropdown = new JComboBox<String>();
		segmentDropdown.setModel(new DefaultComboBoxModel<String>(segmentStrings));
		segmentDropdown.setBounds(145, 11, 137, 20);
		add(segmentDropdown);

		String[] blockStrings = {};
		JComboBox<String> blockDropdown = new JComboBox<String>();
		blockDropdown.setModel(new DefaultComboBoxModel<String>(blockStrings));
    blockDropdown.setBounds(281, 11, 130, 20);
		add(blockDropdown);
    
		lineDropdown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				String l = (String) lineDropdown.getSelectedItem();
				Set<String> segmentStrings = track.trackList.get(l).keySet();
				blockDropdown.removeAllItems();
				for (String item : segmentStrings){
					segmentDropdown.addItem(item);
				}
			}
		});


		segmentDropdown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				String l = (String) lineDropdown.getSelectedItem();
				String s = (String) segmentDropdown.getSelectedItem();
				Set<Integer> blockSet = track.trackList.get(l).get(s).keySet();
				System.out.println(blockSet);
				blockDropdown.removeAllItems();
				for (Integer item : blockSet)
					blockDropdown.addItem(Integer.toString(item));

				System.out.println(blockDropdown.getItemCount());
			}
		});

		blockDropdown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println(blockDropdown.getItemCount());
				String block = (String) blockDropdown.getSelectedItem();
				String section = (String) segmentDropdown.getSelectedItem();
				String line = (String) lineDropdown.getSelectedItem();
			}
		});

		//BLOCK CONSOLE
		blockConsole = new JTextArea();
		blockConsole.setBackground(Color.LIGHT_GRAY);
		blockConsole.setBounds(10, 30, 428, 189);
		add(blockConsole);
		blockConsole.setColumns(10);
		this.blockStream = new TextAreaOutputStream(blockConsole);

		JComboBox<String> comboBox_3 = new JComboBox<String>();
		comboBox_3.setModel(new DefaultComboBoxModel<String>(new String[] {"Select Train"}));
		comboBox_3.setBounds(10, 237, 165, 20);
		add(comboBox_3);

		trainConsole = new JTextArea();
		trainConsole.setColumns(10);
		trainConsole.setBounds(10, 257, 428, 180);
		add(trainConsole);

		JLabel lblNotifications = new JLabel("Notifications");
		lblNotifications.setBounds(448, 14, 92, 14);
		add(lblNotifications);
		notifConsole.setBounds(448, 30, 658, 405);
		add(notifConsole);
		this.notifStream = new TextAreaOutputStream(notifConsole);
		this.notifStream.write(new String("THIS IS A TEST - Wayside Controller " + this.name + " created.").getBytes());

		JButton btnOpenTestConsole = new JButton("Open Test Console");
		btnOpenTestConsole.setBounds(261, 230, 150, 23);
		btnOpenTestConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				testPopUp console = new testPopUp();
			}
		});
		add(btnOpenTestConsole);
	}
	public TextAreaOutputStream getNotifStream(){
		return notifStream;
	}
	public PLC getPlc() {
		return plc;
	}
	public void setPlc(PLC plc) {
		this.plc = plc;
	}
}