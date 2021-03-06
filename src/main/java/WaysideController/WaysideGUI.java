package WaysideController;

import CTC.TrackPanel;
import TrackModel.Block;
import TrackModel.Switch;
import TrackModel.TrackModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import javax.script.ScriptException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

//Imports
//Modules
public class WaysideGUI {

  private JFrame MainFrame;
  ArrayList<WS> Waysides;
  JTextArea notifConsole;
  TrackModel track;
  JComboBox<String> lineDropdown;
  JComboBox<String> segmentDropdown;
  JComboBox<String> blockDropdown;
  Block activeBlock;

  JLabel occupiedLabel;
  JLabel occupiedStatusLabel;
  JLabel switchLabel;
  JLabel crossingLabel;
  JLabel lightsLabel;
  JLabel switchStatusLabel;
  JLabel crossingStatusLabel;
  JLabel lightsStatusLabel;
  JLabel fwdBlockNum;
  JLabel backBlockNum;
  JComboBox<String> wsSelectDropdown;;


  /**
   * Runs whenever launcher advances a clock tick.
   */
  public void update(){
    updateFields();
  }

  /**
   * Enables Block description fields to be updated by event of block dropdown selection or by clock updating.
   */
  public void updateFields(){
    if(blockDropdown.getSelectedItem() != null){
      String block = (String) this.blockDropdown.getSelectedItem();
      String section = (String) this.segmentDropdown.getSelectedItem();
      String line = (String) this.lineDropdown.getSelectedItem();
      this.activeBlock = track.getBlock(line, section, Integer.parseInt(block));

      occupiedStatusLabel.setText("");
      occupiedStatusLabel.setText(this.activeBlock.getOccupied().toString());

      if(this.activeBlock.hasSwitch())
        switchStatusLabel.setText(this.activeBlock.viewSwitchState().toString());
      else
        switchStatusLabel.setText("N/A");

      if(this.activeBlock.getAssociatedCrossing()!=null)
        crossingStatusLabel.setText(this.activeBlock.getAssociatedCrossing().viewCrossingState().toString());
      else
        crossingStatusLabel.setText("N/A");

      if(track.viewLightsMap().get(this.activeBlock)!=null)
        lightsStatusLabel.setText(track.viewLightsMap().get(this.activeBlock).viewLightsState().toString());
      else
        lightsStatusLabel.setText("N/A");

      fwdBlockNum.setText(this.activeBlock.nextBlockForward().blockNum().toString());
      backBlockNum.setText(this.activeBlock.nextBlockBackward().blockNum().toString());

    }
  }

