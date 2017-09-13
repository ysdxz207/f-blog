package com.puyixiaowo.fblog.utils.lucene;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.LeafCollector;

import java.io.IOException;

/**
 * 
 * @author Moses
 * @date 2017-09-13 18:43
 * 
 */
public class CountCollector implements Collector {
    @Override
    public LeafCollector getLeafCollector(LeafReaderContext leafReaderContext) throws IOException {
        return null;
    }

    @Override
    public boolean needsScores() {
        return false;
    }
}
