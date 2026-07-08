package com.aisia.item.module.utils;

import com.aliyun.oss.OSSException;
import com.aliyuncs.exceptions.ClientException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtil {

    /**
     * 获取上传图片的宽和高
     *
     * @param inputStream 上传的文件对象的输入流
     * @return int数组，为宽，为高；如果解析失败返回 null
     */
    public static int[] getImageDimensions(InputStream inputStream) throws IOException {

        try {
            // 将输入流读取为 BufferedImage 对象
            BufferedImage image = ImageIO.read(inputStream);

            // 如果 image 为 null，说明文件不是有效的图片格式或已损坏
            if (image == null) {
                return null;
            }

            int width = image.getWidth();
            int height = image.getHeight();

            return new int[]{width, height};

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 计算图片宽高比
     *
     * @param imageUri 图片uri地址
     * @return ar 宽高比
     */
    public static Float calculateAr(String imageUri) throws URISyntaxException, ClientException, IOException {
        //https://item-shenzhen-lnysys.oss-cn-shenzhen.aliyuncs.com/image/202607/07/4821783416061774_1247x791.png
        // 1. 正则表达式
        String regex = "_(\\d+x\\d+)\\.";

        // 提取图片地址
        URI url = new URI(imageUri);
        String path = url.getPath();

        // 2. 编译 Pattern (建议复用，线程安全)
        Pattern pattern = Pattern.compile(regex);

        // 3. 创建 Matcher
        Matcher matcher = pattern.matcher(imageUri);

        // 4. 循环提取所有匹配项
        List<String> results = new ArrayList<>();
        while (matcher.find()) {
            // group() 或 group(0) 返回整个匹配到的子串
            // group(1) 提取()内的内容
            // results.add(matcher.group());
            results.add(matcher.group(1));
        }
        String widthStr = "";
        String heightStr = "";
        if (results.isEmpty()) {
            //图片url格式不符合要求,没有取到宽高
            // 请求阿里云图片地址， 加载图片来计算宽高
            String[] imageInfo = OSSUtil.getImageInfo(path.startsWith("/") ? path.substring(1) : path);
            //不是有效的图片格式或已损坏
            if (imageInfo == null) {
                return 0F;
            }
            widthStr = imageInfo[0];
            heightStr = imageInfo[1];
        } else {
            String[] split = results.getFirst().split("x");
            if (split[1].equals("0")) {
                // 高为0
                // 请求阿里云图片地址， 加载图片来计算宽高
                String[] imageInfo = OSSUtil.getImageInfo(path.startsWith("/") ? path.substring(1) : path);
                //不是有效的图片格式或已损坏
                if (imageInfo == null) {
                    return 0F;
                }
                widthStr = imageInfo[0];
                heightStr = imageInfo[1];
            }else {
                widthStr = split[0];
                heightStr = split[1];
            }
        }
        // 计算宽高
        int width = Integer.parseInt(widthStr);
        int height = Integer.parseInt(heightStr);
        return (float) width / height;
    }
}
