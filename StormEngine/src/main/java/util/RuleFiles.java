package util;

import util.rules.Rules;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * Created by joao on 13/12/15.
 */
public class RuleFiles {
    public Path path;
    private static RuleFiles instance = null;

    RuleFiles(){
        try {
            URI uri = RuleFiles.class.getResource("/rules").toURI();
            java.nio.file.FileSystem fileSystem = null;
            if (uri.getScheme().equals("jar")) {
                fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                path = fileSystem.getPath("/rules");
            } else {
                path = Paths.get(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static synchronized RuleFiles getInstance() {
        if(instance == null) {
            instance = new RuleFiles();
        }
        return instance;
    }
}
