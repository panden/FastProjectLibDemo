##版本信息
Version:0.0.1  
Auther:siwei  
JpushVersion:3.1.1  

##快速接入
###1.Key的配置：
在build.gradlew 中配置jpush_appkey的值,该值为极光分配的key

###2.需要申请的权限
You Package.permission.JPUSH_MESSAGE	官方定义的权限，允许应用接收JPUSH内部代码发送的广播消息。  
RECEIVE_USER_PRESENT	允许应用可以接收点亮屏幕或解锁广播。  
INTERNET	允许应用可以访问网络。  
WAKE_LOCK	允许应用在手机屏幕关闭后后台进程仍然运行  
READ_PHONE_STATE	允许应用访问手机状态。  
WRITE_EXTERNAL_STORAGE	允许应用写入外部存储。  
READ_EXTERNAL_STORAGE	允许应用读取外部存储。  
WRITE_SETTINGS	允许应用读写系统设置项。  
VIBRATE	允许应用震动。  
MOUNT_UNMOUNT_FILESYSTEMS	允许应用挂载/卸载 外部文件系统。  
ACCESS_NETWORK_STATE	允许应用获取网络信息状态，如当前的网络连接是否有效。