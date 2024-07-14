import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.net.SocketException;

public class FileTransferGUI extends JFrame {

    private FileTransfer fileTransfer = new FileTransferImpl();
    private JComboBox<String> ipComboBox;
    private JButton showUsersButton;
    private Map<String, String> onlineUsers;
    private int registeredPort;
    private String localIP;

    private JTextPane clipboardTextPane;
    private JComboBox<String> languageComboBox;
    private JButton sendClipboardButton;
    private JButton refreshClipboardButton;

    public FileTransferGUI() {
        // 修改图标
        ImageIcon icon = new ImageIcon("./picture/剪切板.png");
        setIconImage(icon.getImage());
        // 设置窗口标题
        setTitle("文件传输在线剪切板");

        // 设置窗口大小
        setSize(800, 600);
        // 设置窗口关闭时程序退出
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口在屏幕中心位置
        setLocationRelativeTo(null);

        // 创建主面板并设置为边框布局
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 创建左侧面板并设置为盒布局，垂直排列
        // 创建左侧面板并设置为 GridBagLayout
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // 创建在线用户和文件传输相关组件
        JLabel nameLabel1 = new JLabel("在线用户");
        // 创建一个图标并将其添加到 JLabel
        ImageIcon originalIcon = new ImageIcon("./picture/用户.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH); // 调整图标大小
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        // 将图标添加到 JLabel，并设置图标在文本后面
        nameLabel1.setIcon(resizedIcon);
        nameLabel1.setHorizontalTextPosition(JLabel.RIGHT); // 将文本位置设置为左边

        ipComboBox = new JComboBox<>();
        ipComboBox.setMaximumRowCount(5); // 设置下拉列表中显示的行数
        ipComboBox.setPreferredSize(new Dimension(200, 30));

        showUsersButton = new JButton("Show Online Users");
        // 刷新在线用户图标
        ImageIcon receiveIcon2 = new ImageIcon("./picture/showUsersButton.png");
        Image image2 = receiveIcon2.getImage();
        Image scaledImage2 = image2.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH); // 这里设定图标大小
        ImageIcon scaledIcon2 = new ImageIcon(scaledImage2);
        JButton showUsersButton = new JButton("刷新列表");
        showUsersButton.setLayout(null);
        showUsersButton.setIcon(scaledIcon2);
        // showUsersButton.setText(null); // 移除按钮文本
        // showUsersButton.setBorderPainted(false); // 移除按钮边框
        // showUsersButton.setContentAreaFilled(false); // 移除按钮背景
        showUsersButton.setPreferredSize(new Dimension(200, 40));

        // JLabel languageLabel1 = new JLabel("刷新用户列表：");
        // 右对齐
        // languageLabel1.setHorizontalAlignment(SwingConstants.RIGHT);

        // 添加组件到左侧面板
        // 使用 GridBagConstraints 将组件添加到左侧面板
        // 设置nameLabel1靠顶部
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weighty = 0; // 没有额外的垂直空间
        leftPanel.add(nameLabel1, gbc);

        // 放大ipComboBox
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 水平方向填充
        gbc.weightx = 1; // 占用额外的水平空间
        gbc.weighty = 1; // 占用额外的垂直空间
        leftPanel.add(ipComboBox, gbc);

        // 设置showUsersButton靠底部
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.weighty = 0; // 没有额外的垂直空间
        leftPanel.add(showUsersButton, gbc);

        // 创建右侧面板并设置为边框布局
        JPanel rightPanel = new JPanel(new BorderLayout());

        // 创建剪贴板面板的中心文本区
        clipboardTextPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(clipboardTextPane);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // 创建一个新的面板用于放置发送和接收文件按钮
        JPanel fileTransferButtonPanel = new JPanel(new GridLayout(1, 2));

        // JButton sendButton = new JButton("Send File",new
        // ImageIcon("C:\\Users\\zhb\\Desktop\\图片\\新建文件夹\\QQ图片202212051025292.jpg"));
        // ImageIcon sendIcon = new
        // ImageIcon("C:\\Users\\zhb\\Desktop\\图片\\新建文件夹\\QQ图片202212051025292.jpg");
        // JButton SendButton = new JButton(sendIcon);
        // 不移除背景的版本发送按钮图标
        // ImageIcon sendIcon = new
        // ImageIcon("C:\\\\Users\\\\zhb\\\\Desktop\\\\图片\\\\新建文件夹\\\\QQ图片202212051025292.jpg");
        // Image image = sendIcon.getImage();
        // Image scaledImage = image.getScaledInstance(50, 50,
        // java.awt.Image.SCALE_SMOOTH); // 这里设定图标大小
        // ImageIcon scaledIcon = new ImageIcon(scaledImage);
        // JButton sendButton = new JButton(scaledIcon);
        // sendButton.setPreferredSize(new Dimension(50, 50));
        // 发送按钮图标
        ImageIcon sendIcon = new ImageIcon("./picture/send.png");
        Image image = sendIcon.getImage();
        Image scaledImage = image.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // 这里设定图标大小
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JButton sendButton = new JButton();
        sendButton.setLayout(null);
        sendButton.setIcon(scaledIcon);
        sendButton.setText(null); // 移除按钮文本
        // sendButton.setBorderPainted(false); // 移除按钮边框
        sendButton.setContentAreaFilled(false); // 移除按钮背景
        sendButton.setPreferredSize(new Dimension(30, 30));

        // 接收按钮
        ImageIcon receiveIcon = new ImageIcon("./picture/receive.png");
        Image image1 = receiveIcon.getImage();
        Image scaledImage1 = image1.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // 这里设定图标大小
        ImageIcon scaledIcon1 = new ImageIcon(scaledImage1);
        JButton receiveButton = new JButton();
        receiveButton.setLayout(null);
        receiveButton.setIcon(scaledIcon1);
        receiveButton.setText(null); // 移除按钮文本
        // receiveButton.setBorderPainted(false); // 移除按钮边框
        receiveButton.setContentAreaFilled(false); // 移除按钮背景
        receiveButton.setPreferredSize(new Dimension(30, 30));

        // fileTransferButtonPanel.add(sendButton);
        // fileTransferButtonPanel.add(receiveButton);
        //
        // // 将文件传输按钮面板添加到右侧面板的南部
        // rightPanel.add(fileTransferButtonPanel, BorderLayout.NORTH);
        languageComboBox = new JComboBox<>(new String[] { "纯文本", "Java", "Python", "C++" });
        languageComboBox.setPreferredSize(new Dimension(300, 30));

        // 创建剪贴板面板的底部按钮区
        JPanel clipboardBottomPanel = new JPanel(new GridLayout(1, 2));
        // 更新剪切板的图标
        refreshClipboardButton = new JButton("Refresh");
        ImageIcon refreshIcon = new ImageIcon("./picture/refreshClipboardButton.png");
        Image image4 = refreshIcon.getImage();
        Image scaledImage4 = image4.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // 设置图标大小
        ImageIcon scaledIcon4 = new ImageIcon(scaledImage4);
        JButton refreshClipboardButton = new JButton();
        refreshClipboardButton.setLayout(null);
        refreshClipboardButton.setIcon(scaledIcon4);
        refreshClipboardButton.setText(null); // 移除按钮文本
        // refreshClipboardButton.setBorderPainted(false); // 移除按钮边框
        // refreshClipboardButton.setContentAreaFilled(false); // 移除按钮背景
        refreshClipboardButton.setPreferredSize(new Dimension(30, 30));

        // 剪切板发送图标
        sendClipboardButton = new JButton("Send");
        ImageIcon clipboardIcon = new ImageIcon("./picture/sendClipboardButton.png");
        Image image3 = clipboardIcon.getImage();
        Image scaledImage3 = image3.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH); // 设置图标大小
        ImageIcon scaledIcon3 = new ImageIcon(scaledImage3);
        JButton sendClipboardButton = new JButton();
        sendClipboardButton.setLayout(null);
        sendClipboardButton.setIcon(scaledIcon3);
        sendClipboardButton.setText(null); // 移除按钮文本
        // sendClipboardButton.setBorderPainted(false); // 移除按钮边框
        // sendClipboardButton.setContentAreaFilled(false); // 移除按钮背景
        sendClipboardButton.setPreferredSize(new Dimension(30, 30));

