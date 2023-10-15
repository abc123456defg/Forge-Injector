package com.fyxar.injector;

import javax.swing.*;

import com.sun.tools.attach.AttachNotSupportedException;

import mdlaf.MaterialLookAndFeel;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.themes.MaterialOceanicTheme;
import mdlaf.utils.MaterialColors;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class InjectGui extends JFrame {

	private JTextField filePathTextField;

    public InjectGui() {
        super("Injector");

        try {
        	MaterialLookAndFeel materialLookAndFeel = new MaterialLookAndFeel(new MaterialOceanicTheme());
        	UIManager.setLookAndFeel(materialLookAndFeel);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Create components
        filePathTextField = new JTextField(20);
        JButton selectFileButton = createStyledButton("Choose File");
        JButton injectButton = createStyledButton("Inject");

        // Set layout manager
        setLayout(new BorderLayout());

        // Create panels
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Add components to panels
        inputPanel.add(new JLabel("File path:"));
        inputPanel.add(filePathTextField);
        inputPanel.add(selectFileButton);

        buttonPanel.add(injectButton);

        // Add panels to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Add action listeners
        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooser();
            }
        });

        injectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                injectFile();
            }
        });

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set frame properties
        setSize(600, 150);
        setLocationRelativeTo(null);
        setVisible(true);
        
        setResizable(false);
    }

    @SuppressWarnings("deprecation")
	private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        MaterialUIMovement.add(button, MaterialColors.WHITE);

        return button;
    }

    private void showFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void injectFile() {
        String filePath = filePathTextField.getText();
        
        if(!filePath.toLowerCase().endsWith(".jar")) {
            JOptionPane.showMessageDialog(this, "Failed! This is not a jar file");
            return;
        }
        
        if(!InjectUtil.isMinecraftFound()) {
            JOptionPane.showMessageDialog(this, "Failed! Minecraft not found");
        	return;
        }
        
        try {
			if(InjectUtil.inject(filePath)) {
				 JOptionPane.showMessageDialog(this, "Injected. Now just wait for a few seconds");
			}else {
				JOptionPane.showMessageDialog(this, "Injected with exceptions.");
			}
		} catch (AttachNotSupportedException | IOException e) {
			 JOptionPane.showMessageDialog(this, "Inject failed, don't know why lol.");
		}
    }
    
}