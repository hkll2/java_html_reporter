package html_report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.util.ExportUtils;

/**
 * HtmlReport is a utility class to create HTML pages for reporting purposes.
 *
 */
public class HtmlReport {
  final private File reportFolder;
  final private File dataFolder;
  private String htmlContent;

  /**
   * Constructs the HtmlReport object. Create 1 object for each separate report.
   * 
   * @param nonExistentReportFolder
   *          A local folder to store the report html. This folder must not
   *          exist. The root folder (and all parents) will be created.
   * @throws IOException
   *           If there is any IO issues (e.g., permissions, etc.).
   */
  public HtmlReport(final String nonExistentReportFolder) throws IOException {
    this.reportFolder = new File(nonExistentReportFolder);

    if (this.reportFolder.exists()) {
      throw new IOException(nonExistentReportFolder
          + " already exists. Use a different folder.");
    }
    // Create the report folder.
    this.reportFolder.mkdirs();

    // Create a data subfolder to store all the images, etc..
    this.dataFolder = new File(nonExistentReportFolder, "data");
    dataFolder.mkdir();

    htmlContent = "<!DOCTYPE html> <html> <body>";
  }

  /**
   * Adds a header to the HTML report (uses the h1 tag).
   * 
   * @param header
   *          Header to add.
   * @see <a href="http://www.w3schools.com/tags/tag_hn.asp">HMTL Header
   *      Tutorial</a>
   */
  public void addHeader1(final String header) {
    System.out.println("Adding header1.");
    htmlContent += "<h1 style=\"color:blue;\">" + header + "</h1>";
  }

  /**
   * Adds a header to the HTML report (uses the h2 tag).
   * 
   * @param header
   *          Header to add.
   * @see <a href="http://www.w3schools.com/tags/tag_hn.asp">HMTL Header
   *      Tutorial</a>
   */
  public void addHeader2(final String header) {
    System.out.println("Adding header2.");
    htmlContent += "<h2 style=\"color:blue;\">" + header + "</h2>";
  }

  /**
   * Adds a paragraph to the HTML report (uses the p tag).
   * 
   * @param text
   *          Paragraph to add.
   * @see <a href="http://www.w3schools.com/tags/tag_p.asp">HMTL Paragraph
   *      Tutorial</a>
   */
  public void addText(final String text) {
    System.out.println("Adding text.");
    htmlContent += "<p>" + text + "</p>";
  }

  /**
   * Adds a horizontal line to the HTML report (uses the hr tag).
   * 
   * @see <a href="http://www.w3schools.com/tags/tag_hr.asp">HMTL Horizontal
   *      Line Tutorial</a>
   */
  public void addHorizontalLine() {
    System.out.println("Adding horizontal line.");
    htmlContent += "<hr>";
  }

  /**
   * Adds empty vertical lines to the HTML report (uses the br tag).
   * 
   * @param number
   *          Number of lines to add.
   * @see <a href="http://www.w3schools.com/tags/tag_br.asp">HMTL Line Break
   *      Tutorial</a>
   */
  public void addLineBreak(final int number) {
    System.out.println("Adding a line break.");
    for (int i = 0; i < number; i++) {
      htmlContent += "<br>";
    }
  }

  /**
   * Helper function to return number of columns of a table.
   * 
   * @param table
   *          A matrix table.
   * @param debugName
   *          A debug name which will be used if there is an exception.
   * @return The number of columns in the matrix.
   * @throws Exception
   *           If the table is empty, or inconsistent number of columns.
   */
  private int numCols(final String[][] table, final String debugName)
      throws Exception {
    Set<Integer> nColumns = new HashSet<Integer>();
    for (String[] row : table) {
      nColumns.add(row.length);
    }
    if (nColumns.size() != 1) {
      throw new Exception(debugName + " table has different sized columns.");
    }
    return nColumns.iterator().next();
  }

