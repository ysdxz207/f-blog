package com.puyixiaowo;

import com.puyixiaowo.fblog.bean.ArticleBean;
import com.puyixiaowo.fblog.utils.DBUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.lionsoul.jcseg.analyzer.JcsegAnalyzer;
import org.lionsoul.jcseg.tokenizer.core.JcsegTaskConfig;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

//Lucene 为mysql数据库表 简历完整索引
public class Test {
    private static final Path path = Paths.get(System.getProperty("user.dir") + "/lucene/indexs");

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
        IndexWriter writer = null;
        DBUtils.initDB();


        String sql = "SELECT * FROM article";
        // 把lucene的索引文件保存到机器的磁盘
        Directory dirWrite = FSDirectory.open(path);
        // lucene分析器    使用lucene默认的暂停词
        Analyzer analyzer = new JcsegAnalyzer(JcsegTaskConfig.COMPLEX_MODE);
        // 初始化写入配置
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(OpenMode.CREATE);// 创建模式 OpenMode.CREATE_OR_APPEND 添加模式
        //如果是CREATE ,每次都会重新创建这个索引，清空以前的数据，如果是append 每次都会追加，之前的不删除
        //在日常的需求索引添加中，一般都是 APPEND 持续添加模式
        writer = new IndexWriter(dirWrite, iwc);
        List<ArticleBean> articleBeanList =  DBUtils.selectList(ArticleBean.class,
                "select * from article", null);

        for (ArticleBean articleBean : articleBeanList) {
            Document doc = new Document();
            // 该表所有的字段建立索引
            doc.add(new Field("id", articleBean.getId() + "", new FieldType(TextField.TYPE_STORED)));
            doc.add(new Field("title", articleBean.getTitle(), new FieldType(TextField.TYPE_STORED)));
            doc.add(new Field("context", articleBean.getContext(), new FieldType(TextField.TYPE_STORED)));
            writer.addDocument(doc);
        }
        // writer.optimize(); //索引优化
        writer.close(); // 关闭读写器

        //索引查询,通过如下2个字段进行 or  查询
        Directory dir = FSDirectory.open(path);
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        // 选择姓名中包含张字的记录
  /*   单字段查询
   *   QueryParser parser = new QueryParser("name", new StandardAnalyzer());
       Query query = parser.parse("三");
  */
        // 选择姓名中包含张字的记录
        String[] queries = { "第二", "挽留" };
        String[] fields = { "title", "context" };
        /**
         * 这里需要注意的就是BooleanClause.Occur[]数组,它表示多个条件之间的关系,
         * BooleanClause.Occur.MUST表示and,BooleanClause.Occur.MUST_NOT表示not,BooleanClause.Occur.SHOULD表示or.
         */
        BooleanClause.Occur[] clauses = { BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD };
        Query query = MultiFieldQueryParser.parse(queries, fields, clauses, analyzer);

        TopDocs topDocs = searcher.search(query, 2);
        ScoreDoc[] hits = topDocs.scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            int DocId = hits[i].doc;
            Document doc = searcher.doc(DocId);
            System.out.println(doc.get("title") +" "+doc.get("context"));
        }
    }
}