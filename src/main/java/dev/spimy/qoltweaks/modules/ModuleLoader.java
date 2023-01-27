package dev.spimy.qoltweaks.modules;

import dev.spimy.qoltweaks.QoLTweaks;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;

public class ModuleLoader {

    private final QoLTweaks plugin = QoLTweaks.getInstance();

    public ModuleLoader() {
        String packageName = getClass().getPackageName();

        for (Class<?> module : new Reflections(packageName).getSubTypesOf(Module.class)) {
            RequireProtocolLib annotation = module.getAnnotation(RequireProtocolLib.class);
            if (annotation != null && !plugin.hasProtocolLib()) {
                String moduleName = plugin.getModuleName(module.getSimpleName());
                plugin.getLogger().warning(
                    moduleName + " module could not be loaded as it requires ProtocolLib."
                );
                continue;
            }

            try {
                registerEvent((Module) module.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void registerEvent(Module module) {
        plugin.getServer().getPluginManager().registerEvents(module, plugin);
    }

}
