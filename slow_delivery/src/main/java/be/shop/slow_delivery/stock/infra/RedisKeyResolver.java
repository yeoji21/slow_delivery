package be.shop.slow_delivery.stock.infra;

public class RedisKeyResolver{
    public static String getKey(long stockId) {
        return "stock:" + stockId;
    }
}
