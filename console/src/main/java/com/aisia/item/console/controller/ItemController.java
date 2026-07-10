package com.aisia.item.console.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ZipUtil;
import com.aisia.item.console.domain.*;
import com.aisia.item.console.listener.UploadDataListener;
import com.aisia.item.console.listener.UploadZipDataListener;
import com.aisia.item.console.utils.DateTimeUtil;
import com.aisia.item.console.utils.DownLoadUtil;
import com.aisia.item.module.entity.CategoryEntity;
import com.aisia.item.module.entity.Item;
import com.aisia.item.module.service.CategoryService;
import com.aisia.item.module.service.ItemService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/item")
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadPoolTaskExecutor excelExecutor;

    @RequestMapping("/insert")
    public String insert(@RequestParam("itemImages") String itemImages,
                         @RequestParam("title") String title,
                         @RequestParam("price") Float price,
                         @RequestParam("description") String description,
                         @RequestParam("categoryId") Long categoryId) {
        log.info("创建商品,itemImages:{},title:{},price:{},description:{},categoryId:{}", itemImages, title, price, description, categoryId);
        Long result = 0L;
        try {
            result = itemService.edit(null, itemImages, title, price, description, 0, categoryId);
            return "新增商品成功，商品id为:" + result;
        } catch (RuntimeException e) {
            log.error("出错了:{}", e.getMessage());
        }
        return "新增商品失败";
    }

    @RequestMapping("/update")
    public String update(@RequestParam("itemId") Long itemId,
                         @RequestParam("itemImages") String itemImages,
                         @RequestParam("title") String title,
                         @RequestParam("price") Float price,
                         @RequestParam("description") String description,
                         @RequestParam("isDeleted") Integer isDeleted,
                         @RequestParam("categoryId") Long categoryId) {
        log.info("更新商品信息,itemId:{},itemImages:{},title:{},price:{},description:{},isDeleted:{},categoryId:{}", itemId, itemImages, title, price, description, isDeleted, categoryId);
        Long result = 0L;
        try {
            result = itemService.edit(itemId, itemImages, title, price, description, isDeleted, categoryId);
            return "更新商品信息成功，商品id为:" + result;
        } catch (RuntimeException e) {
            log.error("出错了:{}", e.getMessage());
        }
        return "更新商品失败";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam("itemId") Long itemId) {
        log.info("删除商品,itemId:{}", itemId);
        return itemService.delete(itemId);
    }

    @RequestMapping("/list")
    public ItemListVo list(@RequestParam("page") Integer page,
                           @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("console端获取商品列表,页码:{}", page);
        // 指定分页大小
        Integer pageSize = 5;
        List<Item> items = itemService.consoleGetByPage(page, pageSize, keyword); //查询数据
        Long total = itemService.getTotal(keyword); //总页数
        List<ItemInfoVo> infoVoList = new ArrayList<>(items.size());
        for (Item item : items) {
            ItemInfoVo itemInfoVo = ItemInfoVo.builder()
                    .itemImage(item.getItemImages().split("\\$")[0])
                    .price(item.getPrice())
                    .title(item.getTitle())
                    .build();
            infoVoList.add(itemInfoVo);
        }
        ItemListVo itemListVo = new ItemListVo();
        itemListVo.setList(infoVoList);
        itemListVo.setPageSize(pageSize);
        itemListVo.setTotal(total);
        return itemListVo;
    }

    @RequestMapping("/info")
    public ItemDetailInfoVo info(@RequestParam("itemId") Long itemId) {
        log.info("console端根据商品id获取商品详情:{}", itemId);
        Item item = itemService.getById(itemId);
        ItemDetailInfoVo itemDetailInfoVo = new ItemDetailInfoVo();
        itemDetailInfoVo.setItemImages(Arrays.asList(item.getItemImages().split("\\$")));
        itemDetailInfoVo.setDescription(item.getDescription());
        itemDetailInfoVo.setTitle(item.getTitle());
        itemDetailInfoVo.setPrice(item.getPrice());
        // 将时间戳转换为指定日期格式
        itemDetailInfoVo.setCreateTime(DateTimeUtil.formatTime(item.getCreateTime()));
        itemDetailInfoVo.setUpdateTime(DateTimeUtil.formatTime(item.getUpdateTime()));
        return itemDetailInfoVo;
    }

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p>
     * 1. 创建excel对应的实体对象 参照{ DownloadData}
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     * <p>
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     */
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        try {
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("商品表", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 头的策略
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            // 内容的策略
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            WriteFont contentWriteFont = new WriteFont();
            // 字体大小
            contentWriteFont.setFontHeightInPoints((short) 16);
            contentWriteCellStyle.setWriteFont(contentWriteFont);
            // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
            HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                    new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            // 获取商品数据
            List<Item> items = itemService.getAll();

            List<ItemDownLoadData> data = items.stream().map(item -> {
                ItemDownLoadData itemDownLoadData = new ItemDownLoadData();
                itemDownLoadData.setItemImages(item.getItemImages());
                itemDownLoadData.setTitle(item.getTitle());
                itemDownLoadData.setPrice(item.getPrice());
                itemDownLoadData.setDescription(item.getDescription());
                itemDownLoadData.setCategoryId(item.getCategoryId());
                itemDownLoadData.setCreateTime(new Date(item.getCreateTime() * 1000));
                return itemDownLoadData;
            }).collect(Collectors.toList());
            EasyExcel.write(response.getOutputStream(), ItemDownLoadData.class)
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .autoCloseStream(Boolean.FALSE) // 这里需要设置不关闭流
                    .sheet("商品表模板")
                    .doWrite(data);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    /**
     * 文件上传
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link }
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UploadDataListener}
     * <p>
     * 3. 直接读即可
     */
    @PostMapping("/upload")
    public String upload(@Param("file") MultipartFile file){
        List<CategoryEntity> categoryEntities = categoryService.list();
        try {
            EasyExcel.read(file.getInputStream(),
                            ItemUploadData.class,
                            new UploadDataListener(itemService))
                    .sheet().doRead();
        } catch (IOException e) {
            log.error("error:{}",e.getMessage());
            return "false";
        }
        return "success";
    }

    @GetMapping("/download-zip")
    public void downloadZip(HttpServletResponse response) {
        List<Item> items = itemService.getAll();

        List<ItemDownLoadData> data = items.stream().map(item -> {
            ItemDownLoadData itemDownLoadData = BeanUtil.toBean(item, ItemDownLoadData.class);
            itemDownLoadData.setCreateTime(new Date(item.getCreateTime() * 1000));
            return itemDownLoadData;
        }).toList();
        Map<Long, List<ItemDownLoadData>> map = data.stream()
                .collect(Collectors.groupingBy(i -> i.getId() % 10));

        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 16);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        List<Future<File>> futures = new ArrayList<>();

        for (Map.Entry<Long, List<ItemDownLoadData>> entry : map.entrySet()) {
            Long groupId = entry.getKey();
            List<ItemDownLoadData> groupItems = entry.getValue();
            Future<File> future = excelExecutor.submit(() -> {
                // 每一个分组写一个excel表格
                String fileName = "商品表_" + groupId + ".xlsx";
                // 写入目录
                String filePath = "D:/item/download" + File.separator + fileName;

                log.info("线程 {} 开始生成文件: {}, 包含 {} 条数据",
                        Thread.currentThread().getName(), fileName, groupItems.size());
                EasyExcel.write(filePath, ItemDownLoadData.class)
                        .registerWriteHandler(horizontalCellStyleStrategy)
                        .sheet("商品表_" + groupId)
                        .doWrite(() -> {
                            // 分页查询数据
                            return groupItems;
                        });
                return new File(filePath); //读取写入好的文件
            });
            futures.add(future);
        }

        List<File> fileList = new ArrayList<>();
        for (Future<File> future : futures) {
            try {
                File file = future.get();
                fileList.add(file);
                log.info("文件生成完成: {}", file);
            } catch (InterruptedException | ExecutionException e) {
                log.error("文件生成失败", e);
            }
        }
        // 读取所有文件并打包成Zip
        String zipFileName = "商品表" + ".zip";
        String zipPath = "D:/item" + File.separator + zipFileName;

        File[] files = fileList.toArray(new File[0]);
        // 不保留源文件目录结构
        File zipFile = ZipUtil.zip(new File(zipPath), StandardCharsets.UTF_8, false, files);

        try {
            DownLoadUtil.downLoadZipFile(zipFile, response);
        } catch (IOException e) {
            log.error("导出失败", e);
        } finally {
            // 打包完成后，立即清理临时 Excel 文件
            for (File file : fileList) {
                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        log.debug("删除临时文件: {}", file.getName());
                    } else {
                        log.warn("删除临时文件失败: {}", file.getAbsolutePath());
                    }
                }
            }
        }
    }

    @PostMapping("/upload/zip")
    public void uploadZip(@RequestParam("file") MultipartFile file){
        File outFile = new File("D:\\item\\upload");
        File unZipFiles = null;
        try {
            unZipFiles = ZipUtil.unzip(file.getInputStream(), outFile,StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("error:{}",e.getMessage());
        }
        if(unZipFiles == null){
            log.error("没有文件");
            return;
        }

        List<Future<List<Item>>> futures = new ArrayList<>();
        if(unZipFiles.isDirectory()){
            for (File f : Objects.requireNonNull(unZipFiles.listFiles())) {
                Future<List<Item>> future = excelExecutor.submit(() -> {
                    UploadZipDataListener listener = new UploadZipDataListener();
                    EasyExcel.read(f.getAbsolutePath(), ItemUploadData.class,listener)
                            .sheet()
                            .doRead();

                    List<Item> list = listener.getCachedDataList();
                    return list;
                });
                futures.add(future);
            }
        }

        // 合并所有文件的数据
        List<Item> allDataList = new ArrayList<>();
        for (Future<List<Item>> future : futures) {
            try {
                List<Item> items = future.get();
                allDataList.addAll(items);
            } catch (InterruptedException | ExecutionException e) {
                log.error("error:{}",e.getMessage());
            }
        }
        log.info("所有文件读取完成，总数据量: {}", allDataList.size());
        itemService.saveBatch(allDataList);
    }
}
