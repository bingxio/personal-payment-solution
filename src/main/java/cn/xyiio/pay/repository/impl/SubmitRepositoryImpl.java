package cn.xyiio.pay.repository.impl;

import cn.xyiio.pay.entity.CommonEntity;
import cn.xyiio.pay.entity.SubmitEntity;
import cn.xyiio.pay.errno.StatusCode;
import cn.xyiio.pay.listener.CommonHasOrderScheduleListener;
import cn.xyiio.pay.repository.SubmitRepository;
import cn.xyiio.pay.task.CommonHasOrderScheduleTask;
import cn.xyiio.pay.utils.DateUtils;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SubmitRepositoryImpl implements SubmitRepository, CommonHasOrderScheduleListener {

    @Resource
    private Environment environment;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SubmitRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void OnTaskRunningEnd() {
        mongoTemplate.updateFirst(new Query().addCriteria(Criteria.where("hasOrder").is(true)),
                Update.update("hasOrder", false).set("updateAt", DateUtils.currentAt()), CommonEntity.class);
    }

    @Override
    public StatusCode saveSubmit(SubmitEntity submitEntity, String secret) {
        if (isSecret(secret, false)) return StatusCode.ERR_SECRET;

        if (CommonHasOrderScheduleTask.hasRun) {
            return StatusCode.ERR_SAVE_RUN_TASK;
        }

        UpdateResult updateResult = mongoTemplate.updateFirst(new Query().addCriteria(Criteria.where("hasOrder").is(false)),
                Update.update("hasOrder", true).set("updateAt", DateUtils.currentAt()), CommonEntity.class);

        if (updateResult.getModifiedCount() == 0) {
            return StatusCode.ERR_DATABASE;
        }

        new Thread(() -> CommonHasOrderScheduleTask.configureTask(this)).start();

        SubmitEntity submitEntity1 = mongoTemplate.save(submitEntity);

        if (submitEntity1 == null) {
            return StatusCode.ERR_DATABASE;
        }

        return StatusCode.OK;
    }

    @Override
    public StatusCode updateLastLimit(String secret) {
        if (isSecret(secret, true)) return StatusCode.ERR_SECRET;

        if (!CommonHasOrderScheduleTask.hasRun) {
            return StatusCode.ERR_UPDATE_NO_TASK;
        }

        SubmitEntity submitEntity = mongoTemplate.find(new Query().with(new Sort(Sort.Direction.DESC, "_id")),
                SubmitEntity.class).get(0);

        UpdateResult updateResult = mongoTemplate.updateFirst(new Query(Criteria.where("createAt").is(submitEntity.getCreateAt())),
                Update.update("payed", true), SubmitEntity.class);

        if (updateResult.getModifiedCount() == 0) {
            return StatusCode.ERR_DATABASE;
        } else {
            CommonHasOrderScheduleTask.hasRun = false;

            this.OnTaskRunningEnd();
        }

        return StatusCode.OK;
    }

    @Override
    public SubmitEntity queryLastLimit(String secret) {
        if (isSecret(secret, false)) return null;

        return mongoTemplate.find(new Query().with(new Sort(Sort.Direction.DESC, "_id")),
                SubmitEntity.class).get(0);
    }

    private boolean isSecret(String secret, boolean isOrder) {
        String envSecret = environment.getProperty("secret.api");

        assert envSecret != null;

        if (isOrder) envSecret = environment.getProperty("secret.order");

        assert envSecret != null;

        return !(envSecret.equals(secret));
    }
}
