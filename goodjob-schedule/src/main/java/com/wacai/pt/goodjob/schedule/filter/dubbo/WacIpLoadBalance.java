package com.wacai.pt.goodjob.schedule.filter.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.AtomicPositiveInteger;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.LoadBalance;
import com.wacai.pt.goodjob.remote.bean.ExecuteRequest;
import com.wacai.pt.goodjob.schedule.util.HostsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xuanwu on 16/5/9.
 */
public class WacIpLoadBalance implements LoadBalance {
    public static final String                                 NAME            = "wacip";
    private final ConcurrentMap<String, AtomicPositiveInteger> sequences       = new ConcurrentHashMap<String, AtomicPositiveInteger>();

    private final ConcurrentMap<String, AtomicPositiveInteger> weightSequences = new ConcurrentHashMap<String, AtomicPositiveInteger>();
    private static HostsUtil                                   hostsUtil       = HostsUtil
                                                                                   .getHostsUtil();

    /**
     * select one invoker in list.
     *
     * @param invokers   invokers.
     * @param url        refer url
     * @param invocation invocation.
     * @return selected invoker.
     */
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        List<Invoker<T>> _invokers = null;
        if (invocation.getArguments() != null
            && invocation.getArguments()[0] instanceof ExecuteRequest) {
            ExecuteRequest request = (ExecuteRequest) invocation.getArguments()[0];
            if (request.getJobId() == null){
                //ip缓存
                if (request.getTaskType() == null){
                    ipCache(invokers, request.getTaskId(), false);
                }else {
                    ipCache(invokers, request.getTaskId(), true);
                }
                return null;
            }

            //指定执行机或者禁用
            if(hostsUtil.getOwnerHosts().containsKey(String.valueOf(request.getTaskId())) || hostsUtil.getDisableHosts().containsKey(String.valueOf(request.getTaskId()))){
                _invokers = new ArrayList<>(invokers.size());
                for (Invoker<T> invoker : invokers) {
                    _invokers.add(invoker);
                }

                if (hostsUtil.getOwnerHosts().containsKey(String.valueOf(request.getTaskId()))) {
                    //指定执行机
                    processOwnerIps(_invokers, request.getTaskId());
                }else {
                    //排除禁用IP
                    excludeIps(_invokers, request.getTaskId());
                }
            }
        }

        if (_invokers != null) {
            return LoadBalance(_invokers, invocation);
        }

        return LoadBalance(invokers, invocation);
    }

    private <T> Invoker<T> LoadBalance(List<Invoker<T>> invokers, Invocation invocation)
                                                                                        throws RpcException {
        if (invokers.size() == 0) {
            throw new RpcException("没有可用的执行机，请检查配置.");
        } else if (invokers.size() == 1) {
            return invokers.get(0);
        }

        String key = invokers.get(0).getUrl().getServiceKey() + "." + invocation.getMethodName();
        int length = invokers.size(); // 总个数
        int maxWeight = 0; // 最大权重
        int minWeight = Integer.MAX_VALUE; // 最小权重
        for (int i = 0; i < length; i++) {
            int weight = getWeight(invokers.get(i), invocation);
            maxWeight = Math.max(maxWeight, weight); // 累计最大权重
            minWeight = Math.min(minWeight, weight); // 累计最小权重
        }
        if (maxWeight > 0 && minWeight < maxWeight) { // 权重不一样
            AtomicPositiveInteger weightSequence = weightSequences.get(key);
            if (weightSequence == null) {
                weightSequences.putIfAbsent(key, new AtomicPositiveInteger());
                weightSequence = weightSequences.get(key);
            }
            int currentWeight = weightSequence.getAndIncrement() % maxWeight;
            List<Invoker<T>> weightInvokers = new ArrayList<Invoker<T>>();
            for (Invoker<T> invoker : invokers) { // 筛选权重大于当前权重基数的Invoker
                if (getWeight(invoker, invocation) > currentWeight) {
                    weightInvokers.add(invoker);
                }
            }
            int weightLength = weightInvokers.size();
            if (weightLength == 1) {
                return weightInvokers.get(0);
            } else if (weightLength > 1) {
                invokers = weightInvokers;
                length = invokers.size();
            }
        }
        AtomicPositiveInteger sequence = sequences.get(key);
        if (sequence == null) {
            sequences.putIfAbsent(key, new AtomicPositiveInteger());
            sequence = sequences.get(key);
        }
        // 取模轮循
        return invokers.get(sequence.getAndIncrement() % length);
    }

    private <T> void ipCache(List<Invoker<T>> invokers, Integer taskId, boolean manual) {
        for (Invoker invoker : invokers) {
            if (manual){
                hostsUtil.ipCheck(invoker.getUrl().getIp(), taskId);
            }else {
                hostsUtil.ipCache(invoker.getUrl().getIp(), taskId);
            }

        }
    }

    private <T> void processOwnerIps(List<Invoker<T>> invokers, Integer taskId) {
        List<String> ownerHosts = hostsUtil.getOwnerHosts().get(String.valueOf(taskId));
        boolean isExist = false;
        if (ownerHosts != null && ownerHosts.size() > 0) {
            for (int i = 0; i < invokers.size(); i++) {
                isExist = false;
                for (String _host : ownerHosts) {
                    if (_host.equals(invokers.get(i).getUrl().getIp())) {
                        isExist = true;
                        break;
                    }
                }

                if (!isExist) {
                    invokers.remove(i);
                    i--;
                }
            }
        }
    }

    /**
     * 排除禁用IP
     * @param invokers
     * @param taskId
     * @param <T>
     */
    private <T> void excludeIps(List<Invoker<T>> invokers, Integer taskId) {
        List<String> disableHosts = hostsUtil.getDisableHosts().get(String.valueOf(taskId));
        if (disableHosts != null && disableHosts.size() > 0) {
            boolean isExist = false;
            for (int i = 0; i < invokers.size(); i++) {
                isExist = false;
                for (String _host : disableHosts) {
                    if (_host.equals(invokers.get(i).getUrl().getIp())) {
                        isExist = true;
                        break;
                    }
                }

                if (isExist) {
                    invokers.remove(i);
                    i--;
                }
            }
        }
    }

    protected int getWeight(Invoker<?> invoker, Invocation invocation) {
        int weight = invoker.getUrl().getMethodParameter(invocation.getMethodName(),
            Constants.WEIGHT_KEY, Constants.DEFAULT_WEIGHT);
        if (weight > 0) {
            long timestamp = invoker.getUrl().getParameter(Constants.TIMESTAMP_KEY, 0L);
            if (timestamp > 0L) {
                int uptime = (int) (System.currentTimeMillis() - timestamp);
                int warmup = invoker.getUrl().getParameter(Constants.WARMUP_KEY,
                    Constants.DEFAULT_WARMUP);
                if (uptime > 0 && uptime < warmup) {
                    weight = calculateWarmupWeight(uptime, warmup, weight);
                }
            }
        }
        return weight;
    }

    static int calculateWarmupWeight(int uptime, int warmup, int weight) {
        int ww = (int) ((float) uptime / ((float) warmup / (float) weight));
        return ww < 1 ? 1 : (ww > weight ? weight : ww);
    }
}
