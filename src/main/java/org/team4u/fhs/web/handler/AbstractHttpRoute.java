package org.team4u.fhs.web.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.fhs.server.HttpMethod;
import org.team4u.fhs.server.HttpServerRequest;
import org.team4u.fhs.web.HttpRoute;
import org.team4u.fhs.web.util.PathUtil;
import org.team4u.kit.core.action.Function;
import org.team4u.kit.core.util.CollectionExUtil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jay Wu
 */
public abstract class AbstractHttpRoute implements HttpRoute {

    protected static final String PARAM_VALUE_REGEX = "(.+)[/]*";
    protected static final Pattern PARAM_NAME_PATTERN = Pattern.compile(":([_a-z0-9A-Z]*)", Pattern.CASE_INSENSITIVE);
    protected static final Pattern COMMA_SPLITTER = Pattern.compile(" *, *");
    protected static final Pattern SEMICOLON_SPLITTER = Pattern.compile(" *; *");
    protected static final Pattern EQUAL_SPLITTER = Pattern.compile(" *= *");
    private static final Log log = LogFactory.get();
    protected final Set<HttpMethod> httpMethods = new HashSet<HttpMethod>();
    protected final Set<String> consumes = new LinkedHashSet<String>();
    protected final Set<String> produces = new LinkedHashSet<String>();
    protected String path;
    protected boolean enabled = true;
    protected Pattern paramValuePattern;
    protected List<String> parameterNames;
    protected boolean exactPath;
    protected Comparator<String> ACCEPT_X_COMPARATOR = new Comparator<String>() {
        float getQuality(String s) {
            if (s == null) {
                return 0;
            }

            String[] params = SEMICOLON_SPLITTER.split(s);
            for (int i = 1; i < params.length; i++) {
                String[] q = EQUAL_SPLITTER.split(params[1]);
                if ("q".equals(q[0])) {
                    return Float.parseFloat(q[1]);
                }
            }
            return 1;
        }

        @Override
        public int compare(String o1, String o2) {
            float f1 = getQuality(o1);
            float f2 = getQuality(o2);
            if (f1 < f2) {
                return 1;
            }
            if (f1 > f2) {
                return -1;
            }
            return 0;
        }
    };

    public AbstractHttpRoute() {
    }

    public AbstractHttpRoute(HttpMethod method, String path) {
        httpMethods.add(method);
        setPath(path);
    }

    public AbstractHttpRoute(String path) {
        setPath(path);
    }

    @Override
    public synchronized HttpRoute httpMethod(HttpMethod method) {
        httpMethods.add(method);
        return this;
    }

    @Override
    public synchronized HttpRoute path(String path) {
        setPath(path);
        return this;
    }

    @Override
    public synchronized HttpRoute pathRegex(String regex) {
        setRegex(regex);
        return this;
    }

    @Override
    public synchronized HttpRoute produces(String contentType) {
        produces.add(contentType);
        return this;
    }

    @Override
    public synchronized HttpRoute consumes(String contentType) {
        consumes.add(contentType);
        return this;
    }

    @Override
    public synchronized HttpRoute disable() {
        enabled = false;
        return this;
    }

    @Override
    public synchronized HttpRoute enable() {
        enabled = true;
        return this;
    }

    @Override
    public String getPath() {
        return path;
    }

    private void setPath(String path) {
        Assert.notEmpty(path, "Path must start with " + PathUtil.PATH_PREFIX);

        // See if the path contains ":" - if so then it contains parameter capture groups and we have to generate
        // a regex for that
        if (path.contains(":")) {
            createPatternRegex(path);
            this.path = path;
        } else {
            if (!path.endsWith("*")) {
                exactPath = true;
                this.path = PathUtil.normalisePath(path);
            } else {
                exactPath = false;
                this.path = path.substring(0, path.length() - 1);
            }
        }
    }

    @Override
    public boolean matches(HttpServerRequest request) {
        if (!enabled || path == null) {
            return false;
        }

        if (!httpMethods.isEmpty() &&
                (request.getMethod() == null || !httpMethods.contains(HttpMethod.valueOf(request.getMethod())))) {
            return false;
        }

        String requestPath = PathUtil.normalisePath(request.getPath());
        if (paramValuePattern == null) {
            if (!pathMatches(requestPath)) {
                return false;
            }
        } else {
            if (!pathPatternMatches(request, requestPath)) {
                return false;
            }
        }

        if (!checkConsumes(request.getContentType())) {
            return false;
        }

        if (!checkProduces(request.getHeader("accept"))) {
            return false;
        }

        return true;
    }

