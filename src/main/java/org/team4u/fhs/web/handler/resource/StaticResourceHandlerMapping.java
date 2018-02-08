package org.team4u.fhs.web.handler.resource;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.web.handler.AbstractHandlerMapping;
import org.team4u.fhs.web.util.PathUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class StaticResourceHandlerMapping extends AbstractHandlerMapping {

    private final String baseDir;
    private final String mappingPath;
    private Map<String, StaticResource> resources = new HashMap<String, StaticResource>();
    private StaticResourceLoader staticResourceLoader;

    public StaticResourceHandlerMapping(String baseDir, String mappingPath) {
        this(new ClasspathResourceLoader(), baseDir, mappingPath);
    }

    public StaticResourceHandlerMapping(StaticResourceLoader staticResourceLoader,
                                        String baseDir,
                                        String mappingPath) {
        this.staticResourceLoader = staticResourceLoader;
        this.baseDir = baseDir;
        Assert.notNull(mappingPath, "mappingPath can't be null");

        if (StrUtil.isEmpty(mappingPath)) {
            mappingPath = PathUtil.PATH_PREFIX;
        }

        if (!mappingPath.startsWith(PathUtil.PATH_PREFIX)) {
            mappingPath = PathUtil.PATH_PREFIX + mappingPath;
        }

        this.mappingPath = PathUtil.endWithPathPrefix(mappingPath);
    }

    @Override
    protected Object getHandlerInternal(HttpServerRequest request) throws Exception {
        String requestPath = request.getPath();

        if (!requestPath.startsWith(mappingPath)) {
            return null;
        }

        requestPath = requestPath.substring(mappingPath.length() - 1);

        return getResource(baseDir + requestPath);
    }

    private Object getResource(String path) {
        StaticResource resource = resources.get(path);
        if (resource != null) {
            return resource;
        }

        // 只保留存在的资源，防止大量无用资源占用缓存
        resource = staticResourceLoader.getResource(path);
        if (resource == null) {
            return null;
        }

        resources.put(resource.getPath(), resource);

        return resource;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public String getMappingPath() {
        return mappingPath;
    }
}