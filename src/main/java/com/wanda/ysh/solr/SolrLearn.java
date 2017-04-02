/*
 * Copyright 2012-2014 Wanda.cn All right reserved. This software is the
 * confidential and proprietary information of Wanda.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Wanda.cn.
 */
package com.wanda.ysh.solr;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
 
/**
 * 测试运行server case方法，如果成功创建对象，那你就成功的链接到。
 * 注意：在运行本方法之前，请启动你的solr官方自动的项目。http://localhost:8983/solr/
 * @author yangshihong Apr 20, 2016 10:20:25 AM
 */
public class SolrLearn {
    
    private HttpSolrClient server;
    
    /**
     * 场景：在本地用http://localhost:8983/solr的链接访问没问题，但是如果用
     *      http://192.168.0.188:8983/solr这样的链接访问别的服务器上的solr
     *      时报以上sorl的链接错误,返回tomcat的404页面。
     * 解决：经尝试访问别的solr服务器的链接时，需在后面core核心名字,即你需要访问的core核心名字。
     * 例如：http://localhost:8983/solr/core1
     */
    private static final String DEFAULT_URL = "http://10.1.169.221:8983/solr/solr";
    
    @Before
    public void init() {
        fail("*********************init**************************");
        server = new HttpSolrClient(DEFAULT_URL);
        server.setSoTimeout(1000); // socket read timeout 
        server.setConnectionTimeout(100); 
        server.setDefaultMaxConnectionsPerHost(100); 
        server.setMaxTotalConnections(100); 
        server.setFollowRedirects(false); // defaults to false 
        // allowCompression defaults to false. 
        // Server side must support gzip or deflate for this to have any effect. 
        server.setAllowCompression(true); 
        server.setMaxRetries(1); // defaults to 0.  > 1 not recommended. 
         
        //sorlr J 目前使用二进制的格式作为默认的格式。对于solr1.2的用户通过显示的设置才能使用XML格式。
        server.setParser(new XMLResponseParser());
        //二进制流输出格式
        //server.setRequestWriter(new BinaryRequestWriter());
    }
    
    @After
    public void destory() {
        server = null;
        System.runFinalization();
        System.gc();
    }
    
    public final void fail(Object o) {
        System.out.println(o);
    }
    
    /**
     * <b>function:</b> 测试是否创建server对象成功
     * @author hoojo
     * @createDate 2011-10-21 上午09:48:18
     */
    @Test
    public void server() {
        fail(server);
    }
 
    /**
     * <b>function:</b> 根据query参数查询索引
     * @author hoojo
     * @createDate 2011-10-21 上午10:06:39
     * @param query
     */
    public void query(String query) {
        SolrParams params = new SolrQuery(query);
        try {
            QueryResponse response = server.query(params);

            SolrDocumentList list = response.getResults();
            for (int i = 0; i < list.size(); i++) {
                fail(list.get(i));
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * function: 利用SolrJ完成Index Document的添加操作
     * @author hoojo
     * @createDate 2011-10-21 上午09:49:10
     */
    @Test
    public void addDoc() {
        fail("*********************addDoc**************************");
        //创建doc文档
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", 1);
        doc.addField("name", "Solr Input Document");
        doc.addField("manu", "this is SolrInputDocument content");
        
        try {
            //添加一个doc文档
            UpdateResponse response = server.add(doc);
            fail(server.commit());//commit后才保存到索引库
            fail(response);
            fail("query time：" + response.getQTime());
            fail("Elapsed Time：" + response.getElapsedTime());
            fail("status：" + response.getStatus());
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        query("name:solr");
    }
    
    @Test
    public void addDocs() {
        fail("*********************addDocs**************************");
        List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", 2);
        doc.addField("name", "Solr Input Documents 1");
        doc.addField("manu", "this is SolrInputDocuments 1 content");
        
        docs.add(doc);
        
        doc = new SolrInputDocument();
        doc.addField("id", 3);
        doc.addField("name", "Solr Input Documents 2");
        doc.addField("manu", "this is SolrInputDocuments 3 content");
        
        docs.add(doc);
        
        try {
            //add docs
            UpdateResponse response = server.add(docs);
            //commit后才保存到索引库
            fail(server.commit());
            fail(response);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        query("solr");
    }
    
    /**
     * <b>function:</b> 添加JavaEntity Bean
     * @author hoojo
     * @createDate 2011-10-21 上午09:55:37
     */
    @Test
    public void addBean() {
        fail("*********************addBean**************************");
        //Index需要添加相关的Annotation注解，便于告诉solr哪些属性参与到index中
        SolrBean index = new SolrBean();
        index.setId("4");
        index.setName("add bean index");
        index.setManu("index bean manu");
        index.setCat(new String[] { "a1", "b2" });
        
        try {
            //添加Index Bean到索引库
            UpdateResponse response = server.addBean(index);
            fail(server.commit());//commit后才保存到索引库
            fail(response);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        queryAll();
    }
    
    /**
     * <b>function:</b> 添加Entity Bean集合到索引库
     * @author hoojo
     * @createDate 2011-10-21 上午10:00:55
     */
    @Test
    public void addBeans() {
        SolrBean index = new SolrBean();
        index.setId("6");
        index.setName("add beans index 1");
        index.setManu("index beans manu 1");
        index.setCat(new String[] { "a", "b" });
        
        List<SolrBean> indexs = new ArrayList<SolrBean>();
        indexs.add(index);
        
        index = new SolrBean();
        index.setId("5");
        index.setName("add beans index 2");
        index.setManu("index beans manu 2");
        index.setCat(new String[] { "aaa", "bbbb" });
        indexs.add(index);
        fail("*********************addBeans**************************");
        try {
            //添加索引库
            UpdateResponse response = server.addBeans(indexs);
            fail(server.commit());//commit后才保存到索引库
            fail(response);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        queryAll();
    }
    
    /**
     * <b>function:</b> 删除索引操作
     * @author hoojo
     * @createDate 2011-10-21 上午10:04:28
     */
    @Test
    public void remove() {
        fail("*********************remove**************************");
        try {
            //删除id为1的索引
            server.deleteById("1");
            server.commit();
            query("id:1");
            
            //根据id集合，删除多个索引
            List<String> ids = new ArrayList<String>();
            ids.add("2");
            ids.add("3");
            server.deleteById(ids);
            server.commit(true, true);
            query("id:3 id:2");
            
            //删除查询到的索引信息
            server.deleteByQuery("id:4 id:6");
            server.commit(true, true);
            queryAll();
            
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * <b>function:</b> 查询所有索引信息
     * @author hoojo
     * @createDate 2011-10-21 上午10:05:38
     */
    @Test
    public void queryAll() {
        ModifiableSolrParams params = new ModifiableSolrParams();
        // 查询关键词，*:*代表所有属性、所有值，即所有index
        params.set("q", "*:*");
        // 分页，start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
        params.set("start", 0);
        params.set("rows", Integer.MAX_VALUE);

        // 排序，，如果按照id 排序，，那么将score desc 改成 id desc(or asc)
        params.set("sort", "score desc");

        // 返回信息 * 为全部 这里是全部加上score，如果不加下面就不能使用score
        params.set("fl", "*,score");

        try {
            QueryResponse response = server.query(params);

            SolrDocumentList list = response.getResults();
            for (int i = 0; i < list.size(); i++) {
                fail(list.get(i));
            }
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}