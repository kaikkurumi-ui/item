package com.aisia.item.console.listener;

import cn.hutool.core.bean.BeanUtil;
import com.aisia.item.console.domain.ItemUploadData;
import com.aisia.item.module.entity.Item;
import com.aisia.item.module.service.ItemService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UploadDataListener extends AnalysisEventListener<ItemUploadData> {

    private ItemService itemService;
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 30;
    /**
     * 缓存的数据
     */
    private List<Item> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    public UploadDataListener(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param itemUploadData    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param analysisContext
     */
    @Override
    public void invoke(ItemUploadData itemUploadData, AnalysisContext analysisContext) {
        //log.info("解析到一条数据:{}", JSON.toJSONString(itemService));
        Item item = BeanUtil.toBean(itemUploadData, Item.class);
        item.setCreateTime(System.currentTimeMillis() / 1000);
        item.setIsDeleted(0);
        cachedDataList.add(item);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        itemService.saveBatch(cachedDataList);
        log.info("存储数据库成功！");
    }
}
