package com.ecommerce.ui;

import com.ecommerce.model.Customer;
import com.ecommerce.model.Notification;
import com.ecommerce.service.NotificationService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationFrame extends JFrame {
 private final Customer customer; private final NotificationService service; private final JTextArea area; private final JLabel count;
 public NotificationFrame(Customer c,NotificationService s){customer=c;service=s;setTitle("SAIVEE - Notification Center");setSize(760,520);setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);setLocationRelativeTo(null);setLayout(new BorderLayout(10,10));count=new JLabel();area=new JTextArea();area.setEditable(false);area.setFont(new Font("Arial",Font.PLAIN,14));JPanel top=new JPanel(new BorderLayout());top.add(new JLabel("  NOTIFICATION CENTER"),BorderLayout.WEST);top.add(count,BorderLayout.EAST);JButton refresh=new JButton("Refresh");JButton mark=new JButton("Mark Selected as Read");JButton close=new JButton("Close");JPanel bottom=new JPanel();bottom.add(refresh);bottom.add(mark);bottom.add(close);add(top,BorderLayout.NORTH);add(new JScrollPane(area),BorderLayout.CENTER);add(bottom,BorderLayout.SOUTH);refresh.addActionListener(e->load());mark.addActionListener(e->markSelected());close.addActionListener(e->dispose());load();}
 private void load(){List<Notification> all=service==null?List.of():service.getCustomerNotifications(customer);count.setText("Unread: "+(service==null?0:service.getUnreadCount(customer))+"  ");StringBuilder b=new StringBuilder("========== SAIVEE NOTIFICATION HISTORY ==========\n\n");if(all.isEmpty())b.append("No notifications available.");else for(Notification n:all)b.append("[ID ").append(n.getNotificationId()).append("] ").append(n.isRead()?"READ":"UNREAD").append("\n").append(n.getType()).append("\n").append(n.getMessage()).append("\n----------------------------------------\n");area.setText(b.toString());}
 private void markSelected(){String selected=JOptionPane.showInputDialog(this,"Enter notification ID to mark as read:");if(selected==null)return;try{service.markNotificationAsRead(Integer.parseInt(selected.trim()));load();}catch(Exception e){JOptionPane.showMessageDialog(this,"Enter a valid notification ID.");}}
}
