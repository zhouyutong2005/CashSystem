package com.cashsystem.config;

import com.cashsystem.entity.DepositRecord;
import com.cashsystem.service.BlockchainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class BlockchainAspect {

    private final BlockchainService blockchainService;

    /**
     * 存款记录创建后自动存证
     */
    @AfterReturning(pointcut = "execution(* com.cashsystem.service.DepositRecordService.createDepositRecord(..))",
            returning = "result")
    public void afterCreateDepositRecord(JoinPoint joinPoint, boolean result) {
        if (result) {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof DepositRecord) {
                DepositRecord record = (DepositRecord) args[0];
                try {
                    blockchainService.storeData("DEPOSIT", record.getId(), record);
                    log.info("存款记录自动存证: depositRecordId={}", record.getId());
                } catch (Exception e) {
                    log.error("存款记录存证失败: depositRecordId={}", record.getId(), e);
                }
            }
        }
    }

    /**
     * 清机任务创建后自动存证
     */
    @AfterReturning(pointcut = "execution(* com.cashsystem.service.CleaningTaskService.createCleaningTask(..))",
            returning = "result")
    public void afterCreateCleaningTask(JoinPoint joinPoint, boolean result) {
        if (result) {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof com.cashsystem.entity.CleaningTask) {
                com.cashsystem.entity.CleaningTask task = (com.cashsystem.entity.CleaningTask) args[0];
                try {
                    blockchainService.storeData("CLEANING", task.getId(), task);
                    log.info("清机任务自动存证: cleaningTaskId={}", task.getId());
                } catch (Exception e) {
                    log.error("清机任务存证失败: cleaningTaskId={}", task.getId(), e);
                }
            }
        }
    }

    /**
     * 日结记录创建后自动存证
     */
    @AfterReturning(pointcut = "execution(* com.cashsystem.service.DailySettlementService.createDailySettlement(..))",
            returning = "result")
    public void afterCreateDailySettlement(JoinPoint joinPoint, boolean result) {
        if (result) {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0 && args[0] instanceof com.cashsystem.entity.DailySettlement) {
                com.cashsystem.entity.DailySettlement settlement = (com.cashsystem.entity.DailySettlement) args[0];
                try {
                    blockchainService.storeData("SETTLEMENT", settlement.getId(), settlement);
                    log.info("日结记录自动存证: settlementId={}", settlement.getId());
                } catch (Exception e) {
                    log.error("日结记录存证失败: settlementId={}", settlement.getId(), e);
                }
            }
        }
    }
}