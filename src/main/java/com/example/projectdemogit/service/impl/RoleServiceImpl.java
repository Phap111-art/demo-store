package com.example.projectdemogit.service.impl;

import com.example.projectdemogit.entity.Role;
import com.example.projectdemogit.exception.CustomException;
import com.example.projectdemogit.repository.RoleRepository;
import com.example.projectdemogit.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public Set<Role> getRolesByRoleIds(Set<Integer> roleIds) {
        return roleIds.stream().map(id -> roleRepository.findById(id).orElseGet(() -> {
            throw new CustomException("id " + id + " not found in data", HttpStatus.NOT_FOUND);
        })).collect(Collectors.toSet());
    }
}
