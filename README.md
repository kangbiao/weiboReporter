## 微博爬虫

> 项目使用WebMagic爬虫框架，能够分析微博动态，评论和用户数据。

**实现功能**

1. 根据用户uid爬取用户所有微博动态信息
2. 爬取每条动态下面的评论信息和评论用户信息
3. 实现多线程
4. 实现配置化
5. 实现爬虫停止后重新启动继续上次任务
6. 与elasticsearch集成
7. 扩展downloader和scheduler，支持对每个Request设置请求头

@author [kangbiao@kangbiao.org](mailto://kangbiao@kangbiao.org)