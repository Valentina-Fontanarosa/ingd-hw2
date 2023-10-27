package hw2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;

import org.apache.lucene.index.*;

import java.io.File;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.*;

public class IndexBuilder {

    private String indexPath;
    private String dataPath;
    private IndexWriter writer;
    private long elapsedTime = 0;

    public IndexBuilder(String _indexPath, String _dataPath){
        this.indexPath = _indexPath;
        this.dataPath = _dataPath;
    }
    public void create_index(Map<String, Analyzer> perFieldAnalyzer, Codec codec) {
        // Avvia il timer
        long startTime = new Date().getTime();

        Analyzer defaultAnalyzer = new StandardAnalyzer();

        // Creazione di un indice in una directory specifica
        try (Directory dir_index = FSDirectory.open(Paths.get(this.indexPath))) {

            // Creazione di un analyzer per il nome del file e il contenuto del file
            //Analyzer nameAnalyzer = new StandardAnalyzer();
            //Analyzer contentAnalyzer = new StandardAnalyzer();

            Analyzer analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, perFieldAnalyzer);

            // Configurazione dell'IndexWriter
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setCodec( new SimpleTextCodec());

            if (codec != null) {
                config.setCodec(codec);
            }

            this.writer = new IndexWriter(dir_index, config);

            this.writer.deleteAll();

            // Indicizza i file .txt nella directory specificata
            File dir_data = new File(this.dataPath);

            if (dir_data.exists() && dir_data.isDirectory()) {
                File[] files = dir_data.listFiles();

                if (files != null) {
                    //System.out.println(Arrays.stream(files).toList().toString());
                    for (File file : files) {
                        if (file.isFile() && file.getName().toLowerCase().endsWith(".txt"))
                            indexFiles(file);
                    }
                }
            }

            // Chiudi l'IndexWriter
            this.writer.close();
            dir_index.close();

            // Ferma il timer
            long endTime = new Date().getTime();
            // Calcola il tempo trascorso in millisecondi
            this.elapsedTime = endTime - startTime;

            System.out.println("\nIndicizzazione completata con successo.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void indexFiles(File file) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    indexFiles(f);
                }
            }
        } else {

            Document doc = new Document();
            doc.add(new TextField("nomeFile", file.getName(), Field.Store.YES));

            // Legge il contenuto del file .txt
            String fileContent = String.join(System.lineSeparator(),
                    Files.readAllLines(
                            Paths.get(this.dataPath +"/" + file.getName()), StandardCharsets.UTF_8));

            // Aggiunge il contenuto del file all'indice
            doc.add(new TextField("contenuto", fileContent, Field.Store.YES));

            // Aggiunge il documento all'indice
            this.writer.addDocument(doc);

            // Salvataggio dei dati sul disco
            this.writer.commit();
        }
    }

    public void statistiche(){
        try (Directory dir_index = FSDirectory.open(Paths.get(this.indexPath))) {

            try (IndexReader reader = DirectoryReader.open(dir_index)) {
                IndexSearcher searcher = new IndexSearcher(reader);

                Collection<String> indexedFields = FieldInfos.getIndexedFields(reader);

                //FieldInfos fieldInfos = FieldInfos.getMergedFieldInfos(reader);

                for (String field : indexedFields) {
                    System.out.println(searcher.collectionStatistics(field));
                }
            } finally {
                dir_index.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("indexTime(ms)=" + elapsedTime);
    }

   public boolean isField(String campo){
       // torna vero se il campo è presente negli indici
       boolean ritorno = false;
       try {
           IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(this.indexPath)));
           FieldInfos fieldInfos = FieldInfos.getMergedFieldInfos(reader);
           for (FieldInfo fieldInfo : fieldInfos) {
               System.out.println(fieldInfo.name);
               if (fieldInfo.name.equals(campo)) {
                   ritorno = true;
                   break;
               }
           }

           reader.close();
       } catch (Exception e) {
           e.printStackTrace();
       }

       return ritorno;
   }
}