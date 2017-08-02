package manbu.dev;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class Main extends JFrame implements ActionListener, MonitorListener {

    private static final long serialVersionUID = 8168145503379462953L;

    private JLabel labelSource = new JLabel("Source");
    private JLabel labelDestination = new JLabel("Destination");

    private JTextField sourceField = new JTextField();
    private JTextField destinationField = new JTextField();

    private JButton sourceChooseButton = new JButton("Choose");
    private JButton destinationChooseButton = new JButton("Choose");

    private JLabel statusLabel = new JLabel("", JLabel.LEFT);
    private JButton startStopSwitchButton = new JButton("Start");

    private FileAlterationMonitor monitor;
    private boolean start = false;

    public Main() {

        this.setSize(640, 200);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        Container container = this.getContentPane();
        container.setLayout(null);

        //row 1
        labelSource.setBounds(20, 20, 80, 40);
        container.add(labelSource);

        sourceField.setBounds(110, 20, 430, 40);
        sourceField.setEditable(false);
        sourceField.setText(History.getSource());
        container.add(sourceField);

        sourceChooseButton.setBounds(550, 20, 70, 40);
        container.add(sourceChooseButton);

        //row 2
        labelDestination.setBounds(20, 70, 80, 40);
        container.add(labelDestination);

        destinationField.setBounds(110, 70, 430, 40);
        destinationField.setEditable(false);
        destinationField.setText(History.getDestination());
        container.add(destinationField);

        destinationChooseButton.setBounds(550, 70, 70, 40);
        container.add(destinationChooseButton);

        //row 3
        statusLabel.setBounds(20, 120, 310, 40);
        statusLabel.setForeground(Color.RED);
        container.add(statusLabel);

        startStopSwitchButton.setBounds(480, 120, 140, 40);
        container.add(startStopSwitchButton);

        //listener
        sourceChooseButton.addActionListener(this);
        destinationChooseButton.addActionListener(this);
        startStopSwitchButton.addActionListener(this);

        this.setTitle("Sync file (by manbu)");
        this.addWindowListener(new MainWindowListener());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setVisible(true);

    }


    public static void main( String[] args ) {

        new Main();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource().equals(sourceChooseButton)) {

            showFileChooser(sourceField);
            History.setSource(sourceField.getText());

        } else if(e.getSource().equals(destinationChooseButton)) {

            showFileChooser(destinationField);
            History.setDestination(destinationField.getText());

        } else if(e.getSource().equals(startStopSwitchButton)) {

            if(! start) {

                start();

            } else {

                stop();

            }
        }
    }

    private void showFileChooser(JTextField textField) {

        String file = textField.getText();

        JFileChooser chooser = new JFileChooser(file);

        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        chooser.showOpenDialog(this);

        File selectedFile = chooser.getSelectedFile();

        if(selectedFile != null) {
            textField.setText(chooser.getSelectedFile().getPath());
        }
    }

    @Override
    public void changed(File file, Action action) {

        String source = sourceField.getText();
        String destination = destinationField.getText();
        String filePath = file.getPath();

        String changeFilePath = StringUtils.difference(source, filePath);

        File targetFile = new File(destination + changeFilePath);

        if(action == Action.CREATE || action == Action.CHANGE) {

            try {

                if(file.isDirectory()) {
                    FileUtils.copyDirectory(file, targetFile);
                } else {
                    FileUtils.copyFile(file, targetFile);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if(action == Action.DELETE) {

            FileUtils.deleteQuietly(targetFile);
        }

        statusLabel.setText(action + "\t" + changeFilePath);
    }

    private void start() {

        String sourceDirectory = StringUtils.trimToEmpty(sourceField.getText());
        String destinationDirectory = StringUtils.trimToEmpty(destinationField.getText());

        if (StringUtils.isNotEmpty(sourceDirectory) && StringUtils.isNotEmpty(destinationDirectory)) {

            FileAlterationObserver observer = new FileAlterationObserver(sourceDirectory);
            observer.addListener(new Monitor(this));

            monitor = null;
            monitor = new FileAlterationMonitor(3000L);
            monitor.addObserver(observer);

            try {
                monitor.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            sourceChooseButton.setEnabled(false);
            destinationChooseButton.setEnabled(false);
            startStopSwitchButton.setText("Stop");
            start =  true;
            statusLabel.setText("start");

        } else {

            JOptionPane.showMessageDialog(null, "Please source and destination directory first!");
        }

    }

    private void stop() {

        try {

            monitor.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        sourceChooseButton.setEnabled(true);
        destinationChooseButton.setEnabled(true);
        startStopSwitchButton.setText("Start");
        start = false;
        statusLabel.setText("stop");
    }

    class MainWindowListener implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {

        }

        @Override
        public void windowClosing(WindowEvent e) {
            System.out.println(e);

            if(monitor != null) {
                if(start) { stop(); }
            }

        }

        @Override
        public void windowClosed(WindowEvent e) { }

        @Override
        public void windowIconified(WindowEvent e) { }

        @Override
        public void windowDeiconified(WindowEvent e) { }

        @Override
        public void windowActivated(WindowEvent e) { }

        @Override
        public void windowDeactivated(WindowEvent e) { }
    }

}
