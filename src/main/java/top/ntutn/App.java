package top.ntutn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import lombok.Cleanup;

/**
 * Hello world!
 *
 */
public class App {
    private static final String todoFolder = System.getProperty("user.home") + "/.todo";
    private static final String todoText = System.getProperty("user.home") + "/.todo/todo.txt";
    private static final String todoAchieve = System.getProperty("user.home") + "/.todo/todo.achieve.txt";

    private static String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static void main(String[] args) {
        //若相关的目录和文件不存在，创建。
        File folder = new File(todoFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File todoFile = new File(todoText);
        if (!todoFile.exists()) {
            try {
                todoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File todoAchieveFile = new File(todoAchieve);
        if (!todoAchieveFile.exists()) {
            try {
                todoAchieveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将数据读入content
        LinkedList<String> content = new LinkedList<>();
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(todoFile));
            @Cleanup
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                if (line.trim().length() > 0) {
                    content.add(line);
                }
                line = br.readLine(); // 一次读入一行数据
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
            //显示帮助
            case "-h":
                System.out.println("todo工具By归零幻想");
                System.out.println("https://ntutn.top");
                System.out.println();
                System.out.println("显示\ttodo");
                System.out.println("帮助\ttodo -h");
                System.out.println("添加\ttodo 文本内容");
                System.out.println("完成\ttodo -c 行号");
                System.out.println("整理\ttodo -a");
                System.out.println();
                System.out.println("数据存储在~/.todo中。");
                System.out.println("显示：显示所有任务。");
                System.out.println("帮助：查看本文件。");
                System.out.println("添加：添加一条记录。");
                System.out.println("完成：将任务在完成/未完成间切换。");
                System.out.println("整理：将已经完成的任务移动到todo.achieve.txt中。");
                return;
            case "-c":
                //切换任务状态
                if (i >= args.length - 1) {
                    System.out.println("-c后面没有参数！");
                    return;
                }
                Integer id = Integer.parseInt(args[i + 1]);
                String chNote = content.get(id);
                content.remove(id.intValue());
                String[] arrNote = chNote.split("\\s");
                if (arrNote[0].equals("x")) {
                    chNote = "";
                    boolean fi = true;
                    for (int j = 2; j < arrNote.length; j++) {
                        if (fi) {
                            fi = false;
                        } else {
                            chNote += "\t";
                        }
                        chNote += arrNote[j];
                    }
                    content.add(chNote);
                } else {
                    content.add("x\t" + getDateString() + "\t" + chNote);
                }
                try {
                    @Cleanup
                    BufferedWriter out = new BufferedWriter(new FileWriter(todoFile));
                    String outString = "";
                    for (String j : content) {
                        outString += j + "\r\n";
                    }
                    out.write(outString);
                    out.flush(); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            case "-a":
                //整理已完成任务
                try {
                    BufferedWriter out = new BufferedWriter(new FileWriter(todoAchieveFile, true));
                    String outString = "";
                    for (String j : content) {
                        if (j.startsWith("x")) {
                            outString += j + "\r\n";
                        }
                    }
                    out.write(outString);
                    out.flush(); // 把缓存区内容压入文件
                    out.close();
                    out = new BufferedWriter(new FileWriter(todoFile));
                    outString = "";
                    for (String j : content) {
                        if (!j.startsWith("x")) {
                            outString += j + "\r\n";
                        }
                    }
                    out.write(outString);
                    out.flush(); // 把缓存区内容压入文件
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            default:
                //添加任务
                try {
                    @Cleanup
                    BufferedWriter out = new BufferedWriter(new FileWriter(todoFile, true));
                    out.write(getDateString() + "\t" + args[i] + "\r\n");
                    out.flush(); 
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (args.length != 0) {
            return;
        }
        //显示任务状态
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i).startsWith("x")) {
                System.out.println(i + "\t" + content.get(i).replaceFirst("x\t", ""));
            } else {
                System.out.println(i + "\t\033[7m还没有完成\033[0m\t" + "" + content.get(i));
            }
        }
        System.out.println();
        System.out.println("使用“todo -h”参数获取帮助。");
    }
}
