# springboot-pay-api
个人用户使用支付宝、微信收款实时到账监听系统

- APP 拉取当前公有的配置信息 CommonEntity 判断 hasOrder 字段真假
- 如果为假，表示当前没有正在支付的订单，即开个新订单，Task 开启五分钟后自动销毁订单，同时 hasOrder 为真
