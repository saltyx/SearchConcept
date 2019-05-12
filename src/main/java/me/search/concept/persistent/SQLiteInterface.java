package me.search.concept.persistent;

import me.search.concept.model.ConceptTableViewItem;
import me.search.concept.model.Concepts;

import java.util.List;

public interface SQLiteInterface {

    void queryAllConcepts(SQLite.QueryCallable<List<ConceptTableViewItem>> callable);
    void queryConceptsByStockCode(String stockCode, SQLite.QueryCallable<List<ConceptTableViewItem>> callable);
    void insertConcepts(List<Concepts> conceptsList, SQLite.ExecuteCallable<String> callable);
    void clearConcepts(SQLite.ExecuteCallable<String> callable);
}
