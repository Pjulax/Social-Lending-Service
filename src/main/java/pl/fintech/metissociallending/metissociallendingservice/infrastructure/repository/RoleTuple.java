package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import pl.fintech.metissociallending.metissociallendingservice.domain.user.Role;


public enum RoleTuple{
    ROLE_ADMIN, ROLE_CLIENT;

    static RoleTuple from(Role role){
        return valueOf(role.toString());
    }

    Role toDomain(){
        return Role.valueOf(this.toString());
    }

}
