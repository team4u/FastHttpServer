package org.team4u.fhs.web.handler.resource;

/**
 * @author Jay Wu
 */
public abstract class StaticResource {

    protected StaticResourceLoader staticResourceLoader = null;
    protected String path = null;

    public StaticResource(String path, StaticResourceLoader loader) {
        this.path = path;
        this.staticResourceLoader = loader;
    }

    /**
     * 获取资源内容
     */
    public abstract byte[] getContent();

    public abstract long lastModified();

    /**
     * 检测资源是否改变
     */
    public abstract boolean isModified();

    /**
     * 得到Resource对应的ResourceLoader
     */
    public StaticResourceLoader getStaticResourceLoader() {
        return this.staticResourceLoader;
    }

    public void setStaticResourceLoader(StaticResourceLoader staticResourceLoader) {
        this.staticResourceLoader = staticResourceLoader;
    }

    public String getPath() {
        return this.path;
    }
}