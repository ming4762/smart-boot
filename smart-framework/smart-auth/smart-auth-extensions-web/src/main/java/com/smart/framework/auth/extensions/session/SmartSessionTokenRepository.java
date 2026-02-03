package com.smart.framework.auth.extensions.session;

import com.google.common.collect.Lists;
import com.smart.framework.auth.common.userdetails.RestUserDetails;
import com.smart.framework.auth.common.utils.AuthUtils;
import com.smart.framework.auth.core.service.AbstractAuthCache;
import com.smart.framework.auth.core.service.AuthCache;
import com.smart.framework.auth.core.token.SmartTokenRepository;
import com.smart.framework.auth.core.token.TokenCacheData;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.session.*;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * token session存储器
 * @author shizhongming
 * 2025/3/13 17:15
 * @since 5.0.0
 */
public class SmartSessionTokenRepository implements SmartTokenRepository, SessionIdGenerator, SessionRepository<SmartSessionTokenRepository.SmartSession> {

    private static final String TOKE_KEY_PREFIX = "smart-session";

    private FlushMode flushMode = FlushMode.ON_SAVE;
    private SaveMode saveMode = SaveMode.ON_SET_ATTRIBUTE;
    private Duration defaultMaxInactiveInterval = Duration.ofSeconds(MapSession.DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS);

    private final AuthCache authCache;

    public SmartSessionTokenRepository(AuthCache authCache) {
        this.authCache = authCache;
    }

    /**
     * Set the flush mode.
     * @param flushMode the flush mode
     */
    public void setFlushMode(FlushMode flushMode) {
        Assert.notNull(flushMode, "flushMode must not be null");
        this.flushMode = flushMode;
    }

    /**
     * Set the save mode.
     * @param saveMode the save mode
     */
    public void setSaveMode(SaveMode saveMode) {
        Assert.notNull(saveMode, "saveMode must not be null");
        this.saveMode = saveMode;
    }

    /**
     * Set the maximum inactive interval in seconds between requests before newly created
     * sessions will be invalidated. A negative time indicates that the session will never
     * time out. The default is 30 minutes.
     * @param defaultMaxInactiveInterval the default maxInactiveInterval
     */
    public void setDefaultMaxInactiveInterval(Duration defaultMaxInactiveInterval) {
        Assert.notNull(defaultMaxInactiveInterval, "defaultMaxInactiveInterval must not be null");
        this.defaultMaxInactiveInterval = defaultMaxInactiveInterval;
    }

        /**
         * 生成token
         *
         * @return token
         */
    @Override
    public String generateToken() {
        RestUserDetails currentUser = AuthUtils.getCurrentUser();
        if (currentUser == null) {
            return UUID.randomUUID().toString();
        }
        String token = Stream.of(TOKE_KEY_PREFIX, currentUser.getUsername(), currentUser.getUserTenant().getTenantId(), UUID.randomUUID())
                .map(Object::toString)
                .collect(Collectors.joining(AbstractAuthCache.SPLIT));
        currentUser.setToken(token);
        return token;
    }

    /**
     * 查询所有数据
     *
     * @return jwt数据
     */
    @Override
    @NonNull
    public List<TokenCacheData> listToken() {
        return this.listToken(this.getCacheKey(null, null, null));
    }

    /**
     * 通过用户名查询token
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return token
     */
    @NonNull
    @Override
    public List<TokenCacheData> listToken(String username, Long tenantId) {
        return this.listToken(this.getCacheKey(username, tenantId, null));
    }

    private List<TokenCacheData> listToken(String cacheKey) {
        Set<String> keys = this.authCache.matchKeys(cacheKey);
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        return this.authCache.batchGet(keys).stream()
                .map(TokenCacheData::createFormCache)
                .filter(item -> !item.isExpired())
                .toList();
    }

