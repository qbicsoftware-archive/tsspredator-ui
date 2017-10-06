package view.tsswindows;

import com.vaadin.ui.*;
import javafx.scene.shape.HLineTo;

public class ParametersWindow3 extends TSSWindow{

    @Override
    void designContentLayout() {
        Layout contentLayout = new VerticalLayout();

        HorizontalLayout percentiles = new HorizontalLayout();
        Slider normalizationPercentile = new Slider("Normalization Percentile");
        normalizationPercentile.setMin(0);
        normalizationPercentile.setMax(1);
        normalizationPercentile.setResolution(1);
        Slider enrichedNormalizationPercentile = new Slider("Enriched Normalization Percentile");
        enrichedNormalizationPercentile.setMin(0);
        enrichedNormalizationPercentile.setMax(1);
        enrichedNormalizationPercentile.setResolution(1);
        percentiles.addComponents(normalizationPercentile,enrichedNormalizationPercentile);

        HorizontalLayout methodAndDistance = new HorizontalLayout();
        ComboBox<String> clusterMethod = new ComboBox<>("Clustering Method");
        clusterMethod.setItems("HIGHEST", "FIRST");
        Slider clusteringDistance = new Slider("TSS Clustering Distance");
        clusteringDistance.setMin(0);
        clusteringDistance.setMax(100);
        clusteringDistance.setResolution(0);
        methodAndDistance.addComponents(clusterMethod,clusteringDistance);

        contentLayout.addComponents(percentiles,methodAndDistance);
        setContentLayout(contentLayout);

    }
}
