package com.achpay.wallet.model;

import java.io.Serializable;

public class SettingExpenseInfo implements Serializable {
    private String title;
    private String cashExpenseDes;
    private String cashExpenseHint;
    private String percentExpenseDes;
    private String percentExpenseHint;

    private boolean isCashExpense;
    private boolean isPercentExpense;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCashExpenseDes() {
        return cashExpenseDes;
    }

    public void setCashExpenseDes(String cashExpenseDes) {
        this.cashExpenseDes = cashExpenseDes;
    }

    public String getCashExpenseHint() {
        return cashExpenseHint;
    }

    public void setCashExpenseHint(String cashExpenseHint) {
        this.cashExpenseHint = cashExpenseHint;
    }

    public String getPercentExpenseDes() {
        return percentExpenseDes;
    }

    public void setPercentExpenseDes(String percentExpenseDes) {
        this.percentExpenseDes = percentExpenseDes;
    }

    public String getPercentExpenseHint() {
        return percentExpenseHint;
    }

    public void setPercentExpenseHint(String percentExpenseHint) {
        this.percentExpenseHint = percentExpenseHint;
    }

    public boolean isCashExpense() {
        return isCashExpense;
    }

    public void setCashExpense(boolean cashExpense) {
        isCashExpense = cashExpense;
    }

    public boolean isPercentExpense() {
        return isPercentExpense;
    }

    public void setPercentExpense(boolean percentExpense) {
        isPercentExpense = percentExpense;
    }
}
