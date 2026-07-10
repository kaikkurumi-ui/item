package com.aisia.item.console.utils;

import cn.hutool.core.util.ZipUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
public class DownLoadUtil {

    public static void downLoadZipFile(File zipFile, HttpServletResponse response) throws IOException {
       try {
           // 设置响应头
           response.setContentType("application/zip");
           response.setCharacterEncoding("utf-8");
           String fileName = URLEncoder.encode("商品表", "UTF-8").replaceAll("\\+", "%20");
           response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".zip");

           try(ServletOutputStream os = response.getOutputStream();
               FileInputStream fls = new FileInputStream(zipFile)) {
               byte[] buffer = new byte[8192];
               int len;

               while ((len = fls.read(buffer)) != -1) {
                   os.write(buffer, 0, len);
               }
               os.flush();
           }
       } catch (Exception e) {
           // 可以返回错误信息
           log.error("写入Zip文件到响应流失败", e);
           throw e;
       }finally {
           // 清理临时文件
           if (zipFile != null && zipFile.exists()) {
               zipFile.delete();
           }
       }
    }
}
