

-- ----------------------------
-- Records of menu
-- ----------------------------
UPDATE menu SET menu_name = '博客管理', icon = 'element-icons el-icon-blog', sort = '1', href = null, status = 1, remark = '博客管理', type = 'blog', code = 'blog_manage', pid = 0, expand = 0 WHERE id = 1;
UPDATE menu SET menu_name = '权限管理', icon = 'element-icons el-icon-permission', sort = '2', href = null, status = 1, remark = '权限管理', type = 'permission', code = 'role_manage', pid = 0, expand = 0 WHERE id = 2;
UPDATE menu SET menu_name = '文章列表', icon = 'element-icons el-icon-article', sort = '3', href = '/blog/article', status = 1, remark = '文章列表', type = 'blog', code = 'article', pid = 1, expand = 0 WHERE id = 3;
UPDATE menu SET menu_name = '用户信息', icon = 'element-icons el-icon-users', sort = '4', href = '/permission/user', status = 1, remark = '用户信息', type = 'permission', code = 'user', pid = 2, expand = 0 WHERE id = 4;
UPDATE menu SET menu_name = '角色信息', icon = 'element-icons el-icon-role', sort = '5', href = '/permission/role', status = 1, remark = '角色信息', type = 'permission', code = 'role', pid = 2, expand = 0 WHERE id = 5;
UPDATE menu SET menu_name = '菜单信息', icon = 'element-icons el-icon-menu', sort = '6', href = '/permission/menu', status = 1, remark = '菜单信息', type = 'permission', code = 'menu', pid = 2, expand = 0 WHERE id = 6;
UPDATE menu SET menu_name = '权限信息', icon = 'element-icons el-icon-permissions', sort = '7', href = '/permission/permisson', status = 1, remark = '权限信息', type = 'permission', code = 'permission', pid = 2, expand = 0 WHERE id = 7;
UPDATE menu SET menu_name = '文章分类', icon = 'element-icons el-icon-category', sort = '8', href = '/blog/category', status = 1, remark = '文章分类', type = 'blog', code = 'category', pid = 1, expand = 0 WHERE id = 8;
UPDATE menu SET menu_name = '文章标签', icon = 'element-icons el-icon-tag', sort = '9', href = '/blog/tag', status = 1, remark = '文章标签', type = 'blog', code = 'tag', pid = 1, expand = 0 WHERE id = 9;
UPDATE menu SET menu_name = '阿福管理', icon = 'element-icons el-icon-afu', sort = '10', href = null, status = 1, remark = '阿福管理', type = 'afu', code = 'afu_manage', pid = 0, expand = 0 WHERE id = 10;
UPDATE menu SET menu_name = '阿福信息', icon = 'element-icons el-icon-afus', sort = '11', href = 'afu/afu', status = 1, remark = '阿福管理', type = 'afu', code = 'afu', pid = 10, expand = 0 WHERE id = 11;
UPDATE menu SET menu_name = '阿福类别', icon = 'element-icons el-icon-type', sort = '12', href = 'afu/type', status = 1, remark = '阿福类别管理', type = 'afu', code = 'afu_type', pid = 10, expand = 0 WHERE id = 12;
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
