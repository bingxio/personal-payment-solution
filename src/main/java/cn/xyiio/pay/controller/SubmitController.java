package cn.xyiio.pay.controller;

import cn.xyiio.pay.entity.CommonEntity;
import cn.xyiio.pay.entity.ResponseEntity;
import cn.xyiio.pay.entity.SubmitEntity;
import cn.xyiio.pay.errno.StatusCode;
import cn.xyiio.pay.repository.impl.SubmitRepositoryImpl;
import cn.xyiio.pay.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings({"unused", "Duplicates"})
@RestController
public class SubmitController {

    private final SubmitRepositoryImpl submitRepository;

    @Autowired
    public SubmitController(SubmitRepositoryImpl submitRepository) {
        this.submitRepository = submitRepository;
    }

    /**
     * 获取公有配置、当前是否有订单、当前的随机值
     *
     * @return  响应
     */
    @GetMapping("/api/common")
    private ResponseEntity<CommonEntity> common() {
        return new ResponseEntity<>(StatusCode.OK.ordinal(), "OK", submitRepository.findCommonEntity());
    }

    /**
     * 提交支付订单
     *
     * -> 设置公有订单为真，定时任务五分钟后设置为假
     * -> 添加一条订单记录，用户必须在五分钟之内进行支付，在这期间其他用户不能新开启支付
     * -> 客户端会有倒计时
     *
     * @param money 金额
     * @return      响应
     */
    @PostMapping("/api/submit")
    private ResponseEntity<SubmitEntity> submit(@RequestParam("money") int money, @RequestParam("secret") String secret) {
        SubmitEntity submitEntity = new SubmitEntity(money, false, DateUtils.currentAt());

        StatusCode statusCode = submitRepository.saveSubmit(submitEntity, secret);

        if (statusCode == StatusCode.ERR_SAVE_RUN_TASK) {
            return new ResponseEntity<>(statusCode.ordinal(), "is have other order task running ?", null);
        }

        if (statusCode == StatusCode.ERR_DATABASE) {
            return new ResponseEntity<>(statusCode.ordinal(), "error database.", null);
        }

        if (statusCode == StatusCode.ERR_SECRET) {
            return new ResponseEntity<>(statusCode.ordinal(), "illegal request.", null);
        }

        return new ResponseEntity<>(StatusCode.OK.ordinal(), "OK", submitEntity);
    }

    /**
     * 提交订单支付信息
     *
     * -> 支付成功后进行调用，更新最新的记录的支付订单字段为已支付
     *
     * @return  响应
     */
    @PostMapping("/api/submit/update")
    private ResponseEntity<StatusCode> update(@RequestParam("secret") String secret) {
        StatusCode statusCode = submitRepository.updateLastLimit(secret);

        if (statusCode == StatusCode.ERR_UPDATE_NO_TASK) {
            return new ResponseEntity<>(statusCode.ordinal(), "no one order task running !", null);
        }

        if (statusCode == StatusCode.ERR_DATABASE) {
            return new ResponseEntity<>(statusCode.ordinal(), "error database.", null);
        }

        if (statusCode == StatusCode.ERR_SECRET) {
            return new ResponseEntity<>(statusCode.ordinal(), "illegal request.", null);
        }

        return new ResponseEntity<>(StatusCode.OK.ordinal(), "OK", null);
    }

    /**
     * 获取最新的支付记录
     *
     * -> 用户客户端进行查询最新的支付记录的支付情况
     *
     * @return  响应
     */
    @GetMapping("/api/submit/last")
    private ResponseEntity<SubmitEntity> query(@RequestParam("secret") String secret) {
        return new ResponseEntity<>(StatusCode.OK.ordinal(), "OK", submitRepository.queryLastLimit(secret));
    }
}
