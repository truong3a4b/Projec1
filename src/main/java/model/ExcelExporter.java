package model; // Khai báo package của lớp ExcelExporter, giúp tổ chức mã nguồn.

import org.apache.poi.ss.usermodel.*; // Import các lớp liên quan để làm việc với bảng tính (Workbook, Sheet, Row, Cell,...).
import org.apache.poi.xssf.usermodel.XSSFWorkbook; // Import lớp dùng để làm việc với định dạng Excel .xlsx.

import java.io.FileOutputStream; // Import lớp để ghi dữ liệu ra file.
import java.io.IOException; // Import lớp để xử lý ngoại lệ I/O.

public class ExcelExporter { // Định nghĩa lớp ExcelExporter.

    // Phương thức tĩnh xuất báo cáo ra file Excel.
    public static void exportReportToExcel(Report report, String filePath) {
        Workbook workbook = new XSSFWorkbook(); // Tạo một workbook mới (file Excel) ở định dạng .xlsx.
        Sheet sheet = workbook.createSheet("Report"); // Tạo một sheet mới trong workbook với tên "Report".

        // Tạo tiêu đề cho bảng.
        String[] headers = {"Name", "Method", "Engine Name", "Category", "Result"}; // Danh sách các tiêu đề cột.
        Row headerRow = sheet.createRow(0); // Tạo dòng đầu tiên của sheet để chứa tiêu đề.

        // Duyệt qua từng tiêu đề trong mảng headers.
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i); // Tạo một ô trong dòng tiêu đề.
            cell.setCellValue(headers[i]); // Gán giá trị (tiêu đề) cho ô.

            // Tạo style cho tiêu đề.
            CellStyle style = workbook.createCellStyle(); // Tạo một đối tượng CellStyle để định dạng ô.
            Font font = workbook.createFont(); // Tạo một đối tượng Font để định dạng chữ.
            font.setBold(true); // Đặt font chữ in đậm.
            style.setFont(font); // Áp dụng font cho style.
            cell.setCellStyle(style); // Áp dụng style cho ô.
        }

        int rowNum = 1; // Bắt đầu từ dòng thứ 2 (dòng đầu tiên là tiêu đề).

        // Duyệt qua danh sách kết quả từ đối tượng Report.
        for (ResultAnalysis result : report.getResults()) {
            Row row = sheet.createRow(rowNum++); // Tạo một dòng mới cho mỗi kết quả và tăng số dòng.

            // Tạo và gán giá trị cho từng ô trong dòng dựa trên thuộc tính của result.
            row.createCell(0).setCellValue(result.getName()); // Ghi tên.
            row.createCell(1).setCellValue(result.getMethod()); // Ghi phương thức.
            row.createCell(2).setCellValue(result.getEngine_name()); // Ghi tên công cụ.
            row.createCell(3).setCellValue(result.getCategory()); // Ghi danh mục.
            row.createCell(4).setCellValue(result.getResult()); // Ghi kết quả.
        }

        // Tự động điều chỉnh kích thước cột dựa trên nội dung.
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i); // Căn chỉnh kích thước cột để phù hợp với nội dung.
        }

        // Ghi workbook ra file tại đường dẫn filePath.
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut); // Ghi nội dung workbook vào file.
            System.out.println("Tệp Excel đã được xuất thành công tại: " + filePath); // Thông báo ghi file thành công.
        } catch (IOException e) { // Bắt ngoại lệ nếu có lỗi khi ghi file.
            e.printStackTrace(); // In lỗi ra console.
        } finally { // Đảm bảo workbook được đóng sau khi ghi file.
            try {
                workbook.close(); // Đóng workbook để giải phóng tài nguyên.
            } catch (IOException e) { // Bắt lỗi nếu không đóng được workbook.
                e.printStackTrace(); // In lỗi ra console.
            }
        }
    }
}
