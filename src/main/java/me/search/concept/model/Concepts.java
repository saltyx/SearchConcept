package me.search.concept.model;

import me.search.concept.util.SQLiteUtil;


/**
 * concepts info
 */
public class Concepts implements SQLiteModel {

    private String conceptCode;
    private String conceptName;
    private String stockCode;
    private String updatedTime;

    public String getConceptCode() {
        return conceptCode;
    }

    public void setConceptCode(String conceptCode) {
        this.conceptCode = conceptCode;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public String toInsertSql() {
        return String.format("insert into stock_concept(concept_code, concept_name, " +
                             "stock_code, updated_time, create_time) values ('%s', '%s', " +
                             "'%s', '%s',%d);", this.conceptCode, this.conceptName,
                             this.stockCode, this.updatedTime, SQLiteUtil.today());
    }

    @Override
    public String toString() {
        return "Concepts{" +
                "conceptCode='" + conceptCode + '\'' +
                ", conceptName='" + conceptName + '\'' +
                ", stockCode='" + stockCode + '\'' +
                ", updatedTime='" + updatedTime + '\'' +
                '}';
    }
}
