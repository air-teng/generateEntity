# generateEntity
一个根据MySQL表格快速生成实体类的Java 项目；

tableName -- 配置表名和实体类名的映射（必配）；
dbFile.properties -- 配置jdbc属性；
filedTypeMap -- 配置数据库字段类型和Java成员变量类型的映射（可修改和增加）；

dbFile.properties中filepath以/分割，生成包名的时候会以/进行分割；

目前配置项比较多。如果有时间之后再进一步完善扩展。
