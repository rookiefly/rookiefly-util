package com.rookiefly.commons.lottery;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Arithmetic {

    private static final int MULRIPLE = 1000000;

    private Map<Integer, int[]> prizeScopes = new HashMap<>();

    private Map<Integer, Integer> prizeQuantity = new HashMap<>();

    public Arithmetic(List<Prize> prizes) {
        init(prizes);
    }

    public int lottery() {
        // 获取1-1000000之间的一个随机数
        int luckyNumber = ThreadLocalRandom.current().nextInt(MULRIPLE);
        int luckyPrizeId = 0;
        // 查找随机数所在的区间
        if ((null != prizeScopes) && !prizeScopes.isEmpty()) {
            Set<Map.Entry<Integer, int[]>> entrySets = prizeScopes.entrySet();
            for (Map.Entry<Integer, int[]> m : entrySets) {
                int key = m.getKey();
                if (luckyNumber >= m.getValue()[0] && luckyNumber <= m.getValue()[1]) {
                    if (prizeQuantity.get(key) > 0) {
                        luckyPrizeId = key;
                        break;
                    }
                    break;
                }
            }
        }

        if (luckyPrizeId > 0) {
            prizeQuantity.put(luckyPrizeId, prizeQuantity.get(luckyPrizeId) - 1);
        }

        return luckyPrizeId;
    }

    private void init(List<Prize> prizes) {
        int lastScope = 0;
        // 洗牌，打乱奖品次序
        Collections.shuffle(prizes);
        for (Prize prize : prizes) {
            int prizeId = prize.getPrizeId();
            // 划分区间
            int currentScope = lastScope + prize.getProbability().multiply(new BigDecimal(MULRIPLE)).intValue();
            prizeScopes.put(prizeId, new int[]{lastScope + 1, currentScope});
            prizeQuantity.put(prizeId, prize.getQuantity());

            lastScope = currentScope;
        }
    }
}