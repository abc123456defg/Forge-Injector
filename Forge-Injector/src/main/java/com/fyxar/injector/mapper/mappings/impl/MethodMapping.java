package com.fyxar.injector.mapper.mappings.impl;

import com.fyxar.injector.mapper.mappings.Mapping;
import com.fyxar.injector.mapper.mappings.MappingType;

public class MethodMapping extends Mapping {

    private final String obfOwner, deobfOwner;
    private final String obfName, deobfName;

    public MethodMapping(String obfOwner, String deobfOwner, String obfName, String deobfName) {
        super(MappingType.METHOD);
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
