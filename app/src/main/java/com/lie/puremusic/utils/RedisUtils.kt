package com.lie.puremusic.utils

import redis.clients.jedis.Jedis

object RedisUtils {
    fun getConnection(): Jedis? {
        var jedis = Jedis("r-m5eiii8dfrqdjvdmdspd.redis.rds.aliyuncs.com",6379)
        jedis.auth("Lcz123456")
        return jedis
    }
}