package model;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ExtensiveConfigFileBuilderTest {
    int datasets = 3;
    int replicates = 4;

    @Test
    public void testBuildExtensiveGenomeConfigFile() {
        ConfigFileBuilder configFileBuilder = new ExtensiveGenomeConfigFileBuilder();
        configFileBuilder.buildProjectName("test_project");
        configFileBuilder.buildMode();
        configFileBuilder.buildNumberOfDatasets(datasets);
        configFileBuilder.buildNumberOfReplicates(replicates);
        configFileBuilder.buildAlignmentFile("MyAlignmentFile.xmfa");

        for (int i = 0; i < datasets; i++) {
            configFileBuilder.buildGenomeName(i, "" + i);
            configFileBuilder.buildGenomeFasta(i, "MyFasta" + i + ".fa");
            configFileBuilder.buildGenomeAlignmentID(i, "" + i);
            configFileBuilder.buildGenomeAnnotation(i, "MyAnnotation" + i + ".gff");
            for (int j = 0; j < replicates; j++) {
                configFileBuilder.buildReplicateID(i, j, "" + (char) (j + 97));
                configFileBuilder.buildEnrichedPlus(i, j, "MyEnrichedCoding" + i + (char) (j + 97) + ".gr");
                configFileBuilder.buildEnrichedMinus(i, j, "MyEnrichedTemplate" + i + (char) (j + 97) + ".gr");
                configFileBuilder.buildNormalPlus(i, j, "MyNormalCoding" + i + (char) (j + 97) + ".gr");
                configFileBuilder.buildNormalMinus(i, j, "MyNormalTemplate" + i + (char) (j + 97) + ".gr");
            }
        }

        configFileBuilder.buildWriteGraphs(true);
        configFileBuilder.buildStepHeight(1);
        configFileBuilder.buildStepHeightReduction(0.1);
        configFileBuilder.buildStepFactor(2);
        configFileBuilder.buildStepFactorReduction(0.2);
        configFileBuilder.buildEnrichmentFactor(3);
        configFileBuilder.buildProcessingSiteFactor(4);
        configFileBuilder.buildStepLength(5);
        configFileBuilder.buildBaseHeight(6);
        configFileBuilder.buildNormalizationPercentile(0.7);
        configFileBuilder.buildEnrichmentNormalizationPercentile(0.8);
        configFileBuilder.buildClusterMethod("HIGHEST");
        configFileBuilder.buildTssClusteringDistance(9);
        configFileBuilder.buildAllowedCrossSubjectShift(10);
        configFileBuilder.buildAllowedCrossReplicateShift(11);
        configFileBuilder.buildMatchingReplicates(12);
        configFileBuilder.buildUtrLength(13);
        configFileBuilder.buildAntisenseUtrLength(14);

        ConfigFile configFile = configFileBuilder.createConfigFile();

        File confFile = new File("test_resources/ExtensiveConfigFileBuilderTest.conf");
        configFile.writeConfigFile(confFile);


    }

}