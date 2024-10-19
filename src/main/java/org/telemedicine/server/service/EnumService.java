package org.telemedicine.server.service;

import org.springframework.stereotype.Service;
import org.telemedicine.server.enums.Role;

import java.util.Arrays;
import java.util.List;

@Service
public class EnumService {
    public List<Role> getRoles() {
        return Arrays.asList(Role.values());
    }
}
