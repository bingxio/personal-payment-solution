# springboot-pay-api
个人用户使用支付宝、微信收款实时到账监听系统

- APP 拉取当前公有的配置信息 CommonEntity 判断 hasOrder 字段真假
- 如果为假，表示当前没有正在支付的订单，即开个新订单，Task 开启五分钟后自动销毁订单，同时 hasOrder 为真
- 客户端监听到账信息，POST /submit/update 接口，即更新最近的订单 payed 字段为真，同时 hasOrder 为假，停止 Task
- APP 限制在五分钟内进行支付结果查询，GET /submit/last 接口，即查询最近的订单，判断 payed 字段
