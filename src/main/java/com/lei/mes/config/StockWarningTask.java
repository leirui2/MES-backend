package com.lei.mes.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lei.mes.entity.common.Material;
import com.lei.mes.service.common.MaterialService;
import com.lei.mes.util.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 库存预警定时任务
 * @author lei
 */
@Slf4j
@Component
public class StockWarningTask {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MailService mailService;

    /**
     * 每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void checkStockWarning() {
        log.info("开始检查库存预警");
        // 查询库存 <= 安全库存的物料
        List<Material> warningMaterials = materialService.list(
                new LambdaQueryWrapper<Material>()
                        .apply("stock_qty <= min_stock")
                        .eq(Material::getStatus, 1)
        );

        if (warningMaterials.isEmpty()) {
            return;
        }

        // 发送通知
        StringBuilder msg = new StringBuilder("库存预警通知：\n");
        for (Material m : warningMaterials) {
            msg.append(String.format("%s (%s) - 库存: %s, 安全库存: %s\n",
                    m.getMaterialName(), m.getMaterialCode(),
                    m.getStockQty(), m.getMinStock()));
        }

        log.warn(msg.toString());
        // 发邮件
        mailService.sendSimpleMail("leirui2@outlook.com", "库存预警通知", msg.toString());
    }
}

