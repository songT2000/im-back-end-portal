## 启动参数说明
* 设置启动环境，个人开发环境用个人的配置文件application-yourname.yml，并在启动时指定运行配置
    * -Dspring.profiles.active=test
    
* 设置日志文件输出位置
    * -Dlogging.file=/data/im/log/scheduler/scheduler.log
   
* 超过毫秒数打印SQL，设置大一点就不会打印SQL了，设置VM参数
    * -Dp6spy.config.executionThreshold=5000
    
* 设置SQL文件输出位置
    * -Dp6spy.config.logfile=/data/im/log/sql/scheduler.log
    
## Swagger2使用说明
* https://www.jianshu.com/p/c79f6a14f6c9
    * @Api：描述整个类，显示在tab里的导航标识，描述Controller的作用，一般只写一个tags就可以了（比如用户类相关接口）
    * @ApiOperation：描述一个接口，一般写一个value（接口名字）+ notes（接口描述，可以不用写）就可以了
    * @ApiParam：单个参数描述，本项目基本不会用到，所有接口都会使用对象来接收参数
    * @ApiModel：描述接口参数对象或者接口返回对象的
    * @ApiModelProperty：描述接口参数对象或者接口返回对象，描述对象中的一个字段
    * @ApiIgnore：使用该注解忽略这个API
    
## 语音合成1，一块钱一次
* http://www.zaixianai.cn/voiceCompose
    * 选择客服场景里的[艾悦，温柔女声]
    * 打开F12控制台
    * 点击[播放]按钮，会加载mp3文件，再把这个mp3文件下载就可以了，现在下载好像要1块钱一次了

## 语音合成2，免费
* https://neuhub.jd.com/ai/api/speech/tts?cu=true&utm_source=baidu-search&utm_medium=cpc&utm_campaign=t_262767352_baidusearch&utm_term=130799208315_0_9e55429190fc482cb7a53dc44dcb5745
    * 选择通用里的[桃桃|标准女声]
    * 打开F12控制台
    * 点击[播放]按钮，会加载mp3文件，再把这个mp3文件下载就可以了# im
