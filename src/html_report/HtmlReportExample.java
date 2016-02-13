package html_report;

import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * This is an example on how to use the HTML report.
 */
public class HtmlReportExample {
  public static void main(String[] args) throws Exception {
    /***********************************/
    /***** CREATE the HTML REPORT ******/
    /***********************************/
    HtmlReport report = new HtmlReport("/tmp/reports/report5");

    /**************************/
    /***** ADD A SECTION ******/
    /**************************/
    report.addHeader1("Spain");

    /****************************/
    /***** ADD A PARAGRAPH ******/
    /****************************/
    report
        .addText("Spain, officially the Kingdom of Spain, is a sovereign state largely located on the Iberian Peninsula in southwestern Europe, with archipelagos in the Atlantic Ocean and Mediterranean Sea, and several small territories on and near the north African coast. ");

    /******************************/
    /***** ADD BULLET POINTS ******/
    /******************************/
    String[] bulletPoints = { "GDP: $1.6T", "Gini: 33.7", "HDI: 0.876" };
    report.addBulletPoints(bulletPoints);

    /************************/
    /***** ADD A TABLE ******/
    /************************/
    // Add a subsection (not required).
    report.addHeader2("Cities");

    // Prepare the table data.
    String[][] cities = { { "City", "Population" }, { "Madrid", "3.2M" },
        { "Barcelona", "1.6M" }, { "Valencia", "814K" } };
    // Mark which cells will be bold.
    boolean[][] boldCells = { { true, true }, { false, false },
        { false, false }, { false, false } };
    // Mark which cells will be red.
    boolean[][] redCells = { { false, false }, { false, false },
        { false, false }, { false, true } };
    // Mark which cells will be green.
    boolean[][] greenCells = { { false, false }, { false, true },
        { false, false }, { false, false } };
    // Add the table to the HTML report. Either of boldCells, redCells,
    // greenCells nullable.
    report.addTable(cities, boldCells, redCells, greenCells);

    /****************************/
    /***** ADD A PIE CHART ******/
    /****************************/
    // Add a subsection (not required).
    report.addHeader2("Demographics");
    // Prepare the data.
    DefaultPieDataset dataset = new DefaultPieDataset();
    dataset.setValue("2-24", 40);
    dataset.setValue("25-34", 40.2);
    dataset.setValue("35+", 19.8);
    // Create the chart object.
    JFreeChart chart = ChartFactory.createPieChart("Demographic Breakdown",
        dataset, false, false, false);
    // Increase the font sizes so they are visible.
    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 15));
    chart.getTitle().setFont(new Font("SansSerif", Font.PLAIN, 15));
    // Finally add the chart to the HTML Report.
    report.addJFreeChart(chart, 300, 300, "Demographics");

    /****************************/
    /***** ADD A LINE CHART *****/
    /****************************/
    // Add a subsection (not required).
    report.addHeader2("Economy");
    // 1st plot has 3 datapoints.
    XYSeries imports = new XYSeries("Imports");
    imports.add(1, 100);
    imports.add(2, 150);
    imports.add(3, 200);
    // 2nd plot has 3 datapoints.
    XYSeries exports = new XYSeries("Exports");
    exports.add(1, 150);
    exports.add(2, 250);
    exports.add(3, 260);
    // Prepare the final dataset.
    XYSeriesCollection dataset1 = new XYSeriesCollection();
    dataset1.addSeries(imports);
    dataset1.addSeries(exports);
    // Create the chart object.
    final JFreeChart chart1 = ChartFactory.createXYLineChart(
        "Imports and Exports", "Year", "Billions", dataset1,
        PlotOrientation.VERTICAL, false, false, false);
    // Massage the plots a bit.
    XYPlot plot1 = chart1.getXYPlot();
    NumberAxis domainAxis = (NumberAxis) plot1.getDomainAxis();
    // X-axis will be shown as integers.
    domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
    // Increase X-axis font.
    domainAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 15));
    NumberAxis rangeAxis = (NumberAxis) plot1.getRangeAxis();
    // Increase Y-axis font.
    rangeAxis.setLabelFont(new Font("SansSerif", Font.PLAIN, 15));
    // Increase the title font.
    chart1.getTitle().setFont(new Font("SansSerif", Font.PLAIN, 15));
    // Finally add the Chart to the HTML report.
    report.addJFreeChart(chart1, 400, 600, "Economy");

    /*********************************/
    /***** ADD A HORIZONTAL LINE *****/
    /*********************************/
    report.addHorizontalLine();

    /***************************/
    /***** ADD A FILE LINK *****/
    /***************************/
    // Add a subsection (not required).
    report.addHeader2("Links");
    // Add a link.
    report.addFileLink("/tmp/abc.csv", "ABC File", true);

    /***************************/
    /***** ADD A HYPER LINK *****/
    /***************************/
    // Add a link.
    report.addHyperLink("https://www.google.com/", "Google Search", true);

    /*****************************/
    /***** RENDER THE REPORT *****/
    /*****************************/
    String reportFile = report.createHtmlReport();
    System.out.println("Report is written to: " + reportFile);
  }
}
