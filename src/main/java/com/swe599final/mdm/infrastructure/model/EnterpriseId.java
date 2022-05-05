package com.swe599final.mdm.infrastructure.model;

import com.swe599final.mdm.domain.service.IdValueObject;

public final class EnterpriseId implements IdValueObject<Long> {
    private final Long id;

    public EnterpriseId(Long id) {
        this.id = id;
    }

    public String toString() {
        return String.valueOf(id);
    }

    public Long value() {
        return id;
    }

    public static EnterpriseId create(Long id) {
        return new EnterpriseId(id);
    }
}
