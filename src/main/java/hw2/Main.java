package hw2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.queryparser.classic.ParseException;


import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws ParseException {

        CharArraySet stopWords = new CharArraySet(Arrays.asList(
                "in", "nel", "nelle", "nella",
                "dei", "di", "da", "dalla", "del", "delle",
                "con", "ma", "come", "cui",
                "un", "una", "uno",
                "e", "o", "a", "che", "al", "ed",
                "i", "il", "la", "le", "gli", "alla", "alle",
                "sua", "suo", "sue", "suoi", "si", "sulla"
                ), true);

        Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();

        try {
            Analyzer mioAnalyzer = CustomAnalyzer.builder()
                    .withTokenizer(WhitespaceTokenizerFactory.class)
                    .addTokenFilter(LowerCaseFilterFactory.class)
                    .addTokenFilter(WordDelimiterGraphFilterFactory.class)
                    .build();
            perFieldAnalyzers.put("nomeFile", mioAnalyzer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        perFieldAnalyzers.put("contenuto", new StandardAnalyzer(stopWords));


        // crea file di indice
        IndexBuilder indice = new IndexBuilder("Index","Text");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Vuoi creare gli indici? [s/n]: ");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("s"))
            indice.create_index(perFieldAnalyzers, new SimpleTextCodec());

        // visualizza le statistiche degli indici
        System.out.println("\nStatistiche dei file indici:");
        indice.statistiche();

        System.out.println("\nPer cercare nei documenti specifiche chiavi inserisci una query: \nla prima parola deve indicare" +
                " il nome dell'indice (nomeFile o contenuto) " +
                "seguita da una sequenza di termini eventualmente racchiusi tra virgolette. \nTra i termini da cercare " +
                "nei File.txt puoi utilizzare gli operatori booleani: AND, OR oppure NOT per esprimere una phrase query");

        System.out.println("Inserisci la tua query: ");

        //legge una sequenza di caratteri da tastiera
        BufferedReader readerConsole = new BufferedReader(new InputStreamReader(System.in));
        String queryString = null;
        try {
            queryString = readerConsole.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //String queryString = "contenuto romano NOT Roma";
        QueryBuilder query = new QueryBuilder("Index");

        // abilita i messaggi di debug
        query.setDebug(false);

        query.lanciaQuery(queryString);

    }
}
