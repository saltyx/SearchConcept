package me.search.concept;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import me.search.concept.model.ConceptTableViewItem;
import me.search.concept.model.Concepts;
import me.search.concept.persistent.SQLite;
import me.search.concept.util.LogUtil;
import me.search.concept.util.SQLiteUtil;
import me.search.concept.util.http.ApiHttpClient;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testIntDay() {
        assertEquals(SQLiteUtil.today(),  20190512);
    }

    @Test
    public void testGenerateInsertSQL() {
        Concepts concepts = new Concepts();
        concepts.setConceptCode("123123");
        concepts.setConceptName("aaaa");
        concepts.setStockCode("aaaaaa");
        concepts.setUpdatedTime("20190---");
        LogUtil.info(concepts.toInsertSql());
    }

    @Test
    public void testHttpClient() throws IOException {

    }

    @Test
    public void testSQL() throws Exception {
        SQLite.getInstance().queryAllConcepts(new SQLite.QueryCallable<List<ConceptTableViewItem>>() {
            @Override
            public void onDataAvailable(List<ConceptTableViewItem> value) {

            }

            @Override
            public void onFail(String error, Exception e) {
                e.printStackTrace();
            }
        });
    }
}
