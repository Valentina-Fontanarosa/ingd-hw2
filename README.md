RELAZIONE DEL PROGETTO:

L’obiettivo del progetto è stato quello di realizzare un programma Java in grado di indicizzare i file .txt contenuti in una directory del laptop, al fine di effettuare successive ricerche utilizzando le librerie di Apache Lucene. In particolare, sono stati creati 2 indici, uno relativo al titolo del file, campo denominato “nomeFile”, e l’altro relativo al contenuto del file denominato “contenuto”. 

Per quanto riguarda l’indice relativo al titolo del file “nomeFile” si è ritenuto opportuno utilizzare un Analyzer Customizzato per ridurre e velocizzare i tempi di ricerca. 
In particolare, questo Analyzer utilizza CustomAnalyzer con un tokenizzatore WhitespaceTokenizerFactory e diversi filtri, di seguito indicato.

<img width="371" alt="image" src="https://github.com/user-attachments/assets/787a26ef-4afc-4ea0-9eff-772e3c739c11">

Per suddividere il testo in termini separati in base agli spazi bianchi, è stato utilizzato il tokenizzatore .withTokenizer(WhitespaceTokenizerFactory.class). 

Successivamente è stato aggiunto un filtro per convertire i termini in lettere minuscole al fine di garantire che le ricerche siano non case-sensitive, in modo che le maiuscole e minuscole vengano considerate uguali; nello specifico .addTokenFilter(LowerCaseFilterFactory.class). 

Infine, è stato aggiunto un filtro che esegue operazioni di divisione e unione di parole, inclusa la gestione della punteggiatura. Esso può essere utilizzato per rimuovere la punteggiatura e dividere o unire le parole in base alle configurazioni specificate. Nel caso specifico è stato utilizzato .addTokenFilter(WordDelimiterGraphFilterFactory.class).

L’utilizzo di tale analyzer ha fatto in modo che la ricerca di un documento possa essere effettuata indipendentemente dall’utilizzo delle lettere maiuscole o minuscole e dalla presenza o meno di punteggiatura nel titolo del testo.


Per quanto riguarda l’indice relativo al titolo del file “contenuto” si è ritenuto opportuno utilizzare uno StandardAnalyze. 

In particolare, l’analizzatore StandardAnalyzer in Apache Lucene è uno dei tokenizzatori che suddivide il testo in termini e applica diverse operazioni di analisi, tra cui la rimozione di stop words e la conversione dei termini in lettere minuscole. Per utilizzare una lista personalizzata di stop words è stato creato un CharArraySet contenente le stop words personalizzate:

<img width="336" alt="image" src="https://github.com/user-attachments/assets/6fc35769-2cd1-44b2-a17c-48fdbf23929c">


<img width="408" alt="image" src="https://github.com/user-attachments/assets/62612aae-10e0-4dc5-9b54-3de06699a559">

L’utilizzo di tale analyzer ha fatto in modo che la ricerca del contenuto di un documento sia più efficiente con tempi di esecuzione, in quanto l’utilizzo di stop words consente di effettuare la ricerca in modo rapido e indipendentemente dalla presenza di specifiche parole ritenute superflue (ad esempio la presenza di articoli, preposizioni aggettivi e congiunzioni)!
Per quanto concerne il numero di file indicizzati e i tempi di indicizzazione, si riportano di seguito le statistiche degli indici dei file e il tempo di esecuzione dell’indice.
I file utilizzati sono stati in totale 6 con caratteristiche simili, in quanto trattano dello stesso argomento e tale decisione è stata presa per verificare la corretta funzionalità dell’indicizzazione.
Nello specifico la sumTotalTermFreq rappresenta la somma delle frequenze totali dei termini nel campo "contenuto"oppure ”nomeFile”. In altre parole, rappresenta la somma delle frequenze di tutti i termini nel campo specifico in tutti i documenti.
Inoltre, il campo sumDocFreq rappresenta la somma delle frequenze dei termini nei documenti, in altre parole indica quanti documenti contengono i termini del campo Di seguito la rappresentazione:

 <img width="482" alt="image" src="https://github.com/user-attachments/assets/62a57d78-5971-4a43-9222-5b7751f24930">


Con riferimento al tempo di esecuzione dell’indice, il tempo medio totale è risultato pari a 912 ms.

<img width="141" alt="image" src="https://github.com/user-attachments/assets/19370b3c-662e-4cf8-8219-3fce7f8ffe60">

 
Se si vuole calcolare il tempo medio di indicizzazione per ogni file basta dividere tale tempo per 6. In conclusione, risulterà che l’indextime medio per ogni file è pari a 152ms, in proporzione al numero di termini presenti nel file. 

Di conseguenza se si vuole aumentare il numero di file .txt da indicizzare, il tempo totale risulterà maggiore poiché ci saranno più termini diversi nel documento.

Infine, le query usate per testare il sistema sono state diverse. Le più significative sono di seguito illustrate:

Test per verificare la correttezza del TermQuery:

<img width="419" alt="image" src="https://github.com/user-attachments/assets/c6c7fe74-6660-4d77-baa4-cd4f9dd29cfc">
 

Risultato:

<img width="255" alt="image" src="https://github.com/user-attachments/assets/2de45bb3-4687-4f04-9b95-e82e7fc256ee">


Questo test verifica una query di ricerca composta da termini con operatori booleani come "AND" e "NOT". 
L'obiettivo di questo test è verificare che la query restituisca i risultati attesi e che i risultati siano conformi alle specifiche della query.
Il risultato che è stato ottenuto è coerente con quanto ci si aspettava, ovvero sono stati trovati 2 documenti (Storia degli Etruschi e Storia dei Sumeri) in cui è stato cercato il termine “citta – stato ”, escludendo i documenti che contenevano la parola “grecia”, nel contenuto dei file.
Inoltre, si specifica che il numero indicato tra parentesi rappresenta quanto bene un documento corrisponde ai criteri specificati nella query di ricerca. Il punteggio è utilizzato per classificare i documenti recuperati in base alla loro rilevanza rispetto ai termini della query e ad altri fattori come la frequenza dei termini e la loro posizione nel documento. Più alto è il punteggio, più rilevante è considerato il documento rispetto alla query.

Test per verificare la correttezza del PhraseQuery:

 <img width="438" alt="image" src="https://github.com/user-attachments/assets/238696a9-c157-4385-91f7-1a21875497af">


Risultato:

<img width="267" alt="image" src="https://github.com/user-attachments/assets/34500c33-0725-4249-9b18-e0b3358becda">

 

Questo test invece verifica una query composta dall’operatore booleano “OR”.
Il risultato che è stato ottenuto è coerente con quanto ci si aspettava, ovvero è stato trovato un documento (Storia dei Babilonesi) in cui è stato cercato il termine “Babilonesi”, oppure il termine “romani”.

In conclusione, il progetto mi ha permesso di comprendere al meglio le librerie di Apache Lucene e gli analizzatori che sono costituiti principalmente da tokenizzatori e filtri mediante le loro diverse combinazioni al fine di realizzare l'indicizzazione e la ricerca di termini all’interno di documenti.

