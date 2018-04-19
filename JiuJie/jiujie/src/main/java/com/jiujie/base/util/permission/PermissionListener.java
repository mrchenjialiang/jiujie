package com.jiujie.base.util.permission;

import java.util.List;

/**
 * Created by ChenJiaLiang on 2018/4/19.
 * Email:576507648@qq.com
 */

public interface PermissionListener {
    void onResult(Boolean isAllHas);
    void onFailPermissions(List<String> failPermissionList);
}
