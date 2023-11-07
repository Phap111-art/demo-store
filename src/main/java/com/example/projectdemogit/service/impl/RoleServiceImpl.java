package com.example.projectdemogit.service.impl;

import com.example.projectdemogit.entity.Role;
import com.example.projectdemogit.repository.RoleRepository;
import com.example.projectdemogit.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Set<Role> getRolesByRoleIds(Set<Integer> roleIds) {
        return roleIds.stream().map(id -> roleRepository.findById(id).orElseGet(() -> {
            throw new RuntimeException("id " + id + " not found in data");
        })).collect(Collectors.toSet());
    }
}
