package sample.Database;

import com.sun.istack.internal.NotNull;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.Alert.AlertMaker;
import sample.Model.Customer;
import sample.Model.PurchaseBill;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelDatabaseHelper {

    public static boolean writeDBCustomer(@NotNull File dest) {
        boolean okay = false;
        try {
            String FILE_NAME = dest.getAbsolutePath();

            XSSFWorkbook workbook = getWorkBook(dest);
            XSSFSheet sheet;

            try {
                sheet = workbook.getSheet("Customer");
            } catch (Exception e) {
                sheet = workbook.getSheet("History");
            }

            boolean b = true;

            for (Row currentRow : sheet) {
                String n = "", p = "", g = "", a1 = "", a2 = "", a3 = "", z = "", id = "";
                if (b) {
                    b = false;
                    continue;
                }
                if (currentRow.getCell(0) != null) {
                    currentRow.getCell(0).setCellType(CellType.STRING);
                    n = currentRow.getCell(0).getStringCellValue();
                }
                if (currentRow.getCell(1) != null) {
                    currentRow.getCell(1).setCellType(CellType.STRING);
                    p = currentRow.getCell(1).getStringCellValue();
                }
                if (currentRow.getCell(2) != null) {
                    currentRow.getCell(2).setCellType(CellType.STRING);
                    g = currentRow.getCell(2).getStringCellValue();
                }
                if (currentRow.getCell(3) != null) {
                    currentRow.getCell(3).setCellType(CellType.STRING);
                    a1 = currentRow.getCell(3).getStringCellValue();
                }
                if (currentRow.getCell(4) != null) {
                    a2 = currentRow.getCell(4).getStringCellValue();
                }
                if (currentRow.getCell(5) != null) {
                    a3 = "" + currentRow.getCell(5).getStringCellValue();
                }
                if (currentRow.getCell(6) != null) {
                    z = "" + currentRow.getCell(6).getStringCellValue();
                }
                if (currentRow.getCell(7) != null) {
                    id = "" + currentRow.getCell(7).getStringCellValue();
                }
                DatabaseHelper_Cutomer.insertNewCustomer(new Customer(n, p, g, a1, a2, a3, z, id));
            }

            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);

            outputStream.close();
            workbook.close();

            okay = true;

        } catch (Exception e) {
            AlertMaker.showErrorMessage(e);
        }
        return okay;
    }

    public static boolean writeDBPurchaseBill(@NotNull File dest) {
        boolean okay = false;
        try {
            String FILE_NAME = dest.getAbsolutePath();
            getWorkBook(dest);
            XSSFWorkbook workbook = getWorkBook(dest);
            Sheet sheet = null;
            try {
                sheet = workbook.getSheet("PURCHASE BILLS");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            PurchaseBill purchaseBill;
            Date date1 = new Date();
            boolean b = true;
            assert sheet != null;
            for (Row currentRow : sheet) {
                String cmpName = "", invoice = "", amount = "", _12 = "", _18 = "", _28 = "", totalNet = "", sendToAuditor = "";
                String date;
                if (b) {
                    b = false;
                    continue;
                }
                if (currentRow.getCell(0) != null) {

                    switch (currentRow.getCell(0).getCellType()) {
                        case STRING: {
                            date = currentRow.getCell(0).getStringCellValue();
                            try {
                                date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                            } catch (Exception e) {
                                try {
                                    date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
                                } catch (Exception e1) {
                                    System.out.println(e1.getMessage());
                                }
                            }

                            break;
                        }
                        case NUMERIC: {
                            date1 = currentRow.getCell(0).getDateCellValue();
                            break;
                        }
                    }
                    try {
                        System.out.println(date1.getTime());
                    } catch (Exception e1) {
                        System.out.println(e1.getMessage());
                    }
                }
                if (currentRow.getCell(1) != null) {
                    try {
                        switch (currentRow.getCell(1).getCellType()) {
                            case STRING: {
                                cmpName = currentRow.getCell(1).getStringCellValue();
                                break;
                            }
                            case NUMERIC: {
                                cmpName = "" + currentRow.getCell(1).getNumericCellValue();
                                break;
                            }
                        }
                        cmpName = currentRow.getCell(1).getStringCellValue();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (currentRow.getCell(2) != null) {
                    try {
                        switch (currentRow.getCell(2).getCellType()) {
                            case STRING: {
                                invoice = currentRow.getCell(2).getStringCellValue();
                                break;
                            }
                            case NUMERIC: {
                                currentRow.getCell(2).setCellType(CellType.STRING);
                                break;
                            }
                        }
                        invoice = currentRow.getCell(2).getStringCellValue();

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (currentRow.getCell(3) != null) {
                    try {
                        switch (currentRow.getCell(3).getCellType()) {
                            case STRING: {
                                amount = currentRow.getCell(3).getStringCellValue();
                                break;
                            }
                            case NUMERIC: {
                                amount = "" + currentRow.getCell(3).getNumericCellValue();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (currentRow.getCell(4) != null) {
                    try {
                        switch (currentRow.getCell(4).getCellType()) {
                            case STRING: {
                                _12 = currentRow.getCell(4).getStringCellValue();
                                break;
                            }
                            case NUMERIC: {
                                _12 = "" + currentRow.getCell(4).getNumericCellValue();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (currentRow.getCell(5) != null) {
                    try {
                        switch (currentRow.getCell(5).getCellType()) {
                            case STRING: {
                                _18 = currentRow.getCell(5).getStringCellValue();
                                break;
                            }
                            case NUMERIC: {
                                _18 = "" + currentRow.getCell(5).getNumericCellValue();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (currentRow.getCell(6) != null) {
                    try {
                        switch (currentRow.getCell(6).getCellType()) {
                            case STRING: {
                                _28 = currentRow.getCell(6).getStringCellValue();
                                break;
                            }
                            case NUMERIC: {
                                _28 = "" + currentRow.getCell(6).getNumericCellValue();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (currentRow.getCell(7) != null) {
                    try {
                        switch (currentRow.getCell(7).getCellType()) {
                            case STRING: {
                                totalNet = currentRow.getCell(7).getStringCellValue();
                                break;
                            }
                            case NUMERIC: {
                                totalNet = "" + currentRow.getCell(7).getNumericCellValue();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (currentRow.getCell(8) != null) {
                    try {
                        switch (currentRow.getCell(8).getCellType()) {
                            case STRING: {
                                sendToAuditor = currentRow.getCell(8).getStringCellValue();
                                break;
                            }
                            case NUMERIC: {
                                sendToAuditor = "" + currentRow.getCell(8).getNumericCellValue();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                purchaseBill = new PurchaseBill("" + date1.getTime(), cmpName, invoice, amount, _12, _18, _28, totalNet, sendToAuditor);
                try {
                    DatabaseHelper_PurchaseBill.insertNewPurchaseBill(purchaseBill);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
            okay = true;
        } catch (Exception e) {
            AlertMaker.showErrorMessage(e);
        }
        return okay;
    }

    static XSSFWorkbook getWorkBook(@NotNull File dest) {
        FileInputStream excel;
        XSSFWorkbook workbook;
        try {
            excel = new FileInputStream(dest);
            workbook = new XSSFWorkbook(excel);
        } catch (Exception e) {
            workbook = new XSSFWorkbook();
        }
        return workbook;
    }
}
