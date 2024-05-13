package com.v.im;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

public class Manager extends JFrame {
	
	private JFrame self;
	private JPanel contentPane;
	private CardLayout tabToggle, cardLayout;
	
	private JPanel pnlTabToggle, pnlContent;
	private JPanel pnlCreate, pnlConnect, pnlManageConnection;
	
	private static final long serialVersionUID = 1L;
	
	private Main main;

	public Manager(Main main) {
		this.main = main;
		self = this;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(300, 270);
		setTitle("Manage Connection");
		
		initManager();
		
		initServerCard(pnlCreate);
		initClientCard(pnlConnect);
		initManageCard(pnlManageConnection);
		
		tabToggle.show(pnlTabToggle, "Init");
		cardLayout.show(pnlContent, "Manage");
		
		setLocationRelativeTo(null);
		setVisible(true);
		
		requestFocus();
	}
	
	public Manager(Main main, boolean isLive) {
		this.main = main;
		self = this;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(300, 270);
		setTitle("Manage Server");
		
		initManager();
		
		initServerCard(pnlCreate);
		initClientCard(pnlConnect);
		initManageCard(pnlManageConnection);
		
		if(isLive) {
			tabToggle.show(pnlTabToggle, "Manage");
			cardLayout.show(pnlContent, "Manage");
		} else {
			tabToggle.show(pnlTabToggle, "Init");
			cardLayout.show(pnlContent, "Create");
		}
		
		
		setLocationRelativeTo(null);
		setVisible(true);
		
		requestFocus();
	}
	
	private void initManager() {
		contentPane = new JPanel();
		setContentPane(contentPane);
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 300 };
		layout.rowHeights = new int[] { 25, 275 };
		layout.columnWeights = new double[] { 1.0 };
		layout.rowWeights = new double[] { 0.0, 1.0 };
		contentPane.setLayout(layout);
		
		pnlTabToggle = new JPanel();
		pnlTabToggle.setBorder(new MatteBorder(1, 1, 0, 1, Color.BLACK));
		GridBagConstraints pnlTabToggleConstraints = new GridBagConstraints();
		pnlTabToggleConstraints.insets = new Insets(0, 0, 0, 0);
		pnlTabToggleConstraints.fill = GridBagConstraints.BOTH;
		pnlTabToggleConstraints.gridx = 0;
		pnlTabToggleConstraints.gridy = 0;
		contentPane.add(pnlTabToggle, pnlTabToggleConstraints);
		pnlTabToggle.setLayout(tabToggle = new CardLayout());
		
		JPanel pnlTabs = new JPanel();
		GridBagLayout pnlTabsLayout = new GridBagLayout();
		pnlTabsLayout.columnWidths = new int[] { 150, 150 };
		pnlTabsLayout.rowHeights = new int[] { 25 };
		pnlTabsLayout.columnWeights = new double[] { 1.0, 1.0 };
		pnlTabsLayout.rowWeights = new double[] { 1.0 };
		pnlTabToggle.add("Init", pnlTabs);
		pnlTabs.setLayout(pnlTabsLayout);
		
		JPanel pnlManage = new JPanel();
		GridBagLayout pnlManageLayout = new GridBagLayout();
		pnlManageLayout.columnWidths = new int[] { 300 };
		pnlManageLayout.rowHeights = new int[] { 25 };
		pnlManageLayout.columnWeights = new double[] { 1.0 };
		pnlManageLayout.rowWeights = new double[] { 1.0 };
		pnlTabToggle.add("Manage", pnlManage);
		pnlManage.setLayout(pnlManageLayout);
		
		JButton btnManage = new JButton("Manage");
		btnManage.setEnabled(false);
		GridBagConstraints btnManageConstraints = new GridBagConstraints();
		btnManageConstraints.insets = new Insets(0, 0, 0, 0);
		btnManageConstraints.fill = GridBagConstraints.BOTH;
		btnManageConstraints.gridx = 0;
		btnManageConstraints.gridy = 0;
		pnlManage.add(btnManage, btnManageConstraints);
		
		pnlContent = new JPanel();
		pnlContent.setBorder(new LineBorder(Color.BLACK, 1));
		GridBagConstraints pnlContentConstraints = new GridBagConstraints();
		pnlContentConstraints.insets = new Insets(0, 0, 0, 0);
		pnlContentConstraints.fill = GridBagConstraints.BOTH;
		pnlContentConstraints.gridx = 0;
		pnlContentConstraints.gridy = 1;
		contentPane.add(pnlContent, pnlContentConstraints);
		pnlContent.setLayout(cardLayout = new CardLayout());
		
