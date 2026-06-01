package com.KamyEsm.AAA.Enum;

public enum InitialPermission {
    //user
    CREATE_USER,
    UPDATE_USER,
    DELETE_USER,
    READ_USER,
    //role
    CREATE_ROLE,
    UPDATE_ROLE,
    DELETE_ROLE,
    READ_ROLE,
    //role management
    ADD_ROLE_MANAGEMENT,
    DELETE_ROLE_MANAGEMENT,
    //permission
    CREATE_PERMISSION,
    UPDATE_PERMISSION,
    DELETE_PERMISSION,
    READ_PERMISSION,

    //permission management
    ADD_PERMISSION_MANAGEMENT,
    DELETE_PERMISSION_MANAGEMENT,

    //scopes
    openid,
    profile
}
