package com.v.im;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import com.v.im.client.Client;
import com.v.im.server.Controller;

public class Main extends JFrame {
	
	static final String strDefaultTitle = "EZ IM - Client";
	
	public static final int CASE_NORMAL = 0;
	public static final int CASE_SHUTDOWN = 1;
	public static final int CASE_UNEXPECTED = 2;
	public static final int CASE_KICKED = 3;

	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<String> lstUsersModel;
	
	private boolean isLive;
	
	private JTextArea txtChat;
	private JButton btnSend;
	
	private String userName, serverName;
	
	private boolean isServer;
	private Controller serverController;
	private Client clientController;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main main = new Main();
					main.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Main() {
		Main self = this;
		isLive = false;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 400);
		setTitle(strDefaultTitle);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 150, 450 };
		layout.rowHeights = new int[] { 375, 25 };
		layout.columnWeights = new double[]{ 0.0, 1.0 };
		layout.rowWeights = new double[]{ 1.0, 0.0 };
		contentPane.setLayout(layout);
		
		JPanel pnlUsers = new JPanel();
		pnlUsers.setBackground(Color.white);								// Match the JList background below
		pnlUsers.setBorder(new LineBorder(Color.BLACK, 1));
		GridBagConstraints pnlUsersConstraints = new GridBagConstraints();
		pnlUsersConstraints.insets = new Insets(0, 0, 5, 5);
		pnlUsersConstraints.fill = GridBagConstraints.BOTH;
		pnlUsersConstraints.gridx = 0;
		pnlUsersConstraints.gridy = 0;
		contentPane.add(pnlUsers, pnlUsersConstraints);
		GridBagLayout pnlUsersLayout = new GridBagLayout();
		pnlUsersLayout.columnWidths = new int[]{ 150 };
		pnlUsersLayout.rowHeights = new int[]{ 25, 350 };
		pnlUsersLayout.columnWeights = new double[]{ 1.0 };
		pnlUsersLayout.rowWeights = new double[]{ 0.0, 1.0 };
		pnlUsers.setLayout(pnlUsersLayout);
		
		JLabel lblUsers = new JLabel("Users");
		GridBagConstraints lblUsersConstraints = new GridBagConstraints();
		lblUsersConstraints.insets = new Insets(0, 0, 5, 0);
		lblUsersConstraints.gridx = 0;
		lblUsersConstraints.gridy = 0;
		pnlUsers.add(lblUsers, lblUsersConstraints);
		
		lstUsersModel = new DefaultListModel<String>();
		
		JList<String> lstUsers = new JList<String>(lstUsersModel);
		lstUsers.setBorder(new MatteBorder(1, 0, 0, 0, Color.BLACK));		// Because the parent panel has border we only need the top portion as to avoid overlap
		GridBagConstraints listUsersConstraints = new GridBagConstraints();	//														and extra thickness
		listUsersConstraints.fill = GridBagConstraints.BOTH;				// (Note. TitledBorder exists but I personally did not like the look or the white background
		listUsersConstraints.gridx = 0;										//														overlapping past the border
		listUsersConstraints.gridy = 1;
		pnlUsers.add(lstUsers, listUsersConstraints);
		
		txtChat = new JTextArea();
		txtChat.setEditable(false);
		txtChat.setLineWrap(true);
		txtChat.setWrapStyleWord(true);
		
		JScrollPane txtChatScroll = new JScrollPane(txtChat);				// Scroll functionality to text area when filled with text 
		txtChatScroll.setBorder(new LineBorder(Color.BLACK, 1));
		txtChatScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints txtChatScrollConstraints = new GridBagConstraints();
		txtChatScrollConstraints.insets = new Insets(0, 0, 5, 0);
		txtChatScrollConstraints.fill = GridBagConstraints.BOTH;
		txtChatScrollConstraints.gridx = 1;
		txtChatScrollConstraints.gridy = 0;
		contentPane.add(txtChatScroll, txtChatScrollConstraints);
		
		JButton btnManage = new JButton("Manage Connection");
		GridBagConstraints btnManageConstraints = new GridBagConstraints();
		btnManageConstraints.insets = new Insets(5, 0, 5, 5);
		btnManageConstraints.fill = GridBagConstraints.HORIZONTAL;
		btnManageConstraints.gridx = 0;
		btnManageConstraints.gridy = 1;
		contentPane.add(btnManage, btnManageConstraints);
		btnManage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Manager(self, isLive);
			}
		});
		
		// JPanel for the JTextField and JButton for organizing them to certain sizes
		JPanel pnlMsg = new JPanel();
		GridBagConstraints  pnlMsgConstraints = new GridBagConstraints();
		pnlMsgConstraints.fill = GridBagConstraints.BOTH;
		pnlMsgConstraints.gridx = 1;
		pnlMsgConstraints.gridy = 1;
		contentPane.add(pnlMsg, pnlMsgConstraints);
		GridBagLayout pnlMsgLayout = new GridBagLayout();
		pnlMsgLayout.columnWidths = new int[] { 400, 100 };
		pnlMsgLayout.rowHeights = new int[] { 25 };
		pnlMsgLayout.columnWeights = new double[]{ 1.0, 0.0 };
		pnlMsgLayout.rowWeights = new double[]{0.0};
		pnlMsg.setLayout(pnlMsgLayout);
		
		JTextField txtMsg = new JTextField();
		GridBagConstraints txtMsgConstraints = new GridBagConstraints();
		txtMsgConstraints.insets = new Insets(0, 0, 0, 5);
		txtMsgConstraints.fill = GridBagConstraints.BOTH;
		txtMsgConstraints.gridx = 0;
		txtMsgConstraints.gridy = 0;
		pnlMsg.add(txtMsg, txtMsgConstraints);
		txtMsg.setColumns(10);
		
		// Hitting enter in the text field emulates the button
		txtMsg.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent event) {}
			
			@Override
			public void keyPressed(KeyEvent event) {
				if(KeyEvent.getKeyText(event.getKeyCode()) == "Enter")
					btnSend.doClick();
			}
			
			public void keyReleased(KeyEvent event) {}
		});
		
		btnSend = new JButton("Send");
		GridBagConstraints btnSendConstraints = new GridBagConstraints();
		btnSendConstraints.fill = GridBagConstraints.HORIZONTAL;
		btnSendConstraints.gridx = 1;
		btnSendConstraints.gridy = 0;
		pnlMsg.add(btnSend, btnSendConstraints);
		
		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!isLive)
					return;
				
				String msg = txtMsg.getText();
				
				if(msg.isBlank())
					return;
				
				if(isServer) {
					serverController.sendMessageToClients(userName, msg);
					
					txtMsg.setText("");
				} else {
					clientController.sendMessage(msg);
					
					txtMsg.setText("");
				}
			}
		});
		
		setLocationRelativeTo(null);
		
		sendToConsole("Create or Connect to a server");
	}
	
	public void goLive(String newUserName) {
		isLive = true;
		
		setUserName(newUserName);
	}
	
	// Consolidated UI to allow both hosting and connecting
	public void closeSocket() {
		if(!isLive)
			return;
		
		if(isServer) {
			serverController.close();
			serverController = null;
			
			closeConnection();
		} else {
			clientController.close(CASE_NORMAL, null);
			clientController = null;

			disconnectAsClient(CASE_NORMAL, null);
		}
	}
	
	// Empty chat, set flag, clear the users, and log
	public void closeConnection() {
		isLive = false;
		this.setTitle(strDefaultTitle);
		lstUsersModel.clear();
		
		sendToConsole("Server has shutdown.", true);
	}
	
	// Empty chat, set flag, clear the users, and log
	public void disconnectAsClient(int caseType, String sub) {
		isLive = false;
		this.setTitle(strDefaultTitle);
		lstUsersModel.clear();
		
		String str = "";
		
		switch(caseType) {
			case(CASE_NORMAL):
				str += " (User Disconnected)";
			break;
			
			case(CASE_SHUTDOWN):
				str += " (Server Shutdown)";
			break;
			
			case(CASE_UNEXPECTED):
				str += " (Server Shutdown / Lost Connection)";
			break;
			
			case(CASE_KICKED):
				str += " (Kicked from Server)";
			break;
		}
		
		sendToConsole("Disconnected from server" + str, true);
		
		if(sub != null)
			sendToConsole("Reason: " + sub);
	}
	
	
	public String getServerName() {
		if(!isLive)
			return "";
		
		return serverName;
	}
	
	public void setServerName(String serverName, boolean isSelf) {
		this.serverName = serverName;
		
		if(isSelf)
			setTitle("EZ IM - " + serverName + " (Hosting)");
		else
			setTitle("EZ IM - " + serverName + " (Connected)");
	}
	
	public void setUserName(String newUserName) {
		userName = newUserName;
	}
	
	public void setController(Controller controller) {
		serverController = controller;
		
		isServer = true;
	}
	
	public void setController(Client controller) {
		clientController = controller;
		
		isServer = false;
	}
	
	public boolean isServer() {
		if(!isLive)
			return false;
		
		return isServer;
	}
	
	public int getPort() {
		if(!isLive)
			return -1;
		
		if(isServer) 
			return serverController.getPort();
		else
			return clientController.getPort();
	}
	
	public String getUserName() {
		if(!isLive)
			return "Offline";
		
		return userName;
	}
	
	// When we want to send a message should only be displayed for the server client
	public void sendToConsole(String msg) {
		txtChat.append(" " + msg + "\n");
	}
	
	// Overload for when we want to clear the console view like when creating new server
	public void sendToConsole(String msg, boolean shouldClear) {
		txtChat.setText("");
		txtChat.append(" " + msg + "\n");
	}
	
	public void addUser(String displayName) {
		lstUsersModel.addElement(displayName);
		
		sendToConsole(displayName + " has joined the server.");
	}
	
	public void addUser(String displayName, boolean hideInChat) {
		lstUsersModel.addElement(displayName);
		
		if(!hideInChat)
			sendToConsole(displayName + " has joined the server.");
	}
	
	// Override user list
	public void updateUsers(String[] newUsers) {
		DefaultListModel<String> lstUsersModelHist = lstUsersModel;
		lstUsersModel.clear();
		
		for(String newUser : newUsers) {
			if(lstUsersModelHist.contains(newUser))
				addUser(newUser, true);
			else
				addUser(newUser, false);
		}
	}
	
	public void updateUsers(String[] newUsers, String lostUser, boolean isKicked) {
		lstUsersModel.clear();
		
		for(String newUser : newUsers) {
			addUser(newUser, true);
		}
		
		sendToConsole(lostUser + " has disconnected.");
	}
	
	public int getUserCount() {
		return lstUsersModel.getSize();
	}
	
	// Appending text chat with a user message
	public void receiveMessage(String fromUser, String msg) {
		sendToConsole(fromUser + ": " + msg);
	}
}
