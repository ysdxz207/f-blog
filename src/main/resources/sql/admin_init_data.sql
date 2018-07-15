

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (1, '博客管理', 'element-icons el-icon-blog', '1', null, 1, '博客管理', 'blog', 'blog_manage', 0, 0);
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (2, '权限管理', 'element-icons el-icon-permission', '2', null, 1, '权限管理', 'permission', 'role_manage', 0, 0);
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (3, '文章列表', 'element-icons el-icon-article', '3', 'article/false', 1, '文章列表', 'blog', 'article', 1, 0);
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (4, '用户信息', 'element-icons el-icon-users', '4', 'user/false', 1, '用户信息', 'permission', 'user', 2, 0);
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (5, '角色信息', 'element-icons el-icon-role', '5', 'role/false', 1, '角色信息', 'permission', 'role', 2, 0);
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (6, '菜单信息', 'element-icons el-icon-menu', '6', 'menu/false', 1, '菜单信息', 'permission', 'menu', 2, 0);
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (7, '权限信息', 'element-icons el-icon-permissions', '7', 'permission/false', 1, '权限信息', 'permission', 'permission', 2, 0);
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (8, '文章分类', 'element-icons el-icon-category', '8', 'category/false', 1, '文章分类', 'blog', 'category', 1, 0);
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (9, '文章标签', 'element-icons el-icon-tag', '9', 'tag/false', 1, '文章标签', 'blog', 'tag', 1, 0);
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (10, '阿福管理', 'element-icons el-icon-afu', '10', null, 1, '阿福管理', 'afu', 'afu_manage', 0, 0);
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (11, '阿福信息', 'element-icons el-icon-afus', '11', 'afu/false', 1, '阿福管理', 'afu', 'afu', 10, 0);
INSERT INTO menu (id, menu_name, icon, sort, href, status, remark, type, code, pid, expand) VALUES (12, '阿福类别', 'element-icons el-icon-type', '12', 'afu/type/false', 1, '阿福类别管理', 'afu', 'afu_type', 10, 0);

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', '4', '查看用户', 'user:view');
INSERT INTO `permission` VALUES ('2', '4', '添加用户', 'user:add');
INSERT INTO `permission` VALUES ('3', '4', '编辑用户', 'user:edit');
INSERT INTO `permission` VALUES ('4', '4', '删除用户', 'user:delete');
INSERT INTO `permission` VALUES ('5', '5', '查看角色', 'role:view');
INSERT INTO `permission` VALUES ('6', '5', '添加角色', 'role:add');
INSERT INTO `permission` VALUES ('7', '5', '编辑角色', 'role:edit');
INSERT INTO `permission` VALUES ('8', '5', '删除角色', 'role:delete');
INSERT INTO `permission` VALUES ('9', '5', '设置权限', 'role:setPermission');
INSERT INTO `permission` VALUES ('10', '6', '查看菜单', 'menu:view');
INSERT INTO `permission` VALUES ('11', '6', '添加菜单', 'menu:add');
INSERT INTO `permission` VALUES ('12', '6', '编辑菜单', 'menu:edit');
INSERT INTO `permission` VALUES ('13', '6', '删除菜单', 'menu:delete');
INSERT INTO `permission` VALUES ('14', '7', '查看权限', 'permission:view');
INSERT INTO `permission` VALUES ('15', '7', '添加权限', 'permission:add');
INSERT INTO `permission` VALUES ('16', '7', '编辑权限', 'permission:edit');
INSERT INTO `permission` VALUES ('17', '7', '删除权限', 'permission:delete');
INSERT INTO `permission` VALUES ('18', '3', '查看文章', 'article:view');
INSERT INTO `permission` VALUES ('19', '3', '添加文章', 'article:add');
INSERT INTO `permission` VALUES ('20', '3', '编辑文章', 'article:edit');
INSERT INTO `permission` VALUES ('21', '3', '删除文章', 'article:delete');
INSERT INTO `permission` VALUES ('22', '8', '查看分类', 'category:view');
INSERT INTO `permission` VALUES ('23', '8', '添加分类', 'category:add');
INSERT INTO `permission` VALUES ('24', '8', '编辑分类', 'category:edit');
INSERT INTO `permission` VALUES ('25', '8', '删除分类', 'category:delete');
INSERT INTO `permission` VALUES ('26', '9', '查看标签', 'tag:view');
INSERT INTO `permission` VALUES ('27', '9', '添加标签', 'tag:add');
INSERT INTO `permission` VALUES ('28', '9', '编辑标签', 'tag:edit');
INSERT INTO `permission` VALUES ('29', '9', '删除标签', 'tag:delete');
INSERT INTO `permission` VALUES ('30', '10', '查看阿福', 'afu:view');
INSERT INTO `permission` VALUES ('31', '10', '添加阿福', 'afu:add');
INSERT INTO `permission` VALUES ('32', '10', '编辑阿福', 'afu:edit');
INSERT INTO `permission` VALUES ('33', '10', '删除阿福', 'afu:delete');
INSERT INTO `permission` VALUES ('34', '11', '查看阿福类别', 'afuType:view');
INSERT INTO `permission` VALUES ('35', '11', '查看阿福类别', 'afuType:add');
INSERT INTO `permission` VALUES ('36', '11', '查看阿福类别', 'afuType:edit');
INSERT INTO `permission` VALUES ('37', '11', '查看阿福类别', 'afuType:delete');


-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '管理员', 'admin');




-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ("20151106", "feihong", "飞鸿", "57BA5C80AFEE75C5", "1502610564000", null, 1);


-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '20151106', '1');
