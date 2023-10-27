package hw2;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.jupiter.api.Test;

public class TestQuery {

    @Test
    public void testPhraseQuery(){
        QueryBuilder query = new QueryBuilder("Index");

        try {
            query.lanciaQuery("contenuto \"Guerre Persiane\"");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPhraseANDQuery(){
        QueryBuilder query = new QueryBuilder("Index");

        try {
            query.lanciaQuery("contenuto \"Guerre Persiane\" AND \"Impero Bizantino\"");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testPhraseORQuery(){
        QueryBuilder query = new QueryBuilder("Index");

        try {
            query.lanciaQuery("contenuto \"Babilonesi\" OR \"romani\"");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTermAND_NOTQuery(){
        QueryBuilder query = new QueryBuilder("Index");

        try {
            query.lanciaQuery("contenuto città AND stato NOT grecia");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testThermQuery(){
        QueryBuilder query = new QueryBuilder("Index");

        try {
            query.lanciaQuery("nomeFile Storia");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testThermANDQuery(){
        QueryBuilder query = new QueryBuilder("Index");

        try {
            query.lanciaQuery("nomeFile Storia AND Roma");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testThermORQuery(){
        QueryBuilder query = new QueryBuilder("Index");

        try {
            query.lanciaQuery("nomeFile Storia OR Roma");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testThermNOTQuery(){
        QueryBuilder query = new QueryBuilder("Index");

        try {
            query.lanciaQuery("nomeFile Storia NOT Roma");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
