package com.example.projectdemogit.service;

import com.example.projectdemogit.entity.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> getRolesByRoleIds(Set<Integer> roleIds);
}
