package com.aisia.item.console.controller;

import com.aisia.item.module.enums.UploadType;
import com.aisia.item.module.service.FileService;
import com.aisia.item.module.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload-local")
    public String uploadLocal(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "上传文件不能为空";
        }
        // 获取文件类型
        String contentType = file.getContentType();
        // 获取后缀
        String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String fileSuffix = split[split.length - 1];
        // 获取文件类型“/”前的字符串
        String type = Objects.requireNonNull(contentType.substring(0, contentType.lastIndexOf("/")));
        System.out.println("type: " + type);
        // 随机数
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        System.out.println("随机数:" + num);
        String name = "";
        String uploadPath = "";
        switch (type) {
            case "image":
                int[] imageDimensions = null;
                try {
                    imageDimensions = ImageUtil.getImageDimensions(file.getInputStream());
                } catch (IOException e) {
                    log.error("上传图片失败:{}",e.getMessage());
                    return "图片上传失败";
                }
                if(imageDimensions == null){
                    return "图片上传失败";
                }
                name = String.valueOf(num) + System.currentTimeMillis() + "_" + imageDimensions[0] + "x" + imageDimensions[1] + "." + fileSuffix;
                uploadPath = "D:/upload/image";
                break;
            case "video":
                name = String.format("%s%s.%s", num, System.currentTimeMillis(), fileSuffix);
                uploadPath = "D:/upload/video";
                break;
            case "application", "text":
                name = String.format("%s%s.%s", num, System.currentTimeMillis(), fileSuffix);
                uploadPath = "D:/upload/file";
                break;
            default:
                return "文件不符合要求";
        }
        // 创建文件的完整路径
        Path path = Paths.get(uploadPath).resolve(name);

        try {
            // 创建目录
            Files.createDirectories(path.getParent());
            // 保存文件
            Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("出错了:{}",e.getMessage());
            return "上传失败";
        }
        return "上传成功";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,@RequestParam("type") Integer type){
        if (file.isEmpty()) {
            return "上传文件不能为空";
        }
        UploadType uploadType = UploadType.getByType(type);
        if(uploadType == null){
            return "不符合上传类型";
        }
        // 获取后缀
        String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String fileSuffix = split[split.length - 1];
        // 随机数
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        // 给文件名初始值
        String name = "";
        if(uploadType.getName().equals("/image")){
            int[] imageDimensions = null;
            try {
               imageDimensions = ImageUtil.getImageDimensions(file.getInputStream());
            } catch (IOException e) {
                log.error("上传图片失败:{}",e.getMessage());
                return "图片上传失败";
            }
            if(imageDimensions == null){
                return "图片上传失败";
            }
            name = String.valueOf(num) + System.currentTimeMillis() + "_" + imageDimensions[0] + "x" + imageDimensions[1] + "." + fileSuffix;
        }else {
            name = String.format("%s%s.%s", num, System.currentTimeMillis(), fileSuffix);
        }
        String resourceUri = "";
        try {
            InputStream inputStream = file.getInputStream();
            resourceUri = fileService.upload(inputStream,uploadType,name);
        } catch (Exception e) {
            log.error("出错了:{}", e.getMessage());
            return "上传失败";
        }
        return resourceUri;
    }
}
