package model;

/**
 * @author jmueller
 */
public class ExtensiveGenomeConfigFileBuilder extends ExtensiveConfigFileBuilder{
    @Override
    public void buildMode() {
        configFile.setModeConditions(false);
    }

    public void buildAlignmentFile(String alignmentFile){
        configFile.setAlignmentFile(alignmentFile);
    }
}
