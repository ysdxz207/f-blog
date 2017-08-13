

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '新闻管理', null, '1', null, '1', '新闻管理', '2', 'news_manage', '0', '0');
INSERT INTO `menu` VALUES ('2', '权限管理', null, '2', null, '1', '权限管理', '1', 'role_manage', '0', '0');
INSERT INTO `menu` VALUES ('3', '新闻信息', null, '3', 'news/false', '1', '新闻信息', '2', 'news', '1', '0');
INSERT INTO `menu` VALUES ('4', '用户信息', null, '4', 'user/false', '1', '用户信息', '1', 'user', '2', '0');
INSERT INTO `menu` VALUES ('5', '角色信息', null, '5', 'role/false', '1', '角色信息', '1', 'role', '2', '0');
INSERT INTO `menu` VALUES ('6', '菜单信息', null, '6', 'menu/false', '1', '菜单信息', '1', 'menu', '2', '0');
INSERT INTO `menu` VALUES ('7', '权限信息', null, '7', 'permission/false', '1', '权限信息', '1', 'permission', '2', '0');
INSERT INTO `menu` VALUES ('8', '新闻频道', null, '8', 'news/channel/false', '1', '新闻频道', '2', 'news_channel', '1', '0');
INSERT INTO `menu` VALUES ('9', '字典管理', null, '9', null, '1', '字典管理', '3', 'dictionary_manage', '0', '0');
INSERT INTO `menu` VALUES ('10', '字典信息', null, '10', 'dictionary/false', '1', '字典信息', '3', 'dictionary', '9', '0');


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
INSERT INTO `permission` VALUES ('18', '3', '查看新闻', 'news:view');
INSERT INTO `permission` VALUES ('19', '3', '添加新闻', 'news:add');
INSERT INTO `permission` VALUES ('20', '3', '编辑新闻', 'news:edit');
INSERT INTO `permission` VALUES ('21', '3', '删除新闻', 'news:delete');
INSERT INTO `permission` VALUES ('22', '8', '查看新闻频道', 'news:channel:view');
INSERT INTO `permission` VALUES ('23', '8', '添加新闻频道', 'news:channel:add');
INSERT INTO `permission` VALUES ('24', '8', '编辑新闻频道', 'news:channel:edit');
INSERT INTO `permission` VALUES ('25', '8', '删除新闻频道', 'news:channel:delete');
INSERT INTO `permission` VALUES ('26', '10', '查看字典', 'dictionary:view');
INSERT INTO `permission` VALUES ('27', '10', '添加字典', 'dictionary:add');
INSERT INTO `permission` VALUES ('28', '10', '编辑字典', 'dictionary:edit');
INSERT INTO `permission` VALUES ('29', '10', '删除字典', 'dictionary:delete');



-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '管理员', 'admin');



-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('1', '20151106', '1');
INSERT INTO `role_permission` VALUES ('2', '20151106', '2');
INSERT INTO `role_permission` VALUES ('3', '20151106', '3');
INSERT INTO `role_permission` VALUES ('4', '20151106', '4');
INSERT INTO `role_permission` VALUES ('5', '20151106', '5');
INSERT INTO `role_permission` VALUES ('6', '20151106', '6');
INSERT INTO `role_permission` VALUES ('7', '20151106', '7');
INSERT INTO `role_permission` VALUES ('8', '20151106', '8');
INSERT INTO `role_permission` VALUES ('9', '20151106', '9');
INSERT INTO `role_permission` VALUES ('10', '20151106', '10');
INSERT INTO `role_permission` VALUES ('11', '20151106', '11');
INSERT INTO `role_permission` VALUES ('12', '20151106', '12');
INSERT INTO `role_permission` VALUES ('13', '20151106', '13');
INSERT INTO `role_permission` VALUES ('14', '20151106', '14');
INSERT INTO `role_permission` VALUES ('15', '20151106', '15');
INSERT INTO `role_permission` VALUES ('16', '20151106', '16');
INSERT INTO `role_permission` VALUES ('17', '20151106', '17');
INSERT INTO `role_permission` VALUES ('18', '20151106', '18');
INSERT INTO `role_permission` VALUES ('19', '20151106', '19');
INSERT INTO `role_permission` VALUES ('20', '20151106', '20');
INSERT INTO `role_permission` VALUES ('21', '20151106', '21');
INSERT INTO `role_permission` VALUES ('22', '20151106', '22');
INSERT INTO `role_permission` VALUES ('23', '20151106', '23');
INSERT INTO `role_permission` VALUES ('24', '20151106', '24');
INSERT INTO `role_permission` VALUES ('25', '20151106', '25');
INSERT INTO `role_permission` VALUES ('26', '20151106', '26');
INSERT INTO `role_permission` VALUES ('27', '20151106', '27');
INSERT INTO `role_permission` VALUES ('28', '20151106', '28');
INSERT INTO `role_permission` VALUES ('29', '20151106', '29');



-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ("20151106", "feihong", "飞鸿", "31B8F3F489C3DB90CB6DCBCA3B7B3F50", "1502610564000", null, 1);


-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('1', '20151106', '1');
