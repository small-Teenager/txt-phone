package com.ouer.txt.txtphone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @Description TODO
 * @Author quancong
 * @Email quancong@ixiappu.com
 * @Date 2020/7/7
 */
public class Main {
    private final static Logger log = LoggerFactory.getLogger(Main.class);
    /**
     * 字符编码
     */
    private static final String ENCODING = "utf-8";

    public static void main(String[] args) {
        Main main = new Main();

        // 原始文件
        List<String> resource = main.readTxtFile("src/main/resources/phone-qqzeng-201912-441831.txt");
        System.err.println("start");

        // 输入的手机号文件
        List<String> input = main.readTxtFile("src/main/resources/input/0708.txt");

        // 需要过滤的城市  "北京","上海","广州","深圳","杭州","厦门","福州","长沙","南昌"
        List<String> citys = Arrays.asList("北京", "上海", "广州", "深圳", "杭州", "厦门", "福州", "长沙", "南昌");

        // 输出的手机号文件
        Set<String> output = main.outputPhone(resource, input, citys);
        System.err.println("output end");
        main.outputTxtFile(output, "src/main/resources/output/output.txt");
    }


    /**
     * 读取资源文件，将内容添加到set集合中
     *
     * @param path 资源路径
     * @return
     */
    @SuppressWarnings("resource")
    private List<String> readTxtFile(String path) {
        List<String> ret = null;
        //读取文件
        File file = new File(path);
        InputStreamReader read = null;
        try {
            read = new InputStreamReader(new FileInputStream(file), ENCODING);
            //文件流是否存在
            if (file.isFile() && file.exists()) {
                ret = new ArrayList<>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                //读取文件，将文件内容放入到set中
                while ((txt = bufferedReader.readLine()) != null) {
                    ret.add(txt);
                }
            }
        } catch (Exception e) {
            log.error("method:{}, message:{}", this.getClass().getName(), e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (null != read) {
                    read.close();
                }
            } catch (IOException e) {
                log.error("method:{}, message:{}", this.getClass().getName(), e.getMessage());
                e.printStackTrace();
            }
        }
        return ret;
    }


    /**
     * 输出资源文件
     *
     * @param output 输出资源的手机号
     * @param path   资源路径
     */
    @SuppressWarnings("resource")
    private void outputTxtFile(Set<String> output, String path) {
        File file = new File(path);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);

//            for (int i = 0; i < output.size(); i++) {
//                bw.write(output.get(i));
//                bw.write("\r\n");
//            }
            for (String str : output) {
                bw.write(str);
                bw.write("\r\n");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (null != bw) {
                    bw.close();
                }
                if (null != fw) {
                    fw.close();
                }
            } catch (IOException e) {
                log.error("method:{}, message:{}", this.getClass().getName(), e.getMessage());
                e.printStackTrace();
            }
        }

        System.err.println("Write is over");
    }


    /**
     * 过滤排除指定城市的手机号
     *
     * @param resource 原始资源  {@link #readTxtFile}
     * @param input    输入的手机号 {@link #readTxtFile}
     * @param citys    需要过滤的不在指定的城市
     * @return 输出的手机号
     * @see
     */
    private Set<String> outputPhone(List<String> resource, List<String> input, List<String> citys) {
        Set<String> output = new HashSet<>();
        if (!input.isEmpty()) {
            for (int i = 0; i < resource.size(); i++) {
                String resourceString = resource.get(i);
                for (int j = 0; j < input.size(); j++) {
                    String phone = input.get(j);
                    String phone_7 = phone.substring(0, 7);
                    if (resourceString.contains(phone_7)) {
                        // 过滤城市
                        for (int k = 0; k < citys.size(); k++) {
                            String city = citys.get(k);
                            // 不在指定范围的城市
                            if (!resourceString.contains(city)) {
                                output.add(phone);
                            }
                        }

                    }
                }
            }
        }
        return output;
    }

}
