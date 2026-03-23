package com.smart.framework.extension.captcha.resource;

import cloud.tianai.captcha.common.constant.CommonConstant;
import cloud.tianai.captcha.resource.CrudResourceStore;
import cloud.tianai.captcha.resource.ImageCaptchaResourceManager;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import cloud.tianai.captcha.resource.common.model.dto.ResourceMap;
import com.smart.framework.commons.core.utils.JsonUtils;
import com.smart.framework.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RList;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 基于redis service实现的资源存储
 * @author <a href="https://github.com/ming4762">ShiZhongMing</a>
 * 2026-03-22 21:40
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class SmartRedisServiceResourceStore implements CrudResourceStore {

    private static final String RESOURCE_KEY_PREFIX = "captcha:resource:";
    private static final String TEMPLATE_KEY_PREFIX = "captcha:template:";

    private final RedisService redisService;

    /**
     * 添加资源
     *
     * @param type     验证码类型
     * @param resource 资源
     */
    @Override
    public void addResource(String type, Resource resource) {
        this.redisService.getRedissonClient()
                .getList(joinResourceKey(type, resource.getTag()))
                .addLast(JsonUtils.toJsonString(resource));
    }

    /**
     * 添加模板
     *
     * @param type     验证码类型
     * @param template 模板
     */
    @Override
    public void addTemplate(String type, ResourceMap template) {
        this.redisService.getRedissonClient()
                .getList(joinTemplateKey(type, template.getTag()))
                .addLast(JsonUtils.toJsonString(template));
    }

    /**
     * 删除资源
     *
     * @param type 验证码类型
     * @param id   资源ID
     * @return Resource
     */
    @Override
    public Resource deleteResource(String type, String id) {
        List<String> keys = this.redisService.matchKeys(joinResourceKey(type, "*"));
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        }
        for (String key : keys) {
            RList<String> redisList = this.redisService.getRedissonClient()
                    .getList(key);
            int size = redisList.size();
            if (size < 1) {
                continue;
            }
            List<String> range = redisList.range(0, size);
            if (range == null) {
                return null;
            }
            for (String json : range) {
                Resource resource = JsonUtils.parse(json, Resource.class);
                if (resource.getId().equals(id)) {
                    redisList.remove(json);
                    return resource;
                }
            }
        }
        return null;
    }

    /**
     * 删除模板
     *
     * @param type 验证码类型
     * @param id   资源ID
     * @return ResourceMap
     */
    @Override
    public ResourceMap deleteTemplate(String type, String id) {
        List<String> keys = this.redisService.matchKeys(joinTemplateKey(type, "*"));
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        }
        for (String key : keys) {
            RList<String> redisList = this.redisService.getRedissonClient()
                    .getList(key);
            int size = redisList.size();
            if (size < 1) {
                continue;
            }
            List<String> range = redisList.range(0, size);
            if (range == null) {
                return null;
            }
            for (String json : range) {
                ResourceMap template = JsonUtils.parse(json, ResourceMap.class);
                if (template.getId().equals(id)) {
                    redisList.remove(json);
                    return template;
                }
            }
        }
        return null;
    }

    /**
     * 获取某个资源列表
     *
     * @param type 验证码类型
     * @param tag  资源标签(可为空)
     * @return List<Resource>
     */
    @Override
    public List<Resource> listResourcesByTypeAndTag(String type, String tag) {
        if (StringUtils.isNotBlank(tag)) {
            return getResources(type, tag);
        }
        return List.of();
    }

    protected List<Resource> getResources(String type, String tag) {
        String key = joinResourceKey(type, tag);
        RList<String> redisList = this.redisService.getRedissonClient()
                .getList(key);
        if (redisList.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> range = redisList.range(0, redisList.size());
        List<Resource> result = new ArrayList<>(range.size());
        for (String json : range) {
            result.add(JsonUtils.parse(json, Resource.class));
        }
        return result;
    }

    /**
     * 获取某个模板列表
     *
     * @param type 验证码类型
     * @param tag  资源标签(可为空)
     * @return List<ResourceMap>
     */
    @Override
    public List<ResourceMap> listTemplatesByTypeAndTag(String type, String tag) {
        if (StringUtils.isNotBlank(tag)) {
            return getTemplates(type, tag);
        }
        List<String> keys = this.redisService.matchKeys(joinTemplateKey(type, "*"));
        if (CollectionUtils.isEmpty(keys)) {
            return List.of();
        }
        List<ResourceMap> templates = new ArrayList<>();
        for (String key : keys) {
            RList<String> redisList = this.redisService.getRedissonClient()
                    .getList(key);
            if (redisList.isEmpty()) {
                continue;
            }
            List<String> range = redisList.range(0, redisList.size());
            if (!CollectionUtils.isEmpty(range)) {
                for (String json : range) {
                    templates.add(JsonUtils.parse(json, ResourceMap.class));
                }
            }
        }
        return templates;
    }

    protected List<ResourceMap> getTemplates(String type, String tag) {
        String key = joinTemplateKey(type, tag);
        RList<String> redisList = this.redisService.getRedissonClient()
                .getList(key);
        if (redisList.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> range = redisList.range(0, redisList.size());
        List<ResourceMap> result = new ArrayList<>(range.size());
        for (String json : range) {
            result.add(JsonUtils.parse(json, ResourceMap.class));
        }
        return result;
    }

    /**
     * 清除所有内置模板
     */
    @Override
    public void clearAllTemplates() {
        List<String> keys = this.redisService.matchKeys(TEMPLATE_KEY_PREFIX + "*");
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        this.redisService.batchDelete(keys);
    }

    /**
     * 清除所有内置资源
     */
    @Override
    public void clearAllResources() {
        List<String> keys = this.redisService.matchKeys(RESOURCE_KEY_PREFIX + "*");
        if (CollectionUtils.isEmpty(keys)) {
            return;
        }
        this.redisService.batchDelete(keys);
    }

    @Override
    public void init(ImageCaptchaResourceManager resourceManager) {

    }

    /**
     * 随机获取某个资源
     *
     * @param type     type
     * @param tag      tag
     * @param quantity 获取数量
     * @return Resource
     */
    @Override
    public List<Resource> randomGetResourceByTypeAndTag(String type, String tag, Integer quantity) {
        String key = this.joinResourceKey(type, tag);
        RList<String> redisList = this.redisService.getRedissonClient().getList(key);
        int size = redisList.size();
        if (size == 0 || quantity > size) {
            throw new IllegalArgumentException("请求的资源数量超过可用资源总数");
        }
        Set<Integer> indexes = new HashSet<>(quantity);
        while (indexes.size() < quantity) {
            indexes.add(ThreadLocalRandom.current().nextInt(size));
        }
        List<Resource> result = new ArrayList<>(quantity);
        for (Integer index : indexes) {
            String resourceJson = redisList.get(index);
            result.add(JsonUtils.parse(resourceJson, Resource.class));
        }
        return result;
    }

    /**
     * 随机获取某个模板通过type
     *
     * @param type     type
     * @param tag      tag
     * @param quantity 获取数量
     * @return Map<String, Resource>
     */
    @Override
    public List<ResourceMap> randomGetTemplateByTypeAndTag(String type, String tag, Integer quantity) {
        String key = this.joinTemplateKey(type, tag);
        RList<String> redisList = this.redisService.getRedissonClient().getList(key);
        int size = redisList.size();
        if (size == 0 || quantity > size) {
            throw new IllegalArgumentException("请求的模板数量超过可用模板总数");
        }
        Set<Integer> indexes = new HashSet<>(quantity);
        while (indexes.size() < quantity) {
            indexes.add(ThreadLocalRandom.current().nextInt(size));
        }
        List<ResourceMap> result = new ArrayList<>(quantity);
        for (Integer index : indexes) {
            String templateJson = redisList.get(index);
            result.add(JsonUtils.parse(templateJson, ResourceMap.class));
        }
        return result;
    }

    protected String joinResourceKey(String type, String tag) {
        if (tag == null) {
            tag = CommonConstant.DEFAULT_TAG;
        }
        type = type.toUpperCase();
        return RESOURCE_KEY_PREFIX + tag + ":" + type;
    }

    protected String joinTemplateKey(String type, String tag) {
        if (tag == null) {
            tag = CommonConstant.DEFAULT_TAG;
        }
        type = type.toUpperCase();
        return TEMPLATE_KEY_PREFIX + tag + ":" + type;
    }
}
