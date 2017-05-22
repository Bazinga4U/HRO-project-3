package info.bhl.parcodom;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.*;
//Make a graph
public class GraphActivity2 extends AppCompatActivity  {

    BarGraphSeries<DataPoint> series;
    public static int A1Aantal;
    public static int A2Aantal;
    public static int B1Aantal;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //creating an onCreate class which will draw the graph later.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_2);

        GraphView graph = (GraphView) findViewById(R.id.graph2);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {

                new DataPoint(1, A1Aantal),
                new DataPoint(2, A2Aantal),
                new DataPoint(3, B1Aantal),
        });
        graph.addSeries(series);

        graph.getViewport().setMinX(0.75);
        graph.getViewport().setMaxX(3.25);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(400);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        series.setSpacing(60);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);

    }

}
