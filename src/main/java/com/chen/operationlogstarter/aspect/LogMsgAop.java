package com.chen.operationlogstarter.aspect;

import com.alibaba.fastjson2.JSON;
import com.hzwotu.operationlogsdk.aspect.config.OperationLogConfigProperties;
import com.hzwotu.operationlogsdk.aspect.config.ThreadPoolConfigOperationLog;
import com.hzwotu.operationlogsdk.dto.FilterDto;
import com.hzwotu.operationlogsdk.po.OperationLogEntity;
import com.hzwotu.operationlogsdk.service.OperationLogService;
import com.hzwotu.operationlogsdk.utils.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @Author chenl
 * @Date 2022/9/14 11:51 上午
 */
@Aspect
@Component
public class LogMsgAop {
    public static final String AUTHORIZATION_FINANCE_SERVICE = "finance-service";

    private final Logger logger = LoggerFactory.getLogger(LogMsgAop.class);

    @Autowired
    OperationLogService operationLogService;
    @Autowired
    OperationLogConfigProperties operationLogConfigProperties;
    @Autowired
    ThreadPoolConfigOperationLog threadPoolConfigOperationLog;
    @Autowired
    ExecutorService threadPool;

    Map<String, FilterDto> parseCache = new ConcurrentHashMap<>();

    @AfterReturning("@annotation(com.hzwotu.operationlogsdk.aspect.LogMsgAspectAnnotation)")
    public void logMsg(final JoinPoint jp) {
        logger.info("进入操作日志切面===========>>>>");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String remoteHost = getRemoteHost(request);
        String requestURI = request.getRequestURI();
        threadPool.execute(() -> {
            try {
                async(jp, request, remoteHost, requestURI);
            } catch (Throwable e) {
                throw new RuntimeException("错误:", e);
            }
        });


    }

    /**
     * 异步插入
     */
    private void async(final JoinPoint jp, HttpServletRequest request, String ipAddress, String requestUri) throws Throwable {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Method method = signature.getMethod();
        Object[] args = jp.getArgs();
        LogMsgAspectAnnotation logAnnotation = method.getAnnotation(LogMsgAspectAnnotation.class);
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);

        String modelResult = "";
        if (!Objects.isNull(annotation)) {
            modelResult = annotation.value();
        }

        String userCode = request.getHeader("userCode");
        String userName = URLDecoder.decode(Optional.of(request.getHeader("userName")).orElse(""), StandardCharsets.UTF_8);
        if (StringUtils.isEmpty(userCode)) {
            userCode = "";
        }
        if (StringUtils.isEmpty(userName)){
            userName = "";
        }
        FilterDto filterDto = parseNoExpress(request, modelResult);
        OperationLogEntity operationLogEntity = new OperationLogEntity();
        operationLogEntity.create(operationLogEntity,userCode,filterDto);
        operationLogEntity.setIp(ipAddress);
        operationLogEntity.setAdminName(userName);

        //填充keyCodeAndParam
        fillKeyCodeAndParam(requestUri, parameterNames, args, operationLogEntity);

        logger.debug("操作日志切面 插入数据:{}", operationLogEntity);
        try {
            operationLogService.sendMessageToMQ(operationLogEntity);
        } catch (Exception e) {
            logger.error("插入异常:", e);
            throw new RuntimeException("插入异常:", e);
        }
    }

    private void fillKeyCodeAndParam(String requestUri, String[] parameterNames, Object[] args, OperationLogEntity operationLogEntity) {
        Map<String, Object> stringObjectMap = changeArgsToMap(parameterNames, args);
        String paramJson = JSON.toJSONString(stringObjectMap);
        operationLogEntity.setParam(paramJson);

        Collection<Object> values = stringObjectMap.values();
        List<Object> codes = values.stream().map((item) -> {
            //传入的参数 目前来看只有String和DTO两种 这样不会有问题。String 是因为有userCode
            // 如果有其他基础类型的话 只能强制挂到DTO下面去
            //不能直接丢到controller的入参
            if (item instanceof String){
                return null;
            }
            return JSON.parseObject(JSON.toJSONString(item)).get("code");
        }).collect(Collectors.toList());

        Object first = codes.stream().filter(Objects::nonNull).findFirst().orElse("");
        if (StringUtil.isNotEmpty((String)first)) {
            operationLogEntity.setKeyCode((String)first);
        } else {
            String[] strings = parseUri(requestUri);
            operationLogEntity.setKeyCode(strings[strings.length - 1]);
        }
    }



    private FilterDto parseNoExpress(HttpServletRequest request, String
            modelResult) {
        String uri = request.getRequestURI();
        if (parseCache.containsKey(uri)) {
            return parseCache.get(uri);
        }
        String modelNameOrDefault = operationLogConfigProperties.getZhName();
        FilterDto filterDto = new FilterDto(modelNameOrDefault, modelResult);
        parseCache.put(uri, filterDto);
        return filterDto;
    }

    /**
     * @param "https://www.baidu.com/customer/follow/update" /customer/follow/update
     * @return [, customer, follow, update]
     */
    private String[] parseUri(String uri) {
        String[] split = uri.split("/");
        if (split.length == 0) {
            //一般不会走到这里
            logger.error("splitString:{}", JSON.toJSONString(split));
            throw new RuntimeException("uri解析失败");
        }
        return split;
    }


    private String getRemoteHost(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "0:0:0:0:0:0:0:1".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        logger.debug(e.getMessage());
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length()
            if (ipAddress != null && ipAddress.length() > 15) {
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * 初始化的时候需要从配置文件获取数据
     */
    @PostConstruct
    private void checkConfigIsFull() {
        if (StringUtils.isEmpty(operationLogConfigProperties.getEnName()) || StringUtils.isEmpty(operationLogConfigProperties.getZhName())) {
            throw new RuntimeException("com.wotu.operationlog.zhName/enName映射配置需要配置完全");
        }
    }


    /**
     * 生成参数的key-value 用来序列化成json丢到ES 当param
     * key String     参数的名称
     * value Object   参数的具体的值
     * */
    private Map<String,Object> changeArgsToMap(String[] argsName,Object[] args){
        HashMap<String, Object> hashMap = new HashMap<>();
        if (argsName.length == 0 || args.length == 0){
            return hashMap;
        }
        for (int i = 0; i < argsName.length; i++) {
            String s = argsName[i];
            Object arg = args[i];
            hashMap.put(s,arg);
        }
        return hashMap;
    }

}

