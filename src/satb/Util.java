/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satb;

import java.io.File;
import java.nio.file.Paths;

/**
 *
 * @author Leandro
 */
public class Util {
    
    public static String getCurrentRelativePath () {
        return Paths.get("").toAbsolutePath().normalize().toString() + File.separator;
    }
    
}
