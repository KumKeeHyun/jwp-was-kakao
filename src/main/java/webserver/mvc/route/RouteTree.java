package webserver.mvc.route;

import webserver.mvc.Handler;

import java.util.*;

public class RouteTree {

    private final TreeNodeType nodeType;
    private final String path;
    private final String fullPath;
    private Handler handler;
    private final List<RouteTree> children = new ArrayList<>();

    public RouteTree(TreeNodeType nodeType, String path, String fullPath) {
        this(nodeType, path, fullPath, null);
    }

    public RouteTree(TreeNodeType nodeType, String path, String fullPath, Handler handler) {
        this.nodeType = nodeType;
        this.path = path;
        this.fullPath = fullPath;
        this.handler = handler;
    }

    public void addRoute(String path, Handler handler) {
        if (path.isEmpty()) {
            if (this.handler != null) {
                throw new IllegalArgumentException("duplicate routing rule");
            }
            this.handler = handler;
            return;
        }
        if (nodeType == TreeNodeType.CATCH_ALL) {
            throw new IllegalArgumentException("catch all node cannot add Child");
        }

        String curPath = path;
        String nextPath = "";
        int curPathEnd = path.indexOf("/", path.indexOf("/") + 1);
        if (curPathEnd != -1) {
            curPath = path.substring(0, curPathEnd);
            nextPath = path.substring(curPathEnd);
        }

        for (RouteTree child: this.children){
            if (child.match(curPath)) {
                child.addRoute(nextPath, handler);
                return;
            }
        }

        RouteTree newChild = new RouteTree(TreeNodeType.of(curPath), curPath, this.fullPath+curPath);
        if (newChild.nodeType == TreeNodeType.CATCH_ALL && !children.isEmpty()) {
            throw new IllegalArgumentException("non empty node cannot add catch all");
        }
        newChild.addRoute(nextPath, handler);
        this.children.add(newChild);
    }

    private boolean match(String curPath) {
        if (nodeType == TreeNodeType.PARAM || nodeType == TreeNodeType.CATCH_ALL) {
            return true;
        }
        if (nodeType == TreeNodeType.STATIC) {
            return this.path.equals(curPath);
        }
        return false;
    }

    public Optional<RouteValue> findRoute(String path) {
        return findRoute(path, new HashMap<>());
    }

    private Optional<RouteValue> findRoute(String path, Map<String, String> pathVariables) {
        if (path.isEmpty()) {
            if (this.handler == null) {
                return Optional.empty();
            }
            return Optional.of(new RouteValue(this.handler, pathVariables));
        }

        String curPath = path;
        String nextPath = "";
        int curPathEnd = path.indexOf("/", path.indexOf("/") + 1);
        if (curPathEnd != -1) {
            curPath = path.substring(0, curPathEnd);
            nextPath = path.substring(curPathEnd);
        }

        for (RouteTree child: this.children){
            if (child.match(curPath)) {
                if (child.nodeType == TreeNodeType.CATCH_ALL) {
                    return Optional.of(new RouteValue(child.handler, pathVariables));
                }
                if (child.nodeType == TreeNodeType.PARAM) {
                    pathVariables.put(child.path.substring(2), curPath.substring(1));
                }
                return child.findRoute(nextPath, pathVariables);
            }
        }
        return Optional.empty();
    }
}
