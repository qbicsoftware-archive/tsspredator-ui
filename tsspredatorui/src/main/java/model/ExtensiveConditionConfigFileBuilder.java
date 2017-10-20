package model;

/**
 * @author jmueller
 */
public class ExtensiveConditionConfigFileBuilder extends ExtensiveConfigFileBuilder{
    @Override
    public void buildMode() {
        configFile.setModeConditions(true);
    }

    @Override
    public void buildAlignmentFile(String alignmentFile) {
        //DO NOTHING
    }
}
