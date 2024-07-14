import java.io.IOException;

//定义FileReceiveThread类继承Thread类
public class FileReceiveThread extends Thread {

    private final String savePath; //用于保存文件的路径（私有）
    private final int port;  //接收文件所使用的端口（私有）
    private final FileTransfer fileTransfer; //调用FileTransfer对象，用于文件传输的操作

    // 构造函数，初始化FileReceiveThread对象
    public FileReceiveThread(String savePath, int port, FileTransfer fileTransfer) {
        this.savePath = savePath; //文件保存路径的初始化
        this.port = port; //端口号的初始化
        this.fileTransfer = fileTransfer; //FileTransfer对象的初始化
    }

    // 重写Thread类的run方法
    @Override
    public void run() {
        try {
            //使用指定的端口接收文件，并将接收的文件保存到指定的路径
            fileTransfer.receiveFile(savePath, port);
        } catch (IOException e) {
            //若接受文件的过程中发生I/O异常，则捕获该异常并打印堆栈信息
            e.printStackTrace();
        }
    }
}

