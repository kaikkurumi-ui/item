package com.aisia.item.module.service;

import com.aisia.item.module.entity.FileEntity;
import com.aisia.item.module.enums.UploadType;
import com.aisia.item.module.mapper.FileMapper;
import com.aisia.item.module.utils.OSSUtil;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件存储表 服务实现类
 * </p>
 *
 * @author kaikai
 * @since 2026-07-07
 */
@Service
public class FileService {

    @Autowired
    private FileMapper fileMapper;

    public int insert(FileEntity fileEntity){
        return fileMapper.insert(fileEntity);
    }

    public int update(FileEntity fileEntity){
        return fileMapper.update(fileEntity);
    }

    public FileEntity getById(Long id){
        return fileMapper.getById(id);
    }

    public FileEntity extractById(Long id){
        return fileMapper.extractById(id);
    }

    public int delete(Long id){
        return fileMapper.delete(id);
    }

    public String upload(InputStream inputStream, UploadType uploadType, String fileName) {
        //获取当前日期和时间对象
        LocalDateTime now = LocalDateTime.now();
        // 获取年份
        int year = now.getYear();
        // 获取月份（1-12）
        int month = now.getMonthValue();
        // 获取当日
        int day = now.getDayOfMonth();
        //%02d: 月份和日期如果不足2位，补零至2位
        String strMonth = String.format("%02d", month);
        String strDay = String.format("%02d", day);
        // 上传文件
        String pathUri = uploadType.getName() + "/" + year + strMonth + "/" + strDay + "/" + fileName;

        try {
            OSSUtil.uploadFile(pathUri, inputStream);
        } catch (ClientException e) {
            throw new RuntimeException(e);
        }

        // 获取bucket域名
        String bucketName = "item-shenzhen-lnysys";
        // 获取endpoint地域节点
        String endpoint = "oss-cn-shenzhen.aliyuncs.com";
        String resourceUri = "https://" + bucketName + "." + endpoint + pathUri;
        FileEntity fileEntity = new FileEntity();
        fileEntity.setType(uploadType.getType());
        fileEntity.setResourceUri(resourceUri);
        fileEntity.setIsDeleted(0);
        fileEntity.setCreateTime(Instant.now().getEpochSecond());
        int insert = insert(fileEntity);
        if (insert > 0) {
            return resourceUri;
        } else {
            throw new RuntimeException("insert file fail");
        }
    }
}
