package com.puyixiaowo.fblog.utils;

import com.puyixiaowo.fblog.bean.ArticleBean;
import com.puyixiaowo.fblog.bean.sys.PageBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.analysis.Analyzer;
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
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);// 创建模式 OpenMode.CREATE_OR_APPEND 添加模式
        //如果是CREATE ,每次都会重新创建这个索引，清空以前的数据，如果是append 每次都会追加，之前的不删除
        //在日常的需求索引添加中，一般都是 APPEND 持续添加模式
        writer = new IndexWriter(dirWrite, iwc);
        return writer;
    }

    private static void addLuceneIndex(ArticleBean articleBean) throws Exception {

        if (articleBean.getStatus() == 0
                || StringUtils.isBlank(articleBean.getContext())) {
            return;
        }

        articleBean.setContext(StringUtils.delHTMLTag(articleBean.getContext()));

        IndexWriter writer = getIndexWriter();

        Document doc = new Document();

        // 对字段建立索引
        doc.add(new Field("id", articleBean.getId() + "", new FieldType(TextField.TYPE_STORED)));
        doc.add(new Field("title", articleBean.getTitle(), new FieldType(TextField.TYPE_STORED)));
        doc.add(new Field("context", articleBean.getContext(), new FieldType(TextField.TYPE_STORED)));
        doc.add(new Field("createDate",
                DateFormatUtils.format(articleBean.getCreateDate(),
                        "yyyy-MM-dd HH:mm:ss"), new FieldType(TextField.TYPE_STORED)));
        writer.addDocument(doc);
        writer.close(); // 关闭读写器
    }

    public static void removeLuceneIndex() {
    }

    public static PageBean search(PageBean pageBean,
                              String queries) throws Exception {
        List<ArticleBean> list = new ArrayList<>();
        Directory dir = FSDirectory.open(path);
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        String[] fields = {"title", "context", "createDate"};

        QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
        Query query = queryParser.parse(QueryParser.escape(queries));

        try {
            //获取搜索结果总数
            pageBean.setTotalCount(searcher.count(query));
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
                bean.setCreateDate(DateUtils
                        .parseDate(doc.get("createDate"),
                                "yyyy-MM-dd HH:mm:ss").getTime());
                list.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }

        pageBean.setList(list);
        return pageBean;
    }



    private static ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher indexSearcher) throws IOException {
        if (pageIndex == 1) return null;//如果是第一页返回空
        int num = pageSize * (pageIndex - 1);//获取上一页的数量
        TopDocs tds = indexSearcher.search(query, num);
        return tds.scoreDocs[num - 1];
    }

    /**
     * 删除搜索索引目录
     * @throws Exception
     */
    public static void deleteIndexDir() throws Exception {
        FileUtils.deleteDirectory(path.toFile());
    }

    public static void deleteLuceneIndex(Long id) throws Exception {

        if (id == null ||
                id <= 0) {
            return;
        }

        Directory dir = FSDirectory.open(path);
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(dir, iwc);

        QueryParser queryParser = new QueryParser("id", analyzer);
        Query query = queryParser.parse("id:" + id);

        indexWriter.deleteDocuments(query);
        indexWriter.commit();
        indexWriter.close();
    }

    public static void dealLuceneIndex(ArticleBean articleBean) throws Exception{
        deleteLuceneIndex(articleBean.getId());
        addLuceneIndex(articleBean);
    }
}
