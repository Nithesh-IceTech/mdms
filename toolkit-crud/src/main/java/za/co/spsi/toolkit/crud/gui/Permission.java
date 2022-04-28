package za.co.spsi.toolkit.crud.gui;

import za.co.spsi.toolkit.ano.Qualifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdb on 2016/05/06.
 */
public class Permission {
    public static final int PERMISSION_READ = 0x01;
    public static final int PERMISSION_WRITE = 0x02;
    public static final int PERMISSION_SEARCH = 0x04;
    public static final int PERMISSION_CREATE = 0x08;
    public static final int PERMISSION_DELETE = 0x10;
    public static final int PERMISSION_HOME = 0x20;
    public static final int PERMISSION_ALL = PERMISSION_READ | PERMISSION_WRITE | PERMISSION_SEARCH
            | PERMISSION_CREATE | PERMISSION_DELETE | PERMISSION_HOME;
    // by default you can not create
    private Integer permissionFlag = PERMISSION_ALL;

    private List<Permission> childPermissions = new ArrayList<>();
    private Layout layout;

    public Permission() {
    }

    public Permission(Layout layout) {
        this.layout = layout;
    }

    public void addChild(Permission permission) {
        childPermissions.add(permission);
    }


    public Permission(Integer permissionFlag) {
        this.permissionFlag = permissionFlag;
    }

    public void initFromParent(Permission parent) {
        setMayCreate(parent.mayUpdate() && mayCreate());
        setMayDelete(parent.mayUpdate() && mayDelete());
    }

    public void initFrom(Qualifier qualifier) {
        if (qualifier != null) {
            setMayCreate(ToolkitUI.mayCreate(qualifier));
            setMayUpdate(ToolkitUI.mayUpdate(qualifier));
            setMayDelete(ToolkitUI.mayDelete(qualifier));
        }
    }

    public static boolean mayUpdate(int flag) {
        return (flag & PERMISSION_WRITE) == PERMISSION_WRITE;
    }

    public static boolean mayGoHome(int flag) {
        return (flag & PERMISSION_HOME) == PERMISSION_HOME;
    }

    public static boolean mayRead(int flag) {
        return (flag & PERMISSION_READ) == PERMISSION_READ;
    }

    public static boolean mayDelete(int flag) {
        return (flag & PERMISSION_DELETE) == PERMISSION_DELETE;
    }

    public Integer getPermissionFlag() {
        return permissionFlag;
    }

    public Permission setPermissionFlag(Integer permissionFlag) {
        this.permissionFlag = permissionFlag;
        if (layout != null && layout.getToolbar() != null){
            layout.getToolbar().init(this);
        }
        // update all your kids
        for (Permission child : childPermissions) {
            child.initFromParent(this);
        }
        return this;
    }

    // update &&
    public Permission updatePermission(Permission permission) {
        setMayUpdate(mayUpdate() && permission.mayUpdate());
        setMayCreate(mayUpdate() && permission.mayCreate());
        setMayDelete(mayUpdate() && permission.mayDelete());
        return this;
    }

    public boolean mayCreate(int flag) {
        return (flag & PERMISSION_CREATE) == PERMISSION_CREATE;
    }

    public boolean mayCreate() {
        return mayCreate(permissionFlag);
    }

    public boolean mayGoHome() {
        return mayGoHome(permissionFlag);
    }

    public boolean mayUpdate() {
        return mayUpdate(permissionFlag);
    }

    public Permission setMayUpdate(boolean update) {
        return setPermissionFlag(!update?permissionFlag & ~PERMISSION_WRITE:permissionFlag | PERMISSION_WRITE);
    }

    public Permission updateMayUpdate(boolean update) {
        setMayUpdate(mayUpdate() && update);
        return this;
    }

    public Permission updateMayCreate(boolean update) {
        setMayUpdate(mayCreate() && update);
        return this;
    }

    public Permission updateMayDelete(boolean update) {
        setMayUpdate(mayDelete() && update);
        return this;
    }

    public Permission setMayCreate(boolean create) {
        return setPermissionFlag(!create?permissionFlag & ~PERMISSION_CREATE:permissionFlag | PERMISSION_CREATE);
    }

    public Permission setMayDelete(boolean delete) {
        return setPermissionFlag(permissionFlag = !delete?permissionFlag & ~PERMISSION_DELETE:permissionFlag | PERMISSION_DELETE);
    }

    public Permission setReadOnly(boolean readOnly) {
        permissionFlag = readOnly?permissionFlag & ~PERMISSION_WRITE:permissionFlag | PERMISSION_WRITE;
        permissionFlag = readOnly?permissionFlag & ~PERMISSION_CREATE:permissionFlag | PERMISSION_CREATE;
        permissionFlag = readOnly?permissionFlag & ~PERMISSION_DELETE:permissionFlag | PERMISSION_DELETE;
        return setPermissionFlag(permissionFlag);
    }

    public boolean mayRead() {
        return mayRead(permissionFlag);
    }

    public boolean mayDelete() {
        return mayDelete(permissionFlag);
    }

}
