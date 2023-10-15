package com.fyxar.injector.mapper.mappings.impl;

import com.fyxar.injector.mapper.mappings.Mapping;
import com.fyxar.injector.mapper.mappings.MappingType;

public class FieldMapping extends Mapping {

    private final String obfOwner, deobfOwner;
    private final String obfName, deobfName;

    public FieldMapping(String obfOwner, String deobfOwner, String obfName, String deobfName) {
        super(MappingType.FIELD);
        this.obfOwner = obfOwner;
        this.deobfOwner = deobfOwner;
        this.obfName = obfName;
        this.deobfName = deobfName;
    }

    public String getDeobfName() {
        return deobfName;
    }

    public String getObfName() {
        return obfName;
    }

    public String getDeobfOwner() {
        return deobfOwner;
    }

    public String getObfOwner() {
        return obfOwner;
    }

}
