package com.chen.operationlogstarter.aspect;

import com.chen.operationlogstarter.aspect.config.OperationLogConfigProperties;
import com.chen.operationlogstarter.aspect.config.ThreadPoolConfig;
import com.chen.operationlogstarter.dto.FilterDto;
import com.chen.operationlogstarter.dto.LogMsgDto;
import com.chen.operationlogstarter.dto.SubStringPosition;
import com.chen.operationlogstarter.filter.MyRequestWrapper;
import com.chen.operationlogstarter.po.OperationLogEntity;
import com.chen.operationlogstarter.service.OperationLogService;
import com.chen.operationlogstarter.utils.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
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
import java.lang.reflect.Parameter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

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
    ThreadPoolConfig threadPoolConfig;
    Map<String, FilterDto> parseCache = new ConcurrentHashMap<>();


    private static final Map<String, String> modelName = new HashMap();


    @AfterReturning("@annotation(com.chen.operationlogstarter.aspect.LogMsgAspectAnnotation)")
    public void logMsg(final JoinPoint jp) {
        ExecutorService executorService = threadPoolConfig.threadPool();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String remoteHost = getRemoteHost(request);
        String requestURI = request.getRequestURI();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    async(jp, request, remoteHost, requestURI);
                } catch (Throwable e) {
                    throw new RuntimeException("错误:", e);
                }
            }
        });


    }


    private void async(final JoinPoint jp, HttpServletRequest request, String ipAddress, String requestUri) throws Throwable {
        Object[] args = jp.getArgs();
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        String s = jp.getTarget().getClass().getName() + "." + method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = ((MethodSignature) jp.getSignature()).getParameterNames();
        LogMsgAspectAnnotation logAnnotation = method.getAnnotation(LogMsgAspectAnnotation.class);
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        String modelResult = "";
        if (!Objects.isNull(annotation)) {
            modelResult = annotation.value();
        }

        String expression = logAnnotation.expression();
        if (!StringUtils.isEmpty(expression)) {
            String s1 = parseExpressWithRequest(expression, parameters, args, parameterTypes);
        } else {
            String userCode = request.getHeader("userCode");
            if (StringUtils.isEmpty(userCode)) {
                userCode = "";
            }
            FilterDto filterDto = parseNoExpress(request, userCode, modelResult);
            OperationLogEntity operationLogEntity = new OperationLogEntity();
            LocalDateTime now = LocalDateTime.now();
            operationLogEntity.setCreatedAt(now);

            operationLogEntity.setCode(getCodeFromHttp());
            operationLogEntity.setModule(filterDto.getModelName());
            operationLogEntity.setAdminCode(userCode);
            operationLogEntity.setAction(filterDto.getModelResult());
            operationLogEntity.setIp(ipAddress);

            String requestString = "{}";

            MyRequestWrapper myRequestWrapper = new MyRequestWrapper(request);
            byte[] body = myRequestWrapper.getBody();
            if (!Objects.isNull(body) && body.length != 0) {
                requestString = new String(body);
            }
            operationLogEntity.setParam(requestString);
            Map<String, Object> stringObjectMap = JsonUtil.parseJSON2Map(requestString);
            String code = (String) stringObjectMap.getOrDefault("code", "");
            if (!StringUtils.isEmpty(code)) {
                operationLogEntity.setKeyCode(code);
            } else {
                String[] strings = parseUri(requestUri);
                if (body.length == 0) {
                    operationLogEntity.setKeyCode(strings[strings.length - 1]);
                } else {
                    operationLogEntity.setKeyCode("");
                }
            }

            try {
                operationLogService.insert(operationLogEntity);
            } catch (Exception e) {
                logger.error("插入异常:", e);
                throw new RuntimeException("插入异常:", e);
            }
        }
    }

    private String parseExpressWithRequest(String express, Parameter[] parameters, Object[] args, Class<?>[] parameterTypes) {

        char[] expressChars = express.toCharArray();
        List<SubStringPosition> subStringPosition = getSubStringPosition(expressChars);

        HashMap<String, LogMsgDto> stringObjectHashMap = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            LogMsgDto logMsgDto = new LogMsgDto(parameterTypes[i].getName(), args[i]);
            stringObjectHashMap.put(parameters[i].getName(), logMsgDto);
        }
        return getFinalString(subStringPosition, express, stringObjectHashMap);
    }

    private List<SubStringPosition> getSubStringPosition(char[] expressChars) {
        ArrayList<SubStringPosition> subStringPositions = new ArrayList<>();
        int left = 0;
        int right = 0;
        while (left < expressChars.length) {
            if (expressChars[left] == '{') {
                right = left;
                while (right < expressChars.length) {
                    if (expressChars[right] == '}') {
                        subStringPositions.add(new SubStringPosition(left, right));
                        left = right;
                        break;
                    }
                    ++right;
                }
            }
            ++left;
        }
        return subStringPositions;
    }

    private String getFinalString(List<SubStringPosition> subStringPosition, String express, HashMap<String, LogMsgDto> stringObjectHashMap) {
        String[] strings = new String[subStringPosition.size()];
        for (int i = 0; i < subStringPosition.size(); i++) {
            String key = express.substring(subStringPosition.get(i).getLeft() + 1, subStringPosition.get(i).getRight());
            key = String.format("#{%s}", key);
            strings[i] = key;
        }

        String expressCopy = express;
        for (int i = 0; i < strings.length; i++) {
            String key = express.substring(subStringPosition.get(i).getLeft() + 1, subStringPosition.get(i).getRight());
            //#{vo.databaseName} #{in} key = 括号里面的东西 vo.database vo.ipaddress in
            String s = getObjectValueUseReflect(stringObjectHashMap, key);
            expressCopy = expressCopy.replace(strings[i], s);
        }
        return expressCopy;
    }

    /**
     * key requestIn
     * if requestIn contanin . get first
     */
    private String getObjectValueUseReflect(HashMap<String, LogMsgDto> map, String key) {
//        FieldUtils.readDeclaredField()
        String[] split = new String[2];
        if (key.contains(".")) {
            split = Arrays.copyOf(key.split("\\."), 2);
            key = split[0];
        }
        LogMsgDto logMsgDto = map.get(key);
        String classType = logMsgDto.getClassType();
/*        try {
            switch (classType) {
                case "java.lang.String":
                    Object value = FieldUtils.readDeclaredField(logMsgDto.getValue(), "value", true);
                    char[] o1 = (char[]) value;
                    return new String(o1);
                case "java.lang.int":
                    System.out.println("待实现");
                    break;
                default:
                    Object o = FieldUtils.readDeclaredField(logMsgDto.getValue(), split[1], true);
                    return o.toString();
            }
        } catch (IllegalAccessException exception) {
            throw new RuntimeException("内部错误");
        }*/
        return "";
    }


    private FilterDto parseNoExpress(HttpServletRequest request, String userCode, String modelResult) {
        String uri = request.getRequestURI();
        if (parseCache.containsKey(uri)) {
            return parseCache.get(uri);
        }
        String[] split = parseUri(uri);
        String modelNameOrDefault = modelName.getOrDefault(split[1], "");
        String key = split[split.length - 1];
        if (key.startsWith("update") || key.endsWith("update")) {
            key = "update";
        } else if (key.startsWith("delete") || key.endsWith("delete")) {
            key = "delete";
        } else if (key.startsWith("insert") || key.endsWith("insert")) {
            key = "insert";
        } else {
            key = "unknown";
        }
        String methodName = modelName.getOrDefault(key, "");
        String childName = modelName.getOrDefault(split[split.length - 2], "");
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
            logger.error("split:{}", split);
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
    private void initDataMap() {
        if (operationLogConfigProperties.getEnName().isEmpty() || operationLogConfigProperties.getZhName().isEmpty()) {
            throw new RuntimeException("映射配置需要配置完全");
        }
        modelName.put(operationLogConfigProperties.getEnName(), operationLogConfigProperties.getZhName());
    }


    private String getCodeFromHttp() {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = String.format("http://%s/id/next_id", operationLogConfigProperties.getIdServiceAddress());
        HttpGet get = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            return (EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            logger.error("获取id-service code 失败", e);
        }
        return "";
    }


}

