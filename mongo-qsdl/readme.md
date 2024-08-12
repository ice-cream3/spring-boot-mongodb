集成mongodb使用qdsl查询,参考csdn:https://mp.csdn.net/mp_blog/creation/editor/141066024
#### 1>引入jar包

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
            <version>2.6.13</version>
        </dependency>

        <dependency>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-core</artifactId>
            <version>5.0.0</version>
        </dependency>

        <dependency>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-mongodb</artifactId>
            <version>5.0.0</version>
        </dependency>

        <dependency>
            <groupId>com.mysema.querydsl</groupId>
            <artifactId>querydsl-apt</artifactId>
            <version>3.6.9</version>
            <scope>provided</scope>
        </dependency>
#### 2>pom.xml中指定生成Qxxx查询路径

    <plugin>
                <groupId>com.mysema.maven</groupId>
                <artifactId>apt-maven-plugin</artifactId>
                <version>1.1.3</version>
                <dependencies>
                    <dependency>
                        <groupId>com.querydsl</groupId>
                        <artifactId>querydsl-apt</artifactId>
                        <version>${querydsl.version}</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>process</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>target/generated-sources/queries</outputDirectory>
                            <processor>org.springframework.data.mongodb.repository.support.MongoAnnotationProcessor</processor>
                            <logOnlyOnError>true</logOnlyOnError>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
#### 3>配置yml

    server:
      port: 8008
      # monog数据库
      mongodb:
        uri: mongodb://192.168.10.1:2000,192.168.10.2:2000,192.168.10.3:2000/
        database: mongo_db
        vipRecordName: vip_record
#### 4>编码配置类(创建mongiconfig初始类)
#### 5>创建数据存储bean:VipRecordMongo 
#### 6>创建mongdo数据表:

    db.getCollection("vip_record").insert({
    "_id" : ObjectId("16546411849465656"),
    "tid" : NumberInt(2999),
    "name" : "小明",
    "memberId" : NumberLong(1),
    "vipAmount" : 0.0,
    "gameStartTime" : ISODate("2024-07-31T08:55:44.000+0000"),
    "gameEndTime" : ISODate("2024-07-31T08:55:44.000+0000"),
    "gameType" : "type",
    "gameCode" : "code"
});
#### 7>创建服务类:VipRecordRepository 
#### 8>编写测试类:MongoTemplateTest

    测试说明:
    queryParam转换成mongodb的查询语句:
        db.vip_record.aggregate([
            { 
                $match: { 
            //           id: {$gt:34},
                    queryType: "type",queryCode: "code",
                    queryStartTime: { $gte: new ISODate("2020-07-09"), $lt: new ISODate("2021-08-08") } 
                }
            },
            {     
                $group: { 
                    _id: {
                        queryStartTime: {
                            $dateToString: { format: "%Y-%m-%d", date: "$queryStartTime" }
                        },
                        queryAccount:"$queryAccount",
                        queryType:"$queryType",
                        queryCode:"$queryCode"
                    },
                    totalTaobao:{$sum: "$taobao"},
                    totalVipAmount:{$sum: "$vipAmount"},
                    'count':{'$sum':NumberInt(1)}
                }
            },
            {
                    $project: {
                        queryAccount: true,
                        queryType: true,
                        queryCode: true,
                        count:true,
                        total:true,
                        totalTaobao:true,
                        totalVipAmount:true
                    }
                },
                {$sort: {_id: -1}},
                {$skip:Number(0)},
                {$limit:Number(10)} 
        ]);

    queryParam转换成mongodb的查询语句:
        db.vip_record.aggregate([
            { 
                $match: {
                    queryType: "type",queryCode: "code",
                    queryStartTime: { $gte: new ISODate('2020-07-09'), $lt: new ISODate('2020-08-08') } 
                }
            },
            {     
                $group: { 
                    _id: {
                        queryStartTime: {
                            $dateToString: { format: "%Y-%m-%d", date: "$queryStartTime" }
                        },
                        queryAccount:"$queryAccount",
                        queryType:"$queryType",
                        queryCode:"$queryCode",
                    },
                    totalTaobao:{$sum: "$taobao"},
                    totalVipAmount:{$sum: "$vipAmount"},
                    'count':{'$sum':NumberInt(1)}
                }
            },
            {
                $group: {
                _id: null,
                count: { $sum: NumberInt(1)}
            }
            }
        ]);
