package com.puyixiaowo.core.thread;

import com.puyixiaowo.fblog.bean.admin.book.BookshelfBean;
import com.puyixiaowo.fblog.service.book.BookFilterService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 *
 * @author Moses
 * @date 2017-12-19
 */
public class BookByFilterThread extends RecursiveTask {



    private List<BookshelfBean> bookshelfBeanList;
    private boolean isMainThread;//是否主线程
    private int i;//子线程索引


    public BookByFilterThread(List<BookshelfBean> bookshelfBeanList,
                              int i,
                              boolean isMainThread) {

        this.bookshelfBeanList = bookshelfBeanList;
        this.isMainThread = isMainThread;
        this.i = i;
    }

    @Override
    protected Object compute() {


        if (bookshelfBeanList != null && i == bookshelfBeanList.size()) {
            //处理到结尾
            return null;
        }
        if (!isMainThread) {
            //子线程直接返回处理结果
            BookFilterService.fetchUserBookUpdate(bookshelfBeanList.get(i).getUserId());

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        List<BookByFilterThread> threads = new ArrayList<>();

        for (int j = 0; j < bookshelfBeanList.size(); j++) {

            BookByFilterThread newsByFilterThread = new BookByFilterThread(bookshelfBeanList, j, false);//子线程
            threads.add(newsByFilterThread);
            newsByFilterThread.fork();
        }

        for (BookByFilterThread newsByFilterThread :
                threads) {
            newsByFilterThread.join();
        }

        return null;
    }


}
