package com.puyixiaowo.fblog.utils;

import com.puyixiaowo.fblog.bean.ArticleBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Moses
 * @date 2017-09-13 17:01
 * 
 */
public class LuceneIndexUtils {
    private static final Path path = Paths.get(System.getProperty("user.dir") + "/lucene/indexs");
    private static Analyzer analyzer = new JcsegAnalyzer(JcsegTaskConfig.COMPLEX_MODE);


    public static IndexWriter getIndexWriter() throws Exception {
        IndexWriter writer = null;

        // 把lucene的索引文件保存到机器的磁盘
        Directory dirWrite = FSDirectory.open(path);
        // 初始化写入配置
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);// 创建模式 OpenMode.CREATE_OR_APPEND 添加模式
        //如果是CREATE ,每次都会重新创建这个索引，清空以前的数据，如果是append 每次都会追加，之前的不删除
        //在日常的需求索引添加中，一般都是 APPEND 持续添加模式
        writer = new IndexWriter(dirWrite, iwc);
        return writer;
    }

    public static void addLuceneIndex(ArticleBean articleBean) throws Exception {
        IndexWriter writer = getIndexWriter();

        Document doc = new Document();

        // 对字段建立索引
        doc.add(new Field("id", articleBean.getId() + "", new FieldType(TextField.TYPE_STORED)));
        doc.add(new Field("title", articleBean.getTitle(), new FieldType(TextField.TYPE_STORED)));
        doc.add(new Field("context", articleBean.getContext(), new FieldType(TextField.TYPE_STORED)));
        writer.addDocument(doc);
        writer.close(); // 关闭读写器
    }

    public static void removeLuceneIndex() {
    }

    public static List<ArticleBean> search(PageBean pageBean,
                              String queries) throws Exception {
        List<ArticleBean> list = new ArrayList<>();
        Directory dir = FSDirectory.open(path);
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        String[] fields = {"title", "context"};

        QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
        Query query = queryParser.parse(queries);

        try {
            //获取上一页的最后一个元素
            ScoreDoc lastScoreDoc = getLastScoreDoc(pageBean.getPageCurrent(),
                    pageBean.getPageSize(), query, searcher);
            //通过最后一个元素搜索下页的pageSize个元素
            TopDocs topDocs = searcher.searchAfter(lastScoreDoc, query, pageBean.getPageSize());
            for (ScoreDoc item : topDocs.scoreDocs) {
                Document doc = searcher.doc(item.doc);
                ArticleBean bean = new ArticleBean();
                bean.setId(Long.valueOf(doc.get("id")));
                bean.setTitle(doc.get("title"));
                bean.setContext(doc.get("context"));
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }

        return list;
    }


    private static ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher indexSearcher) throws IOException {
        if (pageIndex == 1) return null;//如果是第一页返回空
        int num = pageSize * (pageIndex - 1);//获取上一页的数量
        TopDocs tds = indexSearcher.search(query, num);
        return tds.scoreDocs[num - 1];
    }

    /**
     * 打印分词结果
     * @param text
     */
    private static void analyze(String text) {
        try {
            TokenStream tokenStream = analyzer.tokenStream("context", new StringReader(text));
            tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                CharTermAttribute charTermAttribute = (CharTermAttribute) tokenStream
                        .getAttribute(CharTermAttribute.class);
                System.out.println(charTermAttribute.toString());
            }
            tokenStream.end();
            tokenStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除搜索索引目录
     * @throws Exception
     */
    public static void deleteIndexDir() throws Exception {
        FileUtils.deleteDirectory(path.toFile());
    }
}
