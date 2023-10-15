package com.fyxar.injector.mapper.mappings.impl;

import com.fyxar.injector.mapper.mappings.Mapping;
import com.fyxar.injector.mapper.mappings.MappingType;

public class ClassMapping extends Mapping {

    private final String obfName, deobfName;

    public ClassMapping(String obfName, String deobfName) {
        super(MappingType.CLASS);
        this.obfName = obfName;
        this.deobfName = deobfName;
    }

    public String getDeobfName() {
        return deobfName;
    }

    public String getObfName() {
        return obfName;
    }

}
