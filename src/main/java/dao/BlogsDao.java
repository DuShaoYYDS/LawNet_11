package dao;

import entity.Blog;
import entity.Type;
import entity.User;
import utils.JdbcUtils;
import utils.MarkDownUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dushao 2022/10/24
 * 获取数据库信息的实体类
 * */
public class BlogsDao {

    /**
     *从数据库获取所有案件(博客内容)封装为List
     * @return 返回存放所有的博客列表
     */
    public List<Blog> getAllBlogs(){

        List<Blog> blogs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = JdbcUtils.getConnection();
            connection.setAutoCommit(false);
            StringBuffer stringBuffer = new StringBuffer("select * from blog,user,type where username=publishername and blog.type_id = type.id");
            preparedStatement = connection.prepareStatement(stringBuffer.toString());

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Blog blog = new Blog();
                User writer = new User();
                blog.setId(resultSet.getLong("id"));
                blog.setTitle(resultSet.getString("titel"));
                blog.setContent(resultSet.getString("content"));
                blog.setFlag(resultSet.getString("flag"));
                blog.setViews(resultSet.getInt("views"));
                blog.setAppreciation(resultSet.getInt("appreciation") == 1 ? true : false);
                blog.setShareStatment(resultSet.getInt("share_statment") == 1 ? true : false);
                blog.setCommentabled(resultSet.getInt("commentabled") == 1 ? true : false);
                blog.setPublished(resultSet.getInt("published") == 1 ? true : false);
                blog.setRecommend(resultSet.getInt("recommend") == 1 ? true : false);
                blog.setCreateTime(resultSet.getDate("create_time"));
                blog.setUpdateTime(resultSet.getDate("update_time"));
                blog.setPublisherName(resultSet.getString("publishername"));
                blog.setFirstPicture(resultSet.getString("first_picture"));

                Type type = new Type();
                type.setId(resultSet.getLong("blog.type_id"));
                type.setName(resultSet.getString("name"));
                blog.setType(type);

                writer.setUsername(resultSet.getString("username"));
                writer.setAvatar(resultSet.getString("avatar"));
                writer.setId(resultSet.getLong("id"));
                blog.setUser(writer);
                blogs.add(blog);
            }

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }

        return blogs;
    }
    /**
     * 获取所有公开的博客
     * @return 返回所有符合要求的博客
     */
    public List<Blog> getAllPublishedBlogs(){

        List<Blog> blogs = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = JdbcUtils.getConnection();
            connection.setAutoCommit(false);
            StringBuffer stringBuffer = new StringBuffer("select * from blog,user,type where username=publishername " +
                    "and blog.type_id = type.id and published = 1");
            preparedStatement = connection.prepareStatement(stringBuffer.toString());

            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Blog blog = new Blog();
                User writer = new User();
                blog.setId(resultSet.getLong("id"));
                blog.setTitle(resultSet.getString("titel"));
                blog.setContent(resultSet.getString("content"));
                blog.setFlag(resultSet.getString("flag"));
                blog.setViews(resultSet.getInt("views"));
                blog.setAppreciation(true);
                blog.setShareStatment(true);
                blog.setCommentabled(true);
                blog.setPublished(true);
                blog.setRecommend(true);
                blog.setCreateTime(resultSet.getDate("create_time"));
                blog.setUpdateTime(resultSet.getDate("update_time"));
                blog.setPublisherName(resultSet.getString("publishername"));
                blog.setFirstPicture(resultSet.getString("first_picture"));

                Type type = new Type();
                type.setId(resultSet.getLong("blog.type_id"));
                type.setName(resultSet.getString("name"));
                blog.setType(type);

                writer.setUsername(resultSet.getString("username"));
                writer.setAvatar(resultSet.getString("avatar"));
                writer.setId(resultSet.getLong("id"));
                blog.setUser(writer);
                blogs.add(blog);
            }

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }

        return blogs;
    }

    /**
     *根据用户名获取博客
     * @param  user 当前需要获取博客的用户
     * @return 返回当前用户的所有博客
     */
    public List<Blog> getMyselfBlogByUsername(User user){
        List<Blog> blogs = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getConnection();
            connection.setAutoCommit(false);


            StringBuffer stringBuffer = new StringBuffer("select * from blog,type where publishername = ? and blog.type_id = type.id");
            preparedStatement = connection.prepareStatement(stringBuffer.toString());
            preparedStatement.setString(1,user.getUsername());

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Blog blog = new Blog();
                blog.setId(resultSet.getLong("id"));
                blog.setTitle(resultSet.getString("titel"));
                blog.setContent(resultSet.getString("content"));
                blog.setFlag(resultSet.getString("flag"));
                blog.setViews(resultSet.getInt("views"));
                blog.setAppreciation(resultSet.getInt("appreciation") == 1 ? true : false);
                blog.setShareStatment(resultSet.getInt("share_statment") == 1 ? true : false);
                blog.setCommentabled(resultSet.getInt("commentabled") == 1 ? true : false);
                blog.setPublished(resultSet.getInt("published") == 1 ? true : false);
                blog.setRecommend(resultSet.getInt("recommend") == 1 ? true : false);
                blog.setCreateTime(resultSet.getDate("create_time"));
                blog.setUpdateTime(resultSet.getDate("update_time"));
                blog.setPublisherName(resultSet.getString("publishername"));
                blog.setFirstPicture(resultSet.getString("first_picture"));
                Type type = new Type();
                type.setId(resultSet.getLong("blog.type_id"));
                type.setName(resultSet.getString("name"));
                blog.setType(type);

                blogs.add(blog);
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }
        return blogs;
    }

    /**
     *根据博客id获取相对应的博客
     * @param id 当前需要获取的博客id
     * @return 返回查找到的博客
     */
    public Blog getBlogDetailsByID(String id){
        Blog blog = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getConnection();
            StringBuffer stringBuffer = new StringBuffer("select * from blog,user where blog.id = ? and publishername=username");
            preparedStatement = connection.prepareStatement(stringBuffer.toString());
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                blog = new Blog();
                User writer = new User();
                blog.setId(resultSet.getLong("id"));
                blog.setTitle(resultSet.getString("titel"));
                blog.setContent(resultSet.getString("content"));
                blog.setFlag(resultSet.getString("flag"));
                blog.setViews(resultSet.getInt("views"));
                blog.setAppreciation(resultSet.getInt("appreciation") == 1 ? true : false);
                blog.setShareStatment(resultSet.getInt("share_statment") == 1 ? true : false);
                blog.setCommentabled(resultSet.getInt("commentabled") == 1 ? true : false);
                blog.setPublished(resultSet.getInt("published") == 1 ? true : false);
                blog.setRecommend(resultSet.getInt("recommend") == 1 ? true : false);
                blog.setCreateTime(resultSet.getDate("create_time"));
                blog.setFirstPicture(resultSet.getString("first_picture"));
                blog.setPublisherName(resultSet.getString("publishername"));
                writer.setAvatar(resultSet.getString("avatar"));
                writer.setUsername(resultSet.getString("username"));
                blog.setUser(writer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }


        return blog;
    }

    /**
     * 根据博客id获取到相对应的博客信息并且转化成为HTML格式
     * @param id 当前需要查找的的博客id
     * @return 查找到并且转化后的博客内容
     */
    public Blog getBlogDetailsByIDConvert(String id){
        Blog blog = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getConnection();
            StringBuffer stringBuffer = new StringBuffer("select * from blog,user where blog.id = ? and publishername=username");
            preparedStatement = connection.prepareStatement(stringBuffer.toString());
            preparedStatement.setString(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                blog = new Blog();
                User writer = new User();
                blog.setId(resultSet.getLong("id"));
                blog.setTitle(resultSet.getString("titel"));
                blog.setContent(resultSet.getString("content"));
                blog.setFlag(resultSet.getString("flag"));
                blog.setViews(resultSet.getInt("views"));
                blog.setAppreciation(resultSet.getInt("appreciation") == 1 ? true : false);
                blog.setShareStatment(resultSet.getInt("share_statment") == 1 ? true : false);
                blog.setCommentabled(resultSet.getInt("commentabled") == 1 ? true : false);
                blog.setPublished(resultSet.getInt("published") == 1 ? true : false);
                blog.setRecommend(resultSet.getInt("recommend") == 1 ? true : false);
                blog.setCreateTime(resultSet.getDate("create_time"));
                blog.setFirstPicture(resultSet.getString("first_picture"));
                blog.setPublisherName(resultSet.getString("publishername"));
                writer.setAvatar(resultSet.getString("avatar"));
                writer.setUsername(resultSet.getString("username"));
                blog.setUser(writer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(resultSet,preparedStatement,connection);
        }

        String content = blog.getContent();
        MarkDownUtils markDownUtils = new MarkDownUtils();
        content = markDownUtils.markdownToHtmlExtensions(content);
        /*MarkDownUtils.markdownToHtmlExtensions(content);*/
        blog.setContent(content);
        return blog;
    }


    /**
     * 用户创建新的博客信息
     * @param blog 用户创建的博客内容
     */
    public void creatNewBlogByUser(Blog blog){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JdbcUtils.getConnection();

            StringBuffer stringBuffer = new StringBuffer("insert into blog(publishername,content,commentabled," +
                    "recommend,create_time,share_statment,published,titel,flag,first_picture,appreciation,update_time,type_id) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement = connection.prepareStatement(stringBuffer.toString());
            preparedStatement.setString(1,blog.getPublisherName());
            preparedStatement.setString(2,blog.getContent());
            preparedStatement.setBoolean(3,blog.isCommentabled());
            preparedStatement.setBoolean(4,blog.isRecommend());
            /*如何将时间保存进数据库*/
            Date date = new Date(blog.getCreateTime().getTime());
            preparedStatement.setDate(5, date);
            preparedStatement.setBoolean(6,blog.isShareStatment());
            preparedStatement.setBoolean(7,blog.isPublished());
            preparedStatement.setString(8,blog.getTitle());
            preparedStatement.setString(9,blog.getFlag());
            preparedStatement.setString(10,blog.getFirstPicture());
            preparedStatement.setBoolean(11,blog.isAppreciation());
            preparedStatement.setDate(12,date);
            preparedStatement.setLong(13,blog.getTypeId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }
    }

    /**
     * 更新博客内容
     * @param blog 需要更新的博客对象
     * @param targetId 当前更新的博客的id
     * */
    public void updateBlog(Blog blog,Long targetId){

        PreparedStatement preparedStatement = null;

        Connection connection = null;

        try {
            connection = JdbcUtils.getConnection();

            StringBuffer stringBuffer = new StringBuffer("update blog set titel=?,publishername=?,content=?,first_picture =?,update_time=?,type_id = ?,commentabled  = ?,recommend = ?,share_statment = ?,published = ?,appreciation = ?,flag = ? where id = ?");
            preparedStatement = connection.prepareStatement(stringBuffer.toString());
            preparedStatement.setString(1,blog.getTitle());
            preparedStatement.setString(2,blog.getPublisherName());
            preparedStatement.setString(3,blog.getContent());
            preparedStatement.setString(4,blog.getFirstPicture());
            Date date = new Date(blog.getUpdateTime().getTime());
            preparedStatement.setDate(5,date);
            preparedStatement.setLong(6,blog.getTypeId());
            preparedStatement.setBoolean(7,blog.isCommentabled());
            preparedStatement.setBoolean(8,blog.isRecommend());
            preparedStatement.setBoolean(9,blog.isShareStatment());
            preparedStatement.setBoolean(10,blog.isPublished());
            preparedStatement.setBoolean(11,blog.isAppreciation());
            preparedStatement.setString(12,blog.getFlag());

            //类型修改出错，暂时无法解决
            /*preparedStatement.setLong(5,blog.getTypeId());*/
            preparedStatement.setLong(13,targetId);



            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }
    }

    /**
     * 根据博客的id删除对应的博客内容
     * @param id 需要删除的博客的id
     * */
    public void delBlogById(String id){

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        System.out.println(id);

        try {
            connection = JdbcUtils.getConnection();
            StringBuffer stringBuffer = new StringBuffer("delete from blog where id = ?");
            preparedStatement = connection.prepareStatement(stringBuffer.toString());
            preparedStatement.setString(1,id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }

    }

    /**
     * 根据据博客id更新博客浏览次数
     * @param id 需要更新的博客id
     * @param views 更新后的浏览次数
     * */
    public void updateViewsById(Long id,int views) {

        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = JdbcUtils.getConnection();
            StringBuffer stringBuffer = new StringBuffer("update blog set views = ? where id = ?");
            preparedStatement = connection.prepareStatement(stringBuffer.toString());
            preparedStatement.setInt(1,views);
            preparedStatement.setLong(2,id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            JdbcUtils.close(null,preparedStatement,connection);
        }

    }
}
