package com.fyxar.injector.mapper.mappings;

public class Mapping {

	private final MappingType type;

	public Mapping(MappingType type) {
		this.type = type;
	}

	public MappingType getMappingType() {
		return type;
	}

}
