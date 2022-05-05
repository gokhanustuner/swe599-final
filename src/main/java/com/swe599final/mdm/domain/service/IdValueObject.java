package com.swe599final.mdm.domain.service;

import java.io.Serializable;

public interface IdValueObject<T> extends Serializable {
    abstract public T value();
}
