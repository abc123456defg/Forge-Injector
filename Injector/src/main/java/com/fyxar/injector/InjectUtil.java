package com.fyxar.injector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

public class InjectUtil {

	public static boolean isMinecraftFound() {
		Optional<VirtualMachineDescriptor> first = VirtualMachine.list()
                .stream()
                .filter(vm_desc -> vm_desc.displayName().startsWith("net.minecraft.client.main.Main") ||
                		vm_desc.displayName().startsWith("net.minecraft.launchwrapper.Launch"))
                .findFirst();
		
		return first.isPresent();
	}
	
	public static boolean inject(String filePath) throws AttachNotSupportedException, IOException {
		Optional<VirtualMachineDescriptor> first = VirtualMachine.list()
                .stream()
                .filter(vm_desc -> vm_desc.displayName().startsWith("net.minecraft.client.main.Main") ||
                		vm_desc.displayName().startsWith("net.minecraft.launchwrapper.Launch"))
                .findFirst();
		
		File tmpFile = File.createTempFile("mlv_", ".jar");

		try (
				InputStream inputStream = InjectUtil.class.getResourceAsStream("/inject-jar/Forge-Injector.jar");
				FileOutputStream fos = new FileOutputStream(tmpFile)
		) {
			byte[] buf = new byte[1024];
			int read;

			while ((read = inputStream.read(buf)) != -1) {
				fos.write(buf, 0, read);
			}
		} catch (IOException e) {
			return false;
		}
		
		VirtualMachine attach = VirtualMachine.attach(first.get());

		try {
            attach.loadAgent(tmpFile.getAbsolutePath(), filePath);
            attach.detach();
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
        }
		
		return false;
	}
	
}
