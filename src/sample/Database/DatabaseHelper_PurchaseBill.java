package sample.Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Alert.AlertMaker;
import sample.Model.PurchaseBill;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DatabaseHelper_PurchaseBill {

    private static String tableName = "PURCHASEBILLS";

    public static boolean insertNewPurchaseBill(PurchaseBill purchaseBill) {
        PreparedStatement preparedStatement = null;
        boolean okay = false;
        try {
            String query = String.format("INSERT INTO %s (DATE , CompanyName ," +
                    " INVOICE , AmountBeforeTax , TwelvePerAmt , EighteenPerAmt ," +
                    " TwentyEightPerAmt , AmountAfterTax,HasGoneToAuditor) VALUES (?,?,?,?,?,?,?,?,?)", tableName);
            preparedStatement = DatabaseHandler.getInstance().getConnection().prepareStatement(query);
            preparedStatement.setString(1, purchaseBill.getDateInLong());
            preparedStatement.setString(2, purchaseBill.getCompanyName());
            preparedStatement.setString(3, purchaseBill.getInvoiceNo());
            preparedStatement.setString(4, purchaseBill.getAmountBeforeTax());
            preparedStatement.setString(5, purchaseBill.getTwelve());
            preparedStatement.setString(6, purchaseBill.getEighteen());
            preparedStatement.setString(7, purchaseBill.getTwentyEight());
            preparedStatement.setString(8, purchaseBill.getTotalAmount());
            preparedStatement.setString(9, purchaseBill.getHasSentToAuditor());
            okay = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert preparedStatement != null;
                preparedStatement.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return okay;
    }

    public static boolean deletePurchaseBill(PurchaseBill purchaseBill) {
        boolean okay = false;
        PreparedStatement preparedStatement = null;
        try {
            String delete = String.format("DELETE FROM %s WHERE INVOICE = ? AND CompanyName =? ", tableName);
            preparedStatement = DatabaseHandler.getInstance().getConnection().prepareStatement(delete);
            preparedStatement.setString(1, purchaseBill.getInvoiceNo());
            preparedStatement.setString(2, purchaseBill.getCompanyName());
            okay = preparedStatement.executeUpdate() > 0;
            preparedStatement.close();
        } catch (SQLException e) {
            AlertMaker.showErrorMessage(e);
            e.printStackTrace();
        } finally {
            try {
                assert preparedStatement != null;
                preparedStatement.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return okay;
    }

    public static boolean updatePurchaseBill(PurchaseBill purchaseBill) {
        boolean okay = true;
        PreparedStatement preparedStatement;

        String updateQuery = " UPDATE " + tableName + " SET DATE = ?, CompanyName = ?, " +
                " AmountBeforeTax = ?, TwelvePerAmt = ? , EighteenPerAmt = ?," +
                " TwentyEightPerAmt = ?, AmountAfterTax = ? , HasGoneToAuditor = ? WHERE INVOICE = ?";
        try {
            preparedStatement = DatabaseHandler.getInstance().getConnection().prepareStatement(updateQuery);
            preparedStatement.setString(1, purchaseBill.getDateInLong());
            preparedStatement.setString(2, purchaseBill.getCompanyName());
            preparedStatement.setString(3, purchaseBill.getAmountBeforeTax());
            preparedStatement.setString(4, purchaseBill.getTwelve());
            preparedStatement.setString(5, purchaseBill.getEighteen());
            preparedStatement.setString(6, purchaseBill.getTwentyEight());
            preparedStatement.setString(7, purchaseBill.getTotalAmount());
            preparedStatement.setString(8, purchaseBill.getHasSentToAuditor());
            preparedStatement.setString(9, purchaseBill.getInvoiceNo());

            okay = preparedStatement.executeUpdate() > 0;

        } catch (Exception e) {
            AlertMaker.showErrorMessage(e);
        }
        return okay;
    }

    public static ObservableList<PurchaseBill> getAllPurchaseBillList() {
        ObservableList<PurchaseBill> bills = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = getResultSet();
            while (resultSet.next()) bills.add(getPurchaseBill(resultSet));
        } catch (SQLException e) {
            AlertMaker.showErrorMessage(e);
        }
        return bills;
    }

    public static ObservableList<PurchaseBill> getPurchaseBillList(String text) {
        String getQuery = " SELECT * FROM " + tableName + " WHERE INVOICE LIKE ?";
        return getResultSetSearchByString(getQuery, text);
    }

    public static ObservableList<PurchaseBill> getPurchaseBillList(String companyName, LocalDate a, LocalDate b) {
        String getQuery = " SELECT * FROM " + tableName + " WHERE CompanyName LIKE ?";
        ObservableList<PurchaseBill> bills = FXCollections.observableArrayList();
        for (PurchaseBill bill : getResultSetSearchByString(getQuery, companyName)) {
            try {
                long l = Long.parseLong(bill.getDateInLong());
                Date date = new Date(l);
                LocalDate d = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if ((d.isEqual(a) || d.isEqual(b)) || (d.isAfter(a) && d.isBefore(b)))
                    bills.add(bill);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return bills;
    }

    public static ObservableList<PurchaseBill> getPurchaseBillListByCompanyName(String text) {
        String getQuery = " SELECT * FROM " + tableName + " WHERE CompanyName LIKE ?";
        return getResultSetSearchByString(getQuery, text);
    }

    public static ObservableList<PurchaseBill> getPurchaseBillList(LocalDate a, LocalDate b) {
        ObservableList<PurchaseBill> bills = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = getResultSet();
            assert resultSet != null;
            while (resultSet.next()) {
                try {
                    long l = Long.parseLong(resultSet.getString(1));
                    Date date = new Date(l);
                    LocalDate d = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    if ((d.isEqual(a) || d.isEqual(b)) || (d.isAfter(a) && d.isBefore(b)))
                        bills.add(getPurchaseBill(resultSet));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            AlertMaker.showErrorMessage(e);
        }
        return bills;
    }

    private static PurchaseBill getPurchaseBill(ResultSet resultSet) throws SQLException {
        return new PurchaseBill(resultSet.getString(1)
                , resultSet.getString(2)
                , resultSet.getString(3)
                , resultSet.getString(4)
                , resultSet.getString(5)
                , resultSet.getString(6)
                , resultSet.getString(7)
                , resultSet.getString(8)
                , resultSet.getString(9));
    }

    private static ObservableList<PurchaseBill> getResultSetSearchByString(String getQuery, String text) {
        ObservableList<PurchaseBill> bills = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = DatabaseHandler.getInstance().getConnection().prepareStatement(getQuery);
            preparedStatement.setString(1, text);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) bills.add(getPurchaseBill(resultSet));
        } catch (SQLException e) {
            AlertMaker.showErrorMessage(e);
        } finally {
            try {
                assert preparedStatement != null;
                preparedStatement.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return bills;
    }

    private static ResultSet getResultSet() throws SQLException {
        PreparedStatement preparedStatement;
        String getQuery = " SELECT * FROM " + tableName + " WHERE TRUE";
        preparedStatement = DatabaseHandler.getInstance().getConnection().prepareStatement(getQuery);
        return preparedStatement.executeQuery();
    }

    public static boolean markBillAsGoneToAuditor(PurchaseBill purchaseBill) {
        boolean okay = true;
        PreparedStatement preparedStatement;
        String updateQuery = " UPDATE " + tableName + " SET HasGoneToAuditor = ? WHERE INVOICE = ? AND CompanyName = ? ";
        try {
            preparedStatement = DatabaseHandler.getInstance().getConnection().prepareStatement(updateQuery);
            preparedStatement.setString(1, "true");
            preparedStatement.setString(2, purchaseBill.getInvoiceNo());
            preparedStatement.setString(3, purchaseBill.getCompanyName());
            okay = preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            AlertMaker.showErrorMessage(e);
        }
        return okay;
    }
}
