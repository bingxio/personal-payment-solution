package cn.xyiio.pay.repository.impl;

import cn.xyiio.pay.entity.CommonEntity;
import cn.xyiio.pay.repository.CommonRepository;
import cn.xyiio.pay.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommonRepositoryImpl implements CommonRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public CommonRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CommonEntity findCommonEntity() {
        if (!mongoTemplate.collectionExists(CommonEntity.class)) {
            mongoTemplate.save(
                    new CommonEntity(false, DateUtils.currentAt(), "")
            );
        }

        return mongoTemplate.findAll(CommonEntity.class).get(0);
    }
}