        // 整合
        clipboardBottomPanel.add(sendButton);
        clipboardBottomPanel.add(receiveButton);
        JLabel languageLabel = new JLabel("语言：");
        // 文本加图标
        // 创建一个图标并将其大小调整为所需的尺寸
        ImageIcon originalIcon1 = new ImageIcon("./picture/语言.png"); // 请确保替换为实际的图标路径
        Image originalImage2 = originalIcon1.getImage();
        Image resizedImage3 = originalImage2.getScaledInstance(20, 20, Image.SCALE_SMOOTH); // 调整图标大小
        ImageIcon resizedIcon1 = new ImageIcon(resizedImage3);
        // 将相同的图标添加到 languageLabel，并设置图标在文本后面
        languageLabel.setIcon(resizedIcon1);
        languageLabel.setHorizontalTextPosition(JLabel.RIGHT); // 将文本位置设置为左边

        // 设置字体
        Font font1 = new Font("DialogInput", Font.BOLD, 14);
        Font font2 = new Font("Monospaced", Font.BOLD, 12);
        nameLabel1.setFont(font1);
        showUsersButton.setFont(font1);
        clipboardTextPane.setFont(font1);
        languageLabel.setFont(font1);
        languageComboBox.setFont(font2);
        refreshClipboardButton.setFont(font1);
        sendClipboardButton.setFont(font1);

        // 右对齐
        languageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        clipboardBottomPanel.add(languageLabel);
        clipboardBottomPanel.add(languageComboBox);
        clipboardBottomPanel.add(refreshClipboardButton);
        clipboardBottomPanel.add(sendClipboardButton);

        // 将按钮区添加到剪贴板面板的南部
        rightPanel.add(clipboardBottomPanel, BorderLayout.SOUTH);

