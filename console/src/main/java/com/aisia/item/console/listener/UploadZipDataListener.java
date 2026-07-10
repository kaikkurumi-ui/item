package com.aisia.item.console.listener;

import cn.hutool.core.bean.BeanUtil;
import com.aisia.item.console.domain.ItemUploadData;
import com.aisia.item.module.entity.Item;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UploadZipDataListener extends AnalysisEventListener<ItemUploadData> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 30;
    /**
     * 缓存的数据
     */
    @Getter
    private List<Item> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    public UploadZipDataListener() {
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
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

}
