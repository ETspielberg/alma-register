package org.unidue.ub.unidue.almaregister.model.reports;

public class FinancialReport {

    private String fineFeeStatus;

    private String fineFeeType;

    private String paymentMethod;

    private String operatorLastName;

    private String transactionDate;

    private String originalAmount;

    public FinancialReport() {}

    public String getFineFeeStatus() {
        return fineFeeStatus;
    }

    public void setFineFeeStatus(String fineFeeStatus) {
        this.fineFeeStatus = fineFeeStatus;
    }

    public String getFineFeeType() {
        return fineFeeType;
    }

    public void setFineFeeType(String fineFeeType) {
        this.fineFeeType = fineFeeType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOperatorLastName() {
        return operatorLastName;
    }

    public void setOperatorLastName(String operatorLastName) {
        this.operatorLastName = operatorLastName;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }
}