    /**
     * 获取用户缓存数据
     *
     * @param attributeName 属性名称
     * @return 属性值
     */
    @Override
    public <T> T getAttribute(String attributeName) {
        HttpSession session = Objects.requireNonNull(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())).getRequest().getSession();
        return (T) session.getAttribute(attributeName);
    }

    /**
     * 设置用户缓存数据
     *
     * @param attributeName  属性名称
     * @param attributeValue 属性值
     */
    @Override
    public boolean setAttribute(String attributeName, Object attributeValue) {
        HttpSession session = Objects.requireNonNull(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())).getRequest().getSession();
        session.setAttribute(attributeName, attributeValue);
        return true;
    }

    /**
     * 使token失效
     *
     * @param token token
     * @return 是否失效成功
     */
    @Override
    public boolean invalidateByToken(String token) {
        this.authCache.remove(token);
        return true;
    }

    /**
     * 使用户登录失效
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return 是否失效成功
     */
    @Override
    public boolean invalidateByUsername(Long tenantId, String username) {
        String cacheKey = this.getCacheKey(username, tenantId, null);
        this.authCache.matchRemove(cacheKey);
        return true;
    }

    /**
     * 通过token获取用户信息
     *
     * @param token token
     * @return 用户信息
     */
    @Override
    public RestUserDetails getUserByToken(String token) {
        Map<String, Object> cacheData = this.authCache.get(token);
        if (cacheData == null) {
            return null;
        }
        TokenCacheData tokenCacheData = TokenCacheData.createFormCache(cacheData);
        return tokenCacheData.getUser();
    }

    @Override
    @NonNull
    public String generate() {
        return this.generateToken();
    }

    @Override
    public SmartSession createSession() {
        TokenCacheData cacheData = new TokenCacheData();
        cacheData.setTimeout(this.defaultMaxInactiveInterval);
        cacheData.setToken(this.generate());

        SmartSession smartSession = new SmartSession(cacheData, true);
        smartSession.flushIfRequired();
        return smartSession;
    }

    @Override
    public void save(SmartSession session) {
        if (session.isNew) {
            session.save();
            return;
        }
        String cachedKey = session.hasChangedSessionId() ? session.originalSessionId : session.getId();
        boolean sessionExists = this.authCache.hasKey(cachedKey);
        if (sessionExists) {
            session.save();
        }
    }

    @Override
    public SmartSession findById(String sessionId) {
        Map<String, Object> cacheData = this.authCache.get(sessionId);
        if (CollectionUtils.isEmpty(cacheData)) {
            return null;
        }
        TokenCacheData tokenCacheData = TokenCacheData.createFormCache(cacheData);
        if (tokenCacheData.isExpired()) {
            this.deleteById(sessionId);
            return null;
        }
        return new SmartSession(tokenCacheData, false);
    }

    @Override
    public void deleteById(String id) {
        this.authCache.remove(id);
    }

    private String getCacheKey(String username, Long tenantId, String token) {
        return Lists.newArrayList(TOKE_KEY_PREFIX, username, tenantId, token)
                .stream().filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.joining(AbstractAuthCache.SPLIT));
    }

    public final class SmartSession implements Session {

        private final TokenCacheData cacheData;
        private boolean isNew;
        private String originalSessionId;
        private final Map<String, Object> delta = new HashMap<>();

        SmartSession(TokenCacheData cacheData, boolean isNew) {
            this.cacheData = cacheData;
            this.isNew = isNew;
            this.originalSessionId = cacheData.getToken();
            if (this.isNew) {
                this.delta.putAll(this.cacheData.convertToMap());
            }
            if (this.isNew || SmartSessionTokenRepository.this.saveMode == SaveMode.ALWAYS) {
                this.delta.putAll(this.cacheData.convertAttributesToMap());
            }
        }

        @Override
        public String getId() {
            return this.cacheData.getToken();
        }

        @Override
        public String changeSessionId() {
            String newSessionId = SmartSessionTokenRepository.this.generate();
            this.cacheData.setToken(newSessionId);
            this.delta.put(TokenCacheData.TOKEN_KEY, newSessionId);
            return newSessionId;
        }

        @Override
        public <T> T getAttribute(String attributeName) {
            T attributeValue = (T) this.cacheData.getAttributes().get(attributeName);
            if (attributeValue != null && SmartSessionTokenRepository.this.saveMode.equals(SaveMode.ON_GET_ATTRIBUTE)) {
                this.delta.put(TokenCacheData.getAttributeKey(attributeName), attributeValue);
            }
            return attributeValue;
        }

        @Override
        public Set<String> getAttributeNames() {
            return this.cacheData.getAttributes().keySet();
        }

        @Override
        public void setAttribute(String attributeName, Object attributeValue) {
            this.cacheData.getAttributes().put(attributeName, attributeValue);
            this.delta.put(TokenCacheData.getAttributeKey(attributeName), attributeValue);
            flushIfRequired();
        }

        @Override
        public void removeAttribute(String attributeName) {
            setAttribute(attributeName, null);
        }

        @Override
        public Instant getCreationTime() {
            return this.cacheData.getCreateTime();
        }

        @Override
        public void setLastAccessedTime(Instant lastAccessedTime) {
            this.cacheData.setRefreshTime(lastAccessedTime);
            this.delta.put(TokenCacheData.REFRESH_TIME_KEY, getLastAccessedTime());
            flushIfRequired();
        }

        @Override
        public Instant getLastAccessedTime() {
            return this.cacheData.getRefreshTime();
        }

        @Override
        public void setMaxInactiveInterval(Duration interval) {
            this.cacheData.setTimeout(interval);
            this.delta.put(TokenCacheData.TIMEOUT_KEY, getMaxInactiveInterval());
        }

        @Override
        public Duration getMaxInactiveInterval() {
            return this.cacheData.getTimeout();
        }

        @Override
        public boolean isExpired() {
            return this.cacheData.isExpired();
        }

        private void flushIfRequired() {
            if (SmartSessionTokenRepository.this.flushMode == FlushMode.IMMEDIATE) {
                save();
            }
        }

        private void save() {
            saveChangeSessionId();
            saveDelta();
            if (this.isNew) {
                this.isNew = false;
            }
        }

        private boolean hasChangedSessionId() {
            return !getId().equals(this.originalSessionId);
        }
        private void saveChangeSessionId() {
            if (this.hasChangedSessionId()) {
                if (!this.isNew) {
                    String originalSessionIdKey = this.originalSessionId;
                    String sessionIdKey = getId();

                    SmartSessionTokenRepository.this.authCache.rename(originalSessionIdKey, sessionIdKey);
                }
                this.originalSessionId = getId();
            }
        }

        private void saveDelta() {
            if (this.delta.isEmpty()) {
                return;
            }
            String sessionIdKey = getId();
            SmartSessionTokenRepository.this.authCache.putAll(sessionIdKey, new HashMap<>(this.delta), this.cacheData.getTimeout());
            this.delta.clear();
        }
    }

}
