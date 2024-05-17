## 说明
 * 所有接口只会是POST和GET方式，大部分都是POST
 * 所有跳转类的接口，只会是GET，比如客服链接跳转接口
 * 所有POST接口的Content-Type都是application/json;charset=UTF-8，数据格式用JSON
 
## 登录
 * 登录成功：把登录后返回的token永久持久化到本地，此后任何请求都必须把该token进行携带，放在header的Authorization中
 * 服务器强制登出：返回数据中forceLogout为true时，把token移除，并提示message给用户，**应该要全局拦截这个响应字段**
 * 用户主动登出：把token移除，导向到登录页面
 
## 国际化
接口返回的数据也需要支持国际化，需要接口传参指明返回哪种语言的翻译内容
 * 初次进入页面时，首先调用国际化语言列表接口，获取目前支持的所有语言，必须以接口数据为准，因为可能会对某几种语言进行开关
 
 
    POST类请求，在header中加入language参数
    GET类请求，在url中加入language参数，比如http://www.api.com?language=zh-CN