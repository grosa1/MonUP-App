package pz_apps.monup.calculator;

import pz_apps.monup.main.NullValueException;

/**
 * Created by giiio on 10/12/2017.
 */

public class RevenueCalculator {

    public static final Float NO_VALUE = 0f;
    private Float currentUnitPrice;
    private Float expectedUnitPrice;
    private Float budget;
    private Float currentQuantity;
    private Float expectedQuantity;
    private Float grossResult;
    private Float revenue;

    public void setCurrentUnitPrice(Float currentUnitPrice) {
        this.currentUnitPrice = currentUnitPrice;
    }

    public void setExpectedUnitPrice(Float expectedUnitPrice) {
        this.expectedUnitPrice = expectedUnitPrice;
    }

    public void setBudget(Float budget) {
        this.budget = budget;
    }

    public Float getCurrentUnitPrice() throws NullValueException {
        if (this.currentUnitPrice == null) {
            throw new NullValueException();
        }
        return this.currentUnitPrice;
    }

    public Float getExpectedUnitPrice() throws NullValueException {
        if (this.expectedUnitPrice == null) {
            throw new NullValueException();
        }
        return this.expectedUnitPrice;
    }

    public Float getBudget() throws NullValueException {
        if (this.budget == null) {
            throw new NullValueException();
        }
        return this.budget;
    }

    public Float getCurrentQuantity() throws NullValueException {
        try {
            if (this.getCurrentUnitPrice() != 0) {
                this.currentQuantity = this.getBudget() / this.getCurrentUnitPrice();
            } else {
                this.currentQuantity = NO_VALUE;
            }

        } catch (NullValueException e) {
            e.printStackTrace();
            throw new NullValueException();
        }

        return this.currentQuantity;
    }


    public Float getGrossResult() throws NullValueException {
        try {
            this.grossResult = getCurrentQuantity() * getExpectedUnitPrice();

        } catch (NullValueException e) {
            e.printStackTrace();
            throw new NullValueException();
        }

        return this.grossResult;
    }

    public Float getExpectedQuantity() throws NullValueException {
        try {
            if (getExpectedUnitPrice() != 0) {
                this.expectedQuantity = getBudget() / getExpectedUnitPrice();
            } else {
                this.expectedQuantity = NO_VALUE;
            }
        } catch (NullValueException e) {
            e.printStackTrace();
            throw new NullValueException();
        }

        return this.expectedQuantity;
    }

    public Float getRevenue() throws NullValueException {

        try {
            if (getGrossResult() != 0) {
                this.revenue = getGrossResult() - getBudget();
            } else {
                this.revenue = NO_VALUE;
            }

        } catch (NullValueException e) {
            e.printStackTrace();
            throw new NullValueException();
        }

        return this.revenue;
    }
}