    private boolean checkConsumes(final String contentType) {
        return CollectionExUtil.all(consumes, new Function<String, Boolean>() {
            @Override
            public Boolean invoke(String consume) {
                return ctMatches(contentType, consume);
            }
        });
    }

    private boolean checkProduces(String accept) {
        if (produces.isEmpty()) {
            return true;
        }

        if (accept != null) {
            List<String> acceptableTypes = getSortedAcceptableMimeTypes(accept);
            for (String acceptable : acceptableTypes) {
                for (String produce : produces) {
                    if (ctMatches(produce, acceptable)) {
                        return true;
                    }
                }
            }
        } else {
            // According to rfc2616-sec14,
            // If no Accept header field is present, then it is assumed that the client accepts all media types.
            return true;
        }

        return false;
    }

    /**
     * E.g.
     * "text/html", "text/*"  - returns true
     * "text/html", "html" - returns true
     * "application/json", "json" - returns true
     * "application/*", "json" - returns true
     */
    //TODO - don't parse consumes types on each request - they can be preparsed!
    private boolean ctMatches(String actualCT, String allowsCT) {
        if (allowsCT.equals("*") || allowsCT.equals("*/*")) {
            return true;
        }

        if (actualCT == null) {
            return false;
        }

        // get the content type only (exclude charset)
        actualCT = actualCT.split(";")[0];

        // if we received an incomplete CT
        if (allowsCT.indexOf('/') == -1) {
            // when the content is incomplete we assume */type, e.g.:
            // json -> */json
            allowsCT = "*/" + allowsCT;
        }

        // process wildcards
        if (allowsCT.contains("*")) {
            String[] consumesParts = allowsCT.split(PathUtil.PATH_PREFIX);
            String[] requestParts = actualCT.split(PathUtil.PATH_PREFIX);
            return "*".equals(consumesParts[0]) && consumesParts[1].equals(requestParts[1]) ||
                    "*".equals(consumesParts[1]) && consumesParts[0].equals(requestParts[0]);
        }

        return actualCT.contains(allowsCT);
    }

    private boolean pathMatches(String requestPath) {
        if (exactPath) {
            return StrUtil.equals(requestPath, path);
        } else {
            return requestPath.startsWith(path);
        }
    }

    private boolean pathPatternMatches(HttpServerRequest request, String requestPath) {
        Matcher m = paramValuePattern.matcher(requestPath);
        if (!m.matches()) {
            return false;
        }

        if (m.groupCount() == parameterNames.size()) {
            for (int i = 0; i < parameterNames.size(); i++) {
                String name = parameterNames.get(i);
                String value = m.group(i + 1);
                request.getParameterMap().put(name, CollUtil.newArrayList(value));
            }
        } else {
            return false;
        }

        return true;
    }

    private void setRegex(String regex) {
        paramValuePattern = Pattern.compile(regex);
    }

    private void createPatternRegex(String path) {
        // We need to search for any :<token name> tokens in the String and replace them with named capture groups
        Matcher m = PARAM_NAME_PATTERN.matcher(path);
        StringBuffer sb = new StringBuffer();
        parameterNames = new ArrayList<String>();
        while (m.find()) {
            String group = m.group().substring(1);
            if (parameterNames.contains(group)) {
                throw new IllegalArgumentException("Cannot use identifier " + group + " more than once in pattern string");
            }
            m.appendReplacement(sb, PARAM_VALUE_REGEX);
            parameterNames.add(group);
        }
        m.appendTail(sb);
        path = sb.toString();
        paramValuePattern = Pattern.compile(path);
    }

    protected List<String> getSortedAcceptableMimeTypes(String acceptHeader) {
        // accept anything when accept is not present
        if (acceptHeader == null) {
            return Collections.emptyList();
        }

        // parse
        String[] items = COMMA_SPLITTER.split(acceptHeader);
        // sort on quality
        Arrays.sort(items, ACCEPT_X_COMPARATOR);

        List<String> list = new ArrayList<String>(items.length);

        for (String item : items) {
            // find any ; e.g.: "application/json;q=0.8"
            int space = item.indexOf(';');

            if (space != -1) {
                list.add(item.substring(0, space));
            } else {
                list.add(item);
            }
        }

        return list;
    }

    @Override
    public String toString() {
        return "HttpRoute{" +
                "methods=" + httpMethods +
                ", consumes=" + consumes +
                ", produces=" + produces +
                ", path='" + path + '\'' +
                ", enabled=" + enabled +
                ", actualParamPattern=" + paramValuePattern +
                ", parameterNames=" + parameterNames +
                ", exactPath=" + exactPath +
                '}';
    }
}