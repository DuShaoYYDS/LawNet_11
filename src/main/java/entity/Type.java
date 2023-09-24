package entity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by dushao 2022/10/24
 * 存储博客类型的实体类
 * */
public class Type {

    private Long id;
    private String name;

    private int count;


    private List<Blog> blogs = new ArrayList<>();

    public Type() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
