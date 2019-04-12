package cn.xyiio.pay.entity;

import org.bson.types.ObjectId;

public class SubmitEntity {

    private ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    private int money;

    private boolean payed;

    private String createAt;

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public SubmitEntity(int money, boolean payed, String createAt) {
        this.money = money;
        this.payed = payed;
        this.createAt = createAt;
    }

    public int getMoney() {
        return money;
    }

    @Override
    public String toString() {
        return "SubmitEntity: [ id = " + id + ", money = " + money + ", payed = " + payed + ", currentAt = " + createAt + " ]";
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
