/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * 类TxtFileIndexer.java的实现描述：
 * This class demonstrate the process of creating index with Lucene for text files  
 * @author yangshihong Mar 16, 2016 5:09:11 PM
 */
public class TxtFileIndexer { 
    private static String indexPath = "E:\\lucene\\luceneIndex";
    private static String docsPath = "E:\\lucene\\luceneData";

    /** Index all text files under a directory. */
    public static void main(String[] args) {
//      luceneIndex();
      searcher();
    }

    public static void luceneIndex() {

          boolean create = true;
          final Path docDir = Paths.get(docsPath);
          if (!Files.isReadable(docDir)) {
            System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
            System.exit(1);
          }
          
          Date start = new Date();
          try {
            System.out.println("Indexing to directory '" + indexPath + "'...");

            Directory dir = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

            if (create) {
              // Create a new index in the directory, removing any
              // previously indexed documents:
              iwc.setOpenMode(OpenMode.CREATE);
            } else {
              // Add new documents to an existing index:
              iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
            }

            // Optional: for better indexing performance, if you
            // are indexing many documents, increase the RAM
            // buffer.  But if you do this, increase the max heap
            // size to the JVM (eg add -Xmx512m or -Xmx1g):
            //
            // iwc.setRAMBufferSizeMB(256.0);

            IndexWriter writer = new IndexWriter(dir, iwc);
            indexDocs(writer, docDir);

            // NOTE: if you want to maximize search performance,
            // you can optionally call forceMerge here.  This can be
            // a terribly costly operation, so generally it's only
            // worth it when your index is relatively static (ie
            // you're done adding documents to it):
            //
            // writer.forceMerge(1);
            writer.close();

            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");

          } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
          }
    }
    
    
    public static void searcher() {
        IndexReader reader = null;
        try {
            // 1、创建Directory
            Directory directory = FSDirectory.open(Paths.get(indexPath));
            // 2、创建IndexReader用于读取索引
            reader = DirectoryReader.open(directory);
            // 3、根据IndexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);
            // 4、创建搜索的Query，这里使用常用的QueryParser来创建
            QueryParser parser = new QueryParser("contents", new StandardAnalyzer());
            // 搜索content域中包含hello的
            Query query = parser.parse("lucene");
            // 5、根据searcher搜索并且返回TopDocs
            TopDocs tds = searcher.search(query, 10);
            // 6、根据TopDocs获取ScoreDoc对象
            ScoreDoc[] sds = tds.scoreDocs;
            int count = 0;
            for (ScoreDoc sd : sds) {
                // 7、根据searcher和ScoreDoc对象获取具体的Document对象
                Document doc = searcher.doc(sd.doc);
                // 8、根据Document对象获取需要的值
                System.out.println("文件名：" + doc.get("name") + "\t" + "文件路径："
                        + doc.get("path"));
                count++;
            }
            System.out.println(count+ " document matching");
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            // 9、关闭reader
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Indexes the given file using the given writer, or if a directory is given,
     * recurses over files and directories found under the given directory.
     * 
     * NOTE: This method indexes one document per input file.  This is slow.  For good
     * throughput, put multiple documents into your input file(s).  An example of this is
     * in the benchmark module, which can create "line doc" files, one document per line,
     * using the
     * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
     * >WriteLineDocTask</a>.
     *  
     * @param writer Writer to the index where the given file/dir info will be stored
     * @param path The file to index, or the directory to recurse into to find files to index
     * @throws IOException If there is a low-level I/O error
     */
    public static void indexDocs(final IndexWriter writer, Path path)
            throws IOException {
        if (Files.isDirectory(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file,
                        BasicFileAttributes attrs) throws IOException {
                    try {
                        indexDoc(writer, file, attrs.lastModifiedTime()
                                .toMillis());
                    } catch (IOException ignore) {
                        // don't index files that can't be read.
                        System.out.println(ignore.getMessage());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }

    /** 
     * Indexes a single document
     */
    public static void indexDoc(IndexWriter writer, Path file, long lastModified)
            throws IOException {
        try (InputStream stream = Files.newInputStream(file)
                ) {
            // make a new, empty document
            Document doc = new Document();

            // Add the path of the file as a field named "path". Use a
            // field that is indexed (i.e. searchable), but don't tokenize
            // the field into separate words and don't index term frequency
            // or positional information:
            Field pathField = new StringField("path", file.toString(), Field.Store.YES);
            doc.add(pathField);
            
            // Add the last modified date of the file a field named "modified".
            // Use a LongField that is indexed (i.e. efficiently filterable with
            // NumericRangeFilter). This indexes to milli-second resolution,
            // which
            // is often too fine. You could instead create a number based on
            // year/month/day/hour/minutes/seconds, down the resolution you
            // require.
            // For example the long value 2011021714 would mean
            // February 17, 2011, 2-3 PM.
            doc.add(new LongField("modified", lastModified, Field.Store.NO));

            
            Field nameField = new StringField("name", file.getFileName().toString(), Field.Store.YES);
            doc.add(nameField);
            
            // Add the contents of the file to a field named "contents". Specify
            // a Reader,
            // so that the text of the file is tokenized and indexed, but not
            // stored.
            // Note that FileReader expects the file to be in UTF-8 encoding.
            // If that's not the case searching for special characters will
            // fail.
            doc.add(new TextField("contents", new BufferedReader(
                    new InputStreamReader(stream, StandardCharsets.UTF_8))));

            if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                // New index, so we just add the document (no old document can
                // be there):
                System.out.println("adding " + file);
                writer.addDocument(doc);
            } else {
                // Existing index (an old copy of this document may have been
                // indexed) so
                // we use updateDocument instead to replace the old one matching
                // the exact
                // path, if present:
                System.out.println("updating " + file);
                writer.updateDocument(new Term("path", file.toString()), doc);
            }
        }
    }
     
    
//  public static void learnLucene() throws IOException, FileNotFoundException {
//  //indexDir is the directory that hosts Lucene's index files 
//   File   indexDir = new File("E:\\lucene\\luceneIndex"); 
//   //dataDir is the directory that hosts the text files that to be indexed 
//   File   dataDir  = new File("E:\\lucene\\luceneData"); 
//   Analyzer luceneAnalyzer = new StandardAnalyzer(); 
//   File[] dataFiles  = dataDir.listFiles(); 
////IndexWriter indexWriter = new IndexWriter(indexDir,luceneAnalyzer,true); 
//   IndexWriter indexWriter = new IndexWriter(indexDir,luceneAnalyzer); 
//   
//   
////   Directory dir = FSDirectory.open(Paths.get(indexPath));
////   Analyzer analyzer = new StandardAnalyzer();
////   IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
//   
//   long startTime = new Date().getTime(); 
//   for(int i = 0; i < dataFiles.length; i++){ 
//        if(dataFiles[i].isFile() && dataFiles[i].getName().endsWith(".txt")){
//             System.out.println("Indexing file " + dataFiles[i].getCanonicalPath()); 
//             Document document = new Document(); 
//             Reader txtReader = new FileReader(dataFiles[i]);
//             document.add(Field.Text("path",dataFiles[i].getCanonicalPath())); 
//             document.add(Field.Text("contents",txtReader)); 
//             indexWriter.addDocument(document);
//        } 
//   } 
//   indexWriter.optimize(); 
//   indexWriter.close(); 
//   long endTime = new Date().getTime(); 
//      
//   System.out.println("It takes " + (endTime - startTime) 
//       + " milliseconds to create index for the files in directory "
//       + dataDir.getPath());
//} 
//

}