		// After our card panel is declared, otherwise we'd declare it in the scope of the class privately
		
		JButton tabCreate = new JButton("Create");
		GridBagConstraints tabCreateConstraints = new GridBagConstraints();
		tabCreateConstraints.insets = new Insets(0, 0, 0, 0);
		tabCreateConstraints.fill = GridBagConstraints.BOTH;
		tabCreateConstraints.gridx = 0;
		tabCreateConstraints.gridy = 0;
		pnlTabs.add(tabCreate, tabCreateConstraints);
		tabCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pnlContent, "Create");
			}
		});
		tabCreate.setOpaque(false);
		tabCreate.setContentAreaFilled(false);

		JButton tabConnect = new JButton("Connect");
		GridBagConstraints tabConnectConstraints = new GridBagConstraints();
		tabConnectConstraints.insets = new Insets(0, 0, 0, 0);
		tabConnectConstraints.fill = GridBagConstraints.BOTH;
		tabConnectConstraints.gridx = 1;
		tabConnectConstraints.gridy = 0;
		pnlTabs.add(tabConnect, tabConnectConstraints);
		tabConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(pnlContent, "Connect");
			}
		});
		tabConnect.setOpaque(false);
		tabConnect.setContentAreaFilled(false);
		
		pnlCreate = new JPanel();
		GridBagLayout pnlCreateLayout = new GridBagLayout();
		pnlCreateLayout.columnWidths = new int[] { 250 };
		pnlCreateLayout.rowHeights = new int[] { 65, 65, 65, 50 };
		pnlCreateLayout.columnWeights = new double[] { 0.0 };
		pnlCreateLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		pnlContent.add("Create", pnlCreate);
		pnlCreate.setLayout(pnlCreateLayout);
		
		pnlConnect = new JPanel();
		GridBagLayout pnlConnectLayout = new GridBagLayout();
		pnlConnectLayout.columnWidths = new int[] { 250 };
		pnlConnectLayout.rowHeights = new int[] { 65, 65, 65, 50 };
		pnlConnectLayout.columnWeights = new double[] { 0.0 };
		pnlConnectLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		pnlContent.add("Connect", pnlConnect);
		pnlConnect.setLayout(pnlConnectLayout);
		
		pnlManageConnection = new JPanel();
		GridBagLayout pnlManageConnectionLayout = new GridBagLayout();
		pnlManageConnectionLayout.columnWidths = new int[] { 250 };
		pnlManageConnectionLayout.rowHeights = new int[] { 65, 65, 65, 50 };
		pnlManageConnectionLayout.columnWeights = new double[] { 0.0 };
		pnlManageConnectionLayout.rowWeights = new double[] { 1.0, 1.0, 1.0 };
		pnlContent.add("Manage", pnlManageConnection);
		pnlManageConnection.setLayout(pnlManageConnectionLayout);
	}
	
	
	private void initServerCard(JPanel pnlCreate) {
		JPanel pnlServerName = new JPanel();
		pnlServerName.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "Server Name", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		GridBagConstraints pnlCreateNameConstraints = new GridBagConstraints();
		pnlCreateNameConstraints.insets = new Insets(0, 0, 0, 0);
		pnlCreateNameConstraints.fill = GridBagConstraints.HORIZONTAL;
		pnlCreateNameConstraints.gridx = 0;
		pnlCreateNameConstraints.gridy = 0;
		pnlCreate.add(pnlServerName, pnlCreateNameConstraints);
		pnlServerName.setLayout(new BorderLayout());
		
		JTextField txtServerName = new JTextField();
		txtServerName.setHorizontalAlignment(JTextField.CENTER);
		pnlServerName.add(txtServerName);
		
		JPanel pnlPort = new JPanel();
		pnlPort.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "Port", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		GridBagConstraints pnlPortConstraints = new GridBagConstraints();
		pnlPortConstraints.insets = new Insets(0, 0, 0, 0);
		pnlPortConstraints.fill = GridBagConstraints.HORIZONTAL;
		pnlPortConstraints.gridx = 0;
		pnlPortConstraints.gridy = 1;
		pnlCreate.add(pnlPort, pnlPortConstraints);
		pnlPort.setLayout(new BorderLayout());
		
		NumberFormat txtPortFormat = NumberFormat.getIntegerInstance();
		txtPortFormat.setGroupingUsed(false);
		
		NumberFormatter txtPortFormatter = new NumberFormatter(txtPortFormat);
		txtPortFormatter.setValueClass(Integer.class);
		txtPortFormatter.setMinimum(0);
		txtPortFormatter.setMaximum(65535);	// Highest assignable port is 0xFFFF = (1 << 16) -1 = 65535
		txtPortFormatter.setAllowsInvalid(false);
		txtPortFormatter.setCommitsOnValidEdit(true);
		
		JFormattedTextField txtPort = new JFormattedTextField(txtPortFormatter);
		txtPort.setText(Integer.toString(0));
		txtPort.setHorizontalAlignment(JTextField.CENTER);
		pnlPort.add(txtPort, BorderLayout.CENTER);
		txtPort.setColumns(10);
		txtPort.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				String keyText = KeyEvent.getKeyText(e.getKeyCode());
				
				/*		Because we are using a JFormattedTextField with our said format
				 * 										we are unable to delete any final characters that would
				 * 										render the text field empty
				 * 										we can bypass this by adding a listener
				 * 										checking for backspace and delete key presses
				 * 										and in their respective functional caret positions
				 * 										set the value of the field to its minimum
				 * 										a port value of 0 will indicate to use some open port not defined
				 * 										that can be fetched later
				 * */
				if(txtPort.getText().length() == 1)
					switch(txtPort.getCaretPosition()) {
						case(0):
							if(keyText.equals("Delete"))
								txtPort.setText(Integer.toString(0));
						break;
							
						case(1):
							if(keyText.equals("Backspace"))
								txtPort.setText(Integer.toString(0));
						break;
					}
			}
			
			public void keyReleased(KeyEvent e) {}
			
			public void keyTyped(KeyEvent e) {}
		});
		
		JPanel pnlDisplayName = new JPanel();
		pnlDisplayName.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "Display Name", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		GridBagConstraints pnlDisplayNameConstraints = new GridBagConstraints();
		pnlDisplayNameConstraints.insets = new Insets(0, 0, 0, 0);
		pnlDisplayNameConstraints.fill = GridBagConstraints.HORIZONTAL;
		pnlDisplayNameConstraints.gridx = 0;
		pnlDisplayNameConstraints.gridy = 2;
		pnlCreate.add(pnlDisplayName, pnlDisplayNameConstraints);
		pnlDisplayName.setLayout(new BorderLayout());
		
		JTextField txtDisplayName = new JTextField();
		txtDisplayName.setHorizontalAlignment(JTextField.CENTER);
		pnlDisplayName.add(txtDisplayName, BorderLayout.CENTER);
		txtDisplayName.setColumns(10);
		
		JButton btnLaunch = new JButton("Launch");
		GridBagConstraints btnLaunchConstraints = new GridBagConstraints();
		btnLaunchConstraints.insets = new Insets(0, 0, 0, 0);
		btnLaunchConstraints.fill = GridBagConstraints.CENTER;
		btnLaunchConstraints.gridx = 0;
		btnLaunchConstraints.gridy = 3;
		pnlCreate.add(btnLaunch, btnLaunchConstraints);
		btnLaunch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isValid = true;
				int portConfirm = 0;
				
				String serverName, displayName;
				int port;
				
				serverName = txtServerName.getText();
				port = Integer.parseInt(txtPort.getText());
				displayName = txtDisplayName.getText();
				
				if(serverName.isBlank()) {
					JOptionPane.showMessageDialog(self, "Server name cannot be blank.");
					
					isValid = false;
				}
				
				if(displayName.isBlank()) {
					JOptionPane.showMessageDialog(self, "Display name cannot be blank.");
					
					isValid = false;
				}
				
				/*		For Confirm Dialog returns
				 * 		-1 = Closed Dialog
				 * 		 0 = Yes
				 * 		 1 = No
				 * 		 2 = Canceled
				 * */
				if(isValid)
					if(port == 0)
						portConfirm = JOptionPane.showConfirmDialog(self, "Port will be randomly assigned, is this ok?");
				
				if(isValid) {
					if(port != 0 || (port == 0 && portConfirm == 0)) {
						initController(main, serverName, port, displayName);
						
						self.dispose();
					}
				}
			}
		});
	}
	
	private void initClientCard(JPanel pnlConnect) {
		JPanel pnlDisplayName = new JPanel();
		pnlDisplayName.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "Display Name", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		GridBagConstraints pnlDisplayNameConstraints = new GridBagConstraints();
		pnlDisplayNameConstraints.insets = new Insets(0, 0, 0, 0);
		pnlDisplayNameConstraints.fill = GridBagConstraints.HORIZONTAL;
		pnlDisplayNameConstraints.gridx = 0;
		pnlDisplayNameConstraints.gridy = 0;
		pnlConnect.add(pnlDisplayName, pnlDisplayNameConstraints);
		pnlDisplayName.setLayout(new BorderLayout(0, 0));
		
		JTextField txtDisplayName = new JTextField();
		txtDisplayName.setHorizontalAlignment(JTextField.CENTER);
		pnlDisplayName.add(txtDisplayName, BorderLayout.CENTER);
		txtDisplayName.setColumns(10);
		
		JPanel pnlCreateAddress = new JPanel();
		pnlCreateAddress.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "Address", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		GridBagConstraints pnlCreateNameConstraints = new GridBagConstraints();
		pnlCreateNameConstraints.insets = new Insets(0, 0, 0, 0);
		pnlCreateNameConstraints.fill = GridBagConstraints.HORIZONTAL;
		pnlCreateNameConstraints.gridx = 0;
		pnlCreateNameConstraints.gridy = 1;
		pnlConnect.add(pnlCreateAddress, pnlCreateNameConstraints);
		pnlCreateAddress.setLayout(new BorderLayout(0, 0));
		
		JTextField txtServerAddress = new JTextField();
		txtServerAddress.setHorizontalAlignment(JTextField.CENTER);
		pnlCreateAddress.add(txtServerAddress);
		txtServerAddress.setColumns(10);
		
		JPanel pnlPort = new JPanel();
		pnlPort.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "Port", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		GridBagConstraints pnlPortConstraints = new GridBagConstraints();
		pnlPortConstraints.insets = new Insets(0, 0, 0, 0);
		pnlPortConstraints.fill = GridBagConstraints.HORIZONTAL;
		pnlPortConstraints.gridx = 0;
		pnlPortConstraints.gridy = 2;
		pnlConnect.add(pnlPort, pnlPortConstraints);
		pnlPort.setLayout(new BorderLayout(0, 0));
		
		NumberFormat txtPortFormat = NumberFormat.getIntegerInstance();
		txtPortFormat.setGroupingUsed(false);
		
		NumberFormatter txtPortFormatter = new NumberFormatter(txtPortFormat);
		txtPortFormatter.setValueClass(Integer.class);
		txtPortFormatter.setMinimum(0);
		txtPortFormatter.setMaximum(65535);	// Highest assignable port is 0xFFFF = (1 << 16) -1 = 65535
		txtPortFormatter.setAllowsInvalid(false);
		txtPortFormatter.setCommitsOnValidEdit(true);
		
		JFormattedTextField txtPort = new JFormattedTextField(txtPortFormatter);
		txtPort.setText(Integer.toString(0));
		txtPort.setHorizontalAlignment(JTextField.CENTER);
		pnlPort.add(txtPort, BorderLayout.CENTER);
		txtPort.setColumns(10);
		txtPort.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				String keyText = KeyEvent.getKeyText(e.getKeyCode());
				
				/*		Because we are using a JFormattedTextField with our said format
				 * 										we are unable to delete any final characters that would
				 * 										render the text field empty
				 * 										we can bypass this by adding a listener
				 * 										checking for backspace and delete key presses
				 * 										and in their respective functional caret positions
				 * 										set the value of the field to its minimum
				 * 										a port value of 0 will indicate to use some open port not defined
				 * 										that can be fetched later
				 * */
				if(txtPort.getText().length() == 1)
					switch(txtPort.getCaretPosition()) {
						case(0):
							if(keyText.equals("Delete"))
								txtPort.setText(Integer.toString(0));
						break;
							
						case(1):
							if(keyText.equals("Backspace"))
								txtPort.setText(Integer.toString(0));
						break;
					}
			}
			
			public void keyReleased(KeyEvent e) {}
			
			public void keyTyped(KeyEvent e) {}
		});
		
		JButton btnConnect = new JButton("Connect");
		GridBagConstraints btnConnectConstraints = new GridBagConstraints();
		btnConnectConstraints.insets = new Insets(0, 0, 0, 0);
		btnConnectConstraints.fill = GridBagConstraints.CENTER;
		btnConnectConstraints.gridx = 0;
		btnConnectConstraints.gridy = 3;
		pnlConnect.add(btnConnect, btnConnectConstraints);
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isValid = true;
				
				String serverAddress, userName;
				int serverPort;
				
				serverAddress = txtServerAddress.getText();
				serverPort = Integer.parseInt(txtPort.getText());
				userName = txtDisplayName.getText();
				
				if(serverAddress.isBlank()) {
					JOptionPane.showMessageDialog(self, "Server name cannot be blank.");
					
					isValid = false;
				}
				
				if(userName.isBlank()) {
					JOptionPane.showMessageDialog(self, "Display name cannot be blank.");
					
					isValid = false;
				}
				
				if(isValid)
					if(serverPort == 0)
						JOptionPane.showMessageDialog(self, "Port must be non-zero positive integer");
					else {
						initController(main, userName, serverAddress, serverPort);
						
						self.dispose();
					}
			}
		});
	}
	
	private void initManageCard(JPanel pnlManageConnection) {
		JPanel pnlDisplayName = new JPanel();
		pnlDisplayName.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "Display Name", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		GridBagConstraints pnlDisplayNameConstraints = new GridBagConstraints();
		pnlDisplayNameConstraints.insets = new Insets(0, 0, 0, 0);
		pnlDisplayNameConstraints.fill = GridBagConstraints.HORIZONTAL;
		pnlDisplayNameConstraints.gridx = 0;
		pnlDisplayNameConstraints.gridy = 0;
		pnlManageConnection.add(pnlDisplayName, pnlDisplayNameConstraints);
		pnlDisplayName.setLayout(new BorderLayout(0, 0));
		
		JTextField txtDisplayName = new JTextField();
		txtDisplayName.setText(main.getUserName());
		txtDisplayName.setEditable(false);
		txtDisplayName.setHorizontalAlignment(JTextField.CENTER);
		pnlDisplayName.add(txtDisplayName, BorderLayout.CENTER);
		txtDisplayName.setColumns(10);
		
		JPanel pnlServerName = new JPanel();
		pnlServerName.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "Server Name", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		GridBagConstraints pnlServerNameConstraints = new GridBagConstraints();
		pnlServerNameConstraints.insets = new Insets(0, 0, 0, 0);
		pnlServerNameConstraints.fill = GridBagConstraints.HORIZONTAL;
		pnlServerNameConstraints.gridx = 0;
		pnlServerNameConstraints.gridy = 1;
		pnlManageConnection.add(pnlServerName, pnlServerNameConstraints);
		pnlServerName.setLayout(new BorderLayout(0, 0));
		
		JTextField txtServerName = new JTextField();
		txtServerName.setText(main.getServerName());
		txtServerName.setEditable(false);
		txtServerName.setHorizontalAlignment(JTextField.CENTER);
		pnlServerName.add(txtServerName, BorderLayout.CENTER);
		txtServerName.setColumns(10);
		
		JPanel pnlPort = new JPanel();
		pnlPort.setBorder(new TitledBorder(new EmptyBorder(0, 0, 0, 0), "Port", TitledBorder.CENTER, TitledBorder.ABOVE_TOP));
		GridBagConstraints pnlPortConstraints = new GridBagConstraints();
		pnlPortConstraints.insets = new Insets(0, 0, 0, 0);
		pnlPortConstraints.fill = GridBagConstraints.HORIZONTAL;
		pnlPortConstraints.gridx = 0;
		pnlPortConstraints.gridy = 2;
		pnlManageConnection.add(pnlPort, pnlPortConstraints);
		pnlPort.setLayout(new BorderLayout(0, 0));
		
		JTextField txtPort = new JTextField();
		txtPort.setText(Integer.toString(main.getPort()));
		txtPort.setEditable(false);
		txtPort.setHorizontalAlignment(JTextField.CENTER);
		pnlPort.add(txtPort, BorderLayout.CENTER);
		txtPort.setColumns(10);
		
		JButton btnTerminate = new JButton();
		if(main.isServer())
			btnTerminate.setText("Close Server");
		else
			btnTerminate.setText("Disconnect");
		GridBagConstraints btnTerminateConstraints = new GridBagConstraints();
		btnTerminateConstraints.insets = new Insets(0, 0, 0, 0);
		btnTerminateConstraints.fill = GridBagConstraints.CENTER;
		btnTerminateConstraints.gridx = 0;
		btnTerminateConstraints.gridy = 3;
		pnlManageConnection.add(btnTerminate, btnTerminateConstraints);
		btnTerminate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				main.closeSocket();
				
				self.dispose();
			}
		});
	}
	
	private void initController(Main client, String userName, String serverAddress, int serverPort) {
		new com.v.im.client.Controller(client).attemptConnect(userName, serverAddress, serverPort);;
	}
	
	private void initController(Main server, String serverName, int port, String displayName) {
		new com.v.im.server.Controller(server, serverName, port, displayName);
	}

}
