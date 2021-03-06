package directions;

import core.Applet;
import text.ImmutableTex;
import util.Useful;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Directions {
    public static List<Scene> allScenes = new ArrayList<>(); // Order must be preserved!

    public static void init(Applet window) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        File[] files = new File(".\\src\\directions\\subscene").listFiles();

        if (files != null)
            for (File file : files) {
                if (file.isFile()) { // Assuming folders?
                    Class<?> c;
                    try {
                        c = Class.forName("directions.subscene." + Useful.removeExtension(file.getName()));
                    } catch (ClassNotFoundException e) {
                        System.out.println(file.getName() + " is not a java file!");
                        continue;
                    }
                    if (Scene.class.isAssignableFrom(c)) {
                        Scene scene = (Scene) c.getDeclaredConstructor(Applet.class).newInstance(window);
                        if (scene.runScene())
                            allScenes.add(scene);
                    }

                }
            }
        System.out.println(allScenes);
    }

    public static boolean directions() {
        //call the scenes here
        for (Scene s : allScenes) {
            if (!s.execute())
                return false;
        }
        return true;
    }
}
