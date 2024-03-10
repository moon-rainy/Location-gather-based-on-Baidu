# Location-gather-based-on-Baidu
A Android Application of Location Gather Based On BaiduMap SDK，that you can get your location and send to MySQL database

## 1、 获取百度地图访问AK
进入百度地图官网（https://lbsyun.baidu.com）
进入控制台，按照官网教程创建安卓应用
<div align=center>
           <img align="middle" width="741" alt="image" src="https://github.com/moon-rainy/Location-gather-based-on-Baidu/assets/110764944/5baac2c9-cd17-4eab-b9f2-799318309f47">
</div>

将申请到的访问AK粘贴到 AndroidManifest.xml 中的对应位置，即可使用应用中的三个功能。

## 2、 将数据发送到Mysql数据库
在app.src.main.java.com.example.maplocationdemo.DataBase.Connection.CloudBDConnection.java分别配置和自己数据库的信息
<div align=center>
           <img width="471" alt="image" src="https://github.com/moon-rainy/Location-gather-based-on-Baidu/assets/110764944/a58107c9-c67b-43b4-a8f0-e0cee4c744c2">
</div>

在app.src.main.java.com.example.maplocationdemo.baidu.location.BaiDuLocationActivity.java中的对应位置取消注释创建后台发送数据进程
<div align=center>
           <img width="573" alt="image" src="https://github.com/moon-rainy/Location-gather-based-on-Baidu/assets/110764944/e2406a7e-4657-4ec3-9d67-598f5f616c26">
</div>

安装应用后点击定位数据显示，点击开始定位，每十秒会获取一次定位信息，并发送给数据库，相关配置可以在./LocationService.java中进行修改
