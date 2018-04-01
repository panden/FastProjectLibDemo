lib初版  
versionCode:1  
versionName:V0.0.1  
data:2018.3.13 01:34  

1.BaseActivity封装常用的一些方法：输入法弹出和隐藏、Eventbus事件、Loading、Toast、标题栏等
2.BaseMVPActivity封装MVP在其中，View层继承BaseMVPActivity即可，Presenter继承BasePresenter即可、Model层继承IModel即可
3.打算封装路由框架在其中，以跳转地址的方式实现相互跳转来解耦，ARouter这个框架还在学习中；https://github.com/alibaba/ARouter
4.选择器使用：https://github.com/ImKarl/CharacterPickerView