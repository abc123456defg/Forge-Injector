package com.fyxar.injector.mapper;

import com.fyxar.injector.mapper.enums.MappingVersion;
import com.fyxar.injector.mapper.mappings.Mapping;
import com.fyxar.injector.mapper.mappings.impl.ClassMapping;
import com.fyxar.injector.mapper.mappings.impl.FieldMapping;
import com.fyxar.injector.mapper.mappings.impl.MethodMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MappingList extends ArrayList<Mapping> {

	public MappingList(MappingVersion version) throws Exception {
		handleMappingFile(version);
	}

	//TODO: fix this stupid code and improve performance :sob:

	private void handleMappingFile(MappingVersion version) throws Exception {
		InputStream inputStream = AutoRemapper.class.getClassLoader().getResourceAsStream(version.getVersion() + ".txt");

		if (inputStream != null) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				String line;
				while ((line = reader.readLine()) != null) {
					//System.out.println(line);

					if(line.startsWith("CL: ")) {
						String[] mappingClass = line.replace("CL: ", "").split(" ");
						if(mappingClass.length < 2) continue;

						this.add(new ClassMapping(mappingClass[0], mappingClass[1]));
					}

					if(line.startsWith("FD: ")) {
						String[] mappingField = line.replace("FD: ", "").split(" ");
						if(mappingField.length < 2) continue;

						String[] obfFieldArray = mappingField[0].split("/");
						String[] deObfFieldArray = mappingField[1].split("/");

						String obfFieldName = obfFieldArray[obfFieldArray.length - 1];
						String deobfFieldName = deObfFieldArray[deObfFieldArray.length - 1];

						String obfOwner = mappingField[0].replace("/" + obfFieldName, "");
						String deobfOwner = mappingField[1].replace("/" + deobfFieldName, "");

						this.add(new FieldMapping(obfOwner, deobfOwner, obfFieldName, deobfFieldName));
					}

					if(line.startsWith("MD: ")) {
						String[] mappingField = line.replace("MD: ", "").split(" ");
						if(mappingField.length < 4) continue;

						String[] obfMethodArray = mappingField[0].split("/");
						String[] deObfMethodArray = mappingField[2].split("/");

						String obfMethodName = obfMethodArray[obfMethodArray.length - 1];
						String deobfMethodName = deObfMethodArray[deObfMethodArray.length - 1];

						String obfOwner = mappingField[0].replace("/" + obfMethodName, "");
						String deobfOwner = mappingField[2].replace("/" + deobfMethodName, "");

						this.add(new MethodMapping(obfOwner, deobfOwner, obfMethodName, deobfMethodName));
					}
				}
			}
		}
	}
	
}
