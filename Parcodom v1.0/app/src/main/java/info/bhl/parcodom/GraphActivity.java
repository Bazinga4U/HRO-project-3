package info.bhl.parcodom;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
//Make a graph
public class GraphActivity extends AppCompatActivity  {

    LineGraphSeries<DataPoint> series;
    LineGraphSeries<DataPoint> series2; //making variable.
    LineGraphSeries<DataPoint> series3;
    LineGraphSeries<DataPoint> series4;
    LineGraphSeries<DataPoint> series5;
    LineGraphSeries<DataPoint> series6;
    public static int A1Uur;
    public static int A1Dag;
    public static int A2Uur;
    public static int A2Dag;
    public static int B1Uur;
    public static int B1Dag;
    @Override
    protected void onCreate(Bundle savedInstanceState) { //creating an onCreate class which will draw the graph later.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        double y,x, y2, y3, y4,y5, y6;
        x = 0;
        y = 0;
        y2 = 0;
        y3 = 0;
        y4 = 0;
        y5 = 0;
        y6 = 0;

        GraphView graph = (GraphView) findViewById(R.id.graph1); //Graph instance
        graph.getViewport().setMinX(0);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxX(12);
        graph.getViewport().setMaxY(60);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        series = new LineGraphSeries<DataPoint>();
        series2 = new LineGraphSeries<DataPoint>();
        series3 = new LineGraphSeries<DataPoint>();
        series4 = new LineGraphSeries<DataPoint>();
        series5 = new LineGraphSeries<DataPoint>();
        series6 = new LineGraphSeries<DataPoint>();
        //50% en 15%
        series5.setColor(Color.parseColor("#33cc33"));
        series3.setColor(Color.parseColor("#ffff00"));
        series.setColor(Color.parseColor("#ff0000"));
        series6.setColor(Color.parseColor("#004d33"));
        series4.setColor(Color.parseColor("#4d4d00"));
        series2.setColor(Color.parseColor("#4d0000"));
        series.setDrawDataPoints(true);
        series3.setDrawDataPoints(true);
        series5.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series3.setDataPointsRadius(10);
        series5.setDataPointsRadius(10);
        for(int i =0; i<20; i++) { //Formula for the graph itself.
            x = x + 1;
            y = y + A1Uur;
            y2 = A1Dag;
            y3 = y3 + A2Uur;
            y4 = A2Dag;
            y5 = y5 + B1Uur;
            y6 = B1Dag;
            series.appendData(new DataPoint(x, y), true, 20);
            series2.appendData(new DataPoint(x, y2), true, 20);
            series3.appendData(new DataPoint(x, y3), true, 20);
            series4.appendData(new DataPoint(x, y4), true, 20);
            series5.appendData(new DataPoint(x, y5), true, 20);
            series6.appendData(new DataPoint(x, y6), true, 20);
        }
        graph.addSeries(series);
        graph.addSeries(series2);
        graph.addSeries(series3);
        graph.addSeries(series4);
        graph.addSeries(series5);
        graph.addSeries(series6);
    }

}
