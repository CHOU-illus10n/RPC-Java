package p1.common.service;


import p1.common.pojo.Blog;

// 新的服务接口
public interface BlogService {
    Blog getBlogById(Integer id);
}