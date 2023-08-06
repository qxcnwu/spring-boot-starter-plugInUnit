package com.qxc.utiles.reflectancetools;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @Author qxc
 * @Date 2023 2023/7/12 14:04
 * @Version 1.0
 * @PACKAGE com.qxc.utiles
 */
@Slf4j
public final class AnnotationTools {
    /**
     * 监测对象有没有循环引用
     *
     */
    @Contract(pure = true)
    public static boolean checkCircleReference(@NotNull HashMap<String, PipeLine.MethodNode> map) {
        final Optional<Map.Entry<String, PipeLine.MethodNode>> entry = map.entrySet().stream().max(Comparator.comparingInt(a -> a.getValue().getId()));
        if (entry.isEmpty()) {
            return false;
        }
        final int max = entry.get().getValue().getId() + 1;
        for (String key : map.keySet()) {
            int[] temp = new int[max];
            if (checkCircleReference(key, map, temp)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 监测单个对象有没有循环引用
     *
     */
    @Contract(pure = true)
    public static boolean checkCircleReference(String currentMethod, @NotNull HashMap<String, PipeLine.MethodNode> map, int @NotNull [] occur) {
        // 如果存在循环使用那么返回false
        if (occur[map.get(currentMethod).getId()] != 0) {
            log.info("Method {} occur two nums;", currentMethod);
            return true;
        }
        // 查询得到不存在的方法
        if (!map.containsKey(currentMethod)) {
            return true;
        }
        for (String s : map.get(currentMethod).parents) {
            occur[map.get(currentMethod).getId()] = 1;
            if (checkCircleReference(s, map, occur)) {
                return true;
            }
            occur[map.get(currentMethod).getId()] = 0;
        }
        return false;
    }


    /**
     * 水漫获取
     *
     */
    @Contract(pure = true)
    public static @NotNull List<PipeLine.MethodNode> parse(@NotNull HashMap<String, PipeLine.MethodNode> map) {
        List<PipeLine.MethodNode> list = new ArrayList<>();
        PriorityQueue<PipeLine.MethodNode> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.pnum));
        for (String key : map.keySet()) {
            for (String parent : map.get(key).getParents()) {
                map.get(parent).add(key);
            }
            queue.add(map.get(key));
        }
        // 执行顺序
        int runStat = 0;
        // 添加完成后开始轮询
        while (!queue.isEmpty()) {
            // 首先找到当前前置引用为0的对象，移除队列
            ArrayList<PipeLine.MethodNode> temp = new ArrayList<>();
            while (!queue.isEmpty() && queue.peek().getPnum() == 0) {
                temp.add(queue.poll().setExecProc(runStat));
            }
            // 对应的基础引用计数-1
            for (PipeLine.MethodNode pm : temp) {
                for (String son : pm.getSons()) {
                    map.get(son).pnumDecrease();
                }
            }
            list.addAll(temp);
            // 如果当前没有任何计数为0的方法那么则移除
            if (temp.isEmpty()) {
                return list;
            }
            // 重新激活排序
            queue.add(new PipeLine.MethodNode().setPnum(-1));
            queue.poll();
            runStat++;
        }
        return list;
    }
}
