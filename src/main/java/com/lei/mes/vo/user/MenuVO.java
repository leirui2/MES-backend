package com.lei.mes.vo.user;

import lombok.Data;
import java.io.Serializable;

/**
 * 菜单VO
 * @author lei
 */
@Data
public class MenuVO implements Serializable {
    private Long menuId;
    private String menuName;
    private String menuCode;
    // 1-目录 2-菜单 3-按钮
    private Integer menuType;
    private String url;
    private String icon;
    private Integer sortOrder;
    private String perms;
}
