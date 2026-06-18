package com.lei.mes.vo.user;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 菜单树VO
 * @author lei
 */
@Data
public class MenuTreeVO implements Serializable {
    private Long id;
    private Long parentId;
    private String menuName;
    private String menuCode;
    private Integer menuType;
    private String url;
    private String icon;
    private Integer sortOrder;
    private String perms;
    private Integer status;

    // 子菜单列表
    private List<MenuTreeVO> children;

    // 是否被选中（用于角色分配菜单时）
    private Boolean checked;
}