  /**
   * Constructor for GUI of Wayside Controller(s).
   * @param  TrackModel    track - The Global (Real) Track
   * @param  ArrayList<WS> Waysides - Arraylist of all Wayside units
   * @bug Dropdowns not automatically displaying default value or displaying 'nice' fields in JLabels below
   */
  public WaysideGUI(TrackModel track, ArrayList<WS> Waysides) {
      /*for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
    if ("Windows".equals(info.getName()))
    {javax.swing.UIManager.setLookAndFeel(info.getClassName());
    break;}
    }*/
    this.track=track;
    this.Waysides=Waysides;
    JFrame frame = new JFrame();
    this.MainFrame = frame;
    frame.setBounds(100, 100, 789, 379);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    frame.getContentPane().setLayout(null);

    Set<Integer> blockInts = track.viewTrackList().get("Red").get("A").keySet();

    Integer[] intArr = blockInts.toArray(new Integer[blockInts.size()]);
    Set<String> lineSet = track.viewTrackList().keySet();
    String[] lineStrings = lineSet.toArray(new String[lineSet.size()]);

    this.lineDropdown = new JComboBox<String>();
    lineDropdown.setModel(new DefaultComboBoxModel<String>(lineStrings));
    lineDropdown.setToolTipText("");
    lineDropdown.setBounds(10, 11, 137, 20);
    frame.add(lineDropdown);

    String[] segmentStrings = {};
    this.segmentDropdown = new JComboBox<String>();
    segmentDropdown.setModel(new DefaultComboBoxModel<String>(segmentStrings));
    segmentDropdown.setBounds(145, 11, 137, 20);
    frame.add(segmentDropdown);

    String[] blockStrings = {};
    this.blockDropdown = new JComboBox<String>();
    blockDropdown.setModel(new DefaultComboBoxModel<String>(blockStrings));
    blockDropdown.setBounds(281, 11, 130, 20);
    frame.add(blockDropdown);

    lineDropdown.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e){
        String l = (String) lineDropdown.getSelectedItem();
        Set<String> segmentStrings = track.viewTrackList().get(l).keySet();
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
        Set<Integer> blockSet = track.viewTrackList().get(l).get(s).keySet();
        blockDropdown.removeAllItems();
        for (Integer item : blockSet)
        blockDropdown.addItem(Integer.toString(item));
      }
    });

    blockDropdown.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        // updateFields();
      }
    });

    JButton btnSwitch = new JButton("Switch");
    btnSwitch.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(activeBlock.hasSwitch()){
          Boolean currState = activeBlock.viewSwitchState();
          if(currState==true){
            activeBlock.setManualOverride(!activeBlock.getManualOverride());
            activeBlock.getAssociatedSwitch().setSwitchState(false);
          }
          else{
            activeBlock.setManualOverride(!activeBlock.getManualOverride());
            activeBlock.getAssociatedSwitch().setSwitchState(true);
          }
        }
      }
    });

    btnSwitch.setBounds(270, 93, 89, 23);
    frame.getContentPane().add(btnSwitch);

    this.occupiedLabel = new JLabel("Occupied: ");
    occupiedLabel.setBounds(10, 61, 69, 20);
    frame.getContentPane().add(occupiedLabel);

    this.occupiedStatusLabel = new JLabel("N/A");
    occupiedStatusLabel.setBounds(77, 61, 53, 20);
    frame.getContentPane().add(occupiedStatusLabel);

    this.switchLabel = new JLabel("Switch:");
    switchLabel.setBounds(10, 93, 69, 20);
    frame.getContentPane().add(switchLabel);

    this.switchStatusLabel = new JLabel("N/A");
    switchStatusLabel.setBounds(78, 93, 69, 20);
    frame.getContentPane().add(switchStatusLabel);

    this.crossingLabel = new JLabel("Crossing:");
    crossingLabel.setBounds(10, 128, 69, 20);
    frame.getContentPane().add(crossingLabel);

    this.crossingStatusLabel = new JLabel("N/A");
    crossingStatusLabel.setBounds(77, 128, 69, 20);
    frame.getContentPane().add(crossingStatusLabel);

    this.lightsLabel = new JLabel("Lights:");
    lightsLabel.setBounds(10, 159, 69, 20);
    frame.getContentPane().add(lightsLabel);

    this.lightsStatusLabel = new JLabel("N/A");
    lightsStatusLabel.setBounds(76, 159, 69, 20);
    frame.getContentPane().add(lightsStatusLabel);

    JLabel notificationsLabel = new JLabel("Notifications");
    notificationsLabel.setBounds(448, 14, 92, 14);
    frame.add(notificationsLabel);


    notifConsole = new JTextArea();
    JScrollPane Scroller = new JScrollPane(notifConsole);
    Scroller.setBounds(421, 29, 339, 295);
    frame.getContentPane().add(Scroller);
    notifConsole.setBounds(421, 29, 339, 295);
    notifConsole.setTabSize(8);


    JLabel lblMurphy = new JLabel("Murphy");
    lblMurphy.setFont(new Font("Tahoma", Font.PLAIN, 14));
    lblMurphy.setBounds(10, 455, 69, 18);
    frame.getContentPane().add(lblMurphy);

    JComboBox<String> murphyDropdown = new JComboBox<String>();
    murphyDropdown.setModel(new DefaultComboBoxModel<String>(new String[] {"Select", "Comm Failure with CTC", "Comm Failure with Track", "Wayside Failure"}));
    murphyDropdown.setBounds(10, 294, 143, 20);
    frame.getContentPane().add(murphyDropdown);

    /*
    @bug Failure button doesn't print to Notifications console
    */
    JButton failBtn = new JButton("Fail");
    failBtn.setBounds(163, 293, 60, 23);
    failBtn.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        // added to test CTC broken Notifications
        Block brokenBlock1 = track.getBlock("Red", "C", 8);
        brokenBlock1.setBroken(true);
        Block brokenBlock2 = track.getBlock("Red", "H", 25);
        brokenBlock2.setBroken(true);
        // end
        failBtn.setActionCommand(murphyDropdown.getSelectedItem().toString());
        if(!failBtn.getActionCommand().equals("Select"))
        printNotification(failBtn.getActionCommand() + "!!!");
      }
    });
    frame.getContentPane().add(failBtn);

    JTextField txtEnterPlcFile = new JTextField();
    txtEnterPlcFile.setText("Enter PLC file path here...");
    txtEnterPlcFile.setBounds(10, 237, 220, 20);
    frame.getContentPane().add(txtEnterPlcFile);
    txtEnterPlcFile.setColumns(10);


    JButton loadPLCBtn = new JButton("Load");
    loadPLCBtn.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        loadPLCBtn.setActionCommand(txtEnterPlcFile.getText());
        try {
          tryPLCFile(loadPLCBtn.getActionCommand());	//see if selected is a valid file
        } catch (IOException | ScriptException e1) {
          e1.printStackTrace();
        }
      }
    });
    loadPLCBtn.setBounds(240, 233, 69, 23);
    frame.getContentPane().add(loadPLCBtn);

    JLabel PLCLabel = new JLabel("Load PLC File");
    PLCLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
    PLCLabel.setBounds(10, 190, 115, 18);
    frame.getContentPane().add(PLCLabel);

    JButton button = new JButton("Browse");
    button.setBounds(311, 233, 79, 23);
    frame.getContentPane().add(button);

    ArrayList<String> names = new ArrayList<String>();
    String [] namesArray = new String [Waysides.size()];
    for(WS ws : this.Waysides)
      names.add(ws.line + ws.number);
    this.wsSelectDropdown = new JComboBox<String>();
    wsSelectDropdown.setModel(new DefaultComboBoxModel<String>(names.toArray(namesArray)));
    wsSelectDropdown.setBounds(30, 213, 89, 20);
    frame.getContentPane().add(wsSelectDropdown);

    JLabel wsSelectLabel = new JLabel("WS");
    wsSelectLabel.setBounds(10, 213, 26, 20);
    frame.getContentPane().add(wsSelectLabel);

    this.fwdBlockNum = new JLabel("N/A");
    fwdBlockNum.setBounds(145, 64, 46, 14);
    frame.getContentPane().add(fwdBlockNum);

    JLabel fwdLabel = new JLabel("Forward");
    fwdLabel.setBounds(145, 42, 46, 14);
    frame.getContentPane().add(fwdLabel);

    this.backBlockNum = new JLabel("N/A");
    backBlockNum.setBounds(220, 64, 46, 14);
    frame.getContentPane().add(backBlockNum);

    JLabel backLabel = new JLabel("Backward");
    backLabel.setBounds(220, 42, 46, 14);
    frame.getContentPane().add(backLabel);

    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e){
        //open file browser
        JFileChooser PLCChoose = new JFileChooser();
        PLCChoose.setFileFilter(new FileNameExtensionFilter("PLC Files (.plc)", "plc"));
        int response = PLCChoose.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION){
          txtEnterPlcFile.setText(PLCChoose.getSelectedFile().getAbsolutePath());
        } else {
        }
      }
    });
  }

  /**
  * Tries to open give PLC file and checks for existence
  * @param filename - user entered filename/path of PLC Code File
  * @throws IOException
  * @throws ScriptException
  * @bug PLC file not being set to wayside(s) or print success to console, or clear loading bar.
  */
  public boolean tryPLCFile(String filename) throws IOException, ScriptException{
    File PLCFile= new File(filename);
    if(PLCFile.exists()){
      String wsName = (String) wsSelectDropdown.getSelectedItem();
      for(WS ws : Waysides){
        if(ws.name.equals(wsName)){
          PLC plc = new PLC(track, PLCFile, ws.line);
          plc.parse();
          ws.setPlc(plc);
          printNotification("PLC Data loaded to " + ws.name);
        }
      }
      return true;
    }
    else{
      JOptionPane.showMessageDialog(new Frame(),"Specified file was not found", "Invalid Filename", JOptionPane.WARNING_MESSAGE);
      return false;
    }
  }

  /**
  * Prints to nofication area
  * @param String toPrint string to print out to notification area.
  */
  public void printNotification(String toPrint){
    notifConsole.setText(notifConsole.getText()+ "\n" + toPrint);
  }

  /**
  * Gets frame to launch GUI from launcher.
  * @return Frame of GUI class
  */
  public JFrame getFrame(){
    return this.MainFrame;
  }

}