        // 将左右面板添加到主面板的相应区域
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // 将主面板设置为JFrame的内容面板
        setContentPane(mainPanel);

        showUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showOnlineUsers();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String selectedUser = (String) ipComboBox.getSelectedItem();
                    String destination = getKeyByValue(onlineUsers, selectedUser);
                    String[] ipPort = destination.split(":");
                    String destinationIp = ipPort[0];
                    int port = Integer.parseInt(ipPort[1]);
                    try {
                        System.out.println("Sending file to " + destinationIp + ":" + port);
                        fileTransfer.sendFile(file, destinationIp, port);
                        JOptionPane.showMessageDialog(null, "File Sent Successfully!");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error sending file: " + ex.getMessage());
                    }
                }
            }
        });

        receiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File directory = fileChooser.getSelectedFile();
                    try {
                        FileReceiveThread receiveThread = new FileReceiveThread(directory.getAbsolutePath(),
                                registeredPort, fileTransfer);
                        receiveThread.start();
                        JOptionPane.showMessageDialog(null, "Receiving File...");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error starting file reception: " + ex.getMessage());
                    }
                }
            }
        });

        sendClipboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = clipboardTextPane.getText();
                String language = (String) languageComboBox.getSelectedItem();
                String selectedUser = (String) ipComboBox.getSelectedItem();
                String destination = getKeyByValue(onlineUsers, selectedUser);
                String[] ipPort = destination.split(":");
                String destinationIp = ipPort[0];
                int port = Integer.parseInt(ipPort[1]);
                try {
                    // ClipboardClient.setClipboardContent(content, language);
                    new_ClipboardClient.setClipboardContent(content, language, destinationIp, port);
                    JOptionPane.showMessageDialog(null, "Clipboard Sent Successfully!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error sending clipboard: " + ex.getMessage());
                }
            }
        });

        refreshClipboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String selectedUser = (String) ipComboBox.getSelectedItem();
                    String destination = getKeyByValue(onlineUsers, selectedUser);
                    String[] ipPort = destination.split(":");
                    String destinationIp = ipPort[0];
                    int port = Integer.parseInt(ipPort[1]);
                    String[] clipboardData = new_ClipboardClient.getClipboardContent(destinationIp, port);
                    clipboardTextPane.setText(clipboardData[0]);
                    languageComboBox.setSelectedItem(clipboardData[1]);
                    Highlighter.applyHighlight(clipboardTextPane, clipboardData[1]);
                } catch (IOException f) {
                    f.printStackTrace();
                }
            }
        });

        // 注册客户端到服务器
        registerClient();

        // 添加关闭钩子以取消注册
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                unregisterClient();
            }
        });

        // 初始化剪切板内容
        // refreshClipboard();

        // 启动文件接收线程
        //startFileReceiveThread();
    }

    private void showOnlineUsers() {
        try {
            onlineUsers = FileTransferClient.getClients();
            ipComboBox.removeAllItems();
            for (String alias : onlineUsers.values()) {
                ipComboBox.addItem(alias);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error getting online users: " + e.getMessage());
        }
    }

    private void registerClient() {
        try {
            localIP = NetworkUtils.getLocalIpAddress(); // 使用 NetworkUtils 获取本地 IP 地址
            String alias = JOptionPane.showInputDialog("Enter your alias:");
            String portString = JOptionPane.showInputDialog("Enter your port number:");
            registeredPort = Integer.parseInt(portString);
            FileTransferClient.register(localIP, registeredPort, alias);
        } catch (SocketException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error determining local IP address: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering client: " + e.getMessage());
        }
    }

    private void unregisterClient() {
        try {
            FileTransferClient.unregister(localIP, registeredPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshClipboard() {
        try {
            /*
             * String selectedUser = (String) ipComboBox.getSelectedItem();
             * String destination = getKeyByValue(onlineUsers, selectedUser);
             * String[] ipPort = destination.split(":");
             * String destinationIp = ipPort[0];
             * int port = Integer.parseInt(ipPort[1]);
             * String[] clipboardData=new_ClipboardClient.getClipboardContent(destinationIp,
             * port);
             */
            String[] clipboardData = ClipboardClient.getClipboardContent();
            clipboardTextPane.setText(clipboardData[0]);
            languageComboBox.setSelectedItem(clipboardData[1]);
            Highlighter.applyHighlight(clipboardTextPane, clipboardData[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startFileReceiveThread() {
        try {
            FileReceiveThread receiveThread = new FileReceiveThread("./receivedFiles/", registeredPort, fileTransfer); // 设置一个默认接收目录
            receiveThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error starting file reception thread: " + e.getMessage());
        }
    }

    private String getKeyByValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // try {

        // JFrame.setDefaultLookAndFeelDecorated(true);

        // UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");

        // } catch (Exception e) {

        // e.printStackTrace();

        // }
        // try
        // {
        // org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        // }
        // catch(Exception e)
        // {
        // //TODO exception
        // System.out.println("加载主题失败！");
        // }

        SwingUtilities.invokeLater(() -> new FileTransferGUI().setVisible(true));
    }

}