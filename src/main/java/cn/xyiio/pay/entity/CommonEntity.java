package cn.xyiio.pay.entity;

import org.bson.types.ObjectId;

public class CommonEntity {

    private ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    private boolean hasOrder;

    private String createAt;
    private String updateAt;

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public CommonEntity() {}

    public CommonEntity(boolean hasOrder, String createAt, String updateAt) {
        this.hasOrder = hasOrder;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public boolean isHasOrder() {
        return hasOrder;
    }

    public void setHasOrder(boolean hasOrder) {
        this.hasOrder = hasOrder;
    }
}
