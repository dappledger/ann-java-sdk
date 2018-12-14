package com.zhongan.sdk.abi;

import com.zhongan.sdk.abi.datatypes.Type;

import java.util.List;

/**
 * Persisted solidity event parameters.
 */
public class EventValues {
    private final List<Type> indexedValues;
    private final List<Type> nonIndexedValues;

    public EventValues(List<Type> indexedValues, List<Type> nonIndexedValues) {
        this.indexedValues = indexedValues;
        this.nonIndexedValues = nonIndexedValues;
    }

    public List<Type> getIndexedValues() {
        return indexedValues;
    }

    public List<Type> getNonIndexedValues() {
        return nonIndexedValues;
    }
}
