import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lanyage on 2018/5/6.
 */
/** 文件操作的所有方法  */
public class FileTest {

    public static void main(String[] args) {
        File file = new File("/Users/lanyage/IdeaProjects/mybatis/src/main/resources/mapper.xml");
        System.out.println("能够被执行:" + file.canExecute());//能否被执行
        System.out.println("文件的名字:" + file.getName());//获取文件的名字
        System.out.println("文件的绝对路径" + file.getAbsolutePath());//获取文件的绝对路径
        System.out.println("能不能读:" + file.canRead());//能不能读
        System.out.println("能不能写:" + file.canWrite());//能不能写
        System.out.println("比较两个文件:" + file.compareTo(new File("mybatis-config.xml")));//和另外一个文件做比较
        System.out.println("文件是否存在:" + file.exists());//文件是否存在
        System.out.println("系统剩余空间" + file.getFreeSpace());//空余的空间
        System.out.println("文件的路径" + file.getParent());//获取文件的前缀
        System.out.println("将文件的副路径封装成文件" + file.getParentFile());//获取文件的父路径的文件
        System.out.println("路径:" + file.getPath());//获取文件的路径
        System.out.println("文件大小:" + file.getTotalSpace());//获取电脑的总内存
        System.out.println("文件是不是绝对路径:" + file.isAbsolute());//文件是不是绝对路径
        System.out.println("系统可用空间:" + file.getUsableSpace());
        System.out.println(file.isDirectory());//是不是目录
        System.out.println(file.isFile());//是不是文件
        System.out.println("uri:" + file.toURI());//将文件以uri形式展现
        System.out.println(file.renameTo(new File("mapper2.xml")));//更改文件名字
        System.out.println("文件长度:" + file.length());
        System.out.println("文件上次Modified的时间:" + file.lastModified());//上次修改的时间戳
    }
}
