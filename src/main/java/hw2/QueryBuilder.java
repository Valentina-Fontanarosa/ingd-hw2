package hw2;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.IndexSearcher;

import java.io.IOException;

import java.nio.file.Paths;
import java.util.Date;

public class QueryBuilder {

    private String indexPath;
    private boolean DEBUG = false;
    private long elapsedTime = 0;
    public QueryBuilder( String _indexPath){
        this.indexPath = _indexPath;
    }

    public void setDebug (boolean _debug) {this.DEBUG = _debug;}

    public void lanciaQuery(String input) throws ParseException {
        Query luceneQuery = buildLuceneQuery(input);

        System.out.println("\nLucene Query => " + luceneQuery);

        try (Directory directory = FSDirectory.open(Paths.get(this.indexPath))) {
            try (IndexReader reader = DirectoryReader.open(directory)) {

                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher, luceneQuery);

            } finally {
                directory.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runQuery(IndexSearcher searcher, Query query) throws IOException {
        runQuery(searcher, query, false);
    }
    private void runQuery(IndexSearcher searcher, Query query,boolean explain) throws IOException {
        // Avvia il timer
        long startTime = new Date().getTime();

        TopDocs hits = searcher.search(query, 10);

        // Ferma il timer
        long endTime = new Date().getTime();
        // Calcola il tempo trascorso in millisecondi
        this.elapsedTime = endTime - startTime;

        System.out.println("Numero documenti trovati:" + hits.scoreDocs.length);
        System.out.println("Tempi di ricerca(ms):" + this.elapsedTime);

        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println("doc" + scoreDoc.doc + ":" + doc.get("nomeFile") + " (" + scoreDoc.score + ")");
            //Visualizza l'intero documento
            if (DEBUG) System.out.println(doc.getFields());

            if (explain) {
                Explanation explanation = searcher.explain(query, scoreDoc.doc);
                System.out.println(explanation);
            }
        }
    }

    private Query buildLuceneQuery(String input) {
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

        String[] terms = input.split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String fieldName = terms[0]; // Estrai il nome del campo dalla prima parola
        String operator = "SHOULD"; // Default operator is OR
        System.out.println("");

        for (int i = 1; i < terms.length; i++) {
            String term = terms[i];
            if (term.equals("AND")) {
                operator = "MUST";
            } else if (term.equals("OR")) {
                operator = "SHOULD";
            } else if (term.equals("NOT")) {
                operator = "MUST_NOT";
            } else {
                Query query = createQueryFromTerm(fieldName, term.toLowerCase());
                booleanQuery.add(new BooleanClause(query, getBooleanClause(operator)));
            }
        }

        return booleanQuery.build();
    }

    private Query createQueryFromTerm(String fieldName, String term) {
        if (DEBUG) System.out.println("<"+term+">");

        if (term.startsWith("\"") && term.endsWith("\"")) {
            // Phrase query
            if (DEBUG) System.out.println("\t<PhraseQuery>");
            String phrase = term.substring(1, term.length() - 1);
            String[] words = phrase.split(" ");
            PhraseQuery.Builder phraseQuery = new PhraseQuery.Builder();
            for (String word : words) {
                phraseQuery.add(new Term(fieldName, word));
                if (DEBUG) System.out.println("\t\t<"+word+">");
            }
            if (DEBUG) System.out.println("\t<\\PhraseQuery>");
            return phraseQuery.build();
        } else {
            // Term query
            if (DEBUG) System.out.println("\t<TermQuery>\n\t\t<"+term+">\n\t<\\TermQuery>");
            return new TermQuery(new Term(fieldName, term));
        }
    }

    private BooleanClause.Occur getBooleanClause(String operator) {
        switch (operator) {
            case "MUST":
                return BooleanClause.Occur.MUST;
            case "MUST_NOT":
                return BooleanClause.Occur.MUST_NOT;
            case "SHOULD":
            default:
                return BooleanClause.Occur.SHOULD;
        }
    }

}
