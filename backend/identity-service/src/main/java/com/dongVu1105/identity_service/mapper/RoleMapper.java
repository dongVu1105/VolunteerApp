package com.dongVu1105.identity_service.mapper;

import com.dongVu1105.identity_service.dto.response.RoleResponse;
import com.dongVu1105.identity_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse (Role role);
}