  private int numCols(final boolean[][] table, final String debugName)
      throws Exception {
    Set<Integer> nColumns = new HashSet<Integer>();
    for (boolean[] row : table) {
      nColumns.add(row.length);
    }
    if (nColumns.size() != 1) {
      throw new Exception(debugName + " table has different sized columns.");
    }
    return nColumns.iterator().next();
  }

  /**
   * Asserts table2 has same shape with table1. If table 2 is nullable, the
   * assertion succeeds.
   * 
   * @param table1
   *          Reference matrix.
   * @param table2
   *          Test matrix.
   * @param table2Name
   *          A name for debugging purposes.
   * @throws Exception
   *           If shapes don't match.
   */
  private void assertShapeEqual(final String[][] table1,
      final boolean[][] table2, String table2Name) throws Exception {
    if (table2 == null) {
      return;
    }
    if (table1.length != table2.length) {
      throw new Exception(table2Name
          + " number of rows is not equal to table's.");
    }

    if (numCols(table1, "table") != numCols(table2, table2Name)) {
      throw new Exception(table2Name
          + " number of cols is not equal to table's.");
    }
  }

  /**
   * Adds a table to the HTML report (uses table tag).
   * 
   * @param table
   *          A matrix.
   * @param boldCells
   *          A matrix (same shape with table) which indicates which cells will
   *          be bold.
   * @param redCells
   *          A matrix (same shape with table) which indicates which cells will
   *          be red.
   * @param greenCells
   *          A matrix (same shape with table) which indicates which cells will
   *          be green.
   * @throws Exception
   *           If the shapes don't match.
   * @see <a href="http://www.w3schools.com/html/html_tables.asp">HMTL Table
   *      Tutorial</a>
   */
  public void addTable(final String[][] table, final boolean[][] boldCells,
      final boolean[][] redCells, final boolean[][] greenCells)
      throws Exception {
    System.out.println("Adding table.");

    assertShapeEqual(table, boldCells, "boldCells");
    assertShapeEqual(table, redCells, "redCells");
    assertShapeEqual(table, greenCells, "greenCells");

    htmlContent += "<table style=\"width:100%\" border=\"1\">";
    for (int row = 0; row < table.length; row++) {
      htmlContent += "<tr>";
      for (int col = 0; col < table[row].length; col++) {
        String cell = table[row][col];
        if (boldCells != null && boldCells[row][col]) {
          cell = "<b>" + cell + "</b>";
        }

        if (redCells != null && redCells[row][col]) {
          cell = "<td bgcolor=\"red\">" + cell + "</td>";
        } else if (greenCells != null && greenCells[row][col]) {
          cell = "<td bgcolor=\"green\">" + cell + "</td>";
        } else {
          cell = "<td>" + cell + "</td>";
        }

        htmlContent += cell;
      }
      htmlContent += "</tr>";
    }
    htmlContent += "</table>";
  }

  /**
   * Adds a JFreeChart object as an image to the HTML report (uses the src tag).
   * 
   * @param chart
   *          Chart to add.
   * @param height
   *          Height of the chart (suggested 400 but adjust based on how it
   *          looks).
   * @param width
   *          With of the chart (suggested 400 but adjust based on how it
   *          looks).
   * @param uniqueChartName
   *          A unique name for the chart. This will not be shown in the HTML
   *          report but will be used while saving the underlying image to local
   *          disk. Make sure you choose a unique name.
   * @throws IOException
   *           If there is an IO problem while saving the image file to disk.
   * @see <a href="http://www.w3schools.com/html/html_images.asp">HMTL Image
   *      Tutorial</a>
   */
  public void addJFreeChart(final JFreeChart chart, final int height,
      final int width, final String uniqueChartName) throws IOException {
    System.out.println("Adding chart: " + uniqueChartName);

    String strippedUniqueChartName = uniqueChartName.replaceAll("[^A-Za-z0-9]",
        "").toLowerCase()
        + ".png";
    File chartFile = new File(dataFolder, strippedUniqueChartName);
    if (chartFile.exists()) {
      throw new IOException(uniqueChartName
          + " is not unique. Please choose a different name.");
    }
    float widthHeightRatio = ((float) width) / height;
    ExportUtils.writeAsPNG(chart, 500, (int) (500 * widthHeightRatio),
        chartFile);

    File strippedUniqueChartFile = new File("data", strippedUniqueChartName);
    htmlContent += "<img src=\"" + strippedUniqueChartFile.getPath()
        + "\" alt=\"" + uniqueChartName + "\" height=\"" + height
        + "\" width=\"" + width + "\"> <br>";
  }

