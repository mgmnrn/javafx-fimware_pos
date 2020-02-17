//package application.controller;
//
//import java.awt.image.BufferedImage;
//import java.io.*;
//import java.net.URL;
//import java.sql.*;
//import java.util.List;
//import java.util.ResourceBundle;
//
//import application.database.DBConnection;
//import application.model.ProductContainer;
//import application.model.TypeContainer;
//import javafx.embed.swing.SwingFXUtils;
//import javafx.fxml.FXML;
//import javafx.geometry.Pos;
//import javafx.scene.Node;
//import javafx.scene.control.Button;
//import javafx.scene.image.Image;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.controlsfx.control.Notifications;
//
//import javax.imageio.ImageIO;
//
//public class ExcelController {
//
//    @FXML
//    private ResourceBundle resources;
//
//    @FXML
//    private URL location;
//
//    @FXML
//    private AnchorPane rootPane;
//
//    @FXML
//    private Button excelImport;
//
//    @FXML
//    private Button excelExport;
//
//    Notifications notificationBuilder = null;
//
//    @FXML
//    void initialize() {
//        excelExport.setOnAction(e-> {
//            Stage stage = (Stage) rootPane.getScene().getWindow();
//            Connection conn = DBConnection.getConnection();
//
//            notification(Pos.TOP_RIGHT, "Excel файлаа сонгоно уу", "Бүтээгдэхүүн хадгалах");
//            notificationBuilder.darkStyle();
//            notificationBuilder.showInformation();
//
//            File file;
//            FileChooser fileChooser = new FileChooser();
//            configuringFileChooser(fileChooser);
//            file = fileChooser.showOpenDialog(stage);
//
//            if(file == null) {
//                notification(Pos.BOTTOM_RIGHT,"Бүтээгдэхүүний мэдээлэл унших файлаа сонгоно уу", "Бүтээгдэхүүн оруулах");
//                notificationBuilder.darkStyle();
//                notificationBuilder.showWarning();
//            } else {
//                try {
//                    String insertProduct = "INSERT INTO products(name, bar_code, id, unit_price, type_id, weight, measure_unit, size) values(?, ?, ?, ?, ?, ?, ?, ?)";
//                    PreparedStatement pstInsert = conn.prepareStatement(insertProduct);
//
//                    FileInputStream fileIn = new FileInputStream(file);
//
//                    XSSFWorkbook wb = new XSSFWorkbook(fileIn);
//                    XSSFSheet sheet = wb.getSheetAt(0);
//
//                    TypeContainer typeContainer = new TypeContainer();
//                    ProductContainer productContainer = new ProductContainer();
//
//                    Row row;
//                    for (int i=1; i<=sheet.getLastRowNum(); i++) {
//                        row = sheet.getRow(i);
//                        if(productContainer.checkId((int) row.getCell(2).getNumericCellValue()) == false) {
//                            pstInsert.setString(1, row.getCell(0).getStringCellValue());
//                            pstInsert.setInt(2, (int) row.getCell(1).getNumericCellValue());
//                            pstInsert.setInt(3, (int) row.getCell(2).getNumericCellValue());
//                            pstInsert.setInt(4, (int) row.getCell(3).getNumericCellValue());
//                            pstInsert.setInt(5, typeContainer.nameChangeId(row.getCell(4).getStringCellValue()));
//                            pstInsert.setInt(6, (int) row.getCell(5).getNumericCellValue());
//                            pstInsert.setString(7, row.getCell(6).getStringCellValue());
//                            pstInsert.setInt(8, (int) row.getCell(7).getNumericCellValue());
//                            pstInsert.execute();
//                        }
//                    }
//
//                    notification(Pos.BOTTOM_RIGHT,"Бүтээгдэхүүний мэдээллийг амжилттай хадгалаа", "Хадгалах");
//                    notificationBuilder.darkStyle();
//                    notificationBuilder.showConfirm();
//
//                    fileIn.close();
//                    wb.close();
//                    pstInsert.close();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                } catch (FileNotFoundException ex) {
//                    ex.printStackTrace();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            notification(Pos.TOP_RIGHT, "Зургуудаа сонгоно уу?", "Зураг оруулах");
//            notificationBuilder.darkStyle();
//            notificationBuilder.showInformation();
//
//            FileChooser fileChooser1 = new FileChooser();
//            List<File> files = fileChooser1.showOpenMultipleDialog(stage);
//
//            if(files != null) {
//                try {
//                    String updateImage = "UPDATE products SET image = ? WHERE id = ?";
//                    PreparedStatement pstUpdate  = conn.prepareStatement(updateImage);
//
//                    for(int i=0; i<files.size(); i++) {
//                        File temp = new File(files.get(i).getPath());
//                        FileInputStream fis = new FileInputStream(temp);
//                        pstUpdate.setBinaryStream(1, fis, fis.available());
//                        pstUpdate.setInt(2, Integer.parseInt(files.get(i).getName().substring(0, 4)));
//                        pstUpdate.execute();
//                        fis.close();
//                    }
//
//                    notification(Pos.BOTTOM_RIGHT, "Бүтээгдэхүүний зургийг амжилттай хадгаллаа", "Зураг хадгалах");
//                    notificationBuilder.darkStyle();
//                    notificationBuilder.showConfirm();
//
//                    pstUpdate.close();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                } catch (FileNotFoundException ex) {
//                    ex.printStackTrace();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            } else {
//                notification(Pos.BOTTOM_RIGHT, "Бүтээгдэхүүний зургийг сонгоно уу", "Зураг оруулах");
//                notificationBuilder.darkStyle();
//                notificationBuilder.showWarning();
//            }
//        });
//
//        excelImport.setOnAction(e-> {
//            Stage stage = (Stage) rootPane.getScene().getWindow();
//
//            notification(Pos.TOP_RIGHT, "Хадгалах замаа сонгоно уу?", "Файл сонгох");
//            notificationBuilder.darkStyle();
//            notificationBuilder.showInformation();
//
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setInitialFileName("productInfo.xlsx");
//            File file = fileChooser.showSaveDialog(stage);
//
//            if(file == null) {
//                notification(Pos.BOTTOM_RIGHT, "Файлаа сонгоно уу?", "Файл сонгох");
//                notificationBuilder.darkStyle();
//                notificationBuilder.showWarning();
//            } else {
//                try {
//                    Connection conn = DBConnection.getConnection();
//                    Statement statement = conn.createStatement();
//                    ResultSet rsProduct = statement.executeQuery("SELECT p.name, p.bar_code, p.id, p.unit_price, t.name as type_name, p.weight, p.measure_unit, p.size, p.image\n" +
//                            "FROM products p, types t\n" +
//                            "WHERE p.type_id = t.id");
//
//                    XSSFWorkbook wb = new XSSFWorkbook();
//                    XSSFSheet sheet = wb.createSheet("Product Details");
//                    XSSFRow header = sheet.createRow(0);
//                    header.createCell(0).setCellValue("Name");
//                    header.createCell(1).setCellValue("Bar Code");
//                    header.createCell(2).setCellValue("ID");
//                    header.createCell(3).setCellValue("Unit Price");
//                    header.createCell(4).setCellValue("Type Name");
//                    header.createCell(5).setCellValue("Weight");
//                    header.createCell(6).setCellValue("Measure");
//                    header.createCell(7).setCellValue("Size");
//
//                    sheet.setColumnWidth(1, 256 * 25);
//                    sheet.setColumnWidth(2, 256 * 25);
//                    sheet.setColumnWidth(3, 256 * 25);
//                    sheet.setColumnWidth(4, 256 * 25);
//                    sheet.setColumnWidth(5, 256 * 25);
//                    sheet.setColumnWidth(6, 256 * 25);
//                    sheet.setColumnWidth(7, 256 * 25);
//                    sheet.setColumnWidth(8, 256 * 25);
//
//                    int index=1;
//
//                    new File(file.getPath().substring(0, (file.getPath().length() - 16)) + "productImage").mkdir();
//
//                    while (rsProduct.next()) {
//                        XSSFRow row = sheet.createRow(index);
//                        row.createCell(0).setCellValue(rsProduct.getString("name"));
//                        row.createCell(1).setCellValue(rsProduct.getInt("bar_code"));
//                        row.createCell(2).setCellValue(rsProduct.getInt("id"));
//                        row.createCell(3).setCellValue(rsProduct.getDouble("unit_price"));
//                        row.createCell(4).setCellValue(rsProduct.getString("type_name"));
//                        row.createCell(5).setCellValue(rsProduct.getInt("weight"));
//                        row.createCell(6).setCellValue(rsProduct.getString("measure_unit"));
//                        row.createCell(7).setCellValue(rsProduct.getInt("size"));
//
//                        index++;
//                        InputStream inputStream = rsProduct.getBinaryStream("image");
//                        Image image = new Image(inputStream);
//
//                        if(!image.isError()) {
//                            File imageFile = new File(file.getPath().substring(0, (file.getPath().length() - 16)) + "productImage\\" + rsProduct.getLong("id") + ".jpg");
//                            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
//                            ImageIO.write(bImage, "jpg", imageFile);
//                        }
//                    }
//
//                    FileOutputStream fileOut = new FileOutputStream(file);
//                    wb.write(fileOut);
//
//                    notification(Pos.BOTTOM_RIGHT, "Бүтээгдэхүүний мэдээллийг амжилттай татаж авлаа.", "Бүтээгдэхүүний мэдээлэл");
//                    notificationBuilder.darkStyle();
//                    notificationBuilder.showConfirm();
//
//                    fileOut.close();
//                    statement.close();
//                    rsProduct.close();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private void notification(Pos pos, String Text, String Text1) {
//        notificationBuilder = Notifications.create()
//                .title(Text1)
//                .text(Text)
//                .hideAfter(Duration.seconds(3))
//                .position(pos);
//    }
//
//    private void configuringFileChooser(FileChooser fileChooser) {
//        fileChooser.setTitle("Select File");
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("XLSX", "*.xlsx"),
//                new FileChooser.ExtensionFilter("XLS", "*.xls")
//        );
//    }
//}
