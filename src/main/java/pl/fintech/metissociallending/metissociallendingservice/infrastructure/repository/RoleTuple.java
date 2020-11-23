package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import pl.fintech.metissociallending.metissociallendingservice.domain.user.Role;


public enum RoleTuple{
    ROLE_ADMIN("ROLE_ADMIN"), ROLE_CLIENT("ROLE_CLIENT");

    public final String role;
    RoleTuple(String role) {
        this.role = role;
    }

    static RoleTuple from(Role role){
        return RoleTuple.valueOf(role.getAuthority());
    }

    Role toDomain(){
        return Role.valueOf(this.toString());
    }

}
