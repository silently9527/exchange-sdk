<p align="center">
  <a href="https://t.zsxq.com/h2EIR" target="_blank"><img alt="" src="https://img.shields.io/badge/知识星球-Herman's Notes-red&logoColor=FC5531" /></a>
  <a><img src="https://img.shields.io/badge/公众号-贝塔学JAVA-blue.svg" alt="公众号"></a>
  <a href="https://juejin.cn/user/2779199782521693"><img src="https://img.shields.io/badge/juejin-掘金-yellow.svg" alt="掘金"></a>
  <a href="https://blog.csdn.net/asdewq380303318"><img src="https://img.shields.io/badge/csdn-CSDN-red.svg" alt="CSDN"></a>
  <a href="https://my.oschina.net/u/3230120"><img src="https://img.shields.io/badge/oschina-开源中国-green" alt="开源中国"></a>
  <a href="https://www.zhihu.com/people/huaan9527-57/posts"><img src="https://img.shields.io/badge/zhihu-知乎-purple" alt="知乎"></a>
</p>

# ExchangeSdk
作为长期混迹在合约市场的老韭菜来说，已不能满足与手动下单来亏钱，必须得通过脚本来加速，为了达到这个目的就产生了项目。目前封装的主要是合约的API接口，不支持现货交易。


## 知识星球: [Herman's Notes](https://t.zsxq.com/h2EIR)
加微信silently9527, 加入技术交流群；欢迎加入进来一起交流使用脚本快速亏钱秘诀

## Features
1. 抽象出了交易所合约的通用接口
2. 支持的交易所：binance, okex, kucoin
3. 支持 Rest接口 和 WebSocket

## How to install

1. 克隆下仓库代码执行`mvn install`
2. maven依赖添加到自己的项目

```xml
<dependency>
    <groupId>org.herman</groupId>
    <artifactId>exchange-sdk</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## How to use

- `FutureRestApiClient`: 主要抽象出了合约的RestApi接口
- `FutureSubscriptionClient`: 主要抽象出了合约的WebSocket接口
- `FutureApiInternalFactory`: 封装了创建各个交易所FutureRestApiClient和FutureSubscriptionClient的工具类

#### Examples
1. 使用rest接口查询出Binance的标记价格

```java
final FutureRestApiClient restApiClient = FutureApiInternalFactory
        .getInstance()
        .createBinanceFutureRestApiClient(Constants.Future.BINANCE_REST_API_BASE_URL, "xxx", "xxx");
final List<MarkPrice> markPrice = restApiClient.getMarkPrice("BTCUSDT");
System.out.println(markPrice);
```

输出：
```
[MarkPrice[symbol=BTCUSDT,markPrice=57738.69215152,time=1723775413000]]
```


2. 使用WebSocket监听Binance的标记价格

```java
final FutureSubscriptionClient subscriptionClient = FutureApiInternalFactory
        .getInstance()
        .createBinanceFutureSubscriptionClient(Constants.Future.BINANCE_WS_API_BASE_URL, "xxx", "xxx");
subscriptionClient.subscribeMarkPriceEvent("BTCUSDT",
        markPriceEvent -> {
            //订阅成功后的回调
            System.out.println(markPriceEvent);
        },
        e -> {
            //异常后的执行逻辑
            e.printStackTrace();
        }
);
```

## 博客地址
[https://herman7z.site](https://herman7z.site)

![知识星球](https://raw.githubusercontent.com/silently9527/images/main/202408151725390.png)
