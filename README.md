# EasySkin
插件化换肤

流程：  
1、制作皮肤包  
2、添加依赖 implementation project(path: ':skin')  
3、Application中进行SDK初始化 SkinManager.init(this)  
4、换肤 SkinManager.instance?.loadSkin("皮肤包路经")  

注：  
1、皮肤包可以网络下载或者内置在assets目录中，assets中的的皮肤包需要手动拷贝到SD卡后才能试用  
2、制作皮肤包比较简单，直接在原项目的基础上修改背景颜色等选项最后打包生成.apk文件  
3、自定义View实现SkinViewSupport接口，自己管理切换逻辑  