  /**
   * Adds a file hyperlink to the html file (uses the a tag).
   * 
   * @param filePath
   *          File path to link. This file must exists in local disk.
   * @param linkName
   *          The name of the link that appears in the HTML report.
   * @param asABulletPoint
   *          List the link as a bullet point if set true.
   * @throws IOException
   *           If there is a proble copying the file or the underlying file does
   *           not exist.
   * @see <a href="http://www.w3schools.com/tags/att_li_type.asp">HMTL Hyperlink
   *      Tutorial</a>
   */
  public void addFileLink(final String filePath, final String linkName,
      boolean asABulletPoint) throws IOException {
    System.out.println("Adding link: " + linkName);
    File file = new File(filePath);
    String fileNameAndLinkName = linkName + "-" + file.getName();
    fileNameAndLinkName = fileNameAndLinkName.replaceAll("[^A-Za-z0-9.-]", "")
        .toLowerCase();
    File uniqueFile = new File(dataFolder, fileNameAndLinkName);
    if (uniqueFile.exists()) {
      throw new IOException("When creating a link, the filePath (" + filePath
          + ") linkName (" + linkName
          + ") should be unique (after strip out non alphanumeric chars).");
    }

    Files.copy(file.toPath(), uniqueFile.toPath());

    File uniqueRelativePath = new File("data", uniqueFile.getName());

    String link = "<a href=\"" + uniqueRelativePath.getPath() + "\"> "
        + linkName + " </a> <br>";
    if (asABulletPoint) {
      link = "<li>" + link + "</li>";
    } else {
      link += "<br>";
    }
    htmlContent += link;
  }

  /**
   * Adds a hyper link to the HTML report (uses the a tag).
   * 
   * @param weblink
   *          Website link.
   * @param linkName
   *          Name that appears in the HTML report.
   * @param asABulletPoint
   *          List the link as a bullet point if set true.
   * @see <a href="http://www.w3schools.com/tags/att_li_type.asp">HMTL Hyperlink
   *      Tutorial</a>
   */
  public void addHyperLink(final String weblink, final String linkName,
      boolean asABulletPoint) {
    String link = "<a href=\"" + weblink + "\"> " + linkName + " </a>";
    if (asABulletPoint) {
      link = "<li>" + link + "</li>";
    } else {
      link += "<br>";
    }
    htmlContent += link;
  }

  /**
   * Adds bullet points to the HTML report (uses li tag).
   * 
   * @param bullets
   *          Bullets to add.
   * @see <a href="http://www.w3schools.com/tags/att_li_type.asp">HMTL Bullet
   *      Points Tutorial</a>
   */
  public void addBulletPoints(final String[] bullets) {
    htmlContent += "<ul>";
    for (final String bullet : bullets) {
      htmlContent += "<li>" + bullet + "</li>";
    }
    htmlContent += "</ul>";
  }

  /**
   * Renders the HTML report and returns the absolute path.
   * 
   * @return The path of the rendered HTML report.
   * @throws IOException
   *           If there is a problem while saving the file.
   */
  public String createHtmlReport() throws IOException {
    htmlContent += "</body> </html>";
    File reportFile = new File(reportFolder, "report.html");
    BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile));
    writer.write(htmlContent);
    writer.close();
    return reportFile.getPath();
  }
}
