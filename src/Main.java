import java.util.*;
import java.net.*;
import java.nio.CharBuffer;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonListener;

public class Main {
	static String getHTMLRequest(String url, int port) {
		Socket socketObj = null;
		PrintWriter wtr = null;
		BufferedReader bufRead = null;
		try {
			socketObj = new Socket(url, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			wtr = new PrintWriter(socketObj.getOutputStream(), true);
			bufRead = new BufferedReader(new InputStreamReader(socketObj.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wtr.print("GET / HTTP/1.1 \r\n");
		wtr.print("HOST: "+url+" \r\n");
		wtr.print("Connection: Close \r\n\r\n");
		wtr.flush();
		
		String outStr = "";
		boolean loop = true;
		while(loop) {
			try {
				if(bufRead.ready()) {
					int i=0;
					while(i!=-1) {
						i = bufRead.read();
						outStr+=(char)i;
					}
					loop = false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!outStr.isEmpty()&&!outStr.isBlank()) {
			if(outStr.indexOf("<html>")>-1) {
				outStr = outStr.substring(outStr.indexOf("<html>"));
			}
		}
		return outStr;
	}
	public static void main(String[] args) throws IOException {
		String requestResult = null;
		JFrame frame = new JFrame("browser");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextField urlWindow = new JTextField();
		JButton connectButton = new JButton("Go!");
		JLabel renderPlane = new JLabel();
		JPanel upperBar = new JPanel();
		upperBar.setLayout(new BorderLayout());
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String requestResult = null;
				requestResult = getHTMLRequest(urlWindow.getText(), 80);
				if(requestResult==null) {
					requestResult = "empty response!";
				}
				frame.setName(urlWindow.getText());
				renderPlane.setText(requestResult);
			}
		});
		upperBar.add(urlWindow, BorderLayout.CENTER);
		upperBar.add(connectButton, BorderLayout.EAST);
		JScrollPane renderScrollPane = new JScrollPane(renderPlane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(upperBar, BorderLayout.NORTH);
		c.add(renderPlane, BorderLayout.CENTER);
		frame.setSize(new Dimension(300,500));
		frame.setVisible(true);
	}
}
