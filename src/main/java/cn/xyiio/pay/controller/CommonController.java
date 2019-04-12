package cn.xyiio.pay.controller;

import cn.xyiio.pay.entity.CommonEntity;
import cn.xyiio.pay.entity.api.ResponseEntity;
import cn.xyiio.pay.errno.StatusCode;
import cn.xyiio.pay.repository.impl.CommonRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    private final CommonRepositoryImpl commonRepository;

    @Autowired
    public CommonController(CommonRepositoryImpl commonRepository) {
        this.commonRepository = commonRepository;
    }

    /**
     * 获取公有配置、当前是否有订单、当前的随机值
     *
     * @return  响应
     */
    @GetMapping("/api/common")
    private ResponseEntity<CommonEntity> common() {
        return new ResponseEntity<>(StatusCode.OK.ordinal(), "OK", commonRepository.findCommonEntity());
    }
}
