package model;

import java.io.File;

/**
 * All global variables/constants are stored in this class
 */
public class Globals {
    //Strings from the view that have logical dependencies (i.e. used more than once)
    public static final String GENERAL_PANEL_CAPTION = "General Configuration";
    public static final String DATA_PANEL_CAPTION = "Data Settings";
    public static final String PARAMETERS_PANEL_CAPTION = "Parameter Settings";
    public static final String COMPARE_CONDITIONS = "Compare Conditions";
    public static final String COMPARE_GENOMES = "Compare Strain/Species";
    public static final String CONFIG_FILE_TMP_PATH = "/tmp/tssconfiguration.conf";
    public static final String PARAMETERS_PRESET = "Preset";
    public static final String PARAMETERS_CUSTOM = "Custom";
    public static final String CLUSTER_METHOD_HIGHEST = "HIGHEST";
    public static final String CLUSTER_METHOD_FIRST = "FIRST";

    //Initial parameter values
    public static final boolean IS_CONDITIONS_INIT = false;
    public static final int NUMBER_OF_DATASETS_INIT = 1;
    public static final int NUMBER_OF_REPLICATES_INIT = 1;
    public static final double NORMALIZATION_PERCENTILE_INIT = 0.9;
    public static final double ENRICHMENT_NORMALIZATION_PERCENTILE_INIT = 0.5;
    public static final String CLUSTER_METHOD_INIT = CLUSTER_METHOD_HIGHEST;
    public static final int TSS_CLUSTERING_DISTANCE_INIT = 3;
    public static final int CROSS_DATASET_SHIFT_INIT = 1;
    public static final int CROSS_REPLICATE_SHIFT_INIT = 1;
    public static final int MATCHING_REPLICATES_INIT = 1;
    public static final int UTR_LENGTH_INIT = 300;
    public static final int ANTISENSE_UTR_LENGTH_INIT = 100;

}